package dao;

import model.Product;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    private final List<Product> mockDataList;
    public ProductDAO(){
       mockDataList = new ArrayList<>();
        mockDataList.add(new Product("SP001", "NCC001", "Gach men", "Vật liệu hoàn thiện", "Gạch 60*60", "Thùng", new BigDecimal("150000"), new BigDecimal("155000"),500, "Available", LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(), "AD001"));
        mockDataList.add(new Product("SP002", "NCC002", "Xi măng", "Vật liệu thô", "Hàng loại 1", "Bao", new BigDecimal("250000"), new BigDecimal("270000"),1700, "Available", LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(), "AD002"));
        mockDataList.add(new Product("SP003", "NCC003", "Tôn", "Vật liệu hoàn thiện", "Tôn Hoa Sen màu xanh", "mét", new BigDecimal("100000"), new BigDecimal("102000"),2800, "Available", LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(), "AD003"));
    }
    public List<Product> getAll(){
        return mockDataList;
    }
    public boolean insert(Product product){
        product.setLastStockUpdate(LocalDateTime.now());
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        product.setStatus("Waiting"); // thiết lập trạng thái mặc định kho thêm mockDataList
            mockDataList.add(product);
        System.out.println("DAO: [Mock] Product added " + product.getProductID());
        return true;
    }
    public boolean update(Product updateProduct) { // duyệt qua danh sách để tìm đối tượng cũ
        for (int i = 0; i < mockDataList.size(); i++) {
            //  So sánh dựa trên Khóa Chính (ProductID)
            if (mockDataList.get(i).getProductID().equals(updateProduct.getProductID())) {
                // Thay thế đối tượng cũ bằng đối tượng mới
                mockDataList.set(i, updateProduct);
                System.out.println("DAO: [Mock] Product updated: " + updateProduct.getProductID());
                return true;
            }
        }
        return false; // trả về false nếu không tìm thấy ID
    }
    public boolean delete(String productID) {
        System.out.println("DAO: [Mock] Product deleted" + productID);
        return mockDataList.removeIf(product->product.getProductID().equals(productID));// Xóa thực thể khỏi mockDataList để demo hoạt động
    }

    public String generateNewId(){
        int nextID = mockDataList.size()+1;
        return String.format("SP%03d", nextID);
    }
}

