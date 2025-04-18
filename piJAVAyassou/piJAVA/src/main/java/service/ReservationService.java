package service;

import entity.Evenement;
import entity.Reservation;
import entity.User;
import tools.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationService implements IReservation<Reservation> {

    Connection cnx;
    String sql;
    private Statement st;
    private PreparedStatement ste;

    public ReservationService() {
        cnx = MyDataBase.getInstance().getCnx();
    }

    @Override
    public void ajouterReservation(Reservation reservation) throws SQLException {
        String sql = "INSERT INTO reservation (nbr_places, evenement_id, user_id, statut_booking, moyen_payement_booking, date_booking) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement ste = cnx.prepareStatement(sql);

        ste.setInt(1, reservation.getNbr_places());
        ste.setInt(2, reservation.getEvenement().getId());
        ste.setInt(3, reservation.getUser().getId());
        ste.setString(4, reservation.getStatut_booking());
        ste.setString(5, reservation.getMoyen_payement_booking());
        ste.setDate(6, reservation.getDate_booking());
        ste.executeUpdate();
    }


    @Override
    public void supprimerReservation(int id) throws SQLException {
        String sql = "DELETE FROM reservation WHERE id=?";
        ste = cnx.prepareStatement(sql);
        ste.setInt(1, id);
        ste.executeUpdate();
    }


    @Override
    public void modifierReservation(int id, int nbr_places, Evenement evenement, User user, String statut_booking, String moyen_payement_booking, Date date_booking) throws SQLException {

        String sql = "UPDATE reservation SET nbr_places=?, evenement_id=?, user_id=?, statut_booking=?, " +
                "moyen_payement_booking=?, date_booking=? WHERE id=?";

        PreparedStatement ste = cnx.prepareStatement(sql);


        ste.setInt(1, nbr_places);
        ste.setInt(2, evenement.getId());
        ste.setInt(3, user.getId());
        ste.setString(4, statut_booking);
        ste.setString(5, moyen_payement_booking);
        ste.setDate(6, date_booking);
        ste.setInt(7, id);

        ste.executeUpdate();
    }



    @Override
    public List<Reservation> recupererReservation() throws SQLException {
        List<Reservation> reservations = new ArrayList<>();

        String sql = "SELECT r.id, r.nbr_places, r.statut_booking, r.moyen_payement_booking, r.date_booking, " +
                "e.id AS evenement_id, e.titre_evenement, e.image_event, e.type_event, e.description_event, e.date_event, e.capacite_max, e.prix_event, " +
                "u.id AS user_id, u.nom_user, u.prenom_user, u.email_user, u.telephone_user, u.adresse, u.type_user, u.role_user, u.photo_user, u.date_naissance_user " +
                "FROM reservation r " +
                "JOIN evenement e ON r.evenement_id = e.id " +
                "JOIN user u ON r.user_id = u.id";

        try (Statement statement = cnx.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            while (rs.next()) {
                // Créer l'objet Evenement
                Evenement evenement = new Evenement();
                evenement.setId(rs.getInt("evenement_id"));
                evenement.setTitre_evenement(rs.getString("titre_evenement"));
                evenement.setImage_event(rs.getString("image_event"));
                evenement.setType_event(rs.getString("type_event"));
                evenement.setDescription_event(rs.getString("description_event"));
                evenement.setDate_event(rs.getDate("date_event"));
                evenement.setCapacite_max(rs.getInt("capacite_max"));
                evenement.setPrix_event(rs.getDouble("prix_event"));

                // Créer l'objet User
                User user = new User();
                user.setId(rs.getInt("user_id"));
                user.setNom_user(rs.getString("nom_user"));
                user.setPrenom_user(rs.getString("prenom_user"));
                user.setEmail_user(rs.getString("email_user"));
                user.setTelephone_user(rs.getInt("telephone_user"));
                user.setAdresse(rs.getString("adresse"));
                user.setRole_user(rs.getString("role_user"));
                user.setPhoto_user(rs.getString("photo_user"));
                user.setDate_naissance_user(rs.getDate("date_naissance_user"));

                // Créer l'objet Reservation
                Reservation reservation = new Reservation(
                        rs.getInt("id"),
                        rs.getInt("nbr_places"),
                        evenement,
                        user,
                        rs.getString("statut_booking"),
                        rs.getString("moyen_payement_booking"),
                        rs.getDate("date_booking")
                );

                reservations.add(reservation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Erreur lors de la récupération des réservations", e);
        }

        return reservations;
    }






}
