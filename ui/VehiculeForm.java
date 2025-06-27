package projet.ui;

import javax.swing.*;

import projet.dao.VehiculeDAO;
import projet.models.Vehicule;
import projet.tools.DatabaseManager;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VehiculeForm extends JFrame{
	private JTextField txtMarque, txtModele, txtImmat, txtType;
    private JCheckBox chkDisponible;
    private VehiculeDAO voitureDAO;
    private VehiculeListe parent;


    public VehiculeForm(VehiculeDAO voitureDAO, VehiculeListe parent) {
        this.voitureDAO = voitureDAO;
        this.parent=parent;

        setTitle("Ajouter une Voiture");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel principal
        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Champs de saisie
        txtMarque = new JTextField();
        txtModele = new JTextField();
        txtImmat = new JTextField();
        txtType = new JTextField();
        chkDisponible = new JCheckBox("Disponible");

        // Bouton Ajouter
        JButton btnAjouter = new JButton("Ajouter");

        // Ajout des composants au panel
        panel.add(new JLabel("Marque :"));
        panel.add(txtMarque);
        panel.add(new JLabel("Modèle :"));
        panel.add(txtModele);
        panel.add(new JLabel("Immatriculation :"));
        panel.add(txtImmat);
        panel.add(new JLabel("Type :"));
        panel.add(txtType);
        panel.add(new JLabel("Disponible :"));
        panel.add(chkDisponible);
        panel.add(new JLabel(""));
        panel.add(btnAjouter);

        add(panel);

        // Action bouton
        btnAjouter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ajouterVoiture();
            }
        });

        setVisible(true);
    }

    private void ajouterVoiture() {
        String marque = txtMarque.getText();
        String modele = txtModele.getText();
        String immat = txtImmat.getText();
        String type = txtType.getText();
        boolean disponible = chkDisponible.isSelected();

        Vehicule voiture = new Vehicule(0, marque, modele, immat, type, disponible);
        boolean success = voitureDAO.ajouterVoiture(voiture);

        if (success) {
            JOptionPane.showMessageDialog(this, "Voiture ajoutée avec succès !");
            txtMarque.setText("");
            txtModele.setText("");
            txtImmat.setText("");
            txtType.setText("");
            chkDisponible.setSelected(false);
            parent.rechargerVoitures();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout de la voiture.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Méthode main pour tester
    public static void main(String[] args) {
        DatabaseManager db = new DatabaseManager();
        db.connect();
        VehiculeDAO v = new VehiculeDAO(db);
        new VehiculeForm(v, null);
    }
	

}
