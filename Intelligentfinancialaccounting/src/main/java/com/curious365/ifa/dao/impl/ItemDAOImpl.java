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
																				bean.getStock(),Constants.ACTIVE});
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
																				bean.getStock(),bean.getItemId()});
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
	public boolean addNewItemType(String itemType) {
		int flag = jdbcTemplate.update(QueryConstants.INSERT_ITEM_TYPE, new Object[]{itemType});
		if(flag>0){
		return true;
		}else{
		return false;
		}
	}

	@Override
	public boolean removeItemType(long itemTypeId) {
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
	public boolean removeItemQuantity(long itemQuantityId) {
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

}
