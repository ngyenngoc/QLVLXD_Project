package dao;

import model.Supplier;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupplierDAO {

    // 1. Lấy tất cả dữ liệu từ SQL Server
    public List<Supplier> getAll() {
        System.out.println("Đang thực hiện lấy dữ liệu từ SQL...");
        List<Supplier> list = new ArrayList<>();
        String sql = "SELECT * FROM Supplier";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Supplier(
                        rs.getString("supplierID"),
                        rs.getString("supplierName"),
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
    public boolean insert(Supplier s) {
        String sql = "INSERT INTO Supplier (supplierID, supplierName, address, phoneNumber, email) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, s.getSupplierID());
            ps.setString(2, s.getSupplierName());
            ps.setString(3, s.getAddress());
            ps.setString(4, s.getPhoneNumber());
            ps.setString(5, s.getEmail());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 3. Cập nhật thông tin
    public boolean update(Supplier s) {
        String sql = "UPDATE Supplier SET supplierName = ?, address = ?, phoneNumber = ?, email = ? WHERE supplierID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, s.getSupplierName());
            ps.setString(2, s.getAddress());
            ps.setString(3, s.getPhoneNumber());
            ps.setString(4, s.getEmail());
            ps.setString(5, s.getSupplierID());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 4. Xóa nhà cung cấp
    public boolean delete(String supplierID) {
        String sql = "DELETE FROM Supplier WHERE supplierID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, supplierID);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 5. Tự động sinh mã mới (Ví dụ: NCC001, NCC002...)
    public String generateNewID() {
        String sql = "SELECT MAX(CAST(SUBSTRING(supplierID, 4, LEN(supplierID)) AS INT)) FROM Supplier";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                int lastID = rs.getInt(1);
                return String.format("NCC%03d", lastID + 1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "NCC001"; // Mặc định nếu bảng trống
    }
}