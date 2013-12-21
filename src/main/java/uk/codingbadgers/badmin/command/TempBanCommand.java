package uk.codingbadgers.badmin.command;

import static uk.codingbadgers.badmin.TimeUtils.*;
import static uk.codingbadgers.badmin.manager.MessageHandler.*;
import static uk.codingbadgers.badmin.manager.MessageHandler.CommandUsage.*;
import static uk.codingbadgers.badmin.manager.MessageHandler.KickMessage.*;

import java.util.Arrays;

import uk.codingbadgers.badmin.bAdmin;
import uk.codingbadgers.badmin.data.BanType;
import uk.codingbadgers.badmin.manager.BanManager;

import com.mojang.api.profiles.HttpProfileRepository;
import com.mojang.api.profiles.Profile;
import com.mojang.api.profiles.ProfileCriteria;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class TempBanCommand extends Command {

	public TempBanCommand() {
		super("tempban", "badmin.command.ban"); 
	}

	@Override
	public void execute(final CommandSender sender, String[] args) {
		
		if (args.length < 3) {
			sender.sendMessage(tempban());
			return;
		}
		
		final String user = args[0];
		
		if (!sender.getName().equalsIgnoreCase("CONSOLE") && user.equalsIgnoreCase(sender.getName())) { // Skip console, no need
			sender.sendMessage(selfError("tempban"));
			return;
		}
		
		final String reason = join(args, 2);
		final long expire = parseInput(args[1]);
		
		final BanManager manager = bAdmin.getInstance().getBanManager();
		final ProxyServer proxy = ProxyServer.getInstance();
		
		for (ProxiedPlayer player : proxy.getPlayers()) {
			if (player.getName().equals(user)) {
				manager.addBan(player.getUUID(), BanType.TEMP_BAN, reason, "" + expire, sender.getName());
				sender.sendMessage(tempbanSuccess(player.getName(), reason, parseDate(expire)));
				proxy.broadcast(tempbanBroadcast(player.getName(), sender.getName(), reason, parseDate(expire)));
				player.disconnect(tempbanned(sender.getName(), reason, parseDate(expire)));
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
				manager.addBan(profile.getId(), BanType.TEMP_BAN, reason, "" + expire, sender.getName());
				sender.sendMessage(tempbanSuccess(profile.getName(), reason, parseDate(expire)));
				proxy.broadcast(tempbanBroadcast(profile.getName(), sender.getName(), reason, parseDate(expire)));
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
