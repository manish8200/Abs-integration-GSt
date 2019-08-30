package com.axelor.app.gst.service;

import com.axelor.apps.account.db.Invoice;
import com.axelor.apps.account.db.InvoiceLine;
import com.axelor.apps.account.db.InvoiceLineTax;
import com.axelor.apps.account.db.Tax;
import com.axelor.apps.account.db.TaxLine;
import com.axelor.apps.account.db.repo.TaxRepository;
import com.axelor.apps.base.db.Address;
import com.axelor.apps.base.db.Product;
import com.axelor.apps.base.db.repo.ProductRepository;
import com.axelor.apps.gst.db.State;
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
     /* Igst = Igst.add(Activetaxline.getValue().multiply(netamount));
      invoiceline.setiGst(Igst);
      Cgst = Cgst.add((Activetaxline.getValue().divide(new BigDecimal(2)).multiply(netamount)));
      invoiceline.setcGst(Cgst);
      grossamount = grossamount.add(invoiceline.getExTaxTotal().add(Igst));
      invoiceline.setGrossAmount(grossamount);
*/
      inlinelist.add(invoiceline);
    }

    return inlinelist;
  }

@Override
public List<InvoiceLineTax> setinvoicelineastaxline(Invoice invoice) {
	// TODO Auto-generated method stub
	List<InvoiceLineTax> invoiceLineTaxs = new ArrayList<InvoiceLineTax>();
	
	List<InvoiceLine> invoiceLine = invoice.getInvoiceLineList();
	for (InvoiceLine invoiceLine2 : invoiceLine) {
		InvoiceLineTax invoiceLineTax = new InvoiceLineTax();
		invoiceLineTax.setTaxLine(invoiceLine2.getTaxLine());
		invoiceLineTax.setExTaxBase(invoiceLine2.getExTaxTotal());
		invoiceLineTax.setTaxTotal(invoiceLine2.getiGst());
		invoiceLineTax.setInTaxTotal(invoiceLine2.getGrossAmount());
		invoiceLineTaxs.add(invoiceLineTax);
	}
	
	
	return invoiceLineTaxs;
}

@Override
public Invoice setinlinecalculation(Invoice invoice) {

	State CompanyState = invoice.getCompany().getAddress().getStates();
	State PartnerAddress = invoice.getAddress().getStates();
	int cgstdivisible = 2;
	List<InvoiceLine> inline = new ArrayList<>();
	//List<InvoiceLine> invoiceline = invoice.getInvoiceLineList();
	for (InvoiceLine invoiceLine2 : invoice.getInvoiceLineList()) {
		if(CompanyState != PartnerAddress) {
			invoiceLine2.setiGst(invoiceLine2.getTaxLine().getValue().multiply(invoiceLine2.getExTaxTotal()));
			invoiceLine2.setGrossAmount(invoiceLine2.getiGst().add(invoiceLine2.getExTaxTotal()));
		}else {
			invoiceLine2.setcGst(invoiceLine2.getTaxLine().getValue().divide(new BigDecimal(cgstdivisible)).multiply(invoiceLine2.getExTaxTotal()));
			invoiceLine2.setGrossAmount(invoiceLine2.getExTaxTotal().add(invoiceLine2.getcGst()));
		}
	
		inline.add(invoiceLine2);
	}
	invoice.setInvoiceLineList(inline);
	
	
	
	
	
	return invoice;

}
}
