package uk.codingbadgers.badmin.database;

import java.util.List;

import lombok.Getter;
import uk.codingbadgers.badmin.Config.DatabaseInfo;
import uk.codingbadgers.badmin.data.BanType;
import uk.codingbadgers.badmin.data.DataEntry;

public abstract class DatabaseHandler {

	@Getter protected DatabaseInfo info;

	public DatabaseHandler(DatabaseInfo info) {
		this.info = info;
		
		connect();
	}
	
	public abstract boolean connect();
	
	public abstract void addEntry(DataEntry entry);
	
	public abstract DataEntry getData(String uuid, BanType type);
	
	public abstract void removeEntry(String uuid, BanType type);
	
	public abstract List<DataEntry> getBans();
	
}
