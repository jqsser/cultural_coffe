package Controllers;

import entity.Commande;
import entity.Produit;
import entity.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import service.CommandeService;
import service.ProduitService;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class AfficherCommande implements Initializable {
    private static final String DEFAULT_IMAGE = "/images/default-product.png";
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
    @FXML private ScrollPane scrollPane;
    @FXML private TilePane commandesContainer;

    private final CommandeService commandeService = new CommandeService();
    private final ProduitService produitService = new ProduitService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        loadCommandes();
    }

    private User getCurrentUser() {
        // À remplacer par votre système d'authentification réel
        User testUser = new User();
        testUser.setId(15);
        return testUser;
    }

    public void loadCommandes() {
        try {
            commandesContainer.getChildren().clear();
            User currentUser = getCurrentUser();

            if (currentUser == null) {
                showErrorText("Utilisateur non connecté");
                return;
            }

            List<Commande> commandes = commandeService.recupererCommandesParUser(currentUser.getId());

            if (commandes.isEmpty()) {
                showInfoText("Aucune commande trouvée");
            } else {
                commandes.forEach(commande -> {
                    try {
                        Produit produit = commande.getProduit();
                        if (produit.getNom_produit() == null) {
                            produit = produitService.recupererProduitParId(produit.getId_produit());
                        }
                        commandesContainer.getChildren().add(createCommandeCard(commande, produit));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorText("Erreur lors du chargement des commandes");
        }
    }

    private void showErrorText(String message) {
        Text errorText = new Text(message);
        errorText.setStyle("-fx-font-size: 16; -fx-fill: red;");
        commandesContainer.getChildren().add(errorText);
    }

    private void showInfoText(String message) {
        Text infoText = new Text(message);
        infoText.setStyle("-fx-font-size: 16; -fx-fill: white;");
        commandesContainer.getChildren().add(infoText);
    }

    private VBox createCommandeCard(Commande commande, Produit produit) {
        VBox card = new VBox(10);
        card.setStyle("-fx-background-color: rgba(255,255,255,0.9); -fx-padding: 15; -fx-border-radius: 10; -fx-background-radius: 10;");
        card.setPrefSize(280, 350);

        // Header avec ID de commande
        Text commandeId = new Text("Commande #" + commande.getId_commande());
        commandeId.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");

        // Image du produit
        ImageView imageView = createProductImageView(produit);

        // Détails du produit
        Text nomProduit = new Text(produit.getNom_produit());
        nomProduit.setStyle("-fx-font-weight: bold; -fx-font-size: 16;");

        Text typeProduit = new Text("Type: " + produit.getType_produit());
        Text quantite = new Text("Quantité: " + commande.getQuantite_produit());
        Text prixUnitaire = new Text("Prix unitaire: " + String.format("%.2f", produit.getPrix_produit()) + " TND");

        // Prix total en évidence
        Text prixTotal = new Text("Total: " + String.format("%.2f", commande.getPrix_total_commande()) + " TND");
        prixTotal.setStyle("-fx-font-weight: bold; -fx-font-size: 16; -fx-fill: #ff6600;");

        // Boutons d'actions
        HBox buttonsContainer = createActionButtons(commande);

        card.getChildren().addAll(commandeId, imageView, nomProduit, typeProduit,
                quantite, prixUnitaire, prixTotal, buttonsContainer);

        return card;
    }

    private ImageView createProductImageView(Produit produit) {
        ImageView imageView = new ImageView();
        try {
            Image productImage = loadProductImage(produit);
            imageView.setImage(productImage);
            imageView.setPreserveRatio(true);
            imageView.setFitWidth(200);
            imageView.setFitHeight(120);
        } catch (Exception e) {
            System.err.println("Erreur chargement image: " + e.getMessage());
            try {
                imageView.setImage(new Image(getClass().getResourceAsStream(DEFAULT_IMAGE), 200, 120, true, true));
            } catch (Exception ex) {
                System.err.println("Échec chargement image par défaut: " + ex.getMessage());
            }
        }
        return imageView;
    }

    private HBox createActionButtons(Commande commande) {
        HBox buttonsContainer = new HBox(10);
        buttonsContainer.setStyle("-fx-alignment: center;");

        Button modifyButton = new Button("Modifier");
        modifyButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        modifyButton.setOnAction(event -> openModifyPopup(commande));

        Button deleteButton = new Button("Supprimer");
        deleteButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        deleteButton.setOnAction(event -> openDeleteConfirmation(commande));

        buttonsContainer.getChildren().addAll(modifyButton, deleteButton);
        return buttonsContainer;
    }

    private void openModifyPopup(Commande commande) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierCommande.fxml"));
            Parent root = loader.load();

            ModifierCommande controller = loader.getController();
            controller.setCommande(commande);
            controller.setRefreshCallback(this::loadCommandes);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier Commande #" + commande.getId_commande());
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir la fenêtre de modification");
        }
    }

    private void openDeleteConfirmation(Commande commande) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer la commande #" + commande.getId_commande() + "?");
        alert.setContentText("Cette action est irréversible. Confirmer la suppression?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            handleDeleteCommand(commande);
        }
    }

    private void handleDeleteCommand(Commande commande) {
        try {
            commandeService.supprimerCommande(commande.getId_commande());
            loadCommandes();
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Commande supprimée avec succès");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de la suppression: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private Image loadProductImage(Produit produit) throws Exception {
        String imagePath = produit.getImage_produit();

        if (imagePath == null || imagePath.isEmpty()) {
            return loadDefaultImage();
        }

        if (imagePath.startsWith("http://") || imagePath.startsWith("https://")) {
            return new Image(imagePath, 200, 120, true, true);
        }

        if (imagePath.startsWith("file:") || imagePath.startsWith("C:") || imagePath.startsWith("D:")) {
            String cleanPath = imagePath.replace("file:", "").trim();
            File file = new File(cleanPath);
            if (file.exists()) {
                return new Image(file.toURI().toString(), 200, 120, true, true);
            }
            throw new Exception("Fichier image introuvable: " + cleanPath);
        }

        InputStream resourceStream = getClass().getResourceAsStream(
                imagePath.startsWith("/") ? imagePath : "/images/" + imagePath);

        if (resourceStream != null) {
            return new Image(resourceStream, 200, 120, true, true);
        }

        return loadDefaultImage();
    }

    private Image loadDefaultImage() throws Exception {
        InputStream defaultStream = getClass().getResourceAsStream(DEFAULT_IMAGE);
        if (defaultStream != null) {
            return new Image(defaultStream, 200, 120, true, true);
        }
        throw new Exception("Image par défaut non trouvée");
    }

    @FXML
    public void refreshCommandes() {
        loadCommandes();
    }
}