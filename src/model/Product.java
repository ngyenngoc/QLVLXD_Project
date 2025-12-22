package model;
import java.math.BigDecimal;
import java.time.LocalDateTime;
public class Product {
    private String productID;
    private String supplierID;
    private String productName;
    private String productCategory;
    private String description;
    private String unit;
    private BigDecimal purchasePrice;
    private BigDecimal sellingPrice;
    private int stockQuantity;
    private String status;
    private LocalDateTime lastStockUpdate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String adminID;
    public Product(String productID, String supplierID, String productName, String productCategory, String description, String unit, BigDecimal purchasePrice, BigDecimal sellingPrice, int stockQuantity, String status, LocalDateTime lastStockUpdate, LocalDateTime createdAt,  LocalDateTime updatedAt,  String adminID   ){
        this.productID = productID;
        this.supplierID = supplierID;
        this.productName = productName;
        this.productCategory = productCategory;
        this.description = description;
        this.unit = unit;
        this.purchasePrice = purchasePrice;
        this.sellingPrice = sellingPrice;
        this.stockQuantity = stockQuantity;
        this.status = status;
        this.lastStockUpdate = lastStockUpdate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.adminID = adminID;
    }
    public String getProductID() {
        return productID;
    }
    public String getSupplierID() {
        return supplierID;
    }
    public String getProductName() {
        return productName;
    }
    public String getProductCategory() {
        return productCategory;
    }
    public String getDescription() {
        return description;
    }
    public String getUnit() {
        return unit;
    }
    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }
    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }
    public int getStockQuantity() {
        return stockQuantity;
    }
    public String getStatus() {
        return status;
    }
    public LocalDateTime getLastStockUpdate() {
        return lastStockUpdate;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public String getAdminID() {
        return adminID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }
    public void setSupplierID(String supplierID) {
        this.supplierID = supplierID;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }
    public void setPurchasePric(BigDecimal purchasePric) {
        this.purchasePrice = purchasePrice;
    }
    public void setSellingPrice(BigDecimal sellingPrice) {
        this.sellingPrice = sellingPrice;
    }
    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public void setLastStockUpdate(LocalDateTime lastStockUpdate) {
        this.lastStockUpdate = lastStockUpdate;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    public void setAdminID(String adminID) {
        this.adminID = adminID;
    }
}
