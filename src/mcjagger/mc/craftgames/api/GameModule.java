package mcjagger.mc.craftgames.api;

import java.util.UUID;

import org.bukkit.Location;

public abstract class GameModule extends Module {
	
	//public final Game game;
	
	public GameModule(Game game) {
		super(game);
	}
	
	public abstract void pointScored(UUID uuid, Location location);
}
