/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

/**
 *
 * @author aicha
 */
public interface Observer {
    public void update(Observable obs,Object o);
    public void reset();
}
