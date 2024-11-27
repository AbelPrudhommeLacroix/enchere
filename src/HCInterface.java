import java.util.InputMismatchException;
import java.util.Scanner;
import java.io.IOException;
import java.sql.*;

public class HCInterface {




    public static void menuPrincipal() {

        // Connexion à la base de données
        Connection conn = DatabaseConnection.getConnection();
        if (conn != null) {
            Scanner scanner = new Scanner(System.in);
            boolean exit = false;

            //Chargement des tables
            DatabaseConnection.loadSQL(conn);
            

            while (!exit) {
                System.out.println("\n=== Menu Principal ===");
                System.out.println("1. Creer une salle de Vente");
                System.out.println("2. Creer une offre");
                System.out.println("3. Afficher le résultat d'une enchère");
                System.out.println("4. Afficher une table");
                System.out.println("5. Quitter");

                System.out.print("Votre choix : ");

                int choice;
                try {
                    choice = scanner.nextInt();
                } catch (Exception e) {
                    scanner.nextLine();
                    System.out.println("[!] Choix invalide");
                    continue;
                }

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

        //Affichage des catégories
        try {
            System.out.println("Liste des catégories disponibles : "); 
            String categories = DBQueries.searchCategories(conn, scanner);
            System.out.println(categories);
        } catch (SQLException e) {
            System.err.println("[!] Erreur dans l'affichage des categories : " + e); 
            return;
        }

        //Choix de la catégorie
        System.out.print("La catégorie de votre salle : ");
        String categorie = scanner.next(); 
        try {
            if (!DBQueries.doesCategoryExist(conn, categorie)) throw new Exception();
        } catch (Exception e){
            System.err.println("[!] La categorie n'existe pas / n'a pas était trouvé"); 
            return;
        }

        //Sens des ventes
        System.out.print("Sens des ventes (croissant/decroissant) : ");
        String sens = scanner.next();
        if (!sens.equals("croissant") && !sens.equals("decroissant")) {
            System.err.println("[!] Mauvais choix, choix disponible : (croissant/decroissant)");
            return;
        }

        //Revocabilite
        int revocabilite;
        System.out.print("Revocabilite (oui/non) : ");
        String revocabilite_choix = scanner.next();
        switch (revocabilite_choix) {
            case "oui" -> revocabilite = 1;
            case "non" -> revocabilite = 0;
            default -> {
                System.err.println("[!] Mauvais choix, choix disponible : (oui/non)");
                return;
            }
        }

        //Nombre d'offre max
        System.out.print("Nombre d'offres maximum : ");
        try {
            int nb_offre = scanner.nextInt();
            if (nb_offre <= 0) throw new Exception();
        } catch (Exception e) {
            System.err.println("[!] Veuillez renseigner un entier strictement positif.");
            scanner.nextLine();
            return;
        }

        //Duree de la vente
        System.out.print("Duree de la vente (libre/limite) : ");
        String duree_vente = scanner.next();
        if (!duree_vente.equals("libre") && !duree_vente.equals("limite")) {
            System.err.println("[!] Mauvais choix, choix disponible : (croissant/decroissant)");
            return;
        }

        //Creation de la salle
        try {
            DBQueries.creationSalle(conn, scanner, categorie);
        } catch (Exception e) {
            System.err.println("Erreur lors de la creation de la salle : " + e.getMessage());
        }

        //Creation des produits de la salle TODO
        //TODO : produits ici avec une boucle while en gros dans un autre menu par exemple 1- ajouter un nv produit 2 - terminer
    }




    
    public static void main(String[] args) {
        menuPrincipal();
    }
}
