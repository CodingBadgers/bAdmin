package uk.codingbadgers.badmin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import uk.codingbadgers.badmin.database.DatabaseType;

@NoArgsConstructor
public class Config {

	@AllArgsConstructor
	@NoArgsConstructor
	public static class DatabaseInfo {
		@Getter private DatabaseType type = DatabaseType.JSON;
		@Getter private String database = "bans.bdm";
		@Getter private String host = "";
		@Getter private String user = "";
		@Getter private String pass = "";
		@Getter private int port = 3306;
	}

	@AllArgsConstructor
	@NoArgsConstructor
	public static class Warnings {
		@Getter private int TempBan = 3;
		@Getter private int PermBan = 5;
		@Getter private String TempBanTime = "1d";
	}

	@Getter private int configVersion = bAdmin.CURRENT_CONFIG_VERSION;
	
	@Getter private DatabaseInfo databaseInfo = new DatabaseInfo();
	
	@Getter private Warnings warnings = new Warnings();
	
	@Getter private boolean warnOnDelay = true;
	
	
	
}
