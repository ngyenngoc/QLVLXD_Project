package dao;
import model.Customer;

import java.util.ArrayList;
import java.util.List;
public class CustomerDAO {
    private final List<Customer> mockDataList;

    public CustomerDAO() {
        mockDataList = new ArrayList<>();
        mockDataList.add(new Customer("KH001", "Mai Liên", "Đăk Nông", "0867946662", "mailien@gmail.com"));
        mockDataList.add(new Customer("KH002", "Nhi Xuân", "Bắc Ninh", "0924678749", "nhixuann@gmail.com"));
        mockDataList.add(new Customer("KH003", "Kim Hoan", "Quảng Nam", "0679412678", "kimhmhoann@gmail.com"));
    }

    public List<Customer> getAll() {
        return mockDataList;
    }

    public boolean insert(Customer customer) {
        System.out.println("DAO: [Mock] Customer added" + customer.getCustomerName());
        mockDataList.add(customer);
        return true;
    }

    public boolean update(Customer kh) {
        System.out.println("DAO: [Mock] Customer updated" + kh.getCustomerID());
        return true;
    }

    public boolean delete(String customerID) {
        System.out.println("DAO: [Mock] Customer deleted" + customerID);
        return mockDataList.removeIf(customer->customer.getCustomerID().equals(customerID));// Xóa thực thể khỏi mockDataList để demo hoạt động
    }
    public String generateNewId(){
        int nextID = mockDataList.size()+1;
        return String.format("KH%03d", nextID);
    }
}
