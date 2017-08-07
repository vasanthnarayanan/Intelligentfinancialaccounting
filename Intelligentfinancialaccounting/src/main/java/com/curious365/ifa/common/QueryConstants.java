package com.curious365.ifa.common;

public class QueryConstants {

	/**
	 * CustomerDAO
	 */
	
	public static final String LIST_STATE="select * from gst_state";
	public static final String COUNT_CUSTOMERS = "select count(1) from customer where activeflag=? and priveleged=0";
	public static final String LIST_CUSTOMERS = "select * from customer where activeflag=? and priveleged=0";
	/** UNUSED START**/
	public static final String LIST_CUSTOMERS_WT_PAGING = "select * from ( select a.*, ROWNUM rnum from (select * from customer where activeflag=? and priveleged=0) a where ROWNUM <= ?)where rnum  > ?";
	public static final String LIST_CUSTOMERS_ASC_WT_PAGING = "select * from ( select a.*, ROWNUM rnum from (select * from customer where activeflag=? and priveleged=0 order by name) a where ROWNUM <= ?)where rnum  > ?";
	public static final String LIST_CUSTOMERS_DESC_WT_PAGING = "select * from ( select a.*, ROWNUM rnum from (select * from customer where activeflag=? and priveleged=0 order by name desc) a where ROWNUM <= ?)where rnum  > ?";
	/** UNUSED END**/
	public static final String LIST_CUSTOMERS_LIKE = "select * from customer where activeflag=? and priveleged=0 and name like ?";
	public static final String LIST_CUSTOMER_IDS_LIKE = "select customerid from customer where activeflag=? and priveleged=0 and customerid like ?";
	public static final String COUNT_ALL_CUSTOMERS = "select count(1) from customer where activeflag=?";
	public static final String LIST_ALL_CUSTOMERS = "select * from customer where activeflag=?";
	/** UNUSED START**/
	public static final String LIST_ALL_CUSTOMERS_WT_PAGING = "select * from ( select a.*, ROWNUM rnum from (select * from customer where activeflag=?) a where ROWNUM <= ?)where rnum  > ?";
	public static final String LIST_ALL_CUSTOMERS_ASC_WT_PAGING = "select * from ( select a.*, ROWNUM rnum from (select * from customer where activeflag=? order by name) a where ROWNUM <= ?)where rnum  > ?";
	public static final String LIST_ALL_CUSTOMERS_DESC_WT_PAGING = "select * from ( select a.*, ROWNUM rnum from (select * from customer where activeflag=? order by name desc) a where ROWNUM <= ?)where rnum  > ?";
	/** UNUSED END**/
	public static final String LIST_ALL_CUSTOMERS_LIKE = "select * from customer where activeflag=? and name like ?";
	public static final String LIST_ALL_CUSTOMER_IDS_LIKE = "select customerid from customer where activeflag=? and customerid like ?";
	public static final String CHECK_CUSTOMERNAME_AVAILABILITY = "select count(1) from customer where activeflag=? and name=?";
	public static final String GET_CUSTOMER_BY_ID = "select * from customer where activeflag=? and customerid=?";
	public static final String INCREASE_CURRENT_BALANCE_BY_ID = "update customer set currentbalance=currentbalance+? where customerid=?";
	public static final String DECREASE_CURRENT_BALANCE_BY_ID = "update customer set currentbalance=currentbalance-? where customerid=?";
	public static final String INSERT_CUSTOMER = "insert into customer(customerid,name,activeflag,initialbalance,currentbalance,customeraddress,customerstate,customerphonenumber,taxuniqueid,priveleged) values(id_sequence.nextVal,?,?,?,?,?,?,?,?,?)";
	public static final String UPDATE_CUSTOMER = "update customer set name=?,initialbalance=?,customeraddress=?,customerstate=?,customerphonenumber=?,taxuniqueid=?,priveleged=? where customerid=?";
	public static final String SOFT_DELETE_CUSTOMER = "update customer set activeflag=? where customerid=?";
	
	/**
	 * ItemDAO
	 */
	
	public static final String LIST_ITEMS_LIKE = "select item.*,type,taxrate,quantity from item,item_type,item_quantity  where activeflag=? and item.typeid=item_type.typeid and item.quantityid=item_quantity.quantityid and itemname||itemid like ?";	
	public static final String LIST_ITEMS = "select item.*,type,taxrate,quantity from item,item_type,item_quantity where item.typeid=item_type.typeid and item.quantityid=item_quantity.quantityid and activeflag=?";
	public static final String GET_ITEM_BY_ID = "select item.*,type,quantity from item,item_type,item_quantity where itemid=? and item.typeid=item_type.typeid and item.quantityid=item_quantity.quantityid and activeflag=?";
	public static final String INSERT_ITEM= "insert into item(itemid,itemname,quantityid,typeid,cost,stock,vendor,activeflag) values(itemid_sequence.nextVal,?,?,?,?,?,?,?)";
	public static final String UPDATE_ITEM= "update item set itemname=?,quantityid=?,typeid=?,cost=?,stock=?,vendor=? where itemid=?";
	public static final String INCREASE_STOCK= "update item set stock=stock+? where itemid=?";
	public static final String DECREASE_STOCK= "update item set stock=stock-? where itemid=?";
	public static final String VERIFY_STOCK= "select stock-? from item where itemid=?";
	public static final String SOFT_DELETE_ITEM= "update item set activeflag=? where itemid=?";
	public static final String GET_COST_BY_ITEM_ID = "select cost from item where itemid=?";
	public static final String LIST_ITEM_TYPE = "select * from item_type";
	public static final String INSERT_ITEM_TYPE = "insert into item_type(typeid,type,taxrate,hsncode) values(item_type_sequence.nextVal,?,?,?)";
	public static final String DELETE_ITEM_TYPE = "delete * from item_type where typeid=?";
	public static final String LIST_ITEM_QUANTITY = "select * from item_quantity";
	public static final String INSERT_ITEM_QUANTITY = "insert into item_quantity(quantityid,quantity) values(item_quantity_sequence.nextVal,?)";
	public static final String DELETE_ITEM_QUANTITY = "delete * from item_quantity where quantityid=?";
	
	/**
	 * AccountingDAO
	 */
	public static final String COUNT_ACTIVE_ACCOUNT_DETAILS = "select count(1) from ((select salesdate from sales where salescustomerid=? and activeflag=?)" +
			" union all (select purchasedate from purchase where purchasecustomerid=? and activeflag=?)" +
					" union all (select faultdate from fault where faultcustomerid=? and activeflag=?)" +
							" union all (select transactiondate from transaction where transactioncustomerid=? and activeflag=?))";
	/*public static final String LIST_PREV_DATEWISE_BALANCE = "select sum(total) from (select salescost*salespieces as total from sales where salescustomerid=? and salesdate=? and activeflag=?" +
    		" union all select purchasecost*-purchasepieces  from purchase where purchasecustomerid=? and purchasedate=? and activeflag=?" +
			" union all select faultcost*-faultpieces  from fault where faultcustomerid=? and faultdate=? and activeflag=?" +
					" union all select -transactionamount  from transaction where transactioncustomerid=? and transactiondate=? and is_income=? and activeflag=?" +
							" union all select transactionamount  from transaction where transactioncustomerid=? and transactiondate=? and is_income=? and activeflag=?)";*/
	public static final String LIST_ACTIVE_ACCOUNT_DETAILS = "select * from ( select inner.*, ROWNUM rnum from (select recordtype,to_char(salesdate,'DD/MON/YYYY') as recorddate,salesdate,salesrecordid as recordid,salespieces as pieces,salescost as cost,ROUND(salestax) as tax,ROUND(salesrecordtotal) as total,salesremarks as remarks from ((select 'sales' as recordtype,salesdate,salesrecordid,salespieces,salescost,salestax,(salespieces*salescost)+salestax as salesrecordtotal,salesremarks from sales where salescustomerid=? and activeflag=?)" +
			" union all (select 'purchase' as recordtype,purchasedate,purchaserecordid,purchasepieces,purchasecost,purchasetax,(purchasepieces*purchasecost)+purchasetax as purchaserecordtotal,purchaseremarks from purchase where purchasecustomerid=? and activeflag=?)" +
			" union all (select 'fault' as recordtype,faultdate,faultrecordid,faultpieces,faultcost,faulttax,(faultpieces*faultcost)+faulttax as faultrecordtotal,faultremarks from fault where faultcustomerid=? and activeflag=?)" +
			" union all (select 'income' as recordtype,transactiondate,transactionrecordid,0,0,0,transactionamount,transactionremarks from transaction where transactioncustomerid=? and is_income=? and activeflag=?)" +
			" union all (select 'expenditure' as recordtype,transactiondate,transactionrecordid,0,0,0,transactionamount,transactionremarks from transaction where transactioncustomerid=? and is_income=? and activeflag=?)) order by salesdate) inner)where rnum  > ? and rnum <= ?";
	public static final String GET_PREVIOUS_PAGE_BALANCE = "select sum(total) from ( select inner.*, ROWNUM rnum from (select recordtype,to_char(salesdate,'DD/MON/YYYY') as recorddate,salesdate,salesrecordid as recordid,salespieces as pieces,salescost as cost,ROUND(salesrecordtotal) as total from ((select 'sales' as recordtype,salesdate,salesrecordid,salespieces,salescost,(salespieces*salescost)+salestax as salesrecordtotal from sales where salescustomerid=? and activeflag=?)" +
			" union all (select 'purchase' as recordtype,purchasedate,purchaserecordid,purchasepieces,purchasecost,-((purchasepieces*purchasecost)+purchasetax) as purchaserecordtotal from purchase where purchasecustomerid=? and activeflag=?)" +
			" union all (select 'fault' as recordtype,faultdate,faultrecordid,faultpieces,faultcost,-((faultpieces*faultcost)+faulttax) as faultrecordtotal from fault where faultcustomerid=? and activeflag=?)" +
			" union all (select 'income' as recordtype,transactiondate,transactionrecordid,0,0,-transactionamount from transaction where transactioncustomerid=? and is_income=? and activeflag=?)" +
			" union all (select 'expenditure' as recordtype,transactiondate,transactionrecordid,0,0,transactionamount from transaction where transactioncustomerid=? and is_income=? and activeflag=?)) order by salesdate) inner)where rnum  > ? and rnum <= ?";
	/*public static final String LIST_PAGE_DATES = "select dates from ( select a.*, ROWNUM rnum from (select to_char(salesdate,'DD/MON/YYYY') as dates from ((select salesdate from sales where salescustomerid=? and activeflag=?)" +
			" union all (select purchasedate from purchase where purchasecustomerid=? and activeflag=?)" +
			" union all (select faultdate from fault where faultcustomerid=? and activeflag=?)" +
			" union all (select transactiondate from transaction where transactioncustomerid=? and activeflag=?))) a where ROWNUM <= ?)where rnum  > ?";*/
	/*public static final String LIST_CURR_DATEWISE_SALES_BALANCE = "select salesrecordid,salespieces,salescost,salespieces*salescost as salesrecordtotal from sales where salesdate=? and salescustomerid=? and activeflag=?";
	public static final String LIST_CURR_DATEWISE_PURCHASE_BALANCE = "select purchaserecordid,purchasepieces,purchasecost,purchasepieces*purchasecost as purchaserecordtotal from purchase where purchasedate=? and purchasecustomerid=? and activeflag=?";
	public static final String LIST_CURR_DATEWISE_FAULT_BALANCE = "select faultrecordid,faultpieces,faultcost,faultpieces*faultcost as faultrecordtotal from fault where faultdate=? and faultcustomerid=? and activeflag=?";
	public static final String LIST_CURR_DATEWISE_INCOME_BALANCE = "select transactionrecordid,transactionamount from transaction where transactiondate=? and transactioncustomerid=? and is_income=? and activeflag=?";
	public static final String LIST_CURR_DATEWISE_EXPENDITURE_BALANCE = "select transactionrecordid,transactionamount from transaction where transactiondate=? and transactioncustomerid=? and is_income=? and activeflag=?";*/
	public static final String LIST_TOTAL_BALANCE = "select sum((salescost*salespieces)+salestax) from sales where salescustomerid=? and activeflag=?" +
    		" union all select sum((purchasecost*purchasepieces)+purchasetax)  from purchase where purchasecustomerid=? and activeflag=?" +
			" union all select sum((faultcost*faultpieces)+faulttax)  from fault where faultcustomerid=? and activeflag=?" +
					" union all select sum(transactionamount)  from transaction where transactioncustomerid=? and is_income=? and activeflag=?" +
							" union all select sum(transactionamount)  from transaction where transactioncustomerid=? and is_income=? and activeflag=?";
	/**BankAccount
	 * 
	 */
	public static final String LIST_BANK_ACCOUNTS = "select * from bankaccount where activeflag=?";
	public static final String GET_BANK_ACCOUNT = "select * from bankaccount where bankid=? and activeflag=?";
	public static final String CREATE_BANK_ACCONT = "insert into bankaccount(bankid,bankname,bankaddress,bankaccountnumber,bankifsccode,bankaccounttype,activeflag) values(bank_id_sequence.nextVal,?,?,?,?,?,?)";
	public static final String UPDATE_BANK_ACCOUNT = "update bankaccount set bankname=?,bankaddress=?,bankaccountnumber=?,bankifsccode=?,bankaccounttype=? where bankid=?";
	public static final String SOFT_DELETE_BANK_ACCOUNT  = "update bankaccount set activeflag=? where bankid=?";
	
	/**Transportation
	 * 
	 */
	public static final String LIST_TRANSPORT = "select * from transport where activeflag=?";
	public static final String GET_TRANSPORT = "select * from transport where transportid=? and activeflag=?";
	public static final String CREATE_TRANSPORT = "insert into transport(transportid,transportname,transportaddress,transportphone,vehicleno,activeflag) values(transport_id_sequence.nextVal,?,?,?,?,?)";
	public static final String UPDATE_TRANSPORT = "update transport set transportname=?,transportaddress=?,transportphone=?,vehicleno=? where transportid=?";
	public static final String SOFT_DELETE_TRANSPORT  = "update transport set activeflag=? where bankid=?";
	
	/**OrderBook
	 * 
	 */
	public static final String LIST_INVOICE_ORDERS = "select * from invoiceorder where activeflag=?";
	public static final String GET_INVOICE_ORDER = "select * from invoiceorder where orderid=? and activeflag=?";
	public static final String CREATE_INVOICE_ORDER = "insert into invoiceorder(orderid,orderdate,ordertype,ordermode,orderstatus,remarks,activeflag) values(order_id_sequence.nextVal,?,?,?,?,?,?)";
	public static final String UPDATE_INVOICE_ORDER = "update invoiceorder set orderdate=?,ordertype=?,ordermode=?,orderstatus=?,remarks=? where orderid=?";
	public static final String SOFT_DELETE_INVOICE_ORDER  = "update invoiceorder set activeflag=? where bankid=?";
	
	/**
	 * TransactionSheet
	 */
	public static final String GET_TRANSACTION = "select transactionrecordid,to_char(transactiondate,'DD/MM/YYYY')\"transactiondate\",transactioncustomerid,transactioncustomer.name as transactioncustomername,transactionamount,is_income,transactionremarks,modeofpayment,to_char(duedate,'DD/MM/YYYY')\"duedate\",transactionstatus,chequenumber,chequebankid,refcustomerid,refcustomer.name as refcustomername,bankid from transaction inner join customer transactioncustomer on transaction.transactioncustomerid=transactioncustomer.customerid left join customer refcustomer on transaction.refcustomerid=refcustomer.customerid where transactionrecordid=? and transaction.activeflag=?";
	public static final String COUNT_TRANSACTION_BY_STATUS = "select count(1) from transaction where transaction.transactionstatus=? and transaction.activeflag=?";
	public static final String LIST_TRANSACTION_BY_STATUS = "select * from ( select a.*, ROWNUM rnum from (select transactionrecordid,to_char(transactiondate,'DD/MM/YYYY')\"transactiondate\",transactioncustomerid,transactioncustomer.name as transactioncustomername,transactionamount,is_income,transactionremarks,modeofpayment,to_char(duedate,'DD/MM/YYYY')\"duedate\",transactionstatus,chequenumber,chequebankid,refcustomerid,refcustomer.name as refcustomername,bankid from transaction inner join customer transactioncustomer on transaction.transactioncustomerid=transactioncustomer.customerid left join customer refcustomer on transaction.refcustomerid=refcustomer.customerid where transaction.transactionstatus=? and transaction.activeflag=?) a where ROWNUM <= ?)where rnum  > ?";
	public static final String CREATE_TRANSACTION = "insert into transaction(transactionrecordid,transactiondate,transactioncustomerid,transactionamount,is_income,transactionremarks,modeofpayment,duedate,transactionstatus,chequenumber,chequebankid,refcustomerid,bankid,activeflag) values(transactionrecordid_sequence.nextVal,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	public static final String UPDATE_TRANSACTION = "update transaction set transactiondate=?,transactioncustomerid=?,transactionamount=?,is_income=?,transactionremarks=?,modeofpayment=?,duedate=?,transactionstatus=?,chequenumber=?,chequebankid=?,refcustomerid=?,bankid=? where transactionrecordid=?";
	public static final String SOFT_DELETE_TRANSACTION  = "update transaction set activeflag=? where transactionrecordid=?";
	
	/**Estimate Invoice
	 * 
	 */
	public static final String LIST_INVOICE_LIKE = "select invoiceid,to_char(invoicedate,'DD/MM/YYYY')\"invoicedate\",invoicetype,invoicecustomerid,invoice.remarks as remarks,name as invoicecustomername from invoice inner join customer on invoice.invoicecustomerid=customer.customerid where invoice.activeflag=? and  customer.priveleged=0 and invoicetype=? and invoiceid like ?";
	public static final String LIST_INVOICE_INCL_PRIV_LIKE = "select invoiceid,to_char(invoicedate,'DD/MM/YYYY')\"invoicedate\",invoicetype,invoicecustomerid,invoice.remarks as remarks,name as invoicecustomername from invoice inner join customer on invoice.invoicecustomerid=customer.customerid where invoice.activeflag=? and invoicetype=? and invoiceid like ?";
	public static final String GET_INVOICE_BY_ID = "select invoiceid,to_char(invoicedate,'DD/MM/YYYY')\"invoicedate\",invoicetype,invoicecustomerid,invoice.remarks as remarks,name as invoicecustomername from invoice inner join customer on invoice.invoicecustomerid=customer.customerid where invoice.activeflag=? and invoiceid=?";
	public static final String CREATE_INVOICE = "insert into invoice(invoiceid,invoicedate,invoicetype,invoicecustomerid,invoicetransportid,orderid,remarks,activeflag) values(invoice_id_sequence.nextVal,?,?,?,?,?,?,?)";
	public static final String UPDATE_INVOICE = "update invoice set invoicedate=?,invoicetype=?,invoicecustomerid=?,invoicetransportid=?,orderid=?,remarks=? where invoiceid=?";
	public static final String SOFT_DELETE_INVOICE  = "update invoice set activeflag=? where invoiceid=?";
	
	/**Tax Invoice
	 * 
	 */
	public static final String LIST_TAX_INVOICE_LIKE = "select taxinvoiceid,to_char(taxinvoicedate,'DD/MM/YYYY')\"taxinvoicedate\",taxinvoicetype,taxinvoicecustomerid,taxinvoice.remarks as remarks,name as taxinvoicecustomername from taxinvoice inner join customer on taxinvoice.taxinvoicecustomerid=customer.customerid where taxinvoice.activeflag=? and  customer.priveleged=0 and taxinvoicetype=? and taxinvoiceid like ?";
	public static final String LIST_TAX_INVOICE_INCL_PRIV_LIKE = "select taxinvoiceid,to_char(taxinvoicedate,'DD/MM/YYYY')\"taxinvoicedate\",taxinvoicetype,taxinvoicecustomerid,taxinvoice.remarks as remarks,name as taxinvoicecustomername from taxinvoice inner join customer on taxinvoice.taxinvoicecustomerid=customer.customerid where taxinvoice.activeflag=? and taxinvoicetype=? and taxinvoiceid like ?";
	public static final String GET_TAX_INVOICE_BY_ID = "select taxinvoiceid,to_char(taxinvoicedate,'DD/MM/YYYY')\"taxinvoicedate\",taxinvoicetype,taxinvoicecustomerid,taxinvoice.remarks as remarks,name as taxinvoicecustomername from taxinvoice inner join customer on taxinvoice.taxinvoicecustomerid=customer.customerid where taxinvoice.activeflag=? and taxinvoiceid=?";
	public static final String CREATE_INSTANT_TAX_INVOICE = "insert into taxinvoice(taxinvoiceid,taxinvoicedate,taxinvoicetype,taxinvoicecustomerid,taxinvoicetransportid,orderid,remarks,activeflag) values(?,?,?,?,?,?,?,?)";
	public static final String CREATE_TAX_INVOICE = "insert into taxinvoice(taxinvoiceid,taxinvoicedate,taxinvoicetype,taxinvoicecustomerid,taxinvoicetransportid,orderid,remarks,activeflag) values(taxinvoice_id_sequence.nextVal,?,?,?,?,?,?,?)";
	public static final String UPDATE_TAX_INVOICE = "update taxinvoice set taxinvoicedate=?,taxinvoicetype=?,taxinvoicecustomerid=?,taxinvoicetransportid=?,orderid=?,remarks=? where taxinvoiceid=?";
	public static final String SOFT_DELETE_TAX_INVOICE  = "update taxinvoice set activeflag=? where taxinvoiceid=?";
	public static final String GET_LAST_INVOICE_ID = "select max(invoiceid) from invoice where invoicedate=? and activeflag=?";
	public static final String GET_LAST_INVOICE_ID_BY_MONTH = "select max(invoiceid) from invoice where activeflag=?";
	
	/**
	 * Audited Sales
	 */
	public static final String ADD_AUDITED_SALES = "insert into auditedsales(auditedsalesid,salesrecordid,salesdate,salescustomerid,salesitemid,salespieces,salescost,salesremarks,salestax,salestaxrate,invoiceid,taxinvoiceid,activeflag) values(auditedsalesid_sequence.nextVal,?,?,?,?,?,?,?,?,?,?,?,?)";
	public static final String LIST_AUDITED_SALES_BY_MONTH_YEAR = "select auditedsalesid,salesrecordid,to_char(salesdate,'DD/MM/YYYY')\"salesdate\",salescustomerid,name as salescustomername,itemname as salesitemname,salesitemid,salespieces,salescost,salesremarks,quantity as salesitemquantity,type as salesitemtype,stock as salesstock,salestaxrate,salestax,invoiceid,taxinvoiceid from auditedsales,customer,item,item_type,item_quantity where salescustomerid=customerid and salesitemid=itemid and item.quantityid = item_quantity.quantityid and item.typeid = item_type.typeid and auditedsales.activeflag=? and auditedsales.salesdate like ?";
	
	/**
	 * SalesSheet
	 */
	public static final String COUNT_ACTIVE_SALES = "select count(1) from sales,customer,item,item_type,item_quantity where sales.activeflag=? and customer.priveleged=0 and sales.activeflag=customer.activeflag and salescustomerid=customerid and salesitemid=itemid and item.quantityid = item_quantity.quantityid and item.typeid = item_type.typeid";
	// sorted by date and id to ensure order is preserved when two records belong to same date
	public static final String LIST_ALL_SALES = "select * from ( select a.*, ROWNUM rnum from (select salesrecordid,to_char(salesdate,'DD/MM/YYYY')\"date\",salescustomerid,name,itemname as salesitemname,salesitemid,salespieces,salescost,salesremarks,quantity as salesitemquantity,type as salesitemtype from sales,customer,item,item_type,item_quantity where sales.activeflag=? and customer.priveleged=0 and sales.activeflag=customer.activeflag and salescustomerid=customerid and salesitemid=itemid and item.quantityid = item_quantity.quantityid and item.typeid = item_type.typeid order by salesdate,salesrecordid) a where ROWNUM <= ?)where rnum  > ?";
	public static final String COUNT_ACTIVE_SALES_INCL_PRIV = "select count(1) from sales,customer,item,item_type,item_quantity where sales.activeflag=? and sales.activeflag=customer.activeflag and salescustomerid=customerid and salesitemid=itemid and item.quantityid = item_quantity.quantityid and item.typeid = item_type.typeid";
	public static final String LIST_ALL_SALES_INCL_PRIV = "select * from ( select a.*, ROWNUM rnum from (select salesrecordid,to_char(salesdate,'DD/MM/YYYY')\"date\",salescustomerid,name,itemname as salesitemname,salesitemid,salespieces,salescost,salesremarks,quantity as salesitemquantity,type as salesitemtype from sales,customer,item,item_type,item_quantity where sales.activeflag=? and sales.activeflag=customer.activeflag and salescustomerid=customerid and salesitemid=itemid and item.quantityid = item_quantity.quantityid and item.typeid = item_type.typeid order by salesdate,salesrecordid) a where ROWNUM <= ?)where rnum  > ?";
	public static final String GET_SALES ="select salesrecordid,to_char(salesdate,'DD/MM/YYYY')\"salesdate\",salescustomerid,name as salescustomername,itemname as salesitemname,salesitemid,salespieces,salescost,salesremarks,quantity as salesitemquantity,type as salesitemtype,stock as salesstock,salestaxrate,salestax,invoiceid from sales,customer,item,item_type,item_quantity where salesrecordid=? and salescustomerid=customerid and salesitemid=itemid and item.quantityid = item_quantity.quantityid and item.typeid = item_type.typeid and sales.activeflag=?";
	public static final String ADD_SALES = "insert into sales(salesrecordid,salesdate,salescustomerid,salesitemid,salespieces,salescost,salesremarks,salestax,salestaxrate,invoiceid,activeflag) values(salesrecordid_sequence.nextVal,?,?,?,?,?,?,?,?,?,?)";
	public static final String UPDATE_SALES = "update sales set salesdate=?,salescustomerid=?,salesitemid=?,salespieces=?,salescost=?,salesremarks=?,salestax=?,salestaxrate=?,invoiceid=? where salesrecordid=?";
	public static final String SOFT_DELETE_SALES = "update sales set activeflag=? where salesrecordid=?";
	public static final String LIST_SALES_BY_INVOICE_ID = "select salesrecordid,to_char(salesdate,'DD/MM/YYYY')\"salesdate\",salescustomerid,name as salescustomername,itemname as salesitemname,salesitemid,salespieces,salescost,salesremarks,quantity as salesitemquantity,type as salesitemtype,stock as salesstock,salestaxrate,salestax,invoiceid from sales,customer,item,item_type,item_quantity where salescustomerid=customerid and salesitemid=itemid and item.quantityid = item_quantity.quantityid and item.typeid = item_type.typeid and sales.activeflag=? and invoiceid=?";
	public static final String LIST_SALES_BY_MONTH_YEAR = "select salesrecordid,to_char(salesdate,'DD/MM/YYYY')\"salesdate\",salescustomerid,name as salescustomername,itemname as salesitemname,salesitemid,salespieces,salescost,salesremarks,quantity as salesitemquantity,type as salesitemtype,stock as salesstock,salestaxrate,salestax,invoiceid from sales,customer,item,item_type,item_quantity where salescustomerid=customerid and salesitemid=itemid and item.quantityid = item_quantity.quantityid and item.typeid = item_type.typeid and sales.activeflag=? and sales.salesdate like ?";
	
	/**
	 * PurchaseSheet
	 */
	public static final String COUNT_ACTIVE_PURCHASE = "select count(1) from purchase,customer,item,item_type,item_quantity where purchase.activeflag=? and customer.priveleged=0 and purchase.activeflag=customer.activeflag and purchasecustomerid=customerid and purchaseitemid=itemid and item.quantityid = item_quantity.quantityid and item.typeid = item_type.typeid";
	// sorted by date and id to ensure order is preserved when two records belong to same date
	public static final String LIST_ALL_PURCHASE = "select * from ( select a.*, ROWNUM rnum from (select purchaserecordid,to_char(purchasedate,'DD/MM/YYYY')\"date\",purchasecustomerid,name,itemname as purchaseitemname,purchaseitemid,purchasepieces,purchasecost,purchaseremarks,quantity as purchaseitemquantity,type as purchaseitemtype from purchase,customer,item,item_type,item_quantity where purchase.activeflag=? and customer.priveleged=0 and purchase.activeflag=customer.activeflag  and purchasecustomerid=customerid and purchaseitemid=itemid and item.quantityid = item_quantity.quantityid and item.typeid = item_type.typeid order by purchasedate,purchaserecordid) a where ROWNUM <= ?)where rnum  > ?";
	public static final String COUNT_ACTIVE_PURCHASE_INCL_PRIV = "select count(1) from purchase,customer,item,item_type,item_quantity where purchase.activeflag=? and purchase.activeflag=customer.activeflag and purchasecustomerid=customerid and purchaseitemid=itemid and item.quantityid = item_quantity.quantityid and item.typeid = item_type.typeid";
	public static final String LIST_ALL_PURCHASE_INCL_PRIV = "select * from ( select a.*, ROWNUM rnum from (select purchaserecordid,to_char(purchasedate,'DD/MM/YYYY')\"date\",purchasecustomerid,name,itemname as purchaseitemname,purchaseitemid,purchasepieces,purchasecost,purchaseremarks,quantity as purchaseitemquantity,type as purchaseitemtype from purchase,customer,item,item_type,item_quantity where purchase.activeflag=? and purchase.activeflag=customer.activeflag  and purchasecustomerid=customerid and purchaseitemid=itemid and item.quantityid = item_quantity.quantityid and item.typeid = item_type.typeid order by purchasedate,purchaserecordid) a where ROWNUM <= ?)where rnum  > ?";
	public static final String GET_PURCHASE ="select purchaserecordid,to_char(purchasedate,'DD/MM/YYYY')\"purchasedate\",purchasecustomerid,name as purchasecustomername,itemname as purchaseitemname,purchaseitemid,purchasepieces,purchasecost,purchaseremarks,quantity as purchaseitemquantity,type as purchaseitemtype,stock as purchasestock,purchasetaxrate,purchasetax,invoiceid from purchase,customer,item,item_type,item_quantity where purchaserecordid=? and purchasecustomerid=customerid and purchaseitemid=itemid and item.quantityid = item_quantity.quantityid and item.typeid = item_type.typeid and purchase.activeflag=?";
	public static final String ADD_PURCHASE = "insert into purchase(purchaserecordid,purchasedate,purchasecustomerid,purchaseitemid,purchasepieces,purchasecost,purchaseremarks,purchasetax,purchasetaxrate,invoiceid,activeflag) values(purchaserecordid_sequence.nextVal,?,?,?,?,?,?,?,?,?,?)";
	public static final String UPDATE_PURCHASE = "update purchase set purchasedate=?,purchasecustomerid=?,purchaseitemid=?,purchasepieces=?,purchasecost=?,purchaseremarks=?,purchasetax=?,purchasetaxrate=?,invoiceid=? where purchaserecordid=?";
	public static final String SOFT_DELETE_PURCHASE = "update purchase set activeflag=? where purchaserecordid=?";
	public static final String LIST_PURCHASE_BY_INVOICE_ID = "select purchaserecordid,to_char(purchasedate,'DD/MM/YYYY')\"purchasedate\",purchasecustomerid,name as purchasecustomername,itemname as purchaseitemname,purchaseitemid,purchasepieces,purchasecost,purchaseremarks,quantity as purchaseitemquantity,type as purchaseitemtype,stock as purchasestock,purchasetaxrate,purchasetax,invoiceid from purchase,customer,item,item_type,item_quantity where purchasecustomerid=customerid and purchaseitemid=itemid and item.quantityid = item_quantity.quantityid and item.typeid = item_type.typeid and purchase.activeflag=? and invoiceid=?";
	
	
	/**
	 * FaultSheet
	 */
	public static final String COUNT_ACTIVE_FAULT = "select count(1) from fault,customer,item,item_type,item_quantity where fault.activeflag=? and customer.priveleged=0 and fault.activeflag=customer.activeflag and faultcustomerid=customerid and faultitemid=itemid and item.quantityid = item_quantity.quantityid and item.typeid = item_type.typeid";
	// sorted by date and id to ensure order is preserved when two records belong to same date
	public static final String LIST_ALL_FAULT = "select * from ( select a.*, ROWNUM rnum from (select faultrecordid,to_char(faultdate,'DD/MM/YYYY')\"date\",faultcustomerid,name,itemname as faultitemname,faultitemid,faultpieces,faultcost,faultremarks,quantity as faultitemquantity,type as faultitemtype from fault,customer,item,item_type,item_quantity where fault.activeflag=? and customer.priveleged=0 and fault.activeflag=customer.activeflag and faultcustomerid=customerid and faultitemid=itemid and item.quantityid = item_quantity.quantityid and item.typeid = item_type.typeid order by faultdate,faultrecordid) a where ROWNUM <= ?)where rnum  > ?";
	public static final String COUNT_ACTIVE_FAULT_INCL_PRIV = "select count(1) from fault,customer,item,item_type,item_quantity where fault.activeflag=?  and fault.activeflag=customer.activeflag and faultcustomerid=customerid and faultitemid=itemid and item.quantityid = item_quantity.quantityid and item.typeid = item_type.typeid";
	public static final String LIST_ALL_FAULT_INCL_PRIV = "select * from ( select a.*, ROWNUM rnum from (select faultrecordid,to_char(faultdate,'DD/MM/YYYY')\"date\",faultcustomerid,name,itemname as faultitemname,faultitemid,faultpieces,faultcost,faultremarks,quantity as faultitemquantity,type as faultitemtype from fault,customer,item,item_type,item_quantity where fault.activeflag=? and fault.activeflag=customer.activeflag and faultcustomerid=customerid and faultitemid=itemid and item.quantityid = item_quantity.quantityid and item.typeid = item_type.typeid order by faultdate,faultrecordid) a where ROWNUM <= ?)where rnum  > ?";
	public static final String GET_FAULT ="select faultrecordid,to_char(faultdate,'DD/MM/YYYY')\"faultdate\",faultcustomerid,name as faultcustomername,itemname as faultitemname,faultitemid,faultpieces,faultcost,faultremarks,quantity as faultitemquantity,type as faultitemtype,stock as faultstock,faulttaxrate,faulttax,invoiceid from fault,customer,item,item_type,item_quantity where faultrecordid=? and faultcustomerid=customerid and faultitemid=itemid and item.quantityid = item_quantity.quantityid and item.typeid = item_type.typeid and fault.activeflag=?";
	public static final String ADD_FAULT = "insert into fault(faultrecordid,faultdate,faultcustomerid,faultitemid,faultpieces,faultcost,faultremarks,faulttax,faulttaxrate,invoiceid,activeflag) values(faultrecordid_sequence.nextVal,?,?,?,?,?,?,?,?,?,?)";
	public static final String UPDATE_FAULT = "update fault set faultdate=?,faultcustomerid=?,faultitemid=?,faultpieces=?,faultcost=?,faultremarks=?,faulttax=?,faulttaxrate=?,invoiceid=? where faultrecordid=?";
	public static final String SOFT_DELETE_FAULT = "update fault set activeflag=? where faultrecordid=?";
	public static final String LIST_FAULT_BY_INVOICE_ID = "select faultrecordid,to_char(faultdate,'DD/MM/YYYY')\"faultdate\",faultcustomerid,name as faultcustomername,itemname as faultitemname,faultitemid,faultpieces,faultcost,faultremarks,quantity as faultitemquantity,type as faultitemtype,stock as faultstock,faulttaxrate,faulttax,invoiceid from fault,customer,item,item_type,item_quantity where faultcustomerid=customerid and faultitemid=itemid and item.quantityid = item_quantity.quantityid and item.typeid = item_type.typeid and fault.activeflag=? and invoiceid=?";
	
	/**
	 * Get current sequence
	 */
	
	public static final String GET_CUSTOMER_CURR_SEQ = "select id_sequence.currVal from dual";
	public static final String GET_ITEM_CURR_SEQ = "select itemid_sequence.currval from DUAL";
	public static final String GET_ITEM_TYPE_CURR_SEQ = "select item_type_sequence.currval from DUAL";
	public static final String GET_ITEM_QUANTITY_CURR_SEQ = "select item_quantity_sequence.currval from DUAL";
	public static final String GET_TRANSACTION_CURR_SEQ = "select transactionrecordid_sequence.currval from DUAL";
	public static final String GET_SALES_CURR_SEQ = "select salesrecordid_sequence.currval from DUAL";
	public static final String GET_PURCHASE_CURR_SEQ = "select purchaserecordid_sequence.currval from DUAL";
	public static final String GET_FAULT_CURR_SEQ = "select faultrecordid_sequence.currval from DUAL";
	public static final String GET_BANK_ACCOUNT_CURR_SEQ = "select bank_id_sequence.currval from DUAL";
	public static final String GET_TRANSPORT_CURR_SEQ = "select transport_id_sequence.currval from DUAL";
	public static final String GET_INVOICE_CURR_SEQ = "select invoice_id_sequence.currval from DUAL";
	public static final String GET_TAX_INVOICE_CURR_SEQ = "select tax_invoice_id_sequence.currval from DUAL";
	public static final String GET_INVOICE_ORDER_CURR_SEQ = "select order_id_sequence.currval from DUAL";
}
