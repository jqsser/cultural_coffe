package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class SupprimerProduit {
    @FXML
    private Text messageText;


    @FXML
    private Button confirmBtn;
    @FXML
    private Button cancelBtn;

    private Runnable onConfirmAction;

    @FXML
    private void initialize() {
        confirmBtn.setOnAction(this::handleConfirm);
        cancelBtn.setOnAction(this::handleCancel);
    }

    public void setMessage(String message) {
        messageText.setText(message);
    }

    public void setOnConfirmAction(Runnable onConfirmAction) {
        this.onConfirmAction = onConfirmAction;
    }

    private void handleConfirm(ActionEvent event) {
        if (onConfirmAction != null) {
            onConfirmAction.run();  // Exécute l'action de suppression
        }
        closePopup();
    }

    private void handleCancel(ActionEvent event) {
        closePopup();
    }

    private void closePopup() {
        Stage stage = (Stage) confirmBtn.getScene().getWindow();
        stage.close();  // Ferme la pop-up
    }

}

