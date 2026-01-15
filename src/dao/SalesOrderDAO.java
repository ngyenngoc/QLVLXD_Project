package dao;

import model.SalesOrder;
import model.SalesOrderDetail;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SalesOrderDAO {
    private final SalesOrderDetailDAO detailDAO = new SalesOrderDetailDAO();

    public boolean insertFullOrder(SalesOrder order, List<SalesOrderDetail> details) {
        String sql = "INSERT INTO SalesOrder (orderID, orderDate, customerID, userID, notes, totalAmount) VALUES (?, ?, ?, ?, ?, ?)";

        Connection conn = DatabaseConnection.getConnection();
        try {
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, order.getOrderID());
                ps.setDate(2, java.sql.Date.valueOf(order.getOrderDate()));
                ps.setString(3, order.getCustomerID());
                ps.setInt(4, order.getUserID());
                ps.setString(5, order.getNotes());
                ps.setBigDecimal(6, order.getTotalAmount());
                ps.executeUpdate();
            }

            detailDAO.insertDetails(conn, order.getOrderID(), details);
            conn.commit();
            return true;
        } catch (SQLException e) {
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); } // Lỗi là hủy hết
            e.printStackTrace();
            return false;
        } finally {
            try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public List<SalesOrder> getAll() {
        List<SalesOrder> list = new ArrayList<>();
        // Truy vấn lấy đủ thông tin từ bảng cha và bảng con
        String sql = "SELECT o.*, c.customerName, d.materialID, m.materialName, d.quantity, d.salePrice " +
                "FROM SalesOrder o " +
                "JOIN Customer c ON o.customerID = c.customerID " +
                "JOIN SalesOrderDetail d ON o.orderID = d.orderID " +
                "JOIN Material m ON d.materialID = m.materialID";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                SalesOrder order = new SalesOrder(
                        rs.getString("orderID"),
                        rs.getDate("orderDate").toLocalDate(),
                        rs.getString("customerID"),
                        rs.getString("customerName"),
                        rs.getInt("userID"),
                        rs.getString("notes"),
                        rs.getBigDecimal("totalAmount")
                );
                // Gán dữ liệu bổ sung vào model để hiển thị lên bảng
                order.setMaterialID(rs.getString("materialID"));
                order.setMaterialName(rs.getString("materialName"));
                order.setQuantity(rs.getInt("quantity"));
                order.setSalePrice(rs.getBigDecimal("salePrice"));

                list.add(order);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public String generateNewID() {
        String sql = "SELECT MAX(CAST(SUBSTRING(orderID, 4, LEN(orderID)) AS INT)) FROM SalesOrder";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return String.format("ĐHB%03d", rs.getInt(1) + 1);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return "ĐHB001";
    }

    public boolean delete(String orderID) {
        String sqlDetail = "DELETE FROM SalesOrderDetail WHERE OrderID = ?";
        String sqlOrder = "DELETE FROM SalesOrder WHERE OrderID = ?";

        // Sử dụng DatabaseConnection của bạn
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false); // Bắt đầu giao dịch (Transaction)

            try (PreparedStatement psDetail = conn.prepareStatement(sqlDetail);
                 PreparedStatement psOrder = conn.prepareStatement(sqlOrder)) {

                // 1. Xóa trong bảng chi tiết trước
                psDetail.setString(1, orderID);
                psDetail.executeUpdate();

                // 2. Xóa trong bảng hóa đơn chính
                psOrder.setString(1, orderID);
                int rowsAffected = psOrder.executeUpdate();

                conn.commit(); // Xác nhận xóa vĩnh viễn
                return rowsAffected > 0;

            } catch (Exception e) {
                conn.rollback(); // Nếu có lỗi, khôi phục lại dữ liệu ban đầu
                e.printStackTrace();
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(SalesOrder order) {
        String sql = "UPDATE SalesOrder SET CustomerID = ?, TotalAmount = ?, Notes = ? WHERE OrderID = ?";

        // Sử dụng DatabaseConnection của bạn
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, order.getCustomerID());
            ps.setBigDecimal(2, order.getTotalAmount());
            ps.setString(3, order.getNotes());
            ps.setString(4, order.getOrderID());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}