package projet.ui.javafx;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import projet.dao.VehiculeDAO;
import projet.models.Utilisateur;
import projet.models.Vehicule;
import projet.tools.DatabaseManager;

import java.util.List;

public class VehiculeFX extends Application {

    static DatabaseManager db;
    private static VehiculeDAO voitureDAO;
    private TableView<Vehicule> table;
    private ObservableList<Vehicule> data;

    @Override
    public void start(Stage stage) {
        voitureDAO = new VehiculeDAO(db);

        // TABLE
        table = new TableView<>();
        TableColumn<Vehicule, Number> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getId()));

        TableColumn<Vehicule, String> colMarque = new TableColumn<>("Marque");
        colMarque.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getMarque()));

        TableColumn<Vehicule, String> colModel = new TableColumn<>("Modèle");
        colModel.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getModel()));

        TableColumn<Vehicule, String> colMatricule = new TableColumn<>("Matricule");
        colMatricule.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getImmatriculation()));

        TableColumn<Vehicule, Boolean> colDisponible = new TableColumn<>("Disponible");
        colDisponible.setCellValueFactory(c -> new javafx.beans.property.SimpleBooleanProperty(c.getValue().getDisponible()));

        table.getColumns().addAll(colId, colMarque, colModel, colMatricule, colDisponible);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        chargerDonnees();

        // BOUTONS
        Button btnAjouter = new Button("➕ Ajouter");
        Button btnModifier = new Button("✏️ Modifier");
        Button btnSupprimer = new Button("🗑️ Supprimer");
        Button btnRetour = new Button("🏠 Retour");

        btnAjouter.getStyleClass().add("action-button");
        btnModifier.getStyleClass().add("action-button");
        btnSupprimer.getStyleClass().add("action-button");
        btnRetour.getStyleClass().add("action-button");
        
        if (!DashboardFX.utilisateur.estAdmin()) {
            btnAjouter.setDisable(true);
            btnModifier.setDisable(true);
            btnSupprimer.setDisable(true);
        }

        btnAjouter.setOnAction(e -> {
            VehiculeFormFX.afficher(voitureDAO, this::chargerDonnees);
        });

        btnModifier.setOnAction(e -> {
            Vehicule selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                VehiculeModifierFormFX.afficher(voitureDAO, selected, this::chargerDonnees);
            } else {
                showError("Sélectionne une voiture à modifier.");
            }
        });
        btnSupprimer.setOnAction(e -> supprimerVoiture());
        btnRetour.setOnAction(e -> {
            Stage current = (Stage) btnRetour.getScene().getWindow();
            current.close();

            // ✅ Fix : assigner un utilisateur de test ou garder l’ancien
            DashboardFX.db = db;

            try {
                new DashboardFX().start(new Stage());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });


        HBox actions = new HBox(15, btnAjouter, btnModifier, btnSupprimer, btnRetour);
        actions.setAlignment(Pos.CENTER);

        Label title = new Label("🚘 Liste des Voitures");
        title.setId("voiture-title");

        VBox layout = new VBox(15, title, table, actions);
        layout.setPadding(new Insets(30));
        layout.setAlignment(Pos.TOP_CENTER);
        layout.getStyleClass().add("voiture-layout");

        Scene scene = new Scene(layout, 850, 520);
        scene.getStylesheets().add(getClass().getResource("vehicule-dark.css").toExternalForm());

        FadeTransition fade = new FadeTransition(Duration.millis(600), layout);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();

        stage.setTitle("Voitures - Admin");
        stage.setScene(scene);
        stage.show();
    }

    private void chargerDonnees() {
        List<Vehicule> vehicules = voitureDAO.getToutesLesVoitures();
        data = FXCollections.observableArrayList(vehicules);
        table.setItems(data);
    }



    private void supprimerVoiture() {
        Vehicule selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Sélectionne une voiture à supprimer.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Supprimer cette voiture ?", ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(type -> {
            if (type == ButtonType.YES && voitureDAO.supprimerVoiture(selected.getId())) {
                chargerDonnees();
            }
        });
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        db = new DatabaseManager();
        db.connect();
        launch(args);
    }
}
