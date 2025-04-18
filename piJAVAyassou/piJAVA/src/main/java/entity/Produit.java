package entity;

import java.util.Objects;

public class Produit {
    private int id_produit,stock_produit,etat_produit;
    private double prix_produit;
    private String nom_produit,description_produit,type_produit,image_produit,image2_produit,image3_produit,image4_produit;
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public double getPrix_produit() {
        return prix_produit;
    }

    public void setPrix_produit(double prix_produit) {
        this.prix_produit = prix_produit;
    }

    public int getId_produit() {
        return id_produit;
    }

    public void setId_produit(int id_produit) {
        this.id_produit = id_produit;
    }

    public int getStock_produit() {
        return stock_produit;
    }

    public void setStock_produit(int stock_produit) {
        this.stock_produit = stock_produit;
    }

    public String getType_produit() {
        return type_produit;
    }

    public void setType_produit(String type_produit) {
        this.type_produit = type_produit;
    }

    public int getEtat_produit() {
        return etat_produit;
    }

    public void setEtat_produit(int etat_produit) {
        this.etat_produit = etat_produit;
    }

    public String getNom_produit() {
        return nom_produit;
    }

    public void setNom_produit(String nom_produit) {
        this.nom_produit = nom_produit;
    }

    public String getDescription_produit() {
        return description_produit;
    }

    public void setDescription_produit(String description_produit) {
        this.description_produit = description_produit;
    }

    public String getImage_produit() {
        return image_produit;
    }

    public void setImage_produit(String image_produit) {
        this.image_produit = image_produit;
    }

    public String getImage2_produit() {
        return image2_produit;
    }

    public void setImage2_produit(String image2_produit) {
        this.image2_produit = image2_produit;
    }

    public String getImage3_produit() {
        return image3_produit;
    }

    public void setImage3_produit(String image3_produit) {
        this.image3_produit = image3_produit;
    }

    public String getImage4_produit() {
        return image4_produit;
    }

    public void setImage4_produit(String image4_produit) {
        this.image4_produit = image4_produit;
    }


    public Produit() {
    }

    public Produit(double prix_produit, int stock_produit, String type_produit, int etat_produit,
                   String nom_produit, String description_produit,
                   String image_produit, String image2_produit, String image3_produit, String image4_produit,
                   User user) {
        this.prix_produit = prix_produit;
        this.stock_produit = stock_produit;
        this.type_produit = type_produit;
        this.etat_produit = etat_produit;
        this.nom_produit = nom_produit;
        this.description_produit = description_produit;
        this.image_produit = image_produit;
        this.image2_produit = image2_produit;
        this.image3_produit = image3_produit;
        this.image4_produit = image4_produit;
        this.user = user;
    }

    public Produit(int id_produit, double prix_produit, int stock_produit, String type_produit, int etat_produit,
                   String nom_produit, String description_produit,
                   String image_produit, String image2_produit, String image3_produit, String image4_produit,
                   User user) {
        this.id_produit = id_produit;
        this.prix_produit = prix_produit;
        this.stock_produit = stock_produit;
        this.type_produit = type_produit;
        this.etat_produit = etat_produit;
        this.nom_produit = nom_produit;
        this.description_produit = description_produit;
        this.image_produit = image_produit;
        this.image2_produit = image2_produit;
        this.image3_produit = image3_produit;
        this.image4_produit = image4_produit;
        this.user = user;
    }

    @Override
    public String toString() {
        return "Produit{" +
                "id_produit=" + id_produit +
                ", prix_produit=" + prix_produit +
                ", stock_produit=" + stock_produit +
                ", type_produit='" + type_produit + '\'' +
                ", etat_produit=" + etat_produit +
                ", nom_produit='" + nom_produit + '\'' +
                ", description_produit='" + description_produit + '\'' +
                ", image_produit='" + image_produit + '\'' +
                ", image2_produit='" + image2_produit + '\'' +
                ", image3_produit='" + image3_produit + '\'' +
                ", image4_produit='" + image4_produit + '\'' +
                '}';
    }
}
