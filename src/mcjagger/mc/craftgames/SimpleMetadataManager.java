package mcjagger.mc.craftgames;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import mcjagger.mc.mygames.MetadataManager;
import mcjagger.mc.mygames.MyGames;

public class SimpleMetadataManager extends MetadataManager {
	
	/*
	 * (non-Javadoc)
	 * @see mcjagger.mc.mygames.MetadataManager#setInGame(org.bukkit.entity.Player, java.lang.String)
	 */
	@Override
	public boolean setInGame(Player player, String gameName) {
		if (inOtherGame(player))
			return false;

		player.setMetadata(MODE, new FixedMetadataValue(MyGames.getArcade(), INGAME));
		player.setMetadata(GAME, new FixedMetadataValue(MyGames.getArcade(), gameName));
		
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see mcjagger.mc.mygames.MetadataManager#setInSetup(org.bukkit.entity.Player)
	 */
	@Override
	public boolean setInSetup(Player player) {
		if (inOtherGame(player))
			return false;

		player.setMetadata(MODE, new FixedMetadataValue(MyGames.getArcade(), SETUP));
		player.setMetadata(GAME, new FixedMetadataValue(MyGames.getArcade(), null));
		
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see mcjagger.mc.mygames.MetadataManager#setInLobby(org.bukkit.entity.Player)
	 */
	@Override
	public boolean setInLobby(Player player) {
		if (inOtherGame(player))
			return false;

		player.setMetadata(MODE, new FixedMetadataValue(MyGames.getArcade(), LOBBY));
		player.removeMetadata(GAME, MyGames.getArcade());
		player.setMetadata(GAME, new FixedMetadataValue(MyGames.getArcade(), null));
		
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see mcjagger.mc.mygames.MetadataManager#setInOther(org.bukkit.entity.Player)
	 */
	@Override
	public void setInOther(Player player) {
		int mode = getMode(player);
		if (mode == OTHER_GAME)
			return;
		
		player.setMetadata(MODE, new FixedMetadataValue(MyGames.getArcade(), OTHER_GAME));
	}
	
	/*
	 * (non-Javadoc)
	 * @see mcjagger.mc.mygames.MetadataManager#getMode(org.bukkit.entity.Player)
	 */
	@Override
	public int getMode(Player player) {
		try {
			if (inOtherGame(player))
				return OTHER_GAME;
			
			int mode = player.getMetadata(MODE).get(0).asInt();
			return mode;
		} catch (Exception e) {
			return LOBBY;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see mcjagger.mc.mygames.MetadataManager#setMode(org.bukkit.entity.Player, int)
	 */
	@Override
	public void setMode(Player player, int mode) {
		
		switch (mode) {
		case OTHER_GAME:	setInOther(player); break;
		case SETUP:     	setInSetup(player); break;
		case LOBBY:     	setInLobby(player); break;
		}
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see mcjagger.mc.mygames.MetadataManager#getGame(org.bukkit.entity.Player)
	 */
	@Override
	public String getGame(Player player) {
		List<MetadataValue> meta = player.getMetadata(GAME);
		if (meta == null || meta.size() < 1)
			return MyGames.getArcade().getLobbyManager().getCurrentGame(player.getUniqueId());
		
		return player.getMetadata(GAME).get(0).asString();
	}
	
	/*
	 * (non-Javadoc)
	 * @see mcjagger.mc.mygames.MetadataManager#inOtherGame(org.bukkit.entity.Player)
	 */
	public boolean inOtherGame(Player player) {
		try {
			String game = getGame(player);
			if ((!game.trim().equalsIgnoreCase("")) && MyGames.getArcade().getGame(game) == null) {
				setInOther(player);
				return true;
			}
		} catch (Exception e) {
			//Not worried. Will catch if game == null.
		}
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * @see mcjagger.mc.mygames.MetadataManager#remove(org.bukkit.entity.Player)
	 */
	public void remove(Player player) {
		if (!inOtherGame(player)) {
			player.setMetadata(GAME, null);
		}
		player.setMetadata(MODE, null);
	}
}
