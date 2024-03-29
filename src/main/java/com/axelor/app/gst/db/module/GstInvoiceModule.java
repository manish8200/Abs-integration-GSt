package com.axelor.app.gst.db.module;

import com.axelor.app.AxelorModule;
import com.axelor.app.gst.service.GStInvoiceServiceSupplyimpl;
import com.axelor.app.gst.service.Productservice;
import com.axelor.app.gst.service.Productserviceimpl;
import com.axelor.apps.supplychain.service.InvoiceLineSupplychainService;

public class GstInvoiceModule extends AxelorModule {

  @Override
  protected void configure() {
    // TODO Auto-generated method stub
    bind(InvoiceLineSupplychainService.class).to(GStInvoiceServiceSupplyimpl.class);
    bind(Productservice.class).to(Productserviceimpl.class);
  }
}
