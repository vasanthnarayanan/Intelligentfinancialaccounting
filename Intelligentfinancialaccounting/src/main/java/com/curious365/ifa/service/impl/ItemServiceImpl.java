 package com.curious365.ifa.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.curious365.ifa.common.Constants;
import com.curious365.ifa.dao.ItemDAO;
import com.curious365.ifa.dto.Item;
import com.curious365.ifa.dto.ItemAutocomplete;
import com.curious365.ifa.dto.ItemQuantity;
import com.curious365.ifa.dto.ItemType;
import com.curious365.ifa.service.ItemService;

@Service
public class ItemServiceImpl implements ItemService {
	
	@Autowired
	private ItemDAO itemDAO;
	
	public ItemDAO getItemDAO() {
		return itemDAO;
	}

	public void setItemDAO(ItemDAO itemDAO) {
		this.itemDAO = itemDAO;
	}

	@Override
	public Map<String, Object> populateAutocomplete(String query) {
		StringBuilder sb = new StringBuilder();
		Map<String,Object> similarItems = new HashMap<String,Object>();
		List<ItemAutocomplete> suggestions = new ArrayList<ItemAutocomplete>();
		sb.append(Constants.PERCENTAGE);
		sb.append(query);
		sb.append(Constants.PERCENTAGE);
		List<Item> items = itemDAO.listItemLike(sb.toString());
		for (Item item : items) {
			ItemAutocomplete suggestion = new ItemAutocomplete();
			suggestion.setData(item);
			suggestion.setValue(item.getItemName());
			suggestions.add(suggestion);
		}
		similarItems.put(Constants.QUERY, query);
		similarItems.put(Constants.SUGGESTIONS, suggestions);
		return similarItems;
	}

	@Override
	public List<Item> listActiveItems() {
		return itemDAO.listItems(Constants.ACTIVE);
	}

	@Override
	public long addNewItem(Item item) {
			itemDAO.addNewItem(item);
			return itemDAO.getCurrentItemId();
		}

	@Override
	public Item getItemById(String itemId) {
		return itemDAO.getItemById(itemId);
		}

	@Override
	public boolean editItem(Item item) {
		return itemDAO.editItem(item);
	}

	@Override
	public boolean removeItem(String itemId) {
		return itemDAO.removeItem(itemId);
	}

	@Override
	public double getCostByItemId(String itemId) {
		return itemDAO.getCostByItemId(itemId);
	}

	@Override
	public long addNewItemType(ItemType itemType) {
		itemDAO.addNewItemType(itemType);
		return itemDAO.getCurrentItemTypeId();
	}

	@Override
	public void removeItemTypeById(String itemTypeId) {
		itemDAO.removeItemType(itemTypeId);
	}

	@Override
	public long addNewItemQuantity(int itemQuantity) {
		itemDAO.addNewItemQuantity(itemQuantity);
		return itemDAO.getCurrentItemQuantityId();
	}

	@Override
	public void removeItemQuantityById(String itemQuantityId) {
		itemDAO.removeItemQuantity(itemQuantityId);
	}

	@Override
	public List<ItemType> listItemTypes() {
		return itemDAO.listItemTypes();
	}

	@Override
	public List<ItemQuantity> listItemQuantities() {
		return itemDAO.listItemQuantities();
	}

}
