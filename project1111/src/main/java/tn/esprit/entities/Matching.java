package tn.esprit.entities;

import java.util.ArrayList;
import java.util.List;

public class Matching {
    private int id;
    private String name;
    private String sujetRencontre;
    private int numTable;
    private int nbrPersonneMatchy;
    private String image;
    private List<Message> messages = new ArrayList<>();
    private User user;
    private List<User> assessors = new ArrayList<>();

    public Matching() {
    }

    public Matching(String name, String sujetRencontre, int numTable, int nbrPersonneMatchy, String image) {
        this.name = name;
        this.sujetRencontre = sujetRencontre;
        this.numTable = numTable;
        this.nbrPersonneMatchy = nbrPersonneMatchy;
        this.image = image;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSujetRencontre() {
        return sujetRencontre;
    }

    public void setSujetRencontre(String sujetRencontre) {
        this.sujetRencontre = sujetRencontre;
    }

    public int getNumTable() {
        return numTable;
    }

    public void setNumTable(int numTable) {
        this.numTable = numTable;
    }

    public int getNbrPersonneMatchy() {
        return nbrPersonneMatchy;
    }

    public void setNbrPersonneMatchy(int nbrPersonneMatchy) {
        this.nbrPersonneMatchy = nbrPersonneMatchy;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<User> getAssessors() {
        return assessors;
    }

    public void setAssessors(List<User> assessors) {
        this.assessors = assessors;
    }

    @Override
    public String toString() {
        return "Matching{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sujetRencontre='" + sujetRencontre + '\'' +
                ", numTable=" + numTable +
                ", nbrPersonneMatchy=" + nbrPersonneMatchy +
                ", image='" + image + '\'' +
                '}';
    }
}