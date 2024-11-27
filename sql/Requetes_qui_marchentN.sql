-- Faire une offre


-- Si la vente est montante
-- Vérifier que l_offre est bien supérieure à la dernière offre effectuée
-- Renvoie la dernière offre effectuée et le prix d_achat de celle-ci
SELECT Offre.DateHeureOffre, Offre.PrixAchat
FROM Offre
JOIN Vente ON Offre.IdVente = Vente.IdVente
JOIN DateOffre ON Offre.DateHeureOffre = DateOffre.DateHeureOffre
ORDER BY DateOffre.DateHeureOffre DESC
LIMIT 1;


-- Si le nombre d_offres est limité
-- Compter les offres de quelqu_un pour les ventes à offres limitées 
--(si ça concerne une vente avec une offre max et que l_utilisateur a déjà fait une offre on accepte pas sa nouvelle offre)

SELECT COUNT(*) AS NombreOffres
FROM Vente
JOIN Offre ON Offre.IdVente = Vente.IdVente
WHERE Offre.EmailUtilisateur = :emailUtilisateur
AND Offre.IdVente = :idVente;


-- Si la durée est limitée
-- Vérifier que l_offre a été effectuée avant la date de fin de la vente

SELECT COUNT(*) AS OffreValide
FROM Offre
JOIN Vente ON Offre.IdVente = Vente.IdVente
JOIN VenteLimite ON Vente.IdVente = VenteLimite.IdVente
JOIN DateOffre ON DateOffre.DateHeureOffre = Offre.DateHeureOffre
WHERE Offre.IdVente = :idVente
AND DateOffre.DateHeureOffre = :dateOffre
AND Offre.DateHeureOffre <= VenteLimite.DateFin;





