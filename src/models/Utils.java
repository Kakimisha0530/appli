/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author aicha
 */
public abstract class Utils {

    public static int[] getInterval(int annee, int mois) {
        int debut,fin;
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        cal.set(annee, mois, 1);
        debut = Integer.parseInt(dateFormat.format(cal.getTime()));
        cal.set(annee, mois,cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        fin = Integer.parseInt(dateFormat.format(cal.getTime()));
        
        return new int[]{debut,fin};
    }
    
    public static boolean isNotEmpty(String... val){
        for(String str : val){
            if("".equals(str) || str == null)
                return false;
        }
        return true;
    }
    
    public static int getCurrentYear() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        
        return cal.get(Calendar.YEAR);
    }
    
    public static int getCurrentMonth() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        
        return cal.get(Calendar.MONTH);
    }
    
    public static int[] parserDate(String str){
        if(str.length() >= 8){
            //System.out.println(str);
            int a = Integer.parseInt(str.substring(0, 4));
            int m = Integer.parseInt(str.substring(4, 6)) - 1;
            return new int[]{a,m};
        }
        
        return null;
    }
    
    public static boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
    
    public static boolean isFloat(String s) {
		try {
			Float.parseFloat(s);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
}
