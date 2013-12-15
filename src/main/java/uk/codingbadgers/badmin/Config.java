package uk.codingbadgers.badmin;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uk.codingbadgers.badmin.database.DatabaseType;

@Data
@NoArgsConstructor
public class Config {

	@Data
	@NoArgsConstructor
	public static class DatabaseInfo {
		private DatabaseType type = DatabaseType.FILE;
		private String database = "bans.bdm";
		// MYSQL ONLY
		private String host = "";
		private String user = "";
		private String pass = "";
		private int port = -1;
	}
	
	@Getter private DatabaseInfo databaseInfo = new DatabaseInfo();
	
}
