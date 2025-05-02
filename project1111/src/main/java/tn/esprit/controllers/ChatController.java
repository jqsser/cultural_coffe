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

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;

import javafx.geometry.Pos;
import javafx.geometry.Insets;

import java.time.format.DateTimeFormatter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.List;

public class ChatController {
    @FXML
    private ListView<Matching> matchingListView;
    @FXML
    private ListView<Message> messageListView;
    @FXML
    private TextArea messageTextArea;
    @FXML
    private Label matchingNameLabel;
    @FXML
    private Label matchingSubjectLabel;
    @FXML
    private Label matchingIdLabel; // Added for displaying the Matching ID
    @FXML
    private ImageView matchingImageView; // Added for displaying the Matching image
    @FXML
    private ComboBox<Matching> unaccessibleMatchingsComboBox;

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

        // Load user's matchings and unaccessible matchings
        loadUserMatchings();
        loadUnaccessibleMatchings();

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

                    // Dynamically load the image from the database path or fallback to the default
                    // avatar
                    Image image;
                    try {
                        String imagePath = matching.getImage(); // Get the image path from the database
                        if (imagePath == null || imagePath.trim().isEmpty()) {
                            // Use the default avatar if the image path is null or empty
                            image = new Image(getClass().getResource("/images/avatar.png").toExternalForm());
                        } else {
                            // Load the image from the provided path (relative to the filesystem or URL)
                            image = new Image("file:" + imagePath, true); // Lazy loading
                        }
                    } catch (Exception e) {
                        // If loading fails, fallback to the default avatar
                        image = new Image(getClass().getResource("/images/avatar.png").toExternalForm());
                    }

                    ImageView avatar = new ImageView(image);
                    avatar.setFitHeight(40);
                    avatar.setFitWidth(40);

                    VBox vbox = new VBox(5);
                    Label nameLabel = new Label(matching.getName());
                    nameLabel.setStyle("-fx-font-weight: bold;");
                    Label idLabel = new Label("Table #" + matching.getNumTable());
                    idLabel.setStyle("-fx-text-fill: #756c69; -fx-font-size: 12;");

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
                    bubbleContent.setStyle(isCurrentUser ? "-fx-background-color: #ba8b5c; -fx-background-radius: 15;" +
                            "-fx-padding: 8 12; -fx-max-width: 500;"
                            : "-fx-background-color: #8c8271; -fx-background-radius: 15;" +
                                    "-fx-padding: 8 12; -fx-max-width: 500;");

                    // Username label
                    Label userLabel = new Label(message.getUser().getNomUser());
                    userLabel.setStyle(isCurrentUser ? "-fx-font-weight: bold; -fx-text-fill: #d5ccc9;"
                            : "-fx-font-weight: bold; -fx-text-fill: #1c1b1b;");

                    // Message content
                    Label messageLabel = new Label(message.getContent());
                    messageLabel.setWrapText(true);
                    messageLabel.setStyle(isCurrentUser ? "-fx-text-fill: #e8e8e8;" : "-fx-text-fill: #171616;");

                    // Timestamp and edited indicator
                    String timeText = message.getCreatedAt().format(DateTimeFormatter.ofPattern("HH:mm")) +
                            (message.isUpdatedMessage() ? " (edited)" : "");
                    Label timeLabel = new Label(timeText);
                    timeLabel.setStyle("-fx-font-size: 10; -fx-text-fill: " +
                            (isCurrentUser ? "rgba(255,255,255,0.7)" : "#131313"));

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

            // Combine both lists
            List<Matching> allMatchings = FXCollections.observableArrayList();
            allMatchings.addAll(hostMatchings);
            allMatchings.addAll(participantMatchings);

            // Sort matchings by the timestamp of the last message sent
            allMatchings.sort((m1, m2) -> {
                Message lastMessage1 = getLastMessageByMatching(m1.getId());
                Message lastMessage2 = getLastMessageByMatching(m2.getId());

                if (lastMessage1 == null && lastMessage2 == null) {
                    return 0; // Both have no messages
                } else if (lastMessage1 == null) {
                    return 1; // m1 has no messages, so it goes after m2
                } else if (lastMessage2 == null) {
                    return -1; // m2 has no messages, so it goes after m1
                } else {
                    return lastMessage2.getCreatedAt().compareTo(lastMessage1.getCreatedAt());
                }
            });

            // Set the sorted matchings in the ListView
            matchingListView.setItems(FXCollections.observableArrayList(allMatchings));

            // Select the first matching if available
            if (!allMatchings.isEmpty()) {
                matchingListView.getSelectionModel().selectFirst();
            }
        } catch (Exception e) {
            showAlert("Error", "Failed to load matchings: " + e.getMessage());
        }
    }

    public Message getLastMessageByMatching(int matchingId) {
        try {
            List<Message> messages = messageService.getByMatching(matchingId);
            return messages.stream()
                    .max(Comparator.comparing(Message::getCreatedAt))
                    .orElse(null); // Return null if no messages exist
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void loadMatchingMessages(Matching matching) {
        if (matching == null)
            return;

        selectedMatching = matching;

        // Set the Matching ID, name, and subject in the chat header
        matchingIdLabel.setText("ID: " + matching.getId());
        matchingNameLabel.setText(matching.getName());
        matchingSubjectLabel.setText(matching.getSujetRencontre());

        // Load the image dynamically from the Matching object
        try {
            String imagePath = matching.getImage();
            if (imagePath == null || imagePath.trim().isEmpty()) {
                matchingImageView.setImage(new Image(getClass().getResource("/images/avatar.png").toExternalForm()));
            } else {
                matchingImageView.setImage(new Image("file:" + imagePath, true));
            }
        } catch (Exception e) {
            matchingImageView.setImage(new Image(getClass().getResource("/images/avatar.png").toExternalForm()));
        }

        // Load messages for the selected matching
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
            String originalMessage = messageTextArea.getText().trim();
            String censoredMessage = censorMessageWithAPI(originalMessage);

            // Create and save the new message
            Message message = new Message(
                    censoredMessage,
                    currentUser,
                    selectedMatching);
            messageService.ajouter(message);

            // Clear the message input field
            messageTextArea.clear();

            // Reload messages for the selected matching
            loadMatchingMessages(selectedMatching);

            // Re-sort and refresh the matching list
            loadUserMatchings();

        } catch (Exception e) {
            showAlert("Error", "Failed to send message: " + e.getMessage());
        }
    }

    private String censorMessageWithAPI(String message) throws IOException {
        String encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8);
        String apiUrl = "https://www.purgomalum.com/service/json?text=" + encodedMessage + "&fill_char=*";

        HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(3000); // 3 seconds timeout
        connection.setReadTimeout(3000);

        try (InputStream input = connection.getInputStream();
                Scanner scanner = new Scanner(input, StandardCharsets.UTF_8.name())) {

            String response = scanner.useDelimiter("\\A").next();
            JSONObject json = new JSONObject(response);
            return json.getString("result");
        } finally {
            connection.disconnect();
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
    @FXML
    private TextField searchField;

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
            dialog.showAndWait().ifPresent(result -> {
                if (result.getButtonData() == ButtonBar.ButtonData.APPLY) {
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
        Matching selectedMatching = matchingListView.getSelectionModel().getSelectedItem();

        if (selectedMatching == null) {
            showAlert("No Matching Selected", "Please select a matching to edit.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EditMatching.fxml"));
            DialogPane dialogPane = loader.load();

            EditMatchingController controller = loader.getController();
            controller.setCurrentMatching(selectedMatching); // Pass the selected matching

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Edit Matching");

            dialog.showAndWait().ifPresent(result -> {
                if (result.getButtonData() == ButtonBar.ButtonData.APPLY) {
                    System.out.println("APPLY button clicked."); // Debugging
                    if (controller.processResult()) {
                        System.out.println("Changes saved. Reloading matchings."); // Debugging
                        loadUserMatchings(); // Refresh the matching list
                    } else {
                        System.out.println("Failed to save changes."); // Debugging
                    }
                }
            });
        } catch (IOException e) {
            showAlert("Error", "Could not load the edit matching dialog: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void correctMessage(ActionEvent actionEvent) {
        if (messageTextArea.getText().trim().isEmpty()) {
            showAlert("Error", "Please enter a message to correct.");
            return;
        }

        String message = messageTextArea.getText().trim();
        String apiKey = "tEgYuYdN9Axq40To";
        String apiUrl = "https://api.textgears.com/grammar";

        try {
            String query = String.format("text=%s&key=%s",
                    URLEncoder.encode(message, "UTF-8"),
                    URLEncoder.encode(apiKey, "UTF-8"));

            URL url = new URL(apiUrl + "?" + query);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                StringBuilder response = new StringBuilder();
                try (Scanner scanner = new Scanner(conn.getInputStream(), "UTF-8")) {
                    while (scanner.hasNextLine()) {
                        response.append(scanner.nextLine());
                    }
                }

                JSONObject jsonResponse = new JSONObject(response.toString());
                JSONObject responseObj = jsonResponse.getJSONObject("response");
                JSONArray errorsArray = responseObj.getJSONArray("errors");

                // Apply simple corrections
                for (int i = 0; i < errorsArray.length(); i++) {
                    JSONObject error = errorsArray.getJSONObject(i);
                    String bad = error.getString("bad");
                    JSONArray better = error.getJSONArray("better");

                    if (better.length() > 0) {
                        String suggestion = better.getString(0);
                        message = message.replace(bad, suggestion);
                    }
                }

                messageTextArea.setText(message);

            } else {
                showAlert("Error", "Failed to correct the message. HTTP code: " + responseCode);
            }

        } catch (Exception e) {
            showAlert("Error", "An error occurred: " + e.getMessage());
        }
    }

    @FXML
    private void deleteCurrentMatching() {
        if (selectedMatching == null) {
            showAlert("No Matching Selected", "Please select a matching to delete.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Matching");
        confirm.setHeaderText("Are you sure you want to delete this matching?");
        confirm.setContentText("This will also delete all associated messages. This action cannot be undone.");

        confirm.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                try {
                    // Delete all messages associated with the matching
                    List<Message> messages = messageService.getByMatching(selectedMatching.getId());
                    for (Message message : messages) {
                        messageService.supprimer(message.getId());
                    }

                    // Delete the matching
                    matchingService.supprimer(selectedMatching.getId());

                    showAlert("Success", "Matching and its messages deleted successfully!");
                    loadUserMatchings(); // Refresh the matching list
                } catch (Exception e) {
                    showAlert("Error", "Failed to delete matching: " + e.getMessage());
                }
            }
        });
    }

    private void loadUnaccessibleMatchings() {
        try {
            // Get all matchings
            List<Matching> allMatchings = matchingService.recuperer();

            // Filter matchings the user does not have access to
            List<Matching> unaccessibleMatchings = allMatchings.stream()
                    .filter(matching -> matching.getUser().getId() != currentUser.getId() &&
                            matching.getAssessors().stream()
                                    .noneMatch(assessor -> assessor.getId() == currentUser.getId()))
                    .toList();

            // Populate the ComboBox
            ObservableList<Matching> observableList = FXCollections.observableArrayList(unaccessibleMatchings);
            unaccessibleMatchingsComboBox.setItems(observableList);

            // Clear selection to avoid invalid state
            unaccessibleMatchingsComboBox.getSelectionModel().clearSelection();

            // Set the cell factory
            unaccessibleMatchingsComboBox.setCellFactory(param -> new ListCell<>() {
                @Override
                protected void updateItem(Matching matching, boolean empty) {
                    super.updateItem(matching, empty);
                    if (empty || matching == null) {
                        setText(null);
                    } else {
                        setText(matching.getSujetRencontre() + " (Table #" + matching.getNumTable() + ")");
                    }
                }
            });
        } catch (Exception e) {
            showAlert("Error", "Failed to load unaccessible matchings: " + e.getMessage());
        }
    }

    @FXML
    private void handleAddMatching() {
        Matching selectedMatching = unaccessibleMatchingsComboBox.getSelectionModel().getSelectedItem();
        if (selectedMatching == null) {
            showAlert("No Matching Selected", "Please select a matching to add.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Add Matching");
        confirm.setHeaderText("Are you sure you want to add this matching?");
        confirm.setContentText(
                "Subject: " + selectedMatching.getSujetRencontre() + "\nTable: #" + selectedMatching.getNumTable());

        confirm.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                try {
                    // Add the current user as an assessor in the database
                    matchingService.addAssessorToMatching(selectedMatching.getId(), currentUser.getId());

                    // Update the Matching object in memory
                    selectedMatching.getAssessors().add(currentUser);

                    // Refresh the user's matching list and unaccessible matchings
                    loadUserMatchings();
                    loadUnaccessibleMatchings();

                    // Clear the selection only if the list is not empty
                    if (!unaccessibleMatchingsComboBox.getItems().isEmpty()) {
                        unaccessibleMatchingsComboBox.getSelectionModel().clearSelection();
                    }

                    showAlert("Success", "Matching added successfully!");
                } catch (Exception e) {
                    showAlert("Error", "Failed to add matching: " + e.getMessage());
                }
            }
        });
    }

    public void botAI(ActionEvent actionEvent) {
        // Path to your Python script
        String pythonScriptPath = "src/main/resources/bot/bot.py";

        // Command to execute the Python script
        String[] command = { "python", pythonScriptPath };

        try {
            // Create a process builder to execute the Python script
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true);

            // Start the process
            Process process = processBuilder.start();

            // Read the output of the Python script
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line); // Print the output to the console
            }

            // Wait for the process to complete
            int exitCode = process.waitFor();
            System.out.println("Python script exited with code: " + exitCode);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void navigateToAccueil(ActionEvent actionEvent) {

    }

    public void navigateToRencontres(ActionEvent actionEvent) {

    }

    public void navigateToEvenements(ActionEvent actionEvent) {

    }

    public void navigateToForum(ActionEvent actionEvent) {

    }

    public void navigateToBoutique(ActionEvent actionEvent) {

    }

    public void navigateToConnexion(ActionEvent actionEvent) {

    }

}