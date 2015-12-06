package mcjagger.mc.craftgames;

import java.util.Set;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import mcjagger.mc.craftgames.world.SimpleMapConfigManager;
import mcjagger.mc.craftgames.world.SimpleMapManager;
import mcjagger.mc.mygames.Arcade;
import mcjagger.mc.mygames.ConfigManager;
import mcjagger.mc.mygames.Game;
import mcjagger.mc.mygames.LobbyManager;
import mcjagger.mc.mygames.MetadataManager;
import mcjagger.mc.mygames.chat.ChatManager;
import mcjagger.mc.mygames.world.MapConfigManager;
import mcjagger.mc.mygames.world.MapManager;

public class PluginArcade extends Arcade {
	
	private ChatManager chatManager;
	private ConfigManager configManager;
	private LobbyManager lobbyManager;
	private MetadataManager metadataManager;
	private MapManager worldManager;
	private MapConfigManager worldConfigManager;
	
	
	
	@Override
	public void onEnable() {
		super.onEnable();
		
		chatManager = new SimpleChatManager();
		configManager = new SimpleConfigManager();
		lobbyManager = new SimpleLobbyManager();
		metadataManager = new SimpleMetadataManager();
		worldManager = new SimpleMapManager();
		worldConfigManager = new SimpleMapConfigManager();
	}
	
	
	
	
	
	/*
	 * (non-Javadoc)
	 * @see mcjagger.mc.mygames.Arcade#getChatManager()
	 */
	@Override
	public ChatManager getChatManager() {
		return chatManager;
	}
	
	/*
	 * (non-Javadoc)
	 * @see mcjagger.mc.mygames.Arcade#getConfigManager()
	 */
	@Override
	public ConfigManager getConfigManager() {
		return configManager;
	}
	
	/*
	 * (non-Javadoc)
	 * @see mcjagger.mc.mygames.Arcade#getLobbyManager()
	 */
	@Override
	public LobbyManager	getLobbyManager() {
		return lobbyManager;
	}
	
	/*
	 * (non-Javadoc)
	 * @see mcjagger.mc.mygames.Arcade#getMetadataManager()
	 */
	@Override
	public MetadataManager getMetadataManager() {
		return metadataManager;
	}
	
	/*
	 * (non-Javadoc)
	 * @see mcjagger.mc.mygames.Arcade#getWorldManager()
	 */
	@Override
	public MapManager getWorldManager() {
		return worldManager;
	}
	
	/*
	 * (non-Javadoc)
	 * @see mcjagger.mc.mygames.Arcade#getWorldConfigManager()
	 */
	@Override
	public MapConfigManager getWorldConfigManager() {
		return worldConfigManager;
	}
	
	
	
	/*
	 * 
	 */
	@Override
	public Location getSpawnLocation() {
		return getConfigManager().getSpawnLocation();
	}
	
	
	
	// GAME LOADING / ENABLING
	
	/*
	 * (non-Javadoc)
	 * @see mcjagger.mc.mygames.Arcade#load(mcjagger.mc.mygames.Game)
	 */
	@Override
	public boolean load(Game game) {
		return true; // TODO: Separate loading and enabling? Or deprecate loading?
	}
	
	/*
	 * (non-Javadoc)
	 * @see mcjagger.mc.mygames.Arcade#isLoaded(mcjagger.mc.mygames.Game)
	 */
	@Override
	public boolean isLoaded(Game game) {
		return true; // TODO: Separate loading and enabling? Or deprecate loading?
	}
	
	/*
	 * (non-Javadoc)
	 * @see mcjagger.mc.mygames.Arcade#enable(mcjagger.mc.mygames.Game)
	 */
	@Override
	public boolean enable(Game game) {
		return getLobbyManager().addGame(game);
	}
	
	/*
	 * (non-Javadoc)
	 * @see mcjagger.mc.mygames.Arcade#enable(mcjagger.mc.mygames.Game)
	 */
	@Override
	public boolean disable(Game game) {
		return getLobbyManager().removeGame(game);
	}
	
	/*
	 * (non-Javadoc)
	 * @see mcjagger.mc.mygames.Arcade#isLoaded(mcjagger.mc.mygames.Game)
	 */
	@Override
	public boolean isEnabled(Game game) {
		return getLobbyManager().hasGame(game.getName());
	}
	
	/*
	 * (non-Javadoc)
	 * @see mcjagger.mc.mygames.Arcade#getGame(java.lang.String)
	 */
	@Override
	public Game getGame(String gameName) {
		return getLobbyManager().getGame(gameName);
	}
	
	/*
	 * (non-Javadoc)
	 * @see mcjagger.mc.mygames.Arcade#getGames()
	 */
	@Override
	public Set<String> getGames() {
		return getLobbyManager().getGames();
	}
	
	
	
	// PLAYER HANDLING
	
	/*
	 * (non-Javadoc)
	 * @see mcjagger.mc.mygames.Arcade#joinGame(org.bukkit.entity.Player, mcjagger.mc.mygames.Game)
	 */
	@Override
	public boolean joinGame(Player player, Game game) {
		return getLobbyManager().addPlayer(player, game.getName());
	}
	
	/*
	 * (non-Javadoc)
	 * @see mcjagger.mc.mygames.Arcade#getCurrentGame(org.bukkit.entity.Player)
	 */
	@Override
	public Game getCurrentGame(Player player) {
		return getGame(getLobbyManager().getCurrentGame(player));
	}
	
	/*
	 * (non-Javadoc)
	 * @see mcjagger.mc.mygames.Arcade#toLobby(org.bukkit.entity.Player)
	 */
	@Override
	public boolean toLobby(Player player) {
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see mcjagger.mc.mygames.Arcade#toSetup(org.bukkit.entity.Player)
	 */
	@Override
	public boolean toSetup(Player player) {
		return true;
	}
}
