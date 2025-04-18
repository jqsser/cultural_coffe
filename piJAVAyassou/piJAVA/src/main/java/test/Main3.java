package test;

import entity.Evenement;
import entity.Produit;
import entity.Reservation;
import entity.User;
import service.EvenementService;
import service.ProduitService;
import service.ReservationService;

public class Main3 {
    public static void main(String[] args) {
        ReservationService rs = new ReservationService();

        // Manually create a User
        User user = new User();
        user.setId(1);
        user.setNom_user("NomExemple");
        user.setPrenom_user("PrenomExemple");
        user.setEmail_user("email@example.com");
        user.setAdresse("AdresseExemple");
        user.setTelephone_user(12345678);
        user.setRole_user("user");
        user.setPhoto_user("photo.jpg");
        user.setDate_naissance_user(java.sql.Date.valueOf("2000-01-01"));

        // Manually create an Evenement
        Evenement evenement = new Evenement();
        evenement.setId(1);
        evenement.setCapacite_max(100);
        evenement.setTitre_evenement("TitreExemple");
        evenement.setImage_event("image.jpg");
        evenement.setType_event("TypeExemple");
        evenement.setDescription_event("Description test");
        evenement.setDate_event(java.sql.Date.valueOf("2024-05-01"));
        evenement.setPrix_event(50.0);

        java.sql.Date dateEvent = java.sql.Date.valueOf("2002-10-21");

        // Create a Reservation object
        Reservation R = new Reservation(12, evenement, user, "xxxx", "sss", dateEvent);

        System.out.println("****************************************AJOUTER*********************************");
        try {
            rs.ajouterReservation(R);
            System.out.println("Réservation ajoutée !!!!! ");
        } catch (Exception e) {
            System.out.println("Erreur ajouter réservation");
            e.printStackTrace();
        }

        System.out.println("****************************************MODIFIER*********************************");
        try {
            rs.modifierReservation(11, 12, evenement, user, "MOIIII", "sss", dateEvent);
            System.out.println("Réservation modifiée !!!!! ");
        } catch (Exception e) {
            System.out.println("Erreur modifier réservation");
            e.printStackTrace();
        }

        System.out.println("****************************************RECUPERER*********************************");
        try {
            System.out.println(rs.recupererReservation());
            System.out.println("Réservation récupérée !!!!! ");
        } catch (Exception e) {
            System.out.println("Erreur récupérer réservation");
            e.printStackTrace();
        }

        System.out.println("****************************************SUPP*********************************");
        try {
            rs.supprimerReservation(11);
            System.out.println("Réservation supprimée !!!!! ");
        } catch (Exception e) {
            System.out.println("Erreur supprimer réservation");
            e.printStackTrace();
        }
    }
}


        /*EvenementService ps = new EvenementService();*/
       /* java.sql.Date dateEvent = java.sql.Date.valueOf("2002-10-21");
        Evenement E = new Evenement(11,"AA", " AA", "ZZZ", " xxxx",dateEvent,10.2);

        System.out.println("****************************************AJOUTER*********************************");
        try {
            ps.ajouterEvenement(E);
            System.out.println("evenement ajouté!!!!!! ");
        }
        catch (Exception e) {
            System.out.println("Erreur ajouterevenement");

        }
        System.out.println("****************************************MODIFIER*********************************");
        try {
            ps.modifierEvenement(20,11,"Moii", " AA", "ZZZ", " xxxx",dateEvent,10.2);
            System.out.println("evenement modifié!!!!!! ");
        }
        catch (Exception e) {
            System.out.println("Erreur modifierevenement");
            e.printStackTrace(); // Affiche l'erreur complète

        }
        System.out.println("****************************************RECUPERER*********************************");

        try {
            System.out.println( ps.recupererEvenement());
            System.out.println("événements recuperé!!!!!! ");
        }
        catch (Exception e) {
            System.out.println("Erreur recupererevenement");
            e.printStackTrace(); // Affiche l'erreur complète

        }
        System.out.println("****************************************SUPP*********************************");

        try {
            ps.supprimerEvenement(13);
            System.out.println("EVe supp!!!!!! ");
        }
        catch (Exception e) {
            System.out.println("Erreur suppEve");
            e.printStackTrace(); // Affiche l'erreur complète

        }*/



