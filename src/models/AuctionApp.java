/* point d'entrée principal de ton application Java
initialise l'interface utilisateur et configure les 
scènes pour naviguer entre les différentes fenêtres
*/

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class AuctionApp {

    public static void accueil(Scanner scan) {
        // Connexion à la base de données
        Connection conn = DatabaseConnection.getConnection();
        try {
            if (conn != null) {

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
                        case 1 -> Connection.login(conn, scanner);
                        case 2 -> Connection.register(conn, scanner);
                        case 3 -> {
                            System.out.println("Au revoir !");
                            exit = true;
                        }
                        default -> System.out.println("Choix invalide. Veuillez réessayer.");
                    }
                }
            }
            scanner.close();
            else {
                System.err.println("Impossible de se connecter à la base de données.");
            }

        } finally {
            DatabaseConnection.closeConnection(conn);
        }
    }
        
        
    public static void menuEnchere(Connection conn, Scanner scanner) {
        System.out.println("\n=== Menu Principal ===");
        System.out.println("1. Mettre en vente un produit");
        System.out.println("2. Enchérir");
        System.out.println("3. Voir l'etat d'une vente");
        System.out.println("4. Quitter");
        System.out.print("Choix : ");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1 -> Auction.ajoutProduit(conn, scanner);
            case 2 -> Auction.encherir(conn, scanner);
            case 3 -> Auction.etatVente(conn, scanner);
            case 4 -> {
                System.out.println("Au revoir !");
                exit = true;
                }
            }
        }

        public static void main(String[] args) {
            System.out.println("Bienvenue dans l'application d'enchères !");
            accueil();
        }
    }