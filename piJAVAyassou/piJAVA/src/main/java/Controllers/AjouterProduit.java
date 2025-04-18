package Controllers;

import entity.Produit;
import entity.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import service.ProduitService;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.scene.control.Label;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class AjouterProduit {

    private ProduitService ps = new ProduitService();
    // Navigation methods
    @FXML
    private void navigateToAccueil(ActionEvent event) { loadPage("/Accueil.fxml", event); }
    @FXML
    private void navigateToRencontres(ActionEvent event) { loadPage("/Rencontres.fxml", event); }
    @FXML
    private void navigateToEvenements(ActionEvent event) { loadPage("/AfficherEvenementUser.fxml", event); }
    @FXML
    private void navigateToForum(ActionEvent event) { loadPage("/Forum.fxml", event); }
    @FXML
    private void navigateToBoutique(ActionEvent event) { loadPage("/AfficherProduit.fxml", event); }
    @FXML
    private void navigateToConnexion(ActionEvent event) { loadPage("/Connexion.fxml", event); }

    private void loadPage(String fxmlPath, ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private TextField descriptionTF;

    @FXML
    private ImageView img1TF;

    @FXML
    private ImageView img2TF;

    @FXML
    private ImageView img3TF;

    @FXML
    private ImageView img4TF;

    @FXML
    private TextField nomTF;

    @FXML
    private TextField prixTF;

    @FXML
    private TextField stockTF;

    @FXML
    private ComboBox<String> typeTF;

    // Labels pour les erreurs
    @FXML
    private Label nomError;
    @FXML
    private Label prixError;
    @FXML
    private Label descriptionError;
    @FXML
    private Label stockError;
    @FXML
    private Label typeError;
    @FXML
    private Label imageError;

    private String imagePath1;
    private String imagePath2;
    private String imagePath3;
    private String imagePath4;

    @FXML
    void ajouterProduit(ActionEvent event) throws IOException {
        try {
            // Réinitialiser les messages d'erreur
            resetErrorMessages();

            // === CONTRÔLE DE SAISIE ===
            String nom = nomTF.getText().trim();
            String description = descriptionTF.getText().trim();
            String prixText = prixTF.getText().trim();
            String stockText = stockTF.getText().trim();
            String type = typeTF.getValue();

            boolean isValid = true;

            if (nom.isEmpty()) {
                nomError.setText("Le champ 'Nom' est requis.");
                isValid = false;
            } else if (nom.length() > 50) {
                nomError.setText("Le nom ne doit pas dépasser 50 caractères.");
                isValid = false;
            }

            if (description.isEmpty()) {
                descriptionError.setText("La description est requise.");
                isValid = false;
            } else if (description.length() > 255) {
                descriptionError.setText("La description ne doit pas dépasser 255 caractères.");
                isValid = false;
            }

            if (prixText.isEmpty()) {
                prixError.setText("Le prix est requis.");
                isValid = false;
            } else if (!prixText.matches("\\d+")) {
                prixError.setText("Le prix doit être un entier positif.");
                isValid = false;
            } else {
                int prix = Integer.parseInt(prixText);
                if (prix < 0 || prix > 10000) {
                    prixError.setText("Le prix doit être entre 0 et 10000.");
                    isValid = false;
                }
            }

            if (stockText.isEmpty()) {
                stockError.setText("Le stock est requis.");
                isValid = false;
            } else if (!stockText.matches("\\d+")) {
                stockError.setText("Le stock doit être un entier positif.");
                isValid = false;
            } else {
                int stock = Integer.parseInt(stockText);
                if (stock < 0 || stock > 1000) {
                    stockError.setText("Le stock doit être entre 0 et 1000.");
                    isValid = false;
                }
            }

            if (type == null || type.trim().isEmpty()) {
                typeError.setText("Veuillez sélectionner un type de produit.");
                isValid = false;
            }

            // Vérification qu'au moins une image est sélectionnée
            if (imagePath1 == null && imagePath2 == null && imagePath3 == null && imagePath4 == null) {
                imageError.setText("Veuillez sélectionner au moins une image.");
                isValid = false;
            }

            if (!isValid) {
                return;
            }

            // === AJOUT DU PRODUIT ===
            int etat = 0;
            User user = new User();
            user.setId(15);

            ps.ajouterProduit(new Produit(
                    Integer.parseInt(prixText),
                    Integer.parseInt(stockText),
                    type,
                    etat,
                    nom,
                    description,
                    imagePath1,
                    imagePath2,
                    imagePath3,
                    imagePath4,
                    user
            ));

            // Réinitialiser le formulaire après l'ajout
            resetForm();

        } catch (SQLException e) {
            imageError.setText("Erreur lors de l'ajout du produit : " + e.getMessage());
        }
    }

    private void resetErrorMessages() {
        nomError.setText("");
        prixError.setText("");
        descriptionError.setText("");
        stockError.setText("");
        typeError.setText("");
        imageError.setText("");
    }

    private void resetForm() {
        nomTF.setText("");
        prixTF.setText("");
        descriptionTF.setText("");
        stockTF.setText("");
        typeTF.setValue(null);
        img1TF.setImage(null);
        img2TF.setImage(null);
        img3TF.setImage(null);
        img4TF.setImage(null);
        imagePath1 = null;
        imagePath2 = null;
        imagePath3 = null;
        imagePath4 = null;
        resetErrorMessages();
    }

    @FXML
    void selectimg1(ActionEvent event) {
        File file = new FileChooser().showOpenDialog(null);
        if (file != null) {
            imagePath1 = file.toURI().toString();
            img1TF.setImage(new Image(imagePath1));
            imageError.setText("");
        }
    }

    @FXML
    void selectimg2(ActionEvent event) {
        File file = new FileChooser().showOpenDialog(null);
        if (file != null) {
            imagePath2 = file.toURI().toString();
            img2TF.setImage(new Image(imagePath2));
            imageError.setText("");
        }
    }

    @FXML
    void selectimg3(ActionEvent event) {
        File file = new FileChooser().showOpenDialog(null);
        if (file != null) {
            imagePath3 = file.toURI().toString();
            img3TF.setImage(new Image(imagePath3));
            imageError.setText("");
        }
    }

    @FXML
    void selectimg4(ActionEvent event) {
        File file = new FileChooser().showOpenDialog(null);
        if (file != null) {
            imagePath4 = file.toURI().toString();
            img4TF.setImage(new Image(imagePath4));
            imageError.setText("");
        }
    }
}