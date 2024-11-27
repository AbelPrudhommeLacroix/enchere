/* point d'entrée principal de ton application Java
initialise l'interface utilisateur et configure les 
scènes pour naviguer entre les différentes fenêtres
*/

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class Auction {

    public static void vente(void) {
        // Connexion à la base de données
        Connection conn = null;
        try {
            // connexion base de données
            // System.out.println("Connexion à la base de données réussie.");

            Scanner scanner = new Scanner(System.in);
            boolean exit = false;

            while (!exit) {
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
        
    /*
    Ajout d'un produit à la BDD avec toutes ses caractéristiques
    */
    public static void ajoutProduit(void) {
        try {
            System.out.println("\n=== Ajout de votre produit ===");
            System.out.print("Nom : ");
            String nom = scanner.next();

            System.out.print("Prix de revient : ");
            float prixRevient = scanner.next();

            System.out.print("Stock : ");
            int stock = scanner.next();

            System.out.print("Catégorie : ");
            String categorie = scanner.next();

            int idProduit = 0       // générer un idProduit

            String insertUserSql = "INSERT INTO Produit (IdProduit, nom, prixRevient, stock, categorie) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement insertStmt = conn.prepareStatement(insertUserSql)){
                insertStmt.setString(1, idProduit);
                insertStmt.setString(2, nom);
                insertStmt.setString(3, prix);
                insertStmt.setString(4, stock);
                insertStmt.setString(5, stock);
                System.out.println("Produit ajouté");
            }


        // ajout caractéristiques
        System.out.print("Avez vous des caractéristiques à préciser : ");
        System.out.print("1. OUI ");
        System.out.print("2. NON ");
        System.out.print("Choix : ");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1 -> Auction.ajoutCaracteristiques(idProduit);
            case 2 -> Auction.vente()       // retour à la page de vente
            }
        

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Une erreur est survenue lors de l'inscription.");
        }
        AuctionApp.main();
    }



    public static void creationSalle(String categorie) {}

    public static void creationVente(){}

}
