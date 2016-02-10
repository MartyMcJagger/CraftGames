package mcjagger.mc.craftgames;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Scoreboard;

import mcjagger.mc.craftgames.listeners.LobbyListener;
import mcjagger.mc.craftgames.listeners.WorldConfigListener;
import mcjagger.mc.craftgames.world.SimpleMapConfigManager;
import mcjagger.mc.craftgames.world.SimpleMapManager;
import mcjagger.mc.mygames.Arcade;
import mcjagger.mc.mygames.ConfigManager;
import mcjagger.mc.mygames.LobbyManager;
import mcjagger.mc.mygames.MetadataManager;
import mcjagger.mc.mygames.MyGames;
import mcjagger.mc.mygames.ScoreboardSwitcher;
import mcjagger.mc.mygames.chat.ChatManager;
import mcjagger.mc.mygames.game.Game;
import mcjagger.mc.mygames.world.MapConfigManager;
import mcjagger.mc.mygames.world.MapManager;

public class PluginArcade extends Arcade {
	
	private ChatManager chatManager;
	private ConfigManager configManager;
	private LobbyManager lobbyManager;
	private MetadataManager metadataManager;
	private MapManager worldManager;
	private MapConfigManager worldConfigManager;
	private SimpleScoreboardSwitcher scoreboardSwitcher;
	
	private Scoreboard defaultScoreboard;
	
	@Override
	public void onLoad() {
		super.onLoad();

		chatManager = new SimpleChatManager();
		configManager = new SimpleConfigManager();
		lobbyManager = new SimpleLobbyManager();
		metadataManager = new SimpleMetadataManager();
		worldManager = new SimpleMapManager();
		worldConfigManager = new SimpleMapConfigManager();
		scoreboardSwitcher = new SimpleScoreboardSwitcher();
	}
	
	@Override
	public void onEnable() {
		super.onEnable();

		LobbyListener ll = new LobbyListener();
		Bukkit.getPluginManager().registerEvents(ll, MyGames.getArcade());
		WorldConfigListener wcl = new WorldConfigListener();
		Bukkit.getPluginManager().registerEvents(wcl, MyGames.getArcade());
		
		((SimpleScoreboardSwitcher)getScoreboardSwitcher()).enable();
		
		defaultScoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
	}
	
	@Override()
	public void onDisable() {
		super.onDisable();
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
	 * @see mcjagger.mc.mygames.Arcade#getMapManager()
	 */
	@Override
	public MapManager getMapManager() {
		return worldManager;
	}
	
	/*
	 * (non-Javadoc)
	 * @see mcjagger.mc.mygames.Arcade#getMapConfigManager()
	 */
	@Override
	public MapConfigManager getMapConfigManager() {
		return worldConfigManager;
	}
	
	/*
	 * (non-Javadoc)
	 * @see mcjagger.mc.mygames.Arcade#getScoreboardSwitcher()
	 */
	@Override
	public ScoreboardSwitcher getScoreboardSwitcher() {
		return scoreboardSwitcher;
	}
	
	/*
	 * (non-Javadoc)
	 * @see mcjagger.mc.mygames.Arcade#getSpawnLocation()
	 */
	@Override
	public Location getSpawnLocation() {
		return getConfigManager().getSpawnLocation();
	}
	
	/*
	 * (non-Javadoc)
	 * @see mcjagger.mc.mygames.Arcade#getDefaultScoreboard()
	 */
	@Override
	public Scoreboard getDefaultScoreboard() {
		return defaultScoreboard;
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
		player.teleport(MyGames.getSpawnLocation(), TeleportCause.PLUGIN);

		player.setScoreboard(MyGames.getArcade().getDefaultScoreboard());
		MyGames.getArcade().getScoreboardSwitcher().useDefaultProvider(player.getUniqueId());
		MyGames.getMetadataManager().setInLobby(player);
		
		player.getInventory().clear();
		player.getInventory().setArmorContents(new ItemStack[4]);
		player.setGameMode(GameMode.ADVENTURE);
		player.setAllowFlight(false);
		player.setFireTicks(0);
		player.setFlying(false);
		player.setCanPickupItems(false);
		player.setExhaustion(0);
		player.setFoodLevel(20);
		player.setHealth(20);
		player.setHealthScaled(false);
		player.setMaxHealth(20);
		player.setSaturation(20);
		
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see mcjagger.mc.mygames.Arcade#toSetup(org.bukkit.entity.Player)
	 */
	@Override
	public boolean toSetup(Player player) {
		player.getInventory().addItem(MyGames.getArcade().getMapConfigManager().getConfigMenuItem());
		
		player.setGameMode(GameMode.CREATIVE);
		player.setAllowFlight(true);
		
		return true;
	}

}
