package com.curious365.ifa.controller;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import com.curious365.ifa.common.Roles;
import com.curious365.ifa.dto.AuditedSales;
import com.curious365.ifa.dto.Invoice;
import com.curious365.ifa.dto.TaxDetails;
import com.curious365.ifa.dto.TaxInvoice;
import com.curious365.ifa.exceptions.InvoiceLimitExceeded;
import com.curious365.ifa.exceptions.NoStockInHand;
import com.curious365.ifa.service.InvoiceService;
import com.curious365.ifa.service.TaxInvoiceService;
import com.curious365.ifa.util.NumberToWord;
import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.css.CssFile;
import com.itextpdf.tool.xml.css.StyleAttrCSSResolver;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;

@Controller
public class TaxInvoiceController {
	private Log log = LogFactory.getLog(TaxInvoiceController.class);
	
	@Autowired
	private TaxInvoiceService taxInvoiceService;
	
	@Autowired
	private InvoiceService invoiceService;
	
	@Autowired
	private VelocityEngine velocityEngine;
	
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
	
	@RequestMapping(value="/exportInvMonthAsCSV",method=RequestMethod.GET)
	public void exportInvMonthAsCSV(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam("monthOfYear") String monthOfYear) throws Exception {
		/**
		 * [to-do] write seperate b2b,b2cl,b2c and hsn summary, zip it
		 */
		// write b2b,b2cl,b2c and hsn summary
		String COMMA_DELIMITER = ",";
		String NEW_LINE_SEPARATOR = "\n";

		response.setContentType("text/csv");
		
		// set headers for the response
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"",
                monthOfYear+"-gstr1.zip");
        response.setHeader(headerKey, headerValue);
        FileWriter b2bWriter = null;
        FileWriter b2clWriter = null;
        FileWriter b2csWriter = null;
        FileWriter hsnWriter = null;
        ServletOutputStream sos = null;
        ZipOutputStream zipOS = null;
		try {
			sos = response.getOutputStream();
			b2bWriter = new FileWriter(new ClassPathResource("downloads/b2b.csv").getFile());
			b2clWriter = new FileWriter(new ClassPathResource("downloads/b2cl.csv").getFile());
			b2csWriter = new FileWriter(new ClassPathResource("downloads/b2cs.csv").getFile());
			hsnWriter = new FileWriter(new ClassPathResource("downloads/hsn.csv").getFile());
			
			// b2b headers
			b2bWriter.append("GSTIN/UIN of Recipient");
			b2bWriter.append(COMMA_DELIMITER);
			b2bWriter.append("Invoice Number");
			b2bWriter.append(COMMA_DELIMITER);
			b2bWriter.append("Invoice date");
			b2bWriter.append(COMMA_DELIMITER);
			b2bWriter.append("Invoice Value");
			b2bWriter.append(COMMA_DELIMITER);
			b2bWriter.append("Place Of Supply");
			b2bWriter.append(COMMA_DELIMITER);
			b2bWriter.append("Reverse Charge");
			b2bWriter.append(COMMA_DELIMITER);
			b2bWriter.append("Invoice Type");
			b2bWriter.append(COMMA_DELIMITER);
			b2bWriter.append("E-Commerce GSTIN");
			b2bWriter.append(COMMA_DELIMITER);
			b2bWriter.append("Rate");
			b2bWriter.append(COMMA_DELIMITER);
			b2bWriter.append("Taxable Value");
			b2bWriter.append(COMMA_DELIMITER);
			b2bWriter.append("Cess Amount");
			b2bWriter.append(NEW_LINE_SEPARATOR);
			// b2cl headers
			b2clWriter.append("Invoice Number");
			b2clWriter.append(COMMA_DELIMITER);
			b2clWriter.append("Invoice date");
			b2clWriter.append(COMMA_DELIMITER);
			b2clWriter.append("Invoice Value");
			b2clWriter.append(COMMA_DELIMITER);
			b2clWriter.append("Place Of Supply");
			b2clWriter.append(COMMA_DELIMITER);
			b2clWriter.append("Rate");
			b2clWriter.append(COMMA_DELIMITER);
			b2clWriter.append("Taxable Value");
			b2clWriter.append(COMMA_DELIMITER);
			b2clWriter.append("Cess Amount");
			b2clWriter.append(COMMA_DELIMITER);
			b2clWriter.append("E-Commerce GSTIN");
			b2clWriter.append(NEW_LINE_SEPARATOR);
			// b2cs headers
			b2csWriter.append("Type");
			b2csWriter.append(COMMA_DELIMITER);
			b2csWriter.append("Place Of Supply");
			b2csWriter.append(COMMA_DELIMITER);
			b2csWriter.append("Rate");
			b2csWriter.append(COMMA_DELIMITER);
			b2csWriter.append("Taxable Value");
			b2csWriter.append(COMMA_DELIMITER);
			b2csWriter.append("Cess Amount");
			b2csWriter.append(NEW_LINE_SEPARATOR);
			// hsn headers
			hsnWriter.append("HSN");
			hsnWriter.append(COMMA_DELIMITER);
			hsnWriter.append("Description");
			hsnWriter.append(COMMA_DELIMITER);
			hsnWriter.append("UQC");
			hsnWriter.append(COMMA_DELIMITER);
			hsnWriter.append("Total Quantity");
			hsnWriter.append(COMMA_DELIMITER);
			hsnWriter.append("Total Value");
			hsnWriter.append(COMMA_DELIMITER);
			hsnWriter.append("Taxable Value");
			hsnWriter.append(COMMA_DELIMITER);
			hsnWriter.append("Integrated Tax Amount");
			hsnWriter.append(COMMA_DELIMITER);
			hsnWriter.append("Central Tax Amount");
			hsnWriter.append(COMMA_DELIMITER);
			hsnWriter.append("State/UT Tax Amount");
			hsnWriter.append(COMMA_DELIMITER);
			hsnWriter.append("Cess Amount");
			hsnWriter.append(NEW_LINE_SEPARATOR);
			
			List<TaxInvoice> taxInvoiceList = new ArrayList<TaxInvoice>();
			/**
			 * Get current user information
			 */
			Authentication authentication = SecurityContextHolder.getContext()
					.getAuthentication();
			if (authentication.getAuthorities().contains(
					new SimpleGrantedAuthority(Roles.ROLE_ADMIN.getValue()))) {
				taxInvoiceList = taxInvoiceService
						.listTaxInvoicesInclPrivilegedForMonth(monthOfYear);
			} else {
				taxInvoiceList = taxInvoiceService
						.listTaxInvoicesForMonth(monthOfYear);
			}
			Map<String, TaxDetails> hsnSummaryMap = new HashMap<String, TaxDetails>();
			for (TaxInvoice taxInvoiceAbstract : taxInvoiceList) {
				TaxInvoice taxInvoice = null;
				try{
				 taxInvoice = taxInvoiceService
						.getTaxInvoiceWtDetails(taxInvoiceAbstract
								.getTaxInvoiceId());
				}catch(Exception e){
					e.printStackTrace();
					throw e;
				}
				taxInvoice.setConsigneeName("Dharmalakshmi Traders");
				taxInvoice
						.setConsigneeAddress("24 ,vallalar koil street, Redhills,Chennai-600052");
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
				boolean interState = false;
				Map<String, TaxDetails> taxDetailsMap = new HashMap<String, TaxDetails>();
				try{
				if (taxInvoice.getConsigneeStateCode().equalsIgnoreCase(
						taxInvoice.getConsignorStateCode())) {
					interState = false;
				} else {
					interState = true;
				}
				}catch(Exception e){
				}
				int sno = 1;
				try{
				for (Object auditSales : taxInvoice.getRecords()) {
					AuditedSales auditedSales = (AuditedSales) auditSales;

					// tax summary
					if (taxDetailsMap.containsKey(auditedSales.getHsnCode())) {
						TaxDetails taxDetails = taxDetailsMap.get(auditedSales
								.getHsnCode());
						if (interState) {
							double igst = taxDetails.getIgstamount();
							double currentigst = auditedSales.getSalesTax();
							taxDetails.setIgstamount(igst + currentigst);
							totalTax = totalTax + currentigst;
							totalIGST = totalIGST + auditedSales.getSalesTax();
						} else {
							double cgst = taxDetails.getCgstamount();
							double sgst = taxDetails.getSgstamount();
							double tax = auditedSales.getSalesTax();
							totalTax = totalTax + tax;
							taxDetails.setCgstamount(cgst + (tax / 2));
							taxDetails.setSgstamount(sgst + (tax / 2));
							totalCGST = totalCGST + auditedSales.getSalesTax()
									/ 2;
							totalSGST = totalSGST + auditedSales.getSalesTax()
									/ 2;
						}
						double taxableValue = taxDetails.getTaxableValue();
						double currentTaxableValue = auditedSales
								.getSalesAmount();
						totalTaxableValue = totalTaxableValue
								+ currentTaxableValue;
						taxDetails.setTaxableValue(taxableValue
								+ currentTaxableValue);
						taxDetailsMap
								.put(auditedSales.getHsnCode(), taxDetails);
					} else {
						TaxDetails taxDetails = new TaxDetails();
						taxDetails.setHsnCode(auditedSales.getHsnCode());
						taxDetails.setTaxableValue(auditedSales
								.getSalesAmount());
						totalTaxableValue = totalTaxableValue
								+ auditedSales.getSalesAmount();
						if (interState) {
							double igst = auditedSales.getSalesTax();
							taxDetails.setIgst(auditedSales.getSalesTaxRate());
							taxDetails.setIgstamount(igst);
							totalIGST = totalIGST + auditedSales.getSalesTax();
							totalTax = totalTax + igst;
						} else {
							double sgst = auditedSales.getSalesTax() / 2;
							taxDetails
									.setSgst(auditedSales.getSalesTaxRate() / 2);
							taxDetails.setSgstamount(sgst);
							double cgst = auditedSales.getSalesTax() / 2;
							taxDetails
									.setCgst(auditedSales.getSalesTaxRate() / 2);
							taxDetails.setCgstamount(cgst);
							totalTax = totalTax + auditedSales.getSalesTax();
							totalCGST = totalCGST + auditedSales.getSalesTax()
									/ 2;
							totalSGST = totalSGST + auditedSales.getSalesTax()
									/ 2;
						}
						taxDetails.setSerialNo(sno);
						sno++;

						taxDetailsMap
								.put(auditedSales.getHsnCode(), taxDetails);
					}

					// sales summary
					invoiceQuantity = invoiceQuantity
							+ auditedSales.getSalesPieces();
					invoiceValue = invoiceValue + auditedSales.getSalesTotal();
					
					// hsn summary
					
					if (hsnSummaryMap.containsKey(auditedSales.getHsnCode())) {
						TaxDetails taxDetails = hsnSummaryMap.get(auditedSales
								.getHsnCode());
						if (interState) {
							double igst = taxDetails.getIgstamount();
							double currentigst = auditedSales.getSalesTax();
							taxDetails.setIgstamount(igst + currentigst);
						} else {
							double cgst = taxDetails.getCgstamount();
							double sgst = taxDetails.getSgstamount();
							double tax = auditedSales.getSalesTax();
							taxDetails.setCgstamount(cgst + (tax / 2));
							taxDetails.setSgstamount(sgst + (tax / 2));
						}
						double taxableValue = taxDetails.getTaxableValue();
						double currentTaxableValue = auditedSales
								.getSalesAmount();
						taxDetails.setTaxableValue(taxableValue
								+ currentTaxableValue);
						
						double totalValue = taxDetails.getTotalValue();
						double currenttotalValue = auditedSales.getSalesTotal();
						taxDetails.setTotalValue(totalValue+currenttotalValue);
						
						double quantity = taxDetails.getBagQuantity();
						double currentQuantity = auditedSales.getSalesPieces();
						taxDetails.setBagQuantity(quantity+currentQuantity);
						
						hsnSummaryMap
								.put(auditedSales.getHsnCode(), taxDetails);
					} else {
						TaxDetails taxDetails = new TaxDetails();
						taxDetails.setHsnCode(auditedSales.getHsnCode());
						taxDetails.setTaxableValue(auditedSales
								.getSalesAmount());
						if (interState) {
							double igst = auditedSales.getSalesTax();
							taxDetails.setIgst(auditedSales.getSalesTaxRate());
							taxDetails.setIgstamount(igst);
						} else {
							double sgst = auditedSales.getSalesTax() / 2;
							taxDetails
									.setSgst(auditedSales.getSalesTaxRate() / 2);
							taxDetails.setSgstamount(sgst);
							double cgst = auditedSales.getSalesTax() / 2;
							taxDetails
									.setCgst(auditedSales.getSalesTaxRate() / 2);
							taxDetails.setCgstamount(cgst);
						}
						
						taxDetails.setTotalValue(auditedSales.getSalesTotal());
						taxDetails.setBagQuantity(auditedSales.getSalesPieces());

						hsnSummaryMap
								.put(auditedSales.getHsnCode(), taxDetails);
					}

				}
				}catch(Exception e){
					throw e;
				}

				try{
				for (Map.Entry<String, TaxDetails> entry : taxDetailsMap
						.entrySet()) {
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
				taxInvoice.setInvoiceValueInWords(numberToWord
						.convertNumberToWords((int) roundedInvoiceValue));
				taxInvoice.setTotalTaxInWords(numberToWord
						.convertNumberToWords((int) totalTax));
				}catch(Exception e){
					throw e;
				}
				try{
				for (TaxDetails taxDetails : taxDetailsList) {
					if(null != taxInvoice.getConsignorTaxUniqueId()){
						// b2b content	
						b2bWriter.append(taxInvoice
								.getConsignorTaxUniqueId());
						b2bWriter.append(COMMA_DELIMITER);
						b2bWriter.append(taxInvoice.getTaxInvoiceId() + "");
						b2bWriter.append(COMMA_DELIMITER);
						b2bWriter.append(taxInvoice.getTaxInvoiceDate());
						b2bWriter.append(COMMA_DELIMITER);
						b2bWriter.append(taxInvoice.getInvoiceValue() + "");
						b2bWriter.append(COMMA_DELIMITER);
						b2bWriter.append(taxInvoice.getConsignorStateCode());
						b2bWriter.append(COMMA_DELIMITER);
						b2bWriter.append("N"); // reverse tax
						b2bWriter.append(COMMA_DELIMITER);
						b2bWriter.append("Regular"); // inv type
						b2bWriter.append(COMMA_DELIMITER);
						b2bWriter.append(""); // egstin
						b2bWriter.append(COMMA_DELIMITER);
						if (interState) {
							b2bWriter.append(taxDetails.getIgst() + "");
						} else {
							b2bWriter.append((taxDetails.getCgst() + taxDetails
									.getSgst()) + "");
						}
						b2bWriter.append(COMMA_DELIMITER);
						b2bWriter.append(taxDetails.getTaxableValue() + "");
						b2bWriter.append(COMMA_DELIMITER);
						b2bWriter.append("");// cess
						b2bWriter.append(NEW_LINE_SEPARATOR);
					}else if(interState && taxInvoice.getTotalTaxableValue()>250000){
						// b2cl content
						b2clWriter.append(taxInvoice.getTaxInvoiceId() + "");
						b2clWriter.append(COMMA_DELIMITER);
						b2clWriter.append(taxInvoice.getTaxInvoiceDate());
						b2clWriter.append(COMMA_DELIMITER);
						b2clWriter.append(taxInvoice.getInvoiceValue() + "");
						b2clWriter.append(COMMA_DELIMITER);
						b2clWriter.append(taxInvoice.getConsignorStateCode());
						b2clWriter.append(COMMA_DELIMITER);
						b2clWriter.append(taxDetails.getIgst() + "");
						b2clWriter.append(COMMA_DELIMITER);
						b2clWriter.append(taxDetails.getTaxableValue() + "");
						b2clWriter.append(COMMA_DELIMITER);
						b2clWriter.append("");// cess
						b2clWriter.append(COMMA_DELIMITER);
						b2clWriter.append("");// egstin
						b2clWriter.append(NEW_LINE_SEPARATOR);
					}else{
						//b2cs content
						b2csWriter.append("OE"); //ecommerce or not
						b2csWriter.append(COMMA_DELIMITER);
						b2csWriter.append(taxInvoice.getConsignorStateCode());
						b2csWriter.append(COMMA_DELIMITER);
						if (interState) {
							b2csWriter.append(taxDetails.getIgst() + "");
						} else {
							b2csWriter.append((taxDetails.getCgst() + taxDetails
									.getSgst()) + "");
						}
						b2csWriter.append(COMMA_DELIMITER);
						b2csWriter.append(taxDetails.getTaxableValue() + "");
						b2csWriter.append(COMMA_DELIMITER);
						b2csWriter.append("");// cess
						b2csWriter.append(COMMA_DELIMITER);
						b2csWriter.append("");// egstin
						b2csWriter.append(NEW_LINE_SEPARATOR);
					}
					
				}
				
				}catch(Exception e){
					throw e;
				}
				
			}
			b2bWriter.flush();
			b2clWriter.flush();
			b2csWriter.flush();
			
			
			// hsn summary
			for (Map.Entry<String, TaxDetails> entry : hsnSummaryMap
					.entrySet()) {
				TaxDetails hsnSummary = entry.getValue();
				hsnWriter.append(hsnSummary.getHsnCode());
				hsnWriter.append(COMMA_DELIMITER);
				hsnWriter.append("");// description not needed as hsn present
				hsnWriter.append(COMMA_DELIMITER);
				hsnWriter.append("BAG-BAGS");
				hsnWriter.append(COMMA_DELIMITER);
				hsnWriter.append(hsnSummary.getBagQuantity()+""); // total quantity
				hsnWriter.append(COMMA_DELIMITER);
				hsnWriter.append(hsnSummary.getTotalValue()+""); // total value
				hsnWriter.append(COMMA_DELIMITER);
				hsnWriter.append(hsnSummary.getTaxableValue()+""); // taxable value
				hsnWriter.append(COMMA_DELIMITER);
				hsnWriter.append(hsnSummary.getIgstamount()+""); // igst
				hsnWriter.append(COMMA_DELIMITER);
				hsnWriter.append(hsnSummary.getCgstamount()+""); // cgst
				hsnWriter.append(COMMA_DELIMITER);
				hsnWriter.append(hsnSummary.getSgstamount()+""); // sgst
				hsnWriter.append(COMMA_DELIMITER);
				hsnWriter.append(""); // cess
				hsnWriter.append(NEW_LINE_SEPARATOR);
				
			}
			hsnWriter.flush();
			
			// zip files and write to output stream
			zipOS = new ZipOutputStream(sos);
			
			writeToZipFile("downloads/b2b.csv", zipOS);
			writeToZipFile("downloads/b2cl.csv", zipOS);
			writeToZipFile("downloads/b2cs.csv", zipOS);
			writeToZipFile("downloads/hsn.csv", zipOS);
			
		} catch (Exception e) {
			e.printStackTrace();
			try {
				b2bWriter.close();
			} catch (Exception ne) {
				// ignore
			}
			try {
				b2clWriter.close();
			} catch (Exception ne) {
				// ignore
			}
			try {
				b2csWriter.close();
			} catch (Exception ne) {
				// ignore
			}
			try {
				hsnWriter.close();
			} catch (Exception ne) {
				// ignore
			}
			try {
				zipOS.close();
			} catch (Exception ne) {
				// ignore
			}
			try {
				sos.close();
			} catch (Exception ne) {
				// ignore
			}
		} finally {
			try {
				b2bWriter.close();
			} catch (Exception ne) {
				// ignore
			}
			try {
				b2clWriter.close();
			} catch (Exception ne) {
				// ignore
			}
			try {
				b2csWriter.close();
			} catch (Exception ne) {
				// ignore
			}
			try {
				hsnWriter.close();
			} catch (Exception ne) {
				// ignore
			}
			try {
				zipOS.close();
			} catch (Exception ne) {
				// ignore
			}
			try {
				sos.close();
			} catch (Exception ne) {
				// ignore
			}
		}

	}
	
	@RequestMapping(value="/exportInvMonthAsPDF",method=RequestMethod.GET)
	public void exportInvMonthAsPDF(HttpServletRequest request,
            HttpServletResponse response,
			@RequestParam("monthOfYear") String monthOfYear) throws Exception {
		Template template = velocityEngine.getTemplate("./templates/"
				+ "taxinvoiceexport.vm");
		VelocityContext velocityContext = new VelocityContext();
		Document document = new Document(PageSize.A4, 0f, 0f, 0f, 0f);

		try {
			response.setContentType("application/pdf");
			// set headers for the response
	        String headerKey = "Content-Disposition";
	        String headerValue = String.format("attachment; filename=\"%s\"",
	                monthOfYear+"-invoices.pdf");
	        response.setHeader(headerKey, headerValue);
			PdfWriter pdfWriter = PdfWriter.getInstance(document,
					response.getOutputStream());

			List<TaxInvoice> taxInvoiceList = new ArrayList<TaxInvoice>();
			/**
			 * Get current user information
			 */
			Authentication authentication = SecurityContextHolder.getContext()
					.getAuthentication();
			if (authentication.getAuthorities().contains(
					new SimpleGrantedAuthority(Roles.ROLE_ADMIN.getValue()))) {
				taxInvoiceList = taxInvoiceService
						.listTaxInvoicesInclPrivilegedForMonth(monthOfYear);
			} else {
				taxInvoiceList = taxInvoiceService
						.listTaxInvoicesForMonth(monthOfYear);
			}
			document.open();
			int i = 0;
			for (TaxInvoice taxInvoiceAbstract : taxInvoiceList) {
				if (i > 0) {
					document.newPage();
				}
				i++;
				TaxInvoice taxInvoice = taxInvoiceService
						.getTaxInvoiceWtDetails(taxInvoiceAbstract
								.getTaxInvoiceId());
				taxInvoice.setConsigneeName("Dharmalakshmi Traders");
				taxInvoice
						.setConsigneeAddress("24 ,vallalar koil street, Redhills,Chennai-600052");
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
				boolean interState = false;
				Map<String, TaxDetails> taxDetailsMap = new HashMap<String, TaxDetails>();
				if (taxInvoice.getConsigneeStateCode().equalsIgnoreCase(
						taxInvoice.getConsignorStateCode())) {
					interState = false;
				} else {
					interState = true;
				}
				int sno = 1;
				for (Object auditSales : taxInvoice.getRecords()) {
					AuditedSales auditedSales = (AuditedSales) auditSales;

					// tax summary
					if (taxDetailsMap.containsKey(auditedSales.getHsnCode())) {
						TaxDetails taxDetails = taxDetailsMap.get(auditedSales
								.getHsnCode());
						if (interState) {
							double igst = taxDetails.getIgstamount();
							double currentigst = auditedSales.getSalesTax();
							taxDetails.setIgstamount(igst + currentigst);
							totalTax = totalTax + currentigst;
							totalIGST = totalIGST + auditedSales.getSalesTax();
						} else {
							double cgst = taxDetails.getCgstamount();
							double sgst = taxDetails.getSgstamount();
							double tax = auditedSales.getSalesTax();
							totalTax = totalTax + tax;
							taxDetails.setCgstamount(cgst + (tax / 2));
							taxDetails.setSgstamount(sgst + (tax / 2));
							totalCGST = totalCGST + auditedSales.getSalesTax()
									/ 2;
							totalSGST = totalSGST + auditedSales.getSalesTax()
									/ 2;
						}
						double taxableValue = taxDetails.getTaxableValue();
						double currentTaxableValue = auditedSales
								.getSalesAmount();
						totalTaxableValue = totalTaxableValue
								+ currentTaxableValue;
						taxDetails.setTaxableValue(taxableValue
								+ currentTaxableValue);
						taxDetailsMap
								.put(auditedSales.getHsnCode(), taxDetails);
					} else {
						TaxDetails taxDetails = new TaxDetails();
						taxDetails.setHsnCode(auditedSales.getHsnCode());
						taxDetails.setTaxableValue(auditedSales
								.getSalesAmount());
						totalTaxableValue = totalTaxableValue
								+ auditedSales.getSalesAmount();
						if (interState) {
							double igst = auditedSales.getSalesTax();
							taxDetails.setIgst(auditedSales.getSalesTaxRate());
							taxDetails.setIgstamount(igst);
							totalIGST = totalIGST + auditedSales.getSalesTax();
							totalTax = totalTax + igst;
						} else {
							double sgst = auditedSales.getSalesTax() / 2;
							taxDetails
									.setSgst(auditedSales.getSalesTaxRate() / 2);
							taxDetails.setSgstamount(sgst);
							double cgst = auditedSales.getSalesTax() / 2;
							taxDetails
									.setCgst(auditedSales.getSalesTaxRate() / 2);
							taxDetails.setCgstamount(cgst);
							totalTax = totalTax + auditedSales.getSalesTax();
							totalCGST = totalCGST + auditedSales.getSalesTax()
									/ 2;
							totalSGST = totalSGST + auditedSales.getSalesTax()
									/ 2;
						}
						taxDetails.setSerialNo(sno);
						sno++;

						taxDetailsMap
								.put(auditedSales.getHsnCode(), taxDetails);
					}

					// sales summary
					invoiceQuantity = invoiceQuantity
							+ auditedSales.getSalesPieces();
					invoiceValue = invoiceValue + auditedSales.getSalesTotal();

				}

				for (Map.Entry<String, TaxDetails> entry : taxDetailsMap
						.entrySet()) {
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
				taxInvoice.setInvoiceValueInWords(numberToWord
						.convertNumberToWords((int) roundedInvoiceValue));
				taxInvoice.setTotalTaxInWords(numberToWord
						.convertNumberToWords((int) totalTax));

				velocityContext.put("taxrecords", taxDetailsList);
				velocityContext.put("taxinvoice", taxInvoice);
				velocityContext.put("records", taxInvoice.getRecords());

				StringWriter stringWriter = new StringWriter();

				template.merge(velocityContext, stringWriter);

				// XMLWorkerHelper.getInstance().parseXHtml(pdfWriter, document,
				// new
				// ByteArrayInputStream(stringWriter.toString().getBytes(StandardCharsets.UTF_8.name())));
				// CSS
				CSSResolver cssResolver = new StyleAttrCSSResolver();
				CssFile bootstrapCssFile = XMLWorkerHelper
						.getCSS(new ClassPathResource(
								"static/css/bootstrap.min.css")
								.getInputStream());
				CssFile ifasCssFile = XMLWorkerHelper
						.getCSS(new ClassPathResource(
								"static/css/ifas/ifas.css").getInputStream());
				cssResolver.addCss(bootstrapCssFile);
				cssResolver.addCss(ifasCssFile);

				// HTML
				HtmlPipelineContext htmlContext = new HtmlPipelineContext(null);
				htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());

				// Pipelines
				PdfWriterPipeline pdf = new PdfWriterPipeline(document,
						pdfWriter);
				HtmlPipeline html = new HtmlPipeline(htmlContext, pdf);
				CssResolverPipeline css = new CssResolverPipeline(cssResolver,
						html);

				// XML Worker
				XMLWorker worker = new XMLWorker(css, true);
				XMLParser p = new XMLParser(worker);
				p.parse(new ByteArrayInputStream(stringWriter.toString()
						.getBytes(StandardCharsets.UTF_8.name())));
			}

		} catch (Exception e) {
			try {
				document.close();
			} catch (Exception ne) {
				// ignore
			}
		} finally {
			try {
				document.close();
			} catch (Exception ne) {
				// ignore
			}
		}

	}
	
	@RequestMapping(value="/editTaxInvoice",method=RequestMethod.GET)
	public ModelAndView editTaxInvoiceDisplay(@RequestParam(value = "error", required = false) String error,
	@RequestParam(value = "info", required = false) String info,@RequestParam long invoiceid){
		log.debug("entering..");
		ModelAndView mav = new ModelAndView();
		TaxInvoice taxInvoice = null;
		try {
			taxInvoice = taxInvoiceService.getTaxInvoiceWtDetails(invoiceid);
		} catch (Exception e) {
		}
		mav.addObject("taxinvoice", taxInvoice);
		mav.addObject("records", taxInvoice.getRecords());
		mav.addObject("error", error);
		mav.addObject("info", info);
		mav.setViewName("edittaxinvoice");
		return mav;
	}
	
	@RequestMapping(value="/editTaxInvoice",method=RequestMethod.POST)
	public ModelAndView editTaxInvoice(@ModelAttribute Invoice invoice){
		boolean hasError= false;
		UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromPath("redirect:/showSalesSheet");
		log.debug("entering..");
		ModelAndView mav = new ModelAndView();
		// converting to db supported date format
		SimpleDateFormat formatter= 
				new SimpleDateFormat("dd/MMM/yyyy");
		DateFormat newformatter ; 
		Date newdate = null ; 
		newformatter = new SimpleDateFormat("dd/MM/yyyy");
		try {
			newdate = (Date)newformatter.parse(invoice.getInvoiceDate());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String dateString=formatter.format(newdate);
		invoice.setInvoiceDate(dateString);
		try{
			taxInvoiceService.edit(invoice);	
		}catch (NoStockInHand e) {
			hasError= true;
			urlBuilder.queryParam("error", e.getMessage());
		} catch (Exception e) {
			hasError= true;
			urlBuilder.queryParam("error", "Unable to update invoice. Please try with valid data");
		}finally{
			if(hasError){
				urlBuilder.replacePath("redirect:/editTaxInvoice");
				urlBuilder.queryParam("invoiceid", invoice.getInvoiceId());
			}else{
				urlBuilder.queryParam("info", "Invoice successfully updated");
			}
		}

		mav.setViewName(urlBuilder.build().encode().toUriString());
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
	
	/**
	 * * Add a file into Zip archive in Java. * * @param fileName * @param zos * @throws
	 * FileNotFoundException * @throws IOException
	 */
	public static void writeToZipFile(String path, ZipOutputStream zipStream) {
		FileInputStream fis = null;
		ZipEntry zipEntry = null;
		try {
			fis = new FileInputStream(new ClassPathResource(path).getFile());
			zipEntry = new ZipEntry(path);
			zipStream.putNextEntry(zipEntry);
			byte[] bytes = new byte[1024];
			int length;
			while ((length = fis.read(bytes)) >= 0) {
				zipStream.write(bytes, 0, length);
			}
		} catch (Exception e) {
			try {
				zipStream.closeEntry();
			} catch (Exception ne) {
				// ignore
			}
			try {
				fis.close();
			} catch (Exception ne) {
				// ignore
			}
		} finally {
			try {
				zipStream.closeEntry();
			} catch (Exception ne) {
				// ignore
			}
			try {
				fis.close();
			} catch (Exception ne) {
				// ignore
			}
		}
	}


}
