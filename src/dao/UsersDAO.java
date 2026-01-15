package dao;

import model.Users;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsersDAO {

    // Hàm login: Trả về đối tượng User nếu đúng, trả về null nếu sai
    public Users login(String username, String password) {
        Users user = null;

        // Câu lệnh SQL (Lưu ý dấu ngoặc vuông [User] vì User là từ khóa)
        String sql = "SELECT * FROM Users WHERE userName = ? AND passWord = ?";

        // Sử dụng try-with-resources để tự động đóng kết nối (tránh lag máy)
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Gán giá trị vào dấu ?
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Nếu tìm thấy dòng dữ liệu, tạo đối tượng User mới
                    int id = rs.getInt("userID");
                    String name = rs.getString("userName");
                    String pass = rs.getString("passWord");

                    user = new Users(id, name, pass);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }
}