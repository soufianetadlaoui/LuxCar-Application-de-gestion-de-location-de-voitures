package projet.ui;

import javax.swing.*;

import projet.dao.ClientDAO;
import projet.models.Client;
import projet.tools.DatabaseManager;

import java.awt.*;

public class ClientForm extends JFrame{
	
	private JTextField txtNom, txtPrenom, txtCin, txtTelephone, txtEmail;
    private ClientDAO clientDAO;
    private ClientListe parent;

    public ClientForm(ClientDAO clientDAO, ClientListe parent) {
        this.clientDAO = clientDAO;
        this.parent=parent;

        setTitle("Ajouter un Client");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        txtNom = new JTextField();
        txtPrenom = new JTextField();
        txtCin = new JTextField();
        txtTelephone = new JTextField();
        txtEmail = new JTextField();
        JButton btnAjouter = new JButton("Ajouter");

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
        panel.add(btnAjouter);

        add(panel);

        btnAjouter.addActionListener(e -> ajouterClient());

        setVisible(true);
    }

    private void ajouterClient() {
        String nom = txtNom.getText();
        String prenom = txtPrenom.getText();
        String cin = txtCin.getText();
        String tel = txtTelephone.getText();
        String email = txtEmail.getText();

        Client c = new Client(0, nom, prenom, cin, tel, email);
        boolean success = clientDAO.ajouterClient(c);

        if (success) {
            JOptionPane.showMessageDialog(this, "Client ajouté avec succès !");
            txtNom.setText("");
            txtPrenom.setText("");
            txtCin.setText("");
            txtTelephone.setText("");
            txtEmail.setText("");
            parent.rechargerClients();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout du client.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Pour tester
    public static void main(String[] args) {
        DatabaseManager db = new DatabaseManager();
        db.connect();
        ClientDAO c = new ClientDAO(db);
        new ClientForm(c, null);
    }

}
