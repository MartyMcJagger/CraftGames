package mcjagger.mc.craftgames;

import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;

import mcjagger.mc.craftgames.api.WeaponBehavior;

public class Weapon {

	public static final String PATH_NAME = "name";
	public static final String PATH_MATERIAL = "material";
	public static final String PATH_CONFIG = "config";
	
	public static final String PATH_MELEE = "behavior.melee";
	public static final String PATH_INTERACT = "behavior.interact";
	public static final String PATH_PRIMARY = "behavior.primary";
	public static final String PATH_SECONDARY = "behavior.secondary";
	
	private final YamlConfiguration yaml;
	
	public final String name;
	public final Material material;
	
	private static int idCount = 0;
	public final int id = idCount++;
	
	public final WeaponBehavior melee;
	public final WeaponBehavior interact;
	public final WeaponBehavior primary;
	public final WeaponBehavior secondary;
	
	public Weapon(YamlConfiguration yaml) {
		this.yaml = yaml;
		
		name = yaml.getString(PATH_NAME);
		material = Material.matchMaterial(yaml.getString(PATH_MATERIAL, "IRON_SWORD"));
		
		if (name == null || name.length() < 1)
			throw new IllegalArgumentException("Weapon name not defined in YAML file.");
		
		melee = getBehaviorFromYaml(PATH_MELEE);
		interact = getBehaviorFromYaml(PATH_INTERACT);
		primary = getBehaviorFromYaml(PATH_PRIMARY);
		secondary = getBehaviorFromYaml(PATH_SECONDARY);
	}
	
	public WeaponBehavior getBehaviorFromYaml(String path) {
		
		if (yaml.isConfigurationSection(path)) {
			return new YamlWeaponBehavior(yaml.getConfigurationSection(path));
		}
		
		String className = yaml.getString(path);
		
		if (className == null)
			return WeaponBehavior.DEFAULT;
		
		
		try {
			@SuppressWarnings("unchecked")
			Class<WeaponBehavior> clazz = (Class<WeaponBehavior>) ClassLoader.getSystemClassLoader().loadClass(className);
			
			if (className.contains(":")) {
				
				String[] split = className.split(":");
				
				if (split.length != 2)
					throw new IllegalArgumentException();
				
				className = split[0];
				String methodName = split[1];
				
				try {
					Method method = clazz.getMethod(methodName, new Class<?>[0]);
					Object obj = method.invoke(null, new Object[0]);
					return (WeaponBehavior) obj;
				} catch (Exception e) {
					Bukkit.getLogger().severe("Unable to find WeaponBehavior method "+path+" in class '"+className+"' from weapon file '"+yaml+"'.");
					Bukkit.getLogger().severe("Ensure method is static, empty parameters, and class is included in export.");
					return WeaponBehavior.DEFAULT;
				}
				
			} else {
				return clazz.newInstance();
			}
			
		} catch (Exception e) {
			
			Bukkit.getLogger().severe("Unable to load WeaponBehavior class '"+className+"' from weapon file '"+yaml+"'.");;
			
			return WeaponBehavior.DEFAULT;
		}
	}
	
}
