package com.axelor.app.gst.service;

import com.axelor.apps.account.db.Invoice;
import com.axelor.apps.account.db.InvoiceLine;
import com.axelor.apps.account.db.TaxLine;
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
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;

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
    // TODO Auto-generated constructor stub
  }

  @Override
  public Map<String, Object> fillProductInformation(Invoice invoice, InvoiceLine invoiceLine)
      throws AxelorException {

    Map<String, Object> productInformation = super.fillProductInformation(invoice, invoiceLine);
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

  public void setinvoiceitemIninvoice(ActionRequest request, ActionResponse response) {
    Invoice invoice = request.getContext().asType(Invoice.class);
    BigDecimal grossamount = BigDecimal.ZERO,
        igst = BigDecimal.ZERO,
        cGst = BigDecimal.ZERO,
        sgst = BigDecimal.ZERO;

    Collection<InvoiceLine> inline = invoice.getInvoiceLineList();
    for (InvoiceLine invoiceLine : inline) {
      grossamount = grossamount.add(invoiceLine.getGrossAmount());
      igst = igst.add(invoiceLine.getiGst());
      cGst = cGst.add(invoiceLine.getcGst());
      sgst = sgst.add(invoiceLine.getsGSt());
    }
    response.setValue("grossAmount", grossamount);
    response.setValue("sGst", sgst);
    response.setValue("cGst", cGst);
    response.setValue("iGst", igst);
  }
}
