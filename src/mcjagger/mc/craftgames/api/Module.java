package mcjagger.mc.craftgames.api;

import org.bukkit.entity.Player;

public abstract class Module {
	
	protected final Playable playable;
	
	public Module(Playable playable) {
		this.playable = playable;
	}

	/**
	 * Prepare for start. This occurs before any players are in the Playable.
	 */
	public abstract void load();
	
	/**
	 * Invoked when Playable starts
	 */
	public abstract void start();
	/**
	 * Invoked when Playable ends
	 */
	public abstract void stop();
	
	public abstract void addedPlayer(Player player);
	public abstract void removedPlayer(Player player);
}
