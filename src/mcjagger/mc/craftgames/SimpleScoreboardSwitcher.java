package mcjagger.mc.craftgames;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import mcjagger.mc.mygames.MyGames;
import mcjagger.mc.mygames.ScoreboardProvider;
import mcjagger.mc.mygames.ScoreboardSwitcher;
import org.bukkit.ChatColor;

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
			Scoreboard defaultsb = defaultProvider.nextScoreboard();
			
			Map<String, Scoreboard> sbmap = new HashMap<String, Scoreboard>();
			
			for (UUID uuid : providers.keySet()) {
				
				Player player = Bukkit.getPlayer(uuid);
				
				if (player == null)  {
					continue;
				}
				
				ScoreboardProvider provider = providers.get(uuid);
				Scoreboard sb;
				
				if (provider == null || !provider.hasScoreboard()) {
					sb = defaultsb;
				} else {
					if (sbmap.containsKey(provider.getName())) {
						sb = sbmap.get(provider.getName());
					} else {
						sb = provider.nextScoreboard();
						sbmap.put(provider.getName(), sb);
					}
				}
				
				player.setScoreboard(sb);
				
				MyGames.debug("New Scoreboard?");
				/*
				Objective obj = sb.getObjective(DisplaySlot.SIDEBAR);
				Objective old = getObjective(player);
				
				old.setDisplayName(obj.getDisplayName());
				
				for (String entry : old.getScoreboard().getEntries())
					old.*/
			}

			
			for (Player player : Bukkit.getOnlinePlayers()) {
				if (providers.containsKey(player.getUniqueId()))
					continue;
				
				player.setScoreboard(defaultsb);
			}
		}
	};
	
	@Override
	public void setProvider(UUID uuid, ScoreboardProvider sp) {
		Player player = Bukkit.getPlayer(uuid);
		if (sp != null)
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
			obj = sb.registerNewObjective(ChatColor.GOLD + "EG+", "dummy");
			obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		}
		
		return obj;
	}//*/
	
}
