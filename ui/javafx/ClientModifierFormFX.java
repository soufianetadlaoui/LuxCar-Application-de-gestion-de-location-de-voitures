package projet.ui.javafx;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import projet.dao.ClientDAO;
import projet.models.Client;

public class ClientModifierFormFX {

    public static void afficher(ClientDAO clientDAO, Client client, Runnable onSuccess) {
        Stage stage = new Stage();
        stage.setTitle("✏️ Modifier un client");

        VBox root = new VBox(15);
        root.setPadding(new Insets(25));
        root.setAlignment(Pos.CENTER_LEFT);
        root.setStyle("-fx-background-color: #1e1e2f;");

        Label title = new Label("Modification d'un client");
        title.setStyle("-fx-font-size: 18px; -fx-text-fill: white; -fx-font-weight: bold;");

        TextField txtNom = new TextField(client.getNom());
        txtNom.getStyleClass().add("input-field");

        TextField txtPrenom = new TextField(client.getPrenom());
        txtPrenom.getStyleClass().add("input-field");

        TextField txtCin = new TextField(client.getCin());
        txtCin.getStyleClass().add("input-field");

        TextField txtTel = new TextField(client.getTelephone());
        txtTel.getStyleClass().add("input-field");

        TextField txtEmail = new TextField(client.getEmail());
        txtEmail.getStyleClass().add("input-field");

        Button btnModifier = new Button("Enregistrer");
        btnModifier.getStyleClass().add("action-button");

        btnModifier.setOnAction(e -> {
            client.setNom(txtNom.getText());
            client.setPrenom(txtPrenom.getText());
            client.setCin(txtCin.getText());
            client.setTelephone(txtTel.getText());
            client.setEmail(txtEmail.getText());

            if (clientDAO.modifierClient(client)) {
                onSuccess.run();
                stage.close();
            } else {
                new Alert(Alert.AlertType.ERROR, "Erreur lors de la modification.").showAndWait();
            }
        });

        root.getChildren().addAll(title, txtNom, txtPrenom, txtCin, txtTel, txtEmail, btnModifier);

        Scene scene = new Scene(root, 400, 450);
        scene.getStylesheets().add(ClientModifierFormFX.class.getResource("client-dark.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
}
