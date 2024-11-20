# Plateforme d'enchère

## Dépendences & contraintes

<table>
  <tr>
    <th style="border:1px solid">Dépendances Fonctionnelles</th>
    <th style="border:1px solid">Contraintes de Valeur</th>
    <th style="border:1px solid">Contraintes de Multiplicité</th>
    <th style="border:1px solid">Contraintes Contextuelles</th>
  </tr>
  <tr>
    <td style=" text-align: left; border:1px solid">
     EmailUtilisateur → {Nom, Prenom, AdressePostale} <br><br>
     IdProduit → {NomProduit, PrixRevient, Stock , NomCategorie} <br><br>
     NomCategorie → DescriptionCategorie <br><br>
     {IdProduit, NomCaracteristique} → ValeurCaracteristique <br><br>
     IdSalle → {IdCategorie} <br><br>
     IdVente → {IdProduit, IdSalle, PrixDepart, Sens, NbOffres, Revocabilite} <br>
     IdVente → {DateDebut, DateFin}<br><br>
     {EmailUtilisateur, IdVente, DateHeureOffre} → {PrixAchat, QuantiteOffre}
    </td>
    <td style=" text-align: left;   border:1px solid">
     prix_revient > 0 <br><br>
     prix_depart > 0 <br><br>
     prix_offre > 0 <br><br>
     quantite_souhaitee > 0 <br><br>
     date_fin > date_debut_vente
    </td>
    <td style=" text-align: left; border:1px solid">
     Un email peut avoir plusieurs offres ↠ <br><br>
     Un produit peut avoir plusieurs caractéristiques ↠ <br><br>
     Une vente peut avoir plusieurs offres ↠ <br><br>
     Une salle peut contenir plusieurs produits ↠ <br><br>
     Une catégorie peut avoir plusieurs produits ↠
    </td>
    <td style=" text-align: left; border:1px solid">
     Une vente ne concerne qu'un seul produit <br><br>
     Une vente n'a lieu que dans une seule salle <br><br>
     Une vente à durée libre implique un délai maximal de 10 minutes entre deux offres <br><br>
     Ventes par défaut : Montantes, Non révocables, Sans limite de temps, Permettant plusieurs enchères par utilisateur
    </td>
  </tr>
</table>

## Schéma entité-association 

![Schéma entité relation](entite_association.png)