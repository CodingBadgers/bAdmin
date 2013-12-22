package uk.codingbadgers.badmin.listeners;

import static uk.codingbadgers.badmin.manager.MessageHandler.noPlayer;

import com.mojang.api.profiles.HttpProfileRepository;
import com.mojang.api.profiles.Profile;
import com.mojang.api.profiles.ProfileCriteria;

import uk.codingbadgers.badmin.TimeUtils;
import uk.codingbadgers.badmin.bAdmin;
import uk.codingbadgers.badmin.data.BanResult;
import uk.codingbadgers.badmin.data.BanType;
import uk.codingbadgers.badmin.manager.BanManager;
import uk.codingbadgers.badmin.manager.MessageHandler.KickMessage;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class EventListener implements Listener {

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerJoin(final LoginEvent event) {

		String uuid = event.getConnection().getUUID();
		
		// If not in online mode look up the id ourselves, might lag but n3wton wanted it
		if (!ProxyServer.getInstance().getConfig().isOnlineMode()) {
			bAdmin.getInstance().getLogger().info("Looking up id for " + event.getConnection().getName());
			HttpProfileRepository repo = new HttpProfileRepository();
			
			ProfileCriteria criteria = new ProfileCriteria(event.getConnection().getName(), bAdmin.MOJANG_AGENT); 
			Profile[] results = repo.findProfilesByCriteria(criteria);

			if (results.length == 0) {
				bAdmin.getInstance().getLogger().warning(BaseComponent.toPlainText(noPlayer(event.getConnection().getName())));
				return;
			}

			Profile profile = results[0];
			uuid = profile.getId();
		}
		
		BanManager manager = bAdmin.getInstance().getBanManager();
		
		System.out.println("Player " + event.getConnection().getName() + " (" + uuid + ") has logged in");
		
		BanResult result = manager.checkBan(uuid);
		
		if (result.isBanned()) {
			if (result.getEntry().getType() == BanType.BAN) {
				event.setCancelReason(BaseComponent.toLegacyText(KickMessage.currentlyBanned(result.getEntry().getReason())));
			} else if (result.getEntry().getType() == BanType.TEMP_BAN){
				event.setCancelReason(BaseComponent.toLegacyText(KickMessage.currentlyTempBanned(result.getEntry().getReason(), TimeUtils.parseDate(result.getEntry().getData()))));
			}
			
			event.setCancelled(true);
		}
	}

}
