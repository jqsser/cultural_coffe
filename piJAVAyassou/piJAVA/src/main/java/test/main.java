package test;

import entity.Post;
import service.PostService;

import entity.Commentaire;
import service.CommentaireService;
import tools.MyDataBase;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import static service.CommentaireService.*;


public class main {
    public static void main(String[] args) {
        PostService postService = new PostService();
        CommentaireService commentaireService = new CommentaireService();

        try {
          /*  Post newPost = new Post(1, "Mon premier post", Date.valueOf("2024-04-01"), "image.jpg", true);
            postService.ajouter(newPost);

            // Récupérer et afficher les posts
            List<Post> posts = postService.recuperer();
            for (Post p : posts) {
                System.out.println(p);
            }

            //modif
            postService.modifier(2, "changer le contenu du post en java");
            System.out.println("✅ Post modifié avec succès !");

            //supprim
            Post postASupprimer = new Post();
            postASupprimer.setId(8);
            postService.supprimer(postASupprimer);
            System.out.println("✅ Post supprimé avec succès !");*/

            /////////commentaire
            /*Commentaire newComment = new Commentaire(1, 1, "Ceci est un commentaire.", "2024-04-03", 5);
            commentaireService.ajouterCommentaire(newComment);
            System.out.println("✅ Commentaire ajouté avec succès !");

            commentaireService.modifierCommentaire(4, "Contenu du commentaire modifié.");
            System.out.println("✅ Commentaire modifié avec succès !");

            // Supprimer un commentaire
            Commentaire commentaireASupprimer = new Commentaire();
            commentaireASupprimer.setId(4);
            commentaireService.supprimerCommentaire(commentaireASupprimer);*/
            // Récupérer et afficher les commentaires
            List<Commentaire> commentaires = commentaireService.recupererCommentaire();
            for (Commentaire c : commentaires) {
                System.out.println(c);
            }

        } catch (SQLException e) {
            e.printStackTrace();

        }
    }
}
