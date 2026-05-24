package client.controller;

import client.service.SocketClient;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import shared.Request;
import shared.Response;
import shared.model.ChatMessage;
import shared.model.Customer;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class ChatController {

    @FXML private TextArea txtChatHistory;
    @FXML private TextField txtMessage;
    @FXML private ComboBox<String> cbReceiver;

    private Socket chatSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    private String myName = "NV1";

    @FXML
    public void initialize() {
        connectToChatServer();
        loadCustomerList();

        cbReceiver.setOnAction(event -> {
            String selectedCustomer = cbReceiver.getValue();
            if (selectedCustomer != null && !selectedCustomer.trim().isEmpty()) {
                loadHistoryWith(selectedCustomer.trim());
            }
        });
    }

    // Danh sách khách hàng từ Database qua Socket cổng 9000
    private void loadCustomerList() {
        try {
            Request req = new Request("GET_ALL_CUSTOMERS", null);
            Response res = SocketClient.sendRequest(req);

            if (res != null && res.isSuccess()) {
                List<Customer> customers = (List<Customer>) res.getData();

                cbReceiver.getItems().clear();
                for (Customer c : customers) {
                    // ĐÃ THÊM .trim() ĐỂ CẮT BỎ DẤU CÁCH THỪA TỪ SQL
                    cbReceiver.getItems().add(c.getCustomerName().trim());
                }
            }
        } catch (Exception e) {
            System.out.println("Lỗi khi tải danh sách Khách hàng: " + e.getMessage());
        }
    }

    private void connectToChatServer() {
        try {
            chatSocket = new Socket("localhost", 9001);
            out = new ObjectOutputStream(chatSocket.getOutputStream());
            in = new ObjectInputStream(chatSocket.getInputStream());

            // Gửi tên của mình lên đầu tiên để Server "ghi vào danh bạ"
            out.writeObject(myName);
            out.flush();

            Thread listenerThread = new Thread(() -> {
                try {
                    while (true) {
                        ChatMessage msg = (ChatMessage) in.readObject();
                        Platform.runLater(() -> {
                            // --- PHÂN LOẠI NGƯỜI GỬI ---
                            if (msg.getSenderName().equals(myName)) {
                                txtChatHistory.appendText("👤 [Tôi]: " + msg.getContent() + "\n");
                            } else {
                                txtChatHistory.appendText("💬 [" + msg.getSenderName() + "]: " + msg.getContent() + "\n");
                            }
                        });
                    }
                } catch (Exception e) {
                    Platform.runLater(() -> txtChatHistory.appendText(" Mất kết nối chat.\n"));
                }
            });
            listenerThread.setDaemon(true);
            listenerThread.start();

        } catch (Exception e) {
            txtChatHistory.appendText(" Lỗi kết nối Server cổng 9001.\n");
        }
    }

    @FXML
    private void handleSend() {
        // Lấy giá trị chưa cắt
        String rawReceiver = cbReceiver.getValue();
        String content = txtMessage.getText().trim();

        // Kiểm tra xem đã chọn ai chưa
        if (rawReceiver == null || rawReceiver.trim().isEmpty() || content.isEmpty()) return;

        // ĐÃ THÊM .trim() CHO TÊN NGƯỜI NHẬN TRƯỚC KHI ĐÓNG GÓI
        String receiver = rawReceiver.trim();

        try {
            // Lúc này receiver đã sạch sẽ hoàn toàn (VD: "Nguyễn Yến")
            ChatMessage newMsg = new ChatMessage(myName, receiver, content);

            out.writeObject(newMsg);
            out.reset();

            txtMessage.clear();
        } catch (Exception e) {
            txtChatHistory.appendText(" Lỗi gửi tin.\n");
        }
    }
    private void loadHistoryWith(String customerName) {
        // Xóa sạch khung chat cũ trên màn hình
        txtChatHistory.clear();
        txtChatHistory.appendText("");

        try {
            // Gửi Request qua cổng 9000 (API) để đòi lịch sử
            // Truyền mảng String chứa tên 2 người cần lấy lịch sử
            String[] participants = {myName, customerName};
            Request req = new Request("GET_CHAT_HISTORY", participants);
            Response res = SocketClient.sendRequest(req);

            if (res != null && res.isSuccess()) {
                List<ChatMessage> history = (List<ChatMessage>) res.getData();

                // In từng dòng lịch sử ra màn hình
                for (ChatMessage msg : history) {
                    if (msg.getSenderName().equals(myName)) {
                        txtChatHistory.appendText("👤 [Tôi]: " + msg.getContent() + "\n");
                    } else {
                        txtChatHistory.appendText("💬 [" + msg.getSenderName() + "]: " + msg.getContent() + "\n");
                    }
                }
            } else {
                txtChatHistory.appendText("(Chưa có tin nhắn nào)\n");
            }
        } catch (Exception e) {
            txtChatHistory.appendText(" Lỗi khi tải lịch sử chat.\n");
        }
    }
}