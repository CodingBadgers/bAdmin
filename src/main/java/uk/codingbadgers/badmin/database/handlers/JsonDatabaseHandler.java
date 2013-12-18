package uk.codingbadgers.badmin.database.handlers;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import uk.codingbadgers.badmin.bAdmin;
import uk.codingbadgers.badmin.Config.DatabaseInfo;
import uk.codingbadgers.badmin.data.BanType;
import uk.codingbadgers.badmin.data.DataEntry;
import uk.codingbadgers.badmin.database.DatabaseHandler;

public class JsonDatabaseHandler extends DatabaseHandler {

	private static final Gson gson = new Gson();
	private File file;
	
	public JsonDatabaseHandler(DatabaseInfo info) {
		super(info);
	}

	@Override
	public boolean connect() {
		
		this.file = new File(bAdmin.getInstance().getDataFolder(), info.getDatabase());
		
		if (!file.exists()) {
			try {
				file.createNewFile();
				saveJson(new JsonArray());
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		
		return true;
	}

	@Override
	public void addEntry(DataEntry entry) {
		JsonArray bans = getJsonBans();
		bans.add(gson.toJsonTree(entry));
		saveJson(bans);
	}

	@Override
	public DataEntry getData(String uuid, BanType type) {
		DataEntry entry = null;
		
		for (JsonElement element : getJsonBans()) {
			JsonObject object = element.getAsJsonObject();
			
			if (object.get("name").getAsString().equalsIgnoreCase(uuid) &&
					object.get("type").getAsString().equalsIgnoreCase(type.name())) {
				entry = gson.fromJson(object, DataEntry.class);
			}
		}
		
		return entry;
	}

	@Override
	public void removeEntry(String uuid, BanType type) { // TODO better handling of removing a ban
		JsonArray bans = getJsonBans();
		JsonArray newbans = new JsonArray();
		
		for (int i = 0; i < bans.size(); i++) {
			JsonObject ban = bans.get(i).getAsJsonObject();

			if (!ban.get("name").getAsString().equalsIgnoreCase(uuid) ||
					!ban.get("type").getAsString().equalsIgnoreCase(type.name())) {
				newbans.add(ban);
			}
		}
		
		saveJson(newbans);
	}

	@Override
	public List<DataEntry> getBans() {		
		List<DataEntry> bans = new ArrayList<DataEntry>();
		
		for (JsonElement ban : getJsonBans()) {
			bans.add(gson.fromJson(ban, DataEntry.class));
		}
		
		return bans;
	}

	private void saveJson(JsonArray bans) {
		try (FileWriter writer = new FileWriter(file)){
			JsonObject element = getJson();
			
			element.remove("bans");
			element.add("bans", bans);
			
			writer.write(element.toString());
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

	private JsonArray getJsonBans() {
		try (FileReader reader = new FileReader(file)){
			JsonObject data = getJson();
			JsonElement array = data.get("bans");
			
			if (!array.isJsonArray()) {
				return new JsonArray();
			}
			
			return array.getAsJsonArray();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		return null;
	}
	
	private JsonObject getJson() {
		try (FileReader reader = new FileReader(file)){
			JsonParser parser = new JsonParser();
			JsonElement element = parser.parse(reader);
			
			if (!element.isJsonObject()) {
				return new JsonObject();
			}
			
			return element.getAsJsonObject();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		return null;
	}
	
}
