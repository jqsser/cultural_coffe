package tn.esprit.entities;

import java.time.LocalDateTime;

public class Message {
    private int id;
    private String content;
    private Matching matching;
    private User user;
    private boolean updatedMessage;
    private LocalDateTime createdAt;

    public Message() {
    }

    public Message(String content, User user, Matching matching) {
        this.content = content;
        this.user = user;
        this.matching = matching;
        this.createdAt = LocalDateTime.now();
        this.updatedMessage = false;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Matching getMatching() {
        return matching;
    }

    public void setMatching(Matching matching) {
        this.matching = matching;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isUpdatedMessage() {
        return updatedMessage;
    }

    public void setUpdatedMessage(boolean updatedMessage) {
        this.updatedMessage = updatedMessage;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", updatedMessage=" + updatedMessage +
                ", createdAt=" + createdAt +
                '}';
    }
}