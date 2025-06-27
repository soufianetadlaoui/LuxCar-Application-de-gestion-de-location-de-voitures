package projet.ui;

import javax.swing.*;

import projet.dao.VehiculeDAO;
import projet.models.Vehicule;

import java.awt.*;

public class VehiculeModifierForm extends JFrame {
	
	private JTextField txtMarque, txtModele, txtImmat, txtType;
    private JCheckBox chkDisponible;
    private VehiculeDAO voitureDAO;
    private Vehicule voiture;
    private int ligneDansTable;
    private VehiculeListe parent;

    public VehiculeModifierForm(VehiculeListe parent, VehiculeDAO dao, Vehicule voiture, int ligne) {
        super();
        this.parent = parent;
        this.voitureDAO = dao;
        this.voiture = voiture;
        this.ligneDansTable = ligne;

        setSize(400, 300);
        setLocationRelativeTo(parent);

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        txtMarque = new JTextField(voiture.getMarque());
        txtModele = new JTextField(voiture.getModel());
        txtImmat = new JTextField(voiture.getImmatriculation());
        txtType = new JTextField(voiture.getType());
        chkDisponible = new JCheckBox("Disponible", voiture.getDisponible());

        JButton btnEnregistrer = new JButton("Enregistrer");

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
        panel.add(btnEnregistrer);

        add(panel);

        btnEnregistrer.addActionListener(e -> enregistrerModifications());

        setVisible(true);
    }

    private void enregistrerModifications() {
        voiture.setMarque(txtMarque.getText());
        voiture.setModel(txtModele.getText());
        voiture.setImmatriculation(txtImmat.getText());
        voiture.setType(txtType.getText());
        voiture.setDisponible(chkDisponible.isSelected());

        boolean success = voitureDAO.modifierVoiture(voiture);
        if (success) {
            JOptionPane.showMessageDialog(this, "Voiture modifiée !");
            parent.dispose();
            new VehiculeListe(voitureDAO, parent); // recharge la liste
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Erreur lors de la modification.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

}
