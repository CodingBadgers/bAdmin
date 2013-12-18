package uk.codingbadgers.badmin.data;

import java.util.HashMap;
import java.util.Map;

public enum BanType {

	BAN,
	TEMP_BAN,
	WARN,
	;
	
	private static final Map<Integer, BanType> BY_ID = new HashMap<Integer, BanType>();
	
	static {
		for (BanType type : values()) {
			BY_ID.put(type.ordinal(), type);
		}
	}
	
	public static BanType getFromId(int id) {
		return BY_ID.get(id);
	}
	
}
