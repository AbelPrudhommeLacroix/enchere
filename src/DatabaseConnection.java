import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

public class DatabaseConnection {

    public static Connection getConnection() {
        // Oracle JDBC connection URL
        String url = "jdbc:oracle:thin:@oracle1.ensimag.fr:1521:oracle1"; 
        String username = "zimmermh"; 
        String password = "zimmermh"; 

        try {
            // Load the Oracle JDBC driver
            System.out.println("Chargement des drivers oracle...");
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());

            // Establish the connection
            System.out.println("Connection à la BDD...");
            Connection connection = DriverManager.getConnection(url, username, password);
            return connection;
        } catch (SQLException e) {
            System.err.println("[!] Erreur de connexion à la BDD: " + e.getMessage());
            return null;
        }
    }

   public static void loadSQL(Connection conn) {
        try {
            System.out.println("Chargement des tables...");
            DBQueries.executeSQLFile(conn, "sql/tables.sql");
            System.out.println("Chargement des données...");
            DBQueries.executeSQLFile(conn, "sql/init.sql");
        } catch (SQLException e) {
            System.err.println("[!] Erreur lors de l'exécution de SQL: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("[!] Erreur lors de la lecture du fichier SQL: " + e.getMessage());
        }
   }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("Connexion fermé");
            } catch (SQLException e) {
                System.err.println("[!] Erreur en fermant la connexion: " + e.getMessage());
            }
        }
    }
}
