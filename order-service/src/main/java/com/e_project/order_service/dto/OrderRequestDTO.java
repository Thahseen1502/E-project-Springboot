//package com.e_project.order_service.dto;
//
//public class OrderRequestDTO {
//    private Long userId;
//    private Long productId;
//    private int quantity;
//
//    public Long getUserId() { return userId; }
//    public void setUserId(Long userId) { this.userId = userId; }
//
//    public Long getProductId() { return productId; }
//    public void setProductId(Long productId) { this.productId = productId; }
//
//    public int getQuantity() { return quantity; }
//    public void setQuantity(int quantity) { this.quantity = quantity; }
//}
//

package com.e_project.order_service.dto;

public class OrderRequestDTO {
    private Long userId;
    private Long productId;
    private Integer quantity;

    // Getters and Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}