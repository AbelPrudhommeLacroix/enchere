import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Scanner;

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
            return 0;
        }
    }

    //Renvoi l'id du produit le plus grand (0 si aucun produit existe)
    public static int getMaxProduitId(Connection conn) throws SQLException {
        
        String getMaxProduitIdSql = "SELECT IdProduit FROM Produit ORDER BY IdProduit DESC FETCH FIRST 1 ROWS ONLY";
        PreparedStatement stmt = conn.prepareStatement(getMaxProduitIdSql);
    
        // Exécuter la requête
        ResultSet rs = stmt.executeQuery();
    
        if (rs.next()) {
            int maxProduitId = rs.getInt("IdProduit");
            rs.close();
            return maxProduitId; 
        } else {
            rs.close();
            return 0;
        }
    }

    public static int getMaxVenteId(Connection conn) throws SQLException {

        String getMaxVenteIdSql = "SELECT IdVente FROM Vente ORDER BY IdVente DESC FETCH FIRST 1 ROWS ONLY";
        PreparedStatement stmt = conn.prepareStatement(getMaxVenteIdSql);

        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            // Récupérer le plus grand IdVente
            int maxVenteId = rs.getInt("IdVente");
            rs.close();
            return maxVenteId;
        } else {
            rs.close();
            return 0;
        }
    }


    //Creer une salle avec la categorie categorie
    public static int creationSalle(Connection conn, Scanner scanner, String categorie) throws SQLException {

        int idSalle = getMaxSalleId(conn) + 1; // Id unique

        String insertUserSql = "INSERT INTO SalleDeVente (IdSalle, NomCategorie) VALUES (?, ?)";
        PreparedStatement insertStmt = conn.prepareStatement(insertUserSql);
        insertStmt.setInt(1, idSalle);
        insertStmt.setString(2, categorie);

        insertStmt.executeQuery();

        return idSalle;
    }


    public static int creationProduit(Connection conn, String nomProduit, float prixRevient, int stock, String nomCategorie) throws SQLException {

        String insertProduitSql = "INSERT INTO Produit (IdProduit, NomProduit, PrixRevient, Stock, NomCategorie) VALUES (?, ?, ?, ?, ?)";

        int idProduit = getMaxProduitId(conn) + 1;

        PreparedStatement insertStmt = conn.prepareStatement(insertProduitSql);
        insertStmt.setInt(1, idProduit);
        insertStmt.setString(2, nomProduit);
        insertStmt.setFloat(3, prixRevient);
        insertStmt.setInt(4, stock);
        insertStmt.setString(5, nomCategorie);

        insertStmt.executeQuery();

        return idProduit;
        
    }


    public static int creationVente(Connection conn, float prixDepart, String sens, int revocabilite, int nbOffres, int idSalle, int idProduit) throws SQLException {

        String insertVenteSql = "INSERT INTO Vente (IdVente, PrixDepart, Sens, Revocabilite, NbOffres, IdSalle, IdProduit) VALUES (?, ?, ?, ?, ?, ?, ?)";

        int idVente = getMaxVenteId(conn) + 1;

        PreparedStatement insertStmt = conn.prepareStatement(insertVenteSql);
        insertStmt.setInt(1, idVente);
        insertStmt.setFloat(2, prixDepart);
        insertStmt.setString(3, sens);
        insertStmt.setInt(4, revocabilite);
        insertStmt.setInt(5, nbOffres);
        insertStmt.setInt(6, idSalle);
        insertStmt.setInt(7, idProduit);

        insertStmt.executeUpdate();

        return idVente;
    }

    
    public static void creationVenteLimite(Connection conn, int idVente, Timestamp dateDebut, Timestamp dateFin) throws SQLException {

        String insertVenteLimiteSql = "INSERT INTO VenteLimite (IdVente, DateDebut, DateFin) VALUES (?, ?, ?)";

        PreparedStatement insertStmt = conn.prepareStatement(insertVenteLimiteSql);

        insertStmt.setInt(1, idVente);
        insertStmt.setTimestamp(2, dateDebut);
        insertStmt.setTimestamp(3, dateFin);

        insertStmt.executeUpdate();
        
    }


    public static void creationVenteLibre(Connection conn, int idVente) throws SQLException {

 

        String insertVenteLibreSql = "INSERT INTO VenteLibre (IdVente) VALUES (?)";
        PreparedStatement insertStmt = conn.prepareStatement(insertVenteLibreSql);
        insertStmt.setInt(1, idVente);
        insertStmt.executeUpdate();
     
    }
    


}



   