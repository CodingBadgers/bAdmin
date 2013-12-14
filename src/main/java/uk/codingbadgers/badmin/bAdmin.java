package uk.codingbadgers.badmin;

import uk.codingbadgers.badmin.command.BanCommand;
import uk.codingbadgers.badmin.command.TempBanCommand;
import uk.codingbadgers.badmin.command.WarnCommand;

import net.md_5.bungee.api.plugin.Plugin;

public class bAdmin extends Plugin {

	@Override
	public void onEnable() {
		getProxy().getPluginManager().registerCommand(this, new BanCommand(this));
		getProxy().getPluginManager().registerCommand(this, new TempBanCommand(this));
		getProxy().getPluginManager().registerCommand(this, new WarnCommand(this));
	}

}
