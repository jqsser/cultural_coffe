package service;

import java.sql.Date;
import java.util.List;

import java.sql.SQLException;
import java.util.List;
public interface IEvenement<T> {
    void ajouterEvenement(T t) throws SQLException;
    void supprimerEvenement(int id) throws SQLException;
    void modifierEvenement(int id, int capacite_max, String titre_evenement, String image_event,
                           String type_event, String description_event, Date date_event,
                           double prix_event) throws SQLException;
    List<T> recupererEvenement() throws SQLException;
}
