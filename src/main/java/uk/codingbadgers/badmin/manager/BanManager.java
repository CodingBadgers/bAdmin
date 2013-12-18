package uk.codingbadgers.badmin.manager;

import java.util.HashMap;
import java.util.Map;

import uk.codingbadgers.badmin.bAdmin;
import uk.codingbadgers.badmin.data.BanResult;
import uk.codingbadgers.badmin.data.BanType;
import uk.codingbadgers.badmin.data.DataEntry;

import lombok.Getter;

public class BanManager {

	@Getter private Map<String, DataEntry> bans = null;
	
	public BanManager() {
		bans = new HashMap<String, DataEntry>();
	}
	
	public BanResult checkBan(String uuid) {
		DataEntry entry = bans.get(uuid);
		
		if (entry == null) {
			return BanResult.notBanned();
		}
		
		if (entry.getType() == BanType.TEMP_BAN) {
			String expireStr = entry.getData();
			long expire = Long.parseLong(expireStr);
			
			if (expire < System.currentTimeMillis()) {
				removeBan(uuid, BanResult.create(true, entry));
				return BanResult.notBanned();
			}
		}
		
		return BanResult.create(true, entry);
	}
	
	public DataEntry getBanEntry(String uuid) {
		if (bans.containsKey(uuid)) {
			return bans.get(uuid);
		}
		
		return null;
	}
	
	public void addBan(String uuid, BanType type, String reason) {
		DataEntry entry = new DataEntry(uuid, type, reason);
		bAdmin.getInstance().getHandler().addEntry(entry);
		
		synchronized(bans) {
			bans.put(uuid, entry);
		}
	}

	public void addBan(String uuid, BanType type, String reason, String data) {
		DataEntry entry = new DataEntry(uuid, type, reason, data);
		bAdmin.getInstance().getHandler().addEntry(entry);
		
		synchronized(bans) {
			bans.put(uuid, entry);
		}
	}
	
	public void removeBan(String uuid, BanResult result) {		
		if (!result.isBanned()) {
			return;
		}
		
		bans.remove(uuid);
		
		bAdmin.getInstance().getHandler().removeEntry(uuid, result.getEntry().getType());
	}
	
	public void removeBan(String uuid) {
		removeBan(uuid, checkBan(uuid));
	}
	
	public void loadBans() {
		for (DataEntry entry : bAdmin.getInstance().getHandler().getBans()) {
			bans.put(entry.getName(), entry);
		}
		
		bAdmin.getInstance().getLogger().info("Loaded " + bans.size() + " bans from the database");
	}
	
}
