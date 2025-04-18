package service;


import entity.Post;
import tools.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostService implements IService<Post> {
    Connection cnx;
    String sql;
    private Statement st;
    private PreparedStatement ste;


    public PostService() {
        cnx = MyDataBase.getInstance().getCnx();
    }

    @Override
    public void ajouter(Post post) throws SQLException {
        sql = "INSERT INTO post (user_id, description_post, date_post, nbr_likes,image, is_validated) VALUES (?, ?, ?, ?, ?, ?)";
        ste = cnx.prepareStatement(sql);
        ste.setInt(1, post.getId_user());
        ste.setString(2, post.getDescription_post());
        ste.setDate(3, post.getDate_post());
        ste.setInt(4, post.getNbr_likes());
        ste.setString(5, post.getImage());
        ste.setBoolean(6, post.isIs_validated());

        ste.executeUpdate();
        System.out.println("Post ajouté avec succès !");
    }



    @Override
    public void modifier(int id, String description_post) throws SQLException {
        sql = "UPDATE post SET description_post=?, date_post = CURRENT_DATE WHERE id=?";
        ste = cnx.prepareStatement(sql);
        ste.setString(1, description_post);
        ste.setInt(2, id);

        ste.executeUpdate();
        System.out.println("Post modifié avec succès !");
    }

    @Override
    public void supprimer(Post post) throws SQLException {
        sql = "DELETE FROM post WHERE id=?";
        ste = cnx.prepareStatement(sql);
        ste.setInt(1, post.getId());

        ste.executeUpdate();
        System.out.println("Post supprimé avec succès !");
    }

    @Override
    public List<Post> recuperer() throws SQLException {
        sql = "SELECT * FROM post";
        st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);

        List<Post> posts = new ArrayList<>();
        while (rs.next()) {
            int id = rs.getInt("id");
            int user_id = rs.getInt("user_id");
            String description_post = rs.getString("description_post");
            Date date_post = rs.getDate("date_post");
            int nbr_likes = rs.getInt("nbr_likes");
            String image = rs.getString("image");
            boolean is_validated = rs.getBoolean("is_validated");

            Post p = new Post(id, user_id, description_post, date_post, nbr_likes, image, is_validated);
            posts.add(p);
        }
        return posts;
    }
}
