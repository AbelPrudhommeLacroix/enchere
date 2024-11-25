-- Table des catégories de produits
CREATE TABLE Categorie (
    NomCategorie VARCHAR(255) PRIMARY KEY NOT NULL,
    Description TEXT
);

-- Table des produits
CREATE TABLE Produit (
    IdProduit INT PRIMARY KEY NOT NULL,
    NomProduit VARCHAR(255) NOT NULL,
    PrixRevient DECIMAL(10, 2),
    Stock INT CHECK (Stock >= 0),
    NomCategorie VARCHAR(255),
    FOREIGN KEY (NomCategorie) REFERENCES Categorie(NomCategorie)
);

-- Table des salles de vente
CREATE TABLE SalleDeVente (
    IdSalle INT PRIMARY KEY NOT NULL,
    NomCategorie VARCHAR(255),
    FOREIGN KEY (NomCategorie) REFERENCES Categorie(NomCategorie)
);

-- Table des ventes
CREATE TABLE Vente (
    IdVente INT PRIMARY KEY NOT NULL,
    PrixDepart DECIMAL(10, 2),
    Sens VARCHAR(255),
    Revocabilite BOOLEAN,
    NbOffres INT CHECK (NbOffres >= 0),
    IdSalle INT,
    IdProduit INT,
    FOREIGN KEY (IdSalle) REFERENCES SalleDeVente(IdSalle),
    FOREIGN KEY (IdProduit) REFERENCES Produit(IdProduit)
);

-- Table des utilisateurs
CREATE TABLE Utilisateur (
    EmailUtilisateur VARCHAR(255) PRIMARY KEY NOT NULL,
    Nom VARCHAR(255) NOT NULL,
    Prenom VARCHAR(255) NOT NULL,
    AdressePostale TEXT
);

-- Table des offres faites sur une vente
CREATE TABLE Offre (
    EmailUtilisateur VARCHAR(255),
    IdVente INT,
    DateHeureOffre DATETIME NOT NULL,
    PrixAchat DECIMAL(10, 2),
    QuantiteOffre INT CHECK (QuantiteOffre >= 0),
    PRIMARY KEY (EmailUtilisateur, IdVente, DateHeureOffre),
    FOREIGN KEY (EmailUtilisateur) REFERENCES Utilisateur(EmailUtilisateur),
    FOREIGN KEY (IdVente) REFERENCES Vente(IdVente)
);

-- Table des dates des offres
CREATE TABLE DateOffre (
    DateHeureOffre DATETIME PRIMARY KEY NOT NULL
);

-- Table des ventes limitées dans le temps
CREATE TABLE VenteLimite (
    IdVente INT,
    DateDebut DATETIME,
    DateFin DATETIME,
    PRIMARY KEY (IdVente),
    FOREIGN KEY (IdVente) REFERENCES Vente(IdVente)
);

-- Table des caractéristiques de produits
CREATE TABLE Caracteristique (
    NomCaracteristique VARCHAR(255) PRIMARY KEY NOT NULL
);

-- Table des valeurs associées aux caractéristiques des produits
CREATE TABLE ValeurCaracteristique (
    IdProduit INT,
    NomCaracteristique VARCHAR(255),
    PRIMARY KEY (IdProduit, NomCaracteristique),
    FOREIGN KEY (IdProduit) REFERENCES Produit(IdProduit),
    FOREIGN KEY (NomCaracteristique) REFERENCES Caracteristique(NomCaracteristique)
);+