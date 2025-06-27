package projet.ui.javafx;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import projet.dao.VehiculeDAO;
import projet.models.Vehicule;

public class VehiculeFormFX {

    public static void afficher(VehiculeDAO voitureDAO, Runnable onSuccess) {
        Stage stage = new Stage();
        stage.setTitle("➕ Ajouter une Voiture");

        VBox root = new VBox(15);
        root.setPadding(new Insets(25));
        root.setAlignment(Pos.CENTER_LEFT);
        root.setStyle("-fx-background-color: #1e1e2f;");

        Label title = new Label("Ajout d'une voiture");
        title.setStyle("-fx-font-size: 18px; -fx-text-fill: white; -fx-font-weight: bold;");

        TextField txtMarque = new TextField();
        txtMarque.setPromptText("Marque");
        txtMarque.getStyleClass().add("input-field");

        TextField txtModele = new TextField();
        txtModele.setPromptText("Modèle");
        txtModele.getStyleClass().add("input-field");

        TextField txtImmat = new TextField();
        txtImmat.setPromptText("Immatriculation");
        txtImmat.getStyleClass().add("input-field");

        TextField txtType = new TextField();
        txtType.setPromptText("Type");
        txtType.getStyleClass().add("input-field");

        CheckBox chkDispo = new CheckBox("Disponible");
        chkDispo.setStyle("-fx-text-fill: white;");

        Button btnAjouter = new Button("Ajouter");
        btnAjouter.getStyleClass().add("action-button");

        btnAjouter.setOnAction(e -> {
            String marque = txtMarque.getText();
            String modele = txtModele.getText();
            String immat = txtImmat.getText();
            String type = txtType.getText();
            boolean dispo = chkDispo.isSelected();

            Vehicule voiture = new Vehicule(0, marque, modele, immat, type, dispo);
            if (voitureDAO.ajouterVoiture(voiture)) {
                onSuccess.run(); // callback pour recharger la liste
                stage.close();
            } else {
                new Alert(Alert.AlertType.ERROR, "Erreur lors de l'ajout.").showAndWait();
            }
        });

        root.getChildren().addAll(
                title, txtMarque, txtModele, txtImmat, txtType, chkDispo, btnAjouter
        );

        Scene scene = new Scene(root, 400, 400);
        scene.getStylesheets().add(VehiculeFormFX.class.getResource("vehiculeForm-dark.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
}
