package mcjagger.mc.craftgames;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import mcjagger.mc.mygames.ConfigManager;
import mcjagger.mc.mygames.MyGames;

public class SimpleConfigManager extends ConfigManager {
	
	private FileConfiguration defConfig;
	
	public SimpleConfigManager() {
		loadConfig();
	}
	
	/*
	 * (non-Javadoc)
	 * @see mcjagger.mc.mygames.ConfigManager#loadConfig()
	 */
	@Override
	public ConfigurationSection loadConfig() {
		MyGames.getArcade().saveDefaultConfig();
		defConfig = MyGames.getArcade().getConfig();
		return defConfig;
	}
	
	/*
	 * (non-Javadoc)
	 * @see mcjagger.mc.mygames.ConfigManager#saveConfig()
	 */
	@Override
	public void saveConfig() {
		MyGames.getArcade().saveConfig();
	}
	
	/*
	 * (non-Javadoc)
	 * @see mcjagger.mc.mygames.ConfigManager#isLobbyListenerEnabled()
	 */
	@Override
	public boolean isLobbyListenerEnabled() {
		String path = LISTENER_LOBBY;
		
		if (!defConfig.contains(path)) {
			defConfig.set(path, false);
			return false;
		} else {
			return defConfig.getBoolean(path, false);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see mcjagger.mc.mygames.ConfigManager#isSignListenerEnabled()
	 */
	@Override
	public boolean isSignListenerEnabled() {
		String path = LISTENER_SIGN;
		
		if (!defConfig.contains(path)) {
			defConfig.set(path, true);
			return true;
		} else {
			return defConfig.getBoolean(path, true);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see mcjagger.mc.mygames.ConfigManager#isGameEnabled(java.lang.String)
	 */
	@Override
	public boolean isGameEnabled(String gameName) {
		String path = getGameEnabledPath(gameName);
		
		if (!defConfig.contains(path)) {
			defConfig.set(path, true);
			return true;
		} else {
			return defConfig.getBoolean(path, true);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see mcjagger.mc.mygames.ConfigManager#getGameEnabledPath(java.lang.String)
	 */
	@Override
	public String getGameEnabledPath(String gameName) {
		return GAME_PREFIX + gameName + SUFFIX_ENABLED;
	}
	
	/*
	 * (non-Javadoc)
	 * @see mcjagger.mc.mygames.ConfigManager#getSpawnLocation()
	 */
	@Override
	public Location getSpawnLocation() {
		
		if (!(defConfig.getBoolean(SPAWN_WORLD_ENABLED, false)))
			return Bukkit.getWorlds().get(0).getSpawnLocation();
		
		String w = defConfig.getString(SPAWN_WORLD);
		World world = Bukkit.getWorld(w);

		if (!(defConfig.getBoolean(SPAWN_LOCATION_ENABLED, false)))
			return world.getSpawnLocation();
		
		String l = defConfig.getString(SPAWN_LOCATION);
		String[] s = l.split(";");
		
		return new Location(world, Double.parseDouble(s[0]),Double.parseDouble(s[1]),Double.parseDouble(s[2]),
				Float.parseFloat(s[3]), Float.parseFloat(s[4]));
	}
	
}
