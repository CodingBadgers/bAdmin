package uk.codingbadgers.badmin.command;

import java.util.Arrays;

import com.mojang.api.profiles.HttpProfileRepository;
import com.mojang.api.profiles.Profile;
import com.mojang.api.profiles.ProfileCriteria;

import uk.codingbadgers.badmin.bAdmin;
import uk.codingbadgers.badmin.data.BanType;
import uk.codingbadgers.badmin.manager.BanManager;
import static uk.codingbadgers.badmin.manager.MessageHandler.*;
import static uk.codingbadgers.badmin.manager.MessageHandler.KickMessage.*;
import static uk.codingbadgers.badmin.manager.MessageHandler.CommandUsage.*;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class BanCommand extends Command {

	public BanCommand() {
		super("ban", "badmin.command.ban");
	}

	@Override
	public void execute(final CommandSender sender, String[] args) {
		
		if (args.length < 2) {
			sender.sendMessage(ban());
			return;
		}
		
		final String user = args[0];
		
		if (!sender.getName().equalsIgnoreCase("CONSOLE") && user.equalsIgnoreCase(sender.getName())) { // Skip console, no need
			sender.sendMessage(selfError("ban"));
			return;
		}
		
		final String reason = join(args, 1);
		
		final BanManager manager = bAdmin.getInstance().getBanManager();
		final ProxyServer proxy = ProxyServer.getInstance();
		
		for (ProxiedPlayer player : proxy.getPlayers()) {
			if (player.getName().equals(user)) {
				manager.addBan(player.getUUID(), BanType.BAN, reason, sender.getName());
				sender.sendMessage(bannedSuccess(player.getName(), reason));
				proxy.broadcast(bannedBroadcast(player.getName(), sender.getName(), reason));
				player.disconnect(banned(sender.getName(), reason));
				return;
			}
		}

		if (bAdmin.getInstance().getConfig().isWarnOnDelay()) sender.sendMessage(mojangIDLookup(user));
		
		// get UUID from the Mojang API
		proxy.getScheduler().runAsync(bAdmin.getInstance(), new Runnable() {
			@Override
			public void run() {
				HttpProfileRepository repo = new HttpProfileRepository();
			
				ProfileCriteria criteria = new ProfileCriteria(user, bAdmin.MOJANG_AGENT); 
				Profile[] results = repo.findProfilesByCriteria(criteria);
	
				if (results.length == 0) {
					sender.sendMessage(noPlayer(user));
					return;
				}

				Profile profile = results[0];
				
				if (results.length != 1) {
					sender.sendMessage(multiplePlayers(user, profile.getName()));
				}
				
				if (manager.checkBan(profile.getId()).isBanned()) {
					sender.sendMessage(alreadyBanned(profile.getName()));
					return;
				}
				
				// TODO check if played on server
				
				System.out.println(profile.getId() + " " + profile.getName());
				manager.addBan(profile.getId(), BanType.BAN, reason, sender.getName());
				sender.sendMessage(bannedSuccess(profile.getName(), reason));
				proxy.broadcast(bannedBroadcast(profile.getName(), sender.getName(), reason));
			}
		});
	}
	
	public String join(String[] strings, int start) {
		StringBuilder builder = new StringBuilder();
		
		for (String s : Arrays.copyOfRange(strings, start, strings.length)) {
			builder.append(s).append(" ");
		}
		
		return builder.toString();
	}

}
