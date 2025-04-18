package entity;

public class Commande {

    private int id_commande;
    private int quantite_produit; // Corrigé
    private double prix_total_commande;
    private Produit produit; // clé étrangère vers Produit
    private User user;

    // Getters et setters
    public int getId_commande() {
        return id_commande;
    }

    public void setId_commande(int id_commande) {
        this.id_commande = id_commande;
    }

    public int getQuantite_produit() { // Corrigé
        return quantite_produit;
    }

    public void setQauntite_produit(int qauntite_produit) {
        this.quantite_produit = qauntite_produit;
    }

    public double getPrix_total_commande() {
        return prix_total_commande;
    }

    public void setPrix_total_commande(double prix_total_commande) {
        this.prix_total_commande = prix_total_commande;
    }

    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // Constructeurs
    public Commande() {
    }

    public Commande(int id_commande, double prix_total_commande, int quantite_produit, Produit produit, User user) {
        this.id_commande = id_commande;
        this.prix_total_commande = prix_total_commande;
        this.quantite_produit = quantite_produit;
        this.produit = produit;
        this.user = user;
    }

    public Commande(double prix_total_commande, int quantite_produit, Produit produit, User user) {
        this.prix_total_commande = prix_total_commande;
        this.quantite_produit = quantite_produit;
        this.produit = produit;
        this.user = user;
    }

    @Override
    public String toString() {
        return "Commande{" +
                "id_commande=" + id_commande +
                ", quantite_produit=" + quantite_produit +
                ", prix_total_commande=" + prix_total_commande +
                ", produit=" + (produit != null ? produit.getNom_produit() : "null") +
                '}';
    }
}
