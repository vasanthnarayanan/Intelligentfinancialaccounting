package com.curious365.ifa.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.curious365.ifa.common.Constants;
import com.curious365.ifa.common.QueryConstants;
import com.curious365.ifa.dao.ItemDAO;
import com.curious365.ifa.dto.Item;
import com.curious365.ifa.dto.ItemQuantity;
import com.curious365.ifa.dto.ItemType;

@Repository
public class ItemDAOImpl implements ItemDAO {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}


	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public List<Item> listItemLike(String query) {
		List<Item> items = new ArrayList<Item>();
		try{
			items = jdbcTemplate.query(QueryConstants.LIST_ITEMS_LIKE, new BeanPropertyRowMapper<Item>(Item.class),new Object[]{Constants.ACTIVE,query});			
		}catch(Exception e){
			
		}
		return items;
	}


	
	@Override
	public List<Item> listItems(int isActive){
		List<Item> items = new ArrayList<Item>();
		try{
			items = jdbcTemplate.query(QueryConstants.LIST_ITEMS, new BeanPropertyRowMapper<Item>(Item.class), new Object[]{isActive}); 
		}catch(Exception e){
			
		}
		return items;
	}


	@Override
	public boolean addNewItem(Item bean) {
		int flag = jdbcTemplate.update(QueryConstants.INSERT_ITEM, new Object[]{bean.getItemName(),bean.getQuantityId(),
																				bean.getTypeId(),bean.getCost(),
																				bean.getStock(),bean.getVendor(),Constants.ACTIVE});
		if(flag>0){
			return true;
		}else{
			return false;
		}
	}


	@Override
	public Item getItemById(String itemId) {
		Item item = null;
		try{
			item = jdbcTemplate.queryForObject(QueryConstants.GET_ITEM_BY_ID,  new BeanPropertyRowMapper<Item>(Item.class),new Object[]{itemId,Constants.ACTIVE});
		}catch(Exception e){
			
		}
		return item;
	}



	@Override
	public boolean editItem(Item bean) {
		int flag = jdbcTemplate.update(QueryConstants.UPDATE_ITEM, new Object[]{bean.getItemName(),bean.getQuantityId(),
																				bean.getTypeId(),bean.getCost(),		
																				bean.getStock(),bean.getVendor(),bean.getItemId()});
		if(flag>0){
			return true;
		}else{
			return false;
		}
	}


	@Override
	public boolean removeItem(String itemId) {
		int flag = jdbcTemplate.update(QueryConstants.SOFT_DELETE_ITEM, new Object[]{Constants.INACTIVE,itemId});
		if(flag>0){
			return true;
		}else{
			return false;
		}
	}


	@Override
	public double getCostByItemId(String itemId) {
		return jdbcTemplate.queryForObject(QueryConstants.GET_COST_BY_ITEM_ID,Double.class,new Object[]{itemId});
	}


	@Override
	public boolean addNewItemType(ItemType itemType) {
		int flag = jdbcTemplate.update(QueryConstants.INSERT_ITEM_TYPE, new Object[]{itemType.getType(),itemType.getTaxRate(),itemType.getHsnCode()});
		if(flag>0){
		return true;
		}else{
		return false;
		}
	}

	@Override
	public boolean removeItemType(String itemTypeId) {
		int flag = jdbcTemplate.update(QueryConstants.DELETE_ITEM_TYPE, new Object[]{itemTypeId});
		if(flag>0){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public boolean addNewItemQuantity(int itemQuantity) {
		int flag = jdbcTemplate.update(QueryConstants.INSERT_ITEM_QUANTITY, new Object[]{itemQuantity});
		if(flag>0){
		return true;
		}else{
		return false;
		}
	}
	
	@Override
	public boolean removeItemQuantity(String itemQuantityId) {
		int flag = jdbcTemplate.update(QueryConstants.DELETE_ITEM_QUANTITY, new Object[]{itemQuantityId});
		if(flag>0){
			return true;
		}else{
			return false;
		}
	}


	@Override
	public long getCurrentItemId() {
		return jdbcTemplate.queryForObject(QueryConstants.GET_ITEM_CURR_SEQ, Long.class);
	}


	@Override
	public long getCurrentItemTypeId() {
		return jdbcTemplate.queryForObject(QueryConstants.GET_ITEM_TYPE_CURR_SEQ, Long.class);
	}


	@Override
	public long getCurrentItemQuantityId() {
		return jdbcTemplate.queryForObject(QueryConstants.GET_ITEM_QUANTITY_CURR_SEQ, Long.class);
	}


	@Override
	public boolean increaseStock(Item item) {
		int flag = jdbcTemplate.update(QueryConstants.INCREASE_STOCK, new Object[]{item.getStock(),item.getItemId()});
		if(flag>0){
			return true;
		}else{
			return false;
		}
	}


	@Override
	public boolean decreaseStock(Item item) {
		int flag = jdbcTemplate.update(QueryConstants.DECREASE_STOCK, new Object[]{item.getStock(),item.getItemId()});
		if(flag>0){
			return true;
		}else{
			return false;
		}
	}


	@Override
	public boolean verifyStock(Item item) {
		Double stockInHand = jdbcTemplate.queryForObject(QueryConstants.VERIFY_STOCK, Double.class, new Object[]{item.getStock(),item.getItemId()});
		if(stockInHand>=0){
			return true;
		}else{
			return false;
		}
	}


	@Override
	public List<ItemType> listItemTypes() {
		return jdbcTemplate.query(QueryConstants.LIST_ITEM_TYPE, new BeanPropertyRowMapper<ItemType>(ItemType.class));
	}


	@Override
	public List<ItemQuantity> listItemQuantities() {
		return jdbcTemplate.query(QueryConstants.LIST_ITEM_QUANTITY, new BeanPropertyRowMapper<ItemQuantity>(ItemQuantity.class));
	}

}
