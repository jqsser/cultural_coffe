package tn.esprit.services;

import java.sql.SQLException;
import java.util.List;

public interface IService<T> {
    void ajouter(T t) throws SQLException;
    void modifier(T t) throws SQLException;  // Changed to accept full object
    void supprimer(int id) throws SQLException;  // Changed to accept ID
    List<T> recuperer() throws SQLException;
    T getById(int id) throws SQLException;  // Added for single record retrieval
}