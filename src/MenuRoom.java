import java.util.Scanner;
import java.sql.*;

public class MenuRoom {

    public static void launch(Connection conn, Scanner scanner) {
        
        System.out.println("\n=========== Création d'une salle ===========");

        //Affichage des catégories
        try {
            System.out.println("\n------ Liste des catégories disponibles ------"); 
            String categories = DBQueries.searchCategories(conn, scanner);
            System.out.println(categories);
        } catch (SQLException e) {
            System.err.println("[!] Erreur dans l'affichage des categories : " + e); 
            return;
        }

        //Choix de la catégorie
        String categorie = "";
        while (categorie.equals("")) {
            System.out.print("La catégorie de votre salle : ");
            categorie = scanner.next(); 
            try {
                if (!DBQueries.doesCategoryExist(conn, categorie)) throw new Exception();
            } catch (Exception e){
                System.err.println("[!] La categorie n'existe pas."); 
                categorie = "";
            }
        }

        //Sens des ventes
        String sens = "";
        while(sens.equals("")) {
            System.out.print("Sens des ventes (croissant/decroissant) : ");
            sens = scanner.next();
            if (!sens.equals("croissant") && !sens.equals("decroissant")) {
                System.err.println("[!] Mauvais choix, choix disponible : (croissant/decroissant)");
                sens = "";
            }
        }

        //Revocabilite
        int revocabilite = -1;
        while(revocabilite == -1) {
            System.out.print("Revocabilite (oui/non) : ");
            String revocabilite_choix = scanner.next();
            switch (revocabilite_choix) {
                case "oui" -> revocabilite = 1;
                case "non" -> revocabilite = 0;
                default -> {
                    System.err.println("[!] Mauvais choix, choix disponible : (oui/non)");
                    revocabilite = -1;
                }
            }
        }

        //Nombre d'offre max
        int nb_offre = -1;
        System.out.print("Nombre d'offres maximum : ");
        while(nb_offre == -1) {
            try {
                nb_offre = scanner.nextInt();
                if (nb_offre <= 0) throw new Exception();
            } catch (Exception e) {
                System.err.println("[!] Veuillez renseigner un entier strictement positif.");
                scanner.nextLine();
                nb_offre = -1;
            }
        }

        //Duree de la vente
        String duree_vente = "";
        while(duree_vente.equals("")) {
            System.out.print("Duree de la vente (libre/limite) : ");
            duree_vente = scanner.next();
            if (!duree_vente.equals("libre") && !duree_vente.equals("limite")) {
                System.err.println("[!] Mauvais choix, choix disponible : (croissant/decroissant)");
                duree_vente = "";
            }
        }

        //Creation de la salle
        int id_salle;
        try {
            id_salle = DBQueries.creationSalle(conn, scanner, categorie);
        } catch (Exception e) {
            System.err.println("Erreur lors de la creation de la salle : " + e.getMessage());
            return;
        }

        //Nombre de ventes
        int nb_ss_lots = -1;
        while(nb_ss_lots == -1) {

            System.out.print("Nombre de sous-lots de produits dans votre salle : ");
            try {
                nb_ss_lots = scanner.nextInt();
                if (nb_ss_lots < 0) throw new Exception();
            } catch (Exception e) {
                System.err.println("[!] Veuillez renseigner un entier positif (ou nul).");
                scanner.nextLine();
                nb_ss_lots = -1;
            }
        }

        //Insertion des differents produits
        int ind_ss_lot = 1;
        while(nb_ss_lots > 0) {

            System.out.println("\n----- Ajout du sous-lot n°"+(ind_ss_lot++)+" -----");
            
            //Nom de produit
            System.out.print("Nom de votre produit : ");
            String nom_produit = scanner.next();

            //Prix de revient
            float prix_revient = -1;
            while(prix_revient == -1) {
                System.out.print("Prix de revient de votre produit (ex: 9.50) : ");

                try {
                    prix_revient = scanner.nextFloat();
                } catch (Exception e) {
                    System.out.println("[!] Erreur : Vous devez entrer un nombre valide.");
                    scanner.nextLine();
                    prix_revient = -1;
                }
            }

            //Prix de depart
            float prix_depart = -1;
            while(prix_depart == -1) {
                System.out.print("Prix de depart de votre produit (ex: 18.50) : ");
                
                try {
                    prix_depart = scanner.nextFloat();
                } catch (Exception e) {
                    System.out.println("[!] Erreur : Vous devez entrer un nombre valide.");
                    scanner.nextLine();
                    prix_depart = -1;
                }
            }
            //Prix de revient
            int stock = -1;

            while(stock == -1) {
                System.out.print("Nombre de produits dans votre sous-lots : ");
                
                try {
                    stock = scanner.nextInt();
                    scanner.nextLine(); //skip le saut de ligne
                    if (stock <= 0) throw new Exception();
                } catch (Exception e) {
                    System.err.println("[!] Veuillez renseigner un entier strictement positif.");
                    scanner.nextLine();
                    stock = -1;
                }
            }

            //Creation du produit
            int id_produit;
            try {
                id_produit = DBQueries.creationProduit(conn, nom_produit, prix_revient, stock, categorie);
            } catch (Exception e) {
                System.err.println("[!] Erreur à la création du produit.");
                return;
            }

            //Creation de la vente
            int id_vente;
            try {
                id_vente = DBQueries.creationVente(conn, prix_depart, sens,revocabilite, nb_offre, id_salle, id_produit);
            } catch (Exception e) {
                System.err.println("[!] Erreur à la création de la vente." + e.getMessage());
                return;
            }

            //Initialisation des ventes limitée
            if (duree_vente.equals("limite")) {

                String date_debut_str = "";
                String date_fin_str = "";

                while(date_debut_str.equals("") || date_fin_str.equals("")) {

                    System.out.print("Date de debut de la vente (format: yyyy-MM-dd HH:mm:ss) : ");

                    
                    date_debut_str = scanner.nextLine();

                    System.out.print("Date de fin de la vente (format: yyyy-MM-dd HH:mm:ss) : ");
                    date_fin_str = scanner.nextLine();

                    Timestamp date_debut, date_fin;
                    try {
                        date_debut = HCInterface.convertToTimestamp(date_debut_str);
                        date_fin = HCInterface.convertToTimestamp(date_fin_str);
                        //TODO : Verifier si date_fin > date_debut
                        //TODO : Verifier que date_debut >= date actuelle
                    } catch (Exception e) {
                        System.err.println("[!] Erreur, format de date incorrect : "+e.getMessage());
                        date_debut_str = "";
                        date_fin_str = "";
                        continue;
                    }

                    try {
                        DBQueries.creationVenteLimite(conn, id_vente, date_debut, date_fin);
                    } catch (Exception e) {
                        System.err.println("[!] Erreur à la création de la vente limitée "+e.getMessage());
                        return;
                    }
                }
            } else {

                try {
                    DBQueries.creationVenteLibre(conn, id_vente);
                } catch (Exception e) {
                    System.err.println("[!] Erreur à la création de la vente libre "+e.getMessage());
                    return;
                }
            }

            nb_ss_lots--;
        }
        System.out.println("\n[☺] Salle de vente crée !");
    }
}