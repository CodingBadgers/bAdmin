package uk.codingbadgers.badmin.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import uk.codingbadgers.badmin.bAdmin;
import uk.codingbadgers.badmin.data.BanType;
import uk.codingbadgers.badmin.data.DataEntry;

public class WarningManager {

	@Getter private Map<String, List<DataEntry>> warnings = null;
	
	public WarningManager() {
		warnings = new HashMap<>();
	}
	
	public boolean hasWarnings(String uuid) {
		return warnings.containsKey(uuid);
	}
	
	public List<DataEntry> getWarnings(String uuid) {
		if (warnings.containsKey(uuid)) {
			return warnings.get(uuid);
		}
		
		return null;
	}

	public synchronized void addWarning(String uuid, BanType type, String reason) {
		addWarning(new DataEntry(uuid, type, reason));
	}

	public synchronized void addWarning(DataEntry entry) {
		if (warnings.containsKey(entry.getName())) {
			warnings.get(entry.getName()).add(entry);
		} else {
			warnings.put(entry.getName(), new ArrayList<DataEntry>(Arrays.asList(entry)));
		}
		
		bAdmin.getInstance().getHandler().addEntry(entry);
	}
	
	public void removeWarning(String uuid) {
		warnings.remove(uuid);
		
		bAdmin.getInstance().getHandler().removeEntry(uuid, BanType.BAN);
	}
	
	public void loadWarnings() {
		for (DataEntry entry : bAdmin.getInstance().getHandler().getBans()) {
			if (entry.getType().equals(BanType.WARN)) {
				if (warnings.containsKey(entry.getName())) {
					warnings.get(entry.getName()).add(entry);
				} else {
					warnings.put(entry.getName(), new ArrayList<DataEntry>(Arrays.asList(entry)));
				}
			}
		}
		
		bAdmin.getInstance().getLogger().info("Loaded " + warnings.size() + " warnings from the database");
	}
	
}
