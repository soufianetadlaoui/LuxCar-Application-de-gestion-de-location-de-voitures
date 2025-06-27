package projet.ui.javafx;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import projet.dao.UtilisateurDAO;
import projet.models.Utilisateur;

public class UtilisateurFormFX {

    public static void afficher(UtilisateurDAO utilisateurDAO, Runnable onSuccess) {
        Stage stage = new Stage();
        stage.setTitle("➕ Ajouter un utilisateur");

        VBox root = new VBox(15);
        root.setPadding(new Insets(25));
        root.setAlignment(Pos.CENTER_LEFT);
        root.setStyle("-fx-background-color: #1e1e2f;");

        Label title = new Label("Ajout d’un utilisateur");
        title.setStyle("-fx-font-size: 18px; -fx-text-fill: white; -fx-font-weight: bold;");

        TextField txtNom = new TextField();
        txtNom.setPromptText("Nom d'utilisateur");
        txtNom.getStyleClass().add("input-field");

        PasswordField txtMotDePasse = new PasswordField();
        txtMotDePasse.setPromptText("Mot de passe");
        txtMotDePasse.getStyleClass().add("input-field");

        ComboBox<String> cbRole = new ComboBox<>();
        cbRole.getItems().addAll("ADMIN", "USER");
        cbRole.setPromptText("Rôle");
        cbRole.getStyleClass().add("input-field");

        Button btnAjouter = new Button("Ajouter");
        btnAjouter.getStyleClass().add("action-button");

        btnAjouter.setOnAction(e -> {
            String nom = txtNom.getText();
            String pass = txtMotDePasse.getText();
            String role = cbRole.getValue();

            if (nom.isEmpty() || pass.isEmpty() || role == null) {
                new Alert(Alert.AlertType.WARNING, "Tous les champs sont obligatoires.").showAndWait();
                return;
            }

            Utilisateur user = new Utilisateur(0, nom, pass, role);
            if (utilisateurDAO.ajouterUtilisateur(user)) {
                onSuccess.run();
                stage.close();
            } else {
                new Alert(Alert.AlertType.ERROR, "Erreur lors de l'ajout.").showAndWait();
            }
        });

        root.getChildren().addAll(title, txtNom, txtMotDePasse, cbRole, btnAjouter);

        Scene scene = new Scene(root, 400, 350);
        scene.getStylesheets().add(UtilisateurFormFX.class.getResource("utilisateur-dark.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
}
