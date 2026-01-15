package dao;

import model.SalesOrder;
import model.SalesOrderDetail;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SalesOrderDAO {
    private final SalesOrderDetailDAO detailDAO = new SalesOrderDetailDAO();

    public boolean insertFullOrder(SalesOrder order, List<SalesOrderDetail> details) {
        String sql = "INSERT INTO SalesOrder (orderID, orderDate, customerID, userID, paymentMethod, notes, totalAmount) VALUES (?, ?, ?, ?, ?, ?, ?)";

        Connection conn = DatabaseConnection.getConnection();
        try {
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, order.getOrderID());
                ps.setDate(2, java.sql.Date.valueOf(order.getOrderDate()));
                ps.setString(3, order.getCustomerID());
                ps.setInt(4, order.getUserID());
                ps.setString(5, order.getPaymentMethod());
                ps.setString(6, order.getNotes());
                ps.setBigDecimal(7, order.getTotalAmount());
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
        String sql = "SELECT o.*, c.customerName FROM SalesOrder o JOIN Customer c ON o.customerID = c.customerID";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new SalesOrder(
                        rs.getString("orderID"),
                        rs.getDate("orderDate").toLocalDate(),
                        rs.getString("customerID"),
                        rs.getString("customerName"),
                        rs.getInt("userID"),
                        rs.getString("paymentMethod"),
                        rs.getString("notes"),
                        rs.getBigDecimal("totalAmount")
                ));
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
}