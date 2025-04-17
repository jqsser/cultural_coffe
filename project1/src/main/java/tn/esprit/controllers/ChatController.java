package tn.esprit.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tn.esprit.entities.Message;
import tn.esprit.entities.Matching;
import tn.esprit.entities.User;
import tn.esprit.services.MatchingService;
import tn.esprit.services.MessageService;

import javafx.geometry.Pos;
import javafx.geometry.Insets;
import java.time.format.DateTimeFormatter;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ButtonType;
import java.io.IOException;
import java.util.List;

public class ChatController {
    @FXML private ListView<Matching> matchingListView;
    @FXML private ListView<Message> messageListView;
    @FXML private TextArea messageTextArea;
    @FXML private Label matchingNameLabel;
    @FXML private Label matchingSubjectLabel;

    private User currentUser;
    private MatchingService matchingService = new MatchingService();
    private MessageService messageService = new MessageService();
    private Matching selectedMatching;

    @FXML
    public void initialize() {
        // Set test user with ID 38
        currentUser = new User();
        currentUser.setId(34);
        currentUser.setNomUser("Test User");

        // Configure list views
        setupMatchingListView();
        setupMessageListView();

        // Load user's matchings
        loadUserMatchings();
    }

    private void setupMatchingListView() {
        matchingListView.setCellFactory(param -> new ListCell<Matching>() {
            @Override
            protected void updateItem(Matching matching, boolean empty) {
                super.updateItem(matching, empty);
                if (empty || matching == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    HBox hbox = new HBox(10);
                    ImageView avatar = new ImageView(new Image(getClass().getResourceAsStream("/images/avatar.png")));
                    avatar.setFitHeight(40);
                    avatar.setFitWidth(40);

                    VBox vbox = new VBox(5);
                    Label nameLabel = new Label(matching.getName());
                    nameLabel.setStyle("-fx-font-weight: bold;");
                    Label idLabel = new Label("Table #" + matching.getNumTable());
                    idLabel.setStyle("-fx-text-fill: #6c757d; -fx-font-size: 12;");

                    vbox.getChildren().addAll(nameLabel, idLabel);
                    hbox.getChildren().addAll(avatar, vbox);
                    setGraphic(hbox);
                }
            }
        });

        // Handle selection changes
        matchingListView.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> loadMatchingMessages(newVal));
    }

    private void setupMessageListView() {
        messageListView.setCellFactory(param -> new ListCell<Message>() {
            @Override
            protected void updateItem(Message message, boolean empty) {
                super.updateItem(message, empty);
                if (empty || message == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    // Create the main container
                    HBox container = new HBox(10);

                    // Create message content
                    VBox messageBox = new VBox(5);
                    messageBox.setMaxWidth(messageListView.getWidth() * 0.7);

                    boolean isCurrentUser = message.getUser().getId() == currentUser.getId();

                    // Create message bubble
                    VBox bubbleContent = new VBox(2);
                    bubbleContent.setStyle(isCurrentUser ?
                            "-fx-background-color: #0084ff; -fx-background-radius: 15;" +
                                    "-fx-padding: 8 12; -fx-max-width: 500;" :
                            "-fx-background-color: #e4e6eb; -fx-background-radius: 15;" +
                                    "-fx-padding: 8 12; -fx-max-width: 500;");

                    // Username label
                    Label userLabel = new Label(message.getUser().getNomUser());
                    userLabel.setStyle(isCurrentUser ?
                            "-fx-font-weight: bold; -fx-text-fill: white;" :
                            "-fx-font-weight: bold; -fx-text-fill: #000000;");

                    // Message content
                    Label messageLabel = new Label(message.getContent());
                    messageLabel.setWrapText(true);
                    messageLabel.setStyle(isCurrentUser ?
                            "-fx-text-fill: white;" :
                            "-fx-text-fill: #000000;");

                    // Timestamp and edited indicator
                    String timeText = message.getCreatedAt().format(DateTimeFormatter.ofPattern("HH:mm")) +
                            (message.isUpdatedMessage() ? " (edited)" : "");
                    Label timeLabel = new Label(timeText);
                    timeLabel.setStyle("-fx-font-size: 10; -fx-text-fill: " +
                            (isCurrentUser ? "rgba(255,255,255,0.7)" : "#757575"));

                    // Add action buttons for current user's messages
                    if (isCurrentUser) {
                        HBox actionButtons = new HBox(5);
                        actionButtons.setAlignment(Pos.CENTER_RIGHT);

                        // Edit button
                        Button editButton = new Button("Edit");
                        editButton.getStyleClass().add("message-action-button");
                        editButton.setOnAction(e -> showEditDialog(message));

                        // Delete button
                        Button deleteButton = new Button("Delete");
                        deleteButton.getStyleClass().add("message-action-button");
                        deleteButton.setOnAction(e -> deleteMessage(message));

                        actionButtons.getChildren().addAll(editButton, deleteButton);
                        bubbleContent.getChildren().addAll(userLabel, messageLabel, timeLabel, actionButtons);
                    } else {
                        bubbleContent.getChildren().addAll(userLabel, messageLabel, timeLabel);
                    }

                    // Create avatar
                    ImageView avatar = new ImageView(new Image(getClass().getResourceAsStream("/images/avatar.png")));
                    avatar.setFitHeight(30);
                    avatar.setFitWidth(30);

                    // Add components to message box
                    messageBox.getChildren().add(bubbleContent);

                    // Align messages based on sender
                    if (isCurrentUser) {
                        container.setAlignment(Pos.CENTER_RIGHT);
                        container.getChildren().addAll(messageBox, avatar);
                    } else {
                        container.setAlignment(Pos.CENTER_LEFT);
                        container.getChildren().addAll(avatar, messageBox);
                    }

                    container.setPadding(new Insets(5, 10, 5, 10));
                    setGraphic(container);
                }
            }
        });
    }

    // Add these methods to handle edit and delete functionality
    private void showEditDialog(Message message) {
        TextInputDialog dialog = new TextInputDialog(message.getContent());
        dialog.setTitle("Edit Message");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter new message:");

        // Style the dialog
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        dialogPane.getStyleClass().add("custom-dialog");

        dialog.showAndWait().ifPresent(newContent -> {
            if (!newContent.trim().isEmpty() && !newContent.equals(message.getContent())) {
                try {
                    // Update message content
                    message.setContent(newContent.trim());
                    message.setUpdatedMessage(true);
                    messageService.modifier(message);

                    // Refresh the message list
                    loadMatchingMessages(selectedMatching);
                } catch (Exception e) {
                    showAlert("Error", "Failed to update message: " + e.getMessage());
                }
            }
        });
    }

    private void deleteMessage(Message message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Message");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete this message?");

        // Style the alert
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        dialogPane.getStyleClass().add("custom-dialog");

        alert.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                try {
                    messageService.supprimer(message.getId());
                    // Refresh the message list
                    loadMatchingMessages(selectedMatching);
                } catch (Exception e) {
                    showAlert("Error", "Failed to delete message: " + e.getMessage());
                }
            }
        });
    }



    private void loadUserMatchings() {
        try {
            // Get matchings where user is host or participant
            List<Matching> hostMatchings = matchingService.getByUserId(currentUser.getId());
            List<Matching> participantMatchings = matchingService.getAssessorMatchings(currentUser.getId());

            ObservableList<Matching> allMatchings = FXCollections.observableArrayList();
            allMatchings.addAll(hostMatchings);
            allMatchings.addAll(participantMatchings);

            matchingListView.setItems(allMatchings);

            // Select first matching if available
            if (!allMatchings.isEmpty()) {
                matchingListView.getSelectionModel().selectFirst();
            }
        } catch (Exception e) {
            showAlert("Error", "Failed to load matchings: " + e.getMessage());
        }
    }

    private void loadMatchingMessages(Matching matching) {
        if (matching == null) return;

        selectedMatching = matching;
        matchingNameLabel.setText(matching.getName());
        matchingSubjectLabel.setText(matching.getSujetRencontre());

        try {
            List<Message> messages = messageService.getByMatching(matching.getId());
            messageListView.setItems(FXCollections.observableArrayList(messages));
        } catch (Exception e) {
            showAlert("Error", "Failed to load messages: " + e.getMessage());
        }
    }

    @FXML
    private void sendMessage() {
        if (selectedMatching == null || messageTextArea.getText().trim().isEmpty()) {
            return;
        }

        try {
            Message message = new Message(
                    messageTextArea.getText().trim(),
                    currentUser,
                    selectedMatching
            );
            messageService.ajouter(message);
            messageTextArea.clear();
            loadMatchingMessages(selectedMatching);
        } catch (Exception e) {
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

    // Add these fields to your controller
    @FXML private TextField searchField;

    // Add these methods
    @FXML
    private void searchMatchings() {
        String searchText = searchField.getText().trim().toLowerCase();

        if (searchText.isEmpty()) {
            loadUserMatchings(); // Reload all matchings if search is empty
            return;
        }

        try {
            List<Matching> allMatchings = matchingService.getByUserId(currentUser.getId());
            allMatchings.addAll(matchingService.getAssessorMatchings(currentUser.getId()));

            ObservableList<Matching> filtered = FXCollections.observableArrayList();
            for (Matching m : allMatchings) {
                if (m.getName().toLowerCase().contains(searchText) ||
                        m.getSujetRencontre().toLowerCase().contains(searchText)) {
                    filtered.add(m);
                }
            }

            matchingListView.setItems(filtered);
        } catch (Exception e) {
            showAlert("Search Error", "Failed to search matchings: " + e.getMessage());
        }
    }

    @FXML
    private void showCreateMatchingDialog() {
        try {
            // Update the path to match your project structure
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/CreateMatching.fxml"));

            // For debugging - print the URL to verify the resource is found
            System.out.println("FXML Location: " + getClass().getResource("/CreateMatching.fxml"));

            DialogPane dialogPane = loader.load();

            // Create the dialog
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Create New Matching");

            // Get the controller and set necessary data
            CreateMatchingController controller = loader.getController();
            controller.setDialogStage((Stage) dialog.getDialogPane().getScene().getWindow());
            controller.setCurrentUser(currentUser);
            controller.setChatController(this);

            // Show the dialog and process the result
            dialog.showAndWait().ifPresent(buttonType -> {
                if (buttonType.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                    controller.handleCreate();
                    if (controller.isCreateClicked()) {
                        // Refresh the matching list
                        loadUserMatchings();
                    }
                }
            });

        } catch (IOException e) {
            e.printStackTrace(); // This will help debug the exact error
            showAlert("Error", "Could not load the create matching dialog: " + e.getMessage());
        }
    }
    @FXML
    private void handle3DCoffee() {
        // Implement your 3D Coffee navigation here
        showAlert("Info", "Navigating to 3D Coffee view");
    }

    @FXML
    public void showEditMatchingDialog(ActionEvent actionEvent) {
        // Get the selected matching
        Matching selectedMatching = matchingListView.getSelectionModel().getSelectedItem();

        if (selectedMatching == null) {
            showAlert("No Matching Selected", "Please select a matching to edit.");
            return;
        }

        try {
            // Load the EditMatching.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EditMatching.fxml"));
            DialogPane dialogPane = loader.load();

            // Get the controller for the edit dialog
            EditMatchingController controller = loader.getController();
            controller.setCurrentMatching(selectedMatching);
            controller.setChatController(this);

            // Create the dialog
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Edit Matching");

            // Show the dialog and process the result
            dialog.showAndWait().ifPresent(result -> {
                if (result.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                    if (controller.processResult()) {
                        loadUserMatchings(); // Refresh the matching list
                    }
                }
            });

        } catch (IOException e) {
            showAlertw("Error", "Could not load the edit matching dialog: " + e.getMessage());
        }
    }

    // Helper method to show an alert
    private void showAlertw(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void correctMessage(ActionEvent actionEvent) {
    }
}