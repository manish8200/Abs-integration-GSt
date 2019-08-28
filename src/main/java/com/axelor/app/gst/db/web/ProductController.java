package com.axelor.app.gst.db.web;

import com.axelor.apps.account.db.AccountManagement;
import com.axelor.apps.account.db.InvoiceLine;
import com.axelor.apps.account.db.Tax;
import com.axelor.apps.account.db.TaxLine;
import com.axelor.apps.base.db.Product;
import com.axelor.apps.base.db.repo.ProductRepository;
import com.axelor.db.JpaSupport;
import com.axelor.inject.Beans;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.persist.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ProductController extends JpaSupport {

  @Transactional
  public void getproductasinvoice(ActionRequest request, ActionResponse response) {
    List<InvoiceLine> inlinelist = new ArrayList<InvoiceLine>();
    List<Integer> productIds = (List<Integer>) request.getContext().get("productId");
    List<AccountManagement> tax = null;
    List<Product> productlist =
        Beans.get(ProductRepository.class).all().filter("self.id in (?1)", productIds).fetch();
    for (Product product : productlist) {
      InvoiceLine invoiceline = new InvoiceLine();
     
    List<AccountManagement> account =  product.getAccountManagementList();
      for (AccountManagement accountManagement : account) {
	tax =	accountManagement.getSaleTax().getAccountManagementList();
	}
      
      invoiceline.setProduct(product);
      invoiceline.setProductCode(product.getCode());
      invoiceline.setProductName(product.getName());
      invoiceline.setPrice(product.getSalePrice());
      inlinelist.add(invoiceline);
    }
    response.setValue("invoiceLineList", inlinelist);
  }
}
