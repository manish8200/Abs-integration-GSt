package com.axelor.app.gst.service;

import java.math.BigDecimal;
import java.util.Map;

import com.axelor.apps.account.db.Invoice;
import com.axelor.apps.account.db.InvoiceLine;
import com.axelor.apps.account.db.Tax;
import com.axelor.apps.account.db.TaxLine;
import com.axelor.apps.account.db.repo.TaxRepository;
import com.axelor.apps.account.service.AccountManagementAccountService;
import com.axelor.apps.account.service.AnalyticMoveLineService;
import com.axelor.apps.account.service.app.AppAccountService;
import com.axelor.apps.account.service.invoice.InvoiceToolService;
import com.axelor.apps.base.service.CurrencyService;
import com.axelor.apps.base.service.PriceListService;
import com.axelor.apps.gst.db.State;
import com.axelor.apps.purchase.service.PurchaseProductService;
import com.axelor.apps.supplychain.service.InvoiceLineSupplychainService;
import com.axelor.exception.AxelorException;
import com.axelor.inject.Beans;
import com.google.inject.Inject;

public class GStInvoiceServiceSupplyimpl extends InvoiceLineSupplychainService {

  @Inject
  public GStInvoiceServiceSupplyimpl(
      CurrencyService currencyService,
      PriceListService priceListService,
      AppAccountService appAccountService,
      AnalyticMoveLineService analyticMoveLineService,
      AccountManagementAccountService accountManagementAccountService,
      PurchaseProductService purchaseProductService) {
    super(
        currencyService,
        priceListService,
        appAccountService,
        analyticMoveLineService,
        accountManagementAccountService,
        purchaseProductService);
  }

  @Override
  public Map<String, Object> fillProductInformation(Invoice invoice, InvoiceLine invoiceLine)
      throws AxelorException {
    Map<String, Object> productInformation = super.fillProductInformation(invoice, invoiceLine);

    Tax tax = Beans.get(TaxRepository.class).all().filter("self.code = 'GST' ").fetchOne();
    TaxLine Activetaxline = tax.getActiveTaxLine();
    Activetaxline.setValue(invoiceLine.getProduct().getGstRate());
    invoiceLine.setTaxLine(Activetaxline);

    boolean isPurchase = InvoiceToolService.isPurchase(invoice);
    TaxLine taxline = null;
    BigDecimal productprice = BigDecimal.ZERO;
    if (invoiceLine.getPrice().compareTo(BigDecimal.ZERO) == 0) {
      productprice = this.getExTaxUnitPrice(invoice, invoiceLine, taxline, isPurchase);
    } else {
      productprice = invoiceLine.getPrice();
    }
    productInformation.put("price", productprice);
    State companyaddress = invoice.getCompany().getAddress().getStates();
    State partneraddress = invoice.getAddress().getStates();
    BigDecimal qty = invoiceLine.getQty();
    BigDecimal GstRate = invoiceLine.getTaxLine().getValue();
    BigDecimal netamount = qty.multiply(productprice);
    BigDecimal divisiblegst = GstRate.divide(new BigDecimal(2));
    BigDecimal Igst = GstRate.multiply(netamount);
    BigDecimal Cgst = divisiblegst.multiply(netamount);

    if (companyaddress != partneraddress) {

      productInformation.put("iGst", Igst);
      productInformation.put("grossAmount", netamount.add(Igst));

    } else {
      productInformation.put("cGst", Cgst);
      productInformation.put("sGSt", Cgst);
      productInformation.put("grossAmount", netamount.add(Cgst));
    }
    return productInformation;
  }

}
