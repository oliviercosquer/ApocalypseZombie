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
package fr.oliviercosquer.ApocalypseZombie.Bukkit;

import fr.oliviercosquer.ApocalypseZombie.ApocalypseZombie;
import fr.oliviercosquer.ApocalypseZombie.Configuration.AZItemDrop;
import fr.oliviercosquer.ApocalypseZombie.Stats.AZPlayerStats;
import fr.oliviercosquer.ApocalypseZombie.Stats.AZStatsManager;
import java.util.ArrayList;
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
    private boolean lastSpawnIsZombie = false;
    private Random dropRate = new Random();
    private int amountZombies;
    private int amountMonsters;
    private ArrayList<EntityType> allowedCreature;
    private ArrayList<EntityType> standardCreature;

    public AZServerListener(ApocalypseZombie instance) {
        this.plugin = instance;
        this.allowedCreature  = new ArrayList<EntityType>();
        this.standardCreature  = new ArrayList<EntityType>();
        
        //Define allowed Creature
        this.allowedCreature.add(EntityType.CHICKEN);
        this.allowedCreature.add(EntityType.COW);
        this.allowedCreature.add(EntityType.IRON_GOLEM);
        this.allowedCreature.add(EntityType.OCELOT);
        this.allowedCreature.add(EntityType.PIG);
        this.allowedCreature.add(EntityType.SHEEP);
        this.allowedCreature.add(EntityType.VILLAGER);
        this.allowedCreature.add(EntityType.WOLF);
        this.allowedCreature.add(EntityType.ZOMBIE);
        this.allowedCreature.add(EntityType.PLAYER);
        this.allowedCreature.add(EntityType.SQUID);
        
        //Define standard Creature
        this.standardCreature.add(EntityType.CHICKEN);
        this.standardCreature.add(EntityType.COW);
        this.standardCreature.add(EntityType.SHEEP);
        this.standardCreature.add(EntityType.PIG);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        EntityType entType = event.getEntityType();
        Entity ent = event.getEntity();
        World world = ent.getWorld();

        //If it's not a zombie and not a player , change the entity type by a zombie
        if (entType != EntityType.ZOMBIE && entType != EntityType.PLAYER) {

            //If amount zombie not capped
            if (this.amountZombies < this.plugin.getAzConfig().getMaxZombieSpawn() && !this.lastSpawnIsZombie) {
                //Spawn a new zombie at the ent location
                world.spawnEntity(ent.getLocation(), EntityType.ZOMBIE);
                //Destroy the old entity            
                ent.remove();
                this.lastSpawnIsZombie = true;
                this.amountZombies++;
            } else {
                this.amountZombies = 0;

                //Count zombie entities
                for (Entity tmpEnt : world.getEntities()) {
                    if (tmpEnt.getType() == EntityType.ZOMBIE) {
                        this.amountZombies++;
                    }
                }
                
                if(!this.plugin.getAzConfig().isAnimalsCanSpawn()){
                    event.setCancelled(true);
                }else{
                    this.lastSpawnIsZombie = false;
                    
                    //If it's not a sheep, pig, etc...
                    if(!this.allowedCreature.contains(entType) && this.amountMonsters < this.plugin.getAzConfig().getMaxMonsterSpawn()){
                        world.spawnEntity(ent.getLocation(), this.standardCreature.get(dropRate.nextInt(this.standardCreature.size())));                        
                        ent.remove();
                    }else{
                        this.amountMonsters = 0;
                        
                        for(Entity tmpEnt : world.getEntities()){
                            if(this.allowedCreature.contains(tmpEnt.getType()))
                                this.amountMonsters++;
                        }
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onZombieSpawn(CreatureSpawnEvent event) {

        LivingEntity ent = event.getEntity();

        //If amount zombie not capped
        if (this.amountZombies < ent.getWorld().getMonsterSpawnLimit() || event.getSpawnReason() != SpawnReason.EGG) {
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