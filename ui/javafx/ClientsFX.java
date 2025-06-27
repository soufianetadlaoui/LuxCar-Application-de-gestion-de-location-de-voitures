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
import projet.dao.ClientDAO;
import projet.models.Client;
import projet.tools.DatabaseManager;
import projet.models.Utilisateur;

import java.util.List;

public class ClientsFX extends Application {

    public static DatabaseManager db;
    public static Utilisateur utilisateur;

    private TableView<Client> table;
    private ObservableList<Client> data;
    private ClientDAO clientDAO;

    @Override
    public void start(Stage stage) {
        clientDAO = new ClientDAO(db);

        table = new TableView<>();
        TableColumn<Client, Number> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getId()));

        TableColumn<Client, String> colNom = new TableColumn<>("Nom");
        colNom.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNom()));

        TableColumn<Client, String> colPrenom = new TableColumn<>("Prénom");
        colPrenom.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getPrenom()));

        TableColumn<Client, String> colCin = new TableColumn<>("CIN");
        colCin.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getCin()));

        TableColumn<Client, String> colTel = new TableColumn<>("Téléphone");
        colTel.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getTelephone()));

        table.getColumns().addAll(colId, colNom, colPrenom, colCin, colTel);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        chargerClients();

        // Boutons
        Button btnAjouter = new Button("➕ Ajouter");
        Button btnModifier = new Button("✏️ Modifier");
        Button btnSupprimer = new Button("🗑️ Supprimer");
        Button btnRetour = new Button("🏠 Retour");

        for (Button btn : List.of(btnAjouter, btnModifier, btnSupprimer, btnRetour)) {
            btn.getStyleClass().add("action-button");
        }
        
        if (!DashboardFX.utilisateur.estAdmin()) {
            btnModifier.setDisable(true);
            btnSupprimer.setDisable(true);
        }

        btnAjouter.setOnAction(e -> {
            ClientFormFX.afficher(clientDAO, this::chargerClients);
        });

        btnModifier.setOnAction(e -> {
            Client selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                ClientModifierFormFX.afficher(clientDAO, selected, this::chargerClients);
            } else {
                showError("Sélectionne un client à modifier.");
            }
        });

        btnSupprimer.setOnAction(e -> supprimerClient());

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

        HBox buttons = new HBox(15, btnAjouter, btnModifier, btnSupprimer, btnRetour);
        buttons.setAlignment(Pos.CENTER);

        Label title = new Label("👤 Gestion des clients");
        title.setId("client-title");

        VBox layout = new VBox(15, title, table, buttons);
        layout.setPadding(new Insets(30));
        layout.setAlignment(Pos.TOP_CENTER);
        layout.getStyleClass().add("client-layout");

        Scene scene = new Scene(layout, 850, 520);
        scene.getStylesheets().add(getClass().getResource("client-dark.css").toExternalForm());

        FadeTransition fade = new FadeTransition(Duration.millis(600), layout);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();

        stage.setTitle("Clients - LuxCar");
        stage.setScene(scene);
        stage.show();
    }

    private void chargerClients() {
        List<Client> clients = clientDAO.getTousLesClients();
        data = FXCollections.observableArrayList(clients);
        table.setItems(data);
    }

    private void supprimerClient() {
        Client selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Sélectionne un client à supprimer.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Supprimer ce client ?", ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(type -> {
            if (type == ButtonType.YES && clientDAO.supprimerClient(selected.getId())) {
                chargerClients();
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
