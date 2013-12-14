package uk.codingbadgers.badmin.listeners;

import uk.codingbadgers.badmin.bAdmin;
import uk.codingbadgers.badmin.manager.BanManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class EventListener implements Listener {

	@EventHandler
	public void onPlayerJoin(final LoginEvent event) {
		String uuid = event.getConnection().getUUID();
		BanManager manager = bAdmin.getInstance().getBanManager();
		
		System.out.println("Player " + event.getConnection().getName() + " (" + uuid + ") has logged in");
		
		if (manager.isBanned(uuid)) {
			event.setCancelReason(ChatColor.RED + "You are currently banned from this server" + "\n\n" + ChatColor.BOLD + "Reason:" + ChatColor.RESET + ChatColor.RED + "\n" + manager.getReason(uuid));
			event.setCancelled(true);
		}
	}
}
