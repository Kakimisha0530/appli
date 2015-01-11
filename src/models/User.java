package models;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class User extends Mapping {

    private String nom, prenom, email, code, couleur;
    private int id, status;

    public User(int id) throws ClassNotFoundException, IOException, SQLException {
        super("users", "id=" + id);
        setId(id);
    }

    public User() throws ClassNotFoundException, IOException, SQLException {
        this(0);
    }

    public User(String mail, String code) throws ClassNotFoundException, IOException, SQLException {
        super("users", "");
        this.email = mail;
        this.code = code;
    }

    public void reset() throws IOException, ClassNotFoundException, SQLException {
        this.id = 0;
        this.status = 0;
        this.code = "";
        this.email = "";
        this.nom = "";
        this.prenom = "";
        //LocalConfig conf = LocalConfig.getConfig();
        //conf.user = this;
        //conf.saveConfig();
    }
    
    @Override
    public void save(Connexion connect) throws IllegalAccessException, IllegalArgumentException, SQLException{
        setCondition(" id=" + this.getId());
        super.save(connect);
    }

    public int getId() {
        return this.id;
    }

    private void setId(int id) {
        if (this.id <= 0 && id > 0) {
            this.id = id;
        }
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getMail() {
        return email;
    }

    public void setMail(String mail) {
        this.email = mail;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    /* public void setStatus(int status)
     {
     this.status = status;
     }
     

     public int getStatus() {
     return status;
     }*/
    
    public String getCouleur() {
        return couleur;
    }

    public void setCouleur(String code) {
        this.couleur = code;
    }

    @Override
    public String toString() {
        String str = "";
        str += this.getId() + ". ";
        str += this.nom + " " + this.prenom;
        str += " (" + this.email + ")";
        return str;
    }

    @Override
    public void updateObj(int i) {
        setId(i);
        if (this.exist()) {
            try {
                LocalConfig conf = LocalConfig.getConfig();
                conf.user = this;
                conf.saveConfig();
            }
            catch (IOException | ClassNotFoundException | SQLException ex) {
                Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public boolean connecter(Connexion connect) throws SQLException, IllegalArgumentException, IllegalAccessException, IOException, ClassNotFoundException {

        String str = " email=" + Connexion.makeQuery(this.email) + " AND code=" + Connexion.makeQuery(this.code);
        this.find(str, connect);
        if (this.exist()) {
            LocalConfig conf = LocalConfig.getConfig();
            conf.user = this;
            conf.saveConfig();
        }

        return this.exist();
    }

    public static HashMap<Integer, User> getAllUsers(Connexion connect) throws ClassNotFoundException, IOException, SQLException, IllegalArgumentException, IllegalAccessException {
        HashMap<Integer, User> liste = new HashMap<>();

        User u = new User();
        ArrayList<Integer> id_liste = u.findAll("", connect);
        for (int i : id_liste) {
            u = new User(i);
            u.find(connect);
            liste.put(i, u);
        }

        return liste;
    }
}
