import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Auction {

    // Method to list a product for auction
    public static void ajoutProduit(Connection conn, Scanner scanner) {
        try {
            System.out.println("\n=== Add your product ===");
            System.out.print("Name: ");
            String name = scanner.next();

            System.out.print("Price: ");
            float price = scanner.nextFloat();

            System.out.print("Stock: ");
            int stock = scanner.nextInt();

            System.out.print("Category: ");
            scanner.nextLine(); // consume newline
            String category = scanner.nextLine();

            // Simulate ID generation (in real application use auto-increment)
            int productId = (int) (Math.random() * 10000);

            String insertProductSql = "INSERT INTO Products (name, price, stock, category) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(insertProductSql)) {
                stmt.setString(1, name);
                stmt.setFloat(2, price);
                stmt.setInt(3, stock);
                stmt.setString(4, category);
                stmt.executeUpdate();
                System.out.println("Product added!");
            }

        } catch (SQLException e) {
            System.err.println("Error adding product: " + e.getMessage());
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



   