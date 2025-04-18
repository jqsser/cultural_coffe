package Controllers;

import entity.Produit;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ModifierProduitBack {


    @FXML
    private ComboBox<String> etatProduitComboBox;

    private Produit produit;

    public void setProduit(Produit produit) {
        this.produit = produit;
        // Remplir les champs avec les informations actuelles du produit

        etatProduitComboBox.setValue(produit.getEtat_produit() == 1 ? "oui" : "non");
    }
    // Action pour annuler la modification

    @FXML
    private void modifierProduit() {
        // Récupérer les valeurs des champs modifiés

        String etatProduit = etatProduitComboBox.getValue();

        // Mettre à jour les propriétés du produit

        produit.setEtat_produit(etatProduit.equals("oui") ? 1 : 0);

        // Confirmer la modification et fermer la fenêtre
        if (onConfirmAction != null) {
            onConfirmAction.run();
        }

        // Fermer le pop-up
        Stage stage = (Stage) etatProduitComboBox.getScene().getWindow();
        stage.close();
    }

    private Runnable onConfirmAction;

    public void setOnConfirmAction(Runnable onConfirmAction) {
        this.onConfirmAction = onConfirmAction;
    }
}
