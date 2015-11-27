package mcjagger.mc.craftgames;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.configuration.file.YamlConfiguration;

public class GameConfig {

	private HashMap<String, YamlConfiguration> games;
	
	private static Set<String> requiredFields = new HashSet<String>();
	
	public static String NAME = "name";
	public static String GAME_CLASS = "gameClass";
	
	static {
		requiredFields.add(NAME);
		requiredFields.add(GAME_CLASS);
	}
	
	// TODO: Move to another class
	public boolean registerGame(YamlConfiguration configFile) {
		
		if (isValidConfig(configFile)) {
			
			configFile.getString(NAME);
			
			return true;
		}
		
		return false;
	}
	
	public boolean isValidConfig(YamlConfiguration configFile) {
		
		boolean valid = configFile.getKeys(false).containsAll(requiredFields);
		
		// TODO: Check that file has all required class files
		
		return valid;
	}
	
}
