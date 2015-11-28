package mcjagger.mc.craftgames.inventorymenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class InventoryMenu {

	private static HashMap<UUID, InventoryMenu> currentInventories = new HashMap<UUID, InventoryMenu>();
	
	private String name;
	private ArrayList<MenuItem> items = new ArrayList<MenuItem>();
	
	private HashMap<Integer, MenuItem> layout = null;
	private Inventory inventory = null;
	
	public InventoryMenu(String name, ArrayList<MenuItem> items) {
		this.name = name;
		this.items = items;
	}
	
	private Inventory constructInventory(){
		
		if (inventory != null)
			return inventory;

		layout = new HashMap<Integer, MenuItem>();
		
		int itemRows = (int)(-(Math.floor(items.size() / -7.0)));
		
		boolean needsScroll = itemRows > 4;
		boolean useFull = needsScroll && (items.size() <= 54);	
		
		if (!needsScroll) {
			inventory = Bukkit.createInventory(null, itemRows * 9, name);
			
			final int rowLength = (useFull)?9:7;
			
			for (int i = 0; i < items.size(); i++) {
				
				/*
				 * 
				 *   o0123456o
				 *   o7890123o
				 *   o4567890o
				 * 
				 * 
				 */
				
				int rowIndex = i/rowLength;
				int posInRow = i%rowLength;
				
				int sideBuffer = (9 - rowLength)/2;
				
				boolean noodlyRow = (items.size() - (rowIndex*rowLength)) < rowLength;
				if (noodlyRow) {
					sideBuffer = (9 - (items.size() - i)) / 2; 
				}
				
				int slot = (rowIndex * 9) + (posInRow) + (sideBuffer);
				/*
				try {
					Bukkit.broadcastMessage("put " + items.get(i).getIcon().getItemMeta().getDisplayName() + " in slot " + (rowIndex * 9) +"+"+ (posInRow) + "+" +  (sideBuffer)  +": " + slot);
				} catch (Exception e){
					Bukkit.broadcastMessage("put " + items.get(i).getIcon().getType().name() + " in slot " + slot);
					
				}*/
				inventory.setItem(slot, items.get(i).getIcon());
				layout.put(slot, items.get(i));
			}
		}
		
		return inventory;
	}
	
	public static void openMenu(Player player, InventoryMenu menu) {
		Inventory inventory = menu.constructInventory();
		
		player.openInventory(inventory);
		currentInventories.put(player.getUniqueId(), menu);
	}
	
	public static boolean inventoryClicked(UUID uniqueId, int slot) {
		if (!currentInventories.containsKey(uniqueId))
			return false;
		
		InventoryMenu menu = currentInventories.get(uniqueId);
		MenuItem item = menu.layout.get(slot);
		
		if (item == null)
			return true;
		
		inventoryClosed(uniqueId);
		
		item.getListener().onClick(Bukkit.getPlayer(uniqueId), menu, item);
		
		if (item.closeAfterClick())
			Bukkit.getPlayer(uniqueId).closeInventory();
		
		return true;
	}

	public static void inventoryClosed(UUID uniqueId) {
		//Player player = Bukkit.getPlayer(uniqueId);
		//if (player != null)
		//	player.closeInventory();
		
		currentInventories.remove(uniqueId);
	}
	
	

}
