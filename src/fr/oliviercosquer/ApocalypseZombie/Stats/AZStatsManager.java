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

import fr.oliviercosquer.ApocalypseZombie.ApocalypseZombie;
import fr.oliviercosquer.ApocalypseZombie.Configuration.AZConfiguration;
import java.io.*;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Olivier
 */
public class AZStatsManager {
    
    private ApocalypseZombie plugin;
    
    private File statsFile;
    private HashMap<String, AZPlayerStats> playerList;

    public AZStatsManager(ApocalypseZombie plugin) {
        this.plugin = plugin;
        
        this.playerList = new HashMap<String, AZPlayerStats>();
    }
    
    public void init(){
        //Load Player stats data
        this.statsFile = new File(this.plugin.getDataFolder(), "playerStats");

        if (!this.statsFile.exists()) {
            try {
                this.statsFile.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(AZConfiguration.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void loadStats(){        
        try {
            FileInputStream fileInput = null;
            ObjectInputStream objectInput;
            
            try {
                //Open the file
                fileInput = new FileInputStream(this.statsFile);           
            } catch (FileNotFoundException ex) {
                Logger.getLogger(AZStatsManager.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            //Loading data
            objectInput = new ObjectInputStream(fileInput);
            try {
                this.playerList = (HashMap<String, AZPlayerStats>)objectInput.readObject();
                objectInput.close();
                fileInput.close();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(AZStatsManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IOException ex) {
            Logger.getLogger(AZStatsManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void saveStats(){
        try {
            FileOutputStream fileOutput = null;
            ObjectOutputStream objectOutput;
            
            try {
                //Open the file
                fileOutput = new FileOutputStream(this.statsFile);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(AZStatsManager.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            //Writting data
            objectOutput = new ObjectOutputStream(fileOutput);
            objectOutput.writeObject(this.playerList);
            objectOutput.close();
            fileOutput.close();
        } catch (IOException ex) {
            Logger.getLogger(AZStatsManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean addPlayer(String playerName,AZPlayerStats newPlayer){
        if(this.playerExist(playerName)){
            return false;
        }else{
            this.playerList.put(playerName, newPlayer);
            return true;
        }
    }
    
    public boolean playerExist(String playerName){
        return(this.playerList.containsKey(playerName));
    }
    
    public AZPlayerStats getPlayer(String playerName){
        return(this.playerList.get(playerName));
    }
    
    public String getPlayerStats(String playerName){
        String msg = "Statistiques de "+playerName+"\n";
        
        AZPlayerStats stats= this.getPlayer(playerName);
        msg += "Zombies tuees: "+stats.getZkill()+"\n";
        msg += "Morts: "+stats.getDeath();
        
        return msg;
    }
    
    public String getGlobalStats(){
        String msg = "=== Apocalypse Zombie ===+\n";
        int totalZKill = 0;
        int totalDeath = 0;
        
        for(String key : this.playerList.keySet()){
            AZPlayerStats tmpPlayer = this.playerList.get(key);
            totalZKill += tmpPlayer.getZkill();
            totalDeath += tmpPlayer.getDeath();
        }
        
        msg += "Total de zombie tues: "+totalZKill+"\n";
        msg += "Total de joueurs morts: "+totalDeath+"\n";
        return msg;
    }
}
