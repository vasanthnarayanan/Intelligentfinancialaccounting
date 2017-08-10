package com.curious365.ifa.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.curious365.ifa.dto.AuditedSales;
import com.curious365.ifa.dto.Invoice;
import com.curious365.ifa.dto.TaxDetails;
import com.curious365.ifa.dto.TaxInvoice;
import com.curious365.ifa.exceptions.InvoiceLimitExceeded;
import com.curious365.ifa.service.InvoiceService;
import com.curious365.ifa.service.TaxInvoiceService;
import com.curious365.ifa.util.NumberToWord;

@Controller
public class TaxInvoiceController {
	private Log log = LogFactory.getLog(TaxInvoiceController.class);
	
	@Autowired
	private TaxInvoiceService taxInvoiceService;
	
	@Autowired
	private InvoiceService invoiceService;
	
	@RequestMapping(value="/listTaxInvoices",method=RequestMethod.GET)
	public ModelAndView showTaxInvoicesByMonth(){
		log.debug("entering..");
		ModelAndView mav = new ModelAndView();
		/** set default invoice month view **/
		Calendar cal = Calendar.getInstance();
		
		cal.add(Calendar.MONTH, -1);
		
		DateFormat month = new SimpleDateFormat("MMM"); // month, like JUN digits
		DateFormat year = new SimpleDateFormat("yy"); // year, with 2 digits
		
		StringBuffer sb = new StringBuffer();
		sb.append((month.format(cal.getTime())).toUpperCase());
		sb.append("-");
		sb.append(year.format(cal.getTime()));
		
		mav.addObject("monthOfYear", sb.toString());
		mav.setViewName("listinvoices");
		return mav;
	}
	
	@RequestMapping(value="/instantCreateTaxInvoice",method=RequestMethod.GET)
	public ModelAndView instantCreateTaxInvoice(@RequestParam long invoiceid) throws Exception{
		log.debug("entering..");
		ModelAndView mav = new ModelAndView();
		Invoice invoice = invoiceService.getInvoiceWtDetails(invoiceid);
		// instant create and get detailed tax invoice
		long taxInvoiceId = 0; 
		try{
			taxInvoiceId= taxInvoiceService.createInstantTaxInvoice(invoice);
		}catch(InvoiceLimitExceeded ie){
			mav.addObject("status", "Application Active");
			mav.addObject("error", "System Error");
			mav.addObject("custommessage", "Invoice Limit Exceeded!");
			mav.setViewName("error");
			return mav;
		}
		
		TaxInvoice taxInvoice = taxInvoiceService.getTaxInvoiceWtDetails(taxInvoiceId);
		taxInvoice.setConsigneeName("Dharmalakshmi Traders");
		taxInvoice.setConsigneeAddress("24 ,vallalar koil street, Redhills,Chennai-600052");
		taxInvoice.setConsigneeState("Tamil Nadu");
		taxInvoice.setConsigneeStateCode("33");
		taxInvoice.setConsigneeTaxUniqueId("33ACHPN8519Q1ZX");
		List<TaxDetails> taxDetailsList = new ArrayList<TaxDetails>();
		double totalCGST = 0;
		double totalSGST = 0;
		double totalIGST = 0;
		double totalTaxableValue = 0;
		double totalTax = 0;
		double roundOff = 0;
		long invoiceQuantity = 0;
		double invoiceValue = 0;
		boolean interState= false;
		Map<String,TaxDetails> taxDetailsMap = new HashMap<String,TaxDetails> ();
		if(taxInvoice.getConsigneeStateCode().equalsIgnoreCase(taxInvoice.getConsignorStateCode())){
			interState = false;
		}else{
			interState = true;
		}
		int sno=1;
		for (Object auditSales : taxInvoice.getRecords()) {
			AuditedSales auditedSales =(AuditedSales) auditSales;
			
			// tax summary
			if(taxDetailsMap.containsKey(auditedSales.getHsnCode())){
				TaxDetails taxDetails = taxDetailsMap.get(auditedSales.getHsnCode());
				if(interState){
					double igst = taxDetails.getIgstamount();
					double currentigst = auditedSales.getSalesTax();
					taxDetails.setIgstamount(igst+currentigst);
					totalTax = totalTax+currentigst;
					totalIGST = totalIGST +auditedSales.getSalesTax();
				}else{
					double cgst = taxDetails.getCgstamount();
					double sgst = taxDetails.getSgstamount();
					double tax = auditedSales.getSalesTax();
					totalTax = totalTax + tax;
					taxDetails.setCgstamount(cgst+(tax/2));
					taxDetails.setSgstamount(sgst+(tax/2));
					totalCGST = totalCGST +auditedSales.getSalesTax()/2;
					totalSGST = totalSGST +auditedSales.getSalesTax()/2;
				}
				double taxableValue = taxDetails.getTaxableValue();
				double currentTaxableValue = auditedSales.getSalesAmount();
				totalTaxableValue = totalTaxableValue+currentTaxableValue;
				taxDetails.setTaxableValue(taxableValue+currentTaxableValue);
				taxDetailsMap.put(auditedSales.getHsnCode(), taxDetails);
			}else{
				TaxDetails taxDetails = new TaxDetails();
				taxDetails.setHsnCode(auditedSales.getHsnCode());
				taxDetails.setTaxableValue(auditedSales.getSalesAmount());
				totalTaxableValue = totalTaxableValue+auditedSales.getSalesAmount();
				if(interState){
					double igst = auditedSales.getSalesTax();
					taxDetails.setIgst(auditedSales.getSalesTaxRate());
					taxDetails.setIgstamount(igst);
					totalIGST = totalIGST +auditedSales.getSalesTax();
					totalTax = totalTax + igst;
				}else{
					double sgst = auditedSales.getSalesTax()/2;
					taxDetails.setSgst(auditedSales.getSalesTaxRate()/2);
					taxDetails.setSgstamount(sgst);
					double cgst = auditedSales.getSalesTax()/2;
					taxDetails.setCgst(auditedSales.getSalesTaxRate()/2);
					taxDetails.setCgstamount(cgst);
					totalTax = totalTax + auditedSales.getSalesTax();
					totalCGST = totalCGST +auditedSales.getSalesTax()/2;
					totalSGST = totalSGST +auditedSales.getSalesTax()/2;
				}
				taxDetails.setSerialNo(sno);
				sno++;
				
				taxDetailsMap.put(auditedSales.getHsnCode(), taxDetails);
			}
			
			// sales summary
			invoiceQuantity = invoiceQuantity + auditedSales.getSalesPieces();
			invoiceValue = invoiceValue + auditedSales.getSalesTotal();
			
		}
		
		for (Map.Entry<String, TaxDetails> entry : taxDetailsMap.entrySet()){
			taxDetailsList.add(entry.getValue());
		}
		
		double roundedInvoiceValue = Math.round(invoiceValue);
		roundOff = roundedInvoiceValue - invoiceValue;
		
		taxInvoice.setTotalIgst(totalIGST);
		taxInvoice.setTotalCgst(totalCGST);
		taxInvoice.setTotalSgst(totalSGST);
		taxInvoice.setRoundOff(roundOff);
		taxInvoice.setInvoiceValue(roundedInvoiceValue);
		taxInvoice.setTotalQuantity(invoiceQuantity);
		
		taxInvoice.setTotalTaxableValue(totalTaxableValue);
		taxInvoice.setTotalTax(totalTax);
		
		NumberToWord numberToWord = new NumberToWord();
		taxInvoice.setInvoiceValueInWords(numberToWord.convertNumberToWords((int)roundedInvoiceValue));
		taxInvoice.setTotalTaxInWords(numberToWord.convertNumberToWords((int)totalTax));
		
		mav.addObject("taxinvoice", taxInvoice);
		mav.addObject("records", taxInvoice.getRecords());
		mav.addObject("taxrecords", taxDetailsList);
		mav.setViewName("taxinvoiceprint");
		return mav;
	}
	
	@RequestMapping(value="/printTaxInvoice",method=RequestMethod.GET)
	public ModelAndView printTaxInvoice(@RequestParam long invoiceid) throws Exception{
		log.debug("entering..");
		ModelAndView mav = new ModelAndView();
		TaxInvoice taxInvoice = taxInvoiceService.getTaxInvoiceWtDetails(invoiceid);
		taxInvoice.setConsigneeName("Dharmalakshmi Traders");
		taxInvoice.setConsigneeAddress("24 ,vallalar koil street, Redhills,Chennai-600052");
		taxInvoice.setConsigneeState("Tamil Nadu");
		taxInvoice.setConsigneeStateCode("33");
		taxInvoice.setConsigneeTaxUniqueId("33ACHPN8519Q1ZX");
		List<TaxDetails> taxDetailsList = new ArrayList<TaxDetails>();
		double totalCGST = 0;
		double totalSGST = 0;
		double totalIGST = 0;
		double totalTaxableValue = 0;
		double totalTax = 0;
		double roundOff = 0;
		long invoiceQuantity = 0;
		double invoiceValue = 0;
		boolean interState= false;
		Map<String,TaxDetails> taxDetailsMap = new HashMap<String,TaxDetails> ();
		if(taxInvoice.getConsigneeStateCode().equalsIgnoreCase(taxInvoice.getConsignorStateCode())){
			interState = false;
		}else{
			interState = true;
		}
		int sno=1;
		for (Object auditSales : taxInvoice.getRecords()) {
			AuditedSales auditedSales =(AuditedSales) auditSales;
			
			// tax summary
			if(taxDetailsMap.containsKey(auditedSales.getHsnCode())){
				TaxDetails taxDetails = taxDetailsMap.get(auditedSales.getHsnCode());
				if(interState){
					double igst = taxDetails.getIgstamount();
					double currentigst = auditedSales.getSalesTax();
					taxDetails.setIgstamount(igst+currentigst);
					totalTax = totalTax+currentigst;
					totalIGST = totalIGST +auditedSales.getSalesTax();
				}else{
					double cgst = taxDetails.getCgstamount();
					double sgst = taxDetails.getSgstamount();
					double tax = auditedSales.getSalesTax();
					totalTax = totalTax + tax;
					taxDetails.setCgstamount(cgst+(tax/2));
					taxDetails.setSgstamount(sgst+(tax/2));
					totalCGST = totalCGST +auditedSales.getSalesTax()/2;
					totalSGST = totalSGST +auditedSales.getSalesTax()/2;
				}
				double taxableValue = taxDetails.getTaxableValue();
				double currentTaxableValue = auditedSales.getSalesAmount();
				totalTaxableValue = totalTaxableValue+currentTaxableValue;
				taxDetails.setTaxableValue(taxableValue+currentTaxableValue);
				taxDetailsMap.put(auditedSales.getHsnCode(), taxDetails);
			}else{
				TaxDetails taxDetails = new TaxDetails();
				taxDetails.setHsnCode(auditedSales.getHsnCode());
				taxDetails.setTaxableValue(auditedSales.getSalesAmount());
				totalTaxableValue = totalTaxableValue+auditedSales.getSalesAmount();
				if(interState){
					double igst = auditedSales.getSalesTax();
					taxDetails.setIgst(auditedSales.getSalesTaxRate());
					taxDetails.setIgstamount(igst);
					totalIGST = totalIGST +auditedSales.getSalesTax();
					totalTax = totalTax + igst;
				}else{
					double sgst = auditedSales.getSalesTax()/2;
					taxDetails.setSgst(auditedSales.getSalesTaxRate()/2);
					taxDetails.setSgstamount(sgst);
					double cgst = auditedSales.getSalesTax()/2;
					taxDetails.setCgst(auditedSales.getSalesTaxRate()/2);
					taxDetails.setCgstamount(cgst);
					totalTax = totalTax + auditedSales.getSalesTax();
					totalCGST = totalCGST +auditedSales.getSalesTax()/2;
					totalSGST = totalSGST +auditedSales.getSalesTax()/2;
				}
				taxDetails.setSerialNo(sno);
				sno++;
				
				taxDetailsMap.put(auditedSales.getHsnCode(), taxDetails);
			}
			
			// sales summary
			invoiceQuantity = invoiceQuantity + auditedSales.getSalesPieces();
			invoiceValue = invoiceValue + auditedSales.getSalesTotal();
			
		}
		
		for (Map.Entry<String, TaxDetails> entry : taxDetailsMap.entrySet()){
			taxDetailsList.add(entry.getValue());
		}
		
		double roundedInvoiceValue = Math.round(invoiceValue);
		roundOff = roundedInvoiceValue - invoiceValue;
		
		taxInvoice.setTotalIgst(totalIGST);
		taxInvoice.setTotalCgst(totalCGST);
		taxInvoice.setTotalSgst(totalSGST);
		taxInvoice.setRoundOff(roundOff);
		taxInvoice.setInvoiceValue(roundedInvoiceValue);
		taxInvoice.setTotalQuantity(invoiceQuantity);
		
		taxInvoice.setTotalTaxableValue(totalTaxableValue);
		taxInvoice.setTotalTax(totalTax);
		
		NumberToWord numberToWord = new NumberToWord();
		taxInvoice.setInvoiceValueInWords(numberToWord.convertNumberToWords((int)roundedInvoiceValue));
		taxInvoice.setTotalTaxInWords(numberToWord.convertNumberToWords((int)totalTax));
		
		mav.addObject("taxrecords", taxDetailsList);
		mav.addObject("taxinvoice", taxInvoice);
		mav.addObject("records", taxInvoice.getRecords());
		mav.setViewName("taxinvoiceprint");
		return mav;
	}

}
