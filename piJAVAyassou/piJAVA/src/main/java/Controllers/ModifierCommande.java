package Controllers;

import entity.Commande;
import entity.User;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import service.CommandeService;

import java.sql.SQLException;

public class ModifierCommande {
    @FXML private TextField quantityField;
    @FXML private Label unitPriceLabel;
    @FXML private Label totalPriceLabel;

    private Commande commande;
    private Runnable refreshCallback;
    private final CommandeService commandeService = new CommandeService();

    public void setCommande(Commande commande) {
        this.commande = commande;
        quantityField.setText(String.valueOf(commande.getQuantite_produit()));
        unitPriceLabel.setText(String.format("%.2f TND", commande.getProduit().getPrix_produit()));
        calculateTotal();
    }

    public void setRefreshCallback(Runnable callback) {
        this.refreshCallback = callback;
    }

    @FXML
    private void incrementQuantity() {
        int quantity = Integer.parseInt(quantityField.getText()) + 1;
        quantityField.setText(String.valueOf(quantity));
        calculateTotal();
    }

    @FXML
    private void decrementQuantity() {
        int quantity = Integer.parseInt(quantityField.getText());
        if (quantity > 1) {
            quantityField.setText(String.valueOf(quantity - 1));
            calculateTotal();
        }
    }

    @FXML
    private void calculateTotal() {
        try {
            int quantity = Integer.parseInt(quantityField.getText());
            double total = quantity * commande.getProduit().getPrix_produit();
            totalPriceLabel.setText(String.format("%.2f TND", total));
        } catch (Exception e) {
            totalPriceLabel.setText("0.00 TND");
        }
    }

    @FXML
    private void updateCommande() {
        try {
            int newQuantity = Integer.parseInt(quantityField.getText());
            double newTotal = newQuantity * commande.getProduit().getPrix_produit();

            // Vérifier que l'utilisateur est bien associé
            if (commande.getUser() == null) {
                showAlert("Erreur", "Aucun utilisateur associé à cette commande");
                return;
            }

            // Mettre à jour la commande
            commande.setQauntite_produit(newQuantity);
            commande.setPrix_total_commande(newTotal);

            // Enregistrer les modifications
            commandeService.modifierCommande(commande);

            if (refreshCallback != null) {
                refreshCallback.run();
            }

            showAlert("Succès", "Commande modifiée avec succès");
            closePopup();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur SQL", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur inattendue est survenue");
        }
    }

    @FXML
    private void closePopup() {
        ((Stage) quantityField.getScene().getWindow()).close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}