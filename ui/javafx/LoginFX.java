package projet.ui.javafx;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import projet.dao.UtilisateurDAO;
import projet.models.Utilisateur;
import projet.tools.DatabaseManager;

public class LoginFX extends Application {

    static DatabaseManager db = new DatabaseManager();

    @Override
    public void start(Stage stage) {
        db.connect();
        UtilisateurDAO utilisateurDAO = new UtilisateurDAO(db);

        // 🧱 Conteneur principal
        VBox root = new VBox(15);
        root.setPadding(new Insets(40));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #1e1e2f;");

        Label title = new Label("Connexion");
        title.setId("login-title");

        TextField txtUser = new TextField();
        txtUser.setPromptText("Nom d'utilisateur");
        txtUser.getStyleClass().add("input-field");

        PasswordField txtPass = new PasswordField();
        txtPass.setPromptText("Mot de passe");
        txtPass.getStyleClass().add("input-field");

        Button btnLogin = new Button("Se connecter");
        Button btnCreerCompte = new Button("Créer un compte");
        
        btnLogin.getStyleClass().add("login-button");
        btnCreerCompte.getStyleClass().add("link-button");

        Label message = new Label("");
        message.setTextFill(Color.RED);

        btnLogin.setOnAction(e -> {
            String nom = txtUser.getText();
            String pass = txtPass.getText();
            Utilisateur u = utilisateurDAO.verifierConnexion(nom, pass);
            if (u != null) {
                message.setText("✅ Connexion réussie");
                stage.close();
                DashboardFX.utilisateur = u;
                DashboardFX.db = db;
                
                new DashboardFX().start(new Stage());
            } else {
                message.setText("❌ Identifiants incorrects");
            }
        });
        
        btnCreerCompte.setOnAction(e -> {
            UtilisateurFormFX.afficher(new UtilisateurDAO(db), () -> {
                new Alert(Alert.AlertType.INFORMATION, "Compte créé avec succès !").showAndWait();
            });
        });


        root.getChildren().addAll(title, txtUser, txtPass, btnLogin, btnCreerCompte, message);

        Scene scene = new Scene(root, 500, 400);
        scene.getStylesheets().add(getClass().getResource("login-dark.css").toExternalForm());

        // ✨ Animation d'apparition
        FadeTransition fade = new FadeTransition(Duration.millis(600), root);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();

        stage.setTitle("Connexion - LuxCar");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
