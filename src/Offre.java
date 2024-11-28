import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class Offre {
    public String EmailUtilisateur;
    public int PrixAchat;
    public int QttOffre;

    public Offre(String EmailUtilisateur, int PrixAchat, int QttOffre) {
        this.EmailUtilisateur = EmailUtilisateur;
        this.PrixAchat = PrixAchat;
        this.QttOffre = QttOffre;
    }

    public static List<Offre> getOffre(Connection conn, int IdVente) {
        List<Offre> offres = new ArrayList<>();
        
        String selectUserSql = "SELECT PrixAchat, QuantiteOffre, EmailUtilisateur FROM Offre WHERE IdVente = ?";
        System.out.println("coucou1");
        try (PreparedStatement stmt = conn.prepareStatement(selectUserSql)) {
            stmt.setInt(1, IdVente);
            
            // Exécuter la requête et récupérer les résultats
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int prixAchat = rs.getInt("PrixAchat");
                    int quantiteOffre = rs.getInt("QuantiteOffre");
                    String emailUtilisateur = rs.getString("EmailUtilisateur");

                    offres.add(new Offre(emailUtilisateur, prixAchat, quantiteOffre));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return offres;
    }



    // get stock
    public static int getStock(Connection conn, int IdVente) throws SQLException {

        String getStockSql = "SELECT p.Stock FROM Produit p " + 
                          "JOIN Vente v ON p.IdProduit = v.IdProduit " + 
                          "WHERE v.IdVente = ?";
        
        PreparedStatement stmt = conn.prepareStatement(getStockSql);
        stmt.setInt(1, IdVente);
    
        // Exécuter la requête
        ResultSet rs = stmt.executeQuery();
    
        if (rs.next()) {
            int stock = rs.getInt("Stock");
            rs.close();
            return stock; 
        } else {
            rs.close();
            return 1;
        }
    }

    /*
     * Algo du sac a dos 
     * Conditions d'utilisation : 
     *      - vente montante
     *      - vente finie : vente à durée limitée ou bien vente terminée (> 10 min)
     */
    public static List<Offre> sacADos(Connection conn, int IdVente) throws SQLException {
        List<Offre> offres = getOffre(conn, IdVente);
        List<Offre> offresFinales = new ArrayList<>();
        

        // Récupérer le stock disponible
        int stockDisponible = getStock(conn, IdVente);

        // Algo du sac à dos (BackPack)
        int[] bp = new int[stockDisponible + 1];  // Tableau pour le sac à dos, bp[i] est la valeur maximale avec i unités de stock

        // Algorithme de sac à dos : maximisation du prix en fonction des quantités et du stock
        for (Offre offre : offres) {
            int qtt = offre.QttOffre;
            int prix = offre.PrixAchat;
            
            // Parcours en sens inverse pour éviter de surcharger une même offre plus d'une fois
            for (int i = stockDisponible; i >= qtt; i--) {
                if (bp[i - qtt] + qtt * prix > bp[i]) {
                    bp[i] = bp[i - qtt] + (int) (qtt * prix);
                }
            }
        }

        // Reconstruct the solution (sélectionner les offres qui maximisent la valeur)
        int remainingStock = stockDisponible;
        for (Offre offre : offres) {
            int qtt = offre.QttOffre;
            int prix = offre.PrixAchat;
            if (remainingStock >= qtt && bp[remainingStock] == bp[remainingStock - qtt] + (int) (qtt * prix)) {
                offresFinales.add(offre);
                remainingStock -= qtt;
            }
        }
        System.out.println("coucou3");
        return offresFinales;
    }
}
