package projet.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import projet.dao.VehiculeDAO;
import projet.models.Vehicule;
import projet.tools.DatabaseManager;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class VehiculeListe  extends JFrame{
	
	 	private VehiculeDAO voitureDAO;
	    private JTable table;
	    private DefaultTableModel model;
	    private JFrame parent;

	    public VehiculeListe(VehiculeDAO voitureDAO, JFrame parent) {
	        this.voitureDAO = voitureDAO;
	        this.parent=parent;

	        setTitle("Liste des Voitures");
	        setSize(700, 450);
	        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        setLocationRelativeTo(null);

	        // Tableau
	        table = new JTable();
	        JScrollPane scrollPane = new JScrollPane(table);
	        add(scrollPane, BorderLayout.CENTER);

	        // Boutons
	        JPanel buttonPanel = new JPanel();
	        JButton btnAjouter = new JButton("➕ Ajouter une voiture");
	        JButton btnModifier = new JButton("Modifier la sélection");
	        JButton btnSupprimer = new JButton("Supprimer la sélection");
	        buttonPanel.add(btnAjouter);
	        buttonPanel.add(btnModifier);
	        buttonPanel.add(btnSupprimer);
	        add(buttonPanel, BorderLayout.SOUTH);

	        // Actions des boutons
	        btnAjouter.addActionListener(e -> new VehiculeForm(voitureDAO, this));
	        btnSupprimer.addActionListener(e -> supprimerVoitureSelectionnee());
	        btnModifier.addActionListener(e -> modifierVoitureSelectionnee());
	        
	        JButton btnRetour = new JButton("⬅ Retour");
	        btnRetour.addActionListener(e -> {
	            this.dispose();
	            parent.setVisible(true);
	        });
	        buttonPanel.add(btnRetour);

	        // Chargement des données
	        chargerVoitures();

	        setVisible(true);
	    }

	    private void chargerVoitures() {
	        List<Vehicule> voitures = voitureDAO.getToutesLesVoitures();

	        String[] colonnes = {"ID", "Marque", "Modèle", "Immatriculation", "Type", "Disponible"};
	        model = new DefaultTableModel(colonnes, 0);
	        table.setModel(model);

	        for (Vehicule v : voitures) {
	            Object[] ligne = {
	                v.getId(),
	                v.getMarque(),
	                v.getModel(),
	                v.getImmatriculation(),
	                v.getType(),
	                v.getDisponible() ? "Oui" : "Non"
	            };
	            model.addRow(ligne);
	        }
	    }

	    private void supprimerVoitureSelectionnee() {
	        int ligne = table.getSelectedRow();
	        if (ligne == -1) {
	            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une voiture à supprimer.");
	            return;
	        }

	        int id = (int) model.getValueAt(ligne, 0);

	        int confirmation = JOptionPane.showConfirmDialog(this,
	                "Supprimer cette voiture ?", "Confirmation", JOptionPane.YES_NO_OPTION);

	        if (confirmation == JOptionPane.YES_OPTION) {
	            boolean success = voitureDAO.supprimerVoiture(id);
	            if (success) {
	                JOptionPane.showMessageDialog(this, "Voiture supprimée.");
	                model.removeRow(ligne);
	            } else {
	                JOptionPane.showMessageDialog(this, "Erreur lors de la suppression.", "Erreur", JOptionPane.ERROR_MESSAGE);
	            }
	        }
	    }

	    private void modifierVoitureSelectionnee() {
	        int ligne = table.getSelectedRow();
	        if (ligne == -1) {
	            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une voiture à modifier.");
	            return;
	        }

	        int id = (int) model.getValueAt(ligne, 0);
	        String marque = (String) model.getValueAt(ligne, 1);
	        String modele = (String) model.getValueAt(ligne, 2);
	        String immat = (String) model.getValueAt(ligne, 3);
	        String type = (String) model.getValueAt(ligne, 4);
	        boolean dispo = model.getValueAt(ligne, 5).equals("Oui");

	        Vehicule voiture = new Vehicule(id, marque, modele, immat, type, dispo);
	        new VehiculeModifierForm(this, voitureDAO, voiture, ligne);
	    }
	    
	    public void rechargerVoitures() {
	        model.setRowCount(0); // vider le tableau

	        List<Vehicule> voitures = voitureDAO.getToutesLesVoitures();
	        for (Vehicule v : voitures) {
	            model.addRow(new Object[]{
	                v.getId(),
	                v.getMarque(),
	                v.getModel(),
	                v.getImmatriculation(),
	                v.getType(),
	                v.getDisponible() ? "Oui" : "Non"
	            });
	        }
	    }


	    public static void main(String[] args) {
	        DatabaseManager db = new DatabaseManager();
	        db.connect();
	        VehiculeDAO dao = new VehiculeDAO(db);
	        new VehiculeListe(dao, null);
	    }

}
