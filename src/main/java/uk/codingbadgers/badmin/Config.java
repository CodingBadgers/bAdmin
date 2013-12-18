package uk.codingbadgers.badmin;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uk.codingbadgers.badmin.database.DatabaseType;

@NoArgsConstructor
public class Config {

	@Data
	@NoArgsConstructor
	public static class DatabaseInfo {
		private DatabaseType type = DatabaseType.JSON;
		private String database = "bans.bdm";
		// MYSQL ONLY
		private String host = "";
		private String user = "";
		private String pass = "";
		private int port = -1;
	}

	@Data
	@NoArgsConstructor
	public static class Warnings {
		private int TempBan = 3;
		private int PermBan = 5;
		private String TempBanTime = "1d";
	}

	@Getter private int configVersion = 0x01;
	
	@Getter private DatabaseInfo databaseInfo = new DatabaseInfo();
	
	@Getter private Warnings warnings = new Warnings();
	
	@Getter private boolean warnOnDelay = true;
	
	
	
}
