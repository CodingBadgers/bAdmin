package uk.codingbadgers.badmin;

import lombok.Getter;
import uk.codingbadgers.badmin.command.BanCommand;
import uk.codingbadgers.badmin.command.TempBanCommand;
import uk.codingbadgers.badmin.command.WarnCommand;
import uk.codingbadgers.badmin.listeners.EventListener;
import uk.codingbadgers.badmin.manager.BanManager;

import net.md_5.bungee.api.plugin.Plugin;

public class bAdmin extends Plugin {

	@Getter private static bAdmin instance = null;
	@Getter private BanManager banManager = null;
	
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
		banManager.loadBans();
		
		getProxy().getPluginManager().registerListener(this, new EventListener());
		
		getProxy().getPluginManager().registerCommand(this, new BanCommand(this));
		getProxy().getPluginManager().registerCommand(this, new TempBanCommand(this));
		getProxy().getPluginManager().registerCommand(this, new WarnCommand(this));
		getProxy().getPluginManager().registerCommand(this, new BanCommand());
		getProxy().getPluginManager().registerCommand(this, new TempBanCommand());
		getProxy().getPluginManager().registerCommand(this, new WarnCommand());
		getProxy().getPluginManager().registerCommand(this, new KickCommand());
	}

}
