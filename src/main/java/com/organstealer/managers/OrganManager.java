package com.organstealer.managers;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import com.organstealer.models.PlayerOrgans;
import com.organstealer.enums.Organ;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class OrganManager {
    
    private JavaPlugin plugin;
    private Map<UUID, PlayerOrgans> playerOrgans;
    private Random random;
    
    public OrganManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.playerOrgans = new HashMap<>();
        this.random = new Random();
    }
    
    public PlayerOrgans getPlayerOrgans(Player player) {
        return getPlayerOrgans(player.getUniqueId(), player.getName());
    }
    
    public PlayerOrgans getPlayerOrgans(UUID uuid, String name) {
        return playerOrgans.computeIfAbsent(uuid, k -> new PlayerOrgans(uuid, name));
    }
    
    public void stealOrgan(Player killer, Player victim) {
        PlayerOrgans killerOrgans = getPlayerOrgans(killer);
        PlayerOrgans victimOrgans = getPlayerOrgans(victim);
        
        Organ stolenOrgan;
        int stolenStrength;
        
        if (victimOrgans.getTotalKills() == 0) {
            stolenOrgan = Organ.values()[random.nextInt(Organ.values().length)];
            stolenStrength = victimOrgans.getOrganStrength(stolenOrgan);
        } else {
            stolenOrgan = victimOrgans.getStrongestOrgan();
            stolenStrength = victimOrgans.getOrganStrength(stolenOrgan);
        }
        
        killerOrgans.addOrganStrength(stolenOrgan, stolenStrength);
        victimOrgans.resetOrgan(stolenOrgan);
        killerOrgans.addKill();
        
        plugin.getLogger().info(killer.getName() + " stole " + stolenOrgan.getName() + " from " + victim.getName());
    }
    
    public void applyOrganEffects(Player player) {
        PlayerOrgans organs = getPlayerOrgans(player);
        
        applyHeartEffects(player, organs.getOrganStrength(Organ.HEART));
        applyEyesEffects(player, organs.getOrganStrength(Organ.EYES));
        applyLegsEffects(player, organs.getOrganStrength(Organ.LEGS));
        applyArmsEffects(player, organs.getOrganStrength(Organ.ARMS));
        applyBrainEffects(player, organs.getOrganStrength(Organ.BRAIN));
    }
    
    private void applyHeartEffects(Player player, int strength) {
        int level = strength / 10;
        if (level > 0) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, level - 1, true, false));
        }
    }
    
    private void applyEyesEffects(Player player, int strength) {
        int level = strength / 10;
        if (level > 0) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, true, false));
        }
    }
    
    private void applyLegsEffects(Player player, int strength) {
        int level = strength / 10;
        if (level > 0) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, level - 1, true, false));
        }
    }
    
    private void applyArmsEffects(Player player, int strength) {
        int level = strength / 10;
        if (level > 0) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, Integer.MAX_VALUE, level - 1, true, false));
        }
    }
    
    private void applyBrainEffects(Player player, int strength) {
        int level = strength / 10;
        if (level > 0) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, Integer.MAX_VALUE, level - 1, true, false));
        }
    }
    
    public void clearPlayerEffects(Player player) {
        player.removePotionEffect(PotionEffectType.REGENERATION);
        player.removePotionEffect(PotionEffectType.NIGHT_VISION);
        player.removePotionEffect(PotionEffectType.SPEED);
        player.removePotionEffect(PotionEffectType.STRENGTH);
        player.removePotionEffect(PotionEffectType.HASTE);
    }
}