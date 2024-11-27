import java.sql.Connection;
import java.util.Scanner;

public class HCInterface {

    public static void menuPrincipal() {

        // Connexion à la base de données
        Connection conn = DatabaseConnection.getConnection();
        if (conn != null) {
            Scanner scanner = new Scanner(System.in);
            boolean exit = false;

            while (!exit) {
                System.out.println("\n=== Menu Principal ===");
                System.out.println("1. Creer une salle de Vente");
                System.out.println("2. Creer une offre");
                System.out.println("3. Afficher le résultat d'une enchère");
                System.out.println("4. Afficher une table");
                System.out.println("5. Quitter");

                System.out.print("Votre choix : ");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1 -> menuCreationSalle(conn, scanner);
                    case 5 -> {
                        System.out.println("Au revoir !");
                        exit = true;
                    }
                    default -> System.out.println("[!] Choix invalide");
                }
            }
            scanner.close();
        } else {
            System.err.println("Connexion à la BDD impossible.");
        }
        DatabaseConnection.closeConnection(conn);
    }


    public static void menuCreationSalle(Connection conn, Scanner scanner) {
        System.out.println("\n=== Création d'une salle ===");

        System.out.println("Affichage des catégories");
        //TODO : Afficher les catégories
        System.out.println("La catégorie de votre salle : ");
        //TODO : Recuperer la catégorie
        String categorie = null; 
        DBQueries.creationSalle(conn, scanner, categorie);
        
        //TODO : Creer type de vente puis les ventes
        
    }
    
    public static void main(String[] args) {
        menuPrincipal();
    }
}
