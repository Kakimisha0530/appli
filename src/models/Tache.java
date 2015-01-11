package models;

import models.*;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Tache extends Mapping {

    private int id, user, projet,avancement;
    private long creation;
    private String description = "",commentaires = "";
    private float duree_prevue,duree_reel,depassement;
    private boolean valide = false;

    public Tache(int id) throws ClassNotFoundException, IOException, SQLException {
        super("taches", " id=" + id);
        setId(id);
    }

    public Tache() throws ClassNotFoundException, IOException, SQLException {
        this(0);
    }

    public int getId() {
        return this.id;
    }

    private void setId(int id) {
        if (this.id <= 0 && id > 0) {
            this.id = id;
        }
    }

    public long getCreation() {
        return this.creation;
    }

    private void setCreation() {
        if (this.creation <= 0) {
            DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            this.creation = Long.parseLong(dateFormat.format(new Date()));
        }
    }
    

    public int getAvancement()
	{
		return avancement;
	}

	public void setAvancement(int avancement)
	{
		this.avancement = avancement;
	}

	public float getDuree_prevue()
	{
		return duree_prevue;
	}

	public void setDuree_prevue(float duree_prevue)
	{
		this.duree_prevue = duree_prevue;
		setDepassement();
	}

	public float getDuree_reel()
	{
		return duree_reel;
	}

	public void setDuree_reel(float duree_reel)
	{
		this.duree_reel = duree_reel;
		setDepassement();
	}

	public float getDepassement()
	{
		return depassement;
	}

	private void setDepassement()
	{
		this.depassement = this.duree_prevue - this.duree_reel;
                setValide();
	}

	public boolean isValide()
	{
		return valide;
	}

	private void setValide()
	{
		this.valide = this.depassement >= 100;
	}

	@Override
    public void save(Connexion connect) throws IllegalAccessException, IllegalArgumentException, SQLException {
        setCreation();
        super.save(connect);
    }

    public int getUser() {
        return user;
    }

    public void setUser(int u) {
        this.user = u;
    }

    public int getProjet() {
        return projet;
    }

    public void setProjet(int p) {
        this.projet = p;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getCommentaires() {
        return commentaires;
    }

    public void setCommentaires(String c) {
        this.commentaires = c;
    }

    
    public static HashMap<Integer, Tache> getForProject(Connexion connect, int projet) throws ClassNotFoundException, IOException, SQLException, IllegalArgumentException, IllegalAccessException {
        HashMap<Integer, Tache> liste = new HashMap<Integer, Tache>();

        Tache t = new Tache();
        String cnd = " projet = " + Connexion.makeQuery(projet);
        ArrayList<Integer> id_liste = t.findAll(cnd, connect);
        for (int i : id_liste) {
            t = new Tache(i);
            t.find(connect);
            liste.put(i, t);
        }

        return liste;
    }
    
    public static HashMap<Integer, Tache> getForProject(Connexion connect, int projet , boolean valid) throws ClassNotFoundException, IOException, SQLException, IllegalArgumentException, IllegalAccessException {
        HashMap<Integer, Tache> liste = new HashMap<Integer, Tache>();

        Tache t = new Tache();
        String cnd = " projet = " + Connexion.makeQuery(projet) + " AND valide = " + Connexion.makeQuery(valid);
        ArrayList<Integer> id_liste = t.findAll(cnd, connect);
        for (int i : id_liste) {
            t = new Tache(i);
            t.find(connect);
            liste.put(i, t);
        }

        return liste;
    }

    public static HashMap<Integer, Tache> getForUser(Connexion connect, int projet, int[] user) throws ClassNotFoundException, IOException, SQLException, IllegalArgumentException, IllegalAccessException {
        HashMap<Integer, Tache> liste = new HashMap<Integer, Tache>();
        if (Connexion.makeQuery(user).length() > 2) {
            Tache t = new Tache();
            String cnd = " projet=" + projet + " AND user in " + Connexion.makeQuery(user);
            ArrayList<Integer> id_liste = t.findAll(cnd, connect);
            for (int i : id_liste) {
                t = new Tache(i);
                t.find(connect);
                liste.put(i, t);
            }
        }

        return liste;
    }

    @Override
    public void updateObj(int i) {
        setId(i);
    }

    @Override
    public String toString() {
        String str = "";
        str += this.id + ". " + this.description + " (" + this.creation + ")";
        return str;
    }

}
