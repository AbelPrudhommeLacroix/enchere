import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

import java.util.Scanner;
import java.time.Instant;

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



    //                                  ========== CATEGORIES ==========
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



    //                                  ========== SALLE ==========
    //Renvoi l'id de la salle le plus grand (0 si aucune salle existe)
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

    //Renvoi le type de vente d'une salle ("" si la salle n'a pas de vente)
    public static String getTypeVenteSalle(Connection conn, int idSalle) throws SQLException {

        String selectTypeVenteSalleSql = 
            "SELECT IdVente, Sens, Revocabilite, NbOffres " +
            "FROM Vente " +
            "WHERE IdSalle = ?";
        
        PreparedStatement stmt = conn.prepareStatement(selectTypeVenteSalleSql);
        stmt.setInt(1, idSalle);
        ResultSet rs = stmt.executeQuery();
        
        // Si une vente est trouvée pour cette salle
        if (rs.next()) {

            String type_duree = (isVenteLimitee(conn, rs.getInt("IdVente")) == true ? "limitée" : "libre");
            String sens = rs.getString("Sens");
            String revocabilite = (rs.getInt("Revocabilite") == 1) ? "oui" : "non";
            int nbOffres = rs.getInt("NbOffres");
            
            rs.close();
            stmt.close();
            return "Durée : "+type_duree+", Sens : "+sens+", Revocabilité : "+revocabilite+", Nombre d'offres max : "+nbOffres;

        } else {
            rs.close();
            stmt.close();
            return "";
        }
    }


    //Renvoi l'id de chaque salle de vente, leur categorie et leur type de vente
    public static String getSallesDeVente(Connection conn) throws SQLException {
        String selectSalleDeVenteSql = "SELECT IdSalle, NomCategorie FROM SalleDeVente";
        PreparedStatement stmt = conn.prepareStatement(selectSalleDeVenteSql);
        ResultSet rs = stmt.executeQuery();
        StringBuilder salles = new StringBuilder();
        while (rs.next()) {

            int idSalle = rs.getInt("IdSalle");
            String type_de_vente = getTypeVenteSalle(conn, idSalle);
            String nomCategorie = rs.getString("NomCategorie");

            salles.append(idSalle + " - "+nomCategorie + " ("+type_de_vente+")\n");
        }
        rs.close();
        return salles.toString();
    }

    //Renvoi true si la salle de vente existe
    public static boolean doesSalleExist(Connection conn, int idSalle) throws SQLException {
        String checkSalleSql = "SELECT 1 FROM SalleDeVente WHERE IdSalle = ?";
        PreparedStatement stmt = conn.prepareStatement(checkSalleSql);
        stmt.setInt(1, idSalle);
        ResultSet rs = stmt.executeQuery();
        boolean salleExists = rs.next();
        rs.close();
        return salleExists;
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



    //                                  ========== PRODUIT ==========
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

    //Creer un produit
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



    //                                  ========== OFFRE ==========

    //Renvoi true si la quantité et le prix d'une offre est valide 
    public static boolean isPrixQteOffreValide(Connection conn, int idVente, float prix, int quantite) throws SQLException {

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
        
        float meilleureOffre = getValeurMeilleureOffre(conn, idVente);
        if (prix <= meilleureOffre) {
            throw new IllegalArgumentException("Le prix proposé est inférieur ou égal à la meilleure offre actuelle.");
        }
        
        if (quantite > stock) {
            throw new IllegalArgumentException("La quantité proposée est supérieure au stock disponible.");
        }
        
        return true;
    }

    //Renvoi true si la date d'une offre est valide
    public static boolean isDateOffreValide(Connection conn, int idVente, Timestamp date) throws SQLException {
        //Verifier si l'enchère est finie pour les ventes limitée
        if (isVenteLimitee(conn, idVente)) {
                
            Timestamp date_debut = getDateDebutVenteLimitee(conn, idVente);
            Timestamp date_fin = getDateFinVenteLimitee(conn, idVente);

            if (!(date.after(date_debut) && date.before(date_fin))) 
                throw new IllegalArgumentException("[!] L'enchère sur cette vente est terminée.");
        
            return true;

        //Verifier si l'enchère est finie pour les ventes libres
        } else {
            Timestamp date_derniere_offre = getDateMeilleureOffre(conn, idVente);

            if (date_derniere_offre != null) { //Si la dernière offre existe

                //On verifie que l'ecart est plus petit que 10 minutes
                if (date.getTime() - date_derniere_offre.getTime() > 10*60*1000) 
                    throw new IllegalArgumentException("L'enchère sur cette vente est terminée. (La dernière offfre date de plus de 10 minutes)");
            }
            return true;
        }
    }

    //Renvoi la valeur d'une meilleure offre (0 si elle n'existe pas)
    public static float getValeurMeilleureOffre(Connection conn, int idVente) throws SQLException {

        String selectMeilleureOffreSql = "SELECT MAX(PrixAchat) FROM Offre WHERE IdVente = ?";

        PreparedStatement stmt = conn.prepareStatement(selectMeilleureOffreSql);
        stmt.setInt(1, idVente);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            float meilleureOffre = rs.getFloat("MAX(PrixAchat)");
            rs.close();
            return meilleureOffre;
        }
        rs.close();
        return 0;
    }


    //Renvoie l'email de l'utilisateur et le montant de la meilleure offre sous forme de string.
    public static String getEmailEtValeurMeilleureOffre(Connection conn, int idVente) throws SQLException {

        // Requête SQL pour récupérer l'email et le prix maximum de l'offre pour une vente donnée
        String selectMeilleureOffreSql = 
                "SELECT EmailUtilisateur, PrixAchat " +
                "FROM Offre " +
                "WHERE IdVente = ? " +
                "AND PrixAchat = (SELECT MAX(PrixAchat) FROM Offre WHERE IdVente = ?)";

        PreparedStatement stmt = conn.prepareStatement(selectMeilleureOffreSql);
        stmt.setInt(1, idVente);
        stmt.setInt(2, idVente);

        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            String emailUtilisateur = rs.getString("EmailUtilisateur");
            float prixAchat = rs.getFloat("PrixAchat");
            rs.close();
            stmt.close();
            return emailUtilisateur + " : " + prixAchat+"€";
        }

        rs.close();
        stmt.close();
        // Retourne une chaîne vide si aucune offre n'existe
        return "";
    }

    //Renvoi la date d'une meilleure offre (null si pas de meilleure offre)
    public static Timestamp getDateMeilleureOffre(Connection conn, int idVente) throws SQLException {

        // Requête SQL pour obtenir la date de la meilleure offre
        String selectDateMeilleureOffreSql = 
            "SELECT DateHeureOffre " +
            "FROM Offre " +
            "WHERE IdVente = ? " +
            "AND PrixAchat = (SELECT MAX(PrixAchat) FROM Offre WHERE IdVente = ?)";
    
        PreparedStatement stmt = conn.prepareStatement(selectDateMeilleureOffreSql);
        stmt.setInt(1, idVente);
        stmt.setInt(2, idVente);
        ResultSet rs = stmt.executeQuery();
    
        if (rs.next()) {
            Timestamp dateMeilleureOffre = rs.getTimestamp("DateHeureOffre");
            rs.close();
            return dateMeilleureOffre;
        }

        rs.close();
        return null;
    }

    //Creer une offre dont la primary key est DateHeureOffre, Email, IdVente
    public static void creationOffre(Connection conn, float prix, int quantite, int idVente, String email) throws SQLException, IllegalArgumentException {

        //On récupère la vente
        String checkVenteSql = "SELECT COUNT(*) FROM Vente WHERE IdVente = ?";
        PreparedStatement checkStmt = conn.prepareStatement(checkVenteSql);
        checkStmt.setInt(1, idVente);
        ResultSet rs = checkStmt.executeQuery();

        //On verifie qu'elle existe
        if (rs.next() && rs.getInt(1) > 0) {

            Timestamp date = Timestamp.from(Instant.now());
            isDateOffreValide(conn, idVente, date);

            //On creer une Date si elle n'existe pas deja
            try {
                String insertDateSql = "INSERT INTO DateOffre (DateHeureOffre) VALUES (?)";
                PreparedStatement insertDateStmt = conn.prepareStatement(insertDateSql);
                insertDateStmt.setTimestamp(1, date);
                insertDateStmt.executeUpdate();
            } catch (SQLException e) {
            }
    
            //On creer l'offre
            String insertOffreSql = "INSERT INTO Offre (PrixAchat, QuantiteOffre, IdVente, EmailUtilisateur, DATEHEUREOFFRE) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement insertStmt = conn.prepareStatement(insertOffreSql);
            insertStmt.setFloat(1, prix);
            insertStmt.setInt(2, quantite);
            insertStmt.setInt(3, idVente);
            insertStmt.setString(4, email);
            insertStmt.setTimestamp(5, date);
            insertStmt.executeUpdate();

        } else {
            throw new SQLException("La vente n'existe plus.");
        }
    }

    //Throw une exception si l'email n'est pas valide
    public static void isEmailValide(Connection conn, String email) throws SQLException {
        String checkEmailSql = "SELECT 1 FROM Utilisateur WHERE EmailUtilisateur = ?";
        PreparedStatement stmt = conn.prepareStatement(checkEmailSql);
        stmt.setString(1, email);
        ResultSet rs = stmt.executeQuery();
        if (!rs.next()) {
            throw new IllegalArgumentException("L'email n'est pas valide.");
        }
    }
    


    //                                  ========== VENTE ==========
    //Renvoi true si la vente existe
    public static boolean doesVenteExist(Connection conn, int idVente) throws SQLException {
        String checkVenteSql = "SELECT 1 FROM Vente WHERE IdVente = ?";
        PreparedStatement stmt = conn.prepareStatement(checkVenteSql);
        stmt.setInt(1, idVente);
        ResultSet rs = stmt.executeQuery();
        boolean venteExists = rs.next();
        rs.close();
        return venteExists;
    }

    //Affiche toutes les ventes disponibles pour une salle de vente : (idVente, NomProduit, Stock, PrixDepart)
    public static String getVentes(Connection conn, int idSalle, boolean showBest) throws SQLException {
        String selectVentesSql = "SELECT IdVente, NomProduit, Stock, PrixDepart FROM Vente, Produit WHERE Vente.IdProduit = Produit.IdProduit AND IdSalle = ?";
        PreparedStatement stmt = conn.prepareStatement(selectVentesSql);
        stmt.setInt(1, idSalle);
        ResultSet rs = stmt.executeQuery();
        String ventes = "";
        while(rs.next()) {
            int idVente = rs.getInt("IdVente");
            String nomProduit = rs.getString("NomProduit");
            int stock = rs.getInt("Stock");
            float prixDepart = rs.getFloat("PrixDepart");
            
            if (showBest) {
                float meilleureOffre = getValeurMeilleureOffre(conn, idVente);
                ventes += idVente+" - "+nomProduit+" (Stock : "+stock+", Prix de départ : "+prixDepart+"€, Meilleure Offre : "+meilleureOffre+"€)\n";
            } else {
                ventes += idVente+" - "+nomProduit+" (Stock : "+stock+", Prix de départ : "+prixDepart+"€)\n";
            }
        }
        rs.close();
        return ventes;
        
    }
    
    //Renvoi l'id de la vente le plus grand (0 si aucune vente n'existe)
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

    //Creer une vente
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

    //Creer une vente limitée
    public static void creationVenteLimite(Connection conn, int idVente, Timestamp dateDebut, Timestamp dateFin) throws SQLException {

        String insertVenteLimiteSql = "INSERT INTO VenteLimite (IdVente, DateDebut, DateFin) VALUES (?, ?, ?)";

        PreparedStatement insertStmt = conn.prepareStatement(insertVenteLimiteSql);

        insertStmt.setInt(1, idVente);
        insertStmt.setTimestamp(2, dateDebut);
        insertStmt.setTimestamp(3, dateFin);

        insertStmt.executeUpdate();
        
    }

    //Creer une vente libre
    public static void creationVenteLibre(Connection conn, int idVente) throws SQLException {

        String insertVenteLibreSql = "INSERT INTO VenteLibre (IdVente) VALUES (?)";
        PreparedStatement insertStmt = conn.prepareStatement(insertVenteLibreSql);
        insertStmt.setInt(1, idVente);
        insertStmt.executeUpdate();
     
    }

    //Renvoi true si une vente est limitée
    public static boolean isVenteLimitee(Connection conn, int idVente) throws SQLException {

        String checkVenteLimiteeSql = 
            "SELECT CASE " +
            "           WHEN vlim.IdVente IS NOT NULL THEN 1 " +
            "           ELSE 0 " +
            "       END AS isLimitee " +
            "FROM Vente v " +
            "LEFT JOIN VenteLimite vlim ON v.IdVente = vlim.IdVente " +
            "WHERE v.IdVente = ?";
        
        PreparedStatement stmt = conn.prepareStatement(checkVenteLimiteeSql);
        stmt.setInt(1, idVente); 
        ResultSet rs = stmt.executeQuery();
    
        if (rs.next()) {
            boolean isLimitee = (rs.getInt("isLimitee") == 1);
            rs.close();
            stmt.close();
            return isLimitee; 
        } else {
            rs.close();
            stmt.close();
            return false; 
        }
    }

    //Récupérer la date de début
    public static Timestamp getDateDebutVenteLimitee(Connection conn, int idVente) throws SQLException {
        // Requête SQL pour récupérer la date de début
        String getDateDebutSql = "SELECT DateDebut " + "FROM VenteLimite " + "WHERE IdVente = ?";

        PreparedStatement stmt = conn.prepareStatement(getDateDebutSql);
        stmt.setInt(1, idVente);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            Timestamp dateDebut = rs.getTimestamp("DateDebut");
            rs.close();
            stmt.close();
            return dateDebut; 
        } 
        throw new SQLException("Aucune date de début");
    }

    //Récupérer la date de fin
    public static Timestamp getDateFinVenteLimitee(Connection conn, int idVente) throws SQLException {
        String getDateFinSql = "SELECT DateFin " + "FROM VenteLimite " + "WHERE IdVente = ?";

        PreparedStatement stmt = conn.prepareStatement(getDateFinSql);
        stmt.setInt(1, idVente);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {

            Timestamp dateFin = rs.getTimestamp("DateFin");
            rs.close();
            stmt.close();
            return dateFin; 

        } 
        throw new SQLException("Aucune date de fin.");
    }
    
    //Renvoi true si une enchère est finie
    public static boolean isEnchereFinie(Connection conn, int idVente) throws SQLException {

        Timestamp date = Timestamp.from(Instant.now());

        if (isVenteLimitee(conn, idVente)) {

            //Verifie que l'on est après la date de fin 
            return date.after(getDateFinVenteLimitee(conn, idVente));

        } else {

            Timestamp date_derniere_offre = getDateMeilleureOffre(conn, idVente);
            if (date_derniere_offre == null) return false;

            //Verifie que la dernière offre a eu lieu il y a + de 10 minutes
            return (date.getTime() - date_derniere_offre.getTime() > 10*60*1000);
        }
    }

}