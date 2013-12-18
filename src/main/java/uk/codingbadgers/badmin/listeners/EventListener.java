package uk.codingbadgers.badmin.listeners;

import uk.codingbadgers.badmin.TimeUtils;
import uk.codingbadgers.badmin.bAdmin;
import uk.codingbadgers.badmin.data.BanResult;
import uk.codingbadgers.badmin.data.BanType;
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
		
		BanResult result = manager.checkBan(uuid);
		
		if (result.isBanned()) {
			if (result.getEntry().getType() == BanType.BAN) {
				event.setCancelReason(BaseComponent.toLegacyText(KickMessage.currentlyBanned(result.getEntry().getReason())));
			} else if (result.getEntry().getType() == BanType.TEMP_BAN){
				event.setCancelReason(BaseComponent.toLegacyText(KickMessage.currentlyTempBanned(result.getEntry().getReason(), TimeUtils.parseDate(Long.parseLong(result.getEntry().getData())))));
			}
			
			event.setCancelled(true);
		}
	}

}
