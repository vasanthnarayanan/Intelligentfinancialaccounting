package com.curious365.ifa.dao;

import java.util.List;

import com.curious365.ifa.dto.Item;
import com.curious365.ifa.dto.ItemQuantity;
import com.curious365.ifa.dto.ItemType;

public interface ItemDAO {
	public List<Item> listItems(int isActive);
	public boolean addNewItem(Item bean);
	public boolean increaseStock(Item item);
	public boolean decreaseStock(Item item);
	public boolean verifyStock(Item item);
	public Item getItemById(String itemId);
	public boolean editItem(Item bean);
	public boolean removeItem(String itemId);
	public List<Item> listItemLike(String query);
	public double getCostByItemId(String itemId);
	public List<ItemType> listItemTypes();
	public boolean addNewItemType(ItemType itemType);
	public boolean removeItemType(String itemTypeId);
	public List<ItemQuantity> listItemQuantities();
	public boolean addNewItemQuantity(int itemQuantity);
	public boolean removeItemQuantity(String itemQuantityId);
	public long getCurrentItemId();
	public long getCurrentItemTypeId();
	public long getCurrentItemQuantityId();
}
