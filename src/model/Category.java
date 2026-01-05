package model;

public class Category {
    private int categoryID;
    private String categoryName;
    private String description;
    public Category(int categoryID, String categoryName, String description){
        this.categoryID = categoryID;
        this.categoryName = categoryName;
        this.description = description;
    }
    public int getCategoryID(){
        return categoryID;
    }
    public String getCategoryName(){
        return categoryName;
    }
    public String getDescription(){
        return description;
    }
    public void setCategoryID(int categoryID){
        this.categoryID = categoryID;
    }
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
