package projet.ui;

import javax.swing.*;

import projet.dao.ClientDAO;
import projet.models.Client;

import java.awt.*;

public class ClientModifierForm extends JFrame{
	
	private JTextField txtNom, txtPrenom, txtCin, txtTelephone, txtEmail;
    private ClientDAO clientDAO;
    private Client client;
    private ClientListe parent;

    public ClientModifierForm(ClientListe parent, ClientDAO dao, Client client) {
        super();
        this.parent = parent;
        this.clientDAO = dao;
        this.client = client;

        setSize(400, 300);
        setLocationRelativeTo(parent);

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        txtNom = new JTextField(client.getNom());
        txtPrenom = new JTextField(client.getPrenom());
        txtCin = new JTextField(client.getCin());
        txtTelephone = new JTextField(client.getTelephone());
        txtEmail = new JTextField(client.getEmail());

        JButton btnEnregistrer = new JButton("Enregistrer");

        panel.add(new JLabel("Nom :"));
        panel.add(txtNom);
        panel.add(new JLabel("Prénom :"));
        panel.add(txtPrenom);
        panel.add(new JLabel("CIN :"));
        panel.add(txtCin);
        panel.add(new JLabel("Téléphone :"));
        panel.add(txtTelephone);
        panel.add(new JLabel("Email :"));
        panel.add(txtEmail);
        panel.add(new JLabel(""));
        panel.add(btnEnregistrer);

        add(panel);

        btnEnregistrer.addActionListener(e -> enregistrerModification());

        setVisible(true);
    }

    private void enregistrerModification() {
        client.setNom(txtNom.getText());
        client.setPrenom(txtPrenom.getText());
        client.setCin(txtCin.getText());
        client.setTelephone(txtTelephone.getText());
        client.setEmail(txtEmail.getText());

        boolean success = clientDAO.modifierClient(client);
        if (success) {
            JOptionPane.showMessageDialog(this, "Client modifié avec succès !");
            dispose();
            parent.dispose();
            new ClientListe(clientDAO,parent); // recharger
        } else {
            JOptionPane.showMessageDialog(this, "Erreur lors de la modification.");
        }
    }

}
