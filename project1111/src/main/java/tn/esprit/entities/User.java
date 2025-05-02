package tn.esprit.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class User {
    private int id;
    private String password;
    private String roleUser;
    private String nomUser;
    private String prenomUser;
    private String emailUser;
    private String adresse;
    private int telephoneUser;
    private String photoUser;
    private LocalDate dateNaissanceUser;
    private List<Matching> matchings = new ArrayList<>(); // For the inverse side of matching relationship
    private List<Matching> assessorMatchings = new ArrayList<>(); // For the many-to-many relationship

    public User() {
    }

    public User(String password, String roleUser, String nomUser, String prenomUser,
                String emailUser, String adresse, int telephoneUser, LocalDate dateNaissanceUser) {
        this.password = password;
        this.roleUser = roleUser;
        this.nomUser = nomUser;
        this.prenomUser = prenomUser;
        this.emailUser = emailUser;
        this.adresse = adresse;
        this.telephoneUser = telephoneUser;
        this.dateNaissanceUser = dateNaissanceUser;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRoleUser() {
        return roleUser;
    }

    public void setRoleUser(String roleUser) {
        this.roleUser = roleUser;
    }

    public String getNomUser() {
        return nomUser;
    }

    public void setNomUser(String nomUser) {
        this.nomUser = nomUser;
    }

    public String getPrenomUser() {
        return prenomUser;
    }

    public void setPrenomUser(String prenomUser) {
        this.prenomUser = prenomUser;
    }

    public String getEmailUser() {
        return emailUser;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public int getTelephoneUser() {
        return telephoneUser;
    }

    public void setTelephoneUser(int telephoneUser) {
        this.telephoneUser = telephoneUser;
    }

    public String getPhotoUser() {
        return photoUser;
    }

    public void setPhotoUser(String photoUser) {
        this.photoUser = photoUser;
    }

    public LocalDate getDateNaissanceUser() {
        return dateNaissanceUser;
    }

    public void setDateNaissanceUser(LocalDate dateNaissanceUser) {
        this.dateNaissanceUser = dateNaissanceUser;
    }

    public List<Matching> getMatchings() {
        return matchings;
    }

    public void setMatchings(List<Matching> matchings) {
        this.matchings = matchings;
    }

    public List<Matching> getAssessorMatchings() {
        return assessorMatchings;
    }

    public void setAssessorMatchings(List<Matching> assessorMatchings) {
        this.assessorMatchings = assessorMatchings;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", nomUser='" + nomUser + '\'' +
                ", prenomUser='" + prenomUser + '\'' +
                ", emailUser='" + emailUser + '\'' +
                ", roleUser='" + roleUser + '\'' +
                '}';
    }



}