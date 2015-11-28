package mcjagger.mc.craftgames.impl.game;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.material.Wool;

import mcjagger.mc.craftgames.Utils;
import mcjagger.mc.craftgames.api.Game;
import mcjagger.mc.craftgames.api.Module;

public class GameTag extends Game {

	private UUID it;

	private Module moduleTag = new Module(this, false){
		@Override
		public void playerDamaged(EntityDamageEvent event) {
			if (event.getCause() == DamageCause.VOID)
				setIt(event.getEntity().getUniqueId());
		}
		@Override
		public void playerDamagedPlayer(EntityDamageByEntityEvent event) {
			if (event.getDamager().getUniqueId().equals(it))
				setIt(event.getEntity().getUniqueId());
		}
	};
	
	
	public GameTag() {
		addModule(moduleTag);
	}
	
	
	@Override
	public String getName() {
		return "Tag";
	}
	@Override
	public void addedPlayer(UUID uuid) {
		preparePlayer(Bukkit.getPlayer(uuid));//setIt(uuid);
	}
	@Override
	public void removedPlayer(UUID uuid) {
		if (uuid.equals(it)) {
			setIt(Utils.getRandomItem(getPlayers()));
		}
	}

	
	public void preparePlayer(Player player) {
		player.getInventory().clear();

		Wool w = new Wool();
		w.setColor(DyeColor.BLUE);
		
		player.getInventory().setHelmet(w.toItemStack());
	}
	public void prepareIt(Player player) {
		player.getInventory().clear();

		Wool w = new Wool();
		w.setColor(DyeColor.RED);

		player.getInventory().setHelmet(w.toItemStack());
	}	
	public void setIt(UUID newIt) {
		if (it != null) {
			Player player = Bukkit.getPlayer(it);
			if (player != null) {
				preparePlayer(player);
			}
		}
		
		it = newIt;
		
		Player player = Bukkit.getPlayer(it);
		prepareIt(player);
		
		for (UUID uuid : getPlayers()) {
			Player p = Bukkit.getPlayer(uuid);
			p.sendMessage((uuid == it)?
					(ChatColor.RED + "YOU" + ChatColor.DARK_AQUA + " are now it!" ):
					(ChatColor.RED + player.getName() + ChatColor.DARK_AQUA + " is now it!"));
		}
	}
}
