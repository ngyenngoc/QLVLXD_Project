package shared.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ChatMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    private int messageID;
    private String senderName;
    private String receiverName;
    private String content;
    private LocalDateTime sendTime;

    public ChatMessage(String senderName, String receiverName, String content) {
        this.senderName = senderName;
        this.receiverName = receiverName;
        this.content = content;
    }

    public ChatMessage(int messageID, String senderName, String receiverName, String content, LocalDateTime sendTime) {
        this.messageID = messageID;
        this.senderName = senderName;
        this.receiverName = receiverName;
        this.content = content;
        this.sendTime = sendTime;
    }

    public int getMessageID() { return messageID; }
    public void setMessageID(int messageID) { this.messageID = messageID; }

    public String getSenderName() { return senderName; }
    public void setSenderName(String senderName) { this.senderName = senderName; }

    public String getReceiverName() { return receiverName; }
    public void setReceiverName(String receiverName) { this.receiverName = receiverName; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public LocalDateTime getSendTime() { return sendTime; }
    public void setSendTime(LocalDateTime sendTime) { this.sendTime = sendTime; }
}