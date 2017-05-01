package com.curious365.ifa.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.curious365.ifa.common.Constants;
import com.curious365.ifa.dto.Item;
import com.curious365.ifa.service.ItemService;

@Controller
public class ItemsController {
	private Log log = LogFactory.getLog(ItemsController.class);
	
	@Autowired
	private ItemService itemService;
	
	
	public ItemService getItemService() {
		return itemService;
	}

	public void setItemService(ItemService itemService) {
		this.itemService = itemService;
	}

	@RequestMapping(value = "/listItems", method=RequestMethod.GET)
	public ModelAndView listItems(@RequestParam("pageno")int pageNo,@RequestParam("sorttype")String sortType){
		log.debug("entering..");
		ModelAndView mav = new ModelAndView();
		mav.addObject("pageno", pageNo);
		mav.addObject("sorttype", sortType);
		mav.setViewName("itemindex");
		return mav;
	}
	
	@RequestMapping(value="/addItem",method=RequestMethod.GET)
	public ModelAndView addItemDisplay(){
		log.debug("entering..");
		ModelAndView mav = new ModelAndView();
		mav.setViewName("additem");
		return mav;
	}
	
	@RequestMapping(value="/addItem",method=RequestMethod.POST)
	public ModelAndView addItem(@ModelAttribute Item item){
		log.debug("entering..");
		ModelAndView mav = new ModelAndView();
		itemService.addNewItem(item);
		mav.addObject("pageno", 1);
		mav.addObject("sorttype", "normal");
		mav.setViewName("itemindex");
		return mav;
	}
	
	@RequestMapping(value="/editItem",method=RequestMethod.GET)
	public ModelAndView editItemDisplay(@RequestParam(value="itemid",required=false)String itemId){
		log.debug("entering..");
		ModelAndView mav = new ModelAndView();
		Item item = null;
		if(StringUtils.hasText(itemId)){
			item = itemService.getItemById(itemId);	
		}
		
		if(null != item){
			item.setActiveFlag(Constants.ONE);
			mav.addObject("redirect",true);
		}else{
			item = new Item();
			item.setItemName(Constants.EMPTY_STRING);
			item.setItemId(Constants.EMPTY_STRING);
			item.setQuantity(Constants.ZERO);
			item.setType(Constants.EMPTY_STRING);
			item.setCost(Constants.ZERO);
			item.setActiveFlag(Constants.ZERO);
			mav.addObject("redirect",false);
		}
		mav.addObject("item", item);
		mav.setViewName("edititem");
		return mav;
	}
	
	@RequestMapping(value="/editItem",method=RequestMethod.POST)
	public ModelAndView editItem(@ModelAttribute Item item){
		log.debug("entering..");
		ModelAndView mav = new ModelAndView();
		itemService.editItem(item);
		mav.addObject("pageno", 1);
		mav.addObject("sorttype", "normal");
		mav.setViewName("itemindex");
		return mav;
	}
	
	@RequestMapping(value="/removeItem",method=RequestMethod.GET)
	public ModelAndView removeItemDisplay(){
		log.debug("entering..");
		ModelAndView mav = new ModelAndView();
		mav.setViewName("removeitem");
		return mav;
	}
	
	@RequestMapping(value="/removeItemNormal",method=RequestMethod.POST)
	public ModelAndView removeItem(@ModelAttribute Item item){
		log.debug("entering..");
		ModelAndView mav = new ModelAndView();
		itemService.removeItem(item.getItemId());
		mav.addObject("pageno", 1);
		mav.addObject("sorttype", "normal");
		mav.setViewName("itemindex");
		return mav;
	}

}
