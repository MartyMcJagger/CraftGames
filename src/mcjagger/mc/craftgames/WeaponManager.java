package mcjagger.mc.craftgames;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class WeaponManager implements Listener {
	
	Map<Integer, Weapon> weapons = new HashMap<Integer, Weapon>();
	
	public boolean isRegistered(Integer id) {
		return weapons.containsKey(id);
	}
	
	public boolean isRegistered(Weapon weapon) {
		return isRegistered(weapon.id);
	}
	
	public void register(Weapon weapon) {
		weapons.put(weapon.id, weapon);
	}
	
	public Weapon getWeapon(Integer id) {
		return weapons.get(id);
	}
	
	public ItemStack createWeapon(Weapon weapon) {
		ItemStack is = new ItemStack(weapon.material);
		ItemMeta meta = is.getItemMeta();
		
		List<String> lore = meta.getLore();
		if (lore == null)
			lore = new ArrayList<String>();
		
		lore.add(ChatColor.ITALIC + "Weapon:" + weapon.id);
		
		meta.setLore(lore);
		meta.setDisplayName(weapon.name);
		
		is.setItemMeta(meta);
		
		return is;
	}
	
	public boolean isWeapon(ItemStack is) {
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
	
	public Weapon getWeapon(ItemStack is) {
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
