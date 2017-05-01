package com.curious365.ifa.dao;

import java.util.List;

import com.curious365.ifa.dto.Item;

public interface ItemDAO {
	public List<Item> listItems(int isActive);
	public boolean addNewItem(Item bean);
	public Item getItemById(String itemId);
	public boolean editItem(Item bean);
	public boolean removeItem(String itemId);
	public List<Item> listItemLike(String query);
	public double getCostByItemId(String itemId);
	public boolean addNewItemType(String itemType);
	public boolean removeItemType(long itemTypeId);
	public boolean addNewItemQuantity(int itemQuantity);
	public boolean removeItemQuantity(long itemQuantityId);
	public long getCurrentItemId();
	public long getCurrentItemTypeId();
	public long getCurrentItemQuantityId();
}
