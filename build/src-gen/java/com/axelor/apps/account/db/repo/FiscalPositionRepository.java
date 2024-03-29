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
package com.axelor.apps.account.db.repo;

import com.axelor.apps.account.db.FiscalPosition;
import com.axelor.db.JpaRepository;
import com.axelor.db.Query;

public class FiscalPositionRepository extends JpaRepository<FiscalPosition> {

	public FiscalPositionRepository() {
		super(FiscalPosition.class);
	}

	public FiscalPosition findByCode(String code) {
		return Query.of(FiscalPosition.class)
				.filter("self.code = :code")
				.bind("code", code)
				.fetchOne();
	}

	public FiscalPosition findByName(String name) {
		return Query.of(FiscalPosition.class)
				.filter("self.name = :name")
				.bind("name", name)
				.fetchOne();
	}

}

