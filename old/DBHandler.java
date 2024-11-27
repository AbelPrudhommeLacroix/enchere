import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class DBHandler {

    public static void register(Connection conn, Scanner scanner) {
        try {
            System.out.println("\n=== Inscription ===");
            System.out.print("Email : ");
            String email = scanner.next();
    
            System.out.print("Nom : ");
            String nom = scanner.next();
    
            System.out.print("Prénom : ");
            String prenom = scanner.next();
    
            System.out.print("Adresse : ");
            scanner.nextLine(); // Capture newline
            String adresse = scanner.nextLine();
    
            // Update table name to "Utilisateur"
            String insertUserSql = "INSERT INTO Utilisateur (EmailUtilisateur, Nom, Prenom, AdressePostale) VALUES (?, ?, ?, ?)";
            try (PreparedStatement insertStmt = conn.prepareStatement(insertUserSql)){
                insertStmt.setString(1, email);
                insertStmt.setString(2, nom);
                insertStmt.setString(3, prenom);
                insertStmt.setString(4, adresse);
                insertStmt.executeUpdate();
                System.out.println("Inscription réussie ! Vous pouvez maintenant vous connecter.");
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Une erreur est survenue lors de l'inscription.");
        }
        HCInterface.main(new String[]{});
    }    

    public static void login(Connection conn, Scanner scanner){
        System.out.println("\n=== Connexion ===");
    
        System.out.print("Entrez votre email : ");
        String email = scanner.next();
    
        // Update table name to "Utilisateur"
        String loginSql = "SELECT * FROM Utilisateur WHERE EmailUtilisateur = ?";
    
        try (PreparedStatement loginStmt = conn.prepareStatement(loginSql)) {
            loginStmt.setString(1, email);
    
            ResultSet rs = loginStmt.executeQuery();
    
            if (rs.next()) {
                // Connexion réussie
                System.out.println("Connexion réussie. Bienvenue, " + rs.getString("Nom") + " " + rs.getString("Prenom") + " !");
                HCInterface.menuEnchere(conn, scanner);
            } else {
                // Échec de connexion
                System.out.println("Email incorrect. Veuillez réessayer.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Une erreur est survenue lors de la connexion.");
        }
        HCInterface.main(new String[]{});
    }    
}
