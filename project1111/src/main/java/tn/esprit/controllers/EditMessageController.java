package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import tn.esprit.entities.Message;
import tn.esprit.services.MessageService;

public class EditMessageController {
    @FXML private TextArea messageContentArea;

    private Message currentMessage;
    private ChatController chatController;
    private MessageService messageService = new MessageService();

    public void setCurrentMessage(Message message) {
        this.currentMessage = message;
        messageContentArea.setText(message.getContent());
    }

    public void setChatController(ChatController controller) {
        this.chatController = controller;
    }

    public void processResult() {
        String newContent = messageContentArea.getText().trim();
        if (newContent.isEmpty()) {
            showAlert("Error", "Message cannot be empty");
            return;
        }

        try {
            currentMessage.setContent(newContent);
            currentMessage.setUpdatedMessage(true);
            messageService.modifier(currentMessage);
        } catch (Exception e) {
            showAlert("Error", "Failed to update message: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}