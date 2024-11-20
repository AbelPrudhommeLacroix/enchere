auction-platform/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── com/
│   │   │   │   ├── auction/
│   │   │   │   │   ├── App.java                     # Classe principale pour lancer l'application
│   │   │   │   │   ├── controllers/
│   │   │   │   │   │   ├── LoginController.java     # Gestion de la logique de connexion
│   │   │   │   │   │   ├── RegistrationController.java  # Gestion de la logique d'inscription
│   │   │   │   │   │   └── DashboardController.java # Logique après connexion réussie (optionnel)
│   │   │   │   │   ├── models/
│   │   │   │   │   │   ├── User.java                # Représentation d'un utilisateur
│   │   │   │   │   │   └── AuctionItem.java         # (Optionnel) Modèle pour un objet d'enchère
│   │   │   │   │   ├── services/
│   │   │   │   │   │   ├── UserService.java         # Méthodes pour gérer les utilisateurs
│   │   │   │   │   │   └── DatabaseConnection.java  # Connexion à la BDD
│   │   │   │   │   └── utils/
│   │   │   │   │       └── Validator.java           # Validation des champs (email, username, etc.)
│   │   │   └── application.properties               # Configuration (ex. URL de la BDD)
│   │   ├── resources/
│   │   │   ├── fxml/
│   │   │   │   ├── login.fxml                       # Interface utilisateur pour la connexion
│   │   │   │   ├── registration.fxml                # Interface utilisateur pour l'inscription
│   │   │   │   └── dashboard.fxml                   # Interface utilisateur après connexion (optionnel)
│   │   │   └── styles/
│   │   │       └── app.css                          # Feuilles de style pour l'interface utilisateur
│   │   └── lib/
│   │       ├── mysql-connector-java-x.x.x.jar       # Driver JDBC MySQL
├── README.md                                        # Documentation du projet


controllers/ : Logique spécifique aux interfaces utilisateur.

models/ :Contient les classes qui représentent les entités du projet.

services/ : Contient la logique d'accès aux données et d'intégration.

utils/ : Contient des classes utilitaires pour les fonctionnalités communes.
