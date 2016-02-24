package mcjagger.mc.craftgames;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import mcjagger.mc.mygames.MyGames;
import mcjagger.mc.mygames.ScoreboardProvider;
import mcjagger.mc.mygames.game.Game;

public class GameStatusScoreboard implements ScoreboardProvider, Runnable {

	private final Game game;
	private Scoreboard scoreboard;
	
	private Objective pcount;
	private Objective cdown;

	private static final int COUNTDOWN_START = 1200;
	private static final int PERIOD = 100;
	private BukkitTask countdown = null;
	private int ticked = -1;
	
	public GameStatusScoreboard(Game game) {
		this.game = game;
		scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

		pcount = scoreboard.registerNewObjective(game.getName(), "dummy");
		cdown = scoreboard.registerNewObjective("Countdown:", "dummy");
	}
	
	@Override
	public String getName() {
		return "gamestatus." + game.getName();
	}

	@Override
	public Scoreboard nextScoreboard() {
		if (game.isRunning()) {
			cancelCountdown();
			return game.nextScoreboard();
		}
		
		if (countdown == null) {
			pcount.getScore("Current").setScore(game.playerCount());
			pcount.getScore("Min Players").setScore(game.minPlayers);
			pcount.setDisplaySlot(DisplaySlot.SIDEBAR);
		} else {
			updateCountdown();
		}
		
		return scoreboard;
	}
	
	public void startCountdown() {
		if (countdown == null) {
			ticked = COUNTDOWN_START;
			countdown = Bukkit.getScheduler().runTaskTimer(MyGames.getArcade(), this, PERIOD, PERIOD);
		}
	}
	
	public void cancelCountdown() {
		if (countdown != null)
			countdown.cancel();
		countdown = null;
		ticked = -1;
	}
	
	private void updateCountdown() {
		cdown.getScore("Starting in").setScore(ticked/20);
		cdown.setDisplaySlot(DisplaySlot.SIDEBAR);
	}
	/*
	public void updateCount() {
		if (game.playerCount() < game.minPlayers) {
			Objective obj = scoreboard.registerNewObjective(ChatColor.GRAY + "Players Needed", "dummy");
			obj.getScore(ChatColor.GREEN + "Current").setScore(game.playerCount());
			obj.getScore(ChatColor.RED + "Minimum").setScore(game.minPlayers);
		} else {
			
		}
	}*/

	@Override
	public boolean hasScoreboard() {
		if (game.isRunning())
			return game.hasScoreboard();
		
		return true;
	}

	@Override
	public void run() {
		ticked -= PERIOD;
		
		if (ticked <= 0)
			MyGames.getLobbyManager().startGame(game.getName());
		else
			updateCountdown();
	}

}
