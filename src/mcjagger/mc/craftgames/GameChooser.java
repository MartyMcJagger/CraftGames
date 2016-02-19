package mcjagger.mc.craftgames;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import mcjagger.mc.mygames.MyGames;
import mcjagger.mc.mygames.Utils;
import mcjagger.mc.mygames.inventorymenu.InventoryMenu;
import mcjagger.mc.mygames.inventorymenu.MenuItem;
import mcjagger.mc.mygames.inventorymenu.MenuItemListener;
import mcjagger.mc.mygames.weapon.ItemWeapon;

public class GameChooser extends ItemWeapon implements MenuItemListener {
	
	@Override
	public String getName() {
		return "Game Chooser";
	}

	@Override
	public void registerNecessaryListeners() {	}

	@Override
	public void primary(Player player, PlayerInteractEvent event) {
		openMenu(player);
	}

	@Override
	public void secondary(Player player, PlayerInteractEvent event) {
		openMenu(player);
	}

	@Override
	public void melee(Player player, Player victim,
			EntityDamageByEntityEvent event) {
		openMenu(player);
		event.setCancelled(true);
	}

	@Override
	public void interact(Player player, Entity target,
			PlayerInteractEntityEvent event) {
		openMenu(player);
	}

	@Override
	public ItemStack getBaseItem() {
		return new ItemStack(Material.IRON_SWORD);
	}
	
	private void openMenu(Player player) {
		InventoryMenu.openMenu(player,
				new InventoryMenu("Select a Game!",getMenuItems()));
	}
	
	private ArrayList<MenuItem> getMenuItems() {
		ArrayList<MenuItem> ret = new ArrayList<MenuItem>();
		for (String s : MyGames.getLobbyManager().getGames()) {
			ret.add(new MenuItem(Utils.namedStack(Material.IRON_SWORD, s), this, s, true));
		}
		MyGames.debug(ret);
		
		return ret;
	}

	@Override
	public void onClick(Player player, InventoryMenu menu, MenuItem item) {
		MyGames.getLobbyManager().addPlayer(player, item.getActionCommand());
	}

}
