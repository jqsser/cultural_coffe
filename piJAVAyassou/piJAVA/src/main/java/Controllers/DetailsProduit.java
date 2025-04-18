package Controllers;

import entity.Produit;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Paths;

public class DetailsProduit {
    // Chemins constants
    private static final String UPLOAD_DIR = "uploads/images/";
    private static final String DEFAULT_IMAGE = "/images/default-product.png";

    @FXML private TextField productNameField;
    @FXML private TextField productPriceField;
    @FXML private TextArea productDescriptionField;
    @FXML private ImageView productImageView;
    @FXML private ImageView productImage2View;
    @FXML private ImageView productImage3View;
    @FXML private ImageView productImage4View;

    private Produit produit;

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

    public void setProduit(Produit produit) {
        this.produit = produit;
        updateUI();
    }

    private void updateUI() {
        if (produit == null) return;

        // Debug pour vérifier les données
        System.out.println("Chargement du produit: " + produit.getNom_produit());
        System.out.println("Description: " + produit.getDescription_produit());

        // Remplir les champs
        productNameField.setText(produit.getNom_produit());
        productPriceField.setText(String.format("%.2f", produit.getPrix_produit()));
        productDescriptionField.setText(produit.getDescription_produit());

        // Charger les images
        loadImage(productImageView, produit.getImage_produit());
        loadImage(productImage2View, produit.getImage2_produit());
        loadImage(productImage3View, produit.getImage3_produit());
        loadImage(productImage4View, produit.getImage4_produit());
    }

    private void loadImage(ImageView imageView, String imagePath) {
        try {
            if (imagePath == null || imagePath.isEmpty()) {
                setDefaultImage(imageView);
                return;
            }

            // Si c'est une URL web
            if (imagePath.startsWith("http")) {
                imageView.setImage(new Image(imagePath));
                return;
            }

            // Si c'est un chemin local
            String fullPath = UPLOAD_DIR + imagePath;
            Image image = new Image(Paths.get(fullPath).toUri().toString());

            if (image.isError()) {
                throw new Exception("Erreur de chargement");
            }

            imageView.setImage(image);

        } catch (Exception e) {
            System.err.println("Erreur chargement image: " + imagePath);
            setDefaultImage(imageView);
        }
    }

    private void setDefaultImage(ImageView imageView) {
        try {
            imageView.setImage(new Image(getClass().getResourceAsStream(DEFAULT_IMAGE)));
        } catch (Exception e) {
            System.err.println("Erreur chargement image par défaut");
            imageView.setImage(null);
        }
    }
}