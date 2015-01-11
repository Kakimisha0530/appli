/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

/**
 *
 * @author aicha
 */
public class FicheProjet {
    
    public Projet projet;
    public HashMap<Integer,Suivis> suivants;
    
    public FicheProjet(Connexion connect , int pr) throws ClassNotFoundException, IOException, SQLException, IllegalArgumentException, IllegalAccessException{
        this.projet = new Projet(pr);
        this.projet.find(connect);
        this.suivants = Suivis.getForProject(connect, this.projet.getId());
    }
}
