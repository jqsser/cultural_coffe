package service;

import entity.Evenement;
import tools.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EvenementService implements IEvenement<Evenement> {

    Connection cnx;
    String sql;
    private Statement st;
    private PreparedStatement ste;

    public EvenementService() {
        cnx = MyDataBase.getInstance().getCnx();
    }

    @Override
    public void ajouterEvenement(Evenement evenement) throws SQLException {
        String sql = "INSERT INTO evenement (capacite_max, titre_evenement, image_event, type_event, description_event, date_event, prix_event) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ste = cnx.prepareStatement(sql);

        ste.setInt(1, evenement.getCapacite_max());
        ste.setString(2, evenement.getTitre_evenement());
        ste.setString(3, evenement.getImage_event());
        ste.setString(4, evenement.getType_event());
        ste.setString(5, evenement.getDescription_event());
        ste.setDate(6, evenement.getDate_event());
        ste.setDouble(7, evenement.getPrix_event());

        ste.executeUpdate();
    }


    @Override
    public void supprimerEvenement(int id) throws SQLException {
        String sql = "DELETE FROM evenement WHERE id=?";
        ste = cnx.prepareStatement(sql);
        ste.setInt(1, id);
        ste.executeUpdate();
    }


    @Override
    public void modifierEvenement(int id, int capacite_max, String titre_evenement, String image_event,
                                  String type_event, String description_event, Date date_event,
                                  double prix_event) throws SQLException {

        String sql = "UPDATE evenement SET capacite_max=?, titre_evenement=?, image_event=?, type_event=?, " +
                "description_event=?, date_event=?, prix_event=? WHERE id=?";

        PreparedStatement ste = cnx.prepareStatement(sql);


        ste.setInt(1, capacite_max);
        ste.setString(2, titre_evenement);
        ste.setString(3, image_event);
        ste.setString(4, type_event);
        ste.setString(5, description_event);
        ste.setDate(6, date_event);
        ste.setDouble(7, prix_event);
        ste.setInt(8, id);

        ste.executeUpdate();
    }


    @Override
    public List<Evenement> recupererEvenement() throws SQLException {
        String sql = "SELECT * FROM evenement";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);

        List<Evenement> evenements = new ArrayList<>();
        while (rs.next()) {
            int id = rs.getInt("id");
            int capacite_max = rs.getInt("capacite_max");
            String titre_evenement = rs.getString("titre_evenement");
            String image_event = rs.getString("image_event");
            String type_event = rs.getString("type_event");
            String description_event = rs.getString("description_event");
            Date date_event = rs.getDate("date_event");
            double prix_event = rs.getDouble("prix_event");

            Evenement e = new Evenement(id, capacite_max, titre_evenement, image_event, type_event, description_event, date_event, prix_event);
            evenements.add(e);
        }

        return evenements;
    }

}
