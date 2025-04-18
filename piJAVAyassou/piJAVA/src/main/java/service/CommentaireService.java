package service;

import entity.Commentaire;

import tools.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentaireService implements IServiceCommentaire<Commentaire> {
    Connection cnx;
    String sql;
    private Statement st;
    private PreparedStatement ste;

    public CommentaireService() {
        cnx = MyDataBase.getInstance().getCnx();
    }


    @Override
    public void ajouterCommentaire(Commentaire commentaire) throws SQLException {
        sql = "INSERT INTO commentaire (post_id, user_id, contenu, date_commentaire, nbr_like_commentaire) VALUES (?, ?, ?, ?, ?)";
        ste = cnx.prepareStatement(sql);
        ste.setInt(1, commentaire.getPost_id());
        ste.setInt(2, commentaire.getUser_id());
        ste.setString(3, commentaire.getContenu());
        ste.setString(4, commentaire.getDate_commentaire());
        ste.setInt(5, commentaire.getNbr_like_commentaire());

        ste.executeUpdate();
        System.out.println("Commentaire ajouté avec succès !");
    }

    @Override
    public void supprimerCommentaire(Commentaire commentaire) throws SQLException {
        sql = "DELETE FROM commentaire WHERE id=?";
        ste = cnx.prepareStatement(sql);
        ste.setInt(1, commentaire.getId());

        ste.executeUpdate();
        System.out.println("Commentaire supprimé avec succès !");
    }

    @Override
    public void modifierCommentaire(int id, String contenu) throws SQLException {
        sql = "UPDATE commentaire SET contenu=?, date_commentaire = CURRENT_DATE WHERE id=?";
        ste = cnx.prepareStatement(sql);
        ste.setString(1, contenu);
        ste.setInt(2, id);

        ste.executeUpdate();
        System.out.println("Commentaire modifié avec succès !");
    }

    @Override
    public List<Commentaire> recupererCommentaire() throws SQLException {
        sql = "SELECT * FROM commentaire";
        st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);

        List<Commentaire> commentaires = new ArrayList<>();
        while (rs.next()) {
            int id = rs.getInt("id");
            int post_id = rs.getInt("post_id");
            int user_id = rs.getInt("user_id");
            String contenu = rs.getString("contenu");
            String date_commentaire = rs.getString("date_commentaire");
            int nbr_like_commentaire = rs.getInt("nbr_like_commentaire");

            Commentaire c = new Commentaire(id, post_id, user_id, contenu, date_commentaire, nbr_like_commentaire);
            commentaires.add(c);

        }

        return commentaires;
    }
}