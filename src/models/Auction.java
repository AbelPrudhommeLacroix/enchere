/* point d'entrée principal de ton application Java
initialise l'interface utilisateur et configure les 
scènes pour naviguer entre les différentes fenêtres
*/

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Auction {
    /*
    Ajout d'un produit à la BDD avec toutes ses caractéristiques
    */
    public static void ajoutProduit(Connection conn, Scanner scanner) {
        try {
            System.out.println("\n=== Ajout de votre produit ===");
            System.out.print("Nom : ");
            String nom = scanner.next();

            System.out.print("Prix de revient : ");
            String prixRevient = scanner.next();

            System.out.print("Stock : ");
            String stock = scanner.next();

            System.out.print("Catégorie : ");
            String categorie = scanner.next();

            String idProduit = "0";       // générer un idProduit

            String insertUserSql = "INSERT INTO Produit (IdProduit, nom, prixRevient, stock, categorie) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement insertStmt = conn.prepareStatement(insertUserSql)){
                insertStmt.setString(1, idProduit);
                insertStmt.setString(2, nom);
                insertStmt.setString(3, prixRevient);
                insertStmt.setString(4, stock);
                insertStmt.setString(5, categorie);
                System.out.println("Produit ajouté");
            }


        // ajout caractéristiques
        System.out.print("Avez vous des caractéristiques à préciser : ");
        System.out.print("1. OUI ");
        System.out.print("2. NON ");
        System.out.print("Choix : ");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1 -> Auction.ajoutCaracteristique(conn,  scanner, idProduit);
            case 2 -> Auction.vente();       // retour à la page de vente
            }
        

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Une erreur est survenue lors de l'ajout du produit.");
        }
    }



    public static void creationSalle(String categorie) {}

    public static void creationVente(){}

    public static void ajoutCaracteristique(Connection conn, Scanner scanner,String idProduit){
        try {
            System.out.println("\n=== Ajout caracteristique ===");
            System.out.print("Nom caractéristique : ");
            String nomCaract = scanner.next();

            System.out.print("Valeur : ");
            String valCaract = scanner.next();


            String insertUserSql = "INSERT INTO Produit (IdProduit, nom, prixRevient, stock, categorie) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement insertStmt = conn.prepareStatement(insertUserSql)){
                insertStmt.setString(1, idProduit);
                insertStmt.setString(2, nomCaract);
                System.out.println("Produit ajouté");
            }


        // ajout caractéristiques
        System.out.print("Avez vous des caractéristiques à préciser : ");
        System.out.print("1. OUI ");
        System.out.print("2. NON ");
        System.out.print("Choix : ");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1 -> Auction.ajoutCaracteristiques(idProduit);
            case 2 -> Auction.vente()       // retour à la page de vente
            }
        

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Une erreur est survenue lors de l'ajout du produit.");
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
            PreparedStatement statement = conn.prepareStatement(query)
            statement.setInt(1, Idvente);
    
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            int nbOffres = resultSet.getInt("NbOffres");
            boolean revocabilite = resultSet.getBoolean("Revocabilite");
            String sens = resultSet.getString("Sens");
            }
            if (sens == "Montantes", revocabilite == "non révocable",  )
                


        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Une erreur est survenue lors de l'ajout du produit.");
        }
    }
    

}
