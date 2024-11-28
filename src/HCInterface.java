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
                    //TODO
                    case 2 -> menuCreationOffre(conn, scanner);
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

    public static void menuCreationOffre(Connection conn, Scanner scanner) {
        System.out.println("\n=== Création d'une offre ===");
    
        // Affichage de l'id salles de vente et de leur catégorie associée
        try {
            System.out.println("Liste des salles de vente disponibles : ");
            String salles = DBQueries.getSallesDeVente(conn);
            System.out.println(salles);
        } catch (SQLException e) {
            System.err.println("[!] Erreur dans l'affichage des salles : " + e);
            return;
        }
    
        // L'utilisateur choisit une salle de vente
        int id_salle = -1;
        while (id_salle == -1) {
            System.out.print("Choisissez une salle de vente : ");
            if (scanner.hasNextInt()) {
                id_salle = scanner.nextInt();
                try {
                    if (!DBQueries.doesSalleExist(conn, id_salle)) {
                        System.err.println("[!] La salle n'existe pas / n'a pas été trouvée");
                        id_salle = -1; // Réinitialiser pour redemander
                    }
                } catch (Exception e) {
                    System.err.println("[!] Erreur lors de la vérification de la salle : " + e);
                    id_salle = -1; // Réinitialiser pour redemander
                }
            } else {
                System.err.println("[!] Entrée invalide. Veuillez entrer un nombre entier.");
                scanner.next(); // Consommer l'entrée invalide
            }
        }
    
        // Affichage des ventes disponibles
        try {
            System.out.println("Liste des ventes disponibles : ");
            String ventes = DBQueries.getVentes(conn, id_salle);
            System.out.println(ventes);
        } catch (SQLException e) {
            System.err.println("[!] Erreur dans l'affichage des ventes : " + e);
            return;
        }
    
        // L'utilisateur choisit une vente donc un produit
        int id_vente = -1;
        while (id_vente == -1) {
            System.out.print("Choisissez une vente : ");
            if (scanner.hasNextInt()) {
                id_vente = scanner.nextInt();
                try {
                    if (!DBQueries.doesVenteExist(conn, id_vente)) {
                        System.err.println("[!] La vente n'existe pas / n'a pas été trouvée");
                        id_vente = -1; // Réinitialiser pour redemander
                    }
                } catch (Exception e) {
                    System.err.println("[!] Erreur lors de la vérification de la vente : " + e);
                    id_vente = -1; // Réinitialiser pour redemander
                }
            } else {
                System.err.println("[!] Entrée invalide. Veuillez entrer un nombre entier.");
                scanner.next(); // Consommer l'entrée invalide
            }
        }
    
        // L'utilisateur fait une offre sur un produit pour un prix et une quantité qu'il donne
        float prix = -1;
        while (prix == -1) {
            System.out.print("Prix de l'offre : ");
            if (scanner.hasNextFloat()) {
                prix = scanner.nextFloat();
                try {
                    DBQueries.isOffreValide(conn, id_vente, prix, -1); // Valider le prix
                } catch (IllegalArgumentException e) {
                    System.err.println("[!] L'offre n'est pas valide : " + e);
                    prix = -1; // Réinitialiser pour redemander
                } catch (Exception e) {
                    System.err.println("[!] L'offre n'est pas valide : " + e);
                    prix = -1; // Réinitialiser pour redemander
                }
            } else {
                System.err.println("[!] Entrée invalide. Veuillez entrer un nombre décimal.");
                scanner.next(); // Consommer l'entrée invalide
            }
        }
    
        int quantite = -1;
        while (quantite == -1) {
            System.out.print("Quantité de l'offre : ");
            if (scanner.hasNextInt()) {
                quantite = scanner.nextInt();
                if (quantite <= 0) {
                    System.err.println("[!] La quantité doit être strictement positive.");
                    quantite = -1; // Réinitialiser pour redemander
                }
                try {
                    DBQueries.isOffreValide(conn, id_vente, prix, quantite); // Valider la quantité
                } catch (IllegalArgumentException e) {
                    System.err.println("[!] L'offre n'est pas valide : " + e);
                    quantite = -1; // Réinitialiser pour redemander
                } catch (Exception e) {
                    System.err.println("[!] L'offre n'est pas valide : " + e);
                    quantite = -1; // Réinitialiser pour redemander
                }
            } else {
                System.err.println("[!] Entrée invalide. Veuillez entrer un nombre entier.");
                scanner.next(); // Consommer l'entrée invalide
            }
        }
    
        String email = "";
        while (email.isEmpty()) {
            System.out.print("Email de l'acheteur : ");
            email = scanner.next();
            try {
                DBQueries.isEmailValide(conn, email); // Valider l'email
            }
            catch (IllegalArgumentException e) {
                System.err.println("[!] L'email n'est pas valide : " + e);
                email = ""; // Réinitialiser pour redemander
            }
            catch (Exception e) {
                System.err.println("[!] L'email n'est pas valide : " + e);
                email = ""; // Réinitialiser pour redemander
            }
        }
    
        // Création de l'offre associée à la date, l'email et l'id de la vente
        try {
            DBQueries.creationOffre(conn, prix, quantite, id_vente, email);
        } catch (Exception e) {
            System.err.println("Erreur lors de la création de l'offre : " + e.getMessage());
        }
    }    
    
    public static void main(String[] args) {
        menuPrincipal();
    }
}
