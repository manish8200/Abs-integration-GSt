package com.axelor.app.gst.service;

import com.axelor.apps.account.db.Invoice;
import com.axelor.apps.account.db.InvoiceLine;
import java.util.List;

public interface Productservice {

  List<InvoiceLine> setproductasinvoice(Invoice invoice, List<Integer> productIds);
}
