package com.curious365.ifa.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.curious365.ifa.common.Constants;
import com.curious365.ifa.dto.Content;

public class SalesRowMapper implements RowMapper<Content>{

	@Override
	public Content mapRow(ResultSet rs, int rowNum) throws SQLException {
		Content row = new Content();
		row.setCustomerId(rs.getLong(Constants.SALES_CUSTOMER_ID));
		row.setName(rs.getString(Constants.NAME));
		row.setRecordId(rs.getLong(Constants.SALES_RECORD_ID));
		row.setRecordDate(rs.getString(Constants.DATE));
		row.setItemId(rs.getLong(Constants.SALES_ITEM_ID));
		row.setItemName(rs.getString(Constants.SALES_ITEM_NAME));
		row.setItemQuantity(rs.getString(Constants.SALES_ITEM_QUANTITY));
		row.setItemType(rs.getString(Constants.SALES_ITEM_TYPE));
		row.setPieces(rs.getLong(Constants.SALES_PIECES));
		row.setCost(rs.getDouble(Constants.SALES_COST));
		row.setRemarks(rs.getString(Constants.SALES_REMARKS));
		return row;
	}

}
