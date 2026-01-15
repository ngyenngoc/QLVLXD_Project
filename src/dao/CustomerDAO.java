package dao;

import model. Customer;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class  CustomerDAO {

    // 1. Lấy tất cả dữ liệu từ SQL Server
    public List< Customer> getAll() {
        System.out.println("Đang thực hiện lấy dữ liệu từ SQL...");
        List< Customer> list = new ArrayList<>();
        String sql = "SELECT * FROM  Customer";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new  Customer(
                        rs.getString("customerID"),
                        rs.getString("customerName"),
                        rs.getString("address"),
                        rs.getString("phoneNumber"),
                        rs.getString("email")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2. Thêm mới một nhà cung cấp
    public boolean insert( Customer c) {
        String sql = "INSERT INTO Customer ( customerID, customerName, address, phoneNumber, email) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, c.getCustomerID());
            ps.setString(2, c.getCustomerName());
            ps.setString(3, c.getAddress());
            ps.setString(4, c.getPhoneNumber());
            ps.setString(5, c.getEmail());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 3. Cập nhật thông tin
    public boolean update( Customer c) {
        String sql = "UPDATE  Customer SET  customerName = ?, address = ?, phoneNumber = ?, email = ? WHERE  customerID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, c.getCustomerName());
            ps.setString(2, c.getAddress());
            ps.setString(3, c.getPhoneNumber());
            ps.setString(4, c.getEmail());
            ps.setString(5, c.getCustomerID());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 4. Xóa nhà cung cấp
    public boolean delete(String  CustomerID) {
        String sql = "DELETE FROM  Customer WHERE customerID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1,  CustomerID);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 5. Tự động sinh mã mới (Ví dụ: NCC001, NCC002...)
    public String generateNewID() {
        String sql = "SELECT MAX(CAST(SUBSTRING( customerID, 4, LEN( customerID)) AS INT)) FROM Customer";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                int lastID = rs.getInt(1);
                return String.format("KH%03d", lastID + 1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "KH001"; // Mặc định nếu bảng trống
    }
}