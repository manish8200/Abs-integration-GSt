package com.axelor.app.gst.service;

import com.axelor.apps.account.db.Invoice;
import com.axelor.apps.account.db.InvoiceLine;
import com.axelor.apps.account.db.Tax;
import com.axelor.apps.account.db.TaxLine;
import com.axelor.apps.account.db.repo.TaxRepository;
import com.axelor.apps.base.db.Product;
import com.axelor.apps.base.db.repo.ProductRepository;
import com.axelor.inject.Beans;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Productserviceimpl implements Productservice {

  @Override
  public List<InvoiceLine> setproductasinvoice(Invoice invoice, List<Integer> productIds) {
    // TODO Auto-generated method stub

    List<InvoiceLine> inlinelist = new ArrayList<InvoiceLine>();
    Tax tax = Beans.get(TaxRepository.class).all().filter("self.code = 'GST' ").fetchOne();
    TaxLine Activetaxline = tax.getActiveTaxLine();
    BigDecimal netamount = BigDecimal.ZERO,
        Igst = BigDecimal.ZERO,
        Cgst = BigDecimal.ZERO,
        grossamount = BigDecimal.ZERO;

    List<Product> productlist =
        Beans.get(ProductRepository.class).all().filter("self.id in (?1)", productIds).fetch();
    for (Product product : productlist) {
      InvoiceLine invoiceline = new InvoiceLine();
      Activetaxline.setValue(product.getGstRate());
      invoiceline.setTaxLine(Activetaxline);
      invoiceline.setProduct(product);
      invoiceline.setQty(BigDecimal.ONE);
      invoiceline.setProductCode(product.getCode());
      invoiceline.setProductName(product.getName());
      invoiceline.setPrice(product.getSalePrice());
      netamount = netamount.add(invoiceline.getQty().multiply(invoiceline.getPrice()));
      invoiceline.setExTaxTotal(netamount);
      Igst = Igst.add(Activetaxline.getValue().multiply(netamount));
      invoiceline.setiGst(Igst);
      Cgst = Cgst.add((Activetaxline.getValue().divide(new BigDecimal(2)).multiply(netamount)));
      invoiceline.setcGst(Cgst);
      grossamount = grossamount.add(invoiceline.getExTaxTotal().add(Igst));
      invoiceline.setGrossAmount(grossamount);

      inlinelist.add(invoiceline);
    }

    return inlinelist;
  }
}
