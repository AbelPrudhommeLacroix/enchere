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

    // Fonction qui renvoi l'id de chaque salle de vente et leur categorie
    public static String getSallesDeVente(Connection conn) throws SQLException {
        String selectSalleDeVenteSql = "SELECT IdSalle, NomCategorie FROM SalleDeVente";
        PreparedStatement stmt = conn.prepareStatement(selectSalleDeVenteSql);
        ResultSet rs = stmt.executeQuery();
        String salles = "";
        while(rs.next()) {
            int idSalle = rs.getInt("IdSalle");
            String nomCategorie = rs.getString("NomCategorie");
            salles += "- IdSalle : "+idSalle+" | NomCategorie : "+nomCategorie+"\n";
        }
        rs.close();
        return salles;
    }

    // Fonction qui affiche toutes les ventes disponibles pour une salle de vente : (idVente, NomProduit, Stock, PrixDepart, Sens, Revocabilite, NbOffres)
    public static String getVentes(Connection conn, int idSalle) throws SQLException {
        String selectVentesSql = "SELECT IdVente, NomProduit, Stock, PrixDepart, Sens, Revocabilite, NbOffres FROM Vente, Produit WHERE Vente.IdProduit = Produit.IdProduit AND IdSalle = ?";
        PreparedStatement stmt = conn.prepareStatement(selectVentesSql);
        stmt.setInt(1, idSalle);
        ResultSet rs = stmt.executeQuery();
        String ventes = "";
        while(rs.next()) {
            int idVente = rs.getInt("IdVente");
            String nomProduit = rs.getString("NomProduit");
            int stock = rs.getInt("Stock");
            float prixDepart = rs.getFloat("PrixDepart");
            String sens = rs.getString("Sens");
            boolean revocabilite = rs.getBoolean("Revocabilite");
            int nbOffres = rs.getInt("NbOffres");
            float meilleureOffre = getMeilleureOffre(conn, idVente);
            ventes += "- IdVente : "+idVente+" | NomProduit : "+nomProduit+" | Stock : "+stock+" | PrixDepart : "+prixDepart+" | Meilleure Offre : "+meilleureOffre+" | Sens : "+sens+" | Revocabilite : "+revocabilite+" | NbOffres : "+nbOffres+"\n";
        }
        rs.close();
        return ventes;
        
    }

    // Fonction qui dit si la salle de vente existe
    public static boolean doesSalleExist(Connection conn, int idSalle) throws SQLException {
        String checkSalleSql = "SELECT 1 FROM SalleDeVente WHERE IdSalle = ?";
        PreparedStatement stmt = conn.prepareStatement(checkSalleSql);
        stmt.setInt(1, idSalle);
        ResultSet rs = stmt.executeQuery();
        boolean salleExists = rs.next();
        rs.close();
        return salleExists;
    }

    // Fonction qui dit si une offre est valide ( >= PrixDepart, > Meilleure offre, Quantité <= Stock)
    public static boolean isOffreValide(Connection conn, int idVente, float prix, int quantite) throws SQLException {
        String selectProduitSql = "SELECT PrixDepart, Stock FROM Vente, Produit WHERE Vente.IdProduit = Produit.IdProduit AND IdVente = ?";
        PreparedStatement stmt = conn.prepareStatement(selectProduitSql);
        stmt.setInt(1, idVente);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        float prixDepart = rs.getFloat("PrixDepart");
        int stock = rs.getInt("Stock");
        rs.close();
        if (prix < prixDepart) {
            throw new IllegalArgumentException("Le prix proposé est inférieur au prix de départ.");
        }
        
        float meilleureOffre = getMeilleureOffre(conn, idVente);
        if (prix <= meilleureOffre) {
            throw new IllegalArgumentException("Le prix proposé est inférieur ou égal à la meilleure offre actuelle.");
        }
        
        if (quantite > stock) {
            throw new IllegalArgumentException("La quantité proposée est supérieure au stock disponible.");
        }
        
        return true;
    }

    // Fonction qui renvoi la meilleure offre
    public static float getMeilleureOffre(Connection conn, int idVente) throws SQLException {
        String selectMeilleureOffreSql = "SELECT MAX(PrixAchat) FROM Offre WHERE IdVente = ?";
        PreparedStatement stmt = conn.prepareStatement(selectMeilleureOffreSql);
        stmt.setInt(1, idVente);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        float meilleureOffre = rs.getFloat("MAX(PrixAchat)");
        rs.close();
        return meilleureOffre;
    }

    // Fonction qui dit si la vente existe
    public static boolean doesVenteExist(Connection conn, int idVente) throws SQLException {
        String checkVenteSql = "SELECT 1 FROM Vente WHERE IdVente = ?";
        PreparedStatement stmt = conn.prepareStatement(checkVenteSql);
        stmt.setInt(1, idVente);
        ResultSet rs = stmt.executeQuery();
        boolean venteExists = rs.next();
        rs.close();
        return venteExists;
    }

    //Fonction pour la création d'une offre. Dont la primary key est DateHeureOffre, Email, IdVente
    public static void creationOffre(Connection conn, float prix, int quantite, int idVente, String email) throws SQLException {
        System.out.println("Création de l'offre pour " + email + " sur la vente " + idVente + " pour un prix de " + prix + " et une quantité de " + quantite);
        // Vérifiez si l'IdVente existe dans la table parente
        String checkVenteSql = "SELECT COUNT(*) FROM Vente WHERE IdVente = ?";
        PreparedStatement checkStmt = conn.prepareStatement(checkVenteSql);
        checkStmt.setInt(1, idVente);
        ResultSet rs = checkStmt.executeQuery();
        if (rs.next() && rs.getInt(1) > 0) {
            // L'IdVente existe, procédez à l'insertion de l'offre
            Date date = new Date(System.currentTimeMillis());
    
            try {
                String insertDateSql = "INSERT INTO DateOffre (DateHeureOffre) VALUES (?)";
                PreparedStatement insertDateStmt = conn.prepareStatement(insertDateSql);
                insertDateStmt.setDate(1, date);
                insertDateStmt.executeUpdate();
            } catch (SQLException e) {
                // On ignore l'erreur si la date existe déjà
            }
    
            String insertOffreSql = "INSERT INTO Offre (PrixAchat, QuantiteOffre, IdVente, EmailUtilisateur, DATEHEUREOFFRE) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement insertStmt = conn.prepareStatement(insertOffreSql);
            insertStmt.setFloat(1, prix);
            insertStmt.setInt(2, quantite);
            insertStmt.setInt(3, idVente);
            insertStmt.setString(4, email);
            insertStmt.setDate(5, date);
            insertStmt.executeUpdate();
        } else {
            // L'IdVente n'existe pas, lancez une exception ou gérez l'erreur
            throw new SQLException("La vente n'existe plus.");
        }
    }

    public static void isEmailValide(Connection conn, String email) throws SQLException {
        String checkEmailSql = "SELECT 1 FROM Utilisateur WHERE EmailUtilisateur = ?";
        PreparedStatement stmt = conn.prepareStatement(checkEmailSql);
        stmt.setString(1, email);
        ResultSet rs = stmt.executeQuery();
        if (!rs.next()) {
            throw new IllegalArgumentException("L'email n'est pas valide.");
        }
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



   