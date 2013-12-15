package uk.codingbadgers.badmin.database.handlers;

import java.util.List;

import uk.codingbadgers.badmin.BanEntry;
import uk.codingbadgers.badmin.Config.DatabaseInfo;
import uk.codingbadgers.badmin.database.DatabaseHandler;

public class FileDatabaseHandler extends DatabaseHandler {

	public FileDatabaseHandler(DatabaseInfo info) {
		super(info);
	}

	@Override
	public boolean connect() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void addBan(BanEntry entry) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public BanEntry getData(String uuid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeBan(String uuid) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<BanEntry> getBans() {
		// TODO Auto-generated method stub
		return null;
	}

}
