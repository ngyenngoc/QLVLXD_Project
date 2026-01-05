package dao;

import model.SalesOrder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SalesOrderDAO {
    private final List<SalesOrder> mockDataList;

    public SalesOrderDAO() {
        mockDataList = new ArrayList<>();
        mockDataList.add(new SalesOrder("ĐHB001", "KH001", "AD001", "Chuyển khoản", "Không có", new BigDecimal("16789"), new BigDecimal("1425526"), new BigDecimal("2%"), LocalDateTime.now(), LocalDateTime.now()));
        mockDataList.add(new SalesOrder("ĐHB002", "KH002", "AD002", "Chuyển khoản", "Không có", new BigDecimal("16743"), new BigDecimal("14256"), new BigDecimal("10%"), LocalDateTime.now(), LocalDateTime.now()));
        mockDataList.add(new SalesOrder("ĐHB001", "KH001", "AD003", "Tiền mặt", "Không có", new BigDecimal("42526"), new BigDecimal("53727"), new BigDecimal("11%"), LocalDateTime.now(), LocalDateTime.now()));
    }

    public List<SalesOrder> getAll() {
        return mockDataList;
    }

    public boolean insert(SalesOrder salesorder) {
        salesorder.setCreatedAt(LocalDateTime.now());
        salesorder.setUpdatedAt(LocalDateTime.now());
        mockDataList.add(salesorder);
        System.out.print("DAO: [Mock] Đã thêm đơn hàng bán " + salesorder.getOrderID());
        return true;
    }

    public boolean update(SalesOrder updateSalesOrder) { // duyệt qua danh sách để tìm đối tượng cũ
        for (int i = 0; i < mockDataList.size(); i++) {
            //  So sánh dựa trên Khóa Chính (OrderID)
            if (mockDataList.get(i).getOrderID().equals(updateSalesOrder.getOrderID())) {
                // Thay thế đối tượng cũ bằng đối tượng mới
                mockDataList.set(i, updateSalesOrder);
                System.out.println("DAO: [Mock] Đã cập nhật đơn hàng bán: " + updateSalesOrder.getOrderID());
                return true;
            }
        }
        return false;
    }

    public boolean delete(String orderID) {
        System.out.println("DAO: [Mock] Xóa đơn hàng bán " + orderID);
        return mockDataList.removeIf(product -> product.getOrderID().equals(orderID));// Xóa thực thể khỏi mockDataList để demo hoạt động
    }

    public String generateNewId() {
        int nextID = mockDataList.size() + 1;
        return String.format("ĐHB%03d", nextID);
    }
}
