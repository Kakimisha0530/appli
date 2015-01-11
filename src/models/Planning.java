package models;

import models.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

public class Planning
{
	public Projet projet;
	private HashMap<Integer,Tache> taches;
	
	public Planning(Connexion connect , int pro) throws ClassNotFoundException, IOException, SQLException, IllegalArgumentException, IllegalAccessException{
		this.projet = new Projet(pro);
                this.projet.find(connect);
		this.taches = Tache.getForProject(connect, pro);
	}
	
	public void getXsl(){
		
	}
	
	public void getCsv(){
		
	}
	
	public void updateTache(Connexion connect , int ind) throws IllegalArgumentException, IllegalAccessException, SQLException{
		if (ind > 0) {
			Tache t = this.taches.get(ind);
			this.taches.put(t.getId(), t);
			updateProjet(connect);
		}
	}
	
	public void removeTache(Connexion connect , int ind) throws IllegalArgumentException, IllegalAccessException, SQLException{
		if (ind > 0) {
			Tache t = this.taches.get(ind);
			this.taches.remove(ind);
			t.delete(connect);
			updateProjet(connect);
		}
	}
	
	public void addTache(Connexion connect , Tache t) throws IllegalArgumentException, IllegalAccessException, SQLException{
		if(t != null){
			this.taches.put(t.getId(), t);
			updateProjet(connect);
		}
	}
	
	private void updateProjet(Connexion connect) throws IllegalArgumentException, IllegalAccessException, SQLException{
		float duree_p = 0 , duree_r = 0;
		int avan = 0;
		
		for (int t : this.taches.keySet()) {
			duree_p += this.taches.get(t).getDuree_prevue();
			duree_r += this.taches.get(t).getDuree_reel();
		}
		
		for (int t : this.taches.keySet()) {
			avan += (this.taches.get(t).getDuree_prevue() * 100) / duree_p;
		}
		
		this.projet.setDuree_prevue(duree_p);
		this.projet.setDuree_reel(duree_r);
		this.projet.setAvancement(avan);
		this.projet.save(connect);
	}
}
