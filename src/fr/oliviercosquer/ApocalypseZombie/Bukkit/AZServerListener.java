package fr.oliviercosquer.ApocalypseZombie.Bukkit;

import fr.oliviercosquer.ApocalypseZombie.ApocalypseZombie;
import fr.oliviercosquer.ApocalypseZombie.Configuration.AZItemDrop;
import fr.oliviercosquer.ApocalypseZombie.Stats.AZPlayerStats;
import fr.oliviercosquer.ApocalypseZombie.Stats.AZStatsManager;
import java.util.Random;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class AZServerListener implements Listener {

    private ApocalypseZombie plugin;
    private Random dropRate = new Random();

    public AZServerListener(ApocalypseZombie instance) {
        this.plugin = instance;
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
        if (this.zombieCanSpawn(ent.getWorld()) || event.getSpawnReason() != SpawnReason.EGG) {
            //Zombie can't pickup object
            ent.setCanPickupItems(false);
            //Make zombie go faster and deal more damage
            ent.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2147483647, 2));

            //Setting parameters from getAzConfig()uration file
            ent.setMaxHealth(this.plugin.getAzConfig().getZombieHealth());
            ent.setHealth(this.plugin.getAzConfig().getZombieHealth());
        }else {
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
        //Damage the player by the base damage and the getAzConfig() damage if it's a zombie
        if (event.getDamager().getType() == EntityType.ZOMBIE) {
            event.setDamage(this.plugin.getAzConfig().getZombieDamage());
            
            
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityDeathEvent(EntityDeathEvent event) {
        if (this.plugin.getAzConfig().isAllowCustomDrop() && event.getEntity().getType() == EntityType.ZOMBIE) {
            //Delete all the drop
            event.getDrops().clear();

            for (AZItemDrop item : this.plugin.getAzConfig().getDropItemList()) {
                //Drop rate
                if (this.dropRate.nextInt(100) < item.getDropRate()) {
                    //Add the drop item to the dropList
                    event.getDrops().add(item.getItem());
                }
            }
        }

        //Collecting stats
        if (event.getEntity().getType() == EntityType.ZOMBIE) {
            Player killer = event.getEntity().getKiller();
            
            //if there is a killer
            if (killer != null) {
                //if the killer is a player
                if (killer.getType() == EntityType.PLAYER) {                    
                    this.collectStats(killer, true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerDeathEvent(PlayerDeathEvent event) {
        Player killer = event.getEntity().getKiller();
        
        //Collecting stats
        if (killer != null) {
            //If there is a killer and it's a zombie
            if (killer instanceof Zombie) {
                this.collectStats(event.getEntity(), false);
            }
        }
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onFoodLevelChange(FoodLevelChangeEvent event){
        Entity ent = event.getEntity();
        
        if(ent.getType() == EntityType.PLAYER){
            //Change FoodLevel
            ((Player)ent).setFoodLevel(this.plugin.getAzConfig().getFoodChange());
            event.setCancelled(true);
        }
    }

    private boolean zombieCanSpawn(World world) {
        int amountZombie = 0;

        //Count zombie entities
        for (Entity tmpEnt : world.getEntities()) {
            if (tmpEnt.getType() == EntityType.ZOMBIE) {
                amountZombie++;
            }
        }

        return amountZombie < world.getMonsterSpawnLimit();
    }
    
    private void collectStats(Player player, boolean killDeath){
        AZStatsManager statsManager = this.plugin.getStatsManager();
        AZPlayerStats playerStat = null;
        
        //If player doesn't exist, add him to the list
        if(statsManager.playerExist(player.getName())){
            playerStat = statsManager.getPlayer(player.getName());
        }else{
            playerStat = new AZPlayerStats(0,0);
            statsManager.addPlayer(player.getName(), playerStat);
        }
        
        //If zombie kill
        if(killDeath){
            playerStat.setZkill(playerStat.getZkill() + 1);
        }else{ //If death
            playerStat.setDeath(playerStat.getDeath() + 1);
        }
    }
}