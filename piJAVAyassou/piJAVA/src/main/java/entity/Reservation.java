package entity;

import java.sql.Date;

public class Reservation {
    private int id,nbr_places;
    private Evenement evenement;
    private User user;

    private String statut_booking,moyen_payement_booking;
    private Date date_booking;

    public Reservation(int nbr_places, Evenement evenement, User user, String statut_booking, String moyen_payement_booking, Date date_booking) {
        this.nbr_places = nbr_places;
        this.evenement = evenement;
        this.user = user;
        this.statut_booking = statut_booking;
        this.moyen_payement_booking = moyen_payement_booking;
        this.date_booking = date_booking;
    }

    public Reservation(int id, int nbr_places, Evenement evenement, User user, String statut_booking, String moyen_payement_booking, Date date_booking) {
        this.id = id;
        this.nbr_places = nbr_places;
        this.evenement = evenement;
        this.user = user;
        this.statut_booking = statut_booking;
        this.moyen_payement_booking = moyen_payement_booking;
        this.date_booking = date_booking;
    }

    public Reservation() {
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", nbr_places=" + nbr_places +
                ", evenement=" + evenement +
                ", user=" + user +
                ", statut_booking='" + statut_booking + '\'' +
                ", moyen_payement_booking='" + moyen_payement_booking + '\'' +
                ", date_booking=" + date_booking +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNbr_places() {
        return nbr_places;
    }

    public void setNbr_places(int nbr_places) {
        this.nbr_places = nbr_places;
    }

    public Evenement getEvenement() {
        return evenement;
    }

    public void setEvenement(Evenement evenement) {
        this.evenement = evenement;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getStatut_booking() {
        return statut_booking;
    }

    public void setStatut_booking(String statut_booking) {
        this.statut_booking = statut_booking;
    }

    public String getMoyen_payement_booking() {
        return moyen_payement_booking;
    }

    public void setMoyen_payement_booking(String moyen_payement_booking) {
        this.moyen_payement_booking = moyen_payement_booking;
    }

    public Date getDate_booking() {
        return date_booking;
    }

    public void setDate_booking(Date date_booking) {
        this.date_booking = date_booking;
    }
}
