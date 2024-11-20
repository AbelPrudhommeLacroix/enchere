import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;


public class Connection {

    public static void register(Connection conn, Scanner scanner) {
        try {
            System.out.println("\n=== Inscription ===");
            System.out.print("email : ");
            String email = scanner.next();

            System.out.print("nom : ");
            String nom = scanner.next();

            System.out.print("prénom : ");
            String prenom = scanner.next();

            System.out.print("adresse : ");
            scanner.nextLine(); // Pour capturer le saut de ligne précédent
            String adresse = scanner.nextLine();

            String insertUserSql = "INSERT INTO users (email, nom, prenom, adresse, password) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement insertStmt = conn.prepareStatement(insertUserSql)){
                insertStmt.setString(1, email);
                insertStmt.setString(2, nom);
                insertStmt.setString(3, prenom);
                insertStmt.setString(4, adresse);
                System.out.println("Inscription réussie ! Vous pouvez maintenant vous connecter.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Une erreur est survenue lors de l'inscription.");
        }
        AuctionApp.main();
    }

    public static void login(Connection conn, Scanner scanner){
        System.out.println("\n=== Connexion ===");

        System.out.print("Entrez votre email : ");
        String email = scanner.next();

        // Requête pour vérifier l'utilisateur
        String loginSql = "SELECT * FROM users WHERE email = ?";

        try (PreparedStatement loginStmt = conn.prepareStatement(loginSql)) {
            loginStmt.setString(1, email);

            ResultSet rs = loginStmt.executeQuery();

            if (rs.next()) {
                // Connexion réussie
                System.out.println("Connexion réussie. Bienvenue, " + rs.getString("nom") + " " + rs.getString("prenom") + " !");
                AuctionManager.auctionpage(conn,scanner)
            } else {
                // Échec de connexion
                System.out.println("Email incorrect. Veuillez réessayer.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Une erreur est survenue lors de la connexion.");
        }
        AuctionApp.main();
    }
}

