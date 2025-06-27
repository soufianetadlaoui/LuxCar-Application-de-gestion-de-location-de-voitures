package projet.ui.javafx;

import javafx.application.Application;
import javafx.collections.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import projet.dao.UtilisateurDAO;
import projet.models.Utilisateur;
import projet.tools.DatabaseManager;

public class UtilisateurFX extends Application {

    static DatabaseManager db;
    private UtilisateurDAO utilisateurDAO;
    private TableView<Utilisateur> table;
    private ObservableList<Utilisateur> data;

    @Override
    public void start(Stage stage) {
        utilisateurDAO = new UtilisateurDAO(db);

        table = new TableView<>();
        TableColumn<Utilisateur, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Utilisateur, String> colNom = new TableColumn<>("Nom d'utilisateur");
        colNom.setCellValueFactory(new PropertyValueFactory<>("nomUtilisateur"));

        TableColumn<Utilisateur, String> colRole = new TableColumn<>("Rôle");
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));

        table.getColumns().addAll(colId, colNom, colRole);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        chargerUtilisateurs();

        // Boutons
        Button btnAjouter = new Button("➕ Ajouter");
        Button btnModifier = new Button("✏️ Modifier");
        Button btnSupprimer = new Button("🗑 Supprimer");
        Button btnRetour = new Button("🏠 Retour");

        btnAjouter.getStyleClass().add("action-button");
        btnModifier.getStyleClass().add("action-button");
        btnSupprimer.getStyleClass().add("action-button");
        btnRetour.getStyleClass().add("action-button");

        btnAjouter.setOnAction(e -> {
            UtilisateurFormFX.afficher(utilisateurDAO, this::chargerUtilisateurs);
        });


        btnModifier.setOnAction(e -> {
            Utilisateur selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                UtilisateurModifierFormFX.afficher(utilisateurDAO, selected, this::chargerUtilisateurs);
            } else {
                new Alert(Alert.AlertType.WARNING, "Sélectionne un utilisateur à modifier.").showAndWait();
            }
        });


        btnSupprimer.setOnAction(e -> supprimerUtilisateur());
        
        btnRetour.setOnAction(e -> {
            ((Stage) btnRetour.getScene().getWindow()).close();
            DashboardFX.db = db;
            DashboardFX.utilisateur = DashboardFX.utilisateur; // garde l’utilisateur actuel
            new DashboardFX().start(new Stage());
        });

        HBox actions = new HBox(15, btnAjouter, btnModifier, btnSupprimer, btnRetour);
        actions.setAlignment(Pos.CENTER);

        VBox layout = new VBox(20, new Label("👥 Gestion des utilisateurs"), table, actions);
        layout.setPadding(new Insets(30));
        layout.setStyle("-fx-background-color: #1e1e2f;");
        layout.getStylesheets().add(getClass().getResource("utilisateur-dark.css").toExternalForm());


        Scene scene = new Scene(layout, 600, 400);
        stage.setTitle("Utilisateurs - Admin");
        stage.setScene(scene);
        stage.show();
    }

    private void chargerUtilisateurs() {
        data = FXCollections.observableArrayList(utilisateurDAO.getTousLesUtilisateurs());
        table.setItems(data);
    }

    private void supprimerUtilisateur() {
        Utilisateur selected = table.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Confirmer la suppression ?", ButtonType.YES, ButtonType.NO);
            confirm.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    utilisateurDAO.supprimerUtilisateur(selected.getId());
                    chargerUtilisateurs();
                }
            });
        } else {
            new Alert(Alert.AlertType.WARNING, "Sélectionne un utilisateur.").showAndWait();
        }
    }

    public static void main(String[] args) {
        db = new DatabaseManager();
        db.connect();
        launch(args);
    }
}
