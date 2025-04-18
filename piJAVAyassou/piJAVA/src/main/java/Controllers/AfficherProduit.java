package Controllers;


import entity.Commande;
import entity.Produit;
import Controllers.DetailsProduit;
import entity.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import service.CommandeService;
import service.ProduitService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AfficherProduit {
    // Navigation methods
    @FXML
    private void navigateToAccueil(ActionEvent event) {
        loadPage("/Accueil.fxml", event);
    }

    @FXML
    private void navigateToRencontres(ActionEvent event) {
        loadPage("/Rencontres.fxml", event);
    }

    @FXML
    private void navigateToEvenements(ActionEvent event) {
        loadPage("/AfficherEvenementUser.fxml", event);
    }

    @FXML
    private void navigateToForum(ActionEvent event) {
        loadPage("/Forum.fxml", event);
    }

    @FXML
    private void navigateToBoutique(ActionEvent event) {
        loadPage("/AfficherProduit.fxml", event);
    }

    @FXML
    private void navigateToConnexion(ActionEvent event) {
        loadPage("/Connexion.fxml", event);
    }
    private void loadPage(String fxmlPath, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private TilePane containerProduit;

    private final ProduitService produitService = new ProduitService();

    @FXML
    public void initialize() throws SQLException {
        loadProduits();
    }
    @FXML
    private Button deleteBtn;
    @FXML
    private void ouvrirAjoutProduit() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterProduit.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter Produit");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ouvrirBackOffice() {
        try {
            // Charge l'interface du back-office avec onglets
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/BACK.fxml"));
            Parent root = loader.load();

            // Ouvre dans une nouvelle fenêtre
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Back Office");
            stage.show();

            // Ferme la fenêtre actuelle si nécessaire
            // ((Stage) backOfficeBtn.getScene().getWindow()).close();

        } catch (IOException e) {
            e.printStackTrace();
            showError("Erreur", "Impossible d'ouvrir l'interface d'administration");
        }
    }


    @FXML
    private void ouvrirAfficherCommande() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherCommande.fxml"));
            Parent root = loader.load();


            // Passer l'utilisateur connecté au contrôleur
            AfficherCommande controller = loader.getController();
            controller.loadCommandes(); // Charger les commandes

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Mes Commandes");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showError("Erreur", "Impossible d'ouvrir l'interface des commandes");
        }
    }
    @FXML
    private void ouvrirAffichageProduitBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/BACK.fxml"));
            Parent root = loader.load();

            // Crée une nouvelle scène
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Administration");
            stage.show();

            // Ferme la fenêtre actuelle si nécessaire
            // ((Stage) backOfficeBtn.getScene().getWindow()).close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void supprimerProduit(ActionEvent event) {
        // Logique pour supprimer un produit (tu peux personnaliser)
        System.out.println("Produit supprimé !");
    }




   /* private User getCurrentUser() {
        // Implémentation à adapter
        User user = new User();
        user.setId(15); // Exemple
        return user;
    }
*/


    private void loadProduits() throws SQLException {
        containerProduit.getChildren().clear();
        List<Produit> produits = produitService.recupererProduits();

        // Récupérer l'utilisateur connecté

        for (Produit p : produits) {
            if (p.getEtat_produit() == 1) {
                VBox card = new VBox(10);
                card.setPrefSize(180, 280);
                card.setStyle("""
                -fx-background-color: white;
                -fx-padding: 10;
                -fx-border-color: #ccc;
                -fx-border-radius: 10;
                -fx-background-radius: 10;
                -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0.5, 0, 0);
                -fx-alignment: center;
                """);


                // Image
                if (p.getImage_produit() != null && !p.getImage_produit().isEmpty()) {
                    try {
                        ImageView imageView = new ImageView(new Image(p.getImage_produit(), 150, 100, true, true));
                        card.getChildren().add(imageView);
                    } catch (Exception e) {
                        System.out.println("Erreur chargement image : " + e.getMessage());
                    }
                }
                String nom = p.getNom_produit() != null ? p.getNom_produit() : "NOM_INCONNU";
                String type = p.getType_produit() != null ? p.getType_produit() : "TYPE_INCONNU";
                // Infos produit
                Label nomLabel = new Label(p.getNom_produit());
                nomLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
                Label prixLabel = new Label(p.getPrix_produit() + " TND");
                prixLabel.setStyle("-fx-text-fill: #ff6600; -fx-font-weight: bold;");
                Label typeLabel = new Label(p.getType_produit());

                // Boutons
                Button addBtn = new Button("+");
                addBtn.setStyle("-fx-background-color: #ff6600; -fx-text-fill: white; -fx-padding: 4 8 4 8; -fx-background-radius: 8;");
                addBtn.setOnAction(e -> openAddOrderPopup(p));

                Button detailsBtn = new Button("Détails");
                detailsBtn.setStyle("-fx-background-color: #2a9df4; -fx-text-fill: white; -fx-background-radius: 8;");
                detailsBtn.setOnAction(e -> {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/DetailsProduit.fxml"));
                        Parent root = loader.load();

                        DetailsProduit controller = loader.getController();
                        controller.setProduit(p);

                        Stage stage = new Stage();
                        stage.setScene(new Scene(root));
                        stage.setTitle("Détails du Produit");
                        stage.show();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                });

                // Créer les boutons supprimer et modifier
                Button deleteBtn = new Button("Supprimer");
                deleteBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-background-radius: 8;");
                deleteBtn.setOnAction(e -> openPopupSuppression(p));

                Button modifierBtn = new Button("Modifier");
                modifierBtn.setStyle("-fx-background-color: #f1c40f; -fx-text-fill: white; -fx-background-radius: 8;");
                modifierBtn.setOnAction(e -> {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierProduit.fxml"));
                        Parent root = loader.load();

                        ModifierProduit controller = loader.getController();
                        controller.setProduit(p);

                        Stage stage = new Stage();
                        stage.setScene(new Scene(root));
                        stage.setTitle("Modifier Produit");
                        stage.show();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                });

                // Vérifier si l'utilisateur connecté est le propriétaire du produit
             //   boolean isOwner = p.getUser().getId() == currentUser.getId(); // Supposant que Produit a une référence à User

               HBox buttonsBox;
             //   if (isOwner) {
                    // Afficher tous les boutons si l'utilisateur est le propriétaire
                    buttonsBox = new HBox(10, addBtn, detailsBtn, deleteBtn, modifierBtn);
                //} else {
                    // Afficher seulement les boutons add et details si l'utilisateur n'est pas le propriétaire
                 //   buttonsBox = new HBox(10, addBtn, detailsBtn);
             //   }

                card.getChildren().addAll(nomLabel, typeLabel, prixLabel, buttonsBox);
                containerProduit.getChildren().add(card);
            }
        }
    }

    private void openAddOrderPopup(Produit produit) {
        try {
            if (produit.getStock_produit() <= 0) {
                showAlert("Indisponible", "Ce produit est épuisé");
                return;
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterCommande.fxml"));
            Parent root = loader.load();

            AjouterCommande controller = loader.getController();
            controller.setProduit(produit);

            // Définir le callback pour le rechargement
            controller.setOnSuccess(() -> {
                try {
                    loadProduits(); // Recharge les produits après commande
                } catch (SQLException e) {
                    showError("Erreur", "Impossible de rafraîchir la liste");
                }
            });

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Commander: " + produit.getNom_produit());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException e) {
            showError("Erreur", "Impossible d'ouvrir l'interface de commande");
        }
    }

    // Méthodes utilitaires
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }



    private void openPopupSuppression(Produit produit) {
        try {
            // Charger le fichier FXML de la pop-up
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/SupprimerProduit.fxml"));
            Parent root = loader.load();

            // Récupérer le contrôleur de la pop-up
            SupprimerProduit controller = loader.getController();
            controller.setMessage("Êtes-vous sûr de vouloir supprimer le produit : " + produit.getNom_produit() + "?");

            // Définir l'action de confirmation
            controller.setOnConfirmAction(() -> {
                try {
                    produitService.supprimerProduit(produit.getId_produit()); // Supprimer le produit
                    loadProduits(); // Recharge la liste après suppression
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            });

            // Créer une nouvelle scène pour la pop-up
            Stage popupStage = new Stage();
            popupStage.setScene(new Scene(root));
            popupStage.setTitle("Confirmation de Suppression");
            popupStage.initModality(Modality.APPLICATION_MODAL);  // Empêche l'interaction avec la fenêtre principale
            popupStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}