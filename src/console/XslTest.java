/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package console;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Connexion;
import models.LocalConfig;
import models.Tache;
import net.sf.jxls.exception.ParsePropertyException;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

/**
 *
 * @author aicha
 */
public class XslTest {
    public static void main(String[] a){
        try {
            Connexion connect = Connexion.makeConnexion();
            Collection task1 = Tache.getForProject(connect, 1, true).values();
            Collection task2 = Tache.getForProject(connect, 1, false).values();
            Map beans = new HashMap();
            beans.put("task1", task1);
            beans.put("task2", task2);
            XLSTransformer transformer = new XLSTransformer();
            transformer.transformXLS(LocalConfig.CONTEXT_PATH + "planning.xls", beans, LocalConfig.CONTEXT_PATH + "planning_test.xls");
        }
        catch (IOException | ClassNotFoundException | SQLException | IllegalArgumentException | IllegalAccessException | ParsePropertyException | InvalidFormatException ex) {
            Logger.getLogger(XslTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
