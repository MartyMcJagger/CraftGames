package mcjagger.mc.craftgames.weapon;

import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import mcjagger.mc.craftgames.api.Playable;
import mcjagger.mc.craftgames.api.YamlCode;

public class YamlWeaponBehavior extends WeaponBehavior {
	
	private final YamlCode attackerCode;
	private final YamlCode victimCode;
	private final String message;
	
	public static final String PATH_ATTACKER = "attacker";
	public static final String PATH_VICTIM = "victim";
	public static final String PATH_MESSAGE = "message";
	
	public YamlWeaponBehavior(ConfigurationSection configurationSection) {
		attackerCode = YamlCode.fromConfig(configurationSection, PATH_ATTACKER);
		victimCode = YamlCode.fromConfig(configurationSection, PATH_VICTIM);
		message = configurationSection.getString(PATH_MESSAGE, "");
	}
	
	public static WeaponBehavior getBehaviorFromYaml(ConfigurationSection config, String path) {
		
		if (config.isConfigurationSection(path)) {
			return new YamlWeaponBehavior(config.getConfigurationSection(path));
		}
		
		String className = config.getString(path);
		
		if (className == null)
			return new WeaponBehavior(){};//.DEFAULT;
		
		
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
					Bukkit.getLogger().severe("Unable to find WeaponBehavior method "+path+" in class '"+className+"' from weapon file '"+config+"'.");
					Bukkit.getLogger().severe("Ensure method is static, empty parameters, and class is included in export.");
					return new WeaponBehavior(){};
				}
				
			} else {
				return clazz.newInstance();
			}
			
		} catch (Exception e) {
			
			Bukkit.getLogger().severe("Unable to load WeaponBehavior class '"+className+"' from weapon file '"+config+"'.");;
			
			return new WeaponBehavior(){};
		}
	}

	@Override
	public WeaponResult onAction(Player attacker, Player victim, Playable playable) {
		
		try {
			if (attackerCode != null) {
				attackerCode.invoke(Player.class, attacker);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			if (victimCode != null) {
				victimCode.invoke(Player.class, victim);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (message != null && message.length() > 0) {
			Bukkit.getServer().broadcastMessage(message);
		}
		
		return null;
	}
}
