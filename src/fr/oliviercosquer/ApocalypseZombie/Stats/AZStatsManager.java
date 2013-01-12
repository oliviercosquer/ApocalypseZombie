/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.oliviercosquer.ApocalypseZombie.Stats;

import fr.oliviercosquer.ApocalypseZombie.ApocalypseZombie;
import fr.oliviercosquer.ApocalypseZombie.Configuration.AZConfiguration;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
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

        //Load Player stats data
        this.statsFile = new File(this.plugin.getDataFolder(), "playerStats");

        if (!this.statsFile.exists()) {
            try {
                this.statsFile.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(AZConfiguration.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        this.loadStats();
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
        String msg = "=== Apoclypse Zombie ===+\n";
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
