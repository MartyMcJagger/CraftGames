package mcjagger.mc.craftgames.api;

import java.util.UUID;

public abstract class Game extends Playable {
	
	public int maxPlayers;
	public int minPlayers;
	
	@Override
	public boolean canAddPlayer(UUID uuid) {
		return playerCount() < maxPlayers;
	}
	
}
