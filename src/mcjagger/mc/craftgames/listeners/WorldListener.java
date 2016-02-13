package mcjagger.mc.craftgames.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.world.WorldEvent;

import mcjagger.mc.mygames.MyGames;
import mcjagger.mc.mygames.world.MapConfigManager;

public class WorldListener implements Listener {

	@EventHandler
	public void onWeatherChange(WeatherChangeEvent event) {
		if (event.toWeatherState())
			event.setCancelled(true);
		event.getWorld().setStorm(false);
		event.getWorld().setThundering(false);
	}

	@EventHandler
	public void onWorldEvent(WorldEvent event) {
		event.getWorld().setTime(1200);
	}

	@EventHandler
	public void onEntitySpawn(CreatureSpawnEvent event) {
		if (MapCopyManager.event.getSpawnReason() == SpawnReason.NATURAL
				|| event.getSpawnReason() == SpawnReason.CHUNK_GEN)
			event.setCancelled(true);
	}

}
