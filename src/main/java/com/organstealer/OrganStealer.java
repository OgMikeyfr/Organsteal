package com.organstealer;

import org.bukkit.plugin.java.JavaPlugin;
import com.organstealer.listeners.PvPListener;
import com.organstealer.listeners.CommandListener;
import com.organstealer.managers.OrganManager;
import com.organstealer.managers.DataManager;

public class OrganStealer extends JavaPlugin {
    
    private static OrganStealer instance;
    private OrganManager organManager;
    private DataManager dataManager;
    
    @Override
    public void onEnable() {
        instance = this;
        
        getLogger().info("🩸 ORGAN SMP - INITIALIZING 🫀");
        
        this.dataManager = new DataManager(this);
        this.organManager = new OrganManager(this);
        
        dataManager.loadAllPlayerData();
        
        getServer().getPluginManager().registerEvents(new PvPListener(this), this);
        
        getCommand("organ").setExecutor(new CommandListener(this));
        getCommand("organstats").setExecutor(new CommandListener(this));
        getCommand("organadmin").setExecutor(new CommandListener(this));
        
        getLogger().info("✅ ORGAN SMP LOADED - POWER IS STEALABLE!");
    }
    
    @Override
    public void onDisable() {
        if (dataManager != null) {
            dataManager.saveAllPlayerData();
        }
        getLogger().info("💀 ORGAN SMP DISABLED");
    }
    
    public static OrganStealer getInstance() {
        return instance;
    }
    
    public OrganManager getOrganManager() {
        return organManager;
    }
    
    public DataManager getDataManager() {
        return dataManager;
    }
}