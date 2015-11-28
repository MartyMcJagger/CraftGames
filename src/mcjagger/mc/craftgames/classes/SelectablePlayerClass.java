package mcjagger.mc.craftgames.classes;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.common.collect.Lists;

import mcjagger.mc.craftgames.Utils;
import mcjagger.mc.craftgames.weapon.Weapon;
import net.md_5.bungee.api.ChatColor;

public abstract class SelectablePlayerClass extends PlayerClass {

	public abstract Material menuIconMaterial();
	public abstract String shortDescription();
	
	public abstract ItemStack[] getPlayerWeapons();
	
	@Override
	public final ItemStack[] getWeapons() {
		ItemStack[] weaps = getPlayerWeapons();
		
		ItemStack[] ret = Arrays.copyOf(weaps, weaps.length +1);
		ret[weaps.length] = Weapon.createWeapon(new PlayerClassChooser());
		
		return ret;
	}
	
	public final ItemStack menuIcon() {
		ItemStack is = Utils.namedStack(menuIconMaterial(), getName());
		ItemMeta im = is.getItemMeta();
		im.setLore(Lists.asList(ChatColor.GREEN+""+ChatColor.BOLD+"Weapons:", weaponNames()));
		is.setItemMeta(im);
		return is;
	}
	
	public final String[] weaponNames() {
		ItemStack[] weapons = getWeapons();
		String[] names = new String[weapons.length];
		
		for (int i = 0; i < weapons.length; i++) {
			ItemStack is = weapons[i];
			ItemMeta im = is.getItemMeta();
			
			if (im.hasDisplayName())
				names[i] = im.getDisplayName();
			else
				names[i] = is.getType().toString();
		}
		
		return names;
	}
}
