package Controllers;

import entity.Commande;
import entity.Produit;
import entity.User;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import service.CommandeService;
import service.ProduitService;

import java.sql.SQLException;

public class AjouterCommande {
    @FXML private TextField quantityField;
    @FXML private Label unitPriceLabel;
    @FXML private Label totalPriceLabel;
    @FXML private VBox validationContainer;
    @FXML private Label validationMessage;

    private Produit produit;
    private Runnable onSuccessCallback;
    private final ProduitService produitService = new ProduitService();
    private final CommandeService commandeService = new CommandeService();

    @FXML
    public void initialize() {
        quantityField.setText("1");
        validationMessage.setText("");
        validationMessage.setStyle("-fx-text-fill: red;");

        quantityField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) {
                quantityField.setText(oldVal);
            } else {
                calculateTotal();
                validateInput();
            }
        });
    }

    public void setOnSuccess(Runnable callback) {
        this.onSuccessCallback = callback;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
        unitPriceLabel.setText(String.format("%.2f TND", produit.getPrix_produit()));
        calculateTotal();
    }

    @FXML
    private void incrementQuantity() {
        int quantity = getQuantity() + 1;
        quantityField.setText(String.valueOf(quantity));
        validateInput();
    }

    @FXML
    private void decrementQuantity() {
        int quantity = getQuantity();
        if (quantity > 1) {
            quantityField.setText(String.valueOf(quantity - 1));
            validateInput();
        }
    }

    @FXML
    private void calculateTotal() {
        try {
            int quantity = getQuantity();
            double total = quantity * produit.getPrix_produit();
            totalPriceLabel.setText(String.format("%.2f TND", total));
        } catch (Exception e) {
            totalPriceLabel.setText("0 TND");
        }
    }

    private void validateInput() {
        int quantity = getQuantity();

        if (quantity <= 0) {
            showValidationError("La quantité doit être supérieur à 0");
            return;
        }

        if (produit != null && quantity > produit.getStock_produit()) {
            showValidationError("Stock insuffisant. Disponible: " + produit.getStock_produit());
            return;
        }

        clearValidationError();
    }

    private void showValidationError(String message) {
        validationMessage.setText(message);
        validationContainer.setVisible(true);
    }

    private void clearValidationError() {
        validationMessage.setText("");
        validationContainer.setVisible(false);
    }

    @FXML
    private void confirmOrder() {
        validateInput();

        if (!validationMessage.getText().isEmpty()) {
            return;
        }

        try {
            int quantity = getQuantity();
            enregistrerCommande(quantity);
            closePopup();
        } catch (Exception e) {
            showValidationError("Une erreur est survenue: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void enregistrerCommande(int quantity) throws SQLException {
        produit.setStock_produit(produit.getStock_produit() - quantity);
        produitService.modifierProduit(produit);

        Commande commande = new Commande();
        commande.setQauntite_produit(quantity);
        commande.setPrix_total_commande(quantity * produit.getPrix_produit());
        commande.setProduit(produit);
        commande.setUser(getCurrentUser());

        commandeService.ajouterCommande(commande);

        if (onSuccessCallback != null) {
            onSuccessCallback.run();
        }

        showSuccess("Commande enregistrée avec succès!");
    }

    @FXML
    private void closePopup() {
        ((Stage) quantityField.getScene().getWindow()).close();
    }

    private int getQuantity() {
        try {
            return Integer.parseInt(quantityField.getText());
        } catch (NumberFormatException e) {
            return 1;
        }
    }

    private User getCurrentUser() {
        User user = new User();
        user.setId(15);
        return user;
    }

    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}