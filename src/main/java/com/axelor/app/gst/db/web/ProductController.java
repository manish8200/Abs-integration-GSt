package com.axelor.app.gst.db.web;

import com.axelor.app.gst.service.Productservice;
import com.axelor.apps.account.db.Invoice;
import com.axelor.apps.account.db.InvoiceLine;
import com.axelor.apps.account.db.InvoiceLineTax;
import com.axelor.db.JpaSupport;
import com.axelor.exception.AxelorException;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProductController extends JpaSupport {

	/* @Inject private AccountManagementServiceImpl service; */
	@Inject
	Productservice productService;

	@Transactional
	public void getproductasinvoice(ActionRequest request, ActionResponse response) throws AxelorException {
		Invoice invoice = request.getContext().asType(Invoice.class);
		List<Integer> productIds = (List<Integer>) request.getContext().get("productId");
		List<InvoiceLine> inline = productService.setproductasinvoice(invoice, productIds);
		System.err.println(inline);
		response.setValue("invoiceLineList", inline);
	}
	
	public void setinvoiceLineastaxline(ActionRequest request, ActionResponse response) throws AxelorException{
		Invoice invoice = request.getContext().asType(Invoice.class);
		
		Invoice invoicelinelist = productService.setinlinecalculation(invoice);
		List<InvoiceLineTax> inlinetax = productService.setinvoicelineastaxline(invoice); 

	
		for (InvoiceLineTax invoiceLineTax : inlinetax) {
			invoiceLineTax.getExTaxBase();
		}
		
			response.setValue("invoiceLineTaxList", inlinetax);
			response.setValue("invoiceLineList", invoicelinelist);
		}
	
	}	
	


