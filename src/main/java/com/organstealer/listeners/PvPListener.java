package com.organstealer.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import net.md_5.bungee.api.ChatColor;
import com.organstealer.OrganStealer;
import com.organstealer.managers.OrganManager;
import com.organstealer.managers.DataManager;

public class PvPListener implements Listener {
    
    private OrganStealer plugin;
    private OrganManager organManager;
    private DataManager dataManager;
    
    public PvPListener(OrganStealer plugin) {
        this.plugin = plugin;
        this.organManager = plugin.getOrganManager();
        this.dataManager = plugin.getDataManager();
    }
    
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = victim.getKiller();
        
        if (killer == null || !killer.isOnline()) {
            return;
        }
        
        organManager.stealOrgan(killer, victim);
        
        victim.sendMessage(ChatColor.RED + "💀 Your organs were stolen by " + ChatColor.DARK_RED + killer.getName());
        killer.sendMessage(ChatColor.GOLD + "🩸 You stole an organ from " + ChatColor.YELLOW + victim.getName());
        
        organManager.clearPlayerEffects(victim);
        organManager.applyOrganEffects(victim);
        
        organManager.clearPlayerEffects(killer);
        organManager.applyOrganEffects(killer);
        
        dataManager.savePlayerData(victim);
        dataManager.savePlayerData(killer);
    }
}