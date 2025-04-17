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

    private Matching currentMatching;
    private ChatController chatController;
    private MatchingService matchingService = new MatchingService();

    public void setCurrentMatching(Matching matching) {
        this.currentMatching = matching;
        nameField.setText(matching.getName());
        subjectField.setText(matching.getSujetRencontre());
        tableNumberField.setText(String.valueOf(matching.getNumTable()));
        participantCountField.setText(String.valueOf(matching.getNbrPersonneMatchy()));
    }

    public void setChatController(ChatController controller) {
        this.chatController = controller;
    }

    public boolean processResult() {
        try {
            currentMatching.setName(nameField.getText());
            currentMatching.setSujetRencontre(subjectField.getText());
            currentMatching.setNumTable(Integer.parseInt(tableNumberField.getText()));
            currentMatching.setNbrPersonneMatchy(Integer.parseInt(participantCountField.getText()));

            matchingService.modifier(currentMatching);
            return true;
        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter valid numbers for table number and participant count");
            return false;
        } catch (Exception e) {
            showAlert("Error", "Failed to update matching: " + e.getMessage());
            return false;
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