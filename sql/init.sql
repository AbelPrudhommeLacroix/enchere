-- Insertion de données de test pour la base de données

-- Table Categorie
INSERT INTO Categorie (NomCategorie, DescriptionCategorie) VALUES ('Electronique', 'Appareils électroniques et gadgets');
INSERT INTO Categorie (NomCategorie, DescriptionCategorie) VALUES ('Meubles', 'Mobilier pour maison et bureau');
INSERT INTO Categorie (NomCategorie, DescriptionCategorie) VALUES ('Livres', 'Divers types de livres et magazines');

-- Table Produit
INSERT INTO Produit (IdProduit, NomProduit, PrixRevient, Stock, NomCategorie) VALUES (1, 'Téléphone', 200.00, 1, 'Electronique');
INSERT INTO Produit (IdProduit, NomProduit, PrixRevient, Stock, NomCategorie) VALUES (2, 'Table', 50.00, 1, 'Meubles');
INSERT INTO Produit (IdProduit, NomProduit, PrixRevient, Stock, NomCategorie) VALUES (3, 'Roman', 10.00, 5, 'Livres');

-- Table SalleDeVente
INSERT INTO SalleDeVente (IdSalle, NomCategorie) VALUES (1, 'Electronique');
INSERT INTO SalleDeVente (IdSalle, NomCategorie) VALUES (2, 'Meubles');
INSERT INTO SalleDeVente (IdSalle, NomCategorie) VALUES (3, 'Livres');

-- Table Vente
INSERT INTO Vente (IdVente, PrixDepart, Sens, Revocabilite, NbOffres, IdSalle, IdProduit) VALUES 
(1, 150.00, 'croissant', 1, 5, 1, 1); 
INSERT INTO Vente (IdVente, PrixDepart, Sens, Revocabilite, NbOffres, IdSalle, IdProduit) VALUES 
(2, 30.00, 'decroissant', 0, 2, 2, 2); 
INSERT INTO Vente (IdVente, PrixDepart, Sens, Revocabilite, NbOffres, IdSalle, IdProduit) VALUES 
(3, 5.00, 'croissant', 1, 10, 3, 3); 

-- Table Utilisateur
INSERT INTO Utilisateur (EmailUtilisateur, Nom, Prenom, AdressePostale) VALUES ('alice@example.com', 'Dupont', 'Alice', '123 Rue Principale');
INSERT INTO Utilisateur (EmailUtilisateur, Nom, Prenom, AdressePostale) VALUES ('bob@example.com', 'Martin', 'Bob', '456 Avenue Centrale');
INSERT INTO Utilisateur (EmailUtilisateur, Nom, Prenom, AdressePostale) VALUES ('carol@example.com', 'Durand', 'Carol', '789 Boulevard Sud');

-- Table DateOffre
INSERT INTO DateOffre (DateHeureOffre) VALUES (TO_DATE('2024-01-01 10:00:00', 'YYYY-MM-DD HH24:MI:SS'));
INSERT INTO DateOffre (DateHeureOffre) VALUES (TO_DATE('2024-01-02 15:30:00', 'YYYY-MM-DD HH24:MI:SS'));
INSERT INTO DateOffre (DateHeureOffre) VALUES (TO_DATE('2024-01-03 18:45:00', 'YYYY-MM-DD HH24:MI:SS'));

-- Table Offre
INSERT INTO Offre (EmailUtilisateur, IdVente, DateHeureOffre, PrixAchat, QuantiteOffre) VALUES 
('alice@example.com', 1, TO_DATE('2024-01-01 10:00:00', 'YYYY-MM-DD HH24:MI:SS'), 160.00, 1);
INSERT INTO Offre (EmailUtilisateur, IdVente, DateHeureOffre, PrixAchat, QuantiteOffre) VALUES 
('bob@example.com', 2, TO_DATE('2024-01-02 15:30:00', 'YYYY-MM-DD HH24:MI:SS'), 35.00, 2);
INSERT INTO Offre (EmailUtilisateur, IdVente, DateHeureOffre, PrixAchat, QuantiteOffre) VALUES 
('carol@example.com', 3, TO_DATE('2024-01-03 18:45:00', 'YYYY-MM-DD HH24:MI:SS'), 6.00, 3);

-- Table VenteLibre
INSERT INTO VenteLibre (IdVente) VALUES (1);
INSERT INTO VenteLibre (IdVente) VALUES (2);

-- Table VenteLimite
INSERT INTO VenteLimite (IdVente, DateDebut, DateFin) VALUES 
(3, TO_DATE('2024-01-03 17:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2024-01-03 19:00:00', 'YYYY-MM-DD HH24:MI:SS'));

-- Table Caracteristique
INSERT INTO Caracteristique (NomCaracteristique) VALUES ('Couleur');
INSERT INTO Caracteristique (NomCaracteristique) VALUES ('Taille');
INSERT INTO Caracteristique (NomCaracteristique) VALUES ('Poids');

-- Table ValeurCaracteristique
INSERT INTO ValeurCaracteristique (IdProduit, NomCaracteristique, Valeur) VALUES (1, 'Couleur', 'Gris');
INSERT INTO ValeurCaracteristique (IdProduit, NomCaracteristique, Valeur) VALUES (2, 'Taille', '50cm');
INSERT INTO ValeurCaracteristique (IdProduit, NomCaracteristique, Valeur) VALUES (3, 'Poids', '0,5kg');