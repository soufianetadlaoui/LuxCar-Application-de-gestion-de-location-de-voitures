package projet.models;

import java.util.Date;

public class Facture {
	public int id;
	public Contrat contrat;
	public double montantTotal;
	public Date dateFacture;
	
	public Facture(int id, Contrat contrat, double montantTotal, Date dateFacture){
		this.id=id;
		this.contrat=contrat;
		this.montantTotal=montantTotal;
		this.dateFacture=dateFacture;
	}
	
	//getters and stters
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id=id;
	}
	public Contrat getContrat() {
		return contrat;
	}
	public void setContrat(Contrat contrat) {
		this.contrat=contrat;
	}
	public double getMontantTotal() {
		return montantTotal;
	}
	public void setMontantTotal(double montantTotal) {
		this.montantTotal=montantTotal;
	}
	public Date getDateFacture() {
		return dateFacture;
	}
	public void setDateFacture(Date dateFacture) {
		this.dateFacture=dateFacture;
	}
	
	//fonction toString
	public String toString() {
		return "Facture [Id : "+id+", Contrat : "+contrat.getId()+
				", Montant totale : "+montantTotal+", date de la facture : "+ dateFacture+".]";
	}
}
