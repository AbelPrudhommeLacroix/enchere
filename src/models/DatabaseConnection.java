import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    public static Connection getConnection() {
        // Oracle JDBC connection URL
        String url = "jdbc:oracle:thin:@oracle1.ensimag.fr:1521:oracle1"; // Replace with your Oracle database URL
        String username = "zimmermh"; // Replace with your Oracle username
        String password = "zimmermh"; // Replace with your Oracle password

        try {
            // Load the Oracle JDBC driver
            System.out.print("Loading Oracle driver... ");
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            System.out.println("loaded");

            // Establish the connection
            System.out.print("Connecting to the database... ");
            Connection connection = DriverManager.getConnection(url, username, password);
            System.out.println("connected");
            return connection;
        } catch (SQLException e) {
            System.err.println("Error connecting to the database: " + e.getMessage());
            return null;
        }
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("Connection closed.");
            } catch (SQLException e) {
                System.err.println("Error closing the connection: " + e.getMessage());
            }
        }
    }
}
