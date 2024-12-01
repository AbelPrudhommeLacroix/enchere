import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class MenuResult {

    public static void launch(Connection conn, Scanner scanner) {
        System.out.println("\n=========== Résultat d'une enchère ===========\n");
        
        //Affichage des salles de ventes
        try {
            System.out.println("------ Liste des salles de vente disponibles ------");
            String salles = DBQueries.getSallesDeVente(conn);
            System.out.println(salles);
        } catch (SQLException e) {
            System.err.println("[!] Erreur dans l'affichage des salles : " + e);
            return;
        }
    
        //Choix d'une salle de vente
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
            String ventes = DBQueries.getVentes(conn, id_salle, false);
            System.out.println(ventes);
        } catch (SQLException e) {
            System.err.println("[!] Erreur dans l'affichage des ventes : " + e);
            return;
        }

        //Choix de la vente
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

        //On verifie que l'enchère est bien finie
        try {
            if (!DBQueries.isEnchereFinie(conn, id_vente))
                throw new Exception();  
        }catch (Exception e) {
            System.err.println("[!] L'enchère sur cette vente n'est pas finie");
            return;
        }
        
        //Afficher le gagnant
        try {
            String gagnant = DBQueries.getEmailEtValeurMeilleureOffre(conn, id_vente);
            System.out.println("\n[☺] Gagnant de l'enchère : \n" + gagnant);
        } catch (Exception e) {
            System.err.println("[!] Erreur en essayant de récuperer l'offre");
            return;
        }
    }
}