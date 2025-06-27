package projet.models;

public class Vehicule {
	public int id;
	public String marque;
	public String model;
	public String immatriculation;
	public String type;
	public Boolean disponible;
	
	// constructeur 
	public Vehicule(int id, String marque, String model, String immatriculation, String type, Boolean disponible) {
		this.id=id;
		this.marque=marque;
		this.model=model;
		this.immatriculation=immatriculation;
		this.type=type;
		this.disponible=disponible;
	}
	
	//getters et setters
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id=id;
	}
	
	public String getMarque() {
		return marque;
	}
	
	public void setMarque(String marque) {
		this.marque=marque;
	}
	
	public String getModel() {
		return model;
	}
	
	public void setModel(String model) {
		this.model=model;
	}
	
	public String getImmatriculation() {
		return immatriculation;
	}
	
	public void setImmatriculation(String immatriculation) {
		this.immatriculation=immatriculation;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type=type;
	}
	
	public Boolean getDisponible() {
		return disponible;
	}
	
	public void setDisponible(Boolean disponible) {
		this .disponible=disponible;
	}
	
	//méthode toString
	public String toString() {
		return "Voiture {ID : "+id+", Marque : "+marque+", Modele : "+model+", Immatriculation : "
	+immatriculation+", Type : "+type+", Disponibilité : "+disponible+".}";
	}

}
