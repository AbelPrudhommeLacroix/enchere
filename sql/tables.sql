DROP TABLE ValeurCaracteristique;
DROP TABLE Caracteristique;
DROP TABLE VenteLibre;
DROP TABLE VenteLimite;
DROP TABLE Offre;
DROP TABLE DateOffre;
DROP TABLE Utilisateur;
DROP TABLE Vente;
DROP TABLE SalleDeVente;
DROP TABLE Produit;
DROP TABLE Categorie;

CREATE TABLE Categorie (
    NomCategorie VARCHAR2(255) PRIMARY KEY NOT NULL,
    DescriptionCategorie VARCHAR2(255)
);

CREATE TABLE Produit (
    IdProduit INT PRIMARY KEY NOT NULL,
    NomProduit VARCHAR2(255) NOT NULL,
    PrixRevient FLOAT NOT NULL,
    Stock INT CHECK (Stock >= 0),
    NomCategorie VARCHAR2(255),
    FOREIGN KEY (NomCategorie) REFERENCES Categorie(NomCategorie)
);

CREATE TABLE SalleDeVente (
    IdSalle INT PRIMARY KEY NOT NULL,
    NomCategorie VARCHAR2(255),
    FOREIGN KEY (NomCategorie) REFERENCES Categorie(NomCategorie)
);

CREATE TABLE Vente (
    IdVente INT PRIMARY KEY NOT NULL,
    PrixDepart FLOAT NOT NULL,
    Sens VARCHAR2(255),
    Revocabilite NUMBER(1) CHECK (Revocabilite IN (0, 1)),
    NbOffres INT CHECK (NbOffres >= 0),
    IdSalle INT,
    IdProduit INT,
    FOREIGN KEY (IdSalle) REFERENCES SalleDeVente(IdSalle),
    FOREIGN KEY (IdProduit) REFERENCES Produit(IdProduit)
);

CREATE TABLE Utilisateur (
    EmailUtilisateur VARCHAR2(255) PRIMARY KEY NOT NULL,
    Nom VARCHAR2(255) NOT NULL,
    Prenom VARCHAR2(255) NOT NULL,
    AdressePostale VARCHAR2(255)
);

CREATE TABLE DateOffre (
    DateHeureOffre TIMESTAMP PRIMARY KEY NOT NULL  
);

CREATE TABLE Offre (
    EmailUtilisateur VARCHAR2(255),
    IdVente INT,
    DateHeureOffre TIMESTAMP NOT NULL,  
    PrixAchat FLOAT NOT NULL,
    QuantiteOffre INT CHECK (QuantiteOffre >= 0),
    PRIMARY KEY (EmailUtilisateur, IdVente, DateHeureOffre),
    FOREIGN KEY (EmailUtilisateur) REFERENCES Utilisateur(EmailUtilisateur),
    FOREIGN KEY (IdVente) REFERENCES Vente(IdVente),
    FOREIGN KEY (DateHeureOffre) REFERENCES DateOffre(DateHeureOffre)
);

CREATE TABLE VenteLimite (
    IdVente INT,
    DateDebut TIMESTAMP,
    DateFin TIMESTAMP, 
    PRIMARY KEY (IdVente),
    FOREIGN KEY (IdVente) REFERENCES Vente(IdVente)
);

CREATE TABLE VenteLibre (
    IdVente INT,
    PRIMARY KEY (IdVente),
    FOREIGN KEY (IdVente) REFERENCES Vente(IdVente)
);

CREATE TABLE Caracteristique (
    NomCaracteristique VARCHAR2(255) PRIMARY KEY NOT NULL
);

CREATE TABLE ValeurCaracteristique (
    IdProduit INT,
    NomCaracteristique VARCHAR2(255),
    Valeur VARCHAR2(255),
    PRIMARY KEY (IdProduit, NomCaracteristique),
    FOREIGN KEY (IdProduit) REFERENCES Produit(IdProduit),
    FOREIGN KEY (NomCaracteristique) REFERENCES Caracteristique(NomCaracteristique)
);
