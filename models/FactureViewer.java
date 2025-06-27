package projet.models;

import javax.swing.*;

import java.awt.*;
import java.text.SimpleDateFormat;

public class FactureViewer extends JFrame{
	
	 public FactureViewer(Contrat contrat) {
	        setTitle("Facture - Contrat #" + contrat.getId());
	        setSize(600, 500);
	        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	        setLocationRelativeTo(null);

	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	        JTextArea area = new JTextArea();
	        area.setEditable(false);
	        area.setFont(new Font("Monospaced", Font.PLAIN, 14));

	        String facture =
	                "***************** FACTURE DE LOCATION *****************\n\n" +
	                "Date : " + sdf.format(new java.util.Date()) + "\n\n" +
	                "Client : " + contrat.getClient().getNom() + " " + contrat.getClient().getPrenom() + "\n" +
	                "CIN    : " + contrat.getClient().getCin() + "\n" +
	                "Téléphone : " + contrat.getClient().getTelephone() + "\n\n" +

	                "Véhicule : " + contrat.getVehicule().getMarque() + " " + contrat.getVehicule().getModel() + "\n" +
	                "Immatriculation : " + contrat.getVehicule().getImmatriculation() + "\n\n" +

	                "Date début : " + sdf.format(contrat.getDateDebut()) + "\n" +
	                "Date fin   : " + sdf.format(contrat.getDateFin()) + "\n" +
	                "Prix par jour : " + contrat.getPrixParJour() + " MAD\n" +
	                "Montant total : " + contrat.calculerMontantTotal() + " MAD\n\n" +

	                "Conditions : \n" + contrat.getConditions() + "\n\n" +
	                "*******************************************************\n" +
	                "Merci pour votre confiance !";

	        area.setText(facture);
	        add(new JScrollPane(area), BorderLayout.CENTER);

	        JButton btnImprimer = new JButton("Imprimer la facture");
	        add(btnImprimer, BorderLayout.SOUTH);

	        btnImprimer.addActionListener(e -> {
	            try {
	                area.print(); // Lancement impression
	            } catch (Exception ex) {
	                JOptionPane.showMessageDialog(this, "Erreur lors de l'impression : " + ex.getMessage());
	            }
	        });

	        setVisible(true);
	    }

	    // Pour tester avec un contrat fictif
	   /* public static void main(String[] args) {
	        // Remplace par un contrat réel récupéré de la base
	        Client c = new Client(1, "Yassine", "Benali", "AB123", "0600000000", "yassine@email.com");
	        Vehicule v = new Vehicule(1, "BMW", "M3", "1234-AB", "Sport", true);
	        Contrat contrat = new Contrat(1, c, v, new java.util.Date(), new java.util.Date(), 500.0, "Pleine assurance");
	        new FactureViewer(contrat);
	    }*/

}
