package projet.dao;

import java.sql.*;

import projet.models.Utilisateur;
import projet.tools.DatabaseManager;

public class UtilisateurDAO {
	
	private DatabaseManager db;

    public UtilisateurDAO(DatabaseManager db) {
        this.db = db;
    }

    public Utilisateur verifierConnexion(String nom, String motDePasse) {
        String sql = "SELECT * FROM Utilisateur WHERE nomUtilisateur=? AND motDePasse=?";
        try (PreparedStatement pstmt = db.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, nom);
            pstmt.setString(2, motDePasse);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Utilisateur(
                    rs.getInt("id"),
                    rs.getString("nomUtilisateur"),
                    rs.getString("motDePasse"),
                    rs.getString("role")
                );
            }
        } catch (SQLException e) {
            System.out.println("Erreur connexion : " + e.getMessage());
        }
        return null;
    }
    
    public java.util.List<Utilisateur> getTousLesUtilisateurs() {
        java.util.List<Utilisateur> liste = new java.util.ArrayList<>();
        String sql = "SELECT * FROM Utilisateur";
        try (PreparedStatement stmt = db.getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Utilisateur u = new Utilisateur(
                    rs.getInt("id"),
                    rs.getString("nomUtilisateur"),
                    rs.getString("motDePasse"),
                    rs.getString("role")
                );
                liste.add(u);
            }
        } catch (SQLException e) {
            System.out.println("Erreur getTousLesUtilisateurs : " + e.getMessage());
        }
        return liste;
    }
    
    public boolean supprimerUtilisateur(int id) {
        String sql = "DELETE FROM Utilisateur WHERE id=?";
        try (PreparedStatement stmt = db.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erreur suppression utilisateur : " + e.getMessage());
            return false;
        }
    }
    
    public boolean ajouterUtilisateur(Utilisateur user) {
        String sql = "INSERT INTO Utilisateur (nomUtilisateur, motDePasse, role) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = db.getConnection().prepareStatement(sql)) {
            stmt.setString(1, user.getNomUtilisateur());
            stmt.setString(2, user.getMotDePasse());
            stmt.setString(3, user.getRole());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erreur ajouterUtilisateur : " + e.getMessage());
            return false;
        }
    }
    
    public boolean modifierUtilisateur(Utilisateur u) {
        String sql = "UPDATE Utilisateur SET nomUtilisateur=?, motDePasse=?, role=? WHERE id=?";
        try (PreparedStatement stmt = db.getConnection().prepareStatement(sql)) {
            stmt.setString(1, u.getNomUtilisateur());
            stmt.setString(2, u.getMotDePasse());
            stmt.setString(3, u.getRole());
            stmt.setInt(4, u.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erreur modifierUtilisateur : " + e.getMessage());
            return false;
        }
    }


}
