SET DATABASE SQL SYNTAX ORA TRUE;

CREATE TABLE "CUSTOMER" 
   (	"CUSTOMERID" NUMERIC, 
	"NAME" VARCHAR(25), 
	"ACTIVEFLAG" NUMERIC(1,0), 
	"CUSTOMERADDRESS" VARCHAR(250), 
	"CUSTOMERPHONENUMBER" NUMERIC, 
	"INITIALBALANCE" NUMERIC
   );
   
CREATE TABLE "ITEM" 
   (	"ITEMID" NUMERIC, 
	"ITEMNAME" VARCHAR(100), 
	"COST" NUMERIC, 
	"STOCK" NUMERIC, 
	"ACTIVEFLAG" NUMERIC(1,0), 
	"TYPEID" NUMERIC, 
	"QUANTITYID" NUMERIC
   );
   
CREATE TABLE "ITEM_QUANTITY" 
   (	"QUANTITYID" NUMERIC, 
	"QUANTITY" NUMERIC
   );
   
CREATE TABLE "ITEM_TYPE" 
   (	"TYPEID" NUMERIC, 
	"TYPE" VARCHAR(100)
   );
   
CREATE TABLE "PURCHASE" 
   (	"PURCHASERECORDID" NUMERIC, 
	"PURCHASEDATE" DATE, 
	"PURCHASEITEMID" NUMERIC, 
	"PURCHASEPIECES" NUMERIC, 
	"PURCHASECOST" NUMERIC, 
	"ACTIVEFLAG" NUMERIC(1,0), 
	"PURCHASECUSTOMERID" NUMERIC, 
	"PURCHASEREMARKS" VARCHAR(50)
   );
   
CREATE TABLE "FAULT" 
   (	"FAULTRECORDID" NUMERIC, 
	"FAULTDATE" DATE, 
	"FAULTITEMID" NUMERIC, 
	"FAULTPIECES" NUMERIC, 
	"FAULTCOST" NUMERIC, 
	"ACTIVEFLAG" NUMERIC(1,0), 
	"FAULTCUSTOMERID" NUMERIC, 
	"FAULTREMARKS" VARCHAR(50)
   );

CREATE TABLE "SALES" 
   (	"SALESRECORDID" NUMERIC, 
	"SALESDATE" DATE, 
	"SALESITEMID" NUMERIC, 
	"SALESPIECES" NUMERIC, 
	"SALESCOST" NUMERIC, 
	"ACTIVEFLAG" NUMERIC(1,0), 
	"SALESCUSTOMERID" NUMERIC, 
	"SALESREMARKS" VARCHAR(50)
   );
   
CREATE TABLE "TRANSACTION" 
   (	"TRANSACTIONRECORDID" NUMERIC, 
	"TRANSACTIONDATE" DATE, 
	"TRANSACTIONAMOUNT" NUMERIC, 
	"IS_INCOME" VARCHAR(1), 
	"ACTIVEFLAG" NUMERIC(1,0), 
	"TRANSACTIONCUSTOMERID" NUMERIC, 
	"TRANSACTIONREMARKS" VARCHAR(50)
   );
   
create sequence id_sequence start with 1000 increment by 1 maxvalue 999999 nocache nocycle;
create sequence itemid_sequence start with 1000 increment by 1 maxvalue 999999 nocache nocycle;
create sequence item_type_sequence start with 1000 increment by 1 maxvalue 999999 nocache nocycle;
create sequence item_quantity_sequence start with 1000 increment by 1 maxvalue 999999 nocache nocycle;
create sequence transactionrecordid_sequence start with 1000 increment by 1 maxvalue 999999 nocache nocycle;
create sequence salesrecordid_sequence start with 1000 increment by 1 maxvalue 999999 nocache nocycle;
create sequence purchaserecordid_sequence start with 1000 increment by 1 maxvalue 999999 nocache nocycle;
create sequence faultrecordid_sequence start with 1000 increment by 1 maxvalue 999999 nocache nocycle;
create sequence user_id_sequence start with 1000 increment by 1 maxvalue 999999 nocache nocycle;
create sequence user_role_sequence start with 1000 increment by 1 maxvalue 999999 nocache nocycle;