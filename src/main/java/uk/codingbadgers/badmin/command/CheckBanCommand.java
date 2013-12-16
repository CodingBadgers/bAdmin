package uk.codingbadgers.badmin.command;

import uk.codingbadgers.badmin.bAdmin;
import uk.codingbadgers.badmin.manager.BanManager;

import com.mojang.api.profiles.HttpProfileRepository;
import com.mojang.api.profiles.Profile;
import com.mojang.api.profiles.ProfileCriteria;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Command;
import static uk.codingbadgers.badmin.manager.MessageHandler.*;
import static uk.codingbadgers.badmin.manager.MessageHandler.CommandUsage.*;

public class CheckBanCommand extends Command {

	public CheckBanCommand() {
		super("checkban", "badmin.commands.checkban", "isbanned");
	}

	@Override
	public void execute(final CommandSender sender, String[] args) {
		
		if (args.length != 1) {
			sender.sendMessage(isbanned());
			return;
		}
		
		final String user = args[0];
		
		sender.sendMessage(isBannedLooking(user)); // Slight delay, so should tell people
		
		final ProxyServer proxy = ProxyServer.getInstance();
		final BanManager manager = bAdmin.getInstance().getBanManager();

		// get UUID from the Mojang API
		proxy.getScheduler().runAsync(bAdmin.getInstance(), new Runnable() {
			@Override
			public void run() {
				HttpProfileRepository repo = new HttpProfileRepository();
			
				ProfileCriteria criteria = new ProfileCriteria(user, "minecraft"); 
				Profile[] results = repo.findProfilesByCriteria(criteria);
	
				if (results.length == 0) {
					sender.sendMessage(noPlayer(user));
					return;
				}

				Profile profile = results[0];
				
				if (results.length != 1) {
					sender.sendMessage(multiplePlayers(user, profile.getName()));
				}
				
				if (manager.isBanned(profile.getId())) {
					sender.sendMessage(isBanned(profile.getName()));
				} else {
					sender.sendMessage(notBanned(profile.getName()));
				}
			}
		});
		
	}

}
