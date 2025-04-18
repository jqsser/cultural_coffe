package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tn.esprit.entities.Matching;
import tn.esprit.entities.User;
import tn.esprit.services.MatchingService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class CreateMatchingController {

    @FXML private TextField nameField;
    @FXML private TextField subjectField;
    @FXML private TextField tableNumberField;
    @FXML private TextField participantCountField;
    @FXML private TextField imagePathField;
    @FXML private Label errorLabel;
    @FXML private DialogPane dialogPane;

    private Stage dialogStage;
    private boolean createClicked = false;
    private MatchingService matchingService = new MatchingService();
    private static final String IMAGE_UPLOAD_DIRECTORY = "C:\\xampp\\htdocs\\imageC";
    private User currentUser;
    private ChatController chatController;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public boolean isCreateClicked() {
        return createClicked;
    }

    @FXML
    private void initialize() {
        // Ensure dialogPane is not null before using it
        if (dialogPane != null) {
            Button createButton = (Button) dialogPane.lookupButton(ButtonType.APPLY);
            if (createButton != null) {
                createButton.setOnAction(event -> handleCreate());
            }
        } else {
            System.err.println("DialogPane is not properly initialized.");
        }
    }

    @FXML
    private void browseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Matching Image");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));

        File selectedFile = fileChooser.showOpenDialog(dialogStage);
        if (selectedFile != null) {
            try {
                File uploadDir = new File(IMAGE_UPLOAD_DIRECTORY);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }

                String uniqueFileName = System.currentTimeMillis() + "_" + selectedFile.getName();
                File destinationFile = new File(uploadDir, uniqueFileName);

                Files.copy(selectedFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                imagePathField.setText("imageC/" + uniqueFileName);
            } catch (IOException e) {
                errorLabel.setText("Failed to upload image: " + e.getMessage());
            }
        }
    }

    void handleCreate() {
        if (validateInput()) {
            try {
                Matching matching = new Matching(
                        nameField.getText().trim(),
                        subjectField.getText().trim(),
                        Integer.parseInt(tableNumberField.getText().trim()),
                        Integer.parseInt(participantCountField.getText().trim()),
                        imagePathField.getText().trim()
                );

                if (currentUser != null) {
                    matching.setUser(currentUser);
                } else {
                    errorLabel.setText("Current user is not set. Please log in again.");
                    return;
                }

                matchingService.ajouter(matching);
                createClicked = true;
                dialogStage.close();
            } catch (Exception e) {
                errorLabel.setText("Failed to save matching: " + e.getMessage());
            }
        }
    }

    private boolean validateInput() {
        StringBuilder errorMessage = new StringBuilder();

        if (nameField.getText().trim().isEmpty()) {
            errorMessage.append("Name is required!\n");
        } else if (nameField.getText().trim().length() < 3 || nameField.getText().trim().length() > 20) {
            errorMessage.append("Name must be between 3 and 20 characters!\n");
        }

        if (subjectField.getText().trim().isEmpty()) {
            errorMessage.append("Subject is required!\n");
        } else if (subjectField.getText().trim().length() < 3 || subjectField.getText().trim().length() > 20) {
            errorMessage.append("Subject must be between 3 and 20 characters!\n");
        }

        try {
            int tableNumber = Integer.parseInt(tableNumberField.getText().trim());
            if (tableNumber < 1 || tableNumber > 40) {
                errorMessage.append("Table number must be between 1 and 40!\n");
            }
        } catch (NumberFormatException e) {
            errorMessage.append("Table number must be a valid integer!\n");
        }

        try {
            int participantCount = Integer.parseInt(participantCountField.getText().trim());
            if (participantCount < 2 || participantCount > 10) {
                errorMessage.append("Participant count must be between 2 and 10!\n");
            }
        } catch (NumberFormatException e) {
            errorMessage.append("Participant count must be a valid integer!\n");
        }

        if (errorMessage.length() == 0) {
            errorLabel.setText("");
            return true;
        } else {
            errorLabel.setText(errorMessage.toString());
            return false;
        }
    }

    public void setChatController(ChatController chatController) {
        this.chatController = chatController;
    }
}