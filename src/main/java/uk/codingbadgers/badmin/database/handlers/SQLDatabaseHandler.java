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
import uk.codingbadgers.badmin.bAdmin;
import uk.codingbadgers.badmin.data.BanType;
import uk.codingbadgers.badmin.data.DataEntry;
import uk.codingbadgers.badmin.database.DatabaseHandler;
import uk.codingbadgers.badmin.exception.DatabaseException;

public class SQLDatabaseHandler extends DatabaseHandler {

	private static final long TIMEOUT = 1800000;
	private Connection conn;
	private long refreshTime;

	public SQLDatabaseHandler(DatabaseInfo info) {
		super(info);
	}

	@Override
	public boolean connect() {
		refreshTime = System.currentTimeMillis() + TIMEOUT;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new DatabaseException("Could not find MySQL driver.");
		}
		
		try {
			conn = DriverManager.getConnection(
					"jdbc:mysql://" + info.getHost() + ":" + info.getPort() + "/" +
					info.getDatabase() + "?" +
					"user=" + info.getUser() +
					"&password=" + info.getPass()
				);
		} catch (SQLException e) {
			throw new DatabaseException("Cannot connect to database", e);
		}
		
		if (!tableExists("bAdmin_data")) {
			try {
				String query = "CREATE TABLE bAdmin_data (" +
								"`id` VARCHAR(64) NOT NULL," +
								"`type` TINYINT NOT NULL," +
								"`reason` TEXT NOT NULL," +
								"`sender` TEXT NOT NULL," +
								"`data` TEXT);";
				
				PreparedStatement statement = conn.prepareStatement(query);
				
				statement.execute();
			} catch (SQLException e) {
				throw new DatabaseException("Failed to create table", e);
			}
		}

		return conn != null;
	}
	
	private boolean tableExists(String name) {
		try {
			return tableExists0(name);
		} catch (SQLException e) {
			throw new DatabaseException("Error executing query", e);
		}
	}

	private boolean tableExists0(String name) throws SQLException {
		DatabaseMetaData metadata = getConnection().getMetaData();
		ResultSet resultSet = metadata.getTables(null, null, name, null);
		
		return resultSet.next();
	}

	@Override
	public void addEntry(DataEntry entry) {
		try {
			addEntry0(entry);
		} catch (SQLException e) {
			throw new DatabaseException("Error excecuting query", e);
		}
	}

	private void addEntry0(DataEntry entry) throws SQLException {
		String query = "INSERT INTO bAdmin_data VALUES (? , ? , ? , ?, ?);";
		
		PreparedStatement statement = getConnection().prepareStatement(query);
		
		statement.setString(1, entry.getName());
		statement.setInt(2, entry.getType().ordinal());
		statement.setString(3, entry.getReason());
		statement.setString(4, entry.getAdmin());
		statement.setString(5, entry.getData());
		
		statement.execute();
	}

	@Override
	public DataEntry getData(String uuid, BanType type) {
		try {
			return getData0(uuid, type);
		} catch (SQLException ex) {
			throw new DatabaseException("Error executing query", ex);
		}
	}

	private DataEntry getData0(String uuid, BanType type) throws SQLException {
		String query = "SELECT * FROM bAdmin_data WHERE `id` = ? AND `type` = ?;";

		PreparedStatement statement = getConnection().prepareStatement(query);
		statement.setString(1, uuid);
		statement.setInt(2, type.ordinal());
		
		ResultSet results = statement.executeQuery();
		
		if (results.next()) {
			DataEntry entry = new DataEntry(results.getString("id"),
					BanType.getFromId(results.getInt("type")),
					results.getString("reason"),
					results.getString("sender"),
					results.getString("data"));
			
			return entry;
		}
		
		return null;
	}

	@Override
	public void removeEntry(String uuid, BanType type) {
		try {
			removeEntry0(uuid, type);
		} catch (SQLException ex) {
			throw new DatabaseException("Error executing query", ex);
		}
	}

	private void removeEntry0(String uuid, BanType type) throws SQLException {
		String query = "DELETE FROM bAdmin_data WHERE `id` = ? AND `type` = ?;";
		
		PreparedStatement statement = getConnection().prepareStatement(query);
		statement.setString(1, uuid);
		statement.setInt(2, type.ordinal());
		
		statement.execute();
	}

	@Override
	public List<DataEntry> getBans() {
		try {
			return getBans0();
		} catch (SQLException ex) {
			throw new DatabaseException("Error executing query", ex);
		}
	}

	private List<DataEntry> getBans0() throws SQLException {
		List<DataEntry> bans = new ArrayList<DataEntry>();
		String query = "SELECT * FROM bAdmin_data;";

		PreparedStatement statement = getConnection().prepareStatement(query);
		
		ResultSet results = statement.executeQuery();
		
		while(results.next()) {
			DataEntry entry = new DataEntry(results.getString("id"),
								BanType.getFromId(results.getInt("type")),
								results.getString("reason"),
								results.getString("sender"),
								results.getString("data"));
			bans.add(entry);
		}
		
		return bans;
	}
	
	public Connection getConnection() {
		if (conn == null || refreshTime <= System.currentTimeMillis()) {
			bAdmin.getInstance().getLogger().info("Refreshing database connection");
			connect();
		}
		
		return conn;
	}
}
