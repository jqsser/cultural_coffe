package service;

import entity.Produit;
import entity.User;
import tools.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProduitService implements IProduit<Produit> {
    private Connection cnx;
    String sql;
    private Statement st;
    private PreparedStatement ste;

    public ProduitService() {
        cnx = MyDataBase.getInstance().getCnx();
    }

    @Override
    public void supprimerProduit(int id) throws SQLException {
        // D'abord, supprimer toutes les commandes liées à ce produit
        String deleteCommandesSQL = "DELETE FROM commande WHERE produit_id = ?";
        try (PreparedStatement deleteCommandes = cnx.prepareStatement(deleteCommandesSQL)) {
            deleteCommandes.setInt(1, id);
            deleteCommandes.executeUpdate();
        }

        // Ensuite, supprimer le produit
        String deleteProduitSQL = "DELETE FROM produit WHERE id = ?";
        try (PreparedStatement deleteProduit = cnx.prepareStatement(deleteProduitSQL)) {
            deleteProduit.setInt(1, id);
            deleteProduit.executeUpdate();
        }
    }
    @Override
    public void modifierProduit(Produit produit) throws SQLException {
        sql = "UPDATE Produit SET description_produit=?, prix_produit=?, stock_produit=?, type_produit=?, etat_produit=?, " +
                "nom_produit=?, image_produit=?, image2_produit=?, image3_produit=?, image4_produit=? " +
                "WHERE id=?";

        ste = cnx.prepareStatement(sql);

        ste.setString(1, produit.getDescription_produit());
        ste.setDouble(2, produit.getPrix_produit());
        ste.setInt(3, produit.getStock_produit());
        ste.setString(4, produit.getType_produit());
        ste.setInt(5, produit.getEtat_produit());  // Si vous avez une méthode getEtat_produit()
        ste.setString(6, produit.getNom_produit());
        ste.setString(7, produit.getImage_produit());
        ste.setString(8, produit.getImage2_produit());
        ste.setString(9, produit.getImage3_produit());
        ste.setString(10, produit.getImage4_produit());
        ste.setInt(11, produit.getId_produit()); // L'ID du produit pour la mise à jour

        ste.executeUpdate();
    }


    @Override
    public void ajouterProduit(Produit produit) throws SQLException {
        String sql = "INSERT INTO Produit (prix_produit, stock_produit, type_produit, etat_produit, nom_produit, " +
                "description_produit, image_produit, image2_produit, image3_produit, image4_produit, user_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement ps = cnx.prepareStatement(sql);
        ps.setDouble(1, produit.getPrix_produit());
        ps.setInt(2, produit.getStock_produit());
        ps.setString(3, produit.getType_produit());
        ps.setInt(4, produit.getEtat_produit());
        ps.setString(5, produit.getNom_produit());
        ps.setString(6, produit.getDescription_produit());
        ps.setString(7, produit.getImage_produit());
        ps.setString(8, produit.getImage2_produit());
        ps.setString(9, produit.getImage3_produit());
        ps.setString(10, produit.getImage4_produit());
        ps.setInt(11, produit.getUser().getId()); // 👈 user_id depuis l'objet User


        ps.executeUpdate();
    }


    public List<Produit> recupererProduits1() throws SQLException {
        List<Produit> produits = new ArrayList<>();
        String query = "SELECT p.*, u.id as user_id, u.nom as user_nom FROM produit p LEFT JOIN user u ON p.user_id = u.id";

        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            while (rs.next()) {
                Produit produit = new Produit();
                // ... autres champs du produit ...

                // Création de l'utilisateur
                User user = new User();
                user.setId(rs.getInt("user_id"));
                user.setNom_user(rs.getString("user_nom"));
                produit.setUser(user);

                produits.add(produit);
            }
        }
        return produits;
    }
    @Override
    public List<Produit> recupererProduits() throws SQLException {
        List<Produit> produits = new ArrayList<>();

        String sql = "SELECT p.*, u.id AS user_id, u.nom_user, u.prenom_user, u.email_user, u.telephone_user, u.adresse, u.role_user, u.photo_user, u.date_naissance_user " +
                "FROM Produit p " +
                "JOIN user u ON p.user_id = u.id";

        st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);

        while (rs.next()) {

            User user = new User();
            user.setNom_user(rs.getString("nom_user"));

            // Création de l'objet Produit
            Produit produit = new Produit(
                    rs.getInt("id"),
                    rs.getInt("prix_produit"),
                    rs.getInt("stock_produit"),
                    rs.getString("type_produit"),
                    rs.getInt("etat_produit"),
                    rs.getString("nom_produit"),
                    rs.getString("description_produit"),
                    rs.getString("image_produit"),
                    rs.getString("image2_produit"),
                    rs.getString("image3_produit"),
                    rs.getString("image4_produit"),
                    user
            );

            produits.add(produit);
        }

        return produits;
    }
    // Méthode pour récupérer un produit par son ID
    public Produit recupererProduitParId(int id) throws SQLException {
        String sql = "SELECT * FROM produit WHERE id = ?";
        PreparedStatement ste = cnx.prepareStatement(sql);
        ste.setInt(1, id);

        ResultSet rs = ste.executeQuery();

        if (rs.next()) {
            Produit produit = new Produit();
            produit.setId_produit(rs.getInt("id"));
            produit.setNom_produit(rs.getString("nom_produit"));
            produit.setDescription_produit(rs.getString("description_produit"));
            produit.setPrix_produit(rs.getDouble("prix_produit"));
            produit.setType_produit(rs.getString("type_produit"));
            produit.setImage_produit(rs.getString("image_produit"));
            produit.setStock_produit(rs.getInt("stock_produit"));
            // Ajoutez les autres champs selon votre table

            return produit;
        }

        return null; // Retourne null si aucun produit trouvé
    }


}