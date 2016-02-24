package mcjagger.mc.craftgames;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Wool;

import mcjagger.mc.mygames.MyGames;
import mcjagger.mc.mygames.Utils;
import mcjagger.mc.mygames.game.Game;
import mcjagger.mc.mygames.game.GameState;
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
			Game game = MyGames.getLobbyManager().getGame(s);
			
			if (game == null)
				continue;
			
			ItemStack is;
			Wool wool = new Wool();
			List<String> lore = new ArrayList<String>();
			
			if ((game.state == GameState.RUNNING && !game.allowJoinInProgress) || game.state == GameState.COOLING_DOWN) { 
				wool.setColor(DyeColor.RED);
				lore.add(ChatColor.RED + "IN PROGRESS");
			} else if (game.state == GameState.RUNNING && game.playerCount() < game.maxPlayers) {
				wool.setColor(DyeColor.ORANGE);
				lore.add(ChatColor.GOLD + "JOIN IN PROGRESS");
			}  else if (game.playerCount() > game.maxPlayers) {
				wool.setColor(DyeColor.RED);
				lore.add(ChatColor.RED + "FULL");
			} else if (game.state == GameState.STOPPED){
				wool.setColor(DyeColor.GREEN);
				lore.add(ChatColor.GREEN + "JOIN NOW");
			}
			
			lore.add(game.playerCount() + " playing");
			
			is = wool.toItemStack();
			ItemMeta im = is.getItemMeta();
			im.setLore(lore);
			is.setItemMeta(im);
			
			ret.add(new MenuItem(Utils.name(is, s), this, s, true));
		}
		MyGames.debug(ret);
		
		return ret;
	}

	@Override
	public void onClick(Player player, InventoryMenu menu, MenuItem item) {
		MyGames.getLobbyManager().addPlayer(player, item.getActionCommand());
	}

}
