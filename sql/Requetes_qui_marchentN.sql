-- Faire une offre

-- Vérifier que l_offre est bien supérieure à la dernière offre effectuée (si montante)
-- Renvoie la dernière offre effectuée et le prix d_achat de celle-ci
SELECT Offre.DateHeureOffre, Offre.PrixAchat
FROM Offre
JOIN Vente ON Offre.IdVente = Vente.IdVente
JOIN DateOffre ON Offre.DateHeureOffre = DateOffre.DateHeureOffre
ORDER BY DateOffre.DateHeureOffre DESC
LIMIT 1;