package fr.oliviercosquer.ApocalypseZombie;

import java.lang.reflect.Method;
import net.minecraft.server.v1_4_6.EntityTypes;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ApocalypseZombie extends JavaPlugin {

    public final AZServerListener EntityListener = new AZServerListener(this);
    protected AZLogger log;
    public static String name;
    public static String version;
    public static ApocalypseZombie plugin;
    public AZConfiguration config = new AZConfiguration(this);

    @Override
    public void onLoad() {
        this.config.loadConfiguration();
        this.killCreature();
        this.setSpawnLimit();
    }

    @Override
    public void onDisable() {
    }

    @Override
    public void onEnable() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(this.EntityListener, this);
        this.log = new AZLogger(this);

        //If config.yml doesn't exist, create it
        this.saveDefaultConfig();
        this.config.loadConfiguration();
        this.killCreature();    
        this.setSpawnLimit();
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
                    ent.setMaxHealth(this.config.getZombieHealth());
                    ent.setHealth(this.config.getZombieHealth());
                }
            }
        }
    }
    
    private void setSpawnLimit(){
        for(World world : this.getServer().getWorlds()){
            world.setAnimalSpawnLimit(this.config.getMaxZombieSpawn());
            world.setMonsterSpawnLimit(this.config.getMaxZombieSpawn());
        }
    }
}
