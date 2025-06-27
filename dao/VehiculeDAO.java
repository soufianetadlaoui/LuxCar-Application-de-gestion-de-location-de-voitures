package projet.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import projet.models.Vehicule;
import projet.tools.DatabaseManager;


public class VehiculeDAO {
	private DatabaseManager db;

    public VehiculeDAO(DatabaseManager db) {
        this.db = db;
    }

    // Ajouter une voiture
    public boolean ajouterVoiture(Vehicule voiture) {
        String sql = "INSERT INTO Vehicule (marque, modele, immatriculation, type, disponible) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = db.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, voiture.getMarque());
            pstmt.setString(2, voiture.getModel());
            pstmt.setString(3, voiture.getImmatriculation());
            pstmt.setString(4, voiture.getType());
            pstmt.setBoolean(5, voiture.getDisponible());

            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erreur ajout voiture : " + e.getMessage());
            return false;
        }
    }

    // Récupérer toutes les voitures
    public List<Vehicule> getToutesLesVoitures() {
        List<Vehicule> voitures = new ArrayList<>();
        String sql = "SELECT * FROM Vehicule";
        try (Statement stmt = db.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Vehicule v = new Vehicule(
                    rs.getInt("id"),
                    rs.getString("marque"),
                    rs.getString("modele"),
                    rs.getString("immatriculation"),
                    rs.getString("type"),
                    rs.getBoolean("disponible")
                );
                voitures.add(v);
            }
        } catch (SQLException e) {
            System.out.println("Erreur récupération voitures : " + e.getMessage());
        }
        return voitures;
    }

    // Supprimer une voiture
    public boolean supprimerVoiture(int id) {
        String sql = "DELETE FROM Vehicule WHERE id = ?";
        try (PreparedStatement pstmt = db.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.out.println("Erreur suppression voiture : " + e.getMessage());
            return false;
        }
    }

    // Modifier une voiture
    public boolean modifierVoiture(Vehicule voiture) {
        String sql = "UPDATE Vehicule SET marque=?, modele=?, immatriculation=?, type=?, disponible=? WHERE id=?";
        try (PreparedStatement pstmt = db.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, voiture.getMarque());
            pstmt.setString(2, voiture.getModel());
            pstmt.setString(3, voiture.getImmatriculation());
            pstmt.setString(4, voiture.getType());
            pstmt.setBoolean(5, voiture.getDisponible());
            pstmt.setInt(6, voiture.getId());

            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.out.println("Erreur modification voiture : " + e.getMessage());
            return false;
        }
    }

}
