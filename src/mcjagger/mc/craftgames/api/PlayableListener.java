package mcjagger.mc.craftgames.api;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;

public class PlayableListener extends RegisteredListener {

	private final Playable playable;
	
	public PlayableListener(Playable playable, Listener listener, EventExecutor executor, EventPriority priority, Plugin plugin,
			boolean ignoreCancelled) {
		super(listener, executor, priority, plugin, ignoreCancelled);
		this.playable = playable;
	}

	@Override
	public void callEvent(Event event) throws EventException {
		
		if (PlayerEvent.class.isInstance(event)) {
			PlayerEvent pEvent = (PlayerEvent) event;
			Player player = pEvent.getPlayer();
			
			if (!playable.hasPlayer(player.getUniqueId()))
				return;
		}
		
		if (EntityEvent.class.isInstance(event)) {
			EntityEvent pEvent = (EntityEvent) event;
			Entity entity = pEvent.getEntity();
			
			if (!Player.class.isInstance(entity)) {
				super.callEvent(event);
				return;
			}
			
			
			Player player = (Player) entity;
			if (!playable.hasPlayer(player.getUniqueId()))
				return;
		}
		
	}

}
