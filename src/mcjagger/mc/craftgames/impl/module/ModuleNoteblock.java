package mcjagger.mc.craftgames.impl.module;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Note;
import org.bukkit.Note.Tone;
import org.bukkit.entity.Player;

import mcjagger.mc.craftgames.api.Game;
import mcjagger.mc.craftgames.api.GameModule;

public class ModuleNoteblock extends GameModule {
	
	public ModuleNoteblock(Game game) {
		super(game);
	}

	@Override
	public void pointScored(UUID uuid, Location location) {
		Player player = Bukkit.getPlayer(uuid);
		if (player != null) {
			player.playNote(player.getLocation(), Instrument.PIANO, Note.natural(1, Tone.C));
			player.playNote(player.getLocation(), Instrument.PIANO, Note.natural(1, Tone.G));
			player.playNote(player.getLocation(), Instrument.PIANO, Note.natural(2, Tone.C));
		}
	}

	@Override
	public void started() {
		for (UUID uuid : playable.getPlayers()) {
			Player player = Bukkit.getPlayer(uuid);
			if (player != null) {
				player.playNote(player.getLocation(), Instrument.PIANO, Note.natural(1, Tone.C));
			}
		}
	}
	
}