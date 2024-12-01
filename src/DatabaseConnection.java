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
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            
            return connection;
        } catch (SQLException e) {
            System.err.println("[!] Erreur de connexion à la BDD: " + e.getMessage());
            return null;
        }
    }

    public static void loadSQL(Connection conn) {
        try {
            // Désactive l'auto-commit pour gérer les transactions manuellement
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
    
            System.out.println("Chargement des tables...");
            DBQueries.executeSQLFile(conn, "../sql/tables.sql");
            
            // Commit des tables après chargement
            conn.commit();  // Persiste les modifications (création des tables)
            
            System.out.println("Chargement des données...");
            DBQueries.executeSQLFile(conn, "../sql/init.sql");
            
            // Commit des données après chargement
            conn.commit();  // Persiste les données (insertion des données)
            
            // Optionnel : Si vous souhaitez changer le niveau d'isolation pour les opérations suivantes
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
    
        } catch (SQLException e) {
            try {
                conn.rollback(); // Annule les changements en cas d'erreur
                System.err.println("[!] Erreur lors de l'exécution de SQL, rollback effectué : " + e.getMessage());
            } catch (SQLException rollbackEx) {
                System.err.println("[!] Erreur lors du rollback : " + rollbackEx.getMessage());
            }
        } catch (IOException e) {
            System.err.println("[!] Erreur lors de la lecture du fichier SQL: " + e.getMessage());
        } finally {
            try {
                // Optionnel : Réactive l'auto-commit si nécessaire avant de fermer la connexion
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("[!] Erreur lors de la réactivation de l'auto-commit : " + e.getMessage());
            }
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
