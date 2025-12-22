package model;

import java.math.BigDecimal;

public class SalesOrderDetail {
    private String orderID;
    private String productID;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal discountAmount;
    private BigDecimal vatAmount;
    private BigDecimal lineTotal;

    // Constructor đầy đủ tham số
    public SalesOrderDetail(String orderID, String productID, int quantity, BigDecimal unitPrice,
                            BigDecimal discountAmount, BigDecimal vatAmount, BigDecimal lineTotal) {
        this.orderID = orderID;
        this.productID = productID;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.discountAmount = discountAmount;
        this.vatAmount = vatAmount;
        this.lineTotal = lineTotal;
    }

    // --- CÁC HÀM LẤY DỮ LIỆU (GETTER) ---
    public String getOrderID() { return orderID; }
    public String getProductID() { return productID; }
    public int getQuantity() { return quantity; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public BigDecimal getDiscountAmount() { return discountAmount; }
    public BigDecimal getVatAmount() { return vatAmount; }
    public BigDecimal getLineTotal() { return lineTotal; }

    // --- CÁC HÀM GÁN DỮ LIỆU (SETTER) ---
    public void setOrderID(String orderID) { this.orderID = orderID; }
    public void setProductID(String productID) { this.productID = productID; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    public void setDiscountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; }
    public void setVatAmount(BigDecimal vatAmount) { this.vatAmount = vatAmount; }
    public void setLineTotal(BigDecimal lineTotal) { this.lineTotal = lineTotal; }
}