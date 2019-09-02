package com.axelor.app.gst.service;

import com.axelor.apps.account.db.Invoice;
import com.axelor.apps.account.db.InvoiceLine;
import com.axelor.apps.account.db.InvoiceLineTax;
import java.util.List;

public interface Productservice {

	List<InvoiceLine> setproductasinvoice(Invoice invoice, List<Integer> productIds);

	List<InvoiceLineTax> setinvoicelineastaxline(Invoice invoice);

	Invoice setInvoiceGst(Invoice invoice);

	List<InvoiceLine> setinlinecalculation(Invoice invoice);
	
	Invoice setTotal(Invoice invoice);
}
