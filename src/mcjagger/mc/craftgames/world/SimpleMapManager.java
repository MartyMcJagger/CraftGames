package mcjagger.mc.craftgames.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import com.google.common.collect.Lists;

import mcjagger.mc.mygames.MyGames;
import mcjagger.mc.mygames.game.Game;
import mcjagger.mc.mygames.world.MapManager;
import mcjagger.mc.mygames.world.location.MapLocation;

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
	
	
	
	public boolean setMapName(Game game, String name) {
		
		String oldWorld = gameWorlds.get(game.getName());
		
		if (name == null) {
			gameWorlds.remove(game.getName());
		} else {
			
			if (wcm.isKey(name)) {
				
				MyGames.debug(name + " is a key.");
				
				String key = (name);
				if (!wcm.keyIsCompatible(key, game))
					return false;
				
				World copy = wcm.getWorldCopy(key);
				name = copy.getName();
			}
			
			gameWorlds.put(game.getName(), name);	
		}
		
		if (oldWorld != null) {
			if (wcm.isKey(oldWorld))
				wcm.destroyCopy(oldWorld);
		}
		
		return true;
	}
	
	public String getMapName(Game game) {
		String worldName = gameWorlds.get(game.getName());

		if (worldName == null) {
			worldName = getRandomMap(game);
			
			if (worldName == null)
				throw new IllegalArgumentException();
			
			setMapName(game, worldName);
		}
		
		return MapCopyManager.getKey(worldName);
	}
	
	public World getWorld(Game game) {
		String worldName = gameWorlds.get(game.getName());
		
		if (worldName == null) {
			worldName = getRandomMap(game);
			
			if (worldName == null)
				throw new IllegalArgumentException();
			
			setMapName(game, worldName);
		}
		
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
			return mapname != null;
		}
	}
	

	public Location getRandomSpawn(Game game) {
		
		String key = getMapName(game);
		World world = getWorld(game);
		
		if (key == null || world == null) {
			throw new NullPointerException();
			
			//MyGames.debug(key + " " + world);
			//return null;
		}
		
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
		
		MyGames.debug("There are no spawns in world " + key);
		throw new NullPointerException();
	}

	public Location getRandomLocation(Game game, MapLocation... locations) {
		
		String key = getMapName(game);
		World world = getWorld(game);
		
		if (key == null || world == null) {
			throw new NullPointerException();
			
			//MyGames.debug(key + " " + world);
			//return null;
		}
		
		FileConfiguration config = MyGames.getMapConfigManager().getConfig(key);
		
		ArrayList<String> keys = new ArrayList<String>();
		for (MapLocation mapLocation : locations) {
			
			if (config.isConfigurationSection(mapLocation.configKey())) {
				ConfigurationSection section = config.getConfigurationSection(mapLocation.configKey());
				keys.addAll(section.getKeys(false));	
			} else {
				keys.add(mapLocation.configKey());
			}
			
		}
		
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
		
		MyGames.debug("There are no spawns in world " + key);
		throw new NullPointerException();
	}
	
	public Location getLocation(Game game, String locationKey) {
		World world = getWorld(game);
		
		if (world == null)
			return null;
		
		return MapManager.listToLoc(MyGames.getMapConfigManager().getLocation(MapCopyManager.getKey(world.getName()), locationKey), world);
	}

	@Override
	public String getRandomMap(Game game) {
		String key = wcm.getRandomKey(game);
		
		if (key == null) {
			MyGames.getLogger().severe("No Worlds exist for game " + game.getName() + ".");
			return null;
		}
		
		World world = wcm.getWorldCopy(key);
		return world.getName();
	}

	@Override
	public boolean isMapCompatible(String worldName, Game game) {
		return wcm.keyIsCompatible(MapCopyManager.getKey(worldName), game);
	}

	@Override
	public boolean unloadMap(Game game, boolean removePlayers) {
		setMapName(game, null);
		return true;
	}
}
