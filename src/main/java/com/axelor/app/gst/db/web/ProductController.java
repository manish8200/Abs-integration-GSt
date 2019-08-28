package com.axelor.app.gst.db.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder.In;

import com.axelor.apps.account.db.Invoice;
import com.axelor.apps.account.db.InvoiceLine;
import com.axelor.apps.base.db.Partner;
import com.axelor.apps.base.db.Product;
import com.axelor.apps.base.db.repo.PartnerRepository;
import com.axelor.apps.base.db.repo.ProductRepository;
import com.axelor.db.JpaSupport;
import com.axelor.inject.Beans;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.persist.Transactional;

public class ProductController extends JpaSupport {

	@Transactional
	public void getproductasinvoice(ActionRequest request, ActionResponse response) {
		List<InvoiceLine> inlinelist = new ArrayList<InvoiceLine>();
		List<Integer> productIds = (List<Integer>) request.getContext().get("productId");	
		
		List<Product> productlist = Beans.get(ProductRepository.class).all().filter("self.id in (?1)", productIds).fetch();
		for (Product product : productlist) {
			InvoiceLine invoiceline = new InvoiceLine();
			
			invoiceline.setProduct(product);
			invoiceline.setProductCode(product.getCode());
			invoiceline.setProductName(product.getName());
			invoiceline.setPrice(product.getSalePrice());
			inlinelist.add(invoiceline);
		}
			response.setValue("invoiceLineList", inlinelist);
	}
}
