package com.curious365.ifa.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.curious365.ifa.common.Constants;
import com.curious365.ifa.dto.Content;

public class PurchaseRowMapper implements RowMapper<Content>{

	@Override
	public Content mapRow(ResultSet rs, int rowNum) throws SQLException {
		Content row = new Content();
		row.setCustomerId(rs.getLong(Constants.PURCHASE_CUSTOMER_ID));
		row.setName(rs.getString(Constants.NAME));
		row.setRecordId(rs.getLong(Constants.PURCHASE_RECORD_ID));
		row.setRecordDate(rs.getString(Constants.DATE));
		row.setItemId(rs.getLong(Constants.PURCHASE_ITEM_ID));
		row.setItemName(rs.getString(Constants.PURCHASE_ITEM_NAME));
		row.setItemQuantity(rs.getString(Constants.PURCHASE_ITEM_QUANTITY));
		row.setItemType(rs.getString(Constants.PURCHASE_ITEM_TYPE));
		row.setPieces(rs.getLong(Constants.PURCHASE_PIECES));
		row.setCost(rs.getDouble(Constants.PURCHASE_COST));
		row.setRemarks(rs.getString(Constants.PURCHASE_REMARKS));
		return row;
	}

}
