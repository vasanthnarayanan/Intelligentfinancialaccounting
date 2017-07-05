package com.curious365.ifa.service;

import java.util.List;
import java.util.Map;

import com.curious365.ifa.dto.Item;
import com.curious365.ifa.dto.ItemQuantity;
import com.curious365.ifa.dto.ItemType;

public interface ItemService {
	public List<Item> listActiveItems();
	public long addNewItem(Item item);
	public List<ItemType> listItemTypes();
	public long addNewItemType(ItemType itemType);
	public void removeItemTypeById(String itemTypeId);
	public List<ItemQuantity> listItemQuantities();
	public long addNewItemQuantity(int itemQuantity);
	public void removeItemQuantityById(String itemQuantityId);
	public Map<String,Object> populateAutocomplete(String query);
	public Item getItemById(String itemId);
	public boolean editItem(Item item);
	public boolean removeItem(String itemId);
	public double getCostByItemId(String itemId);
}
