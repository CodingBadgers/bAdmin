package uk.codingbadgers.badmin;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;

import lombok.Getter;
import uk.codingbadgers.badmin.command.BanCommand;
import uk.codingbadgers.badmin.command.KickCommand;
import uk.codingbadgers.badmin.command.TempBanCommand;
import uk.codingbadgers.badmin.command.WarnCommand;
import uk.codingbadgers.badmin.database.DatabaseHandler;
import uk.codingbadgers.badmin.listeners.EventListener;
import uk.codingbadgers.badmin.manager.BanManager;

import net.md_5.bungee.api.plugin.Plugin;

public class bAdmin extends Plugin {

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
		
	}

	private void loadConfig() {
		Gson gson = new Gson();
		File config = new File(this.getDataFolder(), "config.json");

		try {
			if (!config.exists()) {
				if (!config.getParentFile().exists()) {
					config.getParentFile().mkdirs();
				}
				
				FileWriter writer = new FileWriter(config);
				
				try {
					config.createNewFile();
					gson.toJson(new Config(), writer);
				} finally {
					writer.flush();
					writer.close();
				}
			}
			
			this.config = gson.fromJson(new FileReader(config), Config.class);
		} catch (JsonIOException e) {
			e.printStackTrace();
			this.config = new Config();
		} catch (IOException e) {
			e.printStackTrace();
			this.config = new Config();
		}
		
		handler = this.config.getDatabaseInfo().getType().getHandler();
	}

}
