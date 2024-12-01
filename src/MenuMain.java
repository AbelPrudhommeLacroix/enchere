import java.sql.Connection;
import java.util.Scanner;

public class MenuMain {

    public static void launch(Connection conn, Scanner scanner) {
         
        System.out.println("\n=========== Menu Principal ===========\n");
        System.out.println("1 - Creer une salle de Vente");
        System.out.println("2 - Creer une offre");
        System.out.println("3 - Afficher le résultat d'une enchère");
        System.out.println("4 - Réinitialiser la BDD");
        System.out.println("5 - Quitter");

        System.out.print("\nVotre choix : ");

        int choice;
        try {
            choice = scanner.nextInt();
        } catch (Exception e) {
            scanner.nextLine();
            System.out.println("[!] Choix invalide");
            return;
        }

        switch (choice) {
            case 1 -> MenuRoom.launch(conn, scanner);
            case 2 -> MenuOffer.launch(conn, scanner);
            case 3 -> MenuResult.launch(conn, scanner);
            case 4 -> DatabaseConnection.loadSQL(conn);
            case 5 -> {
                System.out.println("Au revoir !");
                HCInterface.setExit(false);
            }
            default -> System.out.println("[!] Choix invalide");
        }
    }
}