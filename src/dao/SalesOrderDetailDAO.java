package dao;

import model.SalesOrderDetail;
import java.sql.*;
import java.util.List;

public class SalesOrderDetailDAO {

    // Phương thức lưu danh sách chi tiết (Dùng chung Connection của SalesOrderDAO để chạy Transaction)
    public void insertDetails(Connection conn, String orderID, List<SalesOrderDetail> details) throws SQLException {
        String sql = "INSERT INTO SalesOrderDetail (orderID, materialID, quantity, salePrice) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (SalesOrderDetail item : details) {
                ps.setString(1, orderID);
                ps.setString(2, item.getMaterialID());
                ps.setInt(3, item.getQuantity());
                ps.setBigDecimal(4, item.getSalePrice());
                ps.addBatch(); // Gom lại để thực thi một lần cho nhanh
            }
            ps.executeBatch();
        }
    }
}