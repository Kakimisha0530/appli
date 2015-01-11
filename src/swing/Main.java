/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swing;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Connexion;
import models.LocalConfig;

/**
 *
 * @author aicha
 */
public class Main {
    
    public static void main(String[] args) {
        try {
            
            Connexion connect = Connexion.makeConnexion();
            LocalConfig conf = LocalConfig.getConfig();
            if(conf.user == null || conf.user.getId() <= 0)
                new ConnexionView(connect).setVisible(true);
            else
                new ProjectsListView(connect).setVisible(true);
        }
        catch (IOException | ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
