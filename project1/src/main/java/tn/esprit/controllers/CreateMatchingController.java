package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tn.esprit.entities.Matching;
import tn.esprit.entities.User;
import tn.esprit.services.MatchingService;

import java.io.File;

public class CreateMatchingController {
    @FXML private TextField nameField;
    @FXML private TextField subjectField;
    @FXML private TextField tableNumberField;
    @FXML private TextField participantCountField;
    @FXML private TextField imagePathField;

    private User currentUser;
    private MatchingService matchingService = new MatchingService();
    private Stage dialogStage;
    private boolean createClicked = false;
    private Object chatController;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public boolean isCreateClicked() {
        return createClicked;
    }
    public void setChatController(ChatController chatController) {
        this.chatController = chatController;
    }

    @FXML
    private void browseImage() {  // Method name matches FXML
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Matching Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        File selectedFile = fileChooser.showOpenDialog(dialogStage);
        if (selectedFile != null) {
            imagePathField.setText(selectedFile.getAbsolutePath());
        }
    }

    @FXML
    public void handleCreate() {
        if (validateInput()) {
            Matching matching = createMatchingFromInput();
            try {
                matchingService.ajouter(matching);
                createClicked = true;
                // The dialog will be closed by the Dialog's button handling
            } catch (Exception e) {
                showAlert("Database Error", "Failed to save matching: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    private boolean validateInput() {
        StringBuilder errorMessage = new StringBuilder();

        if (nameField.getText() == null || nameField.getText().trim().isEmpty()) {
            errorMessage.append("Name is required!\n");
        }

        if (subjectField.getText() == null || subjectField.getText().trim().isEmpty()) {
            errorMessage.append("Subject is required!\n");
        }

        if (tableNumberField.getText() == null || tableNumberField.getText().trim().isEmpty()) {
            errorMessage.append("Table number is required!\n");
        } else {
            try {
                Integer.parseInt(tableNumberField.getText().trim());
            } catch (NumberFormatException e) {
                errorMessage.append("Table number must be a valid integer!\n");
            }
        }

        if (participantCountField.getText() == null || participantCountField.getText().trim().isEmpty()) {
            errorMessage.append("Participant count is required!\n");
        } else {
            try {
                Integer.parseInt(participantCountField.getText().trim());
            } catch (NumberFormatException e) {
                errorMessage.append("Participant count must be a valid integer!\n");
            }
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            showAlert("Invalid Fields", errorMessage.toString());
            return false;
        }
    }

    private Matching createMatchingFromInput() {
        Matching matching = new Matching(
                nameField.getText().trim(),
                subjectField.getText().trim(),
                Integer.parseInt(tableNumberField.getText().trim()),
                Integer.parseInt(participantCountField.getText().trim()),
                imagePathField.getText().trim()
        );
        matching.setUser(currentUser);
        return matching;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(dialogStage);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }



}