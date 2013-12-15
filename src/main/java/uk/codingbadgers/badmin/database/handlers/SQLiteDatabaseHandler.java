package uk.codingbadgers.badmin.database.handlers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import uk.codingbadgers.badmin.BanEntry;
import uk.codingbadgers.badmin.Config.DatabaseInfo;
import uk.codingbadgers.badmin.database.DatabaseHandler;
import uk.codingbadgers.badmin.exception.DatabaseException;

public class SQLiteDatabaseHandler extends DatabaseHandler {

	private Connection conn;

	public SQLiteDatabaseHandler(DatabaseInfo info) {
		super(info);
	}

	@Override
	public boolean connect() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new DatabaseException("Could not find MySQL driver.");
		}
		
		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + info.getDatabase() + ":" + info.getPort() + "/" + info.getDatabase(), info.getUser(), info.getPass());
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
		
		
		return true;
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
