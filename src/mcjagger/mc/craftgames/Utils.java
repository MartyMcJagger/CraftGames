package mcjagger.mc.craftgames;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.MetadataValue;

import com.google.common.collect.Lists;

public class Utils {
	
	public static String list(Collection<? extends Object> elements, ChatColor baseColor, ChatColor itemColor) {
		String ret = "";
		
		ArrayList<Object> list = Lists.newArrayList(elements);
		
		if (list.size() == 2) {
			ret = itemColor + list.get(0).toString() + baseColor + " and "
					+ itemColor + list.get(1).toString();
		} else if (list.size() >= 1) {
			for (int i = 0; i < list.size(); i++) {
				ret +=baseColor + ((i==0)?"":", ")
						+ (((i==list.size()-1) && (i != 0))?"and ":"") 
						+ itemColor + list.get(i).toString();
			}
		}
		
		return ret;
	}
	
	public static String list(Collection<? extends Object> elements, ChatColor baseColor) {
		return list(elements, baseColor, ChatColor.RESET);
	}
	
	public static String list(Collection<? extends Object> elements) {
		return list(elements, ChatColor.RESET, ChatColor.RESET);
	}
	
	public static boolean hasMetadataValue(List<MetadataValue> metas, Object value) {
		for (MetadataValue meta : metas) {
			try {
				Object metaValue = meta.value();
				if (metaValue == value || metaValue.equals(value)) {
					return true;
				}
			} catch (Exception e) {
				continue;
			}
		}
		return false;
	}
	
	public static void fakeDeath(Location loc) {
		Villager villager = (Villager) loc.getWorld().spawnEntity(loc, EntityType.VILLAGER);
		villager.playEffect(EntityEffect.HURT);
		villager.playEffect(EntityEffect.DEATH);
		villager.setHealth(0);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getRandomItem(Collection<T> items) {
		Random r = new Random();
		return (T) items.toArray()[r.nextInt(items.size())];
	}
	
	public static ItemStack namedStack(Material mat, String name, int amount) {
		ItemStack is = namedStack(mat, name);
		is.setAmount(amount);
		return is;
	}
	
	public static ItemStack namedStack(Material mat, String name) {
		ItemStack is = new ItemStack(mat);
		return name(is, name);
	}
	
	public static ItemStack name(ItemStack is, String name) {
		ItemMeta meta = is.getItemMeta();
		
		meta.setDisplayName(name);
		is.setItemMeta(meta);
		
		return is;
	}

}
