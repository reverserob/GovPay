/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2016 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package it.govpay.bd.anagrafica.cache;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.PortaliBD;

import org.apache.log4j.Logger;
import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.cache.AbstractCacheWrapper;

public class PortaliBDCacheWrapper extends AbstractCacheWrapper {

	public PortaliBDCacheWrapper(boolean initializeCache, Logger log) throws UtilsException {
		super("portali", initializeCache, log);
	}

	@Override
	public Object getDriver(Object basicBD) throws UtilsException {
		return new PortaliBD((BasicBD) basicBD);
	}

	@Override
	public boolean isCachableException(Throwable trowable) {
		return (trowable instanceof NotFoundException || trowable instanceof MultipleResultException);
	}

}
