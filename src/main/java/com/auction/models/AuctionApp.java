/* point d'entrée principal de ton application Java
initialise l'interface utilisateur et configure les 
scènes pour naviguer entre les différentes fenêtres
*/

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class AuctionApp {

    public static void accueil(void) {
        // Connexion à la base de données
        Connection conn = null;
        try {
            // connexion base de données
            // System.out.println("Connexion à la base de données réussie.");

            Scanner scanner = new Scanner(System.in);
            boolean exit = false;

            while (!exit) {
                System.out.println("\n=== Menu Principal ===");
                System.out.println("1. Se connecter");
                System.out.println("2. S'inscrire");
                System.out.println("3. Quitter");
                System.out.print("Choix : ");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1 -> Auction.login(conn, scanner);
                    case 2 -> Register.register(conn, scanner);
                    case 3 -> {
                        System.out.println("Au revoir !");
                        exit = true;
                    }
                    default -> System.out.println("Choix invalide. Veuillez réessayer.");
                }
            }

            scanner.close();

        } catch (SQLException e) {
            System.err.println("Erreur lors de la connexion à la base de données : " + e.getMessage());
        } finally {
            // Fermeture de la connexion à la base de données
            if (conn != null) {
                try {
                    conn.close();
                    System.out.println("Connexion à la base de données fermée.");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
        
        
    public static void menuEnchere(void) {
        try {
            // connexion base de données
            // System.out.println("Connexion à la base de données réussie.");

            Scanner scanner = new Scanner(System.in);
            boolean exit = false;

            while (!exit) {
                System.out.println("\n=== Menu Principal ===");
                System.out.println("1. Se connecter");
                System.out.println("2. S'inscrire");
                System.out.println("3. Quitter");
                System.out.print("Choix : ");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1 -> Auction.login(conn, scanner);
                    case 2 -> Register.register(conn, scanner);
                    case 3 -> {
                        System.out.println("Au revoir !");
                        exit = true;
                    }
                    default -> System.out.println("Choix invalide. Veuillez réessayer.");
                }
            }
        }
    }

}
