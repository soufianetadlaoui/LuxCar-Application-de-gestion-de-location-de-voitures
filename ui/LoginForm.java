package projet.ui;

import javax.swing.*;

import projet.dao.ClientDAO;
import projet.dao.UtilisateurDAO;
import projet.dao.VehiculeDAO;
import projet.models.Utilisateur;
import projet.tools.DatabaseManager;

import java.awt.*;

public class LoginForm extends JFrame{
	
	private JTextField txtUser;
    private JPasswordField txtPass;
    private UtilisateurDAO utilisateurDAO;
    private DatabaseManager db;
    private ClientDAO clientDAO;
    private VehiculeDAO voitureDAO;

    public LoginForm(DatabaseManager db, ClientDAO clientDAO, VehiculeDAO voitureDAO) {
    	this.db=db;
    	this.clientDAO=clientDAO;
    	this.voitureDAO=voitureDAO;
        utilisateurDAO = new UtilisateurDAO(db);

        setTitle("Connexion");
        setSize(350, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        txtUser = new JTextField();
        txtPass = new JPasswordField();
        JButton btnLogin = new JButton("Se connecter");

        panel.add(new JLabel("Nom d'utilisateur :"));
        panel.add(txtUser);
        panel.add(new JLabel("Mot de passe :"));
        panel.add(txtPass);
        panel.add(new JLabel(""));
        panel.add(btnLogin);

        add(panel);

        btnLogin.addActionListener(e -> seConnecter());

        setVisible(true);
    }

    private void seConnecter() {
        String nom = txtUser.getText();
        String pass = new String(txtPass.getPassword());

        Utilisateur u = utilisateurDAO.verifierConnexion(nom, pass);

        if (u != null) {
            JOptionPane.showMessageDialog(this, "Bienvenue " + u.getNomUtilisateur() + " (" + u.getRole() + ")");
            new Dashboard(u, db, clientDAO, voitureDAO); // ✅ redirige vers ton interface principale
            dispose(); // fermer la fenêtre de login
        } else {
            JOptionPane.showMessageDialog(this, "Identifiants incorrects !");
        }
    }


    public static void main(String[] args) {
        DatabaseManager db = new DatabaseManager();
        db.connect();
        ClientDAO c = new ClientDAO(db);
        VehiculeDAO v = new VehiculeDAO(db);
        
        new LoginForm(db,c,v);
    }

}
