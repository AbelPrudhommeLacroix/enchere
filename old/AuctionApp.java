import java.sql.Connection;
import java.util.Scanner;

public class AuctionApp {

    public static void accueil() {
        // Connexion à la base de données
        Connection conn = DatabaseConnection.getConnection();
        if (conn != null) {
            Scanner scanner = new Scanner(System.in);
            boolean exit = false;

            while (!exit) {
                System.out.println("\n=== Main Menu ===");
                System.out.println("1. Log in");
                System.out.println("2. Register");
                System.out.println("3. Exit");
                System.out.print("Choice: ");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1 -> DBHandler.login(conn, scanner);
                    case 2 -> DBHandler.register(conn, scanner);
                    case 3 -> {
                        System.out.println("Goodbye!");
                        exit = true;
                    }
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            }
            scanner.close();
        } else {
            System.err.println("Unable to connect to the database.");
        }
        DatabaseConnection.closeConnection(conn);
    }

    public static void menuEnchere(Connection conn, Scanner scanner) {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n=== Auction Menu ===");
            System.out.println("1. List a product");
            System.out.println("2. Bid");
            System.out.println("3. View auction status");
            System.out.println("4. Exit");
            System.out.print("Choice: ");
            int choice = scanner.nextInt();

        switch (choice) {
            case 1 -> DBQueries.ajoutProduit(conn, scanner);
            case 2 -> DBQueries.encherir(conn, scanner);
            case 3 -> DBQueries.etatVente(conn, scanner);
            case 4 -> {
                System.out.println("Au revoir !");
                exit = true;
                }
            }
        }
    }
    
    public static void main(String[] args) {
        System.out.println("Welcome to the auction application!");
        accueil();
    }
}
