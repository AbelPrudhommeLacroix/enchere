import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    public static Connection getConnection() {
        String url = "jdbc:mysql://localhost:3306/votre_base_de_donnees";
        String username = "votre_nom_utilisateur";
        String password = "votre_mot_de_passe";

        try {
            // Charger le driver JDBC (pas nécessaire avec les versions modernes de JDBC)
            // Class.forName("com.mysql.cj.jdbc.Driver");

            // Établir la connexion
            Connection connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connexion réussie à la base de données !");
            return connection;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la connexion à la base de données : " + e.getMessage());
            return null;
        }
    }
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("Connexion à la base de données fermée.");
            } catch (SQLException e) {
                System.err.println("Erreur lors de la fermeture de la connexion : " + e.getMessage());
            }
        }
    }
}
