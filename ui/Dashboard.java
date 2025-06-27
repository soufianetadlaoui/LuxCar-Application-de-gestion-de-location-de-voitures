package projet.ui;

import javax.swing.*;

import projet.dao.ClientDAO;
import projet.dao.ContratDAO;
import projet.dao.VehiculeDAO;
import projet.models.Utilisateur;
import projet.tools.DatabaseManager;

import java.awt.*;

public class Dashboard extends JFrame{
	
	private Utilisateur utilisateur;
    private DatabaseManager db;
    private ClientDAO clientDAO;
    private VehiculeDAO voitureDAO;


    public Dashboard(Utilisateur utilisateur, DatabaseManager db, ClientDAO clientDAO, VehiculeDAO voitureDAO) {
        this.utilisateur = utilisateur;
        this.db = db;
        this.clientDAO=clientDAO;
        this.voitureDAO=voitureDAO;

        setTitle("Tableau de bord - " + utilisateur.getRole());
        setSize(400, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(6, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton btnVoitures = new JButton("Gérer les voitures");
        JButton btnClients = new JButton("Gérer les clients");
        JButton btnContrats = new JButton("Gérer les contrats");
        JButton btnFactures = new JButton("Voir les factures");
        JButton btnLogout = new JButton("Déconnexion");

        panel.add(new JLabel("Bienvenue, " + utilisateur.getNomUtilisateur()));
        panel.add(btnVoitures);
        panel.add(btnClients);
        panel.add(btnContrats);
        panel.add(btnFactures);
        panel.add(btnLogout);

        add(panel);

        // Actions des boutons
        btnVoitures.addActionListener(e -> {
            this.setVisible(false);
            new VehiculeListe(new VehiculeDAO(db), this);
        });
        btnClients.addActionListener(e -> {
            this.setVisible(false);
            new ClientListe(new ClientDAO(db), this);
        });

        btnContrats.addActionListener(e -> {
            this.setVisible(false);
            new ContratListe(new ContratDAO(db), clientDAO, voitureDAO, this);
        });

        btnFactures.addActionListener(e -> JOptionPane.showMessageDialog(this, "Ouvre FactureViewer depuis Contrats."));
        btnLogout.addActionListener(e -> {
            dispose();
            new LoginForm(db, clientDAO, voitureDAO); // retour à la connexion
        });

        // Personnalisation des accès
        if (!utilisateur.estAdmin()) {
            btnVoitures.setEnabled(false);
            btnContrats.setEnabled(false);
        }

        setVisible(true);
    }

}
