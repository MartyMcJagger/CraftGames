package mcjagger.mc.craftgames.api;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.event.entity.EntityDamageEvent;

public abstract class GameModule extends Module {
	
	public GameModule(Game game) {
		super(game);
	}
	
}
