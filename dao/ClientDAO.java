package projet.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import projet.models.Client;
import projet.tools.DatabaseManager;

public class ClientDAO {
	
	private DatabaseManager db;

    public ClientDAO(DatabaseManager db) {
        this.db = db;
    }

    public boolean ajouterClient(Client client) {
        String sql = "INSERT INTO Client (nom, prenom, cin, telephone, email) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = db.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, client.getNom());
            pstmt.setString(2, client.getPrenom());
            pstmt.setString(3, client.getCin());
            pstmt.setString(4, client.getTelephone());
            pstmt.setString(5, client.getEmail());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erreur ajout client : " + e.getMessage());
            return false;
        }
    }

    public List<Client> getTousLesClients() {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM Client";
        try (Statement stmt = db.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Client c = new Client(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("cin"),
                        rs.getString("telephone"),
                        rs.getString("email")
                );
                clients.add(c);
            }
        } catch (SQLException e) {
            System.out.println("Erreur récupération clients : " + e.getMessage());
        }
        return clients;
    }

    public boolean supprimerClient(int id) {
        String sql = "DELETE FROM Client WHERE id = ?";
        try (PreparedStatement pstmt = db.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erreur suppression client : " + e.getMessage());
            return false;
        }
    }

    public boolean modifierClient(Client client) {
        String sql = "UPDATE Client SET nom=?, prenom=?, cin=?, telephone=?, email=? WHERE id=?";
        try (PreparedStatement pstmt = db.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, client.getNom());
            pstmt.setString(2, client.getPrenom());
            pstmt.setString(3, client.getCin());
            pstmt.setString(4, client.getTelephone());
            pstmt.setString(5, client.getEmail());
            pstmt.setInt(6, client.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erreur modification client : " + e.getMessage());
            return false;
        }
    }

}
