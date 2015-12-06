package mcjagger.mc.craftgames.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BlockIterator;

import mcjagger.mc.mygames.MetadataManager;
import mcjagger.mc.mygames.MyGames;
import mcjagger.mc.mygames.inventorymenu.InventoryMenu;

public class WorldConfigListener implements Listener {

	/*@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		if (setBlock(event.getPlayer(), event.getItemInHand(), event.getBlock())) {
			event.setCancelled(true);
		}
	}*/
	
	@EventHandler
	public void onPlayerClickTool(PlayerInteractEvent event) {
		Location location = null;
		
		try {
			Block clicked = event.getClickedBlock();
			
			if ((clicked != null) && (event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
				Block relative = clicked.getRelative(event.getBlockFace());
				location = relative.getLocation();
			} else {
				BlockIterator iterator = new BlockIterator(event.getPlayer(), 16);
				Block target = null;
				
				while (iterator.hasNext()) {
					Block block = iterator.next();
					if (block.getType() != Material.AIR) {
						target = iterator.next();
						break;
					}
				}
				
				if (target != null)
					location = target.getLocation();
			}
		} catch (Exception e) {
			
		}
		
		if (setBlock(event.getPlayer(), event.getPlayer().getItemInHand(), location)) {
			event.setCancelled(true);
		}
	}
	
	public boolean setBlock(Player player, ItemStack itemInHand, Location location) {

		if (MyGames.getMetadataManager().getMode(player) != MetadataManager.SETUP)
			return false;
		
		if (MyGames.getMapConfigManager().isConfigMenuItem(itemInHand)) {
			InventoryMenu.openMenu(player, MyGames.getMapConfigManager().getConfigMenu());
			return true;
		}

		try {
			String locationKey = MyGames.getMapConfigManager().getKeyFromTool(itemInHand);
			
			if (locationKey == null || location == null)
				return false;
			
			MyGames.getMapConfigManager().setLocation(player.getWorld().getName(), locationKey,
					location);
			
			player.sendMessage("Attached location to " + locationKey);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
