package uk.codingbadgers.badmin;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;

import lombok.Getter;
import uk.codingbadgers.badmin.command.BanCommand;
import uk.codingbadgers.badmin.command.CheckBanCommand;
import uk.codingbadgers.badmin.command.KickCommand;
import uk.codingbadgers.badmin.command.TempBanCommand;
import uk.codingbadgers.badmin.command.UnbanCommand;
import uk.codingbadgers.badmin.command.WarnCommand;
import uk.codingbadgers.badmin.database.DatabaseHandler;
import uk.codingbadgers.badmin.exception.AdminException;
import uk.codingbadgers.badmin.exception.ConfigException;
import uk.codingbadgers.badmin.listeners.EventListener;
import uk.codingbadgers.badmin.manager.BanManager;
import uk.codingbadgers.badmin.manager.WarningManager;

import net.md_5.bungee.api.plugin.Plugin;

public class bAdmin extends Plugin {

	public static final int CURRENT_CONFIG_VERSION = 0x01;
	public static final String MOJANG_AGENT = "minecraft";
	
	@Getter private static bAdmin instance = null;
	@Getter private Config config = null;
	@Getter private DatabaseHandler handler = null;
	@Getter private BanManager banManager = null;
	@Getter private WarningManager warningManager = null;
	
	@Override
	public void onDisable() {
		instance = null;
		banManager = null;
	}

	@Override
	public void onLoad() {
		banManager = new BanManager();
		warningManager = new WarningManager();
		
		instance = this;
	}

	@Override
	public void onEnable() {		
		try {
			loadConfig();
			
			banManager.loadBans();
			warningManager.loadWarnings();
			
			getProxy().getPluginManager().registerListener(this, new EventListener());
			
			getProxy().getPluginManager().registerCommand(this, new BanCommand());
			getProxy().getPluginManager().registerCommand(this, new TempBanCommand());
			getProxy().getPluginManager().registerCommand(this, new WarnCommand());
			getProxy().getPluginManager().registerCommand(this, new KickCommand());
			getProxy().getPluginManager().registerCommand(this, new CheckBanCommand());
			getProxy().getPluginManager().registerCommand(this, new UnbanCommand());
		} catch (AdminException ex) {
			getLogger().log(Level.SEVERE, "A error has ocurred trying to load " + getDescription().getName() + " v" + getDescription().getVersion() + ".");
			getLogger().log(Level.SEVERE, "Please check your setup before reporting this to the author.");
			getLogger().log(Level.SEVERE, "Error follows:", ex);
		} catch (Exception ex) {
			getLogger().log(Level.SEVERE, "A unexpected error has ocurred trying to load " + getDescription().getName() + " v" + getDescription().getVersion() + ".");
			getLogger().log(Level.SEVERE, "Please report this to the plugin author.");
			getLogger().log(Level.SEVERE, "Error follows:", ex);
		}
		
	}

	private void loadConfig() throws AdminException {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		this.config = new Config();
		File config = new File(this.getDataFolder(), "config.json");

		try{
			if (!config.exists()) {
				if (!config.getParentFile().exists()) {
					if (!config.getParentFile().mkdirs()) {
						throw new ConfigException("Error creating datafolder");
					}
				}
				
				try (FileWriter writer = new FileWriter(config)){
					if (config.createNewFile()) {
						gson.toJson(this.config, writer);
						writer.flush();
					} else {
						throw new ConfigException("Failed to create config file");
					}
				}
			}
			
			try (FileReader reader = new FileReader(config)) {
				this.config = gson.fromJson(reader, Config.class);
			}
			
			if (this.config == null || this.config.getConfigVersion() != CURRENT_CONFIG_VERSION) {
				getLogger().warning("--------------------------------------------------------");
				getLogger().warning("Outdated config, regenerating.");
				getLogger().warning("Please note you will have to resetup parts of the config");
				getLogger().warning("--------------------------------------------------------");

				if (!config.delete()) {
					throw new ConfigException("Error deleting old config file");
				}
				
				try (FileWriter writer = new FileWriter(config)) {
					if (config.createNewFile()) {
						this.config = new Config();
						gson.toJson(this.config, writer);
						writer.flush();
					} else {
						throw new ConfigException("Failed to create config file");
					}
				}
			}
			
		} catch (JsonIOException e) {
			throw new ConfigException(e);
		} catch (IOException e) {
			throw new ConfigException(e);
		}
		
		handler = this.config.getDatabaseInfo().getType().getHandler();
	}

}
