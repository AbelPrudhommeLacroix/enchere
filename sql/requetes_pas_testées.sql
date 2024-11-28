





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


