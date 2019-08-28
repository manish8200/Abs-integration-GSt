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
package com.axelor.apps.base.db;

import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.axelor.apps.gst.db.State;
import com.axelor.auth.db.AuditableModel;
import com.axelor.db.annotations.Widget;
import com.google.common.base.MoreObjects;

import java.util.HashMap;import java.util.Map;

@Entity
@Table(name = "BASE_PARTNER_ADDRESS", indexes = { @Index(columnList = "address"), @Index(columnList = "partner"), @Index(columnList = "states") })
public class PartnerAddress extends AuditableModel {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BASE_PARTNER_ADDRESS_SEQ")
	@SequenceGenerator(name = "BASE_PARTNER_ADDRESS_SEQ", sequenceName = "BASE_PARTNER_ADDRESS_SEQ", allocationSize = 1)
	private Long id;

	@Widget(title = "Invoicing address")
	private Boolean isInvoicingAddr = Boolean.FALSE;

	@Widget(title = "Delivery address")
	private Boolean isDeliveryAddr = Boolean.FALSE;

	@Widget(title = "Default address")
	private Boolean isDefaultAddr = Boolean.FALSE;

	@Widget(title = "Address")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Address address;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Partner partner;

	@Widget(title = "State")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private State states;

	@Widget(title = "Attributes")
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "json")
	private String attrs;

	public PartnerAddress() {
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getIsInvoicingAddr() {
		return isInvoicingAddr == null ? Boolean.FALSE : isInvoicingAddr;
	}

	public void setIsInvoicingAddr(Boolean isInvoicingAddr) {
		this.isInvoicingAddr = isInvoicingAddr;
	}

	public Boolean getIsDeliveryAddr() {
		return isDeliveryAddr == null ? Boolean.FALSE : isDeliveryAddr;
	}

	public void setIsDeliveryAddr(Boolean isDeliveryAddr) {
		this.isDeliveryAddr = isDeliveryAddr;
	}

	public Boolean getIsDefaultAddr() {
		return isDefaultAddr == null ? Boolean.FALSE : isDefaultAddr;
	}

	public void setIsDefaultAddr(Boolean isDefaultAddr) {
		this.isDefaultAddr = isDefaultAddr;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Partner getPartner() {
		return partner;
	}

	public void setPartner(Partner partner) {
		this.partner = partner;
	}

	public State getStates() {
		return states;
	}

	public void setStates(State states) {
		this.states = states;
	}

	public String getAttrs() {
		return attrs;
	}

	public void setAttrs(String attrs) {
		this.attrs = attrs;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (this == obj) return true;
		if (!(obj instanceof PartnerAddress)) return false;

		final PartnerAddress other = (PartnerAddress) obj;
		if (this.getId() != null || other.getId() != null) {
			return Objects.equals(this.getId(), other.getId());
		}

		return false;
	}

	@Override
	public int hashCode() {
		return 31;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
			.add("id", getId())
			.add("isInvoicingAddr", getIsInvoicingAddr())
			.add("isDeliveryAddr", getIsDeliveryAddr())
			.add("isDefaultAddr", getIsDefaultAddr())
			.omitNullValues()
			.toString();
	}
}
