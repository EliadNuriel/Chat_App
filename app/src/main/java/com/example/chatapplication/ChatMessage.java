package com.example.chatapplication;

public class ChatMessage {
    private String senderName;
    private String senderId;
    private String senderPhoto;
    private String messageContent;
    private long dateMillis;


    public ChatMessage(String senderName, String senderId, String senderPhoto, String messageContent, long dateMillis) {
        this.senderName = senderName;
        this.senderId = senderId;
        this.senderPhoto = senderPhoto;
        this.messageContent = messageContent;
        this.dateMillis = dateMillis;
    }

    public ChatMessage() {}

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderPhoto() {
        return senderPhoto;
    }

    public void setSenderPhoto(String senderPhoto) {
        this.senderPhoto = senderPhoto;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public long getDateMillis() {
        return dateMillis;
    }

    public void setDateMillis(long dateMillis) {
        this.dateMillis = dateMillis;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
}
