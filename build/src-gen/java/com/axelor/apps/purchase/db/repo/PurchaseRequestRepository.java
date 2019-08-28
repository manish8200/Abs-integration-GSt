/*
 * Axelor Business Solutions
 * 
 * Copyright (C) 2019 Axelor (<http://axelor.com>).
 * 
 * This program is free software: you can redistribute it and/or  modify
 * it under the terms of the GNU Affero General Public License, version 3,
 * as published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.axelor.apps.purchase.db.repo;

import com.axelor.apps.purchase.db.PurchaseRequest;
import com.axelor.db.JpaRepository;

public class PurchaseRequestRepository extends JpaRepository<PurchaseRequest> {

	public PurchaseRequestRepository() {
		super(PurchaseRequest.class);
	}

	// STATUS
	public static final int STATUS_DRAFT = 1;
	public static final int STATUS_REQUESTED = 2;
	public static final int STATUS_ACCEPTED = 3;
	public static final int STATUS_PURCHASED = 4;
	public static final int STATUS_PARTIAL_DELIVERY = 5;
	public static final int STATUS_RECEIVED = 6;
	public static final int STATUS_REFUSED = 7;
	public static final int STATUS_CANCELED = 8;
}

