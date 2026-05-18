package client.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import shared.model.ChatMessage;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class CustomerChatController {

    @FXML
    private TextField txtMyName;
    @FXML
    private Button btnConnect;
    @FXML
    private TextArea txtChatHistory;

    @FXML
    private TextField txtReceiver; // Thường mặc định gửi cho NV1 hoặc Admin
    @FXML
    private TextField txtMessage;
    @FXML
    private Button btnSend;

    private Socket chatSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    private String myName;

    @FXML
    private void handleConnect() {
        myName = txtMyName.getText().trim();
        if (myName.isEmpty()) {
            txtChatHistory.appendText("Vui lòng nhập tên của bạn trước khi kết nối!\n");
            return;
        }

        try {
            // Kết nối vào Tổng đài Chat 9001
            chatSocket = new Socket("localhost", 9001);
            out = new ObjectOutputStream(chatSocket.getOutputStream());
            in = new ObjectInputStream(chatSocket.getInputStream());

            // Gửi tên Khách hàng lên để Server ghi danh
            out.writeObject(myName);
            out.flush();

            txtChatHistory.appendText(" Chào " + myName + ", bạn đã kết nối với Cửa hàng!\n");

            // Khóa ô nhập tên, mở khóa ô chat
            txtMyName.setDisable(true);
            btnConnect.setDisable(true);
            txtMessage.setDisable(false);
            btnSend.setDisable(false);

            // LUỒNG LẮNG NGHE
            Thread listenerThread = new Thread(() -> {
                try {
                    while (true) {
                        ChatMessage msg = (ChatMessage) in.readObject();
                        Platform.runLater(() -> {
                            // Phân loại tin nhắn
                            if (msg.getSenderName().equals(myName)) {
                                txtChatHistory.appendText(" [Tôi]: " + msg.getContent() + "\n");
                            } else {
                                txtChatHistory.appendText("[" + msg.getSenderName() + "]: " + msg.getContent() + "\n");
                            }
                        });
                    }
                } catch (Exception e) {
                    Platform.runLater(() -> txtChatHistory.appendText(" Bị ngắt kết nối với cửa hàng.\n"));
                }
            });
            listenerThread.setDaemon(true);
            listenerThread.start();

        } catch (Exception e) {
            txtChatHistory.appendText(" Không thể kết nối tới máy chủ.\n");
        }
    }

    @FXML
    private void handleSend() {
        String receiver = txtReceiver.getText().trim();
        String content = txtMessage.getText().trim();

        if (receiver.isEmpty() || content.isEmpty()) return;

        try {
            ChatMessage newMsg = new ChatMessage(myName, receiver, content);
            out.writeObject(newMsg);
            out.reset();

            txtMessage.clear();
        } catch (Exception e) {
            txtChatHistory.appendText(" Lỗi gửi tin.\n");
        }
    }
}