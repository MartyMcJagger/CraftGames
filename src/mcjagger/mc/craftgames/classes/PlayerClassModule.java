package mcjagger.mc.craftgames.classes;

import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import mcjagger.mc.craftgames.api.Module;
import mcjagger.mc.craftgames.api.Playable;
import mcjagger.mc.craftgames.inventorymenu.InventoryMenu;
import mcjagger.mc.craftgames.inventorymenu.MenuItem;
import mcjagger.mc.craftgames.inventorymenu.MenuItemListener;

public abstract class PlayerClassModule extends Module implements MenuItemListener {

	private SelectablePlayerClass[] classes;
	
	public InventoryMenu menu = null;
	
	public PlayerClassModule(Playable playable, SelectablePlayerClass[] classes) { 
		super(playable);
		
		this.classes = classes;
		
		ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>();
		
		for (SelectablePlayerClass playerClass : classes) {
			MenuItem mi = new MenuItem(playerClass.menuIcon(), this, true);
			mi.setListenerData(playerClass.getName());
			menuItems.add(mi);
		}
		
		menu = new InventoryMenu("Select a Class!", menuItems);
	}
	
	private void openMenu(UUID uuid) {
		Player player = Bukkit.getPlayer(uuid);
		if (player == null)
			return;
		
		Bukkit.getLogger().log(Level.FINEST, "Opened Menu For " + player.getDisplayName());
		InventoryMenu.openMenu(player, menu);
	}

	@Override
	public void addedPlayer(Player player) {
		openMenu(player.getUniqueId());
	}

	@Override
	public void onClick(Player player, InventoryMenu menu, MenuItem item) {
		
		String data = item.getListenerData();
		
		for (SelectablePlayerClass playerClass : classes) {
			if (playerClass.getName().equals(data)) {
				PlayerClass.setClass(player, playerClass);
				break;
			}
		}
	}
}
