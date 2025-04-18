package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TabPane;

import java.net.URL;
import java.util.ResourceBundle;

public class BACK implements Initializable {

    @FXML
    private TabPane tabPane; // Ceci sera maintenant injecté correctement

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Vérification que tabPane n'est pas null
        if (tabPane != null) {
            tabPane.getSelectionModel().selectFirst();
        } else {
            System.err.println("Erreur: tabPane est null!");
        }
    }
}