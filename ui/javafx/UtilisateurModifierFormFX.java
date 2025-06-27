package projet.ui.javafx;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import projet.dao.UtilisateurDAO;
import projet.models.Utilisateur;

public class UtilisateurModifierFormFX {

    public static void afficher(UtilisateurDAO utilisateurDAO, Utilisateur utilisateur, Runnable onSuccess) {
        Stage stage = new Stage();
        stage.setTitle("✏️ Modifier un utilisateur");

        VBox root = new VBox(15);
        root.setPadding(new Insets(25));
        root.setAlignment(Pos.CENTER_LEFT);
        root.setStyle("-fx-background-color: #1e1e2f;");

        Label title = new Label("Modification d’un utilisateur");
        title.setStyle("-fx-font-size: 18px; -fx-text-fill: white; -fx-font-weight: bold;");

        TextField txtNom = new TextField(utilisateur.getNomUtilisateur());
        txtNom.setPromptText("Nom d'utilisateur");
        txtNom.getStyleClass().add("input-field");

        PasswordField txtMotDePasse = new PasswordField();
        txtMotDePasse.setPromptText("Nouveau mot de passe");
        txtMotDePasse.getStyleClass().add("input-field");

        ComboBox<String> cbRole = new ComboBox<>();
        cbRole.getItems().addAll("ADMIN", "USER");
        cbRole.setValue(utilisateur.getRole());
        cbRole.getStyleClass().add("input-field");

        Button btnEnregistrer = new Button("Enregistrer");
        btnEnregistrer.getStyleClass().add("action-button");

        btnEnregistrer.setOnAction(e -> {
            String nom = txtNom.getText();
            String motDePasse = txtMotDePasse.getText();
            String role = cbRole.getValue();

            if (nom.isEmpty() || role == null) {
                new Alert(Alert.AlertType.WARNING, "Le nom et le rôle sont obligatoires.").showAndWait();
                return;
            }

            utilisateur.setNomUtilisateur(nom);
            if (!motDePasse.isEmpty()) {
                utilisateur.setMotDePasse(motDePasse);
            }
            utilisateur.setRole(role);

            if (utilisateurDAO.modifierUtilisateur(utilisateur)) {
                onSuccess.run();
                stage.close();
            } else {
                new Alert(Alert.AlertType.ERROR, "Erreur lors de la mise à jour.").showAndWait();
            }
        });

        root.getChildren().addAll(title, txtNom, txtMotDePasse, cbRole, btnEnregistrer);

        Scene scene = new Scene(root, 400, 350);
        scene.getStylesheets().add(UtilisateurModifierFormFX.class.getResource("utilisateur-dark.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
}
