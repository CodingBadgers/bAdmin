package uk.codingbadgers.badmin.database;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

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
		} catch (DatabaseException e) {
			throw e;
		} catch (Exception e) {
			throw new DatabaseException(e); 
		}
	}
	
	public DatabaseHandler getHandler() {
		if (handler == null) {
			try {
				handler = ctor.newInstance(bAdmin.getInstance().getConfig().getDatabaseInfo());
			} catch (InvocationTargetException e) {
				Throwable cause = e.getCause();
				
				if (cause instanceof DatabaseException) {
					throw (DatabaseException) cause;
				} else {
					throw new DatabaseException(cause);
				}
			} catch(Exception ex) {
				throw new DatabaseException(ex);
			}
		}
		
		return handler;
	}
}
