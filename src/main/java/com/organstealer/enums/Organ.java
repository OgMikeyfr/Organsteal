package com.organstealer.enums;

import org.bukkit.Material;
import net.md_5.bungee.api.ChatColor;

public enum Organ {
    HEART("Heart", Material.REDSTONE, "🫀", ChatColor.RED, "Regeneration"),
    EYES("Eyes", Material.ENDER_PEARL, "👀", ChatColor.GRAY, "Night Vision & Awareness"),
    LEGS("Legs", Material.DIAMOND_BOOTS, "🦵", ChatColor.BLUE, "Speed & Mobility"),
    ARMS("Arms", Material.DIAMOND_CHESTPLATE, "💪", ChatColor.GOLD, "Strength & Knockback"),
    BRAIN("Brain", Material.NETHER_STAR, "🧠", ChatColor.LIGHT_PURPLE, "Cooldowns & Combat IQ");
    
    private final String name;
    private final Material material;
    private final String emoji;
    private final ChatColor color;
    private final String ability;
    
    Organ(String name, Material material, String emoji, ChatColor color, String ability) {
        this.name = name;
        this.material = material;
        this.emoji = emoji;
        this.color = color;
        this.ability = ability;
    }
    
    public String getName() {
        return name;
    }
    
    public Material getMaterial() {
        return material;
    }
    
    public String getEmoji() {
        return emoji;
    }
    
    public ChatColor getColor() {
        return color;
    }
    
    public String getAbility() {
        return ability;
    }
    
    public int getMaxStrength() {
        return 50;
    }
    
    public int getLevelFromStrength(int strength) {
        return strength / 10;
    }
}