package entity;

import java.sql.Date;

public class Evenement {
    private int id,capacite_max;
    private String titre_evenement,image_event,type_event,description_event;
    private Date date_event;
    private double prix_event;

    public Evenement(int capacite_max, String titre_evenement, String image_event, String type_event, String description_event, Date date_event, double prix_event) {
        this.capacite_max = capacite_max;
        this.titre_evenement = titre_evenement;
        this.image_event = image_event;
        this.type_event = type_event;
        this.description_event = description_event;
        this.date_event = date_event;
        this.prix_event = prix_event;
    }
    public Evenement(){

    }

    public Evenement(int id, int capacite_max, String titre_evenement, String image_event, String type_event, String description_event, Date date_event, double prix_event) {
        this.id = id;
        this.capacite_max = capacite_max;
        this.titre_evenement = titre_evenement;
        this.image_event = image_event;
        this.type_event = type_event;
        this.description_event = description_event;
        this.date_event = date_event;
        this.prix_event = prix_event;
    }

    @Override
    public String toString() {
        return "Evenement{" +
                "id=" + id +
                ", capacite_max=" + capacite_max +
                ", titre_evenement='" + titre_evenement + '\'' +
                ", image_event='" + image_event + '\'' +
                ", type_event='" + type_event + '\'' +
                ", description_event='" + description_event + '\'' +
                ", date_event=" + date_event +
                ", prix_event=" + prix_event +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCapacite_max() {
        return capacite_max;
    }

    public void setCapacite_max(int capacite_max) {
        this.capacite_max = capacite_max;
    }

    public String getTitre_evenement() {
        return titre_evenement;
    }

    public void setTitre_evenement(String titre_evenement) {
        this.titre_evenement = titre_evenement;
    }

    public String getImage_event() {
        return image_event;
    }

    public void setImage_event(String image_event) {
        this.image_event = image_event;
    }

    public String getType_event() {
        return type_event;
    }

    public void setType_event(String type_event) {
        this.type_event = type_event;
    }

    public String getDescription_event() {
        return description_event;
    }

    public void setDescription_event(String description_event) {
        this.description_event = description_event;
    }

    public Date getDate_event() {
        return date_event;
    }

    public void setDate_event(Date date_event) {
        this.date_event = date_event;
    }

    public double getPrix_event() {
        return prix_event;
    }

    public void setPrix_event(double prix_event) {
        this.prix_event = prix_event;
    }
}
