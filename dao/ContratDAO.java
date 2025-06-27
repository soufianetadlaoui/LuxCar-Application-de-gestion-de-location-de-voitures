package projet.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import projet.models.Client;
import projet.models.Contrat;
import projet.models.Vehicule;
import projet.tools.DatabaseManager;

public class ContratDAO {
	
	private DatabaseManager db;
    private ClientDAO clientDAO;
    private VehiculeDAO voitureDAO;

    public ContratDAO(DatabaseManager db) {
        this.db = db;
        this.clientDAO = new ClientDAO(db);
        this.voitureDAO = new VehiculeDAO(db);
    }

    public boolean ajouterContrat(Contrat contrat) {
        String sql = "INSERT INTO Contrat (idClient, idVehicule, dateDebut, dateFin, prixParJour, conditions) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = db.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, contrat.getClient().getId());
            pstmt.setInt(2, contrat.getVehicule().getId());
            pstmt.setDate(3, new java.sql.Date(contrat.getDateDebut().getTime()));
            pstmt.setDate(4, new java.sql.Date(contrat.getDateFin().getTime()));
            pstmt.setDouble(5, contrat.getPrixParJour());
            pstmt.setString(6, contrat.getConditions());

            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erreur ajout contrat : " + e.getMessage());
            return false;
        }
    }

    public List<Contrat> getTousLesContrats() {
        List<Contrat> contrats = new ArrayList<>();
        String sql = "SELECT * FROM Contrat";
        try (Statement stmt = db.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                int idClient = rs.getInt("idClient");
                int idVoiture = rs.getInt("idVehicule");

                Client client = clientDAO.getTousLesClients().stream().filter(c -> c.getId() == idClient).findFirst().orElse(null);
                Vehicule voiture = voitureDAO.getToutesLesVoitures().stream().filter(v -> v.getId() == idVoiture).findFirst().orElse(null);

                if (client != null && voiture != null) {
                    Contrat contrat = new Contrat(
                        id,
                        voiture,
                        client,
                        rs.getDate("dateDebut"),
                        rs.getDate("dateFin"),
                        rs.getDouble("prixParJour"),
                        rs.getString("conditions")
                    );
                    contrats.add(contrat);
                }
            }

        } catch (SQLException e) {
            System.out.println("Erreur récupération contrats : " + e.getMessage());
        }
        return contrats;
    }

    public boolean supprimerContrat(int id) {
        String sql = "DELETE FROM Contrats WHERE id = ?";
        try (PreparedStatement pstmt = db.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erreur suppression contrat : " + e.getMessage());
            return false;
        }
    }

}
