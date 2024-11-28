-- Faire une offre


-- Si la vente est montante
-- Pour ensuite vérifier que l'offre est bien supérieure à la dernière offre effectuée
-- On renvoie la dernière offre effectuée et le prix d'achat de celle-ci qu'on comparera avec le prix d'achat de l'offre proposée
SELECT Offre.DateHeureOffre, Offre.PrixAchat
FROM Offre
JOIN Vente ON Offre.IdVente = Vente.IdVente
JOIN DateOffre ON Offre.DateHeureOffre = DateOffre.DateHeureOffre
WHERE Vente.IdVente = :idVente
ORDER BY DateOffre.DateHeureOffre DESC
FETCH FIRST ROW ONLY;


-- Si le nombre d_offres est limité
-- Compter les offres de quelqu_un pour les ventes à offres limitées 
--(si ça concerne une vente avec une offre max et que l'utilisateur a déjà fait une offre on accepte pas sa nouvelle offre)

SELECT COUNT(*) AS NombreOffres
FROM Offre
WHERE Offre.EmailUtilisateur = :emailUtilisateur
AND Offre.IdVente = :idVente;


-- Si la durée est limitée
-- Vérifier que l'offre a été effectuée avant la date de fin de la vente

SELECT COUNT(*) AS OffreValide
FROM Offre
JOIN Vente ON Offre.IdVente = Vente.IdVente
JOIN VenteLimite ON Vente.IdVente = VenteLimite.IdVente
JOIN DateOffre ON DateOffre.DateHeureOffre = Offre.DateHeureOffre
WHERE Offre.IdVente = :idVente
AND DateOffre.DateHeureOffre = :dateOffre
AND Offre.DateHeureOffre <= VenteLimite.DateFin;


-- S'il s'agit de la première offre effectuée sur une offre montante
-- Tester si le prix d'achat est supérieur au prix de départ de la vente

SELECT COUNT(*) AS OffreValide
FROM Offre
JOIN Vente ON Offre.IdVente = Vente.IdVente
WHERE Offre.IdVente = :idVente
AND Offre.EmailUtilisateur = :emailUtilisateur
AND Offre.PrixAchat > Vente.PrixDepart;


-- Si la vente est descendante, si une seule offre est présente la vente est fermée
-- On teste donc s'il y a une offre dans la vente

SELECT COUNT(*) AS VenteFermee
FROM Offre
JOIN Vente ON Offre.IdVente = Vente.IdVente
WHERE Offre.IdVente = :idVente
AND Vente.Sens = 'decroissant';





-- Offres montantes

-- Recupérer la dernière offre d_une vente montante à durée libre et non révocable

SELECT Offre.*
FROM Offre
JOIN Vente ON Offre.IdVente = Vente.IdVente
JOIN VenteLibre ON Vente.IdVente = VenteLibre.IdVente
WHERE Vente.Sens = 'croissant'
AND Vente.Revocabilite = 0
AND Vente.IdVente = :idVente
ORDER BY Offre.DateHeureOffre DESC
FETCH FIRST ROW ONLY;


-- Recupérer le gagnant d_une vente montante à durée fixe et non révocable

SELECT Offre.*
FROM Offre
JOIN Vente ON Offre.IdVente = Vente.IdVente
JOIN VenteLimite ON Vente.IdVente = VenteLimite.IdVente
WHERE Vente.Sens = 'croissant'
AND Vente.Revocabilite = 0
AND Vente.IdVente = :idVente
AND VenteLimite.DateFin > Offre.DateHeureOffre
ORDER BY Offre.DateHeureOffre DESC
FETCH FIRST ROW ONLY;


-- Recupérer la dernière offre d_une vente montante à durée libre et révocable

SELECT Offre.EmailUtilisateur, Offre.PrixAchat, Produit.PrixRevient,
  CASE 
    WHEN o.PrixAchat < p.PrixRevient THEN 'Révoquer'
    ELSE 'Conserver'
  END AS StatutVente
FROM Offre
JOIN Vente ON Offre.IdVente = Vente.IdVente
JOIN Produit ON Vente.IdProduit = Produit.IdProduit
JOIN VenteLibre ON Vente.IdVente = VenteLibre.IdVente
WHERE Vente.Sens = 'croissant'
AND Vente.Revocabilite = 1
AND Vente.IdVente = :idVente
ORDER BY Offre.DateHeureOffre DESC, Offre.PrixAchat DESC
FETCH FIRST ROW ONLY;


-- Recupérer le gagnant d_une vente montante à durée fixe et révocable

SELECT Offre.EmailUtilisateur, Offre.PrixAchat, Produit.PrixRevient,
  CASE 
    WHEN Offre.PrixAchat < Produit.PrixRevient THEN 'Révoquer'
    ELSE 'Conserver'
  END AS StatutVente
FROM Offre
JOIN Vente ON Offre.IdVente = Vente.IdVente
JOIN Produit ON Vente.IdProduit = Produit.IdProduit
JOIN VenteLimite ON Vente.IdVente = VenteLimite.IdVente
WHERE Vente.Sens = 'croissant'
AND Vente.Revocabilite = 1
AND Vente.IdVente = :idVente
AND VenteLimite.DateFin > Offre.DateHeureOffre
ORDER BY Offre.DateHeureOffre DESC
FETCH FIRST ROW ONLY;



-- Offres descendantes

-- Recupérer la dernière offre d_une vente descendante à durée libre et non révocable

SELECT Offre.*
FROM Offre
JOIN Vente ON Offre.IdVente = Vente.IdVente
JOIN VenteLibre ON Vente.IdVente = VenteLibre.IdVente
WHERE Vente.Sens = 'decroissant'
AND Vente.Revocabilite = 0
AND Vente.IdVente = :idVente
ORDER BY Offre.DateHeureOffre ASC
FETCH FIRST ROW ONLY;


-- Recupérer le gagnant d_une vente descendante à durée fixe et non révocable

SELECT Offre.*
FROM Offre
JOIN Vente ON Offre.IdVente = Vente.IdVente
JOIN VenteLimite ON Vente.IdVente = VenteLimite.IdVente
WHERE Vente.Sens = 'decroissant'
AND Vente.Revocabilite = 0
AND Vente.IdVente = :idVente
AND VenteLimite.DateFin > Offre.DateHeureOffre
ORDER BY Offre.DateHeureOffre ASC
FETCH FIRST ROW ONLY;


-- Recupérer la dernière offre d_une vente descendante à durée libre et révocable

SELECT Offre.EmailUtilisateur, Offre.PrixAchat, Produit.PrixRevient,
  CASE 
    WHEN Offre.PrixAchat < Produit.PrixRevient THEN 'Révoquer'
    ELSE 'Conserver'
  END AS StatutVente
FROM Offre
JOIN Vente ON Offre.IdVente = Vente.IdVente
JOIN Produit ON Vente.IdProduit = Produit.IdProduit
JOIN VenteLibre ON Vente.IdVente = VenteLibre.IdVente
WHERE Vente.Sens = 'decroissant'
AND Vente.Revocabilite = 1
AND Vente.IdVente = :idVente
ORDER BY Offre.DateHeureOffre ASC, Offre.PrixAchat ASC
FETCH FIRST ROW ONLY;


-- Recupérer le gagnant d_une vente descendante à durée fixe et révocable

SELECT Offre.EmailUtilisateur, Offre.PrixAchat, Produit.PrixRevient,
  CASE 
    WHEN Offre.PrixAchat < Produit.PrixRevient THEN 'Révoquer'
    ELSE 'Conserver'
  END AS StatutVente
FROM Offre
JOIN Vente ON Offre.IdVente = Vente.IdVente
JOIN Produit ON Vente.IdProduit = Produit.IdProduit
JOIN VenteLimite ON Vente.IdVente = VenteLimite.IdVente
WHERE Vente.Sens = 'decroissant'
AND Vente.Revocabilite = 1
AND Vente.IdVente = :idVente
AND VenteLimite.DateFin > Offre.DateHeureOffre
ORDER BY Offre.DateHeureOffre ASC
FETCH FIRST ROW ONLY;

