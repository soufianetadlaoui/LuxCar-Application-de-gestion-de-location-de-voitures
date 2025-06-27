package projet.ui.javafx;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import projet.dao.VehiculeDAO;
import projet.models.Vehicule;

public class VehiculeModifierFormFX {

    public static void afficher(VehiculeDAO voitureDAO, Vehicule voiture, Runnable onSuccess) {
        Stage stage = new Stage();
        stage.setTitle("✏️ Modifier une voiture");

        VBox root = new VBox(15);
        root.setPadding(new Insets(25));
        root.setAlignment(Pos.CENTER_LEFT);
        root.setStyle("-fx-background-color: #1e1e2f;");

        Label title = new Label("Modification d'une voiture");
        title.setStyle("-fx-font-size: 18px; -fx-text-fill: white; -fx-font-weight: bold;");

        TextField txtMarque = new TextField(voiture.getMarque());
        txtMarque.getStyleClass().add("input-field");

        TextField txtModele = new TextField(voiture.getModel());
        txtModele.getStyleClass().add("input-field");

        TextField txtImmat = new TextField(voiture.getImmatriculation());
        txtImmat.getStyleClass().add("input-field");

        TextField txtType = new TextField(voiture.getType());
        txtType.getStyleClass().add("input-field");

        CheckBox chkDispo = new CheckBox("Disponible");
        chkDispo.setSelected(voiture.getDisponible());
        chkDispo.setStyle("-fx-text-fill: white;");

        Button btnModifier = new Button("Enregistrer");
        btnModifier.getStyleClass().add("action-button");

        btnModifier.setOnAction(e -> {
            voiture.setMarque(txtMarque.getText());
            voiture.setModel(txtModele.getText());
            voiture.setImmatriculation(txtImmat.getText());
            voiture.setType(txtType.getText());
            voiture.setDisponible(chkDispo.isSelected());

            if (voitureDAO.modifierVoiture(voiture)) {
                onSuccess.run(); // recharge la liste
                stage.close();
            } else {
                new Alert(Alert.AlertType.ERROR, "Erreur lors de la modification.").showAndWait();
            }
        });

        root.getChildren().addAll(title, txtMarque, txtModele, txtImmat, txtType, chkDispo, btnModifier);

        Scene scene = new Scene(root, 400, 400);
        scene.getStylesheets().add(VehiculeModifierFormFX.class.getResource("vehiculeForm-dark.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
}
