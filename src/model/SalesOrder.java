package model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SalesOrder {
    private String orderID;
    private LocalDate orderDate;
    private String customerID;
    private String customerName;   // dùng để JOIN hiển thị
    private int userID;
    private String notes;
    private BigDecimal totalAmount;
    private String materialID;
    private String materialName;
    private int quantity;
    private BigDecimal salePrice;

    public SalesOrder(String orderID, LocalDate orderDate, String customerID, String customerName, int userID, String notes, BigDecimal totalAmount) {
        this.orderID = orderID;
        this.orderDate = orderDate;
        this.customerID = customerID;
        this.customerName = customerName;
        this.userID = userID;
        this.notes = notes;
        this.totalAmount = totalAmount;
    }

    public String getOrderID() {
        return orderID;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public String getOrderDateStr() {
        return orderDate != null
                ? orderDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                : "";
    }

    public String getCustomerID() {
        return customerID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public int getUserID() {
        return userID;
    }


    public String getNotes() {
        return notes;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }


    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getMaterialID() { return materialID; }
    public void setMaterialID(String materialID) { this.materialID = materialID; }
    public String getMaterialName() { return materialName; }
    public void setMaterialName(String materialName) { this.materialName = materialName; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public BigDecimal getSalePrice() { return salePrice; }
    public void setSalePrice(BigDecimal salePrice) { this.salePrice = salePrice; }
}
