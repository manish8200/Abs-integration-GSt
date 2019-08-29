package com.axelor.app.gst.db.web;

import com.axelor.app.gst.service.Productservice;
import com.axelor.apps.account.db.Invoice;
import com.axelor.apps.account.db.InvoiceLine;
import com.axelor.db.JpaSupport;
import com.axelor.exception.AxelorException;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import java.util.ArrayList;
import java.util.List;

public class ProductController extends JpaSupport {

  /* @Inject private AccountManagementServiceImpl service; */
  @Inject Productservice service;

  @Transactional
  public void getproductasinvoice(ActionRequest request, ActionResponse response)
      throws AxelorException {
    Invoice invoice = request.getContext().asType(Invoice.class);

    List<InvoiceLine> inlinelist = new ArrayList<InvoiceLine>();

    List<Integer> productIds = (List<Integer>) request.getContext().get("productId");
    List<InvoiceLine> inline = service.setproductasinvoice(invoice, productIds);

    for (InvoiceLine invoiceLine : inline) {
      invoiceLine.getTaxLine();
      invoiceLine.getcGst();
      inlinelist.add(invoiceLine);
    }
    System.err.println(inline);
    response.setValue("invoiceLineTaxListPanel", inlinelist);
    response.setValue("invoiceLineList", inline);
  }
}
