package mcjagger.mc.craftgames;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import mcjagger.mc.mygames.LobbyManager;
import mcjagger.mc.mygames.MyGames;
import mcjagger.mc.mygames.game.Game;

public class SimpleLobbyManager extends LobbyManager implements Listener {
	
	private HashMap<String, Game> games = new HashMap<String, Game>();
	private HashMap<String, String> aliases = new HashMap<String, String>();
	
	public HashMap<UUID, String> lobbys = new HashMap<UUID, String>();
	
	/*
	 * (non-Javadoc)
	 * @see mcjagger.mc.mygames.LobbyManager#getGame(java.lang.String)
	 */
	@Override
	public Game getGame(String game) {
		if (aliases.containsKey(game.toLowerCase()))
			game = aliases.get(game.toLowerCase());
		
		if (games.containsKey(game.toLowerCase()))
			return games.get(game.toLowerCase());
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see mcjagger.mc.mygames.LobbyManager#addGame(mcjagger.mc.mygames.Game)
	 */
	@Override
	public boolean addGame(Game game) {
		
		if (games.containsKey(game.getName().toLowerCase()))
			return false;
		
		games.put(game.getName().toLowerCase(), game);
		
		String[] aliases = game.getAliases();
		if (aliases != null)
			for (String alias : aliases)
				this.aliases.put(alias.toLowerCase(), game.getName().toLowerCase());
		
		if (!this.aliases.containsKey(game.getName())) {
			this.aliases.put(game.getName().toLowerCase(), game.getName().toLowerCase());
			
			if (aliases != null)
				for (String alias : aliases)
					this.aliases.put(alias.toLowerCase(), game.getName().toLowerCase());
		}
		
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see mcjagger.mc.mygames.LobbyManager#removeGame(mcjagger.mc.mygames.Game)
	 */
	@Override
	public boolean removeGame(Game gm) {
		if (games.containsKey(gm.getName().toLowerCase())) {
			games.remove(gm.getName().toLowerCase());
			return true;
		}
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * @see mcjagger.mc.mygames.LobbyManager#hasGame(java.lang.String)
	 */
	@Override
	public boolean hasGame(String game) {
		return (games.containsKey(game.toLowerCase()) || aliases.containsKey(game.toLowerCase()));
	}
	
	/*
	 * (non-Javadoc)
	 * @see mcjagger.mc.mygames.LobbyManager#getGames()
	 */
	@Override
	public Set<String> getGames() {	
		Set<String> names = new HashSet<String>();
		for (String string : games.keySet()) {
			//Bukkit.getLogger().info(string);
			if (string != null && !string.trim().equalsIgnoreCase("")){
				names.add(games.get(string).getName());
			}
		}
		
		return names;
	}
	
	/*
	 * (non-Javadoc)
	 * @see mcjagger.mc.mygames.LobbyManager#getGameNames()
	 */
	@Override
	public Set<String> getGameNames() {
		return new HashSet<String>(games.keySet());
	}
	
	/*
	 * (non-Javadoc)
	 * @see mcjagger.mc.mygames.LobbyManager#restartGame(java.lang.String)
	 */
	@Override
	public boolean restartGame(String gameName) {
		Game game = getGame(gameName);
		if (game == null) {
			return false;
		}
		game.restart();
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see mcjagger.mc.mygames.LobbyManager#startGame(java.lang.String)
	 */
	@Override
	public boolean startGame(String gameName) {
		Game game = getGame(gameName);
		if (game == null) {
			return false;
		}
		return game.start();
	}
	
	/*
	 * (non-Javadoc)
	 * @see mcjagger.mc.mygames.LobbyManager#stopGame(java.lang.String)
	 */
	@Override
	public boolean stopGame(String gameName) {
		Game game = getGame(gameName);
		if (game == null) {
			MyGames.debug("Game \""+gameName+"\" not found!");
			return false;
		}	
		return game.stop();
	}
	
	/*
	 * (non-Javadoc)
	 * @see mcjagger.mc.mygames.LobbyManager#stopAll()
	 */
	@Override
	public void stopAll() {
		for (Game game : games.values()) {
			game.stop();
		}
		games.clear();
	}
	
	
	
	
	
	/*
	 * (non-Javadoc)
	 * @see mcjagger.mc.mygames.LobbyManager#addPlayer(org.bukkit.entity.Player, java.lang.String)
	 */
	@Override
	public boolean addPlayer(Player player, String gameName) {
		Game game = getGame(gameName);
		if (game == null) {
			return false;
		}
		else
			game.addPlayer(player.getUniqueId());

		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see mcjagger.mc.mygames.LobbyManager#removePlayer(org.bukkit.entity.Player, java.lang.String)
	 */
	@Override
	public boolean removePlayer(Player player, String gameName) {
		Game game = getGame(gameName);
		if (game == null) {
			return false;
		}
		else
			game.removePlayer(player.getUniqueId());

		MyGames.toLobby(player);
		
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see mcjagger.mc.mygames.LobbyManager#joinedGame(org.bukkit.entity.Player, mcjagger.mc.mygames.Game)
	 */
	@Override
	public void joinedGame(Player player, Game game) {
		String gameName = lobbys.get(player.getUniqueId());
		if ((gameName != null) && (getGame(gameName) != null)) {
			if (!game.getName().equalsIgnoreCase(gameName))
				getGame(gameName).removePlayer(player.getUniqueId());
		}
		
		lobbys.put(player.getUniqueId(), game.getName());
		
		if (game.isRunning())
			player.sendMessage(MyGames.getChatManager().gameStart(game));
		else
			player.sendMessage(MyGames.getChatManager().joinLobbySuccess(game));
	}
	
	/*
	 * (non-Javadoc)
	 * @see mcjagger.mc.mygames.LobbyManager#leftGame(org.bukkit.entity.Player, mcjagger.mc.mygames.Game)
	 */
	@Override
	public void leftGame(Player player, Game game) {
		if (lobbys.get(player.getUniqueId()).equalsIgnoreCase(game.getName())) {
			lobbys.remove(player.getUniqueId());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see mcjagger.mc.mygames.LobbyManager#getCurrentGame(org.bukkit.entity.Player)
	 */
	@Override
	public String getCurrentGame(Player player) {
		return getCurrentGame(player.getUniqueId());
	}

	/*
	 * (non-Javadoc)
	 * @see mcjagger.mc.mygames.LobbyManager#getCurrentGame(java.util.UUID)
	 */
	@Override
	public String getCurrentGame(UUID uuid) {
		if (!lobbys.containsKey(uuid))
				return null;
		
		return lobbys.get(uuid);
	}

	/*
	 * (non-Javadoc)
	 * @see mcjagger.mc.mygames.LobbyManager#sendPlayers(mcjagger.mc.mygames.Game)
	 */
	@Override
	public void sendPlayers(Game game) {
		for (UUID uuid : game.getPlayers()) {
			Player player = Bukkit.getPlayer(uuid);
			
			if (player == null)
				game.removePlayer(uuid);
			
			InventoryManager.saveInventory(player, game.getName() + "." + player.getUniqueId());
			InventoryManager.savePlayerState(player, game.getName() + "." + player.getUniqueId());
			
			player.setGameMode(GameMode.SURVIVAL);
			player.setAllowFlight(false);
			player.setMaxHealth(20);
			player.setSaturation(20);
			player.setHealth(20);
			player.setFireTicks(0);
			
			player.getInventory().clear();
			
			player.setScoreboard(game.getScoreboard());
			MyGames.getArcade().getScoreboardSwitcher().setProvider(uuid, game);
			
			game.preparePlayer(player);
			
			Location location = game.getSpawnLocation(player);
			
			if (location == null) {
				player.teleport(MyGames.getSpawnLocation(), TeleportCause.PLUGIN);
				player.sendMessage(MyGames.getChatManager().errorOccurred());
				
				continue;
			}
			
			player.teleport(location, TeleportCause.PLUGIN);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see mcjagger.mc.mygames.LobbyManager#retrievePlayers(mcjagger.mc.mygames.Game)
	 */
	@Override
	public void retrievePlayers(Game game) {
		Location spawnLocation = MyGames.getSpawnLocation();
		for (UUID uuid : game.getPlayers()) {
			Player player = Bukkit.getPlayer(uuid);
			
			if (player == null)
			continue;
			
			player.getInventory().clear();
			
			InventoryManager.applyInventory(player, game.getName() + "." + player.getUniqueId());
			InventoryManager.applyPlayerState(player, game.getName() + "." + player.getUniqueId());
			
			player.setScoreboard(MyGames.getArcade().getDefaultScoreboard());
			MyGames.getArcade().getScoreboardSwitcher().useDefaultProvider(uuid);
			
			player.teleport(spawnLocation, TeleportCause.PLUGIN);
		}
	}
}
