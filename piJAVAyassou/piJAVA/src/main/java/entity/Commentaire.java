package entity;

public class Commentaire {
    private int id;
    private int post_id;
    private int user_id;
    private String contenu;
    private String date_commentaire;
    private int nbr_like_commentaire;
    public Commentaire() {}

    public Commentaire(int post_id, int user_id, String contenu, String date_commentaire, int nbr_like_commentaire) {
        this.post_id = post_id;
        this.user_id = user_id;
        this.contenu = contenu;
        this.date_commentaire = date_commentaire;
        this.nbr_like_commentaire = nbr_like_commentaire;
    }

    // Constructeur avec ID
    public Commentaire(int id, int post_id, int user_id, String contenu, String date_commentaire, int nbr_like_commentaire) {
        this.id = id;
        this.post_id = post_id;
        this.user_id = user_id;
        this.contenu = contenu;
        this.date_commentaire = date_commentaire;
        this.nbr_like_commentaire = nbr_like_commentaire;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public String getDate_commentaire() {
        return date_commentaire;
    }

    public void setDate_commentaire(String date_commentaire) {
        this.date_commentaire = date_commentaire;
    }

    public int getNbr_like_commentaire() {
        return nbr_like_commentaire;
    }

    public void setNbr_like_commentaire(int nbr_like_commentaire) {
        this.nbr_like_commentaire = nbr_like_commentaire;
    }

    @Override
    public String toString() {
        return "Commentaire{" +
                "id=" + id +
                ", post_id=" + post_id +
                ", user_id=" + user_id +
                ", contenu='" + contenu + '\'' +
                ", date_commentaire='" + date_commentaire + '\'' +
                ", nbr_like_commentaire=" + nbr_like_commentaire +
                '}';
    }
}



