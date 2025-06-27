package projet.ui;

import javax.swing.*;

import projet.dao.ClientDAO;
import projet.dao.ContratDAO;
import projet.dao.VehiculeDAO;
import projet.models.Client;
import projet.models.Contrat;
import projet.models.Vehicule;
import projet.tools.DatabaseManager;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ContratForm extends JFrame{
	
	 private JComboBox<Client> cbClients;
	    private JComboBox<Vehicule> cbVoitures;
	    private JTextField txtDateDebut, txtDateFin, txtPrixParJour;
	    private JTextArea txtConditions;
	    private ContratDAO contratDAO;
	    private ClientDAO clientDAO;
	    private VehiculeDAO voitureDAO;
	    private ContratListe parent;

	    public ContratForm(ContratDAO contratDAO, ClientDAO clientDAO, VehiculeDAO voitureDAO, ContratListe parent) {
	        this.contratDAO = contratDAO;
	        this.clientDAO = clientDAO;
	        this.voitureDAO = voitureDAO;
	        this.parent=parent;

	        setTitle("Créer un Contrat de Location");
	        setSize(500, 400);
	        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        setLocationRelativeTo(null);

	        JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10));
	        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

	        cbClients = new JComboBox<>();
	        cbVoitures = new JComboBox<>();
	        txtDateDebut = new JTextField("2025-04-11");
	        txtDateFin = new JTextField("2025-04-13");
	        txtPrixParJour = new JTextField("300");
	        txtConditions = new JTextArea(3, 20);

	        JButton btnCreer = new JButton("Créer le Contrat");

	        panel.add(new JLabel("Client :"));
	        panel.add(cbClients);
	        panel.add(new JLabel("Voiture disponible :"));
	        panel.add(cbVoitures);
	        panel.add(new JLabel("Date début (yyyy-MM-dd) :"));
	        panel.add(txtDateDebut);
	        panel.add(new JLabel("Date fin (yyyy-MM-dd) :"));
	        panel.add(txtDateFin);
	        panel.add(new JLabel("Prix par jour :"));
	        panel.add(txtPrixParJour);
	        panel.add(new JLabel("Conditions :"));
	        panel.add(new JScrollPane(txtConditions));
	        panel.add(new JLabel(""));
	        panel.add(btnCreer);

	        add(panel);

	        chargerClientsEtVoitures();

	        btnCreer.addActionListener(e -> creerContrat());

	        setVisible(true);
	    }

	    private void chargerClientsEtVoitures() {
	        List<Client> clients = clientDAO.getTousLesClients();
	        List<Vehicule> voitures = voitureDAO.getToutesLesVoitures();

	        for (Client c : clients) cbClients.addItem(c);
	        for (Vehicule v : voitures)
	            if (v.getDisponible()) cbVoitures.addItem(v);
	    }

	    private void creerContrat() {
	        try {
	            Client client = (Client) cbClients.getSelectedItem();
	            Vehicule voiture = (Vehicule) cbVoitures.getSelectedItem();

	            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	            Date dateDebut = sdf.parse(txtDateDebut.getText());
	            Date dateFin = sdf.parse(txtDateFin.getText());
	            double prixParJour = Double.parseDouble(txtPrixParJour.getText());
	            String conditions = txtConditions.getText();

	            Contrat contrat = new Contrat(0, voiture, client, dateDebut, dateFin, prixParJour, conditions);
	            boolean ok = contratDAO.ajouterContrat(contrat);

	            if (ok) {
	                JOptionPane.showMessageDialog(this, "Contrat créé avec succès !");
	                parent.rechargerContrats(); // recharge la liste
	                dispose();
	            } else {
	                JOptionPane.showMessageDialog(this, "Erreur lors de la création du contrat.");
	            }

	        } catch (Exception ex) {
	            JOptionPane.showMessageDialog(this, "Erreur de saisie : " + ex.getMessage());
	        }
	    }

	    // Pour tester
	    public static void main(String[] args) {
	        DatabaseManager db = new DatabaseManager();
	        db.connect();
	        ClientDAO clientDAO = new ClientDAO(db);
	        VehiculeDAO voitureDAO = new VehiculeDAO(db);
	        ContratDAO contratDAO = new ContratDAO(db);
	        new ContratForm(contratDAO, clientDAO, voitureDAO, null);

	    }

}
