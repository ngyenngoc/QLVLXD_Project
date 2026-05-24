package server.dao;

import shared.model.ChatMessage;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChatDAO {

    // Lưu tin nhắn
    public void saveMessage(ChatMessage msg) {

        String sql = "INSERT INTO ChatMessage(senderName, receiverName, content) VALUES (?, ?, ?)";

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, msg.getSenderName());
            ps.setString(2, msg.getReceiverName());
            ps.setString(3, msg.getContent());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Lấy lịch sử chat giữa 2 người
    public List<ChatMessage> getChatHistory(String user1, String user2) {

        List<ChatMessage> history = new ArrayList<>();

        String sql =
                "SELECT * FROM ChatMessage " +
                        "WHERE (senderName = ? AND receiverName = ?) " +
                        "OR (senderName = ? AND receiverName = ?) " +
                        "ORDER BY sendTime ASC";

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, user1);
            ps.setString(2, user2);
            ps.setString(3, user2);
            ps.setString(4, user1);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ChatMessage msg = new ChatMessage(
                        rs.getInt("messageID"),
                        rs.getString("senderName"),
                        rs.getString("receiverName"),
                        rs.getString("content"),
                        rs.getTimestamp("sendTime").toLocalDateTime()
                );
                history.add(msg);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return history;
    }
}