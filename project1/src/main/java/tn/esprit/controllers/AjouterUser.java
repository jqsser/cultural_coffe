package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.esprit.entities.User;
import tn.esprit.services.UserService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AjouterUser {

    @FXML
    private TextField txtLastname;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtemail;

    @FXML
    private PasswordField txtpassword;

    @FXML
    private PasswordField txtConfirmPassword;

    @FXML
    void AjouterUser(ActionEvent event) {

        // Retrieve user input values
        String Name = txtName.getText();
        String LastName = txtLastname.getText();
        String Password = txtpassword.getText();
        String ConfirmPassword = txtConfirmPassword.getText();
        String Email = txtemail.getText();
        String roles = "[\"ROLE_CLIENT\"]";   // Default role for the user
        String photo = null;                  // No photo provided
        java.util.Date dateCreation = new java.util.Date(); // Current date
        boolean isBanned = false;             // Default not banned
        boolean isVerified = true;

        // Input validation
        if (Name.isEmpty() || LastName.isEmpty() || Email.isEmpty() || Password.isEmpty() || ConfirmPassword.isEmpty()) {
            showAlert("Error", "All fields are required.");
            return;
        }

        if (!Password.equals(ConfirmPassword)) {
            showAlert("Error", "Password and Confirm Password do not match.");
            return;
        }

        if (!isValidEmail(Email)) {
            showAlert("Error", "Invalid email format.");
            return;
        }

        if (Password.length() < 6) {
            showAlert("Error", "Password must be at least 6 characters long.");
            return;
        }

        // Create a new user object
        User usr = new User(Name, LastName, roles, Password, Email, photo, dateCreation, isBanned, isVerified);
        UserService ps = new UserService();
        try {
            ps.ajouter(usr);

            // Load and show Profile window
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ProfilUser.fxml"));
            Parent root = loader.load();

            tn.esprit.controllers.ProfilUserController controller = loader.getController();
            controller.setUser(usr);

            Stage profileStage = new Stage();
            profileStage.setTitle("User Profile");
            profileStage.setScene(new javafx.scene.Scene(root));
            profileStage.show();

            ((javafx.scene.Node) (event.getSource())).getScene().getWindow().hide();

        } catch (SQLException | IOException e) {
            showAlert("Error", "Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Method to validate email format
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // Method to show alert messages
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
