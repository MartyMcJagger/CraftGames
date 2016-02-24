package mcjagger.mc.craftgames.listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import mcjagger.mc.mygames.MetadataManager;
import mcjagger.mc.mygames.MyGames;
import mcjagger.mc.mygames.game.Game;

public class LobbyListener implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		MyGames.toLobby(event.getPlayer());
		
		if (!event.getPlayer().hasPlayedBefore())
			Bukkit.broadcastMessage(ChatColor.YELLOW + "Watch out! New challenger: " + event.getPlayer().getDisplayName());
		
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
	public void onMessage(AsyncPlayerChatEvent event) {
		if (event.getPlayer() == null)
			return;
		
		String gameName = MyGames.getLobbyManager().getCurrentGame(event.getPlayer());
		
		if (gameName == null)
			return;
		
		Game game = MyGames.getLobbyManager().getGame(gameName);
		
		if (game == null || !game.isRunning())
			return;
		
		List<Player> players = new ArrayList<Player>();
		
		for (UUID uuid : game.getPlayers()) {
			players.add(Bukkit.getPlayer(uuid));
		}
		
		event.getRecipients().retainAll(players);
		event.getRecipients().addAll(event.getPlayer().getWorld().getPlayers());
	}

	/*@EventHandler
	public void onInvOpen(InventoryOpenEvent event) {
		Player player = Bukkit.getPlayer(event.getPlayer().getUniqueId());
		if (MyGames.getArcade().getMetadataManager().getMode(player) == MetadataManager.LOBBY) {

			event.getPlayer().closeInventory();
			event.setCancelled(true);
			if (player != null)
				sendErrorMessage(player);
		}
	}*/

	@EventHandler
	public void onInvClick(InventoryClickEvent event) {
		Player player = Bukkit.getPlayer(event.getWhoClicked().getUniqueId());
		if (MyGames.getArcade().getMetadataManager().getMode(player) == MetadataManager.LOBBY) {

			event.getWhoClicked().closeInventory();
			event.setCancelled(true);
			//if (player != null)
			//	sendErrorMessage(player);
		}
	}//*/

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
			MyGames.toLobby(event.getPlayer(), false);
		}
	}

	@EventHandler
	public final void onPlayerDeath(PlayerDeathEvent event) {
		if (MyGames.getArcade().getMetadataManager().getMode(event.getEntity()) == MetadataManager.LOBBY) {
			
			event.getDrops().clear();
			event.setDroppedExp(0);
			event.setKeepLevel(true);
			event.setDeathMessage(null);
			
			event.getEntity().setHealth(20d);
	
			event.setKeepInventory(true);
			event.getEntity().getInventory().clear();
			
			MyGames.toLobby(event.getEntity());
		
		}
	}
	
	public void sendErrorMessage(Player player) {
		player.sendMessage(MyGames.getChatManager().actionNotAllowed());
	}
	
	
}
