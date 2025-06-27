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
import projet.dao.ContratDAO;
import projet.dao.VehiculeDAO;
import projet.models.Contrat;
import projet.models.FactureViewer;
import projet.models.Utilisateur;
import projet.tools.DatabaseManager;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.List;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

public class ContratFX extends Application {

    static DatabaseManager db;
    private ContratDAO contratDAO;
    private ClientDAO clientDAO;
    private VehiculeDAO voitureDAO;
    private TableView<Contrat> table;
    private ObservableList<Contrat> data;

    @Override
    public void start(Stage stage) {
        contratDAO = new ContratDAO(db);
        clientDAO = new ClientDAO(db);
        voitureDAO = new VehiculeDAO(db);

        table = new TableView<>();
        TableColumn<Contrat, Number> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getId()));

        TableColumn<Contrat, String> colClient = new TableColumn<>("Client");
        colClient.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                c.getValue().getClient().getNom() + " " + c.getValue().getClient().getPrenom()));

        TableColumn<Contrat, String> colVoiture = new TableColumn<>("Voiture");
        colVoiture.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                c.getValue().getVehicule().getMarque() + " " + c.getValue().getVehicule().getModel()));

        TableColumn<Contrat, String> colDates = new TableColumn<>("Période");
        colDates.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                new SimpleDateFormat("yyyy-MM-dd").format(c.getValue().getDateDebut()) + " → " +
                new SimpleDateFormat("yyyy-MM-dd").format(c.getValue().getDateFin())
        ));

        TableColumn<Contrat, Number> colMontant = new TableColumn<>("Montant");
        colMontant.setCellValueFactory(c -> new javafx.beans.property.SimpleDoubleProperty(
                c.getValue().calculerMontantTotal()));

        table.getColumns().addAll(colId, colClient, colVoiture, colDates, colMontant);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        chargerDonnees();

        // Boutons
        Button btnAjouter = new Button("➕ Ajouter contrat");
        Button btnFacture = new Button("🧾 Voir facture");
        Button btnPdf = new Button("📥 Exporter en PDF");
        Button btnRetour = new Button("🏠 Retour");

        btnAjouter.getStyleClass().add("action-button");
        btnFacture.getStyleClass().add("action-button");
        btnPdf.getStyleClass().add("action-button");
        btnRetour.getStyleClass().add("action-button");

        btnAjouter.setOnAction(e -> {
            ContratFormFX.afficher(contratDAO, clientDAO, voitureDAO, this::chargerDonnees);
        });


        btnFacture.setOnAction(e -> voirFacture());
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
        
        btnPdf.setOnAction(e -> {
            Contrat contrat = table.getSelectionModel().getSelectedItem();
            if (contrat == null) {
                new Alert(Alert.AlertType.WARNING, "Veuillez sélectionner un contrat.").showAndWait();
                return;
            }
            exporterFacturePDF(contrat);
        });


        HBox actions = new HBox(15, btnAjouter, btnFacture,btnPdf,btnRetour);
        actions.setAlignment(Pos.CENTER);

        Label title = new Label("📄 Contrats de location");
        title.setId("contrat-title");

        VBox layout = new VBox(15, title, table, actions);
        layout.setPadding(new Insets(30));
        layout.setAlignment(Pos.TOP_CENTER);
        layout.getStyleClass().add("contrat-layout");

        Scene scene = new Scene(layout, 900, 550);
        scene.getStylesheets().add(getClass().getResource("contrat-dark.css").toExternalForm());

        FadeTransition fade = new FadeTransition(Duration.millis(600), layout);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();

        stage.setTitle("Contrats - Admin");
        stage.setScene(scene);
        stage.show();
    }

    private void chargerDonnees() {
        List<Contrat> contrats = contratDAO.getTousLesContrats();
        data = FXCollections.observableArrayList(contrats);
        table.setItems(data);
    }

    private void voirFacture() {
        Contrat contrat = table.getSelectionModel().getSelectedItem();
        if (contrat == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Sélectionne un contrat !");
            alert.showAndWait();
            return;
        }

        new FactureViewer(contrat);
    }
    
    private void exporterFacturePDF(Contrat contrat) {
        try {
            Document document = new Document();
            String nomFichier = "Facture_Contrat_" + contrat.getId() + ".pdf";
            PdfWriter.getInstance(document, new FileOutputStream(nomFichier));
            document.open();

            // Polices stylées
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, BaseColor.DARK_GRAY);
            Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.BLACK);
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);

            // Titre centré
            Paragraph title = new Paragraph("FACTURE DE LOCATION", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            // Ligne horizontale
            document.add(new LineSeparator());

            // Infos client
            document.add(new Paragraph("Client :", sectionFont));
            document.add(new Paragraph(contrat.getClient().getNom() + " " + contrat.getClient().getPrenom(), normalFont));
            document.add(Chunk.NEWLINE);

            // Infos voiture
            document.add(new Paragraph("Véhicule :", sectionFont));
            document.add(new Paragraph(contrat.getVehicule().getMarque() + " " + contrat.getVehicule().getModel(), normalFont));
            document.add(Chunk.NEWLINE);

            // Infos contrat
            document.add(new Paragraph("Détails du contrat :", sectionFont));
            document.add(new Paragraph("Date début : " + contrat.getDateDebut(), normalFont));
            document.add(new Paragraph("Date fin : " + contrat.getDateFin(), normalFont));
            document.add(new Paragraph("Prix par jour : " + contrat.getPrixParJour() + " MAD", normalFont));
            document.add(new Paragraph("Montant total : " + contrat.calculerMontantTotal() + " MAD", normalFont));
            document.add(Chunk.NEWLINE);

            // Conditions
            document.add(new Paragraph("Conditions :", sectionFont));
            document.add(new Paragraph(contrat.getConditions(), normalFont));
            document.add(Chunk.NEWLINE);

            // Signature
            Paragraph signature = new Paragraph("Signature du client : __________________________", normalFont);
            signature.setSpacingBefore(30);
            document.add(signature);

            document.close();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("PDF généré");
            alert.setContentText("Facture PDF enregistrée sous : " + nomFichier);
            alert.showAndWait();

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Erreur lors de la génération du PDF : " + e.getMessage());
            alert.showAndWait();
        }
    }



    public static void main(String[] args) {
        db = new DatabaseManager();
        db.connect();
        launch(args);
    }
}
