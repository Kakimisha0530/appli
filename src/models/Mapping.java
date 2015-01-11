package models;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

public abstract class Mapping implements Serializable {

	private static final long serialVersionUID = -6957272553879917188L;
	protected String table;
    protected String condition;
    protected boolean exist;
    protected transient static final String FILE_NAME = LocalConfig.CONTEXT_PATH + "userDB";

    /**
     * constructeur de la classe
     *
     * @param table la table sur laquelle on va travailler
     * @param cnd
     * @throws SQLException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public Mapping(String table, String cnd) throws ClassNotFoundException,
    IOException, SQLException {
        this.table = table;
        this.condition = cnd;
        //setClasses();
    }
    
    protected void setCondition(String cnd){
        this.condition = cnd;
    }


    /*
     * public void init(String table,Object objet) { this.table = table;
     * this.objet = objet; setClasses(); }
     */
    /**
     * methode qui permet de remplir une map de données qui vont faciliter les reqêtes sql
     *
     * @return une HashMap contenant en clés les attributs de l'objet(correspondannts aux colonnes de la table) et en entrées leurs valeurs (correspondants aux entrées dans la table)
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public HashMap<String, Object> map() throws IllegalArgumentException,
    IllegalAccessException {
        HashMap<String, Object> map = new HashMap<>();
        for (Field i : this.getClass().getDeclaredFields()) {
            i.setAccessible(true);
            if (true)//!isEntite(i))
            {
                map.put(i.getName(), i.get(this));
            }
        }

        return map;
    }

    /**
     * ************************** Methodes de traitement des données *******************************
     */
    /**
     * methode qui permet de sauvegarder les données en base
     *
     * @param connect
     * @throws IllegalAccessException
     * @throws SQLException
     * @throws IllegalArgumentException
     */
    public void save(Connexion connect) throws IllegalAccessException, IllegalArgumentException,
    SQLException {
        if (this.exist) {
            HashMap<String, Object> donnees = this.map();
            donnees.remove("id");
            connect.update(table, donnees, condition);
        }
        else {
            int i = connect.insertInto(table, this.map());
            exist = i > 0;
            updateObj(i);
        }

    }

    /**
     * methode permettant la mise à jour des données en base
     *
     * @param condition
     * @return
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SQLException
     *
     * public void update(String condition,Connexion connect) throws IllegalArgumentException, IllegalAccessException, SQLException { connect.update(table, this.map(), condition); }
     */
    /**
     * methode de suppression de donn&eacute;es en base
     *
     * @param condition
     * @param connect
     * @throws SQLException
     */
    public void delete(Connexion connect) throws SQLException {
        connect.delete(table, condition);
        exist = false;
    }

    /**
     * methode visant &agrave; retrouver une entit&eacute; en base de données si elle existe (utilisée dans les constructeurs)
     *
     * @param condition
     * @param connect
     * @throws SQLException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public void find(String condition, Connexion connect) throws SQLException,
    IllegalArgumentException, IllegalAccessException {
        ResultSet r = connect.selectObject(table, condition);
        if (r.next()) {
            for (Field i : this.getClass().getDeclaredFields()) {
                i.setAccessible(true);
                if (true)//(!isEntite(i))
                {
                    i.set(this, r.getObject(i.getName()));
                }
            }
            exist = true;

            r.close();
        }
        else {
            exist = false;
        }
    }

    
    public void find(Connexion connect) throws SQLException,
    IllegalArgumentException, IllegalAccessException {
        this.find(condition, connect);
    }

    public ArrayList<Integer> findAll(String condition, Connexion connect) throws SQLException {

        ArrayList<Integer> liste = new ArrayList<>();
        Vector<String> data = new Vector<>();
        data.add("id");
        ResultSet r = connect.select(table, data, condition," id DESC","");

        while (r.next()) {
            liste.add(r.getInt("id"));
        }

        r.close();

        return liste;
    }

    public int count(String tab, String condition, Connexion connect) throws SQLException {
        return connect.count(tab, condition);
    }

    public boolean exist() {
        return exist;
    }

    public abstract void updateObj(int i);
}
