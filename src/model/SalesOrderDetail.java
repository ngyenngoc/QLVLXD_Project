package model;

import java.math.BigDecimal;

public class SalesOrderDetail {

    private int detailID;
    private String orderID;
    private String materialID;
    private String materialName;
    private int quantity;
    private BigDecimal salePrice;

    public SalesOrderDetail(int detailID, String orderID, String materialID, String materialName, int quantity, BigDecimal salePrice) {
        this.detailID = detailID;
        this.orderID = orderID;
        this.materialID = materialID;
        this.materialName = materialName;
        this.quantity = quantity;
        this.salePrice = salePrice;
    }
    public int getDetailID() { return detailID; }
    public String getOrderID() { return orderID; }
    public String getMaterialID() { return materialID; }
    public String getMaterialName() { return materialName; }
    public int getQuantity() { return quantity; }
    public BigDecimal getSalePrice() { return salePrice; }
    public BigDecimal getSubTotal() {
        if (salePrice == null) return BigDecimal.ZERO;
        return salePrice.multiply(BigDecimal.valueOf(quantity));
    }

    public void setDetailID(int detailID) { this.detailID = detailID; }
    public void setOrderID(String orderID) { this.orderID = orderID; }
    public void setMaterialID(String materialID) { this.materialID = materialID; }
    public void setMaterialName(String materialName) { this.materialName = materialName; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setSalePrice(BigDecimal salePrice) { this.salePrice = salePrice; }
}
