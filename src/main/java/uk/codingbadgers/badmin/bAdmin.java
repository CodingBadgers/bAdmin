package uk.codingbadgers.badmin;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;

import lombok.Getter;
import uk.codingbadgers.badmin.command.BanCommand;
import uk.codingbadgers.badmin.command.CheckBanCommand;
import uk.codingbadgers.badmin.command.KickCommand;
import uk.codingbadgers.badmin.command.TempBanCommand;
import uk.codingbadgers.badmin.command.WarnCommand;
import uk.codingbadgers.badmin.database.DatabaseHandler;
import uk.codingbadgers.badmin.listeners.EventListener;
import uk.codingbadgers.badmin.manager.BanManager;

import net.md_5.bungee.api.plugin.Plugin;

public class bAdmin extends Plugin {

	private static final int CURRENT_CONFIG_VERSION = 0x01;
	
	@Getter private static bAdmin instance = null;
	@Getter private BanManager banManager = null;
	@Getter private Config config = null;
	@Getter private DatabaseHandler handler = null;
	
	@Override
	public void onDisable() {
		instance = null;
		banManager = null;
	}

	@Override
	public void onLoad() {
		instance = this;
	}

	@Override
	public void onEnable() {		
		banManager = new BanManager();

		loadConfig();
		
		banManager.loadBans();
		
		getProxy().getPluginManager().registerListener(this, new EventListener());
		
		getProxy().getPluginManager().registerCommand(this, new BanCommand());
		getProxy().getPluginManager().registerCommand(this, new TempBanCommand());
		getProxy().getPluginManager().registerCommand(this, new WarnCommand());
		getProxy().getPluginManager().registerCommand(this, new KickCommand());
		getProxy().getPluginManager().registerCommand(this, new CheckBanCommand());
		//getProxy().getPluginManager().registerCommand(this, new UnbanCommand());
		
	}

	private void loadConfig() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		this.config = new Config();
		File config = new File(this.getDataFolder(), "config.json");

		try{
			if (!config.exists()) {
				if (!config.getParentFile().exists()) {
					config.getParentFile().mkdirs();
				}
				
				try (FileWriter writer = new FileWriter(config)){
					config.createNewFile();
					gson.toJson(this.config, writer);
					writer.flush();
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
					throw new IOException("Error deleting old config file");
				}
				
				try (FileWriter writer = new FileWriter(config)) {
					config.createNewFile();
					gson.toJson(new Config(), writer);
					writer.flush();
				}
			}
			
		} catch (JsonIOException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		handler = this.config.getDatabaseInfo().getType().getHandler();
	}

}
