package service;
import entity.Post ;

import java.sql.SQLException;
import java.util.List;

  public interface IService<T> {
    void ajouter(T t) throws SQLException;

    void modifier(int id, String description_post) throws SQLException;

    void supprimer(T t) throws SQLException;
    List<T> recuperer() throws SQLException;
}
