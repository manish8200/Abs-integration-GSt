package com.axelor.app.gst.db.web;

import java.util.List;

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

public class ProductController extends JpaSupport {

  /* @Inject private AccountManagementServiceImpl service; */
  @Inject Productservice productService;

  @Transactional
  public void getproductasinvoice(ActionRequest request, ActionResponse response)
      throws AxelorException {
    Invoice invoice = request.getContext().asType(Invoice.class);
    List<Integer> productIds = (List<Integer>) request.getContext().get("productId");
    List<InvoiceLine> inline = productService.setproductasinvoice(invoice, productIds);
    System.err.println(inline);
    response.setValue("invoiceLineList", inline);
  }

  public void setinvoiceLineastaxline(ActionRequest request, ActionResponse response)
      throws AxelorException {
    Invoice invoice = request.getContext().asType(Invoice.class);

   List<InvoiceLine> invoicelinelist = productService.setinlinecalculation(invoice);
    List<InvoiceLineTax> inlinetax = productService.setinvoicelineastaxline(invoice);
    Invoice invoice2 = productService.setInvoiceGst(invoice);
    
    response.setValues(invoice2);
    response.setValue("invoiceLineTaxList", inlinetax);
    response.setValue("invoiceLineList", invoicelinelist);
  }
}
