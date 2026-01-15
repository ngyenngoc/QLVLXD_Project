package model;
import java.math.BigDecimal;
import java.time.LocalDateTime;
public class Material {
    private String materialID;
    private String materialName;
    private String unit;
    private BigDecimal purchasePrice;
    private BigDecimal salePrice;
    private int stockQuantity;
    private String description;
    private int categoryID;
    private String categoryName;

    public Material(String materialID, String materialName, String unit, BigDecimal purchasePrice, BigDecimal salePrice,int stockQuantity, String description, int categoryID, String categoryName) {
        this.materialID = materialID;
        this.materialName = materialName;
        this.unit = unit;
        this.purchasePrice = purchasePrice;
        this.salePrice = salePrice;
        this.stockQuantity = stockQuantity;
        this.description = description;
        this.categoryID = categoryID;
        this.categoryName = categoryName;
    }
    public String getMaterialID(){
        return materialID;
    }
    public String getMaterialName(){
        return materialName;
    }

    public String getUnit() {
        return unit;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public String getDescription() {
        return description;
    }

    public int getCategoryID() {
        return categoryID;
    }
    public String getCategoryName(){return categoryName;}

    public void setMaterialID(String materialID) {
        this.materialID = materialID;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }


    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }
    public void setCategoryName(String categoryName){this.categoryName = categoryName;}
}
