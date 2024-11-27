CREATE TABLE Categorie (
    NomCategorie VARCHAR2(255) PRIMARY KEY NOT NULL,
    Description CLOB
);

CREATE TABLE Produit (
    IdProduit INT PRIMARY KEY NOT NULL,
    NomProduit VARCHAR2(255) NOT NULL,
    PrixRevient DECIMAL(10, 2),
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
    PrixDepart DECIMAL(10, 2),
    Sens VARCHAR2(255),
    Revocabilite CHAR(1) CHECK (Revocabilite IN ('Y', 'N')),
    NbOffres INT CHECK (NbOffres >= 0),
    IdSalle INT,
    IdProduit INT,
    DateHeureOffre TIMESTAMP NOT NULL,  -- Remplacer DATETIME par TIMESTAMP
    DateDebut TIMESTAMP,                -- Remplacer DATETIME par TIMESTAMP
    FOREIGN KEY (IdSalle) REFERENCES SalleDeVente(IdSalle),
    FOREIGN KEY (IdProduit) REFERENCES Produit(IdProduit)
);

CREATE TABLE Utilisateur (
    EmailUtilisateur VARCHAR2(255) PRIMARY KEY NOT NULL,
    Nom VARCHAR2(255) NOT NULL,
    Prenom VARCHAR2(255) NOT NULL,
    AdressePostale CLOB
);

CREATE TABLE DateOffre (
    DateHeureOffre TIMESTAMP PRIMARY KEY NOT NULL  -- Remplacer DATETIME par TIMESTAMP
);

CREATE TABLE Offre (
    EmailUtilisateur VARCHAR2(255),
    IdVente INT,
    DateHeureOffre TIMESTAMP NOT NULL,   -- Remplacer DATETIME par TIMESTAMP
    PrixAchat DECIMAL(10, 2),
    QuantiteOffre INT CHECK (QuantiteOffre >= 0),
    PRIMARY KEY (EmailUtilisateur, IdVente, DateHeureOffre),
    FOREIGN KEY (EmailUtilisateur) REFERENCES Utilisateur(EmailUtilisateur),
    FOREIGN KEY (IdVente) REFERENCES Vente(IdVente),
    FOREIGN KEY (DateHeureOffre) REFERENCES DateOffre(DateHeureOffre)
);

CREATE TABLE VenteLimite (
    IdVente INT,
    DateDebut TIMESTAMP,    -- Remplacer DATETIME par TIMESTAMP
    DateFin TIMESTAMP,      -- Remplacer DATETIME par TIMESTAMP
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
