package uk.codingbadgers.badmin.database.handlers;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import uk.codingbadgers.badmin.Config.DatabaseInfo;
import uk.codingbadgers.badmin.data.BanType;
import uk.codingbadgers.badmin.data.DataEntry;
import uk.codingbadgers.badmin.database.DatabaseHandler;
import uk.codingbadgers.badmin.exception.DatabaseException;

public class SQLDatabaseHandler extends DatabaseHandler {

	private Connection conn;

	public SQLDatabaseHandler(DatabaseInfo info) {
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
			conn = DriverManager.getConnection("jdbc:mysql://" + info.getHost() + ":" + info.getPort() + "/" + info.getDatabase(), info.getUser(), info.getPass());
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
	public void addEntry(DataEntry entry) {
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
	public DataEntry getData(String uuid, BanType type) {
		try {
			String query = "SELECT * FROM bAdmin_data WHERE `id` = ? AND `type` = ?;";
	
			PreparedStatement statement = conn.prepareStatement(query);
			statement.setString(1, uuid);
			statement.setInt(2, type.ordinal());
			
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
	public void removeEntry(String uuid, BanType type) {
		try {
			String query = "REMOVE FROM bAdmin_data WHERE `id` = ? AND `type` = ?;";
			
			PreparedStatement statement = conn.prepareStatement(query);
			statement.setString(1, uuid);
			statement.setInt(2, type.ordinal());
			
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
