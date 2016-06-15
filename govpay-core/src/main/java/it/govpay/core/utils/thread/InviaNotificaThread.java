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
package it.govpay.core.utils.thread;

import it.govpay.bd.BasicBD;
import it.govpay.bd.pagamento.NotificheBD;
import it.govpay.bd.model.Notifica;
import it.govpay.bd.model.Notifica.StatoSpedizione;
import it.govpay.bd.model.Notifica.TipoNotifica;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.utils.client.BasicClient.ClientException;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.core.utils.client.NotificaClient;

import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.logger.beans.Property;

public class InviaNotificaThread implements Runnable {

	private static Logger log = LogManager.getLogger();
	private Notifica notifica;
	private boolean completed = false;

	public InviaNotificaThread(Notifica notifica, BasicBD bd) throws ServiceException {
		// Verifico che tutti i campi siano valorizzati
		this.notifica = notifica;
		this.notifica.getApplicazione(bd);
		if(this.notifica.getIdRpt() != null) {
			this.notifica.getRpt(bd).getVersamento(bd);
			this.notifica.getRpt(bd).getCanale(bd);
			this.notifica.getRpt(bd).getPsp(bd);
			this.notifica.getRpt(bd).getPagamenti(bd);
		} else {
			this.notifica.getRr(bd);
			this.notifica.getRr(bd).getRpt(bd);
			this.notifica.getRr(bd).getRpt(bd).getVersamento(bd);
			this.notifica.getRr(bd).getRpt(bd).getCanale(bd);
			this.notifica.getRr(bd).getRpt(bd).getPsp(bd);
			this.notifica.getRr(bd).getRpt(bd).getPagamenti(bd);
		}
	}

	@Override
	public void run() {
		
		GpContext ctx = null;
		BasicBD bd = null;
		try {
			if(notifica.getIdRpt() != null) {
				if(notifica.getTipo().equals(TipoNotifica.ATTIVAZIONE)) {
					ctx = new GpContext(notifica.getRpt(bd).getIdTransazioneRpt());
				} else {
					ctx = new GpContext(notifica.getRpt(bd).getIdTransazioneRt());
				}
				ctx.getContext().getRequest().addGenericProperty(new Property("codDominio", notifica.getRpt(null).getCodDominio()));
				ctx.getContext().getRequest().addGenericProperty(new Property("iuv", notifica.getRpt(null).getIuv()));
				ctx.getContext().getRequest().addGenericProperty(new Property("ccp", notifica.getRpt(null).getCcp()));
				if(notifica.getTipo().equals(TipoNotifica.ATTIVAZIONE)) 
					ctx.log("notifica.rpt");
				else
					ctx.log("notifica.rt");
			} else {
				if(notifica.getTipo().equals(TipoNotifica.ATTIVAZIONE)) {
					ctx = new GpContext(notifica.getRr(bd).getIdTransazioneRr());
				} else {
					ctx = new GpContext(notifica.getRr(bd).getIdTransazioneEr());
				}
				ctx.getContext().getRequest().addGenericProperty(new Property("codDominio", notifica.getRr(null).getCodDominio()));
				ctx.getContext().getRequest().addGenericProperty(new Property("iuv", notifica.getRr(null).getIuv()));
				ctx.getContext().getRequest().addGenericProperty(new Property("ccp", notifica.getRr(null).getCcp()));
				if(notifica.getTipo().equals(TipoNotifica.ATTIVAZIONE)) 
					ctx.log("notifica.rr");
				else
					ctx.log("notifica.er");
			}
			GpThreadLocal.set(ctx);
			ctx.setupPaClient(notifica.getApplicazione(null).getCodApplicazione(), "paNotifica", notifica.getApplicazione(null).getConnettoreNotifica().getVersione());
			
			ThreadContext.put("op", ctx.getTransactionId());
		
			log.info("Spedizione della notifica [idNotifica: " + notifica.getId() +"] all'applicazione [CodApplicazione: " + notifica.getApplicazione(null).getCodApplicazione() + "]");
			if(notifica.getApplicazione(bd).getConnettoreNotifica() == null) {
				ctx.log("notifica.annullata");
				log.info("Connettore Notifica non configurato per l'applicazione [CodApplicazione: " + notifica.getApplicazione(null).getCodApplicazione() + "]. Spedizione inibita.");
				if(bd == null)
					bd = BasicBD.newInstance();
				NotificheBD notificheBD = new NotificheBD(bd);
				long tentativi = notifica.getTentativiSpedizione() + 1;
				Date prossima = new GregorianCalendar(9999,12,31).getTime();
				notificheBD.updateDaSpedire(notifica.getId(), "Connettore Notifica non configurato.", tentativi, prossima);
				return;
			}
			
			NotificaClient client = new NotificaClient(notifica.getApplicazione(bd));
			client.invoke(notifica);
			notifica.setStato(StatoSpedizione.SPEDITO);
			notifica.setDescrizioneStato(null);
			notifica.setDataAggiornamento(new Date());
			bd = BasicBD.newInstance();
			NotificheBD notificheBD = new NotificheBD(bd);
			notificheBD.updateSpedito(notifica.getId());
			ctx.log("notifica.ok");
			log.info("Notifica consegnata con successo");
		} catch(Exception e) {
			if(e instanceof GovPayException || e instanceof ClientException)
				log.error("Errore nella consegna della notifica: " + e);
			else
				log.error("Errore nella consegna della notifica", e);
			try {
				if(bd == null)
					bd = BasicBD.newInstance();
				long tentativi = notifica.getTentativiSpedizione() + 1;
				NotificheBD notificheBD = new NotificheBD(bd);
				Date prossima = new Date(new Date().getTime() + (tentativi * tentativi * 60 * 1000));
				
				ctx.log("notifica.ko", e.getMessage(), prossima.toString());
				
				notificheBD.updateDaSpedire(notifica.getId(), e.getMessage(), tentativi, prossima);
			} catch (Exception ee) {
				// Andato male l'aggiornamento. Non importa, verra' rispedito.
			}
		} finally {
			completed = true;
			if(bd != null) bd.closeConnection(); 
			if(ctx != null) ctx.log();
		}
	}

	public boolean isCompleted() {
		return completed;
	}
}
