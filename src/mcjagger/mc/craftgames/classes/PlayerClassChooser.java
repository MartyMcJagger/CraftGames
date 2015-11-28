package mcjagger.mc.craftgames.classes;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import mcjagger.mc.craftgames.api.Module;
import mcjagger.mc.craftgames.api.Playable;
import mcjagger.mc.craftgames.inventorymenu.InventoryMenu;
import mcjagger.mc.craftgames.weapon.Weapon;

public class PlayerClassChooser extends Weapon {

	@Override
	public String getName() {
		return "Class Chooser";
	}

	@Override
	public ItemStack getBaseItem() {
		return new ItemStack(Material.BOOK_AND_QUILL);
	}

	@Override
	public void secondary(Playable playable, Entity entity) {
		if (!(entity instanceof Player))
			return;
		
		Player player = (Player) entity;
		
		for (Module m : playable.getModules()) {
			if (m instanceof PlayerClassModule) {
				InventoryMenu.openMenu(player, ((PlayerClassModule)m).menu);
			}
		}
	}
}
