package mcjagger.mc.craftgames.world;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Difficulty;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.common.collect.Lists;

import mcjagger.mc.mygames.MyGames;
import mcjagger.mc.mygames.Utils;
import mcjagger.mc.mygames.game.Game;
import mcjagger.mc.mygames.inventorymenu.InventoryMenu;
import mcjagger.mc.mygames.inventorymenu.MenuItem;
import mcjagger.mc.mygames.inventorymenu.MenuItemListener;
import mcjagger.mc.mygames.inventorymenu.Submenu;
import mcjagger.mc.mygames.world.MapConfigManager;
import mcjagger.mc.mygames.world.location.MapLocation;
import mcjagger.mc.mygames.world.location.SpawnLocation;

// TODO: Override annotations and javadocs
public class SimpleMapConfigManager extends MapConfigManager {

	//private WorldConfigListener wcl;

	public static final String WORLD_LIST_PATH = "world.game.list";
	public static final String MAPLOCATION_MARKER = "MapLocation";
	
	private FileConfiguration defConfig;
	List<String> list;

	private HashMap<String, FileConfiguration> configs = new HashMap<String, FileConfiguration>();
	private HashMap<String, File> configFiles = new HashMap<String, File>();
	

	/*public enum LOCATIONS {
		RED, BLUE, GREEN, YELLOW, BOUNDS1, BOUNDS2
	};*/

	/**
	 * BASIC: Contains 4 spawn points (R+B+G+Y)
	 * CAPTURE: Contains 2 spawn points (R+B) and 1 capture point (G).
	 * PAYLOAD: Contains 2 spawn points (R+B), 1 minecart spawn point (G), 
	 * 		and 1 point at the first block the minecart should travel to. (Y)
	 */
	/*public enum WORLD_TYPE {
		BASIC, CAPTURE, PAYLOAD,
	};*/

	public SimpleMapConfigManager() {


		defConfig = MyGames.getArcade().getConfig();

		loadConfigs();
		
		MyGames.debug(list.toString());
	}

	public void addToList(String string) {
		list.add(string);

		saveConfigs();
		loadConfigs();
	}

	public void removeFromList(String string) {
		list.remove(string);
	}
	
	public List<String> getList() {
		return new ArrayList<String>(list);
	}

	@Override
	public World setWorldOptions(World world) {
		
		world.setAutoSave(false);
		world.setKeepSpawnInMemory(false);
		world.setDifficulty(Difficulty.HARD);
		world.setPVP(true);
		
		world.setAnimalSpawnLimit(0);
		world.setMonsterSpawnLimit(0);
		world.setWaterAnimalSpawnLimit(0);
		world.setAmbientSpawnLimit(0);
		world.setSpawnFlags(false, false);
		
		for (Entity entity : world.getEntities()) {
			if (entity instanceof LivingEntity) {
				if (!(entity instanceof Player)) {
					((LivingEntity) entity).setHealth(0);
					entity.remove();
				}
			}
		}
		
		return world;
	}
	public void loadConfigs() {

		configs = new HashMap<String, FileConfiguration>();
		configFiles = new HashMap<String, File>();

		list = defConfig.getStringList(WORLD_LIST_PATH);

		for (String key : list)
			configs.put(key, getConfig(key));
		
	}
	public void saveConfigs() {

		defConfig.set(WORLD_LIST_PATH, list);

		for (String key : list)
			saveConfig(key);

		MyGames.getConfigManager().saveConfig();
	}

	/*public String getTypePath() {
		return "type";
	}*/

	/*
	 * Methods for configuration reloading, getting, and saving.
	 */

	@SuppressWarnings("deprecation")
	public void reloadConfig(String key) {
		
		FileConfiguration config = configs.get(key);
		File file = configFiles.get(key);
		
		if (file == null) {
			file = new File(MyGames.getArcade().getDataFolder(), key + ".yml");
		}
		config = YamlConfiguration.loadConfiguration(file);
		
		// Look for defaults in the jar
		InputStream defConfigStream = MyGames.getArcade().getResource(key + ".yml");
		if (defConfigStream != null) {
			YamlConfiguration defConfig = YamlConfiguration
					.loadConfiguration(defConfigStream);
			config.setDefaults(defConfig);
		}
		
		configs.put(key, config);
		configFiles.put(key, file);
	}

	public void saveConfig(String key) {
		
		FileConfiguration config = configs.get(key);
		File file = configFiles.get(key);
		
		if (config == null || file == null) {
			return;
		}
		try {
			getConfig(key).save(file);
		} catch (IOException e) {
			MyGames.getLogger().log(Level.SEVERE,
					"Could not save config to " + file, e);
		}
	}

	// TODO: return false?
	public boolean saveMapToReferences(String key) {
		MapCopyManager.saveWorldToReferences(key);;
		return true;
	}
	
	public FileConfiguration getConfig(String key) {
		
		FileConfiguration config = configs.get(key);
		
		if (config == null) {
			reloadConfig(key);
		}
		return configs.get(key);
	}
	
	public boolean hasLocation(String worldKey,
			String locationKey) {
		String path = "location." + locationKey;
		
		FileConfiguration config = getConfig(worldKey);
		return config.contains(path);
	}
	
	public List<Double> getLocation(String worldKey,
			String locationKey) {
		
		String path = "location." + locationKey;
		
		FileConfiguration config = getConfig(worldKey);
		

		List<Double> d;
		if (config.isConfigurationSection(path)) {
			Set<String> keys = config.getConfigurationSection(path).getKeys(false);
			String randomKey = Utils.getRandomItem(keys);
			d = config.getConfigurationSection(path).getDoubleList(randomKey);
		} else {
			d = config.getDoubleList(path);
		}
		if (d != null && d.size() == 3)
			return d;//return new Location(key, d.get(0), d.get(1), d.get(2));
		else
			return null;
	}
	/*
	public List<Double> getRandomLocation(String key) {
		
		ArrayList<LOCATIONS> locs = new ArrayList<LOCATIONS>();
		locs.add(LOCATIONS.RED);
		locs.add(LOCATIONS.BLUE);
		locs.add(LOCATIONS.GREEN);
		locs.add(LOCATIONS.YELLOW);

		Random r = new Random();
		while (locs.size() > 0) {
			int index = r.nextInt(locs.size());
			List<Double> l = getLocation(key, locs.get(index));
			if (l != null) {
				return l;
			}
		}
		
		return null;
	}
*/
	@Override
	public void setLocation(String key,
			String locationKey, Location loc) {

		String path = "location." + locationKey;

		FileConfiguration config = getConfig(key);
		List<Double> d = Lists.newArrayList(new Double[] { loc.getX(),
				loc.getY(), loc.getZ() });
		config.set(path, d);
		
		saveConfig(key);
	}
	
	@Override
	public void addLocation(String key, String locationKey, Location loc) {
		String path = "location." + locationKey + ".";
		int i = 0;
		
		FileConfiguration config = getConfig(key);
	
		while (config.contains(path + i))
			i++;
		
		path += i;
		
		List<Double> d = Lists.newArrayList(new Double[] { loc.getX(),
				loc.getY(), loc.getZ() });
		
		config.set(path, d);

		saveConfig(key);
	}
	
	@Override
	public void removeLocation(String key, String locationKey, Location loc) {
		String path = "location." + locationKey;
		
		FileConfiguration config = getConfig(key);
		
		List<Double> d = Lists.newArrayList(new Double[] { loc.getX(),
				loc.getY(), loc.getZ() });
		
		if (config.isConfigurationSection(path)) {
			ConfigurationSection sect = config.getConfigurationSection(path);
			for (String subkey : sect.getKeys(false)) {
				
				List<Double> compare = sect.getDoubleList(subkey);
				
				if (d.equals(compare)) {
					sect.set(subkey, null);
					break;
				}
			}
			MyGames.debug("Is a section");
		}
		else {
			List<Double> compare = config.getDoubleList(path);
			
			if (d.equals(compare)) {
				config.set(path, null);
			}
			MyGames.debug("Not a section");
		}
		
		saveConfig(key);
	}
	
	@Override
	public boolean isMapLocation(Location loc) {
		Block block = loc.getBlock();
		
		if (!(block.getState() instanceof Sign))
			return false;
		
		Sign sign = (Sign) block.getState();
		return sign.getLine(0).equals(MAPLOCATION_MARKER);
	}
	
	@Override
	public void markMapLocation(MapLocation mapLocation, Location location) {
		markMapLocation(mapLocation.configKey(), mapLocation.canHaveMultiple(), location);
	}
	
	@Override
	public void markMapLocation(String configKey, boolean multiple, Location loc) {
		Block block = loc.getBlock();
		
		block.setType(Material.SIGN_POST);
		block.setType(Material.SIGN_POST, false);
		
		Sign sign = (Sign) block.getState();
		sign.setLine(0, MAPLOCATION_MARKER);
		sign.setLine(1, configKey);
		sign.setLine(2, multiple + "");
		
		sign.update();
	}
	
	public void mapLocationBroken(Location loc) {
		Block block = loc.getBlock();
		
		if (!(block.getState() instanceof Sign))
			throw new IllegalArgumentException();
		
		Sign sign = (Sign) block.getState();
		if (!sign.getLine(0).equals(MAPLOCATION_MARKER))
			throw new IllegalArgumentException();
		
		String locationKey = sign.getLine(1);
		removeLocation(loc.getWorld().getName(), locationKey, loc);
	}
	
/*
	private String getLocationPath(LOCATIONS l) {
		String key = "location";
		switch (l) {
		case RED:
			key += ".spawn.red";
			break;
		case BLUE:
			key += ".spawn.blue";
			break;
		case GREEN:
			key += ".spawn.green";
			break;
		case YELLOW:
			key += ".spawn.yellow";
			break;
		case BOUNDS1:
			key += ".bounds1";
			break;
		case BOUNDS2:
			key += ".bounds2";
			break;
		}
		return key;
	}
	
	/*
	public WORLD_TYPE getWorldType(String key) {
		if (configs.containsKey(key)){
			String s = getConfig(key).getString(getTypePath(), WORLD_TYPE.BASIC.toString());
			WORLD_TYPE type = WORLD_TYPE.valueOf(s);
			if (type == null) {
				getConfig(key).set(getTypePath(), WORLD_TYPE.BASIC.toString());
				type = WORLD_TYPE.BASIC;
			}
			return type;
		}
		return null;
	}
	
	public void setWorldType(String key, WORLD_TYPE worldType) {
		if (configs.containsKey(key)){
			getConfig(key).set(getTypePath(), worldType.toString());
			saveConfigs();
		}
	}*/

	/*
	 * Methods for world config items
	 */

	/*public static ItemStack getSpawnTool(DyeColor color) {
		Wool wool = new Wool();
		wool.setColor(color);

		ItemStack is = wool.toItemStack();
		is.setAmount(1);
		ItemMeta meta = is.getItemMeta();
		meta.setDisplayName("Tool: " + color.toString());

		List<String> lore = Lists
				.newArrayList(new String[] { "Place block at desired spawn point." });

		meta.setLore(lore);
		is.setItemMeta(meta);

		return is;
	}

	public static ItemStack getBoundsTool(int i) {
		Wool wool = new Wool();
		wool.setColor((i == 1) ? DyeColor.WHITE : DyeColor.BLACK);

		ItemStack is = wool.toItemStack();
		is.setAmount(i);
		ItemMeta meta = is.getItemMeta();
		meta.setDisplayName("Tool: bounds" + i);

		List<String> lore = Lists
				.newArrayList(new String[] { "Place block at desired spawn point." });

		meta.setLore(lore);
		is.setItemMeta(meta);

		return is;
	}//*/
	
	public InventoryMenu getConfigMenu() {
		Submenu spawns = new Submenu(Utils.namedStack(Material.BONE, "Spawns"), getSpawnMenu());
		Submenu customs = new Submenu(Utils.namedStack(Material.WORKBENCH, "Game Specific"), getWorldLocMenu());
		
		return new InventoryMenu("Game World Setup", Lists.newArrayList(new MenuItem[]{spawns, customs}));
	}
	
	private InventoryMenu getSpawnMenu() {
		ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>();
		for (DyeColor color : DyeColor.values()) {
			menuItems.add(getMenuItem(new SpawnLocation(color)));
		}
		return new InventoryMenu("Spawn Locations", menuItems);
	}
	
	private InventoryMenu getWorldLocMenu() {	
		ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>();
		MyGames.debug(MyGames.getLobbyManager().getGameNames());
		for (String gameName : MyGames.getLobbyManager().getGameNames()) {
			Game game = MyGames.getLobbyManager().getGame(gameName);
			MapLocation[] locs = game.getLocationTypes();
			if (locs == null || locs.length <= 0) {
				MyGames.debug("No locations for " + gameName);
				continue;
			}
			
			menuItems.add(new Submenu(Utils.namedStack(Material.WORKBENCH, gameName), getWorldLocMenu(game)));
		}
		return new InventoryMenu("Games", menuItems);
	}
	
	private InventoryMenu getWorldLocMenu(Game game) {
		ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>();
		MapLocation[] locs = game.getLocationTypes();
		for (MapLocation loc : locs) {
			menuItems.add(getMenuItem(loc));
		}
		return new InventoryMenu(game.getName(), menuItems);
	}
	
	@Override
	public MenuItem getMenuItem(MapLocation wl) {
		return new MenuItem(getTool(wl), new MenuItemListener() {

			@Override
			public void onClick(Player player, InventoryMenu menu, MenuItem item) {
				player.getInventory().addItem(item.getIcon());
			}
			
		});
	}
	
	@Override
	public ItemStack getTool(MapLocation wl) {
		ItemStack stack = wl.icon();
		ItemMeta meta = stack.getItemMeta();
		
		meta.setDisplayName("World Config Tool: \""+wl.configKey() + '"');
		meta.setLore(Lists.asList(ChatColor.GREEN + "World Config Tool", new String[]{wl.configKey(), wl.canHaveMultiple() + ""}));		
		
		stack.setItemMeta(meta);
		return stack;
	}

	@Override
	public String getKeyFromTool(ItemStack itemStack) {
		try {
			if (itemStack == null) {
				//Bukkit.getLogger().info("ItemStack == null");
			}
			
			ItemMeta meta = itemStack.getItemMeta();
			
			if (meta == null) {
			//	Bukkit.getLogger().info("meta == null");
			}
			
			String displayName = meta.getDisplayName();
			
			if (displayName == null) {
			//	Bukkit.getLogger().info("displayName == null");
			}
			
			String nameKey = displayName.substring(displayName.indexOf("\"")+1, displayName.lastIndexOf("\""));
			String loreKey = ChatColor.stripColor(meta.getLore().get(1));
			
			Bukkit.getLogger().info("[MyGames] WorldConfigTool Processing:" + nameKey + ((nameKey.equals(loreKey))?"==":"=/=") + loreKey);
			
			return nameKey;
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public boolean canHaveMultiple(ItemStack itemStack) {
		try {
			if (this.getKeyFromTool(itemStack) == null)
				return false;
			
			ItemMeta meta = itemStack.getItemMeta();
			Boolean multiple = Boolean.parseBoolean(ChatColor.stripColor(meta.getLore().get(2)));
			
			return multiple;
		} catch (Exception e) {
			return false;
		}
	}

	public ItemStack getConfigMenuItem() {
		return Utils.namedStack(Material.WORKBENCH, ChatColor.GREEN + "Location Menu");
	}
	
	public boolean isConfigMenuItem(ItemStack is) {
		try {
			return is.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GREEN + "Location Menu");
		} catch (Exception e) { //No Display name
			return false;
		}
	}
}
