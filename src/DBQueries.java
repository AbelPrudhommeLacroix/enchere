import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
//temps
import java.time.LocalDateTime;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import oracle.net.aso.s;

public class DBQueries {


    //Execute un fichier .sql ligne par ligne
    public static void executeSQLFile(Connection conn, String filePath) throws SQLException, IOException {
        Statement stmt = conn.createStatement();

        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        StringBuilder sqlBuilder = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith("--")) {
                continue;
            }
            sqlBuilder.append(line);

            if (line.endsWith(";")) { //On execute a chaque point virgule
                String sql = sqlBuilder.toString();
                sqlBuilder.setLength(0); 
                sql = sql.substring(0, sql.length() - 1);
                stmt.execute(sql);
            }
        }
        reader.close();
    }

    
    //Renvoi les catégories (ou throw une exception si la requette ne reussie pas)
    public static String searchCategories(Connection conn, Scanner scanner) throws SQLException {

        String selectCategoriesSql = "SELECT NomCategorie FROM Categorie";
        
        String categories = ""; 
    
        PreparedStatement stmt = conn.prepareStatement(selectCategoriesSql);
        ResultSet rs = stmt.executeQuery();

        while(rs.next()) {
            String nomCategorie = rs.getString("NomCategorie");  
            categories += "- "+nomCategorie+"\n";  
        }

        rs.close();
    
        return categories;
    }


    //Renvoi true si une Categorie avec le nomCategorie existe
    public static boolean doesCategoryExist(Connection conn, String nomCategorie) throws SQLException {

        String checkCategorySql = "SELECT 1 FROM Categorie WHERE NomCategorie = ?";
        PreparedStatement stmt = conn.prepareStatement(checkCategorySql);
        stmt.setString(1, nomCategorie);
        ResultSet rs = stmt.executeQuery();
    
        boolean categoryExists = rs.next();
        rs.close();

        return categoryExists;
    }


    //Renvoi l'id de la salle le plus grand (1 si aucune salle existe)
    public static int getMaxSalleId(Connection conn) throws SQLException {

        String getMaxSalleIdSql = "SELECT IdSalle FROM SalleDeVente ORDER BY IdSalle DESC FETCH FIRST 1 ROWS ONLY";
        PreparedStatement stmt = conn.prepareStatement(getMaxSalleIdSql);
    
        // Exécuter la requête
        ResultSet rs = stmt.executeQuery();
    
        if (rs.next()) {
            int maxSalleId = rs.getInt("IdSalle");
            rs.close();
            return maxSalleId; 
        } else {
            rs.close();
            return 1;
        }
    }


    //Creer une salle avec la categorie categorie
    public static void creationSalle(Connection conn, Scanner scanner, String categorie) throws SQLException {

        int idSalle = getMaxSalleId(conn) + 1; // Id unique

        String insertUserSql = "INSERT INTO SalleDeVente (IdSalle, NomCategorie) VALUES (?, ?)";
        PreparedStatement insertStmt = conn.prepareStatement(insertUserSql);
        insertStmt.setInt(1, idSalle);
        insertStmt.setString(2, categorie);
        insertStmt.executeQuery();
    }


    public static String getIds(Connection conn, Scanner scanner) throws SQLException {

        String selectIdsSql = "SELECT IdVente FROM Vente";
        PreparedStatement stmt = conn.prepareStatement(selectIdsSql);
        ResultSet rs = stmt.executeQuery();

        String ids = ""; 

        while(rs.next()) {
            int idV = rs.getInt("IdVente");  
            ids += idV + " | ";  
        }

        rs.close();
    
        return ids;
    }

    public static boolean doesIdExist(Connection conn, int id) throws SQLException {

        String checkIdSql = "SELECT 1 FROM SalleDeVente WHERE IdSalle = ?";
        PreparedStatement stmt = conn.prepareStatement(checkIdSql);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
    
        boolean idExists = rs.next();
        rs.close();

        return idExists;
    }


    /* vente montante illimitée non révocable */
    public static List<Offre> getGagnantMontantNonRevocableIllimite(Connection conn, int IdVente) throws SQLException{
        // String selectUserSql = "SELECT Offre.DateHeureOffre" + 
        //                 "FROM Offre\n" + 
        //                 "JOIN Vente ON Offre.IdVente = Vente.IdVente\n" + 
        //                 "JOIN DateOffre ON Offre.DateHeureOffre = DateOffre.DateHeureOffre\n" + 
        //                 "WHERE Vente.IdVente = ?\n" + 
        //                 "ORDER BY DateOffre.DateHeureOffre DESC\n" + 
        //                 "FETCH FIRST ROW ONLY;";
        // PreparedStatement stmt = conn.prepareStatement(selectUserSql);
        // stmt.setInt(1, IdVente);
        // ResultSet lastTimeRs = stmt.executeQuery();

        // Timestamp lastTime = null;
        // if (lastTimeRs.next()) {
        //     lastTime = lastTimeRs.getTimestamp("DateHeureOffre");
        // }

        // // temps actuel
        // LocalDateTime currentTime = LocalDateTime.now();

        // Duration duration = Duration.between(lastTime.toLocalDateTime(), currentTime);

        // Vérifier si la durée est supérieure à 10 minutes
        if (true) {
            System.out.println("La différence est supérieure à 10 minutes.");
            return Offre.sacADos(conn, IdVente);
        } else {
            System.out.println("La différence est inférieure ou égale à 10 minutes.");
            return null;
        }
    }








    //ANCIEN TRUC EN BAS


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

            //TODO : Produit ID = max(ProduitID) + 1 (avec requette SQL)
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



   