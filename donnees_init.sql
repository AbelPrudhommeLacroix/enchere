CREATE TABLE Categorie (
    NomCategorie VARCHAR(255) PRIMARY KEY NOT NULL,
    Description TEXT
);


CREATE TABLE Produit (
    IdProduit INT PRIMARY KEY NOT NULL,
    NomProduit VARCHAR(255) NOT NULL,
    PrixRevient DECIMAL(10, 2),
    Stock INT CHECK (Stock >= 0),
    NomCategorie VARCHAR(255),
    FOREIGN KEY (NomCategorie) REFERENCES Categorie(NomCategorie)
);


CREATE TABLE SalleDeVente (
    IdSalle INT PRIMARY KEY NOT NULL,
    NomCategorie VARCHAR(255),
    FOREIGN KEY (NomCategorie) REFERENCES Categorie(NomCategorie)
);


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


CREATE TABLE Utilisateur (
    EmailUtilisateur VARCHAR(255) PRIMARY KEY NOT NULL,
    Nom VARCHAR(255) NOT NULL,
    Prenom VARCHAR(255) NOT NULL,
    AdressePostale TEXT
);


CREATE TABLE DateOffre (
    DateHeureOffre DATETIME PRIMARY KEY NOT NULL
);


CREATE TABLE Offre (
    EmailUtilisateur VARCHAR(255),
    IdVente INT,
    DateHeureOffre DATETIME NOT NULL,
    PrixAchat DECIMAL(10, 2),
    QuantiteOffre INT CHECK (QuantiteOffre >= 0),
    PRIMARY KEY (EmailUtilisateur, IdVente, DateHeureOffre),
    FOREIGN KEY (EmailUtilisateur) REFERENCES Utilisateur(EmailUtilisateur),
    FOREIGN KEY (IdVente) REFERENCES Vente(IdVente)
    FOREIGN KEY (DateHeureOffre) REFERENCES DateOffre(DateHeureOffre)
);


CREATE TABLE VenteLimite (
    IdVente INT,
    DateDebut DATETIME,
    DateFin DATETIME,
    PRIMARY KEY (IdVente),
    FOREIGN KEY (IdVente) REFERENCES Vente(IdVente)
);


CREATE TABLE Caracteristique (
    NomCaracteristique VARCHAR(255) PRIMARY KEY NOT NULL
);


CREATE TABLE ValeurCaracteristique (
    IdProduit INT,
    NomCaracteristique VARCHAR(255),
    PRIMARY KEY (IdProduit, NomCaracteristique),
    FOREIGN KEY (IdProduit) REFERENCES Produit(IdProduit),
    FOREIGN KEY (NomCaracteristique) REFERENCES Caracteristique(NomCaracteristique)
);