/* point d'entrée principal de ton application Java
initialise l'interface utilisateur et configure les 
scènes pour naviguer entre les différentes fenêtres
*/

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Auction_Abel {
    /*
    Ajout d'un produit à la BDD avec toutes ses caractéristiques
    */
    public static void ajoutProduit(Connection conn, Scanner scanner) {
    try {
        System.out.println("\n=== Ajout de votre produit ===\n");
        System.out.print("Nom : ");
        String nom = scanner.next();

        System.out.print("Prix de revient : ");
        float prixRevient; // decimal en sql
        while (true) {
            try {
                prixRevient = scanner.nextFloat();
                break;
            } catch (Exception e) {
                System.out.println("Veuillez entrer un prix valide (nombre).");
                scanner.next(); // Nettoie l'entrée incorrecte
            }
        }

        System.out.print("Stock : ");
        int stock;  // int en sql
        while (true) {
            try {
                stock = scanner.nextInt();
                break;
            } catch (Exception e) {
                System.out.println("Veuillez entrer un stock valide (nombre entier).");
                scanner.next(); // Nettoie l'entrée incorrecte
            }
        }

        System.out.print("Catégorie : ");
        String categorie = scanner.next();

        int idProduit = 0; //à générer

        String insertUserSql = "INSERT INTO Produit (IdProduit, nom, prixRevient, stock, categorie) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement insertStmt = conn.prepareStatement(insertUserSql)) {
            insertStmt.setInt(1, idProduit);
            insertStmt.setString(2, nom);
            insertStmt.setFloat(3, prixRevient);
            insertStmt.setInt(4, stock);
            insertStmt.setString(5, categorie);
            insertStmt.executeUpdate();
            System.out.println("Produit ajouté avec succès !");
        }

        System.out.print("Avez-vous des caractéristiques à préciser : ");
        boolean val = true;

        while (val) {
            System.out.println("1. OUI");
            System.out.println("2. NON");
            System.out.print("Choix : ");
            int choice;
            try {
                choice = scanner.nextInt();
            } catch (Exception e) {
                System.out.println("Veuillez entrer un choix valide (1 ou 2).");
                scanner.next(); // Nettoie l'entrée incorrecte
                continue;
            }

            switch (choice) {
                case 1 -> Auction.ajoutCaracteristique(conn, scanner, idProduit);
                case 2 -> val = false;
                default -> System.out.println("Choix invalide. Veuillez entrer 1 ou 2.");
            }
        }

        // Retour au menu enchère
        AuctionApp.menuEnchere(conn, scanner);

    } catch (SQLException e) {
        e.printStackTrace();
        System.out.println("Une erreur est survenue lors de l'ajout du produit.");
    } catch (Exception e) {
        e.printStackTrace();
        System.out.println("Erreur inattendue.");
    }
}

    public static void creationSalle(Connection conn, Scanner scanner, String categorie) {
        try {
            System.out.println("\n --- Création automatique de la salle de vente ---");

            int idSalle = 0; // à générer

            String insertUserSql = "INSERT INTO SalleDeVente (IdSalle, NomCategorie) VALUES (?, ?)";
            try (PreparedStatement insertStmt = conn.prepareStatement(insertUserSql)){
                insertStmt.setInt(1, idSalle);
                insertStmt.setString(2, categorie);
                System.out.println("Salle de vente" + idSalle + "crée depuis la catégorie " + categorie);
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Une erreur est survenue lors de la création de la salle de vente.");
        }
    }


    public static void creationVente(){}
    

    public static void creationVente(Connection conn, Scanner scanner, int IdVente, 
        float PrixDepart, String sens, boolean revoc, int NbOffres, int IdSalle, int IdProduit) {
        try{
            System.out.println("\n --- Création automatique de la vente ---");
            String inserctUserSql = "INSERT INTO Vente (IdVente, PrixDepart, Sens, Revocable, NbOffres, IdSalle, IdProduit) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement insertStmt = conn.prepareStatement(inserctUserSql)){
                insertStmt.setInt(1, IdVente);
                insertStmt.setFloat(2, PrixDepart);
                insertStmt.setString(3, sens);
                insertStmt.setBoolean(4, revoc);
                insertStmt.setInt(5, NbOffres);
                insertStmt.setInt(6, IdSalle);
                insertStmt.setInt(7, IdProduit);}

        }
        catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Une erreur est survenue lors de la création de la vente.");
        }
    }

    public static void ajoutCaracteristique(Connection conn, Scanner scanner, int IdProduit){
        try {
            System.out.println("\n=== Ajout caracteristique ===");
            System.out.print("Nom caractéristique : ");
            String nomCaract = scanner.next();

            System.out.print("Valeur : ");
            String valCaract = scanner.next();


            String insertUserSql0 = "INSERT INTO Caracteristique (NomCaracteristique) VALUES (?)";
            try (PreparedStatement insertStmt = conn.prepareStatement(insertUserSql0)){
                insertStmt.setString(1, nomCaract);
                System.out.println("Caracteristique ajoutée");
            }

            String insertUserSql1 = "INSERT INTO ValeurCaracteristique (IdProduit, NomCaracteristique, Valeur) VALUES (?, ?, ?)";
            try (PreparedStatement insertStmt = conn.prepareStatement(insertUserSql1)){
                insertStmt.setInt(1, IdProduit);
                insertStmt.setString(2, nomCaract);
                insertStmt.setString(3, valCaract);
                System.out.println("Valeur caracteristique ajoutée");
            }
        

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Une erreur est survenue lors de l'ajout de la caractériqtique du produit.");
        }
    }


    public static void encherir(Connection conn, Scanner scanner) {
        try {
            System.out.println("\n=== Détails de votre offre ===");
            System.out.print("Email : ");
            String email = scanner.next();

            System.out.print("Idvente : ");
            String Idvente = scanner.next();

            System.out.print("Date et heure de l'offre : ");
            String date = scanner.next();

            System.out.print("Prix d'achat : ");
            String prix = scanner.next();

            System.out.print("Nombre de produit voulu: ");
            String QuantiteOffre = scanner.next();

            if (verifOffre(email, Idvente, date, prix, QuantiteOffre)){
                String insertUserSql = "INSERT INTO Offre (Email, Idvente, date, prix, QuantiteOffr) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement insertStmt = conn.prepareStatement(insertUserSql)){
                    insertStmt.setString(1, email);
                    insertStmt.setString(2, Idvente);
                    insertStmt.setString(3, date);
                    insertStmt.setString(4, prix);
                    insertStmt.setString(5, QuantiteOffre);
                    System.out.println("Produit ajouté");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Une erreur est survenue lors de l'ajout du produit.");
        }
    }
    
    public static boolean verifOffre(String email, String Idvente, String date, String prix, String QuantiteOffre){
        return true;
    }

    public static void etatVente(Connection conn, Scanner scanner) {
        try {
            System.out.println("\n=== Quelle vente vous intéresse? ===");

            System.out.print("Idvente : ");
            int Idvente = scanner.nextInt();

            String query = "SELECT NbOffres, Revocabilite, Sens FROM Vente WHERE Vente.IdVente = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, Idvente);
    
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            int nbOffres = resultSet.getInt("NbOffres");
            boolean revocabilite = resultSet.getBoolean("Revocabilite");
            String sens = resultSet.getString("Sens");
                


        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Une erreur est survenue lors de l'ajout du produit.");
        }
    }
    


    public static void associerSalleVente(Connection conn, Scanner scanner){}

}