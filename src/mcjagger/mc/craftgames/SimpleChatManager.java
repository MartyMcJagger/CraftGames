package mcjagger.mc.craftgames;

import java.util.Collection;
import java.util.List;
import java.util.StringJoiner;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import mcjagger.mc.mygames.Utils;
import mcjagger.mc.mygames.chat.ChatManager;
import mcjagger.mc.mygames.game.Game;

public class SimpleChatManager implements ChatManager {
	
	@Override
	public String prefix(Game gm) {
		return ChatColor.GREEN + "[" + ChatColor.stripColor(gm.getName()) + "] ";
	}
	
	@Override
	public String prefix() {
		return ChatColor.GREEN + "[MyGames]";
	}
	
	@Override
	public String joinServer(Player player) {
		return ChatColor.YELLOW + player.getDisplayName()
				+ ChatColor.YELLOW + " has entered the arena!";
	}
	
	@Override
	public String leaveServer(Player player) {
		return ChatColor.YELLOW + player.getDisplayName()
				+ ChatColor.YELLOW + " has left the arena.";
	}
	
	@Override
	public String playerDeath(Player player) {
		return ChatColor.YELLOW + player.getDisplayName()
				+ ChatColor.YELLOW + " has perished.";
	}
	
	@Override
	public String playerDeath(Player player, Player killer) {
		return ChatColor.YELLOW + player.getDisplayName()
				+ ChatColor.YELLOW + " has been slaughtered by " + killer.getDisplayName() +".";
	}
	
	@Override
	public String joinLobbySuccess(Game gm) {
		return joinLobbySuccess();
	}
	
	@Override
	public String joinLobbySuccess() {
		return ChatColor.GRAY + "You have joined the wait list. "
				+ "See sidebar for information on when the game will start.";
	}
	
	@Override
	public String leaveLobby(Game gm) {
		return prefix(gm) + leaveLobby();
	}
	
	@Override
	public String leaveLobby() {
		return ChatColor.GRAY + "You have left the wait list.";
	}
	
	@Override
	public String joinLobbyAlreadyJoined(Game gm) {
		return prefix(gm)
				+ ChatColor.RED + joinLobbyAlreadyJoined();
	}
	
	@Override
	public String joinLobbyAlreadyJoined() {
		return ChatColor.RED + "You are already registered for this game!";
	}
	
	@Override
	public String joinLobbyFull(Game gm) {
		return prefix(gm) + joinLobbyFull();
	}
	
	@Override
	public String joinLobbyFull() {
		return ChatColor.RED + "This game is full!";
	}
	
	@Override
	public String joinLobbyIngame(Game gm) {
		return prefix(gm)
				+ joinLobbyIngame();
	}
	
	@Override
	public String joinLobbyIngame() {
		return ChatColor.RED + "You are in another game!";
	}
	
	@Override
	public String joinLobbyInProgress(Game gm) {
		return prefix(gm)
				+ joinLobbyInProgress();
	}

	@Override
	public String joinLobbyInProgress() {
		return ChatColor.RED + "You cannot join this game while it is in session!";
	}
	
	@Override
	public String joinLobbyMaxPlayers(Game gm) {
		return prefix(gm)
				+ joinLobbyMaxPlayers();
	}

	@Override
	public String joinLobbyMaxPlayers() {
		return ChatColor.RED + "This game has already exceeded maximum players!!";
	}
	
	@Override
	public String gameNotFound(String gameName) {
		return prefix()
				+ ChatColor.RED + "Could not find game \"" + ChatColor.stripColor(gameName) + "\". What games are running? /mygames list";
	}
	
	@Override
	public String gameHasNoMaps(Game game) {
		return prefix()
				+ ChatColor.RED + "Could not begin game \"" + game.getName() + "\": No compatible worlds.";
	}
	
	@Override
	public String changeMap(Game gm, String mapName) {
		return prefix()
				+ ChatColor.GRAY + "Map for " + ChatColor.stripColor(gm.getName()) + " is " + mapName + "!";
	}
	
	@Override
	public String gameStart(Game gm) {
		return prefix()
				+ ChatColor.GRAY + ChatColor.stripColor(gm.getName()) + " has begun. Prepare to recieve weapons!";
	}
	
	@Override
	public String gameOver(Game gm, List<Collection<String>> winners) {
		StringJoiner sj = new StringJoiner(" ");
		sj.add(prefix(gm));
		
		if (winners != null) {		
			if (winners.size() >= 1) {
				for (int i = 1; i <= winners.size(); ++i) {
					Collection<String> tier = winners.get(i-1);
					
					String tierMsg = i + "th";
					tierMsg = (i % 10 == 1)?"the victory":tierMsg;
					tierMsg = (i % 10 == 2)?"2nd":tierMsg;
					tierMsg = (i % 10 == 3)?"3nd":tierMsg;
					
					sj.add(Utils.list(tier, ChatColor.GRAY, ChatColor.BLUE).toString());
					sj.add((ChatColor.GRAY + " took " + tierMsg + "!").toString());
					
				}
			} else {
				sj.add(ChatColor.GRAY + "No valid participants found... No Winners!");
			}
		} else {
			sj.add(ChatColor.GRAY + "Ended in mysterious circumstances... No Winners!");
		}
		
		return sj.toString();
	}
	
	@Override
	public String commandNotRegistered() {
		return prefix()
				+ ChatColor.RED + "That command is not registered!";
	}
	
	@Override
	public String actionNotAllowed() {
		return ChatColor.GRAY.toString()
				+ ChatColor.ITALIC.toString() + " I'm sorry, but I'm afraid I can't let you do that.";
	}

	@Override
	public String errorOccurred() {
		return prefix() + ChatColor.DARK_RED.toString()
				+ ChatColor.ITALIC.toString() + " An error has occurred. Incident has been reported to appropriate devs.";
	}
}
