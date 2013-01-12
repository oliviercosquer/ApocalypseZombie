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
package fr.oliviercosquer.ApocalypseZombie;

import fr.oliviercosquer.ApocalypseZombie.Bukkit.AZCommandExecutor;
import fr.oliviercosquer.ApocalypseZombie.Bukkit.AZServerListener;
import fr.oliviercosquer.ApocalypseZombie.Configuration.AZConfiguration;
import fr.oliviercosquer.ApocalypseZombie.Stats.AZStatsManager;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ApocalypseZombie extends JavaPlugin {

    public static String name;
    public static String version;
    public static ApocalypseZombie plugin;
    
    private AZServerListener EntityListener;
    private AZCommandExecutor cmdExecutor;
    private AZConfiguration azConfig;
    private final AZStatsManager statsManager = new AZStatsManager(this);

    @Override
    public void onDisable() {
    }

    @Override
    public void onEnable() {
        //Initialize object
        this.EntityListener = new AZServerListener(this);
        this.azConfig = new AZConfiguration(this);  
        this.cmdExecutor = new AZCommandExecutor(this);
        
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(this.EntityListener, this);

        //If azConfig.yml doesn't exist, create it
        this.saveDefaultConfig();
        this.azConfig.loadConfiguration();
        this.killCreature();
        
        //Load stats
        this.statsManager.init();
        this.statsManager.loadStats();
        
        //Registering commands
        this.getCommand("az").setExecutor(this.cmdExecutor);
        
        //Schedule stats saving
        this.getServer().getScheduler().runTaskTimer(this, new Runnable(){
            @Override
            public void run(){
                statsManager.saveStats();
            }
        },60L,200L);
        
    }

    private void killCreature() {
        //Replace al the entities by zombie
        for (World w : getServer().getWorlds()) {
            for (Entity ent : w.getEntities()) {
                if (ent.getType() != EntityType.PLAYER && ent.getType() != EntityType.ZOMBIE) {
                    w.spawnEntity(ent.getLocation(), EntityType.ZOMBIE);
                    
                    //Destroy the old entity                      
                    ent.remove();
                }
            }
        }

        //Setting damage heal and other to the zombies
        for (World w : getServer().getWorlds()) {
            for (LivingEntity ent : w.getLivingEntities()) {
                if (ent.getType() != EntityType.PLAYER && ent.getType() == EntityType.ZOMBIE) {
                    //Changing zombie parameters
                    ent.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2147483647, 2));
                    //Setting parameters from configuration file
                    ent.setMaxHealth(this.azConfig.getZombieHealth());
                    ent.setHealth(this.azConfig.getZombieHealth());
                }
            }
        }
    }
    
    private void setSpawnLimitAndFequency(){
        for(World world : this.getServer().getWorlds()){
            world.setAnimalSpawnLimit(this.azConfig.getMaxZombieSpawn());
            world.setMonsterSpawnLimit(this.azConfig.getMaxZombieSpawn());
            world.setTicksPerAnimalSpawns(this.azConfig.getSpawnFrequency());
            world.setTicksPerMonsterSpawns(this.azConfig.getSpawnFrequency());
        }
    }

    public AZConfiguration getAzConfig() {
        return azConfig;
    }

    public AZStatsManager getStatsManager() {
        return statsManager;
    }
    
    
}
