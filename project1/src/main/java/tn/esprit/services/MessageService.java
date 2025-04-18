
package tn.esprit.services;

import tn.esprit.entities.Message;
import tn.esprit.entities.Matching;
import tn.esprit.entities.User;
import tn.esprit.tools.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageService implements IService<Message> {
    private Connection cnx;
    private String sql;

    public MessageService() {
        cnx = MyDataBase.getInstance().getCnx();
    }

    @Override
    public void ajouter(Message message) throws SQLException {
        sql = "INSERT INTO message (content, matching_id, user_id, updated_message, created_at) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ste = cnx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ste.setString(1, message.getContent());
            ste.setInt(2, message.getMatching().getId());
            ste.setInt(3, message.getUser().getId());
            ste.setBoolean(4, message.isUpdatedMessage());
            ste.setTimestamp(5, Timestamp.valueOf(message.getCreatedAt()));
            ste.executeUpdate();

            try (ResultSet rs = ste.getGeneratedKeys()) {
                if (rs.next()) {
                    message.setId(rs.getInt(1));
                }
            }
        }
    }

    public void modifier(Message message) throws SQLException {
        String req = "UPDATE message SET content = ?, updated_message = ? WHERE id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setString(1, message.getContent());
            ps.setBoolean(2, message.isUpdatedMessage());
            ps.setInt(3, message.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void supprimer(Message message) throws SQLException {

        String req = "DELETE FROM message WHERE id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setInt(1, message.getId());
            ps.executeUpdate();
        }
    }

    public void supprimer(int id) throws SQLException {

        String req = "DELETE FROM message WHERE id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setInt(1,id);
            ps.executeUpdate();
        }
    }

    @Override
    public List<Message> recuperer() throws SQLException {
        List<Message> messages = new ArrayList<>();
        sql = "SELECT * FROM message";

        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                messages.add(resultSetToMessage(rs));
            }
        }
        return messages;
    }

    public List<Message> getByMatching(int matchingId) throws SQLException {
        List<Message> messages = new ArrayList<>();
        sql = "SELECT * FROM message WHERE matching_id=? ORDER BY created_at ASC";

        try (PreparedStatement st = cnx.prepareStatement(sql)) {
            st.setInt(1, matchingId);

            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    messages.add(resultSetToMessage(rs));
                }
            }
        }
        return messages;
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

    private Message resultSetToMessage(ResultSet rs) throws SQLException {
        Message message = new Message();
        message.setId(rs.getInt("id"));
        message.setContent(rs.getString("content"));
        message.setUpdatedMessage(rs.getBoolean("updated_message"));
        message.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

        // Get matching
        MatchingService matchingService = new MatchingService();
        Matching matching = matchingService.getById(rs.getInt("matching_id"));
        message.setMatching(matching);

        // Get user
        UserService userService = new UserService();
        User user = userService.getById(rs.getInt("user_id"));
        message.setUser(user);

        return message;
    }
}