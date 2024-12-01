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
        conn.commit();
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
        int idSalle = 0;
        boolean transactionSuccess = false;
        int retryCount = 0;
        final int MAX_RETRIES = 5;
    
        while (!transactionSuccess && retryCount < MAX_RETRIES) {
            try {
                idSalle = getMaxSalleId(conn) + 1; // Récupère un ID unique
                
                String insertSalleSql = "INSERT INTO SalleDeVente (IdSalle, NomCategorie) VALUES (?, ?)";
                PreparedStatement stmt = conn.prepareStatement(insertSalleSql);
                stmt.setInt(1, idSalle);
                stmt.setString(2, categorie);
                
                stmt.executeUpdate(); // Insère la salle
                conn.commit(); // Commit la transaction
                transactionSuccess = true;
    
            } catch (SQLIntegrityConstraintViolationException e) {
                // Gestion des conflits d'ID, on réessaie
                System.err.println("[!] Conflit sur IdSalle, tentative de réessayer...");
                conn.rollback(); // Annule la transaction
                retryCount++;
            } catch (SQLException e) {
                // Autres erreurs SQL
                conn.rollback();
                throw e; // Rethrow après rollback
            }
        }
    
        if (!transactionSuccess) {
            throw new SQLException("Impossible de créer une salle après " + MAX_RETRIES + " tentatives.");
        }
    
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
        int idProduit = 0;
        boolean transactionSuccess = false;
        int retryCount = 0;
        final int MAX_RETRIES = 5; // Nombre maximum de tentatives
    
        while (!transactionSuccess && retryCount < MAX_RETRIES) {
            try {
                // Récupérer un nouvel ID unique
                idProduit = getMaxProduitId(conn) + 1;
    
                String insertProduitSql = "INSERT INTO Produit (IdProduit, NomProduit, PrixRevient, Stock, NomCategorie) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement insertStmt = conn.prepareStatement(insertProduitSql);
                insertStmt.setInt(1, idProduit);
                insertStmt.setString(2, nomProduit);
                insertStmt.setFloat(3, prixRevient);
                insertStmt.setInt(4, stock);
                insertStmt.setString(5, nomCategorie);
    
                insertStmt.executeUpdate(); // Exécute l'insertion
                conn.commit(); // Commit explicite de la transaction
                transactionSuccess = true; // Marque la transaction comme réussie
            } catch (SQLIntegrityConstraintViolationException e) {
                // Conflit d'ID détecté, on annule et réessaye avec un nouvel ID
                System.err.println("[!] Conflit sur IdProduit, tentative de réessayer...");
                conn.rollback(); // Annule la transaction
                retryCount++;
            } catch (SQLException e) {
                // Autres erreurs SQL, on annule et on lève l'exception
                conn.rollback(); // Annule la transaction
                throw e;
            }
        }
    
        if (!transactionSuccess) {
            // Si toutes les tentatives échouent
            throw new SQLException("Impossible de créer le produit après " + MAX_RETRIES + " tentatives.");
        }
    
        return idProduit;
    }
    



    //                                  ========== OFFRE ==========

    public static boolean isPrixValide(Connection conn, int idVente, float prix) throws SQLException {
        String selectProduitSql = "SELECT PrixDepart FROM Vente, Produit WHERE Vente.IdProduit = Produit.IdProduit AND IdVente = ?";
        PreparedStatement stmt = conn.prepareStatement(selectProduitSql);
        stmt.setInt(1, idVente);
        ResultSet rs = stmt.executeQuery();
    
        if (rs.next()) {
            float prixDepart = rs.getFloat("PrixDepart");
    
            // Vérifier si le prix est supérieur au prix de départ (pour une vente croissante) ou égal (pour une vente décroissante)
            if (prix < prixDepart) {
                throw new IllegalArgumentException("Le prix proposé est inférieur au prix de départ.");
            }
    
            // Vérifier que le prix est supérieur à la meilleure offre actuelle
            float meilleureOffre = getValeurMeilleureOffre(conn, idVente);
            if (prix <= meilleureOffre) {
                throw new IllegalArgumentException("Le prix proposé est inférieur ou égal à la meilleure offre actuelle.");
            }
        }
    
        rs.close();
        return true; // Le prix est valide
    }

    public static boolean isQuantiteValide(Connection conn, int idVente, int quantite) throws SQLException {
        String selectProduitSql = "SELECT Stock FROM Vente, Produit WHERE Vente.IdProduit = Produit.IdProduit AND IdVente = ?";
        PreparedStatement stmt = conn.prepareStatement(selectProduitSql);
        stmt.setInt(1, idVente);
        ResultSet rs = stmt.executeQuery();
    
        if (rs.next()) {
            int stock = rs.getInt("Stock");
    
            // Vérifier que la quantité proposée ne dépasse pas le stock disponible
            if (quantite > stock) {
                throw new IllegalArgumentException("La quantité proposée est supérieure au stock disponible.");
            }
        }
    
        rs.close();
        return true; // La quantité est valide
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
    public static void creationOffre(Connection conn, float prix, int quantite, int idVente, String email) throws SQLException {
        boolean transactionSuccess = false;
        int retryCount = 0;
        final int MAX_RETRIES = 5;
    
        while (!transactionSuccess && retryCount < MAX_RETRIES) {
            try {
                // Vérifier si la vente existe
                String checkVenteSql = "SELECT COUNT(*) FROM Vente WHERE IdVente = ?";
                PreparedStatement checkStmt = conn.prepareStatement(checkVenteSql);
                checkStmt.setInt(1, idVente);
                ResultSet rs = checkStmt.executeQuery();
    
                if (rs.next() && rs.getInt(1) > 0) {
                    Timestamp date = Timestamp.from(Instant.now());
    
                    // Vérifier la validité de l’offre
                    isDateOffreValide(conn, idVente, date);
                    isPrixValide(conn, idVente, prix);
                    isQuantiteValide(conn, idVente, quantite);
    
                    // Insérer une date si nécessaire
                    String insertDateSql = "INSERT INTO DateOffre (DateHeureOffre) VALUES (?)";
                    try {
                        PreparedStatement insertDateStmt = conn.prepareStatement(insertDateSql);
                        insertDateStmt.setTimestamp(1, date);
                        insertDateStmt.executeUpdate();
                    } catch (SQLException ignored) {
                        // Ignorer les doublons sur DateOffre
                    }
    
                    // Créer l'offre
                    String insertOffreSql = "INSERT INTO Offre (PrixAchat, QuantiteOffre, IdVente, EmailUtilisateur, DateHeureOffre) VALUES (?, ?, ?, ?, ?)";
                    PreparedStatement insertStmt = conn.prepareStatement(insertOffreSql);
                    insertStmt.setFloat(1, prix);
                    insertStmt.setInt(2, quantite);
                    insertStmt.setInt(3, idVente);
                    insertStmt.setString(4, email);
                    insertStmt.setTimestamp(5, date);
                    insertStmt.executeUpdate();
    
                    conn.commit();
                    transactionSuccess = true;
                } else {
                    throw new SQLException("La vente n'existe pas.");
                }
            } catch (SQLIntegrityConstraintViolationException e) {
                // Gérer les conflits et réessayer
                System.err.println("[!] Conflit détecté lors de la création de l'offre, tentative de réessayer...");
                conn.rollback();
                retryCount++;
            } catch (SQLException e) {
                // Annuler pour toute autre erreur SQL
                conn.rollback();
                throw e;
            }
        }
    
        if (!transactionSuccess) {
            throw new SQLException("Impossible de créer l'offre après " + MAX_RETRIES + " tentatives.");
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
        int idVente = 0;
        boolean transactionSuccess = false;
        int retryCount = 0;
        final int MAX_RETRIES = 5;
    
        while (!transactionSuccess && retryCount < MAX_RETRIES) {
            try {
                idVente = getMaxVenteId(conn) + 1;
    
                String insertVenteSql = "INSERT INTO Vente (IdVente, PrixDepart, Sens, Revocabilite, NbOffres, IdSalle, IdProduit) VALUES (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement insertStmt = conn.prepareStatement(insertVenteSql);
                insertStmt.setInt(1, idVente);
                insertStmt.setFloat(2, prixDepart);
                insertStmt.setString(3, sens);
                insertStmt.setInt(4, revocabilite);
                insertStmt.setInt(5, nbOffres);
                insertStmt.setInt(6, idSalle);
                insertStmt.setInt(7, idProduit);
    
                insertStmt.executeUpdate();
                conn.commit();
                transactionSuccess = true;
            } catch (SQLIntegrityConstraintViolationException e) {
                // Conflit sur l'ID de la vente, réessayer
                System.err.println("[!] Conflit sur IdVente, tentative de réessayer...");
                conn.rollback();
                retryCount++;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    
        if (!transactionSuccess) {
            throw new SQLException("Impossible de créer la vente après " + MAX_RETRIES + " tentatives.");
        }
    
        return idVente;
    }
    

    //Creer une vente limitée
    public static void creationVenteLimite(Connection conn, int idVente, Timestamp dateDebut, Timestamp dateFin) throws SQLException {
        try {
            String insertVenteLimiteSql = "INSERT INTO VenteLimite (IdVente, DateDebut, DateFin) VALUES (?, ?, ?)";
            PreparedStatement insertStmt = conn.prepareStatement(insertVenteLimiteSql);
            insertStmt.setInt(1, idVente);
            insertStmt.setTimestamp(2, dateDebut);
            insertStmt.setTimestamp(3, dateFin);
    
            insertStmt.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }    

    //Creer une vente libre
    public static void creationVenteLibre(Connection conn, int idVente) throws SQLException {
        try {
            String insertVenteLibreSql = "INSERT INTO VenteLibre (IdVente) VALUES (?)";
            PreparedStatement insertStmt = conn.prepareStatement(insertVenteLibreSql);
            insertStmt.setInt(1, idVente);
    
            insertStmt.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
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

    public static boolean isVenteRevocable(Connection conn, int idVente) throws SQLException {

        String checkRevocableSql = "SELECT Revocabilite FROM Vente WHERE IdVente = ?";
        
        PreparedStatement stmt = conn.prepareStatement(checkRevocableSql);
        stmt.setInt(1, idVente); 
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            return rs.getInt("Revocabilite") == 1; // Retourne true si la vente est révocable
        } else {
            throw new IllegalArgumentException("IdVente non trouvé.");
        }
    }
    
    public static float getPrixRevientProduit(Connection conn, int idVente) throws SQLException {
        // Requête SQL pour récupérer le prix de revient du produit associé à une vente
        String getPrixRevientSql = "SELECT p.PrixRevient FROM Produit p JOIN Vente v ON p.IdProduit = v.IdProduit WHERE v.IdVente = ?";
        
        PreparedStatement stmt = conn.prepareStatement(getPrixRevientSql);
        stmt.setInt(1, idVente);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            return rs.getFloat("PrixRevient"); 
        } else {
            throw new IllegalArgumentException("IdVente ou produit associé non trouvé.");
        }
    }
        
    
    

    public static boolean isVenteCroissante(Connection conn, int idVente) throws SQLException {
        String selectSensSql = "SELECT Sens FROM Vente WHERE IdVente = ?";
        PreparedStatement stmt = conn.prepareStatement(selectSensSql);
        stmt.setInt(1, idVente);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        String sens = rs.getString("Sens");
        rs.close();
        System.out.println(sens);
        return sens.equals("croissant");
    }

    // récupérer le prix de départ d'une vente
    public static float getPrixDepartVente(Connection conn, int idVente) throws SQLException {
        String selectPrixDepartSql = "SELECT PrixDepart FROM Vente WHERE IdVente = ?";
        PreparedStatement stmt = conn.prepareStatement(selectPrixDepartSql);
        stmt.setInt(1, idVente);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        float prixDepart = rs.getFloat("PrixDepart");
        rs.close();
        return prixDepart;
    }

}