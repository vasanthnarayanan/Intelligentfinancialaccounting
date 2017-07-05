package com.curious365.ifa.controller.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.curious365.ifa.dto.Customer;
import com.curious365.ifa.dto.Item;
import com.curious365.ifa.service.ItemService;

@RestController
public class ItemRestController {
	private Log log = LogFactory.getLog(ItemRestController.class);
	
	@Autowired
	private ItemService itemService;
	
	public ItemService getItemService() {
		return itemService;
	}

	public void setItemService(ItemService itemService) {
		this.itemService = itemService;
	}
	
	@RequestMapping("/listActiveItems")
	public Map<String,List<Item>> listActiveItems(){
		log.debug("entering..listActiveItems");
		Map<String,List<Item>> activeItemMap = new HashMap<String,List<Item>>();
		List<Item> activeItemList = itemService.listActiveItems();
		activeItemMap.put("data", activeItemList);
		return activeItemMap;
	}
		
	@RequestMapping("/listItemsLike")
	public Map<String,Object> populateAutocomplete(@RequestParam("query") String query){
		log.debug("entering..");
		if(StringUtils.hasText(query)){
			query = query.toLowerCase();
		}
		return itemService.populateAutocomplete(query);
	}
	
	@RequestMapping(value="/addItemAsync",method=RequestMethod.POST)
	public String addItem(@ModelAttribute Item item){
		log.debug("entering..");
		String message = "Unable to add item!Please retry.";
		try{
			itemService.addNewItem(item);
		message = "Success";
		}catch(Exception e){
			
		}
		return message;
	}
	
	@RequestMapping("/getItemById")
	public Item getItemById(@RequestParam("itemid")String itemId){
		log.debug("entering..");
		Item item = null;
		if(StringUtils.hasText(itemId)){
			item = itemService.getItemById(itemId);	
		}
		return item;
	}
	
	@RequestMapping(value="/removeItem", method=RequestMethod.POST)
	public boolean removeItem(@RequestParam("itemid")String itemId){
		log.debug("entering..");
		return itemService.removeItem(itemId);
	}
	
	@RequestMapping(value="/getCostByItemId", method=RequestMethod.GET)
	public double getCostByItemId(@RequestParam String itemId){
		log.debug("entering.."+itemId);
		return itemService.getCostByItemId(itemId);
	}



}
