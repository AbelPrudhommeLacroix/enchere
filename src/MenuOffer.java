import java.util.Scanner;
import java.sql.*;

public class MenuOffer {

    public static void launch(Connection conn, Scanner scanner) {

        System.out.println("\n=========== Création d'une offre ===========\n");
    
        // Affichage de l'id salles de vente et de leur catégorie associée
        try {
            System.out.println("------ Liste des salles de vente disponibles ------");
            String salles = DBQueries.getSallesDeVente(conn);
            System.out.println(salles);
        } catch (SQLException e) {
            System.err.println("[!] Erreur dans l'affichage des salles : " + e);
            return;
        }
    
        // L'utilisateur choisit une salle de vente
        int id_salle = -1;
        while (id_salle == -1) {
            System.out.print("Choisissez l'id de la salle de vente : ");
            if (scanner.hasNextInt()) {
                id_salle = scanner.nextInt();
                try {
                    if (!DBQueries.doesSalleExist(conn, id_salle)) {
                        System.err.println("[!] La salle n'existe pas / n'a pas été trouvée");
                        id_salle = -1; // Réinitialiser pour redemander
                    }
                } catch (Exception e) {
                    System.err.println("[!] Erreur lors de la vérification de la salle : " + e);
                    id_salle = -1; // Réinitialiser pour redemander
                }
            } else {
                System.err.println("[!] Entrée invalide. Veuillez entrer un nombre entier.");
                scanner.next(); // Consommer l'entrée invalide
            }
        }
    
        // Affichage des ventes disponibles
        try {
            System.out.println("\n------ Liste des ventes disponibles ------");
            String ventes = DBQueries.getVentes(conn, id_salle, true);
            System.out.println(ventes);
        } catch (SQLException e) {
            System.err.println("[!] Erreur dans l'affichage des ventes : " + e);
            return;
        }
    
        // L'utilisateur choisit une vente donc un produit
        int id_vente = -1;
        while (id_vente == -1) {
            System.out.print("Choisissez l'id d'une vente : ");
            if (scanner.hasNextInt()) {
                id_vente = scanner.nextInt();
                try {
                    if (!DBQueries.doesVenteExist(conn, id_vente)) {
                        System.err.println("[!] La vente n'existe pas / n'a pas été trouvée");
                        id_vente = -1; // Réinitialiser pour redemander
                    }
                } catch (Exception e) {
                    System.err.println("[!] Erreur lors de la vérification de la vente : " + e);
                    id_vente = -1; // Réinitialiser pour redemander
                }
            } else {
                System.err.println("[!] Entrée invalide. Veuillez entrer un nombre entier.");
                scanner.next(); // Consommer l'entrée invalide
            }
        }
    
        // L'utilisateur fait une offre sur un produit pour un prix et une quantité qu'il donne
        float prix = -1;
        try {
            // Vérification si la vente est croissante, et récupération du prix de départ si nécessaire
            if (!DBQueries.isVenteCroissante(conn, id_vente)) {
                prix = DBQueries.getPrixDepartVente(conn, id_vente);
            }
        } catch (SQLException e) {
            System.err.println("[!] Erreur lors de la vérification ou récupération du prix de départ : " + e.getMessage());
            return;
        }

        while (prix == -1) {
            System.out.print("Prix de l'offre (ex : 10.50) : ");
            if (scanner.hasNextFloat()) {
                prix = scanner.nextFloat();
                try {
                    // Validation du prix
                    DBQueries.isPrixValide(conn, id_vente, prix); // Valider le prix avec la nouvelle fonction
                } catch (IllegalArgumentException e) {
                    System.err.println("[!] L'offre n'est pas valide : " + e.getMessage());
                    prix = -1; // Réinitialiser pour redemander
                } catch (SQLException e) {
                    System.err.println("[!] Erreur SQL lors de la validation du prix : " + e.getMessage());
                    prix = -1; // Réinitialiser pour redemander
                }
            } else {
                System.err.println("[!] Entrée invalide. Veuillez entrer un nombre décimal.");
                scanner.next(); // Consommer l'entrée invalide
            }
        }

        int quantite = -1;
        while (quantite == -1) {
            System.out.print("Quantité de l'offre : ");
            if (scanner.hasNextInt()) {
                quantite = scanner.nextInt();
                if (quantite <= 0) {
                    System.err.println("[!] La quantité doit être strictement positive.");
                    quantite = -1; // Réinitialiser pour redemander
                } else {
                    try {
                        // Validation de la quantité
                        DBQueries.isQuantiteValide(conn, id_vente, quantite); // Valider la quantité avec la nouvelle fonction
                    } catch (IllegalArgumentException e) {
                        System.err.println("[!] L'offre n'est pas valide : " + e.getMessage());
                        quantite = -1; // Réinitialiser pour redemander
                    } catch (SQLException e) {
                        System.err.println("[!] Erreur SQL lors de la validation de la quantité : " + e.getMessage());
                        quantite = -1; // Réinitialiser pour redemander
                    }
                }
            } else {
                System.err.println("[!] Entrée invalide. Veuillez entrer un nombre entier.");
                scanner.next(); // Consommer l'entrée invalide
            }
        }
    
        String email = "";
        while (email.isEmpty()) {
            System.out.print("Email de l'acheteur : ");
            email = scanner.next();
            try {
                DBQueries.isEmailValide(conn, email); // Valider l'email
            }
            catch (IllegalArgumentException e) {
                System.err.println("[!] L'email n'est pas valide : " + e);
                email = ""; // Réinitialiser pour redemander
            }
            catch (Exception e) {
                System.err.println("[!] L'email n'est pas valide : " + e);
                email = ""; // Réinitialiser pour redemander
            }
        }
    


        // Création de l'offre associée à la date, l'email et l'id de la vente
        try {
            DBQueries.creationOffre(conn, prix, quantite, id_vente, email);
        } catch (Exception e) {
            System.err.println("[!] Erreur lors de la création de l'offre : " + e.getMessage());
            return;
        }

        System.out.println("\n[☺] Offre crée !");
    }
}