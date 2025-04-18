package service;

import entity.Commentaire;

import java.util.List;

import java.sql.SQLException;
import java.util.List;

public interface IServiceCommentaire<T> {

    void ajouterCommentaire(T t) throws SQLException;
    void supprimerCommentaire(T t) throws SQLException;
    void modifierCommentaire(int id, String contenu) throws SQLException;
    List<T> recupererCommentaire() throws SQLException;

}
