package com.curious365.ifa.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.curious365.ifa.dto.Purchase;

public class DummyTest {

	public static void main(String[] args) {
		Map<String,Long> bagsByCategory = new HashMap<String,Long>();
		List<Purchase> purchases = new ArrayList<Purchase>();
		Purchase one = new Purchase();
		one.setPurchaseItemQuantity("25");
		one.setPurchaseItemType("Non Woven");
		one.setPurchasePieces(15000);
		purchases.add(one);
		
		Purchase two = new Purchase();
		two.setPurchaseItemQuantity("25");
		two.setPurchaseItemType("Non Woven");
		two.setPurchasePieces(9520);
		purchases.add(two);
		
		Purchase three = new Purchase();
		three.setPurchaseItemQuantity("10");
		three.setPurchaseItemType("Non Woven");
		three.setPurchasePieces(5210);
		purchases.add(three);
		
		Purchase four = new Purchase();
		four.setPurchaseItemQuantity("25");
		four.setPurchaseItemType("Woven");
		four.setPurchasePieces(15000);
		purchases.add(four);
		
		
		for (Purchase purchase : purchases) {
			StringBuffer key = new StringBuffer();
			key.append(purchase.getPurchaseItemQuantity());
			key.append(purchase.getPurchaseItemType());
			long purchasepieces = 0;
			if(bagsByCategory.containsKey(key.toString())){
				purchasepieces = bagsByCategory.get(key.toString());
			}
			
			bagsByCategory.put(key.toString(), purchasepieces+purchase.getPurchasePieces());
			
		}
		
		Calendar cal = Calendar.getInstance();
		int numdays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		Map<String,Long> bagsByCatPerDay = new HashMap<String,Long>();
		for (Map.Entry<String, Long> entry : bagsByCategory.entrySet())
		{
			bagsByCatPerDay.put(entry.getKey(),entry.getValue()/numdays);
		}
		
		
		System.out.println(bagsByCategory+""+bagsByCatPerDay);
	}

}
