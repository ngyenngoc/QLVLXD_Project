package dao;

import model.Material;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MaterialDAO {

    public List<Material> getAll() {
        List<Material> list = new ArrayList<>();
        // CÂU LỆNH SQL KẾT NỐI 2 BẢNG
        String sql = "SELECT m.*, c.categoryName " +
                "FROM Material m " +
                "JOIN Category c ON m.categoryID = c.categoryID";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Material(
                        rs.getString("materialID"),
                        rs.getString("materialName"),
                        rs.getString("unit"),
                        rs.getBigDecimal("purchasePrice"),
                        rs.getBigDecimal("salePrice"),
                        rs.getInt("stockQuantity"),
                        rs.getString("description"),
                        rs.getInt("categoryID"),
                        rs.getString("categoryName")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // Phương thức thêm mới vật liệu
    public boolean insert(Material m) {
        String sql = "INSERT INTO Material (materialID, materialName, unit, purchasePrice, salePrice, stockQuantity, description, categoryID) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, m.getMaterialID());
            ps.setString(2, m.getMaterialName());
            ps.setString(3, m.getUnit());
            ps.setBigDecimal(4, m.getPurchasePrice());
            ps.setBigDecimal(5, m.getSalePrice());
            ps.setInt(6, m.getStockQuantity());
            ps.setString(7, m.getDescription());
            ps.setInt(8, m.getCategoryID());
            return ps.executeUpdate() > 0;
        } catch (SQLException e)
        { return false; }
    }


    public boolean update(Material m) {
        String sql = "UPDATE Material SET materialName=?, unit=?, purchasePrice=?, salePrice=?, stockQuantity=?, description=?, categoryID=? WHERE materialID=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, m.getMaterialName());
            ps.setString(2, m.getUnit());
            ps.setBigDecimal(3, m.getPurchasePrice());
            ps.setBigDecimal(4, m.getSalePrice());
            ps.setInt(5, m.getStockQuantity());
            ps.setString(6, m.getDescription());
            ps.setInt(7, m.getCategoryID());
            ps.setString(8, m.getMaterialID());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    // PHƯƠNG THỨC LỖI: Chỉnh sửa tham số String id cho đồng nhất
    public boolean delete(String materialID) {
        String sql = "DELETE FROM Material WHERE materialID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, materialID);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    public int getCurrentStock(String materialID) {
        String sql = "SELECT stockQuantity FROM Material WHERE MaterialID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, materialID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("stockQuantity");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0; // Nếu không tìm thấy hoặc lỗi
    }
    public String generateNewID() {
        String sql = "SELECT MAX(CAST(SUBSTRING(materialID, 3, LEN(materialID)) AS INT)) FROM Material";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return String.format("VL%03d", rs.getInt(1) + 1);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return "VL001";
    }
}