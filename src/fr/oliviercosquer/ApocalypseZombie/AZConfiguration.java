/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.oliviercosquer.ApocalypseZombie;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Olivier
 */
public class AZConfiguration {

    private ApocalypseZombie plugin;
    private int zombieDamage;
    private int zombieHealth;
    public int zombieSpeed;
    private ItemStack zombieItemDrop;
    private int spawnFrequency;
    private int spawnLimit;
    private int dropRate;
    private boolean allowCustomDrop;

    public AZConfiguration(ApocalypseZombie zaPlugin) {
        this.plugin = zaPlugin;
    }

    public void loadConfiguration() {
        //Load the config file        
        FileConfiguration config = this.plugin.getConfig();

        //Read the keys
        this.zombieDamage = config.getInt("ZombieDamage");
        this.zombieHealth = config.getInt("ZombieHealth");
        this.zombieItemDrop = new ItemStack(Material.getMaterial(config.getInt("ZombieItemDrop")), 1);
        this.spawnFrequency = config.getInt("SpawnFrenquency");
        this.spawnLimit = config.getInt("SpawnLimit");
        this.allowCustomDrop = config.getBoolean("AllowCustomDrop");
        this.dropRate = config.getInt("CustomDropRate");
        this.zombieItemDrop = new ItemStack(config.getInt("ItemDrop.id"),1,(short)config.getInt("ItemDrop.damage"));
        this.zombieSpeed = config.getInt("ZombieSpeed");
        
        if(this.dropRate > 100)
            this.dropRate = 100;
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

    public ItemStack getZombieItemDrop() {
        return zombieItemDrop;
    }

    public int getZombieSpeed() {
        return zombieSpeed;
    }
    
    
}
