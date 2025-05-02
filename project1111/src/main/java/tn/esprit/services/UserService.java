package tn.esprit.services;

import tn.esprit.entities.User;
import tn.esprit.tools.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserService implements IService<User> {
    private Connection cnx;
    private String sql;

    public UserService() {
        cnx = MyDataBase.getInstance().getCnx();
    }

    @Override
    public void ajouter(User user) throws SQLException {
        sql = "INSERT INTO user (password, role_user, nom_user, prenom_user, email_user, " +
                "adresse, telephone_user, photo_user, date_naissance_user) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ste = cnx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setUserParameters(ste, user);
            ste.executeUpdate();

            try (ResultSet generatedKeys = ste.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getInt(1));
                }
            }
        }
    }
    public User getByUsername(String username) throws SQLException {
        sql = "SELECT * FROM user WHERE username=?";

        try (PreparedStatement st = cnx.prepareStatement(sql)) {
            st.setString(1, username);

            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setNomUser(rs.getString("username"));
                    // set other fields as needed
                    return user;
                }
            }
        }
        return null;
    }

    @Override
    public void modifier(User user) throws SQLException {
        sql = "UPDATE user SET password=?, role_user=?, nom_user=?, prenom_user=?, " +
                "email_user=?, adresse=?, telephone_user=?, photo_user=?, date_naissance_user=? " +
                "WHERE id=?";

        try (PreparedStatement ste = cnx.prepareStatement(sql)) {
            setUserParameters(ste, user);
            ste.setInt(10, user.getId());
            ste.executeUpdate();
        }
    }

    @Override
    public void supprimer(int id) throws SQLException {
        // First delete from matching_user join table
        sql = "DELETE FROM matching_user WHERE user_id=?";
        try (PreparedStatement stmt = cnx.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }

        // Then delete the user
        sql = "DELETE FROM user WHERE id=?";
        try (PreparedStatement stmt = cnx.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public List<User> recuperer() throws SQLException {
        List<User> users = new ArrayList<>();
        sql = "SELECT * FROM user";

        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while(rs.next()) {
                users.add(resultSetToUser(rs));
            }
        }
        return users;
    }

    @Override
    public User getById(int id) throws SQLException {
        sql = "SELECT * FROM user WHERE id=?";

        try (PreparedStatement st = cnx.prepareStatement(sql)) {
            st.setInt(1, id);

            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return resultSetToUser(rs);
                }
            }
        }
        return null;
    }

    // Helper methods
    private void setUserParameters(PreparedStatement ste, User user) throws SQLException {
        ste.setString(1, user.getPassword());
        ste.setString(2, user.getRoleUser());
        ste.setString(3, user.getNomUser());
        ste.setString(4, user.getPrenomUser());
        ste.setString(5, user.getEmailUser());
        ste.setString(6, user.getAdresse());
        ste.setInt(7, user.getTelephoneUser());
        ste.setString(8, user.getPhotoUser());
        ste.setDate(9, Date.valueOf(user.getDateNaissanceUser()));
    }

    private User resultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setPassword(rs.getString("password"));
        user.setRoleUser(rs.getString("role_user"));
        user.setNomUser(rs.getString("nom_user"));
        user.setPrenomUser(rs.getString("prenom_user"));
        user.setEmailUser(rs.getString("email_user"));
        user.setAdresse(rs.getString("adresse"));
        user.setTelephoneUser(rs.getInt("telephone_user"));
        user.setPhotoUser(rs.getString("photo_user"));

        Date date = rs.getDate("date_naissance_user");
        if (date != null) {
            user.setDateNaissanceUser(date.toLocalDate());
        }

        return user;
    }
}