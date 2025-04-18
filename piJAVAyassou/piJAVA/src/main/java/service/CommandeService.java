package service;

import entity.Commande;
import entity.Produit;
import entity.User;
import tools.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommandeService implements ICommande<Commande> {
    private Connection cnx;
    private String sql;
    private Statement st;
    private PreparedStatement ste;

    public CommandeService() {
        cnx = MyDataBase.getInstance().getCnx();
    }

    @Override
    public void ajouterCommande(Commande commande) throws SQLException {
        sql = "INSERT INTO Commande (quantite_produit, prix_total_commande, produit_id, user_id) VALUES (?, ?, ?, ?)";
        ste = cnx.prepareStatement(sql);

        ste.setInt(1, commande.getQuantite_produit());
        ste.setDouble(2, commande.getPrix_total_commande());
        ste.setInt(3, commande.getProduit().getId_produit()); // Utilisation de l'objet Produit
        ste.setInt(4, commande.getUser().getId());    // Utilisation de l'objet User

        ste.executeUpdate();
        System.out.println("Commande ajoutée !");
    }
@Override
public void modifierCommande(Commande commande) throws SQLException {
    // Vérification plus complète de l'utilisateur
    if (commande.getUser() == null || commande.getUser().getId() <= 0) {
        throw new SQLException("La commande doit avoir un utilisateur valide associé");
    }

    String sql = "UPDATE commande SET quantite_produit = ?, prix_total_commande = ?, produit_id = ?, user_id = ? WHERE id = ?";
    try (PreparedStatement preparedStatement = cnx.prepareStatement(sql)) {
        preparedStatement.setInt(1, commande.getQuantite_produit());
        preparedStatement.setDouble(2, commande.getPrix_total_commande());
        preparedStatement.setInt(3, commande.getProduit().getId_produit());
        preparedStatement.setInt(4, commande.getUser().getId()); // Ajout de l'user_id
        preparedStatement.setInt(5, commande.getId_commande());
        preparedStatement.executeUpdate();
    }
}
    @Override
    public void supprimerCommande(int id) throws SQLException {
        sql = "DELETE FROM Commande WHERE id = ?";
        ste = cnx.prepareStatement(sql);
        ste.setInt(1, id);
        ste.executeUpdate();
        System.out.println("Commande supprimée !");
    }

    @Override
    public List<Commande> recupererCommandes() throws SQLException {
        List<Commande> commandes = new ArrayList<>();
        String query = "SELECT c.*, p.*, u.id as user_id, u.nom as user_nom FROM commande c " +
                "JOIN produit p ON c.produit_id = p.id " +
                "JOIN user u ON c.user_id = u.id";

        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            while (rs.next()) {
                // Création du Produit
                Produit produit = new Produit();
                produit.setId_produit(rs.getInt("p.id"));
                produit.setNom_produit(rs.getString("p.nom_produit"));
                // ... autres champs du produit

                // Création de l'User
                User user = new User();
                user.setId(rs.getInt("user_id"));
                user.setNom_user(rs.getString("user_nom"));
                // ... autres champs de l'user si nécessaire

                // Création de la Commande
                Commande commande = new Commande();
                commande.setId_commande(rs.getInt("c.id"));
                commande.setQauntite_produit(rs.getInt("c.quantite_produit"));
                commande.setPrix_total_commande(rs.getDouble("c.prix_total_commande"));
                commande.setProduit(produit);
                commande.setUser(user); // Important: associer l'user

                commandes.add(commande);
            }
        }
        return commandes;
    }
    public List<Commande> recupererCommandesAvecUser() throws SQLException {
        List<Commande> commandes = new ArrayList<>();
        String query = "SELECT c.id as commande_id, c.quantite_produit, c.prix_total_commande, "
                + "u.nom as user_nom, p.nom_produit "
                + "FROM commande c "
                + "JOIN user u ON c.user_id = u.id "
                + "JOIN produit p ON c.produit_id = p.id";

        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            while (rs.next()) {
                Commande commande = new Commande();
                commande.setId_commande(rs.getInt("commande_id"));
                commande.setQauntite_produit(rs.getInt("quantite_produit"));
                commande.setPrix_total_commande(rs.getDouble("prix_total_commande"));

                // Création d'un user simplifié avec juste le nom
                User user = new User();
                user.setNom_user(rs.getString("user_nom"));
                commande.setUser(user);

                // Création d'un produit simplifié avec juste le nom
                Produit produit = new Produit();
                produit.setNom_produit(rs.getString("nom_produit"));
                commande.setProduit(produit);

                commandes.add(commande);
            }
        }
        return commandes;
    }


    public List<Commande> recupererCommandesParUser(int userId) throws SQLException {
        List<Commande> commandes = new ArrayList<>();
        sql = "SELECT c.*, p.* FROM commande c " +
                "JOIN produit p ON c.produit_id = p.id " +
                "WHERE c.user_id = ?";

        ste = cnx.prepareStatement(sql);
        ste.setInt(1, userId);
        ResultSet rs = ste.executeQuery();

        while (rs.next()) {
            // Création du Produit
            Produit produit = new Produit();
            produit.setId_produit(rs.getInt("p.id"));
            produit.setNom_produit(rs.getString("p.nom_produit"));
            produit.setDescription_produit(rs.getString("p.description_produit"));
            produit.setPrix_produit(rs.getDouble("p.prix_produit"));
            produit.setType_produit(rs.getString("p.type_produit"));
            produit.setImage_produit(rs.getString("p.image_produit"));
            // Ajouter les autres champs si nécessaire

            // Création de la Commande
            Commande commande = new Commande();
            commande.setId_commande(rs.getInt("c.id"));
            commande.setQauntite_produit(rs.getInt("c.quantite_produit"));
            commande.setPrix_total_commande(rs.getDouble("c.prix_total_commande"));
            commande.setProduit(produit);

            commandes.add(commande);
        }

        return commandes;
    }

}
