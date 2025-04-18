package service;

import java.sql.SQLException;
import java.util.List;

import entity.Commande;
import entity.User;
import entity.Produit;

public interface ICommande<T> {
    void ajouterCommande(T t) throws SQLException;
    void supprimerCommande(int id) throws SQLException;
    void modifierCommande(Commande commande) throws SQLException;
    List<T> recupererCommandes() throws SQLException;
}
