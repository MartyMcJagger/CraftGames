package mcjagger.mc.craftgames.weapon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import mcjagger.mc.craftgames.api.Playable;

public abstract class Weapon {

	public static final String PATH_NAME = "name";
	public static final String PATH_MATERIAL = "material";
	public static final String PATH_CONFIG = "config";
	
	public static final String PATH_MELEE = "behavior.melee";
	public static final String PATH_INTERACT = "behavior.interact";
	public static final String PATH_PRIMARY = "behavior.primary";
	public static final String PATH_SECONDARY = "behavior.secondary";

	private static Map<Integer, Weapon> weapons = new HashMap<Integer, Weapon>();
	
	public abstract String getName();
	public abstract ItemStack getBaseItem();

	public void primary(Playable playable, Entity owner){}
	public void secondary(Playable playable, Entity owner){}
	public void melee(Playable playable, Entity owner, Entity victim, EntityDamageByEntityEvent event){}
	public void interact(Playable playable, Entity owner, Entity victim, EntityInteractEvent event){}
	
	private static int idCount = 0;
	public final int id = idCount++;
	
	public static boolean isRegistered(Integer id) {
		return weapons.containsKey(id);
	}
	
	public static boolean isRegistered(Weapon weapon) {
		return isRegistered(weapon.id);
	}
	
	public static void register(Weapon weapon) {
		weapons.put(weapon.id, weapon);
	}
	
	public static Weapon getWeapon(Integer id) {
		return weapons.get(id);
	}
	
	public static ItemStack createWeapon(Weapon weapon) {
		
		if (!isRegistered(weapon))
			register(weapon);
		
		ItemStack is = weapon.getBaseItem();
		ItemMeta meta = is.getItemMeta();
		
		List<String> lore = meta.getLore();
		if (lore == null)
			lore = new ArrayList<String>();
		
		lore.add(ChatColor.ITALIC + "Weapon:" + weapon.id);
		
		meta.setLore(lore);
		meta.setDisplayName(weapon.getName());
		
		is.setItemMeta(meta);
		
		return is;
	}
	
	public static boolean isWeapon(ItemStack is) {
		ItemMeta meta = is.getItemMeta();
		List<String> lore = meta.getLore();
		if (lore == null)
			return false;
		
		for (String str : lore) {
			if (str.startsWith(ChatColor.ITALIC + "Weapon:")) {
				return isRegistered(Integer.parseInt(str.substring(str.indexOf(":") + 1)));
			}
		}
		
		return false;
	}
	
	public static Weapon getWeapon(ItemStack is) {
		ItemMeta meta = is.getItemMeta();
		List<String> lore = meta.getLore();
		if (lore == null)
			return null;
		
		for (String str : lore) {
			if (str.startsWith(ChatColor.ITALIC + "Weapon:")) {
				return weapons.get(Integer.parseInt(str.substring(str.indexOf(":") + 1)));
			}
		}
		
		return null;
	}

	
}
