package dao;

import model.Category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {
    // 1. Lấy tất cả dữ liệu từ SQL Server
    public List<Category> getAll() {
        System.out.println("Đang thực hiện lấy dữ liệu từ SQL...");
        List<Category> list = new ArrayList<>();
        String sql = "SELECT * FROM Category";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Category(
                        rs.getInt("categoryID"),
                        rs.getString("categoryName"),
                        rs.getString("description")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2. Thêm mới một danh mục
    public boolean insert(Category c) {
        String sql = "INSERT INTO Category (categoryID, CategoryName, description) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, c.getCategoryID());
            ps.setString(2, c.getCategoryName());
            ps.setString(3, c.getDescription());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // 3. Cập nhật thông tin
    public boolean update(Category c) {
        String sql = "UPDATE Category SET categoryName = ?, description = ? WHERE categoryID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, c.getCategoryName());
            ps.setString(2, c.getDescription());
            ps.setInt(3, c.getCategoryID());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // 4. Xóa danh mục
    public boolean delete(int categoryID) {
        String sql = "DELETE FROM Category WHERE categoryID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, categoryID);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
