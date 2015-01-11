package models;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Projet extends Mapping {
    
    private int id , avancement;
    private float duree_prevue, duree_reel;
    private int date;
    private String description = "", designation = "", responsable = "" , client = "", dossier = "";

    public Projet(int i) throws ClassNotFoundException, IOException, SQLException {
        super("projets", " id=" + i);
        setId(i);
        setCreation();
    }

    public Projet() throws ClassNotFoundException, IOException, SQLException {
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

    public int getDate() {
        return this.date;
    }

    private void setCreation() {
        if (this.date <= 0) {
            DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            this.date = Integer.parseInt(dateFormat.format(new Date()));
        }
    }

    public float getDuree_prevue() {
        return duree_prevue;
    }

    public void setDuree_prevue(float duree_estimee) {
        this.duree_prevue = duree_estimee;
    }

    public float getDuree_reel() {
        return duree_reel;
    }

    public void setDuree_reel(float duree_reel) {
        this.duree_reel = duree_reel;
    }
    
    public int getAvancement() {
        return avancement;
    }

    public void setAvancement(int avan) {
        this.duree_reel = avan;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String titre) {
        this.designation = titre;
    }

    public String getResponsable()
	{
		return responsable;
	}

	public void setResponsable(String responsable)
	{
		this.responsable = responsable;
	}

	public String getClient()
	{
		return client;
	}

	public void setClient(String client)
	{
		this.client = client;
	}

	public String getDossier()
	{
		return dossier;
	}

	public void setDossier(String dossier)
	{
		this.dossier = dossier;
	}

	@Override
    public void updateObj(int i) {
        setId(i);
    }

    @Override
    public String toString() {
        String str = "";
        str += this.id + ". " + this.designation;
        str += " (" + this.description + ")";
        return str;
    }

    public static HashMap<Integer, Projet> extractForPeriode(int debut, int fin, Connexion connect) throws ClassNotFoundException, IOException, SQLException, IllegalArgumentException, IllegalAccessException {
        HashMap<Integer, Projet> liste = new HashMap<Integer, Projet>();

        Projet p = new Projet();
        String cnd = " date >= " + debut + " AND date <=" + fin;
        ArrayList<Integer> id_liste = p.findAll(cnd, connect);
        for (int i : id_liste) {
            p = new Projet(i);
            p.find(connect);
            liste.put(i, p);
        }

        return liste;
    }

    public int getStateInt() {
        int st = 2;
        if (this.avancement >= 100) {
            st = 3;
        }
        return st;
    }

    public static String getStateString(int i) {
        String str = "";
        switch (i) {
            case 1:
                str = "Prpjet non d&eacute;but&eacute;";
                break;
            case 2:
                str = "Projet en cours";
                break;
            case 3:
                str = "Projet termin&eacute;";
                break;
        }

        return str;
    }
}
