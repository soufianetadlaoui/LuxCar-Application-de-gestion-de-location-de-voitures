package projet.tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
	
	private static final String URL = "jdbc:ucanaccess://D:/Document/LocationVoiture.accdb";
    private Connection connection;
    
 // Connexion à la base de données
    public void connect() {
        try {
            connection = DriverManager.getConnection(URL);
            System.out.println("✅ Connexion réussie à la base de données !");
        } catch (SQLException e) {
            System.out.println("❌ Erreur de connexion : " + e.getMessage());
        }
    }
    
 // Déconnexion
    public void disconnect() {
        try {
            if (connection != null) {
                connection.close();
                System.out.println("🔌 Déconnexion réussie.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la déconnexion : " + e.getMessage());
        }
    }
    
 // Accès à l'objet Connection
    public Connection getConnection() {
        return connection;
    }
    
 // Exemple : exécuter une requête INSERT/UPDATE/DELETE
    public void executeUpdate(String sql) {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("✅ Requête exécutée : " + sql);
        } catch (SQLException e) {
            System.out.println("❌ Erreur dans la requête : " + e.getMessage());
        }
    }

}
