package mcjagger.mc.craftgames.api;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.event.Listener;

public abstract class Playable implements Listener {
	
	public abstract String	getName();
	public abstract boolean	canAddPlayer(UUID uuid);
	public abstract void 	addedPlayer(UUID uuid);
	public abstract void 	removedPlayer(UUID uuid);
	
	
	protected Set<UUID> players = new HashSet<UUID>();
	protected Set<Module> modules = new HashSet<Module>();
	
	
	public Playable() {
		
	}
	

	public Set<Module> getModules() {
		return new HashSet<Module>(modules);
	}
	public final boolean addModule(Module module) {
		return modules.add(module);
	}
	public final boolean removeModule(Module module) {
		if (module.removable)
			return modules.remove(module);
		
		return false;
	}
	
	public final Set<UUID> getPlayers() {
		return new HashSet<UUID>(players);
	}
	public final int playerCount() {
		return players.size();
	}
	
	/**
	 * 
	 * @param uuid Player to check for
	 * @return true if the player set contains this player
	 */
	public final boolean hasPlayer(UUID uuid) {
		return players.contains(uuid);
	}
	
	/**
	 * 
	 * @param uuid Player to add
	 * @return true if the player set changed as a result of this call
	 */
	public final boolean addPlayer(UUID uuid) {
		if (canAddPlayer(uuid) && players.add(uuid)) {
			addedPlayer(uuid);
			return true;
		}
		
		return false;
	}
	
	/**
	 * 
	 * @param uuid Player to remove
	 * @return true if the player set changed as a result of this call
	 */
	public final boolean removePlayer(UUID uuid) {
		if (players.remove(uuid)) {
			removedPlayer(uuid);
			return true;
		}
		
		return false;
	}

}
