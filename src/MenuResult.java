import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class MenuResult {

    public static void launch(Connection conn, Scanner scanner) {
        System.out.println("\n=== Choisissez l'enchère dont vous voulez les infos ===");
        
        try{
            System.out.print("Liste des Id disponibles : ");
            String ids = DBQueries.getIds(conn, scanner);
            System.out.println(ids);
        } catch (SQLException e){
            System.err.println("[!] Erreur lors de la récupération des ids : " + e);
            return; //TODO : boucle while plutot que un return en redemandant
        }
        
        
        // L'utilisateur choisit un id
        int id = -1;
        while (id == -1) {
            System.out.print("Choisissez un Id de vente : ");
            if (scanner.hasNextInt()) {
                id = scanner.nextInt();
                try {
                    if (!DBQueries.doesIdExist(conn, id)) {
                        System.err.println("[!] L'Id' n'existe pas / n'a pas été trouvée");
                        id = -1; // Réinitialiser pour redemander
                    }
                } catch (Exception e) {
                    System.err.println("[!] Erreur lors de la vérification de l'Id' : " + e);
                    id = -1; // Réinitialiser pour redemander
                }

                // affichage infos enchere
            
                try {
                    List<Offre> infos = DBQueries.getGagnantMontantNonRevocableIllimite(conn, id);
                    System.out.println(infos);
                } catch (SQLException e) {
                    System.err.println("[!] Erreur lors de l'affichage des infos de l'enchère : " + e);
                }

            } else {
                System.err.println("[!] Entrée invalide. Veuillez entrer un nombre entier.");
                scanner.next(); // Consommer l'entrée invalide
            }
        }
    }
}