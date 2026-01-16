package model;

public class Supplier {
    private String supplierID;
    private String supplierName;
    private String address;
    private String phoneNumber;
    private String email;

    public Supplier(String supplierID, String supplierName, String address, String phoneNumber, String email) {
        this.supplierID = supplierID;
        this.supplierName = supplierName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }
    @Override
    public String toString() {
        return this.supplierName; // Trả về tên để hiển thị lên ComboBox
    }
    public String getSupplierID() {
        return supplierID;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setSupplierID(String supplierID) {
        this.supplierID = supplierID;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

