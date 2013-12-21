package uk.codingbadgers.badmin.command;

import static uk.codingbadgers.badmin.manager.MessageHandler.*;
import static uk.codingbadgers.badmin.manager.MessageHandler.CommandUsage.*;
import static uk.codingbadgers.badmin.manager.MessageHandler.KickMessage.*;

import java.util.Arrays;
import java.util.List;

import uk.codingbadgers.badmin.Config;
import uk.codingbadgers.badmin.TimeUtils;
import uk.codingbadgers.badmin.bAdmin;
import uk.codingbadgers.badmin.data.BanType;
import uk.codingbadgers.badmin.data.DataEntry;
import uk.codingbadgers.badmin.manager.BanManager;
import uk.codingbadgers.badmin.manager.WarningManager;

import com.mojang.api.profiles.HttpProfileRepository;
import com.mojang.api.profiles.Profile;
import com.mojang.api.profiles.ProfileCriteria;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class WarnCommand extends Command {

	public WarnCommand() {
		super("warn", "badmin.command.warn");
	}

	@Override
	public void execute(final CommandSender sender, String[] args) {

		if (args.length < 2) {
			sender.sendMessage(warn());
			return;
		}
		
		final String user = args[0];
		
		if (!sender.getName().equalsIgnoreCase("CONSOLE") && user.equalsIgnoreCase(sender.getName())) { // Skip console, no need
			sender.sendMessage(selfError("warn"));
			return;
		}
		
		final String reason = join(args, 1);
		
		final WarningManager manager = bAdmin.getInstance().getWarningManager();
		final ProxyServer proxy = ProxyServer.getInstance();
		
		for (ProxiedPlayer player : proxy.getPlayers()) {
			if (player.getName().equals(user)) {
				final Profile profile = new Profile(player.getName(), player.getUUID());
				
				manager.addWarning(player.getUUID(), BanType.WARN, reason, sender.getName());
				sender.sendMessage(warningSuccess(player.getName(), reason));
				proxy.broadcast(warningBroadcast(player.getName(), sender.getName(), reason));
				
				proxy.getScheduler().runAsync(bAdmin.getInstance(), new Runnable() {

					@Override
					public void run() {
						checkWarnings(profile, sender.getName(), reason);
					}
					
				});
				
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
				
				// TODO check if played on server
				
				manager.addWarning(profile.getId(), BanType.WARN, reason, sender.getName());
				sender.sendMessage(warningSuccess(profile.getName(), reason));
				proxy.broadcast(warningBroadcast(profile.getName(), sender.getName(), reason));
				
				checkWarnings(profile, sender.getName(), reason);
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

	private void checkWarnings(Profile profile, String sender, String reason) {
		List<DataEntry> warnings = bAdmin.getInstance().getWarningManager().getWarnings(profile.getId());
		BanManager manager = bAdmin.getInstance().getBanManager();
		Config config = bAdmin.getInstance().getConfig();
		ProxyServer proxy = ProxyServer.getInstance();
		
		if (warnings.size() >= config.getWarnings().getPermBan()) { // AND STAY BANNED
			manager.addBan(profile.getId(), BanType.BAN, reason, sender);
			proxy.broadcast(bannedBroadcast(profile.getName(), sender, reason));
			ProxiedPlayer pplayer = proxy.getPlayer(profile.getName());
			
			if (pplayer != null) {
				pplayer.disconnect(banned(sender, reason));
			}
		} else if (warnings.size() == config.getWarnings().getTempBan()) {
			long expire = TimeUtils.parseInput(config.getWarnings().getTempBanTime());
			manager.addBan(profile.getId(), BanType.TEMP_BAN, reason, "" + expire, sender);
			proxy.broadcast(tempbanBroadcast(profile.getName(), sender, reason, TimeUtils.parseDate(expire)));
			ProxiedPlayer pplayer = proxy.getPlayer(profile.getName());
			
			if (pplayer != null) {
				pplayer.disconnect(tempbanned(sender, reason, TimeUtils.parseDate(expire)));
			}
		}
	}
	

}
