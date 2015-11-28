package mcjagger.mc.craftgames.weapon;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

public class YamlWeapon extends Weapon {

	//private final YamlConfiguration yaml;

	public final String name;
	public final Material material;
	
	public final WeaponBehavior behavior;
	
	public final WeaponBehavior melee;
	public final WeaponBehavior interact;
	public final WeaponBehavior primary;
	public final WeaponBehavior secondary;
	
	public YamlWeapon(YamlConfiguration yaml) {
		//this.yaml = yaml;
		
		name = yaml.getString(PATH_NAME);
		material = Material.matchMaterial(yaml.getString(PATH_MATERIAL, "IRON_SWORD"));
		
		if (name == null || name.length() < 1)
			throw new IllegalArgumentException("Weapon name not defined in YAML file.");
		
		behavior = YamlWeaponBehavior.getBehaviorFromYaml(yaml, "behavior");
		
		melee = YamlWeaponBehavior.getBehaviorFromYaml(yaml, PATH_MELEE);
		interact = YamlWeaponBehavior.getBehaviorFromYaml(yaml, PATH_INTERACT);
		primary = YamlWeaponBehavior.getBehaviorFromYaml(yaml, PATH_PRIMARY);
		secondary = YamlWeaponBehavior.getBehaviorFromYaml(yaml, PATH_SECONDARY);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public ItemStack getBaseItem() {
		return new ItemStack(material);
	}
}
