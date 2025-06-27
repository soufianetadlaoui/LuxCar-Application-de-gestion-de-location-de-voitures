package projet.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import projet.dao.ClientDAO;
import projet.models.Client;
import projet.tools.DatabaseManager;

import java.awt.*;
import java.util.List;

public class ClientListe extends JFrame{
	
	private JTable table;
    private DefaultTableModel model;
    private ClientDAO clientDAO;
    private JFrame parent;

    public ClientListe(ClientDAO clientDAO, JFrame parent) {
        this.clientDAO = clientDAO;
        this.parent=parent;
        
        setTitle("Liste des Clients");
        setSize(750, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JButton btnAjouter = new JButton("➕ Ajouter un client");
        JButton btnSupprimer = new JButton("Supprimer le client sélectionné");
        JButton btnModifier = new JButton("Modifier le client sélectionné");
        JButton btnRetour = new JButton("⬅ Retour");
        JPanel panelBoutons = new JPanel();
        panelBoutons.add(btnAjouter);
        panelBoutons.add(btnModifier);
        panelBoutons.add(btnSupprimer);
        panelBoutons.add(btnRetour);
        add(panelBoutons, BorderLayout.SOUTH);

        btnAjouter.addActionListener(e -> new ClientForm(clientDAO, this));
        btnSupprimer.addActionListener(e -> supprimerClientSelectionne());
        btnModifier.addActionListener(e -> modifierClientSelectionne());
        btnRetour.addActionListener(e -> {
            this.dispose();
            parent.setVisible(true);
        });

        chargerClients();
        setVisible(true);
    }

    private void chargerClients() {
        List<Client> clients = clientDAO.getTousLesClients();

        String[] colonnes = {"ID", "Nom", "Prénom", "CIN", "Téléphone", "Email"};
        model = new DefaultTableModel(colonnes, 0);
        table.setModel(model);

        for (Client c : clients) {
            model.addRow(new Object[]{
                c.getId(),
                c.getNom(),
                c.getPrenom(),
                c.getCin(),
                c.getTelephone(),
                c.getEmail()
            });
        }
    }

    private void supprimerClientSelectionne() {
        int ligne = table.getSelectedRow();
        if (ligne == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un client à supprimer.");
            return;
        }

        int id = (int) model.getValueAt(ligne, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Voulez-vous vraiment supprimer ce client ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean ok = clientDAO.supprimerClient(id);
            if (ok) {
                model.removeRow(ligne);
                JOptionPane.showMessageDialog(this, "Client supprimé.");
            } else {
                JOptionPane.showMessageDialog(this, "Erreur de suppression.");
            }
        }
    }

    private void modifierClientSelectionne() {
        int ligne = table.getSelectedRow();
        if (ligne == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un client à modifier.");
            return;
        }

        int id = (int) model.getValueAt(ligne, 0);
        String nom = (String) model.getValueAt(ligne, 1);
        String prenom = (String) model.getValueAt(ligne, 2);
        String cin = (String) model.getValueAt(ligne, 3);
        String tel = (String) model.getValueAt(ligne, 4);
        String email = (String) model.getValueAt(ligne, 5);

        Client client = new Client(id, nom, prenom, cin, tel, email);
        new ClientModifierForm(this, clientDAO, client);
    }
    
    public void rechargerClients() {
        model.setRowCount(0); // vide la table
        List<Client> clients = clientDAO.getTousLesClients();
        for (Client c : clients) {
            model.addRow(new Object[]{
                c.getId(),
                c.getNom(),
                c.getPrenom(),
                c.getCin(),
                c.getTelephone(),
                c.getEmail()
            });
        }
    }


    public static void main(String[] args) {
        DatabaseManager db = new DatabaseManager();
        db.connect();
        ClientDAO dao = new ClientDAO(db);
        new ClientListe(dao,null);
    }

}
