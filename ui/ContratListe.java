package projet.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import projet.dao.ClientDAO;
import projet.dao.ContratDAO;
import projet.dao.VehiculeDAO;
import projet.models.Contrat;
import projet.models.FactureViewer;
import projet.tools.DatabaseManager;

import java.awt.*;
import java.util.List;

public class ContratListe extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private ContratDAO contratDAO;
    private ClientDAO clientDAO;
    private VehiculeDAO voitureDAO;
    private JFrame parent;

    public ContratListe(ContratDAO contratDAO, ClientDAO clientDAO, VehiculeDAO voitureDAO, JFrame parent) {
        this.contratDAO = contratDAO;
        this.clientDAO = clientDAO;
        this.voitureDAO = voitureDAO;
        this.parent = parent;

        setTitle("Liste des Contrats de Location");
        setSize(900, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        chargerContrats();

        // Boutons
        JButton btnAjouter = new JButton("➕ Ajouter un contrat");
        JButton btnFacture = new JButton("🧾 Afficher la facture");
        JButton btnRetour = new JButton("⬅ Retour");

        JPanel panelBoutons = new JPanel();
        panelBoutons.add(btnAjouter);
        panelBoutons.add(btnFacture);
        panelBoutons.add(btnRetour);
        add(panelBoutons, BorderLayout.SOUTH);

        // Actions
        btnAjouter.addActionListener(e -> new ContratForm(contratDAO, clientDAO, voitureDAO, this));
        btnFacture.addActionListener(e -> afficherFacture());
        btnRetour.addActionListener(e -> {
            this.dispose();
            parent.setVisible(true);
        });

        setVisible(true);
    }

    private void chargerContrats() {
        List<Contrat> contrats = contratDAO.getTousLesContrats();

        String[] colonnes = {
            "ID", "Client", "Voiture", "Date début", "Date fin",
            "Prix/Jour", "Montant total", "Conditions"
        };

        model = new DefaultTableModel(colonnes, 0);
        table.setModel(model);

        for (Contrat c : contrats) {
            model.addRow(new Object[]{
                c.getId(),
                c.getClient().getNom() + " " + c.getClient().getPrenom(),
                c.getVehicule().getMarque() + " " + c.getVehicule().getModel(),
                c.getDateDebut(),
                c.getDateFin(),
                c.getPrixParJour(),
                c.calculerMontantTotal(),
                c.getConditions()
            });
        }
    }

    private void afficherFacture() {
        int ligne = table.getSelectedRow();
        if (ligne == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un contrat.");
            return;
        }

        int idContrat = (int) model.getValueAt(ligne, 0);

        Contrat contrat = contratDAO.getTousLesContrats().stream()
                .filter(c -> c.getId() == idContrat)
                .findFirst()
                .orElse(null);

        if (contrat != null) {
            new FactureViewer(contrat);
        } else {
            JOptionPane.showMessageDialog(this, "Contrat introuvable.");
        }
    }

    public void rechargerContrats() {
        model.setRowCount(0);
        List<Contrat> contrats = contratDAO.getTousLesContrats();
        for (Contrat c : contrats) {
            model.addRow(new Object[]{
                c.getId(),
                c.getClient().getNom() + " " + c.getClient().getPrenom(),
                c.getVehicule().getMarque() + " " + c.getVehicule().getModel(),
                c.getDateDebut(),
                c.getDateFin(),
                c.getPrixParJour(),
                c.calculerMontantTotal(),
                c.getConditions()
            });
        }
    }

    public static void main(String[] args) {
        DatabaseManager db = new DatabaseManager();
        db.connect();
        ContratDAO contratDAO = new ContratDAO(db);
        ClientDAO clientDAO = new ClientDAO(db);
        VehiculeDAO voitureDAO = new VehiculeDAO(db);
        new ContratListe(contratDAO, clientDAO, voitureDAO, null);
    }
}
