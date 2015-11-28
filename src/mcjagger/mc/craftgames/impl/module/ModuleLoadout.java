package mcjagger.mc.craftgames.impl.module;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import mcjagger.mc.craftgames.api.Module;
import mcjagger.mc.craftgames.api.Playable;

public abstract class ModuleLoadout extends Module {
	
	Map<UUID, ItemStack[]> itemContents;
	Map<UUID, ItemStack[]> armorContents;
	
	public ModuleLoadout(Playable playable) {
		super(playable);
		
		itemContents = new HashMap<UUID, ItemStack[]>();
		armorContents = new HashMap<UUID, ItemStack[]>();
	}
	
	public abstract void setupPlayer(PlayerInventory pi);
	
	@Override
	public void addedPlayer(Player player) {
		UUID uuid = player.getUniqueId();

		PlayerInventory pi = player.getInventory();

		armorContents.put(uuid, pi.getArmorContents());
		itemContents.put(uuid, pi.getContents());
		
		setupPlayer(player.getInventory());
	}
	
	@Override
	public void removedPlayer(Player player) {
		UUID uuid = player.getUniqueId();
		
		PlayerInventory pi = player.getInventory();
		
		if (armorContents.containsKey(uuid))
			pi.setArmorContents(armorContents.get(uuid));
		if (itemContents.containsKey(uuid))
			pi.setContents(itemContents.get(uuid));
	}
	
	@Override
	public void started() {
		for (UUID uuid : playable.getPlayers()) {
			Player player = Bukkit.getPlayer(uuid);
			if (player != null)
				addedPlayer(player);
		}
	}
	
	@Override
	public void stopped() {
		for (UUID uuid : playable.getPlayers()) {
			Player player = Bukkit.getPlayer(uuid);
			if (player != null)
				removedPlayer(player);
		}
	}
	
}
