package projet.ui.javafx;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import projet.dao.ClientDAO;
import projet.dao.ContratDAO;
import projet.dao.VehiculeDAO;
import projet.models.Client;
import projet.models.Contrat;
import projet.models.Vehicule;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class ContratFormFX {

    public static void afficher(ContratDAO contratDAO, ClientDAO clientDAO, VehiculeDAO voitureDAO, Runnable onSuccess) {
        Stage stage = new Stage();
        stage.setTitle("➕ Ajouter un contrat");

        VBox root = new VBox(15);
        root.setPadding(new Insets(25));
        root.setAlignment(Pos.CENTER_LEFT);
        root.setStyle("-fx-background-color: #1e1e2f;");

        Label title = new Label("Ajout d’un contrat");
        title.setStyle("-fx-font-size: 18px; -fx-text-fill: white; -fx-font-weight: bold;");

        ComboBox<Client> cbClients = new ComboBox<>();
        cbClients.getItems().addAll(clientDAO.getTousLesClients());
        cbClients.setPromptText("Sélectionner client");
        cbClients.getStyleClass().add("input-field");

        ComboBox<Vehicule> cbVoitures = new ComboBox<>();
        voitureDAO.getToutesLesVoitures().stream()
            .filter(Vehicule::getDisponible)
            .forEach(cbVoitures.getItems()::add);
        cbVoitures.setPromptText("Sélectionner voiture");
        cbVoitures.getStyleClass().add("input-field");


        DatePicker dpDebut = new DatePicker();
        dpDebut.setPromptText("Date de début");

        DatePicker dpFin = new DatePicker();
        dpFin.setPromptText("Date de fin");

        TextField txtPrix = new TextField();
        txtPrix.setPromptText("Prix par jour");
        txtPrix.getStyleClass().add("input-field");

        TextArea txtConditions = new TextArea();
        txtConditions.setPromptText("Conditions");
        txtConditions.setWrapText(true);
        txtConditions.setPrefRowCount(3);
        txtConditions.getStyleClass().add("input-field");

        Button btnAjouter = new Button("Ajouter");
        btnAjouter.getStyleClass().add("action-button");

        btnAjouter.setOnAction(e -> {
            try {
                Client client = cbClients.getValue();
                Vehicule voiture = cbVoitures.getValue();
                LocalDate dateDebut = dpDebut.getValue();
                LocalDate dateFin = dpFin.getValue();

                if (client == null || voiture == null || dateDebut == null || dateFin == null) {
                    new Alert(Alert.AlertType.WARNING, "Tous les champs doivent être remplis, y compris Client et Voiture.").showAndWait();
                    return;
                }

                double prix = Double.parseDouble(txtPrix.getText());
                String conditions = txtConditions.getText();

                Date debut = Date.from(dateDebut.atStartOfDay(ZoneId.systemDefault()).toInstant());
                Date fin = Date.from(dateFin.atStartOfDay(ZoneId.systemDefault()).toInstant());

                Contrat contrat = new Contrat(0, voiture, client, debut, fin, prix, conditions);
                if (contratDAO.ajouterContrat(contrat)) {
                    voiture.setDisponible(false);
                    voitureDAO.modifierVoiture(voiture);

                    onSuccess.run();
                    stage.close();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Erreur lors de l'ajout.").showAndWait();
                }

            } catch (Exception ex) {
                new Alert(Alert.AlertType.ERROR, "Erreur de saisie.").showAndWait();
            }
        });


        root.getChildren().addAll(title, cbClients, cbVoitures, dpDebut, dpFin, txtPrix, txtConditions, btnAjouter);

        Scene scene = new Scene(root, 450, 550);
        scene.getStylesheets().add(ContratFormFX.class.getResource("contrat-dark.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
}
