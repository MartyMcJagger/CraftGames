package mcjagger.mc.craftgames.world;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

import mcjagger.mc.craftgames.InventoryManager;
import mcjagger.mc.mygames.MyGames;
import mcjagger.mc.mygames.Utils;
import mcjagger.mc.mygames.game.Game;
import mcjagger.mc.mygames.world.location.MapLocation;
import mcjagger.mc.mygames.world.location.SpawnLocation;

// TODO Override annotations and javadocs
public final class MapCopyManager {
	
	private static HashMap<String, World> dupes = new HashMap<String, World>();
	
	public void unloadAll() {
		Set<String> keySet = dupes.keySet();
		for (String string : keySet) {
			destroyCopy(string);
		}
	}
	
	public static boolean hasWorld(World world) {
		return dupes.values().contains(world);
	}
	
	public void addToList(String string) {
		MyGames.getMapConfigManager().addToList(string);
	}
	public void removeFromList(String string) {
		MyGames.getMapConfigManager().removeFromList(string);
	}
	
	public String getRandomKey(Game game) {
		return Utils.getRandomItem(getCompatibleWorlds(game));
	}
	
	public World getRandomWorldCopy(Game game) {
		String key = getRandomKey(game);
		return getWorldCopy(key);
	}
	
	public boolean keyIsCompatible(String key, Game game) {
		if (game.getLocationTypes() != null) {
			for (MapLocation loc : game.getLocationTypes()) {
				if (!MyGames.getMapConfigManager().hasLocation(key, loc.configKey()))
					return false;
			}
		}
		
		if (game.getSpawnColors() != null) {
			for (DyeColor color : game.getSpawnColors()) {
				SpawnLocation loc = new SpawnLocation(color);
				if (!MyGames.getMapConfigManager().hasLocation(key, loc.configKey()))
					return false;
			}
		} else {
			try {
				if (MyGames.getMapConfigManager().getConfig(key)
						.getConfigurationSection("location.spawn")
						.getKeys(false).isEmpty()) {
					return false;
				}
			} catch (Exception ignored) {
				return false;
			}
		}
		MyGames.debug(key + " is compatible for " + game.getName());
		return true;
	}

	public Set<String> getCompatibleWorlds(Game game) {
		Set<String> compatible = new HashSet<String>();
		
		for (String key : MyGames.getMapConfigManager().getList()) {
			if (keyIsCompatible(key, game))
				compatible.add(key);
		}
		
		MyGames.debug("Compatible:" + compatible.toString());
		return compatible;
	}
	
	public World getWorldCopy(String key) {
		MyGames.debug(key + " in " + MyGames.getMapConfigManager().getList().toString());
		if (!MyGames.getMapConfigManager().getList().contains(key))
			return null;
		
		String worldname = getAvailableName(key);
		
		copyWorld(key, worldname);
		
		World world = Bukkit.getServer().createWorld(new WorldCreator(worldname));
		world = MyGames.getMapConfigManager().setWorldOptions(world);
		dupes.put(worldname, world);
		
		return world;
	}
	
	/**
	 * HOLD ON TIGER! CALLING THIS METHOD COULD DELETE YOUR MAPS!
	 */
	public boolean destroyCopy(String mapname) {
		if (isKey(mapname) || !dupes.containsKey(mapname)) {
			//Bukkit.broadcastMessage("Almost deleted map " + mapname + "! Tell your devs to be careful with mcjagger.mc.craftgames.WorldCopyManager$destroyCopy(String)!");
			MyGames.getLogger().severe("Almost deleted map " + mapname + "! Tell your devs to be careful with mcjagger.mc.craftgames.WorldCopyManager$destroyCopy(String)!");
			return false;
		}
		
		World world = dupes.get(mapname);
		File directory = world.getWorldFolder();
		
		clearWorld(world);
		if (unloadWorld(mapname)) {
			dupes.remove(mapname);
			return deleteWorld(directory);
		}
		return false;
	}
	
	public static String getWorldReferenceFolder() {
		return MyGames.getArcade().getDataFolder().getAbsolutePath() + File.separator + "worlds";
	}
	
	public static void saveWorldToReferences(String key, World world) {
		File source = world.getWorldFolder();
		File target = new File(getWorldReferenceFolder() + File.separator + key);
		
		copyWorld(source, target);
	}
	
	public static void saveWorldToReferences(String key) {
		File source = new File(Bukkit.getWorldContainer().getAbsolutePath() + File.separator + key);
		File target = new File(getWorldReferenceFolder() + File.separator + key);
		
		copyWorld(source, target);
	}
	
	private static void copyWorld(String key, String mapname) {
		File source = new File(getWorldReferenceFolder() + File.separator + key);
		File target = new File(Bukkit.getWorldContainer().getAbsolutePath() + File.separator + mapname);
		
		copyWorld(source, target);
	}
	
	private static void copyWorld(File source, File target){
		//MyGames.getLogger().info("copying from " + source.getPath() + " to " + target.getPath());
		try {
			ArrayList<String> ignore = new ArrayList<String>(Arrays.asList("uid.dat", "session.dat"));
			if(!ignore.contains(source.getName())) {
				if(source.isDirectory()) {
					if(!target.exists())
						target.mkdirs();
					String files[] = source.list();
					for (String file : files) {
						File srcFile = new File(source, file);
						File destFile = new File(target, file);
						copyWorld(srcFile, destFile);
					}
				} else {
					InputStream in = new FileInputStream(source);
					OutputStream out = new FileOutputStream(target);
					byte[] buffer = new byte[1024];
					int length;
					while ((length = in.read(buffer)) > 0)
						out.write(buffer, 0, length);
					in.close();
					out.close();
				}
			}
		} catch (IOException e) {
		 
		}
	}
	
	private void clearWorld(World world) {
		for (Player player : world.getPlayers()) {
			
			MyGames.toLobby(player);
			
			//InventoryManager.applyInventory(player, "lobby." + player.getUniqueId());
			//InventoryManager.applyPlayerState(player, "lobby." + player.getUniqueId());
		}
	}
	
	public boolean unloadWorld(String mapname) {
		if (Bukkit.getServer().unloadWorld(mapname, false)) {
			//Bukkit.broadcastMessage("Successfully unloaded " + mapname);
			MyGames.getLogger().info("Successfully unloaded " + mapname);
			dupes.remove(mapname);
			return true;
		} else {
			MyGames.getLogger().severe("COULD NOT UNLOAD " + mapname);
			return false;
		}
	}
	
	private boolean deleteWorld(File path) {
		if (path.exists()) {
			File files[] = path.listFiles();
			for (File file : files) {
				if (file.isDirectory())
					deleteWorld(file);
				else
					file.delete();
			}
		}
		return path.delete();
	}
	
	// TODO: Add a bunch of checks?
	public boolean isKey(String mapname) {
		
		return ! mapname.matches("(\\w)+_(\\d)+"); // HOORAH for Regular Expressions!
		/*
		try {
			int endIndex = mapname.lastIndexOf('_');
			String suffix = mapname.substring(endIndex);
			Integer.valueOf(suffix);
			return false;
		} catch (Exception e) {
			return true;
		}*/
	}
	
	public static String getKey(String mapname) {
		int endIndex = mapname.lastIndexOf('_');
		if (endIndex > 0)
			mapname = mapname.substring(0, endIndex);
		return mapname;
	}
	
	protected String getAvailableName(String key){
		int id = 1;
		
		while (dupes.containsKey(key + "_" + id))
			id++;
		
		return key + "_" + id;
	}
}
