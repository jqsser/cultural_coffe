package entity;

import java.sql.Date;

public class User {
    private int id,telephone_user;
    private String role_user,nom_user,prenom_user,email_user,adresse,photo_user;
    private Date date_naissance_user;

    public User() {
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", telephone_user=" + telephone_user +
                ", role_user='" + role_user + '\'' +
                ", nom_user='" + nom_user + '\'' +
                ", prenom_user='" + prenom_user + '\'' +
                ", email_user='" + email_user + '\'' +
                ", adresse='" + adresse + '\'' +
                ", photo_user='" + photo_user + '\'' +
                ", date_naissance_user=" + date_naissance_user +
                '}';
    }

    public User(int id, int telephone_user, String role_user, String nom_user, String prenom_user, String email_user, String adresse, String photo_user, Date date_naissance_user) {
        this.id = id;
        this.telephone_user = telephone_user;
        this.role_user = role_user;
        this.nom_user = nom_user;
        this.prenom_user = prenom_user;
        this.email_user = email_user;
        this.adresse = adresse;
        this.photo_user = photo_user;
        this.date_naissance_user = date_naissance_user;
    }

    public User(int telephone_user, String role_user, String nom_user, String prenom_user, String email_user, String adresse, String photo_user, Date date_naissance_user) {
        this.telephone_user = telephone_user;
        this.role_user = role_user;
        this.nom_user = nom_user;
        this.prenom_user = prenom_user;
        this.email_user = email_user;
        this.adresse = adresse;
        this.photo_user = photo_user;
        this.date_naissance_user = date_naissance_user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTelephone_user() {
        return telephone_user;
    }

    public void setTelephone_user(int telephone_user) {
        this.telephone_user = telephone_user;
    }



    public String getRole_user() {
        return role_user;
    }

    public void setRole_user(String role_user) {
        this.role_user = role_user;
    }

    public String getNom_user() {
        return nom_user;
    }

    public void setNom_user(String nom_user) {
        this.nom_user = nom_user;
    }

    public String getPrenom_user() {
        return prenom_user;
    }

    public void setPrenom_user(String prenom_user) {
        this.prenom_user = prenom_user;
    }

    public String getEmail_user() {
        return email_user;
    }

    public void setEmail_user(String email_user) {
        this.email_user = email_user;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getPhoto_user() {
        return photo_user;
    }

    public void setPhoto_user(String photo_user) {
        this.photo_user = photo_user;
    }

    public Date getDate_naissance_user() {
        return date_naissance_user;
    }

    public void setDate_naissance_user(Date date_naissance_user) {
        this.date_naissance_user = date_naissance_user;
    }
}
