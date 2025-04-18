package Controllers;

import entity.Commande;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.SimpleStringProperty;
import service.CommandeService;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class AffichageCommandeBack implements Initializable {
    private final CommandeService commandeService = new CommandeService();

    @FXML
    private TableView<Commande> tableCommandes;
    @FXML private TableColumn<Commande, Integer> colId;
    @FXML private TableColumn<Commande, String> colUser;
    @FXML private TableColumn<Commande, String> colProduit;
    @FXML private TableColumn<Commande, Integer> colQuantite;
    @FXML private TableColumn<Commande, Double> colPrixTotal;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configureTableColumns();
        loadCommandes();
    }

    private void configureTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id_commande"));
        colUser.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getUser().getNom_user()));
        colProduit.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getProduit().getNom_produit()));
        colQuantite.setCellValueFactory(new PropertyValueFactory<>("quantite_produit"));
        colPrixTotal.setCellValueFactory(new PropertyValueFactory<>("prix_total_commande"));
    }

    private void loadCommandes() {
        try {
            List<Commande> commandes = commandeService.recupererCommandesAvecUser();
            ObservableList<Commande> observableList = FXCollections.observableArrayList(commandes);
            tableCommandes.setItems(observableList);
        } catch (SQLException e) {
            showAlert("Erreur", "Échec du chargement des commandes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}