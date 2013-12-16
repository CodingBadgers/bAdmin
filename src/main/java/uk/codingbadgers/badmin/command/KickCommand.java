package uk.codingbadgers.badmin.command;

import static uk.codingbadgers.badmin.manager.MessageHandler.*;
import static uk.codingbadgers.badmin.manager.MessageHandler.CommandUsage.*;
import static uk.codingbadgers.badmin.manager.MessageHandler.KickMessage.*;

import java.util.Arrays;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class KickCommand extends Command {

	public KickCommand() {
		super("kick", "badmin.command.ban");
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {

		if (args.length < 2) {
			sender.sendMessage(kick());
			return;
		}
		
		final String user = args[0];
		
		if (user.equalsIgnoreCase(sender.getName())) {
			sender.sendMessage(selfError("kick"));
			return;
		}
		
		final String reason = join(args, 1);
		
		final ProxyServer proxy = ProxyServer.getInstance();
		
		for (ProxiedPlayer player : proxy.getPlayers()) {
			if (player.getName().equals(user)) {
				sender.sendMessage(kickedSuccess(player.getName(), reason));
				proxy.broadcast(kickedBroadcast(player.getName(), sender.getName(), reason));
				player.disconnect(kicked(sender.getName(), reason));
				return;
			}
		}
		
	}

	public String join(String[] strings, int start) {
		StringBuilder builder = new StringBuilder();
		
		for (String s : Arrays.copyOfRange(strings, start, strings.length)) {
			builder.append(s).append(" ");
		}
		
		return builder.toString();
	}

}
