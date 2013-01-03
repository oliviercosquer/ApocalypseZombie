/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.oliviercosquer.ApocalypseZombie;

import java.io.File;
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
    private ItemStack zombieItemDrop;
    private int spawnFrequency;
    private int spawnLimit;

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
    }

    public int getSpawnFrequency() {
        return spawnFrequency;
    }

    public void setSpawnFrequency(int spawnFrequency) {
        this.spawnFrequency = spawnFrequency;
    }

    public int getSpawnLimit() {
        return spawnLimit;
    }

    public void setSpawnLimit(int spawnLimit) {
        this.spawnLimit = spawnLimit;
    }

    public int getZombieDamage() {
        return zombieDamage;
    }

    public void setZombieDamage(int zombieDamage) {
        this.zombieDamage = zombieDamage;
    }

    public int getZombieHealth() {
        return zombieHealth;
    }

    public void setZombieHealth(int zombieHealth) {
        this.zombieHealth = zombieHealth;
    }

    public ItemStack getZombieItemDrop() {
        return zombieItemDrop;
    }

    public void setZombieItemDrop(ItemStack zombieItemDrop) {
        this.zombieItemDrop = zombieItemDrop;
    }
}
