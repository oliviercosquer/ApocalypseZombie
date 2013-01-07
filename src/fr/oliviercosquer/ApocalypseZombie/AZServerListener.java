package fr.oliviercosquer.ApocalypseZombie;

import java.util.Random;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Zombie;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class AZServerListener implements Listener {

    public static ApocalypseZombie plugin;
    private Random dropRate = new Random();

    public AZServerListener(ApocalypseZombie instance) {
        plugin = instance;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        EntityType entType = event.getEntityType();
        Entity ent = event.getEntity();
        World world = ent.getWorld();
        
        //If it's not a zombie and not a player , change the entity type by a zombie
        if (entType != EntityType.ZOMBIE && entType != EntityType.PLAYER) {

            //If amount zombie not capped
            if (this.zombieCanSpawn(world)) {
                //Spawn a new zombie at the ent location
                world.spawnEntity(ent.getLocation(), EntityType.ZOMBIE);
                //Destroy the old entity            
                ent.remove();
            } else {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onZombieSpawn(CreatureSpawnEvent event) {
        
        LivingEntity ent = event.getEntity();

        //If amount zombie not capped
        if (this.zombieCanSpawn(ent.getWorld())) {
            //Zombie can't pickup object
            ent.setCanPickupItems(false);
            //Make zombie go faster and deal more damage
            ent.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2147483647, 2));

            //Setting parameters from configuration file
            ent.setMaxHealth(this.plugin.config.getZombieHealth());
            ent.setHealth(this.plugin.config.getZombieHealth());
        } else {
            event.setCancelled(true);
        }


    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityDamage(EntityDamageEvent event) {
        EntityType entType = event.getEntityType();
        Entity ent = event.getEntity();

        //Cancel if zombie damaged by a fire tick
        if (entType == EntityType.ZOMBIE && event.getCause() == DamageCause.FIRE_TICK) {
            event.setCancelled(true);
            ent.setFireTicks(0);
        }

    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        //Damage the player by the base damage and the config damage if it's a zombie
        if (event.getDamager().getType() == EntityType.ZOMBIE) {
            event.setDamage(this.plugin.config.getZombieDamage());
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityDeathEvent(EntityDeathEvent event) {
        if (this.plugin.config.isAllowCustomDrop()) {
            //Delete all the drop
            event.getDrops().clear();

            for (AZItemDrop item : this.plugin.config.getDropItemList()) {
                //Drop rate
                if (this.dropRate.nextInt(100) < item.getDropRate()) {
                    //Add the drop item to the dropList
                    event.getDrops().add(item.getItem());
                }
            }
        }
    }
    
    private boolean zombieCanSpawn(World world){
        int amountZombie = 0;

        //Count zombie entities
        for (Entity tmpEnt : world.getEntities()) {
            if (tmpEnt.getType() == EntityType.ZOMBIE) {
                amountZombie++;
            }
        }
        
        return amountZombie < world.getMonsterSpawnLimit();
    }
}