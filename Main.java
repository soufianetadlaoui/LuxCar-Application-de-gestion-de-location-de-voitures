package projet;

import projet.dao.ClientDAO;
import projet.dao.VehiculeDAO;
import projet.tools.DatabaseManager;
import projet.ui.LoginForm;

public class Main {
	public static void main(String[] args) {
        DatabaseManager db = new DatabaseManager();
        db.connect();
        ClientDAO c = new ClientDAO(db);
        VehiculeDAO v = new VehiculeDAO(db);
        new LoginForm(db,c,v);
    }

}
