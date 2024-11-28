---------------- SALLE DE VENTE ----------------

-- REGARDER SI UN PRODUIT (<ID_PRODUIT>) A LA MEME CATEGORIE D'UNE SALLE (<ID_SALLE>)
-- Renvoi PRODUIT | SALLE, si le produit à la meme categorie que la salle sinon renvoi VIDE

SELECT 
    p.IdProduit, 
    p.NomProduit, 
    s.IdSalle
FROM Produit p
JOIN SalleDeVente s ON p.NomCategorie = s.NomCategorie
WHERE p.IdProduit = <ID_PRODUIT>
AND s.IdSalle = <ID_SALLE>;


-- REGARDER SI UNE NOUVELLE VENTE A LE MEME TYPE DE VENTE QUE LES VENTES D'UNE SALLE
-- UNE VENTE (<SENS_DE_LA_VENTE>, <REVOCABILITE_DE_LA_VENTE>, <NOMBRE_OFFRES_DE_LA_VENTE>, <LIMTE_VENTE>) 
-- <LIMTE_VENTE> doit valoir 'VenteLimitee' ou 'VenteLibre'
-- Renvoi les ventes de salle si la nouvelle vente à le bon type sinon VIDE

SELECT 
    v.IdVente,
    v.Sens,
    v.Revocabilite,
    v.NbOffres
FROM Vente v
WHERE v.IdSalle = <ID_DE_LA_SALLE>
  AND v.Sens = <SENS_DE_LA_VENTE>
  AND v.Revocabilite = <REVOCABILITE_DE_LA_VENTE>
  AND v.NbOffres = <NOMBRE_OFFRES_DE_LA_VENTE>
  AND (
      -- Vérifie que toutes les ventes de la salle sont du même type que la nouvelle vente
      (EXISTS (SELECT 1 FROM VenteLimite vl WHERE vl.IdVente = v.IdVente) 
       AND <LIMTE_VENTE> = 'VenteLimitee')
      OR
      (EXISTS (SELECT 1 FROM VenteLibre vl WHERE vl.IdVente = v.IdVente) 
       AND <LIMTE_VENTE> = 'VenteLibre')
  );

