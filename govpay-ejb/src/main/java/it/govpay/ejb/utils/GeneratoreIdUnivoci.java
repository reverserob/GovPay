/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
 * Copyright (c) 2014-2015 TAS S.p.A. (http://www.tasgroup.it).
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
package it.govpay.ejb.utils;


public class GeneratoreIdUnivoci {
	
	private long lastNumber;
	private static GeneratoreIdUnivoci instance;

	private GeneratoreIdUnivoci() {
		lastNumber = 0;
	}

	public static GeneratoreIdUnivoci getInstance() {
		if (instance == null)
			instance = new GeneratoreIdUnivoci();
		return instance;
	}

	public synchronized String generaId() {
		String mac = "GOV"; // default

		if (lastNumber == 1000000)
			lastNumber = 0; 
		else
			lastNumber++;
		
		String ms = new Long(System.currentTimeMillis()).toString();
		String inc = this.fillNumerico(Long.toString(lastNumber, 32).toUpperCase(), 4);
		return ms + inc + mac;
	}

	private String fillNumerico(String num, int n) {
		String x = num;
		for (int i = x.length(); i < n; i++)
			x = "0" + x;
		return x;
	}
	
	public static void main(String[] args) {

		for(int i = 0; i < 1000000; i++)
		System.out.println("ID: " + GeneratoreIdUnivoci.getInstance().generaId());
	}
}