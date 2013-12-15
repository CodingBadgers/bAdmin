package uk.codingbadgers.badmin.database;

import java.util.List;

import lombok.Getter;
import uk.codingbadgers.badmin.BanEntry;
import uk.codingbadgers.badmin.Config.DatabaseInfo;

public abstract class DatabaseHandler {

	@Getter protected DatabaseInfo info;

	public DatabaseHandler(DatabaseInfo info) {
		this.info = info;
		
		connect();
	}
	
	public abstract boolean connect();
	
	public abstract void addBan(BanEntry entry);
	
	public abstract BanEntry getData(String uuid);
	
	public abstract void removeBan(String uuid);
	
	public abstract List<BanEntry> getBans();
	
}
