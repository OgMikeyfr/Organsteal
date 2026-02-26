package com.organstealer.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import com.organstealer.OrganStealer;
import com.organstealer.managers.OrganManager;
import com.organstealer.models.PlayerOrgans;
import com.organstealer.enums.Organ;
import java.util.ArrayList;
import java.util.List;

public class OrganGUI {
    
    private OrganStealer plugin;
    private OrganManager organManager;
    private Player player;
    
    public OrganGUI(OrganStealer plugin, OrganManager organManager, Player player) {
        this.plugin = plugin;
        this.organManager = organManager;
        this.player = player;
    }
    
    public void open(Player player) {
        Inventory gui = Bukkit.createInventory(null, 27, "🩸 YOUR ORGANS 🫀");
        
        PlayerOrgans organs = organManager.getPlayerOrgans(player);
        
        int slot = 0;
        for (Organ organ : Organ.values()) {
            ItemStack item = createOrganItem(organ, organs.getOrganStrength(organ));
            gui.setItem(slot, item);
            slot += 2;
        }
        
        ItemStack info = new ItemStack(Material.WRITABLE_BOOK);
        ItemMeta infoMeta = info.getItemMeta();
        infoMeta.setDisplayName("📊 STATS");
        List<String> lore = new ArrayList<>();
        lore.add("Total Kills: " + organs.getTotalKills());
        lore.add("Total Power: " + organs.getTotalPower());
        lore.add("");
        lore.add("Each organ goes from Level 0 to Level 5");
        infoMeta.setLore(lore);
        info.setItemMeta(infoMeta);
        gui.setItem(22, info);
        
        player.openInventory(gui);
    }
    
    private ItemStack createOrganItem(Organ organ, int strength) {
        ItemStack item = new ItemStack(organ.getMaterial());
        ItemMeta meta = item.getItemMeta();
        
        int level = organ.getLevelFromStrength(strength);
        
        meta.setDisplayName(organ.getEmoji() + " " + organ.getName() + " - Level " + level);
        
        List<String> lore = new ArrayList<>();
        lore.add("Ability: " + organ.getAbility());
        lore.add("");
        lore.add("Strength: " + strength + "/50");
        lore.add("Progress: " + getProgressBar(strength, 50));
        lore.add("");
        
        if (level == 0) {
            lore.add("Inactive - Get kills to activate");
        } else {
            lore.add("Status: ACTIVE");
        }
        
        meta.setLore(lore);
        item.setItemMeta(meta);
        
        return item;
    }
    
    private String getProgressBar(int current, int max) {
        int filled = (current * 20) / max;
        StringBuilder bar = new StringBuilder();
        
        for (int i = 0; i < 20; i++) {
            if (i < filled) {
                bar.append("█");
            } else {
                bar.append("░");
            }
        }
        
        return bar.toString();
    }
}