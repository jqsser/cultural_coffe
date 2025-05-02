package tn.esprit.services;

import tn.esprit.entities.Matching;
import tn.esprit.entities.User;
import tn.esprit.tools.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MatchingService implements IService<Matching> {
    private Connection cnx;
    private String sql;

    public MatchingService() {
        cnx = MyDataBase.getInstance().getCnx();
    }

    @Override
    public void ajouter(Matching matching) throws SQLException {
        sql = "INSERT INTO matching (name, sujet_rencontre, num_table, nbr_personne_matchy, image, user_id) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ste = cnx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ste.setString(1, matching.getName());
            ste.setString(2, matching.getSujetRencontre());
            ste.setInt(3, matching.getNumTable());
            ste.setInt(4, matching.getNbrPersonneMatchy());
            ste.setString(5, matching.getImage());
            ste.setInt(6, matching.getUser().getId());
            ste.executeUpdate();

            try (ResultSet rs = ste.getGeneratedKeys()) {
                if (rs.next()) {
                    matching.setId(rs.getInt(1));
                }
            }
        }
    }

    @Override
    public void modifier(Matching matching) throws SQLException {
        System.out.println("Updating matching in database: " + matching); // Debugging

        sql = "UPDATE matching SET name=?, sujet_rencontre=?, num_table=?, " +
                "nbr_personne_matchy=?, image=?, user_id=? WHERE id=?";

        try (PreparedStatement ste = cnx.prepareStatement(sql)) {
            ste.setString(1, matching.getName());
            ste.setString(2, matching.getSujetRencontre());
            ste.setInt(3, matching.getNumTable());
            ste.setInt(4, matching.getNbrPersonneMatchy());
            ste.setString(5, matching.getImage());
            ste.setInt(6, matching.getUser().getId());
            ste.setInt(7, matching.getId());
            ste.executeUpdate();
            System.out.println("Matching updated successfully in the database.");
        } catch (Exception e) {
            System.err.println("Error updating matching: " + e.getMessage());
            throw e;
        }

    }

    @Override
    public void supprimer(int id) throws SQLException {
        // First delete from matching_user join table
        sql = "DELETE FROM matching_user WHERE matching_id=?";
        try (PreparedStatement stmt = cnx.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }

        // Then delete the matching
        sql = "DELETE FROM matching WHERE id=?";
        try (PreparedStatement stmt = cnx.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public List<Matching> recuperer() throws SQLException {
        List<Matching> matchings = new ArrayList<>();
        sql = "SELECT * FROM matching";

        try (Statement st = cnx.createStatement();
                ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                matchings.add(resultSetToMatching(rs));
            }
        }
        return matchings;
    }

    @Override
    public Matching getById(int id) throws SQLException {
        sql = "SELECT * FROM matching WHERE id=?";

        try (PreparedStatement st = cnx.prepareStatement(sql)) {
            st.setInt(1, id);

            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return resultSetToMatching(rs);
                }
            }
        }
        return null;
    }

    // Helper method to convert ResultSet to Matching object
    private Matching resultSetToMatching(ResultSet rs) throws SQLException {
        Matching matching = new Matching();
        matching.setId(rs.getInt("id"));
        matching.setName(rs.getString("name"));
        matching.setSujetRencontre(rs.getString("sujet_rencontre"));
        matching.setNumTable(rs.getInt("num_table"));
        matching.setNbrPersonneMatchy(rs.getInt("nbr_personne_matchy"));
        matching.setImage(rs.getString("image"));

        // Get the user (host) of the matching
        UserService userService = new UserService();
        User user = userService.getById(rs.getInt("user_id"));
        matching.setUser(user);

        return matching;
    }

    public List<Matching> getByUserId(int userId) throws SQLException {
        List<Matching> matchings = new ArrayList<>();
        sql = "SELECT * FROM matching WHERE user_id=?";

        try (PreparedStatement st = cnx.prepareStatement(sql)) {
            st.setInt(1, userId);

            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    matchings.add(resultSetToMatching(rs));
                }
            }
        }
        return matchings;
    }

    public List<Matching> getAssessorMatchings(int userId) throws SQLException {
        List<Matching> matchings = new ArrayList<>();
        sql = "SELECT m.* FROM matching m " +
                "JOIN matching_user mu ON m.id = mu.matching_id " +
                "WHERE mu.user_id=?";

        try (PreparedStatement st = cnx.prepareStatement(sql)) {
            st.setInt(1, userId);

            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    matchings.add(resultSetToMatching(rs));
                }
            }
        }
        return matchings;
    }

    public void addAssessorToMatching(int matchingId, int userId) throws SQLException {
        // Insert the relationship into the matching_user table
        String sql = "INSERT INTO matching_user (matching_id, user_id) VALUES (?, ?)";

        try (PreparedStatement stmt = cnx.prepareStatement(sql)) {
            stmt.setInt(1, matchingId);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
            System.out.println("User with ID " + userId + " added as an assessor to matching with ID " + matchingId);
        } catch (SQLException e) {
            System.err.println("Error adding assessor to matching: " + e.getMessage());
            throw e;
        }
    }
}