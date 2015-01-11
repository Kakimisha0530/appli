package console;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Connexion;
import models.Projet;
import models.Tache;

public class Main {

    public static void main(String[] a) {
        try {
            Connexion connect = new Connexion();
             Projet t = new Projet();
             t.setDescription("bla bla bla bla bla bla bla");
             t.setDuree_reel(10);
             t.save(connect);
             System.out.println(t);
            
        }
        catch (IllegalArgumentException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
