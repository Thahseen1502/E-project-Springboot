package com.e_project.order_service.grpc.service;

import com.e_project.order_service.grpc.*;
import com.e_project.order_service.dto.OrderRequestDTO;
import com.e_project.order_service.dto.OrderResponseDTO;
import com.e_project.order_service.service.OrderService;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@GrpcService
public class OrderGrpcService extends OrderServiceGrpc.OrderServiceImplBase {

    @Autowired
    private OrderService orderService;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public void getAllOrders(OrderProto.GetAllOrdersRequest request,
                             StreamObserver<OrderProto.OrdersResponse> responseObserver) {
        try {
            List<OrderResponseDTO> orders = orderService.getAllOrders();

            OrderProto.OrdersResponse.Builder responseBuilder = OrderProto.OrdersResponse.newBuilder();

            for (OrderResponseDTO orderDTO : orders) {
                OrderProto.Order grpcOrder = convertToGrpcOrder(orderDTO);
                responseBuilder.addOrders(grpcOrder);
            }

            responseBuilder.setSuccess(true);
            responseBuilder.setMessage("Orders retrieved successfully");

            responseObserver.onNext(responseBuilder.build());
            responseObserver.onCompleted();

        } catch (Exception e) {
            OrderProto.OrdersResponse errorResponse = OrderProto.OrdersResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage("Error retrieving orders: " + e.getMessage())
                    .build();

            responseObserver.onNext(errorResponse);
            responseObserver.onCompleted();
        }
    }

    @Override
    public void getOrderById(OrderProto.GetOrderByIdRequest request,
                             StreamObserver<OrderProto.OrderResponse> responseObserver) {
        try {
            Optional<OrderResponseDTO> orderOptional = orderService.getOrderById(request.getId());

            OrderProto.OrderResponse.Builder responseBuilder = OrderProto.OrderResponse.newBuilder();

            if (orderOptional.isPresent()) {
                OrderProto.Order grpcOrder = convertToGrpcOrder(orderOptional.get());
                responseBuilder.setOrder(grpcOrder);
                responseBuilder.setSuccess(true);
                responseBuilder.setMessage("Order found successfully");
            } else {
                responseBuilder.setSuccess(false);
                responseBuilder.setMessage("Order not found with id: " + request.getId());
            }

            responseObserver.onNext(responseBuilder.build());
            responseObserver.onCompleted();

        } catch (Exception e) {
            OrderProto.OrderResponse errorResponse = OrderProto.OrderResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage("Error retrieving order: " + e.getMessage())
                    .build();

            responseObserver.onNext(errorResponse);
            responseObserver.onCompleted();
        }
    }


    @Override
    public void getOrdersByUserId(OrderProto.GetOrdersByUserIdRequest request,
                                  StreamObserver<OrderProto.OrdersResponse> responseObserver) {
        try {
            List<OrderResponseDTO> orders = orderService.getOrdersByUserId(request.getUserId());

            OrderProto.OrdersResponse.Builder responseBuilder = OrderProto.OrdersResponse.newBuilder();

            for (OrderResponseDTO orderDTO : orders) {
                OrderProto.Order grpcOrder = convertToGrpcOrder(orderDTO);
                responseBuilder.addOrders(grpcOrder);
            }

            responseBuilder.setSuccess(true);
            responseBuilder.setMessage("User orders retrieved successfully");

            responseObserver.onNext(responseBuilder.build());
            responseObserver.onCompleted();

        } catch (Exception e) {
            OrderProto.OrdersResponse errorResponse = OrderProto.OrdersResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage("Error retrieving user orders: " + e.getMessage())
                    .build();

            responseObserver.onNext(errorResponse);
            responseObserver.onCompleted();
        }
    }

    @Override
    public void createOrder(OrderProto.CreateOrderRequest request,
                            StreamObserver<OrderProto.OrderResponse> responseObserver) {
        try {
            OrderRequestDTO requestDTO = new OrderRequestDTO();
            requestDTO.setUserId(request.getUserId());
            requestDTO.setProductId(request.getProductId());
            requestDTO.setQuantity(request.getQuantity());

            OrderResponseDTO createdOrder = orderService.createOrder(requestDTO);

            OrderProto.Order grpcOrder = convertToGrpcOrder(createdOrder);

            OrderProto.OrderResponse response = OrderProto.OrderResponse.newBuilder()
                    .setOrder(grpcOrder)
                    .setSuccess(true)
                    .setMessage("Order created successfully")
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception e) {
            OrderProto.OrderResponse errorResponse = OrderProto.OrderResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage("Error creating order: " + e.getMessage())
                    .build();

            responseObserver.onNext(errorResponse);
            responseObserver.onCompleted();
        }
    }

    @Override
    public void updateOrder(OrderProto.UpdateOrderRequest request,
                            StreamObserver<OrderProto.OrderResponse> responseObserver) {
        try {
            OrderRequestDTO requestDTO = new OrderRequestDTO();
            requestDTO.setUserId(request.getUserId());
            requestDTO.setProductId(request.getProductId());
            requestDTO.setQuantity(request.getQuantity());

            Optional<OrderResponseDTO> updatedOrderOptional = orderService.updateOrder(request.getId(), requestDTO);

            OrderProto.OrderResponse.Builder responseBuilder = OrderProto.OrderResponse.newBuilder();

            if (updatedOrderOptional.isPresent()) {
                OrderProto.Order grpcOrder = convertToGrpcOrder(updatedOrderOptional.get());
                responseBuilder.setOrder(grpcOrder);
                responseBuilder.setSuccess(true);
                responseBuilder.setMessage("Order updated successfully");
            } else {
                responseBuilder.setSuccess(false);
                responseBuilder.setMessage("Order not found with id: " + request.getId());
            }

            responseObserver.onNext(responseBuilder.build());
            responseObserver.onCompleted();

        } catch (Exception e) {
            OrderProto.OrderResponse errorResponse = OrderProto.OrderResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage("Error updating order: " + e.getMessage())
                    .build();

            responseObserver.onNext(errorResponse);
            responseObserver.onCompleted();
        }
    }

    @Override
    public void deleteOrder(OrderProto.DeleteOrderRequest request,
                            StreamObserver<OrderProto.DeleteOrderResponse> responseObserver) {
        try {
            orderService.deleteOrder(request.getId());

            OrderProto.DeleteOrderResponse response = OrderProto.DeleteOrderResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("Order deleted successfully")
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception e) {
            OrderProto.DeleteOrderResponse errorResponse = OrderProto.DeleteOrderResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage("Error deleting order: " + e.getMessage())
                    .build();

            responseObserver.onNext(errorResponse);
            responseObserver.onCompleted();
        }
    }

    // Helper method to convert OrderResponseDTO to gRPC Order
    private OrderProto.Order convertToGrpcOrder(OrderResponseDTO orderDTO) {
        return OrderProto.Order.newBuilder()
                .setId(orderDTO.getId())
                .setUserId(orderDTO.getUserId())
                .setProductId(orderDTO.getProductId())
                .setQuantity(orderDTO.getQuantity())
                .setTotalPrice(orderDTO.getTotalPrice())
                .setStatus(orderDTO.getStatus())
                .setOrderDate(orderDTO.getOrderDate().format(DATE_FORMATTER))
                .build();
    }
}




