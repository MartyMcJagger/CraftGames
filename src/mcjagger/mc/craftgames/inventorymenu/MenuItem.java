package mcjagger.mc.craftgames.inventorymenu;

import org.bukkit.inventory.ItemStack;

public class MenuItem {
	
	private ItemStack icon;
	private MenuItemListener listener;
	private boolean closeAfterClick = true;
	
	private String data = "";
	
	public MenuItem(ItemStack icon, MenuItemListener listener) {
		this.icon = icon;
		this.listener = listener;
	}
	
	public MenuItem(ItemStack icon, MenuItemListener listener, boolean closeAfterClick) {
		this.icon = icon;
		this.listener = listener;
		this.closeAfterClick = closeAfterClick;
	}
	
	public ItemStack getIcon(){
		return icon;
	}
	
	public void setCloseAfterClick(boolean closeAfterClick) {
		this.closeAfterClick = closeAfterClick;
	}
	public boolean closeAfterClick() {
		return closeAfterClick;
	}
	
	public void setListenerData(String data) {
		this.data = data;
	}
	public String getListenerData() {
		return data;
	}
	
	public MenuItemListener getListener() {
		return listener;
	}
}
