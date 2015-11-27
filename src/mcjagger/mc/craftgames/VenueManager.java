package mcjagger.mc.craftgames;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;

import mcjagger.mc.craftgames.api.Venue;

public class VenueManager implements Listener {
	
	private final Set<Venue> venues = new HashSet<Venue>();
	
	private final Plugin plugin;
	
	public VenueManager(Plugin plugin) {
		this.plugin = plugin;

		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	public void addVenue(Venue venue) {
		if (venues.add(venue))
			Bukkit.getPluginManager().registerEvents(venue, plugin);
	}
	
	public void removeVenue(Venue venue) {
		if (venues.remove(venue))
			HandlerList.unregisterAll(venue);
	}
	
	public Venue getVenueAt(Location l) {
		for (Venue v : venues) {
			
			if (!v.isEnabled())
				continue;
			
			double distSquared = v.getLocation().distanceSquared(l);
			
			if (Math.pow(v.activationProximity(), 2) >= distSquared) {
				return v;
			}
		}
		return null;
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Venue f = getVenueAt(event.getFrom());
		Venue t = getVenueAt(event.getTo());
		
		UUID uuid = event.getPlayer().getUniqueId();
		
		if (f != null && f != t) {
			f.removePlayer(uuid);
		}
		if (t != null) {
			t.addPlayer(uuid);
		}
	}

}
