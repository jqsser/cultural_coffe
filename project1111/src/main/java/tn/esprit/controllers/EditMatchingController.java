package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import tn.esprit.entities.Matching;
import tn.esprit.services.MatchingService;

public class EditMatchingController {

    @FXML private TextField nameField;
    @FXML private TextField subjectField;
    @FXML private TextField tableNumberField;
    @FXML private TextField participantCountField;
    @FXML private Label errorLabel;

    private Matching currentMatching;
    private MatchingService matchingService = new MatchingService();
    private ChatController chatController;

    public void setCurrentMatching(Matching matching) {
        if (matching == null) {
            showError("No matching data provided.");
            return;
        }

        this.currentMatching = matching;
        nameField.setText(matching.getName());
        subjectField.setText(matching.getSujetRencontre());
        tableNumberField.setText(String.valueOf(matching.getNumTable()));
        participantCountField.setText(String.valueOf(matching.getNbrPersonneMatchy()));
    }

    public boolean processResult() {
        if (validateInput()) {
            try {
                System.out.println("Saving updated matching: " + currentMatching); // Debugging
                currentMatching.setName(nameField.getText().trim());
                currentMatching.setSujetRencontre(subjectField.getText().trim());
                currentMatching.setNumTable(Integer.parseInt(tableNumberField.getText().trim()));
                currentMatching.setNbrPersonneMatchy(Integer.parseInt(participantCountField.getText().trim()));

                matchingService.modifier(currentMatching);
                System.out.println("Matching successfully updated."); // Debugging
                return true;
            } catch (Exception e) {
                showError("Failed to update matching: " + e.getMessage());
                e.printStackTrace();
                return false;
            }
        }
        System.out.println("Validation failed."); // Debugging
        return false;
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

    private void showError(String message) {
        errorLabel.setText(message);
    }
    public void setChatController(ChatController chatController) {
        this.chatController = chatController;
    }


}