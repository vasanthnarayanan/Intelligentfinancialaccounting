package com.curious365.ifa.common;

public class QueryConstants {

	/**
	 * CustomerDAO
	 */
	
	public static final String COUNT_CUSTOMERS = "select count(*) from customer where activeflag=?";
	public static final String LIST_CUSTOMERS = "select * from customer where activeflag=?";
	public static final String LIST_CUSTOMERS_WT_PAGING = "select * from ( select a.*, ROWNUM rnum from (select * from customer where activeflag=?) a where ROWNUM <= ?)where rnum  > ?";
	public static final String LIST_CUSTOMERS_ASC_WT_PAGING = "select * from ( select a.*, ROWNUM rnum from (select * from customer where activeflag=? order by name) a where ROWNUM <= ?)where rnum  > ?";
	public static final String LIST_CUSTOMERS_DESC_WT_PAGING = "select * from ( select a.*, ROWNUM rnum from (select * from customer where activeflag=? order by name desc) a where ROWNUM <= ?)where rnum  > ?";
	public static final String CHECK_CUSTOMERNAME_AVAILABILITY = "select count(*) from customer where activeflag=? and name=?";
	public static final String INSERT_CUSTOMER = "insert into customer(customerid,name,activeflag,initialbalance,customeraddress,customerphonenumber) values(id_sequence.nextVal,?,?,?,?,?)";
	public static final String LIST_CUSTOMERS_LIKE = "select * from customer where activeflag=? and name like ?";
	public static final String LIST_CUSTOMER_IDS_LIKE = "select customerid from customer where activeflag=? and customerid like ?";
	public static final String GET_CUSTOMER_BY_ID = "select * from customer where activeflag=? and customerid=?";
	public static final String UPDATE_CUSTOMER = "update customer set name=?,initialbalance=?,customeraddress=?,customerphonenumber=? where customerid=?";
	public static final String SOFT_DELETE_CUSTOMER = "update customer set activeflag=? where customerid=?";
	
	/**
	 * ItemDAO
	 */
	
	public static final String LIST_ITEMS_LIKE = "select item.*,type,quantity from item,item_type,item_quantity  where activeflag=? and item.typeid=item_type.typeid and item.quantityid=item_quantity.quantityid and itemname like ?";	
	public static final String LIST_ITEMS = "select item.*,type,quantity from item,item_type,item_quantity where item.typeid=item_type.typeid and item.quantityid=item_quantity.quantityid and activeflag=?";
	public static final String GET_ITEM_BY_ID = "select item.*,type,quantity from item,item_type,item_quantity where itemid=? and item.typeid=item_type.typeid and item.quantityid=item_quantity.quantityid and activeflag=?";
	public static final String INSERT_ITEM= "insert into item(itemid,itemname,quantityid,typeid,cost,stock,activeflag) values(itemid_sequence.nextVal,?,?,?,?,?,?)";
	public static final String UPDATE_ITEM= "update item set itemname=?,quantityid=?,typeid=?,cost=?,stock=? where itemid=?";
	public static final String SOFT_DELETE_ITEM= "update item set activeflag=? where itemid=?";
	public static final String GET_COST_BY_ITEM_ID = "select cost from item where itemid=?";	
	public static final String INSERT_ITEM_TYPE = "insert into item_type(typeid,type) values(item_type_sequence.nextVal,?)";
	public static final String DELETE_ITEM_TYPE = "delete * from item_type where typeid=?";
	public static final String INSERT_ITEM_QUANTITY = "insert into item_quantity(quantityid,quantity) values(item_quantity_sequence.nextVal,?)";
	public static final String DELETE_ITEM_QUANTITY = "delete * from item_quantity where quantityid=?";
	
	/**
	 * AccountingDAO
	 */
	public static final String COUNT_ACTIVE_ACCOUNT_DETAILS = "select count(*) from ((select salesdate from sales where salescustomerid=? and activeflag=?)" +
			" union all (select purchasedate from purchase where purchasecustomerid=? and activeflag=?)" +
					" union all (select faultdate from fault where faultcustomerid=? and activeflag=?)" +
							" union all (select transactiondate from transaction where transactioncustomerid=? and activeflag=?))";
	public static final String LIST_PREV_DATEWISE_BALANCE = "select sum(total) from (select salescost*salespieces as total from sales where salescustomerid=? and salesdate=? and activeflag=?" +
    		" union all select purchasecost*-purchasepieces  from purchase where purchasecustomerid=? and purchasedate=? and activeflag=?" +
			" union all select faultcost*-faultpieces  from fault where faultcustomerid=? and faultdate=? and activeflag=?" +
					" union all select -transactionamount  from transaction where transactioncustomerid=? and transactiondate=? and is_income=? and activeflag=?" +
							" union all select transactionamount  from transaction where transactioncustomerid=? and transactiondate=? and is_income=? and activeflag=?)";
	public static final String LIST_ACTIVE_ACCOUNT_DETAILS = "select * from ( select inner.*, ROWNUM rnum from (select recordtype,to_char(salesdate,'DD/MON/YYYY') as recorddate,salesdate,salesrecordid as recordid,salespieces as pieces,salescost as cost,salesrecordtotal as total,salesremarks as remarks from ((select 'sales' as recordtype,salesdate,salesrecordid,salespieces,salescost,salespieces*salescost as salesrecordtotal,salesremarks from sales where salescustomerid=? and activeflag=?)" +
			" union all (select 'purchase' as recordtype,purchasedate,purchaserecordid,purchasepieces,purchasecost,purchasepieces*purchasecost as purchaserecordtotal,purchaseremarks from purchase where purchasecustomerid=? and activeflag=?)" +
			" union all (select 'fault' as recordtype,faultdate,faultrecordid,faultpieces,faultcost,faultpieces*faultcost as faultrecordtotal,faultremarks from fault where faultcustomerid=? and activeflag=?)" +
			" union all (select 'income' as recordtype,transactiondate,transactionrecordid,0,0,transactionamount,transactionremarks from transaction where transactioncustomerid=? and is_income=? and activeflag=?)" +
			" union all (select 'expenditure' as recordtype,transactiondate,transactionrecordid,0,0,transactionamount,transactionremarks from transaction where transactioncustomerid=? and is_income=? and activeflag=?)) order by salesdate) inner)where rnum  > ? and rnum <= ?";
	public static final String GET_PREVIOUS_PAGE_BALANCE = "select sum(total) from ( select inner.*, ROWNUM rnum from (select recordtype,to_char(salesdate,'DD/MON/YYYY') as recorddate,salesdate,salesrecordid as recordid,salespieces as pieces,salescost as cost,salesrecordtotal as total from ((select 'sales' as recordtype,salesdate,salesrecordid,salespieces,salescost,salespieces*salescost as salesrecordtotal from sales where salescustomerid=? and activeflag=?)" +
			" union all (select 'purchase' as recordtype,purchasedate,purchaserecordid,purchasepieces,purchasecost,purchasepieces*-purchasecost as purchaserecordtotal from purchase where purchasecustomerid=? and activeflag=?)" +
			" union all (select 'fault' as recordtype,faultdate,faultrecordid,faultpieces,faultcost,faultpieces*-faultcost as faultrecordtotal from fault where faultcustomerid=? and activeflag=?)" +
			" union all (select 'income' as recordtype,transactiondate,transactionrecordid,0,0,-transactionamount from transaction where transactioncustomerid=? and is_income=? and activeflag=?)" +
			" union all (select 'expenditure' as recordtype,transactiondate,transactionrecordid,0,0,transactionamount from transaction where transactioncustomerid=? and is_income=? and activeflag=?)) order by salesdate) inner)where rnum  > ? and rnum <= ?";
	public static final String LIST_PAGE_DATES = "select dates from ( select a.*, ROWNUM rnum from (select to_char(salesdate,'DD/MON/YYYY') as dates from ((select salesdate from sales where salescustomerid=? and activeflag=?)" +
			" union all (select purchasedate from purchase where purchasecustomerid=? and activeflag=?)" +
			" union all (select faultdate from fault where faultcustomerid=? and activeflag=?)" +
			" union all (select transactiondate from transaction where transactioncustomerid=? and activeflag=?))) a where ROWNUM <= ?)where rnum  > ?";
	public static final String LIST_CURR_DATEWISE_SALES_BALANCE = "select salesrecordid,salespieces,salescost,salespieces*salescost as salesrecordtotal from sales where salesdate=? and salescustomerid=? and activeflag=?";
	public static final String LIST_CURR_DATEWISE_PURCHASE_BALANCE = "select purchaserecordid,purchasepieces,purchasecost,purchasepieces*purchasecost as purchaserecordtotal from purchase where purchasedate=? and purchasecustomerid=? and activeflag=?";
	public static final String LIST_CURR_DATEWISE_FAULT_BALANCE = "select faultrecordid,faultpieces,faultcost,faultpieces*faultcost as faultrecordtotal from fault where faultdate=? and faultcustomerid=? and activeflag=?";
	public static final String LIST_CURR_DATEWISE_INCOME_BALANCE = "select transactionrecordid,transactionamount from transaction where transactiondate=? and transactioncustomerid=? and is_income=? and activeflag=?";
	public static final String LIST_CURR_DATEWISE_EXPENDITURE_BALANCE = "select transactionrecordid,transactionamount from transaction where transactiondate=? and transactioncustomerid=? and is_income=? and activeflag=?";
	public static final String LIST_TOTAL_BALANCE = "select sum(salescost*salespieces) from sales where salescustomerid=? and activeflag=?" +
    		" union all select sum(purchasecost*purchasepieces)  from purchase where purchasecustomerid=? and activeflag=?" +
			" union all select sum(faultcost*faultpieces)  from fault where faultcustomerid=? and activeflag=?" +
					" union all select sum(transactionamount)  from transaction where transactioncustomerid=? and is_income=? and activeflag=?" +
							" union all select sum(transactionamount)  from transaction where transactioncustomerid=? and is_income=? and activeflag=?";
	/*
	 * TransactionSheet
	 */
	public static final String GET_TRANSACTION = "select transactionrecordid,to_char(transactiondate,'DD/MM/YYYY')\"transactiondate\",transactioncustomerid,name as transactioncustomername,transactionamount,is_income,transactionremarks from transaction,customer where transactionrecordid=? and transactioncustomerid=customerid and transaction.activeflag=?";
	public static final String CREATE_TRANSACTION = "insert into transaction(transactionrecordid,transactiondate,transactioncustomerid,transactionamount,is_income,transactionremarks,activeflag) values(transactionrecordid_sequence.nextVal,?,?,?,?,?,?)";
	public static final String UPDATE_TRANSACTION = "update transaction set transactiondate=?,transactioncustomerid=?,transactionamount=?,is_income=?,transactionremarks=? where transactionrecordid=?";
	public static final String SOFT_DELETE_TRANSACTION  = "update transaction set activeflag=? where transactionrecordid=?";
	
	/**
	 * SalesSheet
	 */
	public static final String COUNT_ACTIVE_SALES = "select count(*) from sales,customer,item,item_type,item_quantity where sales.activeflag=? and sales.activeflag=customer.activeflag and salescustomerid=customerid and salesitemid=itemid and item.quantityid = item_quantity.quantityid and item.typeid = item_type.typeid";
	public static final String LIST_ALL_SALES = "select * from ( select a.*, ROWNUM rnum from (select salesrecordid,to_char(salesdate,'DD/MM/YYYY')\"date\",salescustomerid,name,itemname as salesitemname,salesitemid,salespieces,salescost,salesremarks,quantity as salesitemquantity,type as salesitemtype from sales,customer,item,item_type,item_quantity where sales.activeflag=? and sales.activeflag=customer.activeflag and salescustomerid=customerid and salesitemid=itemid and item.quantityid = item_quantity.quantityid and item.typeid = item_type.typeid order by salesdate) a where ROWNUM <= ?)where rnum  > ?";
	public static final String GET_SALES ="select salesrecordid,to_char(salesdate,'DD/MM/YYYY')\"salesdate\",salescustomerid,name as salescustomername,itemname as salesitemname,salesitemid,salespieces,salescost,salesremarks,quantity as salesitemquantity,type as salesitemtype from sales,customer,item,item_type,item_quantity where salesrecordid=? and salescustomerid=customerid and salesitemid=itemid and item.quantityid = item_quantity.quantityid and item.typeid = item_type.typeid and sales.activeflag=?";
	public static final String ADD_SALES = "insert into sales(salesrecordid,salesdate,salescustomerid,salesitemid,salespieces,salescost,salesremarks,activeflag) values(salesrecordid_sequence.nextVal,?,?,?,?,?,?,?)";
	public static final String UPDATE_SALES = "update sales set salesdate=?,salescustomerid=?,salesitemid=?,salespieces=?,salescost=?,salesremarks=? where salesrecordid=?";
	public static final String SOFT_DELETE_SALES = "update sales set activeflag=? where salesrecordid=?";
	
	/**
	 * PurchaseSheet
	 */
	public static final String COUNT_ACTIVE_PURCHASE = "select count(*) from purchase,customer,item,item_type,item_quantity where purchase.activeflag=? and purchase.activeflag=customer.activeflag and purchasecustomerid=customerid and purchaseitemid=itemid and item.quantityid = item_quantity.quantityid and item.typeid = item_type.typeid";
	public static final String LIST_ALL_PURCHASE = "select * from ( select a.*, ROWNUM rnum from (select purchaserecordid,to_char(purchasedate,'DD/MM/YYYY')\"date\",purchasecustomerid,name,itemname as purchaseitemname,purchaseitemid,purchasepieces,purchasecost,purchaseremarks,quantity as purchaseitemquantity,type as purchaseitemtype from purchase,customer,item,item_type,item_quantity where purchase.activeflag=? and purchase.activeflag=customer.activeflag  and purchasecustomerid=customerid and purchaseitemid=itemid and item.quantityid = item_quantity.quantityid and item.typeid = item_type.typeid order by purchasedate) a where ROWNUM <= ?)where rnum  > ?";
	public static final String GET_PURCHASE ="select purchaserecordid,to_char(purchasedate,'DD/MM/YYYY')\"purchasedate\",purchasecustomerid,name as purchasecustomername,itemname as purchaseitemname,purchaseitemid,purchasepieces,purchasecost,purchaseremarks,quantity as purchaseitemquantity,type as purchaseitemtype from purchase,customer,item,item_type,item_quantity where purchaserecordid=? and purchasecustomerid=customerid and purchaseitemid=itemid and item.quantityid = item_quantity.quantityid and item.typeid = item_type.typeid and purchase.activeflag=?";
	public static final String ADD_PURCHASE = "insert into purchase(purchaserecordid,purchasedate,purchasecustomerid,purchaseitemid,purchasepieces,purchasecost,purchaseremarks,activeflag) values(purchaserecordid_sequence.nextVal,?,?,?,?,?,?,?)";
	public static final String UPDATE_PURCHASE = "update purchase set purchasedate=?,purchasecustomerid=?,purchaseitemid=?,purchasepieces=?,purchasecost=?,purchaseremarks=? where purchaserecordid=?";
	public static final String SOFT_DELETE_PURCHASE = "update purchase set activeflag=? where purchaserecordid=?";
	
	
	/**
	 * FaultSheet
	 */
	public static final String COUNT_ACTIVE_FAULT = "select count(*) from fault,customer,item,item_type,item_quantity where fault.activeflag=?  and fault.activeflag=customer.activeflag and faultcustomerid=customerid and faultitemid=itemid and item.quantityid = item_quantity.quantityid and item.typeid = item_type.typeid";
	public static final String LIST_ALL_FAULT = "select * from ( select a.*, ROWNUM rnum from (select faultrecordid,to_char(faultdate,'DD/MM/YYYY')\"date\",faultcustomerid,name,itemname as faultitemname,faultitemid,faultpieces,faultcost,faultremarks,quantity as faultitemquantity,type as faultitemtype from fault,customer,item,item_type,item_quantity where fault.activeflag=? and fault.activeflag=customer.activeflag and faultcustomerid=customerid and faultitemid=itemid and item.quantityid = item_quantity.quantityid and item.typeid = item_type.typeid order by faultdate) a where ROWNUM <= ?)where rnum  > ?";
	public static final String GET_FAULT ="select faultrecordid,to_char(faultdate,'DD/MM/YYYY')\"faultdate\",faultcustomerid,name as faultcustomername,itemname as faultitemname,faultitemid,faultpieces,faultcost,faultremarks,quantity as faultitemquantity,type as faultitemtype from fault,customer,item,item_type,item_quantity where faultrecordid=? and faultcustomerid=customerid and faultitemid=itemid and item.quantityid = item_quantity.quantityid and item.typeid = item_type.typeid and fault.activeflag=?";
	public static final String ADD_FAULT = "insert into fault(faultrecordid,faultdate,faultcustomerid,faultitemid,faultpieces,faultcost,faultremarks,activeflag) values(faultrecordid_sequence.nextVal,?,?,?,?,?,?,?)";
	public static final String UPDATE_FAULT = "update fault set faultdate=?,faultcustomerid=?,faultitemid=?,faultpieces=?,faultcost=?,faultremarks=? where faultrecordid=?";
	public static final String SOFT_DELETE_FAULT = "update fault set activeflag=? where faultrecordid=?";
	
	/**
	 * Get current sequence
	 */
	
	public static final String GET_CUSTOMER_CURR_SEQ = "select id_sequence.currval from DUAL";
	public static final String GET_ITEM_CURR_SEQ = "select itemid_sequence.currval from DUAL";
	public static final String GET_ITEM_TYPE_CURR_SEQ = "select item_type_sequence.currval from DUAL";
	public static final String GET_ITEM_QUANTITY_CURR_SEQ = "select item_quantity_sequence.currval from DUAL";
	public static final String GET_TRANSACTION_CURR_SEQ = "select transactionrecordid_sequence.currval from DUAL";
	public static final String GET_SALES_CURR_SEQ = "select salesrecordid_sequence.currval from DUAL";
	public static final String GET_PURCHASE_CURR_SEQ = "select purchaserecordid_sequence.currval from DUAL";
	public static final String GET_FAULT_CURR_SEQ = "select faultrecordid_sequence.currval from DUAL";
}
