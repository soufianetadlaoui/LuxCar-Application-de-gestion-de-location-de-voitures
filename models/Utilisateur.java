package projet.models;

public class Utilisateur {
	public int id;
	public String nomUtilisateur;
	public String motDePasse;
	public String role;
	
	public Utilisateur (int id, String nomUtilisateur, String motDePasse, String role){
		this.id=id;
		this.nomUtilisateur=nomUtilisateur;
		this.motDePasse=motDePasse;
		this.role=role;
	}
	
	//getters and setters
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id=id;
	}
	public String getNomUtilisateur() {
		return nomUtilisateur;
	}
	public void setNomUtilisateur(String nomUtilisateur) {
		this.nomUtilisateur=nomUtilisateur;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
        if (role.equalsIgnoreCase("ADMIN") || role.equalsIgnoreCase("USER")) {
            this.role = role.toUpperCase();
        } else {
            System.out.println("Rôle invalide. Utilisez 'ADMIN' ou 'USER'.");
        }
    }
    public boolean estAdmin() {
        return role.equalsIgnoreCase("ADMIN");
    }
    public String getMotDePasse() {
    	return motDePasse;
    }
    public void setMotDePasse(String motDePasse) {
    	this.motDePasse=motDePasse;
    }
    
    //fonction toString
    
    public String toString() {
    	return "Role: {Id : "+id+", Nom d'utilisateur : "
    +nomUtilisateur+", Mot de passe : "+motDePasse+", Role : "+role+".}";
    }

}
