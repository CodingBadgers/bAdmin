package uk.codingbadgers.badmin.manager;

import java.util.HashMap;
import java.util.Map;

import uk.codingbadgers.badmin.DataEntry;
import uk.codingbadgers.badmin.BanType;
import uk.codingbadgers.badmin.bAdmin;

import lombok.Getter;

public class BanManager {

	@Getter private Map<String, DataEntry> bans = null;
	
	public BanManager() {
		bans = new HashMap<String, DataEntry>();
	}
	
	public boolean isBanned(String uuid) {
		return bans.containsKey(uuid);
	}
	
	public DataEntry getBanEntry(String uuid) {
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
		DataEntry entry = new DataEntry(uuid, type, reason);
		bans.put(uuid, entry);
		bAdmin.getInstance().getHandler().addBan(entry);
	}
	
	public void removeBan(String uuid) {
		bans.remove(uuid);
		bAdmin.getInstance().getHandler().removeBan(uuid);
	}
	
	public void loadBans() {
		for (DataEntry entry : bAdmin.getInstance().getHandler().getBans()) {
			bans.put(entry.getName(), entry);
		}
		
		bAdmin.getInstance().getLogger().info("Loaded " + bans.size() + " bans from the database");
	}
	
	public void saveBans() {
		
	}
	
}
