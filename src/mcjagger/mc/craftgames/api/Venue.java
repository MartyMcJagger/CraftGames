package mcjagger.mc.craftgames.api;

import org.bukkit.Location;
import org.bukkit.event.Listener;

import mcjagger.mc.craftgames.VenueManager;

public abstract class Venue extends Playable implements Listener {
	
	protected final VenueManager vm;
	
	public Venue(VenueManager vm) {
		this.vm = vm;
		
		vm.addVenue(this);
	}
	
	public abstract boolean isEnabled();
	
	public abstract double activationProximity(); 
	public abstract Location getLocation();
	
}
