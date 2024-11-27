### Offres montantes

# Recupérer la dernière offre d_une vente montante à durée libre et non révocable

SELECT Date.DateHeureOffre, Utilisateur.EmailUtilisateur 
FROM Utilisateur, Offre, Vente, Date, VenteLibre
WHERE Offre.EmailUtilisateur = Utilisateur.EmailUtilisateur
AND Offre.IdVente = Vente.IdVente
AND Offre.DateHeureOffre = Vente.DateHeureOffre
AND Vente.IdVente = VenteLibre.IdVente
ORDER BY Date.DateHeureOffre DESC
LIMIT 1


# Recupérer le gagnant d_une vente montante à durée fixe et non révocable

SELECT Date.DateHeureOffre, Utilisateur.EmailUtilisateur 
FROM Utilisateur, Offre, Vente, Date, VenteLimitee
WHERE Offre.EmailUtilisateur = Utilisateur.EmailUtilisateur
AND Offre.IdVente = Vente.IdVente
AND Offre.DateHeureOffre = Vente.DateHeureOffre
AND Vente.IdVente = VenteLimitee.IdVente
AND Date.DateHeureOffre <= VenteLimitee.Fin
ORDER BY Date.DateHeureOffre DESC
LIMIT 1


# Recupérer la dernière offre d_une vente montante à durée libre et révocable

SELECT Date.DateHeureOffre, Utilisateur.EmailUtilisateur 
FROM Utilisateur, Offre, Vente, Date, VenteLibre, Produit
WHERE Offre.EmailUtilisateur = Utilisateur.EmailUtilisateur
AND Offre.IdVente = Vente.IdVente
AND Offre.DateHeureOffre = Vente.DateHeureOffre
AND Vente.IdVente = VenteLibre.IdVente
AND Vente.IdProduit = Produit.IdProduit
AND Offre.PrixAchat >= Produit.PrixRevient
ORDER BY Date.DateHeureOffre DESC
LIMIT 1


# Recupérer le gagnant d_une vente montante à durée fixe et révocable

SELECT Date.DateHeureOffre, Utilisateur.EmailUtilisateur 
FROM Utilisateur, Offre, Vente, Date, VenteLimitee, Produit
WHERE Offre.EmailUtilisateur = Utilisateur.EmailUtilisateur
AND Offre.IdVente = Vente.IdVente
AND Offre.DateHeureOffre = Vente.DateHeureOffre
AND Vente.IdVente = VenteLimitee.IdVente
AND Date.DateHeureOffre <= VenteLimitee.Fin
AND Vente.IdProduit = Produit.IdProduit
AND Offre.PrixAchat >= Produit.PrixRevient
ORDER BY Date.DateHeureOffre DESC
LIMIT 1



### Offres descendantes

# Recupérer la dernière offre d_une vente descendante à durée libre et non révocable

SELECT Date.DateHeureOffre, Utilisateur.EmailUtilisateur 
FROM Utilisateur, Offre, Vente, Date, VenteLibre
WHERE Offre.EmailUtilisateur = Utilisateur.EmailUtilisateur
AND Offre.IdVente = Vente.IdVente
AND Offre.DateHeureOffre = Vente.DateHeureOffre
AND Vente.IdVente = VenteLibre.IdVente
ORDER BY Date.DateHeureOffre ASC
LIMIT 1


# Recupérer le gagnant d_une vente descendante à durée fixe et non révocable

SELECT Date.DateHeureOffre, Utilisateur.EmailUtilisateur 
FROM Utilisateur, Offre, Vente, Date, VenteLimitee
WHERE Offre.EmailUtilisateur = Utilisateur.EmailUtilisateur
AND Offre.IdVente = Vente.IdVente
AND Offre.DateHeureOffre = Vente.DateHeureOffre
AND Vente.IdVente = VenteLimitee.IdVente
AND Date.DateHeureOffre <= VenteLimitee.Fin
ORDER BY Date.DateHeureOffre ASC
LIMIT 1


# Recupérer la dernière offre d_une vente descendante à durée libre et révocable

SELECT Date.DateHeureOffre, Utilisateur.EmailUtilisateur 
FROM Utilisateur, Offre, Vente, Date, VenteLibre, Produit
WHERE Offre.EmailUtilisateur = Utilisateur.EmailUtilisateur
AND Offre.IdVente = Vente.IdVente
AND Offre.DateHeureOffre = Vente.DateHeureOffre
AND Vente.IdVente = VenteLibre.IdVente
AND Vente.IdProduit = Produit.IdProduit
AND Offre.PrixAchat >= Produit.PrixRevient
ORDER BY Date.DateHeureOffre ASC
LIMIT 1


# Recupérer le gagnant d_une vente descendante à durée fixe et révocable

SELECT Date.DateHeureOffre, Utilisateur.EmailUtilisateur 
FROM Utilisateur, Offre, Vente, Date, VenteLimitee, Produit
WHERE Offre.EmailUtilisateur = Utilisateur.EmailUtilisateur
AND Offre.IdVente = Vente.IdVente
AND Offre.DateHeureOffre = Vente.DateHeureOffre
AND Vente.IdVente = VenteLimitee.IdVente
AND Date.DateHeureOffre <= VenteLimitee.Fin
AND Vente.IdProduit = Produit.IdProduit
AND Offre.PrixAchat >= Produit.PrixRevient
ORDER BY Date.DateHeureOffre ASC
LIMIT 1



### Faire une offre

# Compter les offres de quelqu_un pour les ventes à offres limitées

SELECT Count(*), Utilisateur.EmailUtilisateur
FROM Utilisateur, Vente
WHERE Utilisateur.EmailUtilisateur = Vente.EmailUtilisateur
AND Utilisateur.AdressePostale = AdressePostale  # à définir en fonction de celui qu_on veut
AND Utilisateur.Nom = Nom
AND Utilisateur.Prenom = Prenom
GROUP BY EmailUtilisateur




### Mise en place d_une salle de vente

# Récupérer la catégorie de la salle de Vente

SELECT *
FROM Catégorie, SalleDeVente
WHERE Catégorie.NomCatégorie = SalleDeVente.NomCatégorie


# Récupérer la catégorie d_un Produit

SELECT *
FROM Catégorie, Produit
WHERE Catégorie.NomCatégorie = Produit.NomCatégorie

# Récupérer 