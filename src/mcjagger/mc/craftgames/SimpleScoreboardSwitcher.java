package mcjagger.mc.craftgames;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import mcjagger.mc.mygames.MyGames;
import mcjagger.mc.mygames.ScoreboardProvider;
import mcjagger.mc.mygames.ScoreboardSwitcher;

public class SimpleScoreboardSwitcher extends ScoreboardSwitcher {
	
	private ScoreboardProvider defaultProvider = null;
	private HashMap<UUID, ScoreboardProvider> providers = new HashMap<UUID, ScoreboardProvider>();
	
	public void enable() {
		Bukkit.getScheduler().runTaskTimer(MyGames.getArcade(), switcher, 20 * 5, 20 * 5);
	}
	
	public Runnable switcher = new Runnable(){
		@Override
		public void run() 
		{
			for (UUID uuid : providers.keySet()) {
				
				Player player = Bukkit.getPlayer(uuid);
				
				if (player == null)  {
					providers.remove(uuid);
					continue;
				}
				
				ScoreboardProvider provider = providers.get(uuid);
				
				if (provider == null || !provider.hasScoreboard())
					provider = defaultProvider;
				
				Scoreboard sb = provider.nextScoreboard();
				player.setScoreboard(sb);
				
				MyGames.debug("New Scoreboard?");
				/*
				Objective obj = sb.getObjective(DisplaySlot.SIDEBAR);
				Objective old = getObjective(player);
				
				old.setDisplayName(obj.getDisplayName());
				
				for (String entry : old.getScoreboard().getEntries())
					old.*/
			}
		}
	};
	
	@Override
	public void setProvider(UUID uuid, ScoreboardProvider sp) {
		Player player = Bukkit.getPlayer(uuid);
		player.setScoreboard(sp.nextScoreboard());
		providers.put(uuid, sp);
	}
	
	@Override
	public void setDefaultProvider(ScoreboardProvider sp) {
		defaultProvider = sp;
	}
	
	@Override
	public void useDefaultProvider(UUID uuid) {
		providers.remove(uuid);
	}
	
	private static Objective getObjective(Player player) {
		Scoreboard sb = player.getScoreboard();
		if (sb == null) {
			sb = Bukkit.getScoreboardManager().getNewScoreboard();
			player.setScoreboard(sb);
		}
		
		Objective obj = sb.getObjective(DisplaySlot.SIDEBAR);
		if (obj == null) {
			obj = sb.registerNewObjective("Sidebar", "dummy");
			obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		}
		
		return obj;
	}//*/
	
}
