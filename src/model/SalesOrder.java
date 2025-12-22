package model;
import java.math.BigDecimal;
import java.time.LocalDateTime;
public class SalesOrder {
    private String orderID;
    private String customerID;
    private String adminID;
    private String paymentMethod;
    private String notes;
    private BigDecimal totalAmount;
    private BigDecimal discountAmount;
    private BigDecimal vatAmount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    public SalesOrder(String orderID,String customerID,String adminID,String paymentMethod,String notes,BigDecimal totalAmount,BigDecimal discountAmount,BigDecimal vatAmount, LocalDateTime createAt,LocalDateTime updateAt){
        this.orderID = orderID;
        this.customerID = customerID;
        this.adminID = adminID;
        this.paymentMethod = paymentMethod;
        this.notes = notes;
        this.totalAmount = totalAmount;
        this.discountAmount = discountAmount;
        this.vatAmount = vatAmount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getOrderID() {
        return orderID;
    }

    public String getCustomerID() {
        return customerID;
    }

    public String getAdminID() {
        return adminID;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getNotes() {
        return notes;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public BigDecimal getVatAmount() {
        return vatAmount;
    }

    public LocalDateTime getCreateAt() {
        return createdAt;
    }

    public LocalDateTime getUpdateAt() {
        return updatedAt;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public void setAdminID(String adminID) {
        this.adminID = adminID;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public void setVatAmount(BigDecimal vatAmount) {
        this.vatAmount = vatAmount;
    }

    public void setCreatedAt(LocalDateTime createAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updateAt) {
        this.updatedAt = updatedAt;
    }
}
