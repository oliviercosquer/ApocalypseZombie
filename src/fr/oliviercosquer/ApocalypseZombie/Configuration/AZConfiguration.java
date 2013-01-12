/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.oliviercosquer.ApocalypseZombie.Configuration;

import fr.oliviercosquer.ApocalypseZombie.ApocalypseZombie;
import java.util.ArrayList;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Olivier
 */
public class AZConfiguration {

    private ApocalypseZombie plugin;
    
    //Configuration values
    private int maxZombieSpawn;
    private int spawnFrenquency;
    private int zombieDamage;
    private int zombieHealth;
    public int zombieSpeed;
    private int spawnFrequency;
    private int spawnLimit;
    private int foodChange;
    private int dropRate;
    private boolean allowCustomDrop;
    private ArrayList<AZItemDrop> dropItemList;

    public AZConfiguration(ApocalypseZombie zaPlugin) {
        this.plugin = zaPlugin;
        this.dropItemList = new ArrayList<AZItemDrop>();
    }

    public void loadConfiguration() {
        //Load the config file        
        FileConfiguration config = this.plugin.getConfig();

        //Read the keys
        this.maxZombieSpawn = config.getInt("MaxZombieSpawn");
        this.zombieDamage = config.getInt("ZombieDamage");
        this.zombieHealth = config.getInt("ZombieHealth");
        this.spawnFrequency = config.getInt("SpawnFrenquency");
        this.spawnLimit = config.getInt("SpawnLimit");
        this.allowCustomDrop = config.getBoolean("AllowCustomDrop");
        this.dropRate = config.getInt("CustomDropRate");
        this.zombieSpeed = config.getInt("ZombieSpeed");
        this.spawnFrenquency = config.getInt("SpawnFrenquency");
        this.foodChange = config.getInt("FoodChange");

        //Retrieve all the drop item
        for (String key : config.getConfigurationSection("ItemDrop").getKeys(false)) {
            ItemStack tmpItem = new ItemStack(config.getInt("ItemDrop." + key + ".id"), 1, (short) config.getInt("ItemDrop." + key + ".damaged"));
            int tmpDropRate = config.getInt("ItemDrop." + key + ".dropRate");

            this.dropItemList.add(new AZItemDrop(tmpItem, tmpDropRate));
        }

        if (this.dropRate > 100) {
            this.dropRate = 100;
        }
    }

    public boolean isAllowCustomDrop() {
        return allowCustomDrop;
    }

    public int getDropRate() {
        return dropRate;
    }

    public int getSpawnFrequency() {
        return spawnFrequency;
    }

    public int getSpawnLimit() {
        return spawnLimit;
    }

    public int getZombieDamage() {
        return zombieDamage;
    }

    public int getZombieHealth() {
        return zombieHealth;
    }

    public int getZombieSpeed() {
        return zombieSpeed;
    }

    public ArrayList<AZItemDrop> getDropItemList() {
        return dropItemList;
    }

    public int getMaxZombieSpawn() {
        return maxZombieSpawn;
    }

    public int getFoodChange() {
        return foodChange;
    }

    public int getSpawnFrenquency() {
        return spawnFrenquency;
    }
    
    
}
