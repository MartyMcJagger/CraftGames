package mcjagger.mc.craftgames;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import mcjagger.mc.craftgames.api.Playable;
import mcjagger.mc.craftgames.api.WeaponBehavior;
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
