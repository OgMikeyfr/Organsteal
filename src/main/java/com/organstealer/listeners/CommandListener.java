package com.organstealer.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import net.md_5.bungee.api.ChatColor;
import com.organstealer.OrganStealer;
import com.organstealer.managers.OrganManager;
import com.organstealer.managers.DataManager;
import com.organstealer.models.PlayerOrgans;
import com.organstealer.enums.Organ;
import com.organstealer.gui.OrganGUI;

public class CommandListener implements CommandExecutor {
    
    private OrganStealer plugin;
    private OrganManager organManager;
    private DataManager dataManager;
    
    public CommandListener(OrganStealer plugin) {
        this.plugin = plugin;
        this.organManager = plugin.getOrganManager();
        this.dataManager = plugin.getDataManager();
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players!");
            return true;
        }
        
        Player player = (Player) sender;
        
        if (cmd.getName().equalsIgnoreCase("organ")) {
            return handleOrganCommand(player, args);
        } else if (cmd.getName().equalsIgnoreCase("organstats")) {
            return handleStatsCommand(player, args);
        } else if (cmd.getName().equalsIgnoreCase("organadmin")) {
            return handleAdminCommand(player, args);
        }
        
        return false;
    }
    
    private boolean handleOrganCommand(Player player, String[] args) {
        new OrganGUI(plugin, organManager, player).open(player);
        return true;
    }
    
    private boolean handleStatsCommand(Player player, String[] args) {
        Player targetPlayer = player;
        
        if (args.length > 0) {
            targetPlayer = Bukkit.getPlayer(args[0]);
            if (targetPlayer == null) {
                player.sendMessage(ChatColor.RED + "Player not found!");
                return true;
            }
        }
        
        PlayerOrgans organs = organManager.getPlayerOrgans(targetPlayer);
        
        player.sendMessage("\n" + ChatColor.GOLD + "╔════════════════════════════════╗");
        player.sendMessage(ChatColor.GOLD + "║    " + ChatColor.RED + "ORGAN STATS" + ChatColor.GOLD + "        ║");
        player.sendMessage(ChatColor.GOLD + "╚════════════════════════════════╝" + ChatColor.RESET);
        player.sendMessage(ChatColor.YELLOW + "Player: " + ChatColor.WHITE + targetPlayer.getName());
        player.sendMessage(ChatColor.YELLOW + "Total Kills: " + ChatColor.WHITE + organs.getTotalKills());
        player.sendMessage(ChatColor.YELLOW + "Total Power: " + ChatColor.WHITE + organs.getTotalPower());
        player.sendMessage("");
        
        for (Organ organ : Organ.values()) {
            int strength = organs.getOrganStrength(organ);
            int level = organ.getLevelFromStrength(strength);
            player.sendMessage(organ.getColor() + organ.getEmoji() + " " + organ.getName() + ChatColor.RESET + " | Level: " + level + " | Strength: " + strength);
        }
        
        player.sendMessage("");
        return true;
    }
    
    private boolean handleAdminCommand(Player player, String[] args) {
        if (!player.hasPermission("organstealer.admin")) {
            player.sendMessage(ChatColor.RED + "You don't have permission!");
            return true;
        }
        
        if (args.length == 0) {
            player.sendMessage(ChatColor.YELLOW + "/organadmin reset <player> - Reset player organs");
            player.sendMessage(ChatColor.YELLOW + "/organadmin set <player> <organ> <strength> - Set organ strength");
            return true;
        }
        
        if (args[0].equalsIgnoreCase("reset") && args.length >= 2) {
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                player.sendMessage(ChatColor.RED + "Player not found!");
                return true;
            }
            
            PlayerOrgans organs = organManager.getPlayerOrgans(target);
            for (Organ organ : Organ.values()) {
                organs.resetOrgan(organ);
            }
            dataManager.savePlayerData(target);
            player.sendMessage(ChatColor.GREEN + "✅ Reset organs for " + target.getName());
            return true;
        }
        
        return false;
    }
}