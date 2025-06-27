package projet.models;

import java.util.*;

public class Contrat {
	public int id;
	public Client client;
	public Vehicule vehicule;
	public Date dateDebut;
	public Date dateFin;
	public double prixParJour;
	public String conditions;
	
	public Contrat(int id, Vehicule vehicule, Client client, Date dateDebut, Date dateFin, Double prixParJour, String conditions){
		this.id=id;
		this.client=client;
		this.vehicule=vehicule;
		this.dateDebut=dateDebut;
		this.dateFin=dateFin;
		this.prixParJour=prixParJour;
		this.conditions=conditions;
	}
	
	//getters and setters
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id=id;
	}
	public Vehicule getVehicule() {
		return vehicule;
	}
	public void setVehicule(Vehicule vehicule) {
		this.vehicule=vehicule;
	}
	public Client getClient() {
		return client;
	}
	public void setClient(Client client) {
		this.client=client;
	}
	public Date getDateDebut() {
		return dateDebut;
	}
	public void setDateDebut(Date dateDebut) {
		this.dateDebut=dateDebut;
	}
	public Date getDateFin() {
		return dateFin;
	}
	public void setDateFin(Date dateFin) {
		this.dateFin=dateFin;
	}
	public double getPrixParJour() {
		return prixParJour;
	}
	public void setPrixParJour(double prixParJour) {
		this.prixParJour=prixParJour;
	}
	public String getConditions() {
		return conditions;
	}
	public void setConditions(String conditions) {
		this.conditions=conditions;
	}
	public double calculerMontantTotal() {
        long diff = dateFin.getTime() - dateDebut.getTime();
        long jours = diff / (1000 * 60 * 60 * 24);
        return jours * prixParJour;
    }
	
	//la fonction toString
	public String toString() {
		return "Contrat {Id : "+id+", Client :"+client.getNom()+", Vehicule: "+vehicule.marque+" "+vehicule.model+", Date de debut : "+dateDebut+
				", Date de fin : "+dateFin+", Prix par jour : "+prixParJour+", Conditions : "+conditions+".}";
	}

}
