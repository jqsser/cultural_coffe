package Controllers;

import entity.Commande;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class SupprimerCommande {
    @FXML private Text messageText;
    @FXML private Text commandeDetails;
    @FXML private Button confirmBtn;
    @FXML private Button cancelBtn;

    private Commande commande;
    private Runnable onConfirmAction;

    @FXML
    public void initialize() {
        // Initialisation sécurisée
        if (confirmBtn != null) {
            confirmBtn.setOnAction(event -> handleConfirm());
        }
        if (cancelBtn != null) {
            cancelBtn.setOnAction(event -> handleCancel());
        }
    }

    public void setCommande(Commande commande) {
        this.commande = commande;
        if (commande != null && commande.getProduit() != null) {
            String details = String.format("Commande #%d - %s (x%d) - %.2f TND",
                    commande.getId_commande(),
                    commande.getProduit().getNom_produit(),
                    commande.getQuantite_produit(),
                    commande.getPrix_total_commande());
            commandeDetails.setText(details);
        }
    }

    public void setOnConfirmAction(Runnable onConfirmAction) {
        this.onConfirmAction = onConfirmAction;
    }

    private void handleConfirm() {
        if (onConfirmAction != null) {
            onConfirmAction.run();
        }
        closePopup();
    }

    private void handleCancel() {
        closePopup();
    }

    private void closePopup() {
        Stage stage = (Stage) confirmBtn.getScene().getWindow();
        stage.close();
    }
}