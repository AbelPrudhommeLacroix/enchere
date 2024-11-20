CREATE TABLE TypeMission(
    TypeMission VARCHAR(255) PRIMARY KEY NOT NULL
);

CREATE TABLE Milieu(
    CompositionMilieu VARCHAR(255) PRIMARY KEY NOT NULL
);

CREATE TABLE Pilote(
    CodePilote INT PRIMARY KEY NOT NULL,
    Nom VARCHAR(255) NOT NULL,
    Prenom VARCHAR(255) NOT NULL,
    Age INT (Age>=18 and Age<=65),
    Grade VARCHAR(255)
);

CREATE TABLE Mission(
    Mission VARCHAR(255) PRIMARY KEY NOT NULL,
    DateMission INT,
    VitesseMin INT (VitesseMin >= 0),
    RayonEng INT (RayonEng > 0),
    NbVaisseaux INT (NbVaisseaux >=1),
    TypeMission VARCHAR(255),
    FOREIGN KEY (TypeMission) REFERENCES TypeMission(TypeMission)
);

CREATE TABLE Equipage(
    CodeEquipage INT PRIMARY KEY NOT NULL,
    Effectif INT (Effectif > 0)
);

CREATE TABLE Galaxie(
    CodeGalaxie INT PRIMARY KEY NOT NULL,
    NomGalaxie VARCHAR(255) NOT NULL,
    Distance INT (Distance >= 0)
);

CREATE TABLE Planete(
    CodeGalaxie INT NOT NULL,
    CodePlanete INT NOT NULL,
    PRIMARY KEY(CodeGalaxie, CodePlanete)
    FOREIGN KEY(CodeGalaxie) REFERENCES Galaxie(CodeGalaxie)
    NomPlanete VARCHAR(255) NOT NULL,
    Vitesse INT (Vitesse >= 0),
    EtatSoumis VARCHAR(255) NOT NULL,
    CompositionMilieu VARCHAR(255) NOT NULL,
    FOREIGN KEY (CompositionMilieu) REFERENCES Milieu(CompositionMilieu)
);

CREATE TABLE Navire(
    CodeNavire INT PRIMARY KEY NOT NULL,
    RayonAction INT (RayonAction >=0),
    NbPilotes INT (NbPilotes >=1),
    VitesseMax INT (VitesseMax > 0)
);

CREATE TABLE NavireTransport(
    CodeNavire INT PRIMARY KEY NOT NULL,
    FOREIGN KEY (CodeNavire) REFERENCES Navire(CodeNavire),
    Capacite INT (Capacite>=0)
);

CREATE TABLE NavireCombat(
    CodeNavire INT PRIMARY KEY NOT NULL,
    FOREIGN KEY (CodeNavire) REFERENCES Navire(CodeNavire),
    EquipageMin INT (EquipageMin >=1),
    EquipageMax INT (EquipageMax >= EquipageMin)
);

CREATE TABLE Cible(
    CodeMission INT PRIMARY KEY NOT NULL,
    FOREIGN KEY (CodeMission) REFERENCES Navire(CodeNavire),
    FOREIGN KEY (CodeGalaxie) REFERENCES Galaxie(CodeGalaxie),
    FOREIGN KEY (CodePlanete) REFERENCES Planete(CodePlanete)
);

CREATE TABLE FormationPilote(
    CodePilote INT NOT NULL,
    TypeMission VARCHAR(255) NOT NULL,
    PRIMARY KEY (CodePilote,TypeMission),
    FOREIGN KEY (CodePilote) REFERENCES Pilote(CodePilote),
    FOREIGN KEY (TypeMission) REFERENCES TypeMission(TypeMission)
);

CREATE TABLE CapaciteAPiloter(
    CodePilote INT,
    CodeNavire INT,
    PRIMARY KEY (CodePilote,CodeNavire),
    FOREIGN KEY (CodePilote) REFERENCES Pilote(CodePilote),
    FOREIGN KEY (CodeNavire) REFERENCES Navire(CodeNavire)
);

CREATE TABLE FormationEquipage(FOREIGN KEY (TypeMission) REFERENCES TypeMission(TypeMission)
    CodeEquipage INT,
    TypeMission VARCHAR(255),
    PRIMARY KEY (CodeEquipage,TypeMission),
    FOREIGN KEY (CodeEquipage) REFERENCES Equipage(CodeEquipage),
    FOREIGN KEY (TypeMission) REFERENCES TypeMission(TypeMission)
);

CREATE TABLE CompatibiliteNavire(
    CodeNavire INT,
    CompositionMilieu VARCHAR(255),
    PRIMARY KEY (CodeNavire, CompositionMilieu),
    FOREIGN KEY (CodeNavire) REFERENCES Navire(CodeNavire),
    FOREIGN KEY (CompositionMilieu) REFERENCES Milieu(CompositionMilieu)
);

CREATE TABLE AffectationEquipage(
    CodeEquipage INT,
    CodeMission INT,
    CodeNavire INT,
    PRIMARY KEY (CodeEquipage,CodeMission,CodeNavire),
    FOREIGN KEY (CodeEquipage) REFERENCES Equipage(CodeEquipage),
    FOREIGN KEY (CodeMission) REFERENCES Mission(CodeMission),
    FOREIGN KEY (CodeNavire) REFERENCES Navire(CodeNavire)
);

CREATE TABLE AffectationPilote(
    CodePilote INT,
    CodeMission INT,
    CodeNavire INT,
    PRIMARY KEY (CodePilote, CodeMission, CodeNavire),
    FOREIGN KEY (CodePilote) REFERENCES Pilote(CodePilote),
    FOREIGN KEY (CodeMission) REFERENCES Mission(CodeMission),
    FOREIGN KEY (CodeNavire) REFERENCES Navire(CodeNavire)
)
