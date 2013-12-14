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

	public String getReason(String uuid) {
		if (!isBanned(uuid)) {
			return "Not banned";
		}
		
		return bans.get(uuid).getReason();
	}
	
	public void loadBans() {
		bans.put("af94b13ea3a344db81260e75f7cb4c65", new BanEntry("TheFish97", BanType.BAN, "Testing"));
	}
	
}
