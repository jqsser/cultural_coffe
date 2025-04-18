package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tn.esprit.controllers.AfficherUser;
import tn.esprit.entities.User;
import tn.esprit.services.UserService;

import java.io.File;
import java.util.Date;

public class AddUserControllerDash {

    @FXML private TextField nameField;
    @FXML private TextField lastnameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private ComboBox<String> roleComboBox;
    @FXML private TextField photoField;

    private AfficherUser afficherUserController;

    public void setAfficherUserController(AfficherUser afficherUserController) {
        this.afficherUserController = afficherUserController;
    }

    private final UserService userService = new UserService();

    @FXML
    public void initialize() {
        roleComboBox.getItems().addAll("Client", "Admin");
    }

    @FXML
    private void handleBrowse() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a Photo");
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            photoField.setText(selectedFile.getAbsolutePath());
        }
    }

    @FXML
    private void handleAddUser() {
        String name = nameField.getText();
        String lastname = lastnameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String selectedRole = roleComboBox.getValue();
        String role;
        if ("Admin".equals(selectedRole)) {
            role = "[\"ROLE_ADMIN\"]";
        } else {
            role = "[\"ROLE_CLIENT\"]";
        }
        String photo = photoField.getText();

        // Input validation
        if (name.isEmpty() || lastname.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || selectedRole == null || photo.isEmpty()) {
            showAlert("Error", "All fields are required.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert("Error", "Passwords do not match.");
            return;
        }

        try {
            User user = new User(name, lastname, role, password, email, photo, new Date(), false, false);
            userService.ajouter(user);
            showAlert("Success", "User added successfully.");

            // Close the window
            Stage stage = (Stage) nameField.getScene().getWindow();
            stage.close();

            if (afficherUserController != null) {
                afficherUserController.refreshGrid();
            }

        } catch (Exception e) {
            showAlert("Error", "Failed to add user: " + e.getMessage());
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
