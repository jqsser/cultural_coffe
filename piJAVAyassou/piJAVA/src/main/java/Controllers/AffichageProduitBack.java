package Controllers;

import entity.Produit;
import entity.User;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import service.ProduitService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class AffichageProduitBack implements Initializable {

    @FXML
    private TableView<Produit> tableProduits;
    @FXML
    private TableColumn<Produit, String> colNom;
    @FXML
    private TableColumn<Produit, Integer> colPrix;
    @FXML
    private TableColumn<Produit, String> colDescription;
    @FXML
    private TableColumn<Produit, String> colImage;

    @FXML
    private TableColumn<Produit, String> colEtat;
    @FXML
    private TableColumn<Produit, Integer> colStock;
    @FXML
    private TableColumn<Produit, String> colUser;
    @FXML
    private TableColumn<Produit, Void> colModifier;
    @FXML
    private TableColumn<Produit, Void> colSupprimer;

    private final ProduitService produitService = new ProduitService();
    private Produit produit;  // Champ pour stocker le produit

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom_produit"));
        colPrix.setCellValueFactory(new PropertyValueFactory<>("prix_produit"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description_produit"));
        colEtat.setCellValueFactory(new PropertyValueFactory<>("etat_produit"));

        colImage.setCellValueFactory(new PropertyValueFactory<>("image_produit"));



        colStock.setCellValueFactory(new PropertyValueFactory<>("stock_produit"));

        colUser.setCellValueFactory(cellData -> {
            User user = cellData.getValue().getUser();
            return new ReadOnlyStringWrapper(user != null ? user.getNom_user() : "Inconnu");
        });

        configureImageColumn(colImage);


        colModifier.setCellFactory(param -> new TableCell<Produit, Void>() {
            private final Button btn = new Button("Modifier");

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    return;
                }

                btn.setOnAction(event -> {
                    Produit produit = getTableView().getItems().get(getIndex());
                    showPopupModifier(produit);
                });

                setGraphic(btn);
            }
        });

        colSupprimer.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("Supprimer");

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    return;
                }
                btn.setOnAction(event -> {
                    Produit produit = getTableView().getItems().get(getIndex());
                    openPopupSuppression(produit);
                });
                setGraphic(btn);
            }
        });

        loadProduits();
    }

    private void loadProduits() {
        try {
            List<Produit> produits = produitService.recupererProduits();
            ObservableList<Produit> observableProduits = FXCollections.observableArrayList(produits);
            tableProduits.setItems(observableProduits);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void configureImageColumn(TableColumn<Produit, String> colImage) {
        colImage.setCellFactory(column -> new TableCell<>() {
            private final ImageView imageView = new ImageView();
            {
                imageView.setFitWidth(80);
                imageView.setFitHeight(60);
                imageView.setPreserveRatio(true);
            }

            @Override
            protected void updateItem(String imageUrl, boolean empty) {
                super.updateItem(imageUrl, empty);
                if (empty || imageUrl == null || imageUrl.isEmpty()) {
                    setGraphic(null);
                } else {
                    try {
                        imageView.setImage(new Image(imageUrl, true));
                        setGraphic(imageView);
                    } catch (Exception e) {
                        System.out.println("Erreur image: " + e.getMessage());
                        setGraphic(null);
                    }
                }
            }
        });
    }

    private void openPopupSuppression(Produit produit) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/SupprimerProduit.fxml"));
            Parent root = loader.load();
            SupprimerProduit controller = loader.getController();
            controller.setMessage("Êtes-vous sûr de vouloir supprimer le produit : " + produit.getNom_produit() + "?");
            controller.setOnConfirmAction(() -> {
                try {
                    produitService.supprimerProduit(produit.getId_produit());
                    loadProduits();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            });
            Stage popupStage = new Stage();
            popupStage.setScene(new Scene(root));
            popupStage.setTitle("Confirmation de Suppression");
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void showPopupModifier(Produit produit) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierProduitBack.fxml"));
            Parent root = loader.load();
            ModifierProduitBack controller = loader.getController();
            controller.setProduit(produit);
            controller.setOnConfirmAction(() -> {
                try {
                    produitService.modifierProduit(produit);
                    loadProduits();
                } catch (SQLException e) {
                    showError("Erreur lors de la modification du produit.", e.getMessage());
                }
            });
            Stage popupStage = new Stage();
            popupStage.setScene(new Scene(root));
            popupStage.setTitle("Modifier Produit");
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.show();
        } catch (IOException e) {
            showError("Erreur lors de l'ouverture du popup de modification.", e.getMessage());
        }
    }


    // Méthode pour afficher des messages d'erreur
    private void showError(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
