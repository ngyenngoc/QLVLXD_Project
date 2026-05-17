package server.dao;

import shared.model.Users;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsersDAO {

    // Hàm login: Trả về đối tượng User nếu đúng, trả về null nếu sai
    public Users login(String username, String password) {
        Users user = null;


        String sql = "SELECT * FROM Users WHERE userName = ? AND passWord = ?";


        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {


            pstmt.setString(1, username);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {

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