### Offres montantes

# Recupérer la dernière offre d_une vente montante à durée libre et non révocable

SELECT DateOffre.DateHeureOffre, Utilisateur.EmailUtilisateur 
FROM Utilisateur, Offre, Vente, DateOffre, VenteLibre
WHERE Offre.EmailUtilisateur = Utilisateur.EmailUtilisateur
AND Offre.IdVente = Vente.IdVente
AND Offre.DateHeureOffre = DateOffre.DateHeureOffre
AND Vente.IdVente = VenteLibre.IdVente
AND Vente.IdVente = :idVente
ORDER BY DateOffre.DateHeureOffre DESC
LIMIT 1;


# Recupérer le gagnant d_une vente montante à durée fixe et non révocable

SELECT DateOffre.DateHeureOffre, Utilisateur.EmailUtilisateur 
FROM Utilisateur, Offre, Vente, DateOffre, VenteLimite
WHERE Offre.EmailUtilisateur = Utilisateur.EmailUtilisateur
AND Offre.IdVente = Vente.IdVente
AND Offre.DateHeureOffre = DateOffre.DateHeureOffre
AND Vente.IdVente = VenteLimitee.IdVente
AND DateOffre.DateHeureOffre <= VenteLimite.Fin
AND Vente.IdVente = :idVente
ORDER BY DateOffre.DateHeureOffre DESC
LIMIT 1;


# Recupérer la dernière offre d_une vente montante à durée libre et révocable

SELECT DateOffre.DateHeureOffre, Utilisateur.EmailUtilisateur 
FROM Utilisateur, Offre, Vente, DateOffre, VenteLibre, Produit
WHERE Offre.EmailUtilisateur = Utilisateur.EmailUtilisateur
AND Offre.IdVente = Vente.IdVente
AND Offre.DateHeureOffre = DateOffre.DateHeureOffre
AND Vente.IdVente = VenteLibre.IdVente
AND Vente.IdProduit = Produit.IdProduit
AND Offre.PrixAchat >= Produit.PrixRevient
AND Vente.IdVente = :idVente
ORDER BY DateOffre.DateHeureOffre DESC
LIMIT 1;


# Recupérer le gagnant d_une vente montante à durée fixe et révocable

SELECT DateOffre.DateHeureOffre, Utilisateur.EmailUtilisateur 
FROM Utilisateur, Offre, Vente, DateOffre, VenteLimite, Produit
WHERE Offre.EmailUtilisateur = Utilisateur.EmailUtilisateur
AND Offre.IdVente = Vente.IdVente
AND Offre.DateHeureOffre = DateOffre.DateHeureOffre
AND Vente.IdVente = VenteLimitee.IdVente
AND DateOffre.DateHeureOffre <= VenteLimite.Fin
AND Vente.IdProduit = Produit.IdProduit
AND Offre.PrixAchat >= Produit.PrixRevient
AND Vente.IdVente = :idVente
ORDER BY DateOffre.DateHeureOffre DESC
LIMIT 1;



### Offres descendantes

# Recupérer la dernière offre d_une vente descendante à durée libre et non révocable

SELECT DateOffre.DateHeureOffre, Utilisateur.EmailUtilisateur 
FROM Utilisateur, Offre, Vente, DateOffre, VenteLibre
WHERE Offre.EmailUtilisateur = Utilisateur.EmailUtilisateur
AND Offre.IdVente = Vente.IdVente
AND Offre.DateHeureOffre = DateOffre.DateHeureOffre
AND Vente.IdVente = VenteLibre.IdVente
AND Vente.IdVente = :idVente
ORDER BY DateOffre.DateHeureOffre ASC
LIMIT 1;


# Recupérer le gagnant d_une vente descendante à durée fixe et non révocable

SELECT DateOffre.DateHeureOffre, Utilisateur.EmailUtilisateur 
FROM Utilisateur, Offre, Vente, DateOffre, VenteLimite
WHERE Offre.EmailUtilisateur = Utilisateur.EmailUtilisateur
AND Offre.IdVente = Vente.IdVente
AND Offre.DateHeureOffre = DateOffre.DateHeureOffre
AND Vente.IdVente = VenteLimitee.IdVente
AND DateOffre.DateHeureOffre <= VenteLimite.Fin
AND Vente.IdVente = :idVente
ORDER BY DateOffre.DateHeureOffre ASC
LIMIT 1;


# Recupérer la dernière offre d_une vente descendante à durée libre et révocable

SELECT DateOffre.DateHeureOffre, Utilisateur.EmailUtilisateur 
FROM Utilisateur, Offre, Vente, DateOffre, VenteLibre, Produit
WHERE Offre.EmailUtilisateur = Utilisateur.EmailUtilisateur
AND Offre.IdVente = Vente.IdVente
AND Offre.DateHeureOffre = DateOffre.DateHeureOffre
AND Vente.IdVente = VenteLibre.IdVente
AND Vente.IdProduit = Produit.IdProduit
AND Offre.PrixAchat >= Produit.PrixRevient
AND Vente.IdVente = :idVente
ORDER BY DateOffre.DateHeureOffre ASC
LIMIT 1;


# Recupérer le gagnant d_une vente descendante à durée fixe et révocable

SELECT DateOffre.DateHeureOffre, Utilisateur.EmailUtilisateur 
FROM Utilisateur, Offre, Vente, DateOffre, VenteLimite, Produit
WHERE Offre.EmailUtilisateur = Utilisateur.EmailUtilisateur
AND Offre.IdVente = Vente.IdVente
AND Offre.DateHeureOffre = DateOffre.DateHeureOffre
AND Vente.IdVente = VenteLimitee.IdVente
AND DateOffre.DateHeureOffre <= VenteLimite.Fin
AND Vente.IdProduit = Produit.IdProduit
AND Offre.PrixAchat >= Produit.PrixRevient
AND Vente.IdVente = :idVente
ORDER BY DateOffre.DateHeureOffre ASC
LIMIT 1;




### Faire une offre

## Si le nombre d_offres est limité
# Compter les offres de quelqu_un pour les ventes à offres limitées (si ça concerne une vente avec une offre max et que l_utilisateur a déjà fait une offre on accepte pas sa nouvelle offre)

SELECT Count(*) AS NombreOffres, Utilisateur.EmailUtilisateur
FROM Utilisateur, Vente
WHERE Offre.EmailUtilisateur = Vente.EmailUtilisateur
AND Offre.EmailUtilisateur = :emailUtilisateur
AND Vente.NbOffres > 0


## Vérifier que l_offre est bien supérieure à la dernière offre effectuée (si montante)
# Trouver la dernière offre effectuée et le prix d_achat de celle-ci
SELECT Offre.DateHeureOffre, Offre.PrixAchat
FROM Offre
JOIN Vente ON Offre.IdVente = Vente.IdVente
JOIN DateOffre ON Offre.DateHeureOffre = DateOffre.DateHeureOffre
ORDER BY DateOffre.DateHeureOffre DESC
LIMIT 1;

## Vérifier que l_offre a été effectuée avant la date de fin de la vente (si durée limitée)

SELECT COUNT(*) AS OffreValide
FROM Offre
JOIN Vente ON Offre.IdVente = Vente.IdVente
JOIN VenteLimite vl ON Vente.IdVente = vl.IdVente
WHERE Offre.IdVente = :idVente
  AND Offre.DateHeureOffre <= vl.DateFin;




### Mise en place d_une salle de vente

##Comparer la catégorie du produit qu_on veut ajouter à la salle avec la catégorie de la salle
# Récupérer la catégorie de la salle de Vente

SELECT 
    p.IdProduit, 
    p.NomProduit, 
    p.NomCategorie AS CategorieProduit,
    s.IdSalle, 
    s.NomCategorie AS CategorieSalle
FROM Produit p
JOIN SalleDeVente s ON p.NomCategorie = s.NomCategorie
WHERE p.IdProduit = :idProduit
  AND s.IdSalle = :idSalle;

##Comparer le type de la vente avec celui des ventes faites dans la salle quand on veut ajouter une vente à la salle (le sens, la révocabilité, le nombre d_offres)

SELECT 
    V.IdVente AS VenteProposee,
    V.Sens AS SensVenteProposee,
    V.Revocabilite AS RevocabiliteVenteProposee,
    V.NbOffres AS NbOffresVenteProposee,
    Vs.IdVente AS VenteSalle,
    Vs.Sens AS SensVenteSalle,
    Vs.Revocabilite AS RevocabiliteVenteSalle,
    Vs.NbOffres AS NbOffresVenteSalle
FROM Vente V
LEFT JOIN Vente Vs ON Vs.IdSalle = :idSalle
WHERE V.IdVente = :idVente
AND Vs.IdSalle = :idSalle
AND V.Sens != Vs.Sens
AND V.Revocabilite != Vs.Revocabilite
AND V.NbOffres != Vs.NbOffres;


