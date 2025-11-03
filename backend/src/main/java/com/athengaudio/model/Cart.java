package com.athengaudio.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "carts")
public class Cart {
    @Id
    private String id;
    
    private String userId; // Hoặc @DBRef nếu muốn reference
    
    private List<CartItem> items = new ArrayList<>();
    private BigDecimal totalAmount = BigDecimal.ZERO;
    
    // Constructors
    public Cart() {}
    
    public Cart(String userId) {
        this.userId = userId;
    }
    
    // Inner class for CartItem
    public static class CartItem {
        private String productId;
        private String productName;
        private BigDecimal price;
        private Integer quantity;
        private BigDecimal subTotal;
        
        // Constructors, Getters, Setters
        public CartItem() {}
        
        public CartItem(String productId, String productName, BigDecimal price, Integer quantity) {
            this.productId = productId;
            this.productName = productName;
            this.price = price;
            this.quantity = quantity;
            this.subTotal = price.multiply(BigDecimal.valueOf(quantity));
        }
        
        // Getters and Setters
        public String getProductId() { return productId; }
        public void setProductId(String productId) { this.productId = productId; }
        
        public String getProductName() { return productName; }
        public void setProductName(String productName) { this.productName = productName; }
        
        public BigDecimal getPrice() { return price; }
        public void setPrice(BigDecimal price) { this.price = price; }
        
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { 
            this.quantity = quantity;
            this.subTotal = this.price.multiply(BigDecimal.valueOf(quantity));
        }
        
        public BigDecimal getSubTotal() { return subTotal; }
        public void setSubTotal(BigDecimal subTotal) { this.subTotal = subTotal; }
    }
    
    // Methods to calculate total
    public void calculateTotal() {
        this.totalAmount = items.stream()
            .map(CartItem::getSubTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    public void addItem(CartItem item) {
        // Check if item already exists
        for (CartItem existingItem : items) {
            if (existingItem.getProductId().equals(item.getProductId())) {
                existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
                calculateTotal();
                return;
            }
        }
        items.add(item);
        calculateTotal();
    }
    
    public void removeItem(String productId) {
        items.removeIf(item -> item.getProductId().equals(productId));
        calculateTotal();
    }
    
    public void updateQuantity(String productId, Integer quantity) {
        for (CartItem item : items) {
            if (item.getProductId().equals(productId)) {
                item.setQuantity(quantity);
                calculateTotal();
                return;
            }
        }
    }
    
    public void clearCart() {
        items.clear();
        totalAmount = BigDecimal.ZERO;
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public List<CartItem> getItems() { return items; }
    public void setItems(List<CartItem> items) { 
        this.items = items; 
        calculateTotal();
    }
    
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
}