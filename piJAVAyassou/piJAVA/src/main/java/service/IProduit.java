package service;

import entity.Produit;

import java.sql.SQLException;
import java.util.List;
import entity.Produit;
import entity.User;



public interface IProduit<T>{


    void supprimerProduit(int id) throws SQLException;

    void modifierProduit( T produit) throws SQLException;
    void ajouterProduit(T P) throws SQLException;

    List<T> recupererProduits() throws SQLException;
     List<Produit> recupererProduits1() throws SQLException;
}
