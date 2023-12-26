package org.example.objects;

import java.io.Serial;
import java.io.Serializable;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class Message implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private long id;
    private long chatId;
    private long userId;
    private ZonedDateTime time;
    private ZoneId zoneId;
    private String text;
    private Boolean isSending;
    private Boolean isDelivered;
    private Boolean isReading;

    public Message() {
    }

    public Message(long id, long chatId, long userId, String text) {
        this.id = id;
        this.chatId = chatId;
        this.userId = userId;
        this.text = text;
        zoneId = ZoneId.systemDefault();
        time = ZonedDateTime.now(zoneId);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public ZonedDateTime getTime() {
        return time;
    }

    public void setTime(ZonedDateTime time) {
        this.time = time;
    }

    public ZoneId getZoneId() {
        return zoneId;
    }

    public void setZoneId(ZoneId zoneId) {
        this.zoneId = zoneId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean getSending() {
        return isSending;
    }

    public void setSending(Boolean sending) {
        isSending = sending;
    }

    public Boolean getDelivered() {
        return isDelivered;
    }

    public void setDelivered(Boolean delivered) {
        isDelivered = delivered;
    }

    public Boolean getReading() {
        return isReading;
    }

    public void setReading(Boolean reading) {
        isReading = reading;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", chatId=" + chatId +
                ", userId=" + userId +
                ", time=" + time +
                ", zoneId=" + zoneId +
                ", text='" + text + '\'' +
                ", isSending=" + isSending +
                ", isDelivered=" + isDelivered +
                ", isReading=" + isReading +
                '}';
    }
}
