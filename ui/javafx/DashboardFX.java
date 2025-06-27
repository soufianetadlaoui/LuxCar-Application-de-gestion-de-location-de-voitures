package projet.ui.javafx;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import projet.models.Utilisateur;
import projet.tools.DatabaseManager;

public class DashboardFX extends Application {

    public static Utilisateur utilisateur;
    public static DatabaseManager db;

    @Override
    public void start(Stage stage) {
        // 🌄 Image de fond
        BackgroundImage bgImage = new BackgroundImage(
                new Image(getClass().getResource("images/BG.png").toExternalForm(),
                        1000, 600, false, true),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT
        );

        StackPane root = new StackPane();
        root.setBackground(new Background(bgImage));

        // 🧭 Menu latéral transparent avec glass effect
        VBox sidebar = new VBox(20);
        sidebar.setPadding(new Insets(40, 20, 40, 20));
        sidebar.setPrefWidth(250);
        sidebar.setAlignment(Pos.TOP_CENTER);
        sidebar.setStyle("-fx-background-color: rgba(255, 255, 255, 0.1); -fx-background-radius: 20; -fx-border-radius: 20; -fx-border-color: rgba(255,255,255,0.3); -fx-border-width: 1;");
        sidebar.setEffect(new DropShadow(20, Color.rgb(0, 0, 0, 0.2)));

        Label title = new Label("🚗 LuxCar");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setTextFill(Color.web("#ffffff"));


        Button btnClients = new Button("👤 Clients");
        Button btnVoitures = new Button("🚘 Voitures");
        Button btnContrats = new Button("📄 Contrats");
        Button btnUtilisateurs = new Button("👥 Utilisateurs");
        Button btnLogout = new Button("🔓 Déconnexion");

        for (Button btn : new Button[]{btnClients, btnVoitures, btnContrats, btnUtilisateurs, btnLogout}) {
            btn.setMaxWidth(Double.MAX_VALUE);
            btn.getStyleClass().add("nav-button-fancy");
        }

        if (!utilisateur.estAdmin()) {
            //btnVoitures.setDisable(true);
            btnContrats.setDisable(true);
            btnUtilisateurs.setDisable(true);
        }

        sidebar.getChildren().addAll(title, btnClients, btnVoitures, btnContrats, btnUtilisateurs, btnLogout);

        // 🎬 Zone centrale d'accueil
        VBox content = new VBox(15);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(20));
        Label welcome = new Label("Bienvenue, " + utilisateur.getNomUtilisateur());
        welcome.setStyle("-fx-font-size: 36px; -fx-text-fill: #b3e5ff; -fx-font-weight: bold;");

        if (!utilisateur.estAdmin()) {
            Label limit = new Label("Accès restreint : mode utilisateur");
            limit.setStyle("-fx-text-fill: orange; -fx-font-size: 14px;");
            content.getChildren().addAll(welcome, limit);
        } else {
            content.getChildren().add(welcome);
        }

        HBox layout = new HBox(30, sidebar, content);
        layout.setPadding(new Insets(30));
        layout.setAlignment(Pos.CENTER_LEFT);

        // 🪄 Animation
        FadeTransition fade = new FadeTransition(Duration.millis(800), layout);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();

        TranslateTransition slide = new TranslateTransition(Duration.millis(700), sidebar);
        slide.setFromX(-200);
        slide.setToX(0);
        slide.play();

        Scene scene = new Scene(root, 1000, 600);
        root.getChildren().add(layout);
        scene.getStylesheets().add(getClass().getResource("dashboard-dark.css").toExternalForm());

        // Actions
        btnClients.setOnAction(e -> {
            stage.close();
            ClientsFX.db = db;
            new ClientsFX().start(new Stage());
        });

        btnVoitures.setOnAction(e -> {
            stage.close();
            VehiculeFX.db = db;
            new VehiculeFX().start(new Stage());
        });

        btnContrats.setOnAction(e -> {
            stage.close();
            ContratFX.db = db;
            new ContratFX().start(new Stage());
        });

        btnUtilisateurs.setOnAction(e -> {
            stage.close();
            UtilisateurFX.db = db;
            new UtilisateurFX().start(new Stage());
        });

        btnLogout.setOnAction(e -> {
            stage.close();
            LoginFX.db = db;
            new LoginFX().start(new Stage());
        });

        stage.setTitle("Dashboard - LuxCar");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        db = new DatabaseManager();
        db.connect();
        utilisateur = new Utilisateur(1, "admin", "admin123", "ADMIN");
        launch(args);
    }
}
