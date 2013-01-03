package fr.oliviercosquer.ApocalypseZombie;

import fr.oliviercosquer.ApocalypseZombie.ApocalypseZombie;
import org.bukkit.ChatColor;
import org.bukkit.EntityEffect;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class AZServerListener implements Listener {

    public static ApocalypseZombie plugin;

    public AZServerListener(ApocalypseZombie instance) {
        plugin = instance;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        EntityType entType = event.getEntityType();
        Entity ent = event.getEntity();
        World world = ent.getWorld();

        //If it's not a zombie, change the entity type by a zombie
        if (entType != EntityType.ZOMBIE) {

            //Spawn a new zombie at the ent location
            world.spawnEntity(ent.getLocation(), EntityType.ZOMBIE);
            //Destroy the old entity            
            ent.remove();
        }

    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onZombieSpawn(CreatureSpawnEvent event) {

        LivingEntity ent = event.getEntity();

        //Zombie can't pickup object
        ent.setCanPickupItems(false);

        //Make zombie go faster and deal more damage
        ent.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2147483647, 2));

        //Setting parameters from configuration file
        ent.setHealth(this.plugin.config.getZombieHealth());


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
}