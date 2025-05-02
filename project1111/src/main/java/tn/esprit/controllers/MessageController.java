package tn.esprit.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import tn.esprit.entities.Message;
import tn.esprit.entities.Matching;
import tn.esprit.entities.User;
import tn.esprit.services.MessageService;
import tn.esprit.services.MatchingService;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class MessageController {
    @FXML
    private ListView<Matching> matchingListView;
    @FXML
    private ListView<Message> messageListView;
    @FXML
    private TextArea messageTextArea;
    @FXML
    private Button sendButton;
    @FXML
    private VBox messageContainer;
    @FXML
    private Label matchingTitleLabel;

    private MessageService messageService = new MessageService();
    private MatchingService matchingService = new MatchingService();
    private User currentUser; // For testing, set user ID 38

    public void initialize() {
        try {
            // For testing, create a user with ID 38
            currentUser = new User();
            currentUser.setId(38);
            currentUser.setNomUser("Test User");

            loadMatchings();
            setupSelectionListeners();
        } catch (SQLException e) {
            showAlert("Error", "Failed to initialize: " + e.getMessage());
        }
    }

    private void loadMatchings() throws SQLException {
        // Get matchings where user is host or assessor
        ObservableList<Matching> matchings = FXCollections.observableArrayList();
        matchings.addAll(matchingService.getByUserId(currentUser.getId())); // Hosted matchings
        matchings.addAll(matchingService.getAssessorMatchings(currentUser.getId())); // Assessor matchings

        matchingListView.setItems(matchings);
        matchingListView.setCellFactory(param -> new ListCell<Matching>() {
            @Override
            protected void updateItem(Matching matching, boolean empty) {
                super.updateItem(matching, empty);
                if (empty || matching == null) {
                    setText(null);
                } else {
                    setText(matching.getName() + " - " + matching.getSujetRencontre());
                }
            }
        });
    }

    private void setupSelectionListeners() {
        matchingListView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newMatching) -> {
                    if (newMatching != null) {
                        loadMessagesForMatching(newMatching);
                        matchingTitleLabel.setText("Matching: " + newMatching.getName());
                    }
                });
    }

    private void loadMessagesForMatching(Matching matching) {
        try {
            List<Message> messages = messageService.getByMatching(matching.getId());
            ObservableList<Message> messageList = FXCollections.observableArrayList(messages);

            messageListView.setItems(messageList);
            messageListView.setCellFactory(param -> new ListCell<Message>() {
                @Override
                protected void updateItem(Message message, boolean empty) {
                    super.updateItem(message, empty);
                    if (empty || message == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        // Create a custom display for each message
                        VBox messageBox = new VBox();
                        Label userLabel = new Label(message.getUser().getNomUser() + ":");
                        Label contentLabel = new Label(message.getContent());
                        Label timeLabel = new Label(message.getCreatedAt().toString());

                        // Style based on current user
                        if (message.getUser().getId() == currentUser.getId()) {
                            messageBox.setStyle("-fx-background-color: #DCF8C6; -fx-padding: 5px; -fx-background-radius: 5px;");
                        } else {
                            messageBox.setStyle("-fx-background-color: #ECECEC; -fx-padding: 5px; -fx-background-radius: 5px;");
                        }

                        messageBox.getChildren().addAll(userLabel, contentLabel, timeLabel);
                        setGraphic(messageBox);
                    }
                }
            });
        } catch (SQLException e) {
            showAlert("Error", "Failed to load messages: " + e.getMessage());
        }
    }

    @FXML
    private void handleSendMessage() {
        Matching selectedMatching = matchingListView.getSelectionModel().getSelectedItem();
        String content = messageTextArea.getText().trim();

        if (selectedMatching == null) {
            showAlert("Error", "Please select a matching first");
            return;
        }

        if (content.isEmpty()) {
            showAlert("Error", "Message cannot be empty");
            return;
        }

        try {
            Message message = new Message(content, currentUser, selectedMatching);
            messageService.ajouter(message);

            // Refresh messages
            loadMessagesForMatching(selectedMatching);
            messageTextArea.clear();
        } catch (SQLException e) {
            showAlert("Error", "Failed to send message: " + e.getMessage());
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