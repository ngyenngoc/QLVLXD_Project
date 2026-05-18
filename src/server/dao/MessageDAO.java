package server.dao;

import shared.model.ChatMessage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {

    // 1. Lưu tin nhắn mới vào Database
    public boolean insert(ChatMessage msg) {
        String sql = "INSERT INTO Message (senderName, receiverName, content) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, msg.getSenderName());
            ps.setString(2, msg.getReceiverName());
            ps.setString(3, msg.getContent());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 2. Lấy toàn bộ lịch sử tin nhắn để hiển thị khi người dùng mở form Chat
    public List<ChatMessage> getAllMessages() {
        List<ChatMessage> list = new ArrayList<>();
        String sql = "SELECT * FROM Message ORDER BY sendTime ASC"; // Lấy theo thứ tự thời gian cũ đến mới

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new ChatMessage(
                        rs.getInt("messageID"),
                        rs.getString("senderName"),
                        rs.getString("receiverName"),
                        rs.getString("content"),
                        // Chuyển đổi an toàn từ SQL DATETIME sang Java LocalDateTime
                        rs.getTimestamp("sendTime") != null ? rs.getTimestamp("sendTime").toLocalDateTime() : null
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}