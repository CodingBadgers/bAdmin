package uk.codingbadgers.badmin.database;

import java.lang.reflect.Constructor;
import uk.codingbadgers.badmin.bAdmin;
import uk.codingbadgers.badmin.Config.DatabaseInfo;
import uk.codingbadgers.badmin.database.handlers.JsonDatabaseHandler;
import uk.codingbadgers.badmin.database.handlers.SQLDatabaseHandler;
import uk.codingbadgers.badmin.database.handlers.SQLiteDatabaseHandler;
import uk.codingbadgers.badmin.exception.DatabaseException;

public enum DatabaseType {

	SQL(SQLDatabaseHandler.class),
	SQLITE(SQLiteDatabaseHandler.class),
	JSON(JsonDatabaseHandler.class),
	;
	
	private Constructor<? extends DatabaseHandler> ctor;
	
	private DatabaseHandler handler = null;

	private DatabaseType(Class<? extends DatabaseHandler> clazz) {		
		try {
			this.ctor = clazz.getConstructor(DatabaseInfo.class);
		} catch (SecurityException e) { // TODO better error handling
			throw new DatabaseException(e); 
		} catch (NoSuchMethodException e) {
			throw new DatabaseException(e);
		}
	}
	
	public DatabaseHandler getHandler() {
		if (handler == null) {
			try {
				handler = ctor.newInstance(bAdmin.getInstance().getConfig().getDatabaseInfo());
			} catch(Exception ex) {
				throw new DatabaseException(ex);
			}
		}
		
		return handler;
	}
}
