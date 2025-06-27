package projet.ui.javafx;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import projet.dao.ClientDAO;
import projet.models.Client;

public class ClientFormFX {

    public static void afficher(ClientDAO clientDAO, Runnable onSuccess) {
        Stage stage = new Stage();
        stage.setTitle("➕ Ajouter un client");

        VBox root = new VBox(15);
        root.setPadding(new Insets(25));
        root.setAlignment(Pos.CENTER_LEFT);
        root.setStyle("-fx-background-color: #1e1e2f;");

        Label title = new Label("Ajout d'un client");
        title.setStyle("-fx-font-size: 18px; -fx-text-fill: white; -fx-font-weight: bold;");

        TextField txtNom = new TextField();
        txtNom.setPromptText("Nom");
        txtNom.getStyleClass().add("input-field");

        TextField txtPrenom = new TextField();
        txtPrenom.setPromptText("Prénom");
        txtPrenom.getStyleClass().add("input-field");

        TextField txtCin = new TextField();
        txtCin.setPromptText("CIN");
        txtCin.getStyleClass().add("input-field");

        TextField txtTel = new TextField();
        txtTel.setPromptText("Téléphone");
        txtTel.getStyleClass().add("input-field");

        TextField txtEmail = new TextField();
        txtEmail.setPromptText("Email");
        txtEmail.getStyleClass().add("input-field");

        Button btnAjouter = new Button("Ajouter");
        btnAjouter.getStyleClass().add("action-button");

        btnAjouter.setOnAction(e -> {
            String nom = txtNom.getText();
            String prenom = txtPrenom.getText();
            String cin = txtCin.getText();
            String tel = txtTel.getText();
            String email = txtEmail.getText();

            Client client = new Client(0, nom, prenom, cin, tel, email);
            if (clientDAO.ajouterClient(client)) {
                onSuccess.run(); // recharge la liste
                stage.close();
            } else {
                new Alert(Alert.AlertType.ERROR, "Erreur lors de l'ajout.").showAndWait();
            }
        });

        root.getChildren().addAll(title, txtNom, txtPrenom, txtCin, txtTel, txtEmail, btnAjouter);

        Scene scene = new Scene(root, 400, 450);
        scene.getStylesheets().add(ClientFormFX.class.getResource("client-dark.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
}
