package com.curious365.ifa.service;

import java.util.List;
import java.util.Map;

import com.curious365.ifa.dto.Item;

public interface ItemService {
	public List<Item> listActiveItems();
	public boolean addNewItem(Item item);
	public Map<String,Object> populateAutocomplete(String query);
	public Item getItemById(String itemId);
	public boolean editItem(Item item);
	public boolean removeItem(String itemId);
	public double getCostByItemId(String itemId);
}
