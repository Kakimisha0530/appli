/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.HashMap;

/**
 *
 * @author aicha
 */
public class LocalConfig implements Serializable{
    
    public transient static final String CONTEXT_PATH = "/home/aicha/Bureau/GIT/appli/files/";
    //public transient static final String CONTEXT_PATH = "/Users/vzanchi/Documents/workspace/appli/";
    
    private transient static final String FILE_NAME = CONTEXT_PATH + "configDB";
    public User user;
    public boolean fullFrame;
    public HashMap<Integer,String> usernames;
    public HashMap<Integer,Color> usercolors;
    public HashMap<Integer,Boolean> userview;
    
    public transient static final boolean DEBUG_MODE = true;
    
    public LocalConfig(){
        usernames = new HashMap<>();
        usercolors = new HashMap<>();
        userview = new HashMap<>();
    }    
    
    public void saveConfig() {
        try {
            if (DEBUG_MODE) {
                System.out.println("try to write the config file");
            }
            FileOutputStream fichier = new FileOutputStream(FILE_NAME);
            ObjectOutputStream stream = new ObjectOutputStream(fichier);
            stream.writeObject(this);
            stream.flush();
            stream.close();
            if (DEBUG_MODE) {
                System.out.println("config file writen");
            }
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }

    public static LocalConfig getConfig() throws IOException, ClassNotFoundException, SQLException {
        if (new File(FILE_NAME).exists()) {
            if (DEBUG_MODE) {
                System.out.println("try to read the config file");
            }
            FileInputStream fichier = new FileInputStream(FILE_NAME);

            if (DEBUG_MODE) {
                System.out.println("file found");
            }

            ObjectInputStream stream = new ObjectInputStream(fichier);
            LocalConfig lg = (LocalConfig) stream.readObject();

            if (DEBUG_MODE) {
                System.out.println("config file deserialization successful");
            }

            stream.close();

            return lg;
        }

        return new LocalConfig();
    }
}
