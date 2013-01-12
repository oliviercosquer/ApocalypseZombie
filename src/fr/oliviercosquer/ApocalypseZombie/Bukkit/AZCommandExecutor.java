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
import fr.oliviercosquer.ApocalypseZombie.Stats.AZStatsManager;
import java.util.logging.Level;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

/**
 *
 * @author Olivier
 */
public class AZCommandExecutor implements CommandExecutor{

    private ApocalypseZombie plugin;
    private AZStatsManager statsManager;
    private Permission permission;

    public AZCommandExecutor(ApocalypseZombie plugin) {
        this.plugin = plugin;
        this.statsManager = plugin.getStatsManager();
        
        RegisteredServiceProvider<Permission> permProvider = plugin.getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        this.permission = permProvider.getProvider();
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
        String playerName;
        
        if(args.length == 1){
            String arg = args[0];
            
            if(arg.equalsIgnoreCase("global")){
                if (this.permission.has(sender, "apocalypseZombie.globalstats")) {
                    this.showGlobalStats(sender);
                }
            }else if(arg.equalsIgnoreCase("save")){
                if(this.permission.has(sender, "apocalypseZombie.save")){
                    this.statsManager.saveStats();
                    sender.sendMessage(ChatColor.GREEN + "Les statistiques sont sauvegardees!");
                }
            }else{
                if (this.permission.has(sender, "apocalypseZombie.stats")) {
                    if (this.statsManager.playerExist(arg)) {
                        this.displayPlayerStats(sender, arg);
                    } else {
                        this.playerDoesNotExist(sender, arg);
                    }
                }
            }
            return true;
        }else if(args.length == 0){ //sender stats
            if(sender instanceof Player){
                if (this.permission.has(sender, "apocalypseZombie.stats")) {
                    if (this.statsManager.playerExist(sender.getName())) {
                        this.displayPlayerStats(sender, sender.getName());
                    } else {
                        this.playerDoesNotExist(sender, sender.getName());
                    }
                }
            }else{
                sender.sendMessage("Cannot be use from the console!");
            }
            return true;
        }else{ //Usage
            return false;
        }
    }
    
    private void displayPlayerStats(CommandSender player,String playerName){
        if(this.statsManager.playerExist(playerName))
            player.sendMessage(this.statsManager.getPlayerStats(playerName));
        else
            this.playerDoesNotExist(player, playerName);
    }
    
    private void showGlobalStats(CommandSender player){
            player.sendMessage(this.statsManager.getGlobalStats());
    }
    
    private void playerDoesNotExist(CommandSender player,String playerName){
        player.sendMessage(ChatColor.RED+" "+playerName+" n'a pas de statistiques.");
    }
}
