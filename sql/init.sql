-- Insertion de données de test pour la base de données

-- Table Categorie
INSERT INTO Categorie (NomCategorie, Description) VALUES
('Electronique', 'Appareils électroniques et gadgets'),
('Meubles', 'Mobilier pour maison et bureau'),
('Livres', 'Divers types de livres et magazines');

-- Table Produit
INSERT INTO Produit (IdProduit, NomProduit, PrixRevient, Stock, NomCategorie) VALUES
(1, 'Téléphone', 200.00, 50, 'Electronique'),
(2, 'Table', 50.00, 20, 'Meubles'),
(3, 'Roman', 10.00, 100, 'Livres');

-- Table SalleDeVente
INSERT INTO SalleDeVente (IdSalle, NomCategorie) VALUES
(1, 'Electronique'),
(2, 'Meubles'),
(3, 'Livres');

-- Table Vente
INSERT INTO Vente (IdVente, PrixDepart, Sens, Revocabilite, NbOffres, IdSalle, IdProduit) VALUES
(1, 150.00, 'Croissant', TRUE, 5, 1, 1),
(2, 30.00, 'Décroissant', FALSE, 2, 2, 2),
(3, 5.00, 'Croissant', TRUE, 10, 3, 3);

-- Table Utilisateur
INSERT INTO Utilisateur (EmailUtilisateur, Nom, Prenom, AdressePostale) VALUES
('alice@example.com', 'Dupont', 'Alice', '123 Rue Principale'),
('bob@example.com', 'Martin', 'Bob', '456 Avenue Centrale'),
('carol@example.com', 'Durand', 'Carol', '789 Boulevard Sud');

-- Table DateOffre
INSERT INTO DateOffre (DateHeureOffre) VALUES
('2024-01-01 10:00:00'),
('2024-01-02 15:30:00'),
('2024-01-03 18:45:00');

-- Table Offre
INSERT INTO Offre (EmailUtilisateur, IdVente, DateHeureOffre, PrixAchat, QuantiteOffre) VALUES
('alice@example.com', 1, '2024-01-01 10:00:00', 160.00, 1),
('bob@example.com', 2, '2024-01-02 15:30:00', 35.00, 2),
('carol@example.com', 3, '2024-01-03 18:45:00', 6.00, 3);

-- Table VenteLibre
INSERT INTO VenteLibre (IdVente) VALUES
(1),
(2);

-- Table VenteLimite
INSERT INTO VenteLimite (IdVente, DateDebut, DateFin) VALUES
(3, '2024-01-03 17:00:00', '2024-01-03 19:00:00');

-- Table Caracteristique
INSERT INTO Caracteristique (NomCaracteristique) VALUES
('Couleur'),
('Taille'),
('Poids');

-- Table ValeurCaracteristique
INSERT INTO ValeurCaracteristique (IdProduit, NomCaracteristique, Valeur) VALUES
(1, 'Couleur', 'Gris'),
(2, 'Taille', '50cm'),
(3, 'Poids', '0,5kg');
