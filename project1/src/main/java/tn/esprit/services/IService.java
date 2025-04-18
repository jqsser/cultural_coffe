package tn.esprit.services;

import tn.esprit.entities.Matching;

import java.sql.SQLException;
import java.util.List;


public interface IService<T> {
    void ajouter(T t) throws SQLException;
   // void modifier(int id,String nom) throws SQLException;
    void modifier(T t) throws SQLException;
    void supprimer(T t) throws SQLException;
    List<T> recuperer() throws SQLException;


}
