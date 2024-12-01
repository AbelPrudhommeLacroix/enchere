import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;


public class HCInterface {

    private static boolean exit = false;


    //Convertit un string en Timestamp
    public static Timestamp convertToTimestamp(String dateStr) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setLenient(false);
        return new Timestamp(dateFormat.parse(dateStr).getTime());
    }

    public static void setExit(boolean exit) {
        HCInterface.exit = exit;
    }

    //Main de l'IHM
    public static void main(String[] args) {
        // Connexion à la base de données
        Connection conn = DatabaseConnection.getConnection();

        if (conn != null) {

            //Scanner
            Scanner scanner = new Scanner(System.in);
             
             //Boucle du menu
             while (!HCInterface.exit) {
                MenuMain.launch(conn, scanner);
             }
            
             scanner.close();
        } else {
            System.err.println("[!] Connexion à la BDD impossible.");
        }
        DatabaseConnection.closeConnection(conn);
        
    }
}
