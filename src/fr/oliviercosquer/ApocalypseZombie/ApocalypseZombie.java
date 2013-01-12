package fr.oliviercosquer.ApocalypseZombie;

import fr.oliviercosquer.ApocalypseZombie.Bukkit.AZCommandExecutor;
import fr.oliviercosquer.ApocalypseZombie.Bukkit.AZServerListener;
import fr.oliviercosquer.ApocalypseZombie.Configuration.AZConfiguration;
import fr.oliviercosquer.ApocalypseZombie.Stats.AZPlayerStats;
import fr.oliviercosquer.ApocalypseZombie.Stats.AZStatsManager;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
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
    private AZStatsManager statsManager;

    @Override
    public void onDisable() {
    }

    @Override
    public void onEnable() {
        //Initialize object
        this.EntityListener = new AZServerListener(this);
        this.azConfig = new AZConfiguration(this);
        this.statsManager = new AZStatsManager(this);        
        this.cmdExecutor = new AZCommandExecutor(this);
        
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(this.EntityListener, this);

        //If azConfig.yml doesn't exist, create it
        this.saveDefaultConfig();
        this.azConfig.loadConfiguration();
        this.killCreature();    
        this.setSpawnLimitAndFequency();
        
        //Registering commands
        this.getCommand("az").setExecutor(this.cmdExecutor);
        
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
