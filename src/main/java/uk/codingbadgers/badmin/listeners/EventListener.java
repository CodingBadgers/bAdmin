package uk.codingbadgers.badmin.listeners;

import uk.codingbadgers.badmin.bAdmin;
import uk.codingbadgers.badmin.manager.BanManager;
import uk.codingbadgers.badmin.manager.MessageHandler.KickMessage;

import net.md_5.bungee.api.chat.BaseComponent;
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
			event.setCancelReason(BaseComponent.toLegacyText(KickMessage.currentlyBanned(manager.getReason(uuid))));
			event.setCancelled(true);
		}
	}

}