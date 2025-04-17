package tn.esprit.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import tn.esprit.entities.Matching;
import tn.esprit.entities.User;
import tn.esprit.services.MatchingService;
import tn.esprit.services.UserService;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

public class MatchingController {

    @FXML
    private MenuButton AssessortButton;

    @FXML
    private TextField NameSub;

    @FXML
    private MenuButton UaserButton;

    @FXML
    private TextField assessors;

    @FXML
    private TextField messages;

    @FXML
    private TextField nbrPersonneMatchy;

    @FXML
    private Button okB;

    @FXML
    private TextField numTable;

    @FXML
    private TextField sujet;

    @FXML
    private TextField user;

    @FXML
    private ImageView imageView;

    @FXML
    private ListView<Matching> matchingListView;

    private MatchingService matchingService = new MatchingService();
    private UserService userService = new UserService();
    private String imagePath;

    @FXML
    public void initialize() {
        try {
            // Load users for dropdown menus
            loadUsers();

            // Load all matchings in the list view
            refreshMatchingList();

            // Set up selection listener
            matchingListView.getSelectionModel().selectedItemProperty().addListener(
                    (observable, oldValue, newValue) -> showMatchingDetails(newValue));
        } catch (SQLException e) {
            showAlert("Database Error", "Error loading data: " + e.getMessage());
        }
    }

    private void loadUsers() throws SQLException {
        List<User> users = userService.recuperer();
        ObservableList<MenuItem> userItems = FXCollections.observableArrayList();
        ObservableList<MenuItem> assessorItems = FXCollections.observableArrayList();

        for (User u : users) {
            MenuItem userItem = new MenuItem(u.getNomUser());
            userItem.setOnAction(e -> user.setText(u.getNomUser()));

            MenuItem assessorItem = new MenuItem(u.getNomUser());
            assessorItem.setOnAction(e -> assessors.setText(assessors.getText() +
                    (assessors.getText().isEmpty() ? "" : ", ") + u.getNomUser()));

            userItems.add(userItem);
            assessorItems.add(assessorItem);
        }

        UaserButton.getItems().setAll(userItems);
        AssessortButton.getItems().setAll(assessorItems);
    }

    private void refreshMatchingList() throws SQLException {
        List<Matching> matchings = matchingService.recuperer();
        matchingListView.getItems().setAll(matchings);
    }

    private void showMatchingDetails(Matching matching) {
        if (matching != null) {
            NameSub.setText(matching.getName());
            sujet.setText(matching.getSujetRencontre());
            numTable.setText(String.valueOf(matching.getNumTable()));
            nbrPersonneMatchy.setText(String.valueOf(matching.getNbrPersonneMatchy()));
            user.setText(matching.getUser().getNomUser());

            // Load image if exists
            if (matching.getImage() != null && !matching.getImage().isEmpty()) {
                File file = new File(matching.getImage());
                if (file.exists()) {
                    imageView.setImage(new Image(file.toURI().toString()));
                }
            }

            // Load assessors (you'll need to implement this in your service)
            // assessors.setText(...);
        }
    }

    @FXML
    void handleImageUpload(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Matching Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        File selectedFile = fileChooser.showOpenDialog(imageView.getScene().getWindow());
        if (selectedFile != null) {
            imagePath = selectedFile.getAbsolutePath();
            imageView.setImage(new Image(selectedFile.toURI().toString()));
        }
    }

    @FXML
    void handleSave(ActionEvent event) {
        try {
            if (validateInput()) {
                Matching matching = new Matching();
                matching.setName(NameSub.getText());
                matching.setSujetRencontre(sujet.getText());
                matching.setNumTable(Integer.parseInt(numTable.getText()));
                matching.setNbrPersonneMatchy(Integer.parseInt(nbrPersonneMatchy.getText()));
                matching.setImage(imagePath);

                // Get user from the text field (you might need to implement a lookup)
                // This is simplified - you should implement proper user lookup
                User hostUser = userService.getByUsername(user.getText());
                matching.setUser(hostUser);

                // Save the matching
                matchingService.ajouter(matching);

                showAlert("Success", "Matching saved successfully!");
                refreshMatchingList();
                clearFields();
            }
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Please enter valid numbers for table number and person count");
        } catch (SQLException e) {
            showAlert("Database Error", "Error saving matching: " + e.getMessage());
        }
    }

    @FXML
    void handleUpdate(ActionEvent event) {
        Matching selectedMatching = matchingListView.getSelectionModel().getSelectedItem();
        if (selectedMatching == null) {
            showAlert("Selection Error", "Please select a matching to update");
            return;
        }

        try {
            if (validateInput()) {
                selectedMatching.setName(NameSub.getText());
                selectedMatching.setSujetRencontre(sujet.getText());
                selectedMatching.setNumTable(Integer.parseInt(numTable.getText()));
                selectedMatching.setNbrPersonneMatchy(Integer.parseInt(nbrPersonneMatchy.getText()));

                if (imagePath != null) {
                    selectedMatching.setImage(imagePath);
                }

                // Update the matching
                matchingService.modifier(selectedMatching);

                showAlert("Success", "Matching updated successfully!");
                refreshMatchingList();
            }
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Please enter valid numbers for table number and person count");
        } catch (SQLException e) {
            showAlert("Database Error", "Error updating matching: " + e.getMessage());
        }
    }

    @FXML
    void handleDelete(ActionEvent event) {
        Matching selectedMatching = matchingListView.getSelectionModel().getSelectedItem();
        if (selectedMatching == null) {
            showAlert("Selection Error", "Please select a matching to delete");
            return;
        }

        try {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirm Delete");
            confirm.setHeaderText("Delete Matching");
            confirm.setContentText("Are you sure you want to delete this matching?");

            if (confirm.showAndWait().get() == ButtonType.OK) {
                matchingService.supprimer(selectedMatching.getId());
                showAlert("Success", "Matching deleted successfully!");
                refreshMatchingList();
                clearFields();
            }
        } catch (SQLException e) {
            showAlert("Database Error", "Error deleting matching: " + e.getMessage());
        }
    }

    private boolean validateInput() {
        if (NameSub.getText().isEmpty() || sujet.getText().isEmpty() ||
                numTable.getText().isEmpty() || nbrPersonneMatchy.getText().isEmpty()) {
            showAlert("Input Error", "Please fill all required fields");
            return false;
        }

        try {
            Integer.parseInt(numTable.getText());
            Integer.parseInt(nbrPersonneMatchy.getText());
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Table number and person count must be numbers");
            return false;
        }

        return true;
    }

    private void clearFields() {
        NameSub.clear();
        sujet.clear();
        numTable.clear();
        nbrPersonneMatchy.clear();
        messages.clear();
        user.clear();
        assessors.clear();
        imageView.setImage(null);
        imagePath = null;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}