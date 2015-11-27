package mcjagger.mc.craftgames.api;

import org.bukkit.entity.Player;

public abstract class WeaponBehavior {
	
	public static final WeaponBehavior DEFAULT = new WeaponBehavior(){
		public WeaponResult onAction(Player attacker, Player victim, Playable playable) {
			return new WeaponResult(false, null);
		}
	};
	
	//private final YamlConfiguration config;
	
	public WeaponBehavior() {
		
	}
	
	/*public WeaponBehavior(YamlConfiguration config) {
		this.config = config;
	}*/
	
	//protected YamlConfiguration getConfig() {
	//	return config;
	//}
	
	public abstract WeaponResult onAction(Player attacker, Player victim, Playable playable);

	public static class WeaponResult {
		
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
