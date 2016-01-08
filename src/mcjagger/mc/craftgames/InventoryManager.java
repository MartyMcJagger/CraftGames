package mcjagger.mc.craftgames;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import org.bukkit.GameMode;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import mcjagger.mc.mygames.MyGames;

public class InventoryManager {
	
	public static final String GAME_RESTORE = "gameRestore";
	public static final String WORLD = "world";
	
	private static final String key = "inventoryStore";
	
	private static final String INV_PATH = "inv";
	
	private static final String GAMEMODE_PATH = "gm";
	private static final String ALLOW_FLIGHT_PATH = "allowFlight";
	private static final String MAX_HEALTH_PATH = "maxHealth";
	private static final String HEALTH_PATH = "health";
	private static final String SATURATION_PATH = "saturation";
	private static final String MAX_AIR_PATH = "maxAir";
	private static final String XP_PATH = "xp";
	
	private static final String PLAYMODE_PATH = "playMode";
	
	private static File configFile = null;
	private static FileConfiguration config = null;
		
	public static void saveInventory(Player player, String path) {
		PlayerInventory pi = player.getInventory();
		
		String base64 = itemsToBase64(pi);
		
		FileConfiguration config = getConfig();
		
		MyGames.debug("Saving inv to :" + path + "." + INV_PATH);
		config.set(path + "." + INV_PATH, base64);
		
		saveConfig();
	}
	
	public static boolean applyInventory(Player player, String path) {
		FileConfiguration config = getConfig();
		
		try {
			String base64 = config.getString(path + "." + INV_PATH);
			
			ItemStack[] arr = itemsFromBase64(base64);
			player.getInventory().setContents(arr);
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean savePlayerState(Player player, String path) {
		
		FileConfiguration config = getConfig();
		
		try { save(config, path + "." + GAMEMODE_PATH, player.getGameMode()); } catch (Exception e) {}
		try { save(config, path + "." + ALLOW_FLIGHT_PATH, player.getAllowFlight()); } catch (Exception e) {}
		try { save(config, path + "." + MAX_HEALTH_PATH, player.getMaxHealth()); } catch (Exception e) {}
		try { save(config, path + "." + HEALTH_PATH, player.getHealth()); } catch (Exception e) {}
		try { save(config, path + "." + SATURATION_PATH, player.getSaturation()); } catch (Exception e) {}
		try { save(config, path + "." + MAX_AIR_PATH, player.getMaximumAir()); } catch (Exception e) {}
		try { save(config, path + "." + XP_PATH, player.getTotalExperience()); } catch (Exception e) {}
		
		try { save(config, path + "." + PLAYMODE_PATH, MyGames.getMetadataManager().getMode(player)); } catch (Exception e) {}
		
		saveConfig();
		
		return true;
	}
	
	public static boolean applyPlayerState(Player player, String path) {
		
		
		try { player.setGameMode((GameMode) 	load(config,  	path + "." + GAMEMODE_PATH)); } catch (Exception e) {}
		try { player.setAllowFlight((Boolean)	load(config, 	path + "." + ALLOW_FLIGHT_PATH)); } catch (Exception e) {}
		try { player.setMaxHealth((Double)  	load(config,	path + "." + MAX_HEALTH_PATH)); } catch (Exception e) {}
		try { player.setHealth((Double)   	load(config,	path + "." + HEALTH_PATH)); } catch (Exception e) {}
		try { player.setSaturation((Float)  	load(config,	path + "." + SATURATION_PATH)); } catch (Exception e) {}
		try { player.setMaximumAir((Integer)	load(config,	path + "." + MAX_AIR_PATH)); } catch (Exception e) {}
		try { player.setTotalExperience((Integer)
										load(config,	path + "." + XP_PATH )); } catch (Exception e) {}
		
		try { MyGames.getMetadataManager().setMode(player,
							(Integer)	load(config, 	path + "." + PLAYMODE_PATH)); } catch (Exception e) {}
		
		return true;
	}
	
	private static void save(FileConfiguration config, String path, Object object) {
		String base64 = objectToBase64(object);
		config.set(path, base64);
	}
	
	private static Object load(FileConfiguration config, String path) {
		MyGames.debug("loading: " + path);
		String base64 = config.getString(path);
		return objectFromBase64(base64);
	}
	
	private static void reloadConfig() {
		
		if (configFile == null) {
			configFile = new File(MyGames.getArcade().getDataFolder(), key + ".yml");
		}
		
		config = YamlConfiguration.loadConfiguration(configFile);
	}

	private static void saveConfig() {
		
		if (config == null || configFile == null) {
			return;
		}
		try {
			config.save(configFile);
		} catch (IOException e) {
			MyGames.getLogger().log(Level.SEVERE,
					"Could not save config to " + configFile, e);
		}
	}
	
	private static FileConfiguration getConfig() {
		
		if (config == null) {
			reloadConfig();
		}
		
		return config;
	}
	
	private static String itemsToBase64(PlayerInventory inv) {
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			BukkitObjectOutputStream bout = new BukkitObjectOutputStream(out);
			
			ItemStack[] contents = inv.getContents();
			
			bout.writeInt(contents.length);
			
			for (int n = 0; n < contents.length; n++) {
				bout.writeObject(contents[n]);
			}
			
			bout.close();
			
			String str = Base64Coder.encodeLines(out.toByteArray());
			return str;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private static ItemStack[] itemsFromBase64(String base64) throws IOException, ClassNotFoundException {
		byte[] b = Base64Coder.decodeLines(base64);
		
		ByteArrayInputStream in = new ByteArrayInputStream(b);
		BukkitObjectInputStream bin = new BukkitObjectInputStream(in);
		
		int size = bin.readInt();
		
		ItemStack[] arr = new ItemStack[size];
		for (int n = 0; n < size; ++n) {
			arr[n] = ((ItemStack) bin.readObject());
		}
		
		bin.close();
		return arr;
	}
	
	private static String objectToBase64(Object object) {
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			BukkitObjectOutputStream bout = new BukkitObjectOutputStream(out);
			
			bout.writeObject(object);
			bout.close();
			
			MyGames.debug(object);
			
			String str = Base64Coder.encodeLines(out.toByteArray());
			return str;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private static Object objectFromBase64(String base64) {
		try {
			byte[] b = Base64Coder.decodeLines(base64);
			
			ByteArrayInputStream in = new ByteArrayInputStream(b);
			BukkitObjectInputStream bin = new BukkitObjectInputStream(in);
			
			Object obj = bin.readObject();
			
			bin.close();
			
			MyGames.debug(obj);
			
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
//*/
}
