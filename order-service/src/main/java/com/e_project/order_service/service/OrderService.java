//package com.e_project.order_service.service;
//
//import com.e_project.order_service.dto.OrderRequestDTO;
//import com.e_project.order_service.dto.OrderResponseDTO;
//import com.e_project.order_service.model.Order;
//import com.e_project.order_service.repository.OrderRepository;
//import com.e_project.product_service.grpc.ProductProto;
//import com.e_project.product_service.grpc.ProductServiceGrpc;
//import com.e_project.user_service.grpc.UserProto;
//import com.e_project.user_service.grpc.UserServiceGrpc;
//import org.springframework.stereotype.Service;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//@Service
//public class OrderService {
//
//    private static final Logger log = LoggerFactory.getLogger(OrderService.class);
//
//    private final OrderRepository orderRepository;
//    private final UserServiceGrpc.UserServiceBlockingStub userServiceStub;
//    private final ProductServiceGrpc.ProductServiceBlockingStub productServiceStub;
//
//    // Constructor injection
//    public OrderService(OrderRepository orderRepository,
//                        UserServiceGrpc.UserServiceBlockingStub userServiceStub,
//                        ProductServiceGrpc.ProductServiceBlockingStub productServiceStub) {
//        this.orderRepository = orderRepository;
//        this.userServiceStub = userServiceStub;
//        this.productServiceStub = productServiceStub;
//
//        // Debug logs to verify injection
//        log.info("OrderService initialized");
//        log.info("userServiceStub is null: {}", (userServiceStub == null));
//        log.info("productServiceStub is null: {}", (productServiceStub == null));
//
//    }
//
//    public List<OrderResponseDTO> getAllOrders() {
//        return orderRepository.findAll().stream()
//                .map(this::mapToResponseDTO)
//                .collect(Collectors.toList());
//    }
//
//    public Optional<OrderResponseDTO> getOrderById(Long id) {
//        return orderRepository.findById(id).map(this::mapToResponseDTO);
//    }
//
//    public List<OrderResponseDTO> getOrdersByUserId(Long userId) {
//        List<Order> orders = orderRepository.findByUserId(userId);
//        return orders.stream()
//                .map(this::mapToResponseDTO)
//                .collect(Collectors.toList());
//    }
//
//    public OrderResponseDTO createOrder(OrderRequestDTO requestDTO) {
//        log.info("Creating order for userId: {}, productId: {}, quantity: {}",
//                requestDTO.getUserId(), requestDTO.getProductId(), requestDTO.getQuantity());
//
//        // Validate input
//        if (requestDTO.getUserId() == null || requestDTO.getProductId() == null || requestDTO.getQuantity() <= 0) {
//            throw new IllegalArgumentException("Invalid order data: userId, productId are required and quantity must be positive");
//        }
//
//        // Validate User exists
//        try {
//            UserProto.GetUserByIdRequest userRequest = UserProto.GetUserByIdRequest.newBuilder()
//                    .setId(requestDTO.getUserId())
//                    .build();
//            UserProto.UserResponse userResponse = userServiceStub.getUserById(userRequest);
//            if (!userResponse.getSuccess()) {
//                throw new RuntimeException("User not found with ID: " + requestDTO.getUserId());
//            }
//            log.info("User validation successful for ID: {}", requestDTO.getUserId());
//        } catch (Exception e) {
//            log.error("User validation failed: {}", e.getMessage());
//            throw new RuntimeException("User validation failed: " + e.getMessage());
//        }
//
//        // Validate Product exists and get price
//        double totalPrice;
//        try {
//            ProductProto.GetProductByIdRequest productRequest = ProductProto.GetProductByIdRequest.newBuilder()
//                    .setId(requestDTO.getProductId())
//                    .build();
//
//            ProductProto.ProductResponse productResponse = productServiceStub.getProductById(productRequest);
//            if (!productResponse.getSuccess()) {
//                throw new RuntimeException("Product not found with ID: " + requestDTO.getProductId());
//            }
//
//            ProductProto.Product product = productResponse.getProduct();
//
//            if (product.getStock() < requestDTO.getQuantity()) {
//                throw new RuntimeException("Insufficient stock. Available: " + product.getStock() +
//                        ", Requested: " + requestDTO.getQuantity());
//            }
//
//            totalPrice = product.getPrice() * requestDTO.getQuantity();
//            log.info("Product validation successful. Price: {}, Total: {}", product.getPrice(), totalPrice);
//        } catch (Exception e) {
//            log.error("Product validation failed: {}", e.getMessage());
//            throw new RuntimeException("Product validation failed: " + e.getMessage());
//        }
//
//        // Create and save order
//        Order order = new Order();
//        order.setUserId(requestDTO.getUserId());
//        order.setProductId(requestDTO.getProductId());
//        order.setQuantity(requestDTO.getQuantity());
//        order.setTotalPrice(totalPrice);
//        order.setStatus("PLACED");
//        order.setOrderDate(LocalDateTime.now());
//
//        Order savedOrder = orderRepository.save(order);
//        log.info("Order created successfully with ID: {}", savedOrder.getId());
//        return mapToResponseDTO(savedOrder);
//    }
//
//    public Optional<OrderResponseDTO> updateOrder(Long id, OrderRequestDTO requestDTO) {
//        log.info("Updating order ID: {}", id);
//        return orderRepository.findById(id).map(existingOrder -> {
//            try {
//                // Validate input
//                if (requestDTO.getUserId() == null || requestDTO.getProductId() == null || requestDTO.getQuantity() <= 0) {
//                    throw new IllegalArgumentException("Invalid order data: userId, productId are required and quantity must be positive");
//                }
//
//                // Validate User
//                UserProto.GetUserByIdRequest userRequest = UserProto.GetUserByIdRequest.newBuilder()
//                        .setId(requestDTO.getUserId())
//                        .build();
//                UserProto.UserResponse userResponse = userServiceStub.getUserById(userRequest);
//                if (!userResponse.getSuccess()) {
//                    throw new RuntimeException("User not found with ID: " + requestDTO.getUserId());
//                }
//
//                // Validate Product and get price
//                ProductProto.GetProductByIdRequest productRequest = ProductProto.GetProductByIdRequest.newBuilder()
//                        .setId(requestDTO.getProductId())
//                        .build();
//                ProductProto.ProductResponse productResponse = productServiceStub.getProductById(productRequest);
//                if (!productResponse.getSuccess()) {
//                    throw new RuntimeException("Product not found with ID: " + requestDTO.getProductId());
//                }
//                ProductProto.Product product = productResponse.getProduct();
//                if (product.getStock() < requestDTO.getQuantity()) {
//                    throw new RuntimeException("Insufficient stock. Available: " + product.getStock() +
//                            ", Requested: " + requestDTO.getQuantity());
//                }
//                double totalPrice = product.getPrice() * requestDTO.getQuantity();
//
//                // Update order fields
//                existingOrder.setProductId(requestDTO.getProductId());
//                existingOrder.setUserId(requestDTO.getUserId());
//                existingOrder.setQuantity(requestDTO.getQuantity());
//                existingOrder.setTotalPrice(totalPrice);
//                existingOrder.setStatus("UPDATED");
//
//                Order savedOrder = orderRepository.save(existingOrder);
//                log.info("Order updated successfully: {}", savedOrder.getId());
//                return mapToResponseDTO(savedOrder);
//            } catch (Exception e) {
//                log.error("Error during order update: {}", e.getMessage());
//                throw e;
//            }
//        });
//    }
//
//    public void deleteOrder(Long id) {
//        log.info("Deleting order ID: {}", id);
//        if (!orderRepository.existsById(id)) {
//            throw new RuntimeException("Order not found with id: " + id);
//        }
//        orderRepository.deleteById(id);
//        log.info("Order deleted successfully: {}", id);
//    }
//
//    private OrderResponseDTO mapToResponseDTO(Order order) {
//        OrderResponseDTO dto = new OrderResponseDTO();
//        dto.setId(order.getId());
//        dto.setUserId(order.getUserId());
//        dto.setProductId(order.getProductId());
//        dto.setQuantity(order.getQuantity());
//        dto.setTotalPrice(order.getTotalPrice());
//        dto.setStatus(order.getStatus());
//        dto.setOrderDate(order.getOrderDate());
//        return dto;
//    }
//}

package com.e_project.order_service.service;

import com.e_project.order_service.dto.OrderRequestDTO;
import com.e_project.order_service.dto.OrderResponseDTO;
import com.e_project.order_service.model.Order;
import com.e_project.order_service.repository.OrderRepository;
import com.e_project.product_service.grpc.ProductProto;
import com.e_project.product_service.grpc.ProductServiceGrpc;
import com.e_project.user_service.grpc.UserProto;
import com.e_project.user_service.grpc.UserServiceGrpc;
import io.grpc.StatusRuntimeException; // Import for gRPC specific exceptions
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    // These stubs will now be injected by Spring thanks to GrpcClientConfig
    private final UserServiceGrpc.UserServiceBlockingStub userServiceStub;
    private final ProductServiceGrpc.ProductServiceBlockingStub productServiceStub;

    // Constructor injection: Spring will now provide these beans
    public OrderService(OrderRepository orderRepository,
                        UserServiceGrpc.UserServiceBlockingStub userServiceStub,
                        ProductServiceGrpc.ProductServiceBlockingStub productServiceStub) {
        this.orderRepository = orderRepository;
        this.userServiceStub = userServiceStub;
        this.productServiceStub = productServiceStub;

        // Debug logs to verify injection
        log.info("OrderService initialized");
        // These should now log 'false' if injection is successful
        log.info("userServiceStub is null: {}", (userServiceStub == null));
        log.info("productServiceStub is null: {}", (productServiceStub == null));
    }

    public List<OrderResponseDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<OrderResponseDTO> getOrderById(Long id) {
        return orderRepository.findById(id).map(this::mapToResponseDTO);
    }

    public List<OrderResponseDTO> getOrdersByUserId(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public OrderResponseDTO createOrder(OrderRequestDTO requestDTO) {
        log.info("Attempting to create order for userId: {}, productId: {}, quantity: {}",
                requestDTO.getUserId(), requestDTO.getProductId(), requestDTO.getQuantity());

        // Validate input for nulls or invalid values
        if (requestDTO.getUserId() == null || requestDTO.getProductId() == null || requestDTO.getQuantity() <= 0) {
            log.warn("Invalid order data received: userId, productId are required and quantity must be positive. Request: {}", requestDTO);
            throw new IllegalArgumentException("Invalid order data: userId, productId are required and quantity must be positive");
        }

        // Validate User existence using gRPC
        try {
            log.debug("Validating user with ID: {}", requestDTO.getUserId());
            UserProto.GetUserByIdRequest userRequest = UserProto.GetUserByIdRequest.newBuilder()
                    .setId(requestDTO.getUserId())
                    .build();
            UserProto.UserResponse userResponse = userServiceStub.getUserById(userRequest);

            if (!userResponse.getSuccess()) {
                log.error("User validation failed for ID {}: {}", requestDTO.getUserId(), userResponse.getMessage());
                throw new RuntimeException("User not found: " + userResponse.getMessage());
            }
            log.info("User with ID {} validated successfully.", requestDTO.getUserId());

        } catch (StatusRuntimeException e) {
            log.error("gRPC error calling user service for ID {}: Status: {}, Message: {}",
                    requestDTO.getUserId(), e.getStatus(), e.getMessage(), e);
            throw new RuntimeException("Failed to communicate with user service: " + e.getStatus().getDescription());
        } catch (Exception e) {
            log.error("Unexpected error during user validation for ID {}: {}", requestDTO.getUserId(), e.getMessage(), e);
            throw new RuntimeException("User validation failed due to an unexpected error: " + e.getMessage());
        }

        // Validate Product existence and retrieve price using gRPC
        double totalPrice;
        try {
            log.debug("Validating product with ID: {}", requestDTO.getProductId());
            ProductProto.GetProductByIdRequest productRequest = ProductProto.GetProductByIdRequest.newBuilder()
                    .setId(requestDTO.getProductId())
                    .build();

            ProductProto.ProductResponse productResponse = productServiceStub.getProductById(productRequest);

            if (!productResponse.getSuccess()) {
                log.error("Product validation failed for ID {}: {}", requestDTO.getProductId(), productResponse.getMessage());
                throw new RuntimeException("Product not found: " + productResponse.getMessage());
            }

            ProductProto.Product product = productResponse.getProduct();

            // Check for sufficient stock
            if (product.getStock() < requestDTO.getQuantity()) {
                log.warn("Insufficient stock for product ID {}. Available: {}, Requested: {}",
                        requestDTO.getProductId(), product.getStock(), requestDTO.getQuantity());
                throw new RuntimeException("Insufficient stock. Available: " + product.getStock() +
                        ", Requested: " + requestDTO.getQuantity());
            }

            totalPrice = product.getPrice() * requestDTO.getQuantity();
            log.info("Product with ID {} validated successfully. Unit Price: {}, Calculated Total Price: {}",
                    requestDTO.getProductId(), product.getPrice(), totalPrice);

        } catch (StatusRuntimeException e) {
            log.error("gRPC error calling product service for ID {}: Status: {}, Message: {}",
                    requestDTO.getProductId(), e.getStatus(), e.getMessage(), e);
            throw new RuntimeException("Failed to communicate with product service: " + e.getStatus().getDescription());
        } catch (Exception e) {
            log.error("Unexpected error during product validation for ID {}: {}", requestDTO.getProductId(), e.getMessage(), e);
            throw new RuntimeException("Product validation failed due to an unexpected error: " + e.getMessage());
        }

        // Create and save order
        Order order = new Order();
        order.setUserId(requestDTO.getUserId());
        order.setProductId(requestDTO.getProductId());
        order.setQuantity(requestDTO.getQuantity());
        order.setTotalPrice(totalPrice);
        order.setStatus("PLACED");
        order.setOrderDate(LocalDateTime.now());

        try {
            Order savedOrder = orderRepository.save(order);
            log.info("Order successfully created with ID: {}", savedOrder.getId());
            return mapToResponseDTO(savedOrder);
        } catch (Exception e) {
            log.error("Error saving order to database: {}", e.getMessage(), e);
            throw new RuntimeException("Error saving order: " + e.getMessage());
        }
    }

    public Optional<OrderResponseDTO> updateOrder(Long id, OrderRequestDTO requestDTO) {
        log.info("Attempting to update order ID: {}", id);
        return orderRepository.findById(id).map(existingOrder -> {
            try {
                // Validate input
                if (requestDTO.getUserId() == null || requestDTO.getProductId() == null || requestDTO.getQuantity() <= 0) {
                    log.warn("Invalid order data for update: userId, productId are required and quantity must be positive. Request: {}", requestDTO);
                    throw new IllegalArgumentException("Invalid order data: userId, productId are required and quantity must be positive");
                }

                // Validate User
                try {
                    log.debug("Validating user for order update with ID: {}", requestDTO.getUserId());
                    UserProto.GetUserByIdRequest userRequest = UserProto.GetUserByIdRequest.newBuilder()
                            .setId(requestDTO.getUserId())
                            .build();
                    UserProto.UserResponse userResponse = userServiceStub.getUserById(userRequest);
                    if (!userResponse.getSuccess()) {
                        log.error("User validation failed during update for ID {}: {}", requestDTO.getUserId(), userResponse.getMessage());
                        throw new RuntimeException("User not found: " + userResponse.getMessage());
                    }
                    log.debug("User with ID {} validated successfully for update.", requestDTO.getUserId());
                } catch (StatusRuntimeException e) {
                    log.error("gRPC error calling user service during update for ID {}: Status: {}, Message: {}",
                            requestDTO.getUserId(), e.getStatus(), e.getMessage(), e);
                    throw new RuntimeException("Failed to communicate with user service during update: " + e.getStatus().getDescription());
                }

                // Validate Product and get price
                double totalPrice;
                try {
                    log.debug("Validating product for order update with ID: {}", requestDTO.getProductId());
                    ProductProto.GetProductByIdRequest productRequest = ProductProto.GetProductByIdRequest.newBuilder()
                            .setId(requestDTO.getProductId())
                            .build();
                    ProductProto.ProductResponse productResponse = productServiceStub.getProductById(productRequest);
                    if (!productResponse.getSuccess()) {
                        log.error("Product validation failed during update for ID {}: {}", requestDTO.getProductId(), productResponse.getMessage());
                        throw new RuntimeException("Product not found: " + productResponse.getMessage());
                    }
                    ProductProto.Product product = productResponse.getProduct();
                    if (product.getStock() < requestDTO.getQuantity()) {
                        log.warn("Insufficient stock for product ID {} during update. Available: {}, Requested: {}",
                                requestDTO.getProductId(), product.getStock(), requestDTO.getQuantity());
                        throw new RuntimeException("Insufficient stock. Available: " + product.getStock() +
                                ", Requested: " + requestDTO.getQuantity());
                    }
                    totalPrice = product.getPrice() * requestDTO.getQuantity();
                    log.debug("Product with ID {} validated successfully for update. Calculated Total Price: {}", requestDTO.getProductId(), totalPrice);
                } catch (StatusRuntimeException e) {
                    log.error("gRPC error calling product service during update for ID {}: Status: {}, Message: {}",
                            requestDTO.getProductId(), e.getStatus(), e.getMessage(), e);
                    throw new RuntimeException("Failed to communicate with product service during update: " + e.getStatus().getDescription());
                }

                // Update order fields
                existingOrder.setProductId(requestDTO.getProductId());
                existingOrder.setUserId(requestDTO.getUserId());
                existingOrder.setQuantity(requestDTO.getQuantity());
                existingOrder.setTotalPrice(totalPrice);
                existingOrder.setStatus("UPDATED");

                Order savedOrder = orderRepository.save(existingOrder);
                log.info("Order ID {} updated successfully.", savedOrder.getId());
                return mapToResponseDTO(savedOrder);
            } catch (Exception e) {
                log.error("Error updating order ID {}: {}", id, e.getMessage(), e);
                // Re-throw the exception so the controller can handle it appropriately
                throw e;
            }
        });
    }

    public void deleteOrder(Long id) {
        log.info("Attempting to delete order ID: {}", id);
        if (!orderRepository.existsById(id)) {
            log.warn("Order with ID {} not found for deletion.", id);
            throw new RuntimeException("Order not found with id: " + id);
        }
        orderRepository.deleteById(id);
        log.info("Order ID {} deleted successfully.", id);
    }

    private OrderResponseDTO mapToResponseDTO(Order order) {
        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setId(order.getId());
        dto.setUserId(order.getUserId());
        dto.setProductId(order.getProductId());
        dto.setQuantity(order.getQuantity());
        dto.setTotalPrice(order.getTotalPrice());
        dto.setStatus(order.getStatus());
        dto.setOrderDate(order.getOrderDate());
        return dto;
    }
}
