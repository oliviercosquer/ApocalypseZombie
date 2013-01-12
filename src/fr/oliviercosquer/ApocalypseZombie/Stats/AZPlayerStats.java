/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.oliviercosquer.ApocalypseZombie.Stats;

import java.io.Serializable;
import java.util.Map;

/**
 *
 * @author Olivier
 */
public class AZPlayerStats implements Serializable{
    private int zkill;
    private int death;

    public int getDeath() {
        return death;
    }

    public void setDeath(int death) {
        this.death = death;
    }

    public int getZkill() {
        return zkill;
    }

    public void setZkill(int zkill) {
        this.zkill = zkill;
    }

    public AZPlayerStats(int zkill, int death) {
        this.zkill = zkill;
        this.death = death;
    }    
    
    public AZPlayerStats(Map<String, Object> inputMap) {
        this.zkill = (Integer)inputMap.get("zKill");
        this.death = (Integer)inputMap.get("death");
    }   
    
}
