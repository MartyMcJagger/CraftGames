package mcjagger.mc.craftgames.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import mcjagger.mc.mygames.MetadataManager;
import mcjagger.mc.mygames.MyGames;

public class LobbyListener implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		MyGames.toLobby(event.getPlayer());
		event.setJoinMessage(MyGames.getChatManager().joinServer(event.getPlayer()));
	}

	@EventHandler
	public void onDrop(PlayerDropItemEvent event) {
		if (MyGames.getArcade().getMetadataManager().getMode(event.getPlayer()) == MetadataManager.LOBBY) {

			event.setCancelled(true);
			sendErrorMessage(event.getPlayer());
		}
	}

	@EventHandler
	public void onInvOpen(InventoryOpenEvent event) {
		Player player = Bukkit.getPlayer(event.getPlayer().getUniqueId());
		if (MyGames.getArcade().getMetadataManager().getMode(player) == MetadataManager.LOBBY) {

		event.getPlayer().closeInventory();
			event.setCancelled(true);
			if (player != null)
				sendErrorMessage(player);
		}
	}

	@EventHandler
	public void onInvClick(InventoryClickEvent event) {
		Player player = Bukkit.getPlayer(event.getWhoClicked().getUniqueId());
		if (MyGames.getArcade().getMetadataManager().getMode(player) == MetadataManager.LOBBY) {

			event.getWhoClicked().closeInventory();
			event.setCancelled(true);
			if (player != null)
				sendErrorMessage(player);
		}
	}

	@EventHandler
	public void onBlockPlaceEvent(BlockPlaceEvent event) {
		if (MyGames.getArcade().getMetadataManager().getMode(event.getPlayer()) == MetadataManager.LOBBY) {

			event.setCancelled(true);
			sendErrorMessage(event.getPlayer());
		}
	}

	/*
	 * @EventHandler public void onBlockDamageEvent(BlockDamageEvent event) { if
	 * (mm.getMode(event.getPlayer()) != mm.SETUP) {
	 * 
	 * event.setCancelled(true); sendErrorMessage(event.getPlayer()); } }
	 */

	@EventHandler
	public void onBlockBreakEvent(BlockBreakEvent event) {
		if (MyGames.getArcade().getMetadataManager().getMode(event.getPlayer()) == MetadataManager.LOBBY) {

			event.setCancelled(true);
			sendErrorMessage(event.getPlayer());
		}
	}
	
	@EventHandler
	public void onRespawnEvent(PlayerRespawnEvent event) {
		if (MyGames.getArcade().getMetadataManager().getMode(event.getPlayer()) == MetadataManager.LOBBY) {
			event.setRespawnLocation(MyGames.getSpawnLocation());
		}
	}

	public void sendErrorMessage(Player player) {
		player.sendMessage(MyGames.getChatManager().actionNotAllowed());
	}
}
