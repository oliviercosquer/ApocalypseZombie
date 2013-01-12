/*
Copyright 2013 - Olivier Cosquer - http://www.olivier-cosquer.com

 This file is part of ApocalypseZombie.

    ApocalypseZombie is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    ApocalypseZombie is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with ApocalypseZombie.  If not, see <http://www.gnu.org/licenses/>.
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
