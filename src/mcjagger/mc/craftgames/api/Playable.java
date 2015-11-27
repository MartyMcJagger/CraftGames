package mcjagger.mc.craftgames.api;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public abstract class Playable {
	
	protected Set<UUID> players = new HashSet<UUID>();
	protected Set<Module> modules = new HashSet<Module>();

	public abstract String getName();
	
	public boolean addModule(Module module) {
		return modules.add(module);
	}
	
	public boolean removeModule(Module module) {
		return modules.remove(module);
	}
	
	public abstract boolean canAddPlayer(UUID uuid);
	public abstract void addedPlayer(UUID uuid);
	public abstract void removedPlayer(UUID uuid);
	
	public Set<UUID> getPlayers() {
		return new HashSet<UUID>(players);
	}
	
	public int playerCount() {
		return players.size();
	}
	
	/**
	 * 
	 * @param uuid Player to check for
	 * @return true if the player set contains this player
	 */
	public boolean hasPlayer(UUID uuid) {
		return players.contains(uuid);
	}
	
	/**
	 * 
	 * @param uuid Player to add
	 * @return true if the player set changed as a result of this call
	 */
	public boolean addPlayer(UUID uuid) {
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
	public boolean removePlayer(UUID uuid) {
		if (players.remove(uuid)) {
			removedPlayer(uuid);
			return true;
		}
		
		return false;
	}

}
