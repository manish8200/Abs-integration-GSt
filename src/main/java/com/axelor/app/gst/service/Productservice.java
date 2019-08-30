package com.axelor.app.gst.service;

import com.axelor.apps.account.db.Invoice;
import com.axelor.apps.account.db.InvoiceLine;
import com.axelor.apps.account.db.InvoiceLineTax;

import java.util.List;

public interface Productservice {

	/*List<InvoiceLine> setinvoiceCalculationasState(Invoice invoice);*/
  List<InvoiceLine> setproductasinvoice(Invoice invoice, List<Integer> productIds);
  List<InvoiceLineTax> setinvoicelineastaxline(Invoice invoice);
  Invoice setinlinecalculation(Invoice invoice);
}
