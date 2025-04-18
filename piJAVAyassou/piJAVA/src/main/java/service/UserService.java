package service;

import entity.Evenement;
import entity.Reservation;
import entity.User;
import tools.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserService {
    Connection conn;

    // Créer un nouvel utilisateur
    public void ajouterUser(User user) throws SQLException {
        String query = "INSERT INTO user (telephone_user, type_user, role_user, nom_user, prenom_user, email_user, adresse, photo_user, date_naissance_user) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pst = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pst.setInt(1, user.getTelephone_user());
            pst.setString(3, user.getRole_user());
            pst.setString(4, user.getNom_user());
            pst.setString(5, user.getPrenom_user());
            pst.setString(6, user.getEmail_user());
            pst.setString(7, user.getAdresse());
            pst.setString(8, user.getPhoto_user());
            pst.setDate(9, user.getDate_naissance_user());

            pst.executeUpdate();

            // Récupérer l'ID généré
            try (ResultSet generatedKeys = pst.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    // Modifier un utilisateur
    public void modifierUser(User user) throws SQLException {
        String query = "UPDATE user SET telephone_user=?, type_user=?, role_user=?, nom_user=?, prenom_user=?, " +
                "email_user=?, adresse=?, photo_user=?, date_naissance_user=? WHERE id=?";

        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, user.getTelephone_user());
            pst.setString(3, user.getRole_user());
            pst.setString(4, user.getNom_user());
            pst.setString(5, user.getPrenom_user());
            pst.setString(6, user.getEmail_user());
            pst.setString(7, user.getAdresse());
            pst.setString(8, user.getPhoto_user());
            pst.setDate(9, user.getDate_naissance_user());
            pst.setInt(10, user.getId());

            pst.executeUpdate();
        }
    }

    // Supprimer un utilisateur
    public void supprimerUser(int id) throws SQLException {
        String query = "DELETE FROM user WHERE id=?";

        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, id);
            pst.executeUpdate();
        }
    }

    // Récupérer un utilisateur par son ID
    public User recupererParId(int id) throws SQLException {
        String query = "SELECT * FROM user WHERE id=?";
        User user = null;

        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, id);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    user = new User(
                            rs.getInt("id"),
                            rs.getInt("telephone_user"),
                            rs.getString("role_user"),
                            rs.getString("nom_user"),
                            rs.getString("prenom_user"),
                            rs.getString("email_user"),
                            rs.getString("adresse"),
                            rs.getString("photo_user"),
                            rs.getDate("date_naissance_user")
                    );
                }
            }
        }
        return user;
    }

    // Récupérer tous les utilisateurs
    public List<User> recupererTous() throws SQLException {
        List<User> users = new ArrayList<>();
        String query = "SELECT c.*, p.*, u.id as user_id, u.nom_user, u.prenom_user " +
                "FROM commande c " +
                "JOIN produit p ON c.produit_id = p.id_produit " +
                "JOIN user u ON c.user_id = u.id";

        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            while (rs.next()) {
                User user = new User(
                        rs.getInt("id"),
                        rs.getInt("telephone_user"),
                        rs.getString("role_user"),
                        rs.getString("nom_user"),
                        rs.getString("prenom_user"),
                        rs.getString("email_user"),
                        rs.getString("adresse"),
                        rs.getString("photo_user"),
                        rs.getDate("date_naissance_user")
                );
                users.add(user);
            }
        }
        return users;
    }

    // Récupérer un utilisateur par email (pour l'authentification)
    public User recupererParEmail(String email) throws SQLException {
        String query = "SELECT * FROM user WHERE email_user=?";
        User user = null;

        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setString(1, email);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    user = new User(
                            rs.getInt("id"),
                            rs.getInt("telephone_user"),
                            rs.getString("role_user"),
                            rs.getString("nom_user"),
                            rs.getString("prenom_user"),
                            rs.getString("email_user"),
                            rs.getString("adresse"),
                            rs.getString("photo_user"),
                            rs.getDate("date_naissance_user")
                    );
                }
            }
        }
        return user;
    }

    // Vérifier si un email existe déjà
    public boolean emailExiste(String email) throws SQLException {
        String query = "SELECT COUNT(*) FROM user WHERE email_user=?";

        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setString(1, email);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
}