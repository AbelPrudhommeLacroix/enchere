### Offres montantes

# Recupérer la dernière offre d_une vente montante à durée libre et non révocable

SELECT DateOffre.DateHeureOffre, Utilisateur.EmailUtilisateur 
FROM Utilisateur, Offre, Vente, DateOffre, VenteLibre
WHERE Offre.EmailUtilisateur = Utilisateur.EmailUtilisateur
AND Offre.IdVente = Vente.IdVente
AND Offre.DateHeureOffre = DateOffre.DateHeureOffre
AND Vente.IdVente = VenteLibre.IdVente
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
ORDER BY DateOffre.DateHeureOffre ASC
LIMIT 1;




### Faire une offre

# Compter les offres de quelqu_un pour les ventes à offres limitées

SELECT Count(*), Utilisateur.EmailUtilisateur
FROM Utilisateur, Vente
WHERE Utilisateur.EmailUtilisateur = Vente.EmailUtilisateur
AND Utilisateur.AdressePostale = AdressePostale  # à définir en fonction de celui qu_on veut
AND Utilisateur.Nom = Nom                        # à définir en fonction de celui qu_on veut
AND Utilisateur.Prenom = Prenom                  # à définir en fonction de celui qu_on veut
GROUP BY EmailUtilisateur;


## Vérifier que l_offre est bien supérieure à la dernière offre effectuée (si montante)
# Trouver la dernière offre effectuée et le prix d_achat de celle-ci
SELECT DateOffre.DateHeureOffre, Offre.PrixAchat
FROM Offre, Vente, DateOffre
AND Offre.IdVente = Vente.IdVente
AND Offre.DateHeureOffre = DateOffre.DateHeureOffre
ORDER BY DateOffre.DateHeureOffre DESC
LIMIT 1;





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
    v.IdVente AS VenteProposee,
    v.Sens AS SensVenteProposee,
    v.Revocabilite AS RevocabiliteVenteProposee,
    v.NbOffres AS NbOffresVenteProposee,
    v_ex.IdVente AS VenteExistante,
    v_ex.Sens AS SensVenteExistante,
    v_ex.Revocabilite AS RevocabiliteVenteExistante,
    v_ex.NbOffres AS NbOffresVenteExistante
FROM Vente v
LEFT JOIN Vente v_ex ON v_ex.IdSalle = :idSalle
WHERE v.IdVente = :idVente
  AND v_ex.IdSalle = :idSalle
  AND (
        v.Sens != v_ex.Sens OR
        v.Revocabilite != v_ex.Revocabilite OR
        v.NbOffres != v_ex.NbOffres
      );


