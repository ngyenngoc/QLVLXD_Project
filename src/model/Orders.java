package model;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Orders {
    private int orderID;
    private LocalDateTime orderDate;
    private String orderType;
    private String supplierID;
    private String customerID;
    private BigDecimal totalAmount;
    private String orderStatus;
    private String notes;
    private int userID;
    private String partnerName;

    public Orders(int orderID, LocalDateTime orderDate, String orderType, String supplierID, String customerID,
                  BigDecimal totalAmount, String orderStatus, String notes, int userID, String partnerName) {
        this.orderID = orderID;
        this.orderDate = orderDate;
        this.orderType = orderType;
        this.supplierID = supplierID;
        this.customerID = customerID;
        this.totalAmount = totalAmount;
        this.orderStatus = orderStatus;
        this.notes = notes;
        this.userID = userID;
        this.partnerName = partnerName;
    }

    public int getOrderID() {
        return orderID;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public String getOrderType(){
        return orderType;
    }
    public String getSupplierID() {
        return supplierID;
    }

    public String getCustomerID() {
        return customerID;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public String getNotes() {
        return notes;
    }
    public int getUserID(){
        return userID;
    }

    public String getPartnerName(){
        return partnerName;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }
    public void setOrderType(String orderType){
        this.orderType = orderType;
    }

    public void setSupplierID(String supplierID) {
        this.supplierID = supplierID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
    public void setUserID(int userID) {
        this.userID = userID;
    }
    public void setPartnerName(String partnerName){
        this.partnerName = partnerName;
    }
}
