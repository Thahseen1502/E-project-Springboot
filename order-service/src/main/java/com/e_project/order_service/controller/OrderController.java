//package com.e_project.order_service.controller;
//
//import com.e_project.order_service.dto.OrderRequestDTO;
//import com.e_project.order_service.dto.OrderResponseDTO;
//import com.e_project.order_service.service.OrderService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Optional;
//
//@CrossOrigin(origins = "*", allowedHeaders = "*")
//@RestController
//@RequestMapping("/api/orders")
//
//public class OrderController {
//
//    @Autowired
//    private OrderService orderService;
//
//    @GetMapping
//    public List<OrderResponseDTO> getAllOrders() {
//        return orderService.getAllOrders();
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable Long id) {
//        Optional<OrderResponseDTO> responseDTO = orderService.getOrderById(id);
//        return responseDTO.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
//    }
//
//    @PostMapping
//    public ResponseEntity<OrderResponseDTO> createOrder(@RequestBody OrderRequestDTO requestDTO) {
//        OrderResponseDTO createdOrder = orderService.createOrder(requestDTO);
//        return ResponseEntity.ok(createdOrder);
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<OrderResponseDTO> updateOrder(@PathVariable Long id, @RequestBody OrderRequestDTO requestDTO) {
//        Optional<OrderResponseDTO> updated = orderService.updateOrder(id, requestDTO);
//        return updated.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
//        orderService.deleteOrder(id);
//        return ResponseEntity.noContent().build();
//    }
//}

package com.e_project.order_service.controller;

import com.e_project.order_service.dto.OrderRequestDTO;
import com.e_project.order_service.dto.OrderResponseDTO;
import com.e_project.order_service.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
        try {
            List<OrderResponseDTO> orders = orderService.getAllOrders();
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable Long id) {
        try {
            Optional<OrderResponseDTO> responseDTO = orderService.getOrderById(id);
            return responseDTO.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderRequestDTO requestDTO) {
        try {
            // Validate input
            if (requestDTO.getUserId() == null || requestDTO.getProductId() == null || requestDTO.getQuantity() <= 0) {
                return ResponseEntity.badRequest().body("Invalid request data");
            }

            OrderResponseDTO createdOrder = orderService.createOrder(requestDTO);
            return ResponseEntity.ok(createdOrder);
        } catch (RuntimeException e) {
            // Return the specific error message from the service
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal server error: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(@PathVariable Long id, @RequestBody OrderRequestDTO requestDTO) {
        try {
            // Validate input
            if (requestDTO.getUserId() == null || requestDTO.getProductId() == null || requestDTO.getQuantity() <= 0) {
                return ResponseEntity.badRequest().body("Invalid request data");
            }

            Optional<OrderResponseDTO> updated = orderService.updateOrder(id, requestDTO);
            return updated.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal server error: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id) {
        try {
            orderService.deleteOrder(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting order: " + e.getMessage());
        }
    }
}