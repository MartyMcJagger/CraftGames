package mcjagger.mc.craftgames.weapon;

import org.bukkit.entity.Player;

import mcjagger.mc.craftgames.api.Playable;

public abstract class WeaponBehavior {
	
	
	public static final WeaponBehavior DEFAULT = new WeaponBehavior(){
		@Override
		public WeaponResult onAction(Player attacker, Player victim, Playable playable) {
			return new WeaponResult(false, null);
		}
	};
	
	public WeaponResult onAction(Player attacker, Player victim, Playable playable) {return WeaponResult.NO_CHANGE;}
	
	public static class WeaponResult {
		
		public static final WeaponResult NO_CHANGE = new WeaponResult(false, null);
		
		private boolean isCancelled;
		private Double damage;
		
		public WeaponResult(boolean isCancelled, Double damage) {
			this.isCancelled = isCancelled;
			this.damage = damage;
		}
		
		public boolean isCancelled() {
			return isCancelled;
		}

		public void setCancelled(boolean isCancelled) {
			this.isCancelled = isCancelled;
		}

		public Double getDamage() {
			return damage;
		}

		public void setDamage(double damage) {
			this.damage = damage;
		}	
	}
	
}
