package uk.codingbadgers.badmin.manager;

import java.util.HashMap;
import java.util.Map;

import uk.codingbadgers.badmin.BanEntry;
import uk.codingbadgers.badmin.BanType;

import lombok.Getter;

public class BanManager {

	@Getter private Map<String, BanEntry> bans = null;
	
	public BanManager() {
		bans = new HashMap<String, BanEntry>();
	}
	
	public boolean isBanned(String uuid) {
		return bans.containsKey(uuid);
	}
	
	public BanEntry getBanEntry(String uuid) {
		if (bans.containsKey(uuid)) {
			return bans.get(uuid);
		}
		
		return null;
	}

	public String getReason(String uuid) {
		if (!isBanned(uuid)) {
			return "Not banned";
		}
		
		return bans.get(uuid).getReason();
	}
	
	public synchronized void addBan(String uuid, BanType type, String reason) {
		bans.put(uuid, new BanEntry(uuid, type, reason));
	}
	
	public void removeBan(String uuid) {
		bans.remove(uuid);
	}
	
	public void loadBans() {
		
	}
	
	public void saveBans() {
		
	}
	
}
