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
LIMIT 1;


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






