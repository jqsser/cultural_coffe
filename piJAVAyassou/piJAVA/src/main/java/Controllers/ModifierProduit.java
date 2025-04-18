package Controllers;

import entity.Produit;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import service.ProduitService;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Base64;

public class ModifierProduit {
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
    @FXML private TextField nomTF, prixTF, descriptionTF, stockTF;
    @FXML private ComboBox<String> typeTF;
    @FXML private ImageView img1TF, img2TF, img3TF, img4TF;
    @FXML private Label nomError, prixError, descriptionError, stockError, typeError, imageError;

    private File image1, image2, image3, image4;
    private Produit produitAModifier;
    private final ProduitService produitService = new ProduitService();

    public void setProduit(Produit produit) {
        this.produitAModifier = produit;
        loadProductData();
    }

    private void loadProductData() {
        // Remplir les champs texte
        nomTF.setText(produitAModifier.getNom_produit());
        prixTF.setText(String.valueOf(produitAModifier.getPrix_produit()));
        descriptionTF.setText(produitAModifier.getDescription_produit());
        stockTF.setText(String.valueOf(produitAModifier.getStock_produit()));
        typeTF.setValue(produitAModifier.getType_produit());

        // Charger les images
        loadImage(produitAModifier.getImage_produit(), img1TF, 1);
        loadImage(produitAModifier.getImage2_produit(), img2TF, 2);
        loadImage(produitAModifier.getImage3_produit(), img3TF, 3);
        loadImage(produitAModifier.getImage4_produit(), img4TF, 4);
    }

    @FXML
    private void initialize() {
        typeTF.getItems().addAll("Artistique", "Artisanat", "Livre");
    }

    private void loadImage(String imagePath, ImageView imageView, int imageIndex) {
        try {
            if (imagePath == null || imagePath.isEmpty()) {
                setDefaultImage(imageView);
                return;
            }

            // Cas 1: Image encodée en base64
            if (imagePath.startsWith("data:image")) {
                String base64Data = imagePath.split(",")[1];
                byte[] imageBytes = Base64.getDecoder().decode(base64Data);
                Image image = new Image(new java.io.ByteArrayInputStream(imageBytes));
                imageView.setImage(image);
                return;
            }

            // Cas 2: URL web
            if (imagePath.startsWith("http")) {
                imageView.setImage(new Image(imagePath));
                return;
            }

            // Cas 3: Fichier local
            File imageFile = new File(imagePath);
            if (!imageFile.exists()) {
                // Essaye comme chemin relatif
                imageFile = new File("uploads/images/" + imagePath);
            }

            if (imageFile.exists()) {
                Image image = new Image(imageFile.toURI().toString());
                imageView.setImage(image);

                // Stocker la référence
                switch (imageIndex) {
                    case 1 -> image1 = imageFile;
                    case 2 -> image2 = imageFile;
                    case 3 -> image3 = imageFile;
                    case 4 -> image4 = imageFile;
                }
            } else {
                throw new Exception("Fichier introuvable: " + imagePath);
            }
        } catch (Exception e) {
            System.err.println("Erreur chargement image: " + e.getMessage());
            setDefaultImage(imageView);
        }
    }

    private void setDefaultImage(ImageView imageView) {
        try (InputStream stream = getClass().getResourceAsStream("/images/default-product.png")) {
            if (stream != null) {
                imageView.setImage(new Image(stream));
            }
        } catch (Exception e) {
            System.err.println("Erreur chargement image par défaut");
            imageView.setImage(null);
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

    @FXML
    void modifierProduit(ActionEvent event) throws SQLException {
        resetErrorMessages();
        boolean isValid = true;

        // Validation du nom
        String nom = nomTF.getText().trim();
        if (nom.isEmpty()) {
            nomError.setText("Le nom est requis");
            isValid = false;
        } else if (nom.length() > 50) {
            nomError.setText("Le nom ne doit pas dépasser 50 caractères");
            isValid = false;
        }

        // Validation du prix
        String prixText = prixTF.getText().trim();
        double prix = 0;
        if (prixText.isEmpty()) {
            prixError.setText("Le prix est requis");
            isValid = false;
        } else {
            try {
                prix = Double.parseDouble(prixText);
                if (prix <= 0) {
                    prixError.setText("Le prix doit être positif");
                    isValid = false;
                } else if (prix > 10000) {
                    prixError.setText("Le prix doit être inférieur à 10 000");
                    isValid = false;
                }
            } catch (NumberFormatException e) {
                prixError.setText("Le prix doit être un nombre valide");
                isValid = false;
            }
        }

        // Validation de la description
        String description = descriptionTF.getText().trim();
        if (description.isEmpty()) {
            descriptionError.setText("La description est requise");
            isValid = false;
        } else if (description.length() > 255) {
            descriptionError.setText("La description ne doit pas dépasser 255 caractères");
            isValid = false;
        }

        // Validation du stock
        String stockText = stockTF.getText().trim();
        int stock = 0;
        if (stockText.isEmpty()) {
            stockError.setText("Le stock est requis");
            isValid = false;
        } else {
            try {
                stock = Integer.parseInt(stockText);
                if (stock < 0) {
                    stockError.setText("Le stock ne peut pas être négatif");
                    isValid = false;
                } else if (stock > 1000) {
                    stockError.setText("Le stock doit être inférieur à 1000");
                    isValid = false;
                }
            } catch (NumberFormatException e) {
                stockError.setText("Le stock doit être un nombre entier");
                isValid = false;
            }
        }

        // Validation du type
        String type = typeTF.getValue();
        if (type == null || type.trim().isEmpty()) {
            typeError.setText("Veuillez sélectionner un type");
            isValid = false;
        }

        if (!isValid) {
            return;
        }

        // Mise à jour du produit
        produitAModifier.setNom_produit(nom);
        produitAModifier.setPrix_produit(prix);
        produitAModifier.setDescription_produit(description);
        produitAModifier.setStock_produit(stock);
        produitAModifier.setType_produit(type);

        // Gestion des images
        if (image1 != null) produitAModifier.setImage_produit(image1.getName());
        if (image2 != null) produitAModifier.setImage2_produit(image2.getName());
        if (image3 != null) produitAModifier.setImage3_produit(image3.getName());
        if (image4 != null) produitAModifier.setImage4_produit(image4.getName());

        produitService.modifierProduit(produitAModifier);
        System.out.println("Produit modifié avec succès");
    }

    @FXML
    void selectimg1(ActionEvent event) {
        image1 = chooseImage(img1TF);
        imageError.setText("");
    }

    @FXML
    void selectimg2(ActionEvent event) {
        image2 = chooseImage(img2TF);
        imageError.setText("");
    }

    @FXML
    void selectimg3(ActionEvent event) {
        image3 = chooseImage(img3TF);
        imageError.setText("");
    }

    @FXML
    void selectimg4(ActionEvent event) {
        image4 = chooseImage(img4TF);
        imageError.setText("");
    }

    private File chooseImage(ImageView imageView) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Images", "*.jpg", "*.jpeg", "*.png", "*.bmp")
        );
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            imageView.setImage(new Image(file.toURI().toString()));
        }
        return file;
    }
}