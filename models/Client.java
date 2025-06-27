package projet.models;

public class Client {
	public int id;
	public String nom;
	public String prenom;
	public String cin;
	public String telephone;
	public String email;
	
	//constructeur
	public Client (int id, String nom, String prenom, String cin, String telephone, String mail){
		this.id=id;
		this.nom=nom;
		this.prenom=prenom;
		this.cin=cin;
		this.telephone=telephone;
		this.email=mail;
	}
	
	//getters end stters
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id=id;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom=nom;
	}
	public String getPrenom() {
		return prenom;
	}
	public void setPrenom(String prenom) {
		this.prenom=prenom;
	}
	public String getCin() {
		return cin;
	}
	public void setCin(String cin) {
		this.cin=cin;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone=telephone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String mail) {
		this.email=mail;
	}
	//la fonction toString
	public String toString() {
		return "Client {ID : "+id+", Nom : "+nom+", Prenom : "+prenom+
				", Cin : "+cin+", Telephone: "+telephone+", Email : "+email+".}";
	}

}
