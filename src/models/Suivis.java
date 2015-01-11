/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author aicha
 */
public class Suivis extends Mapping{
    private int id,user,projet;
    private float temps;

    public Suivis(int i) throws ClassNotFoundException, IOException, SQLException {
        super("suivi", "id="+i);
        setId(id);
    }
    
    public Suivis() throws ClassNotFoundException, IOException, SQLException {
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
    
    public int getUser(){
        return this.user;
    }
    
    public int getProjet(){
        return this.projet;
    }
    
    public void setUser(int u){
        this.user = u;
    }
    
    public void setProjet(int p){
        this.projet = p;
    }
    
    public static HashMap<Integer, Suivis> getForProject(Connexion connect, int projet) throws ClassNotFoundException, IOException, SQLException, IllegalArgumentException, IllegalAccessException {
        HashMap<Integer, Suivis> liste = new HashMap<>();
        Suivis t = new Suivis();
        String cnd = " projet=" + projet;
        ArrayList<Integer> id_liste = t.findAll(cnd, connect);
        for (int i : id_liste) {
            t = new Suivis(i);
            t.find(connect);
            liste.put(t.getUser(), t);
        }

        return liste;
    }

    @Override
    public void updateObj(int i) {
        setId(i);
    }
}
