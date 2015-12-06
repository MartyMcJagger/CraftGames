package mcjagger.mc.craftgames.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import com.google.common.collect.Lists;

import mcjagger.mc.mygames.Game;
import mcjagger.mc.mygames.MyGames;
import mcjagger.mc.mygames.world.MapManager;

//TODO Override annotations and javadocs
public final class SimpleMapManager extends MapManager {
	
	private HashMap<String, String> gameWorlds = new HashMap<String, String>();
	
	private MapCopyManager wcm = new MapCopyManager();
	
	public void unloadAll() {
		wcm.unloadAll();
	}
	
	public void addToList(String string) {
		MyGames.getMapConfigManager().addToList(string);
	}
	public void removeFromList(String string) {
		MyGames.getMapConfigManager().removeFromList(string);
	}
	
	
	
	public boolean setMapName(Game game, String key) {
		if (!wcm.keyIsCompatible(key, game))
			return false;
		
		String oldWorld = gameWorlds.get(game);
		World copy = wcm.getWorldCopy(key);
		
		gameWorlds.put(game.getName(), copy.getName());
		
		MyGames.getLobbyManager().sendPlayers(game);
		
		game.restart();
		
		wcm.unloadWorld(oldWorld);
		if (wcm.isKey(oldWorld))
			wcm.destroyCopy(oldWorld);
		
		return true;
	}
	
	public String getMapName(Game game) {
		return gameWorlds.get(game.getName());
	}
	
	public World getWorld(Game game) {
		String worldName = getMapName(game);
		return Bukkit.getWorld(worldName);
	}
	
	
	
	//TODO: Add a bunch of checks
	protected boolean isKey(String mapname) {
		try {
			int endIndex = mapname.lastIndexOf('_');
			String suffix = mapname.substring(endIndex);
			Integer.valueOf(suffix);
			return false;
		} catch (Exception e) {
			return true;
		}
	}
	

	public Location getRandomSpawn(Game game) {
		
		String key = getMapName(game);
		World world = getWorld(game);
		
		if (key == null || world == null)
			return null;
		
		FileConfiguration config = MyGames.getMapConfigManager().getConfig(key);
		
		ConfigurationSection section = config.getConfigurationSection("location.spawn");
		ArrayList<String> keys = Lists.newArrayList(section.getKeys(false));
		
		Location loc = null;
		
		Random r = new Random();
		while (keys.size() > 0) {
			
			int index = r.nextInt(keys.size());

			loc = MapManager.listToLoc(MyGames.getMapConfigManager().getLocation(key, "spawn."+keys.get(index)), world);
			if (loc != null) {
				return loc;
			}
			else {
				keys.remove(index);
			}
		}
		
		MyGames.getLogger().log(Level.SEVERE, "There are no spawns in world " + key);
		return null;
	}

	public Location getLocation(Game game, String locationKey) {
		World world = getWorld(game);
		return MapManager.listToLoc(MyGames.getMapConfigManager().getLocation(MapCopyManager.getKey(world.getName()), locationKey), world);
	}

	@Override
	public String getRandomMap(Game game) {
		String key = wcm.getRandomKey(game);
		World world = wcm.getWorldCopy(key);
		return world.getName();
	}

	@Override
	public boolean isMapCompatible(String worldName, Game game) {
		return wcm.keyIsCompatible(MapCopyManager.getKey(worldName), game);
	}
}
