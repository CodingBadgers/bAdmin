package uk.codingbadgers.badmin.database.handlers;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import uk.codingbadgers.badmin.DataEntry;
import uk.codingbadgers.badmin.BanType;
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
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			throw new DatabaseException("Could not find SQLite driver.");
		}
		
		try {
			conn = DriverManager.getConnection("jdbc:sqlite://" + info.getDatabase());
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
		
		if (!tableExists("bAdmin_data")) {
			try {
				String query = "CREATE TABLE bAdmin_data (" +
								"`id` VARCHAR(64) NOT NULL," +
								"`type` TINYINT," +
								"`reason` TEXT NOT NULL," +
								"`data` TEXT);";
				
				PreparedStatement statement = conn.prepareStatement(query);
				
				statement.execute();
			} catch (SQLException e) {
				throw new DatabaseException(e);
			}
		}
		
		return conn != null;
	}
	
	private boolean tableExists(String name) {
		try {
			DatabaseMetaData metadata = conn.getMetaData();
			ResultSet resultSet = metadata.getTables(null, null, name, null);
			
			return resultSet.next();
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	@Override
	public void addBan(DataEntry entry) {
		try {
			String query = "INSERT INTO bAdmin_data VALUES (? , ? , ? , ?);";
			
			PreparedStatement statement = conn.prepareStatement(query);
			
			statement.setString(1, entry.getName());
			statement.setInt(2, entry.getType().ordinal());
			statement.setString(3, entry.getReason());
			statement.setString(4, entry.getData());
			
			statement.execute();
		} catch (SQLException ex) {
			throw new DatabaseException(ex);
		}
	}

	@Override
	public DataEntry getData(String uuid) {
		try {
			String query = "SELECT * FROM bAdmin_data WHERE `id` = ?;";
	
			PreparedStatement statement = conn.prepareStatement(query);
			statement.setString(1, uuid);
			
			ResultSet results = statement.executeQuery();
			
			if (results.next()) {
				DataEntry entry = new DataEntry(results.getString("id"),
						BanType.getFromId(results.getInt("type")),
						results.getString("reason"),
						results.getString("data"));
				
				return entry;
			}
			
			return null;
			
		} catch (SQLException ex) {
			throw new DatabaseException(ex);
		}
	}

	@Override
	public void removeBan(String uuid) {
		try {
			String query = "REMOVE FROM bAdmin_data WHERE `id` = ?";
			
			PreparedStatement statement = conn.prepareStatement(query);
			
			statement.setString(1, uuid);
			
			statement.execute();
		} catch (SQLException ex) {
			throw new DatabaseException(ex);
		}
	}

	@Override
	public List<DataEntry> getBans() {
		List<DataEntry> bans = new ArrayList<DataEntry>();
		
		try {
			String query = "SELECT * FROM bAdmin_data WHERE `type` = ?;";
	
			PreparedStatement statement = conn.prepareStatement(query);
			statement.setInt(1, BanType.BAN.ordinal());
			
			ResultSet results = statement.executeQuery();
			
			while(results.next()) {
				DataEntry entry = new DataEntry(results.getString("id"),
									BanType.getFromId(results.getInt("type")),
									results.getString("reason"),
									results.getString("data"));
				bans.add(entry);
			}
			
		} catch (SQLException ex) {
			throw new DatabaseException(ex);
		}
		
		return bans;
	}
}
