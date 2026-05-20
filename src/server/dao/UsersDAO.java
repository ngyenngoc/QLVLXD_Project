package server.dao;

import shared.model.Users;
import server.utils.PasswordUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsersDAO {

    public Users login(String username, String plaintextPassword) {
        Users user = null;

        String sql = "SELECT * FROM Users WHERE userName = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {

                    String hashedPasswordFromDB = rs.getString("passWord");


                    if (PasswordUtil.checkPassword(plaintextPassword, hashedPasswordFromDB)) {

                        int id = rs.getInt("userID");
                        String name = rs.getString("userName");
                        user = new Users(id, name, hashedPasswordFromDB);

                    } else {
                        System.out.println("Cảnh báo: Sai mật khẩu!");
                    }
                } else {
                    System.out.println("Cảnh báo: Tài khoản không tồn tại!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }
}