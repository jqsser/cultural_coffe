package service;

import entity.Evenement;
import entity.User;

import java.sql.Date;
import java.util.List;

import java.sql.SQLException;
import java.util.List;
public interface IReservation<T> {
    void ajouterReservation(T t) throws SQLException;
    void supprimerReservation(int id) throws SQLException;
    void modifierReservation(int id, int nbr_places, Evenement evenement, User user, String statut_booking, String moyen_payement_booking, Date date_booking) throws SQLException;
    List<T> recupererReservation() throws SQLException;
}
