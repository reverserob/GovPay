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
package it.govpay.core.business;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.logger.beans.Property;

import it.gov.digitpa.schemas._2011.ws.paa.FaultBean;
import it.gov.digitpa.schemas._2011.ws.paa.NodoChiediListaPendentiRPT;
import it.gov.digitpa.schemas._2011.ws.paa.NodoChiediListaPendentiRPTRisposta;
import it.gov.digitpa.schemas._2011.ws.paa.NodoChiediStatoRPTRisposta;
import it.gov.digitpa.schemas._2011.ws.paa.TipoRPTPendente;
import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.anagrafica.DominiBD;
import it.govpay.bd.model.Anagrafica;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Canale;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Notifica;
import it.govpay.bd.model.Canale.ModelloPagamento;
import it.govpay.bd.model.Canale.TipoVersamento;
import it.govpay.bd.model.Intermediario;
import it.govpay.bd.model.Iuv.TipoIUV;
import it.govpay.bd.model.Portale;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.Notifica.TipoNotifica;
import it.govpay.bd.model.Rpt.StatoRpt;
import it.govpay.bd.model.Rr;
import it.govpay.bd.model.Rr.StatoRr;
import it.govpay.bd.model.Stazione;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.model.Versamento.StatoVersamento;
import it.govpay.bd.pagamento.IuvBD;
import it.govpay.bd.pagamento.NotificheBD;
import it.govpay.bd.pagamento.PagamentiBD;
import it.govpay.bd.pagamento.RptBD;
import it.govpay.bd.pagamento.RrBD;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.core.business.model.Risposta;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.NdpException;
import it.govpay.core.exceptions.VersamentoAnnullatoException;
import it.govpay.core.exceptions.VersamentoDuplicatoException;
import it.govpay.core.exceptions.VersamentoScadutoException;
import it.govpay.core.exceptions.VersamentoSconosciutoException;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.core.utils.IuvUtils;
import it.govpay.core.utils.RptUtils;
import it.govpay.core.utils.RrUtils;
import it.govpay.core.utils.UrlUtils;
import it.govpay.core.utils.VersamentoUtils;
import it.govpay.core.utils.client.BasicClient.ClientException;
import it.govpay.core.utils.client.NodoClient;
import it.govpay.core.utils.client.NodoClient.Azione;
import it.govpay.core.utils.thread.InviaNotificaThread;
import it.govpay.core.utils.thread.ThreadExecutorManager;
import it.govpay.servizi.commons.EsitoOperazione;
import it.govpay.servizi.commons.VersamentoKey;
import it.govpay.servizi.gpprt.GpAvviaRichiestaStorno;
import it.govpay.servizi.gpprt.GpAvviaRichiestaStornoResponse;
import it.govpay.servizi.gpprt.GpAvviaTransazionePagamento;
import it.govpay.servizi.gpprt.GpAvviaTransazionePagamentoResponse;
import it.govpay.servizi.gpprt.GpAvviaTransazionePagamentoResponse.RifTransazione;

public class Pagamento extends BasicBD {

	private static Logger log = LogManager.getLogger();
	
	public Pagamento(BasicBD bd) {
		super(bd);
	}
	
	public GpAvviaTransazionePagamentoResponse avviaTransazione(Portale portale, GpAvviaTransazionePagamento gpAvviaTransazionePagamento, Canale canale) throws GovPayException, ServiceException {

		GpContext ctx = GpThreadLocal.get();
		List<Versamento> versamenti = new ArrayList<Versamento>();
		VersamentiBD versamentiBD = new VersamentiBD(this);
		
		for(VersamentoKey versamento : gpAvviaTransazionePagamento.getVersamentoOrVersamentoRef()) {
			Versamento versamentoModel = null;
			if(versamento instanceof it.govpay.servizi.commons.Versamento) {
				ctx.log("rpt.acquisizioneVersamento", versamento.getCodApplicazione(), versamento.getCodVersamentoEnte());
				versamentoModel = VersamentoUtils.toVersamentoModel((it.govpay.servizi.commons.Versamento) versamento, this);
			} else {
				ctx.log("rpt.acquisizioneVersamentoRef", versamento.getCodApplicazione(), versamento.getCodVersamentoEnte());
				Applicazione applicazione = null;
				try {
					applicazione = AnagraficaManager.getApplicazione(this, versamento.getCodApplicazione());
				} catch (NotFoundException e) {
					throw new GovPayException(EsitoOperazione.APP_000, versamento.getCodApplicazione());
				}
				
				try {
					versamentoModel = versamentiBD.getVersamento(applicazione.getId(), versamento.getCodVersamentoEnte());
				} catch (NotFoundException e) {
					throw new GovPayException(EsitoOperazione.VER_008, applicazione.getCodApplicazione(), versamento.getCodVersamentoEnte());
				}
			}
			
			if(!versamentoModel.getApplicazione(this).isAbilitato())
				throw new GovPayException(EsitoOperazione.APP_001, versamento.getCodApplicazione());
			
			versamentoModel.setIuvProposto(versamento.getIuv());
			versamenti.add(versamentoModel);
		}
		
		Anagrafica versante = VersamentoUtils.toAnagraficaModel(gpAvviaTransazionePagamento.getVersante());
		boolean aggiornaSeEsiste = gpAvviaTransazionePagamento.isAggiornaSeEsiste() != null ? gpAvviaTransazionePagamento.isAggiornaSeEsiste() : true;
		List<Rpt> rpts = avviaTransazione(versamenti, portale, canale, gpAvviaTransazionePagamento.getIbanAddebito(), versante, gpAvviaTransazionePagamento.getAutenticazione().value(), gpAvviaTransazionePagamento.getUrlRitorno(), aggiornaSeEsiste);
		
		GpAvviaTransazionePagamentoResponse response = new GpAvviaTransazionePagamentoResponse();
		
		response.setPspSessionId(rpts.get(0).getCodSessione());
		response.setUrlRedirect(rpts.get(0).getPspRedirectURL());
		
		for(Rpt rpt : rpts) {
			RifTransazione rifTransazione = new RifTransazione();
			rifTransazione.setCcp(rpt.getCcp());
			rifTransazione.setCodApplicazione(rpt.getVersamento(this).getApplicazione(this).getCodApplicazione());
			rifTransazione.setCodDominio(rpt.getCodDominio());
			rifTransazione.setCodVersamentoEnte(rpt.getVersamento(this).getCodVersamentoEnte());
			rifTransazione.setIuv(rpt.getIuv());
			response.getRifTransazione().add(rifTransazione);
		}
		
		return response;
	}

	public List<Rpt> avviaTransazione(List<Versamento> versamenti, Portale portale, Canale canale, String ibanAddebito, Anagrafica versante, String autenticazione, String redirect, boolean aggiornaSeEsiste) throws GovPayException {
		GpContext ctx = GpThreadLocal.get();
		try {
			Date adesso = new Date();
			boolean isBollo = false;
			Stazione stazione = null;
			
			for(Versamento versamentoModel : versamenti) {
				
				ctx.log("rpt.validazioneSemantica", versamentoModel.getApplicazione(this).getCodApplicazione(), versamentoModel.getCodVersamentoEnte());
				
				if(!portale.getIdApplicazioni().contains(versamentoModel.getApplicazione(this).getId())) {
					throw new GovPayException(EsitoOperazione.PRT_003, portale.getCodPortale(), versamentoModel.getApplicazione(this).getCodApplicazione());
				}
				
				if(versamentoModel.isBolloTelematico()) isBollo = true;
				
				if(!versamentoModel.getStatoVersamento().equals(StatoVersamento.NON_ESEGUITO)) {
					throw new GovPayException(EsitoOperazione.PAG_006, versamentoModel.getApplicazione(this).getCodApplicazione(), versamentoModel.getCodVersamentoEnte(), versamentoModel.getStatoVersamento().toString());
				}
				
				if(versamentoModel.getDataScadenza() != null && versamentoModel.getDataScadenza().before(adesso) && versamentoModel.isAggiornabile()) {
					
					try {
						versamentoModel = VersamentoUtils.aggiornaVersamento(versamentoModel, this);
					} catch (VersamentoAnnullatoException e){
						throw new GovPayException(EsitoOperazione.VER_013, versamentoModel.getApplicazione(this).getCodApplicazione(), versamentoModel.getCodVersamentoEnte());
					} catch (VersamentoScadutoException e) {
						throw new GovPayException(EsitoOperazione.VER_010, versamentoModel.getApplicazione(this).getCodApplicazione(), versamentoModel.getCodVersamentoEnte());
					} catch (VersamentoDuplicatoException e) {
						throw new GovPayException(EsitoOperazione.VER_012, versamentoModel.getApplicazione(this).getCodApplicazione(), versamentoModel.getCodVersamentoEnte());
					} catch (VersamentoSconosciutoException e) {
						throw new GovPayException(EsitoOperazione.VER_011, versamentoModel.getApplicazione(this).getCodApplicazione(), versamentoModel.getCodVersamentoEnte());
					} catch (ClientException e) {
						throw new GovPayException(EsitoOperazione.VER_014, versamentoModel.getApplicazione(this).getCodApplicazione(), versamentoModel.getCodVersamentoEnte());
					}
				}
				
				if(versamentoModel.getDataScadenza() != null && versamentoModel.getDataScadenza().before(adesso) && !versamentoModel.isAggiornabile()) {
					throw new GovPayException(EsitoOperazione.PAG_007, versamentoModel.getApplicazione(this).getCodApplicazione(), versamentoModel.getCodVersamentoEnte());
				}
				
				if(stazione == null) {
					stazione = versamentoModel.getUo(this).getDominio(this).getStazione(this);
				} else {
					if(stazione.getId().compareTo(versamentoModel.getUo(this).getDominio(this).getStazione(this).getId()) != 0) {
						throw new GovPayException(EsitoOperazione.PAG_000);
					}
				}
				
				ctx.log("rpt.validazioneSemanticaOk", versamentoModel.getApplicazione(this).getCodApplicazione(), versamentoModel.getCodVersamentoEnte());
			}
			
			it.govpay.bd.model.Psp psp = AnagraficaManager.getPsp(this, canale.getIdPsp());
			
			ctx.log("rpt.validazioneSemanticaPsp", psp.getCodPsp(), canale.getCodCanale());
			
			if(isBollo && !psp.isBolloGestito()){
				throw new GovPayException(EsitoOperazione.PAG_003);
			}
			
			// Verifico che il canale sia compatibile con la richiesta
			if(!canale.isAbilitato())
				throw new GovPayException(EsitoOperazione.PSP_001, psp.getCodPsp(), canale.getCodCanale(), canale.getTipoVersamento().toString());
			
			if(versamenti.size() > 1 && !canale.getModelloPagamento().equals(ModelloPagamento.IMMEDIATO_MULTIBENEFICIARIO)) {
				throw new GovPayException(EsitoOperazione.PAG_001);
			}
			
			if(canale.getModelloPagamento().equals(ModelloPagamento.ATTIVATO_PRESSO_PSP)){
				throw new GovPayException(EsitoOperazione.PAG_002);
			}
				
			if(canale.getTipoVersamento().equals(TipoVersamento.ADDEBITO_DIRETTO) && ibanAddebito == null){
				throw new GovPayException(EsitoOperazione.PAG_004);
			} 
			
			// Se non sono in Addebito Diretto, ignoro l'iban di addebito
			if(!canale.getTipoVersamento().equals(TipoVersamento.ADDEBITO_DIRETTO)) {
				ibanAddebito = null;
			}
			
			if(canale.getTipoVersamento().equals(TipoVersamento.MYBANK) && (versamenti.size() > 1 || versamenti.get(0).getSingoliVersamenti(this).size() > 1)){
				throw new GovPayException(EsitoOperazione.PAG_005);
			}
			
			ctx.log("rpt.validazioneSemanticaPspOk", psp.getCodPsp(), canale.getCodCanale());
			
			Intermediario intermediario = AnagraficaManager.getIntermediario(this, stazione.getIdIntermediario());
			
			String codCarrello = null;
			
			if(versamenti.size() > 1) {
				codCarrello = RptUtils.buildUUID35();
			}
			IuvBD iuvBD = new IuvBD(this);
			RptBD rptBD = new RptBD(this);
			it.govpay.core.business.Versamento versamentiBusiness = new it.govpay.core.business.Versamento(this);
			this.setAutoCommit(false);
			List<Rpt> rpts = new ArrayList<Rpt>();
			for(Versamento versamento : versamenti) {
				// Aggiorno tutti i versamenti che mi sono stati passati
				
				if(versamento.getId() == null) {
					versamentiBusiness.caricaVersamento(versamento, false, aggiornaSeEsiste);
				}
				it.govpay.bd.model.Iuv iuv = null;
				String ccp = null;
				
				// Verifico se ha uno IUV suggerito ed in caso lo assegno
				if(versamento.getIuvProposto() != null) {
					Iuv iuvBusiness = new Iuv(this);
					iuv = iuvBusiness.caricaIUV(versamento.getApplicazione(this), versamento.getUo(this).getDominio(this), versamento.getIuvProposto(), TipoIUV.ISO11694, versamento.getCodVersamentoEnte());
					ccp = IuvUtils.buildCCP();
					ctx.log("iuv.assegnazioneIUVCustom", versamento.getApplicazione(this).getCodApplicazione(), versamento.getCodVersamentoEnte(), versamento.getUo(this).getDominio(this).getCodDominio(), versamento.getIuvProposto(), ccp);
				} else {
					// Verifico se ha gia' uno IUV numerico assegnato. In tal caso lo riuso se il dominio e' configurato in tal senso. 
					if(versamento.getUo(this).getDominio(this).isRiusoIuv()) {
						try {
							iuv = iuvBD.getIuv(versamento.getIdApplicazione(), versamento.getCodVersamentoEnte(), TipoIUV.NUMERICO);
							ccp = IuvUtils.buildCCP();
							ctx.log("iuv.assegnazioneIUVRiuso", versamento.getApplicazione(this).getCodApplicazione(), versamento.getCodVersamentoEnte(), versamento.getUo(this).getDominio(this).getCodDominio(), iuv.getIuv(), ccp);
						} catch (NotFoundException e) {
							iuv = iuvBD.generaIuv(versamento.getApplicazione(this), versamento.getUo(this).getDominio(this), versamento.getCodVersamentoEnte(), it.govpay.bd.model.Iuv.AUX_DIGIT, stazione.getApplicationCode(), it.govpay.bd.model.Iuv.TipoIUV.ISO11694);
							ccp = Rpt.CCP_NA;
							ctx.log("iuv.assegnazioneIUVGenerato", versamento.getApplicazione(this).getCodApplicazione(), versamento.getCodVersamentoEnte(), versamento.getUo(this).getDominio(this).getCodDominio(), iuv.getIuv(), ccp);
						}
					} else {
						iuv = iuvBD.generaIuv(versamento.getApplicazione(this), versamento.getUo(this).getDominio(this), versamento.getCodVersamentoEnte(), it.govpay.bd.model.Iuv.AUX_DIGIT, stazione.getApplicationCode(), it.govpay.bd.model.Iuv.TipoIUV.ISO11694);
						ccp = Rpt.CCP_NA;
						ctx.log("iuv.assegnazioneIUVGenerato", versamento.getApplicazione(this).getCodApplicazione(), versamento.getCodVersamentoEnte(), versamento.getUo(this).getDominio(this).getCodDominio(), iuv.getIuv(), ccp);
					}
				}
				
				if(codCarrello != null) {
					ctx.setCorrelationId(codCarrello);
				} else {
					ctx.setCorrelationId(versamento.getUo(this).getDominio(this).getCodDominio() + iuv.getIuv() + ccp);
				}
				Rpt rpt = RptUtils.buildRpt(intermediario, stazione, codCarrello, versamento, iuv, ccp, portale, psp, canale, versante, autenticazione, ibanAddebito, redirect, this);
				rptBD.insertRpt(rpt);
				rpts.add(rpt);
				ctx.log("rpt.creazioneRpt", versamento.getUo(this).getDominio(this).getCodDominio(), iuv.getIuv(), ccp, rpt.getCodMsgRichiesta());
				log.info("Inserita Rpt per il versamento ("+versamento.getCodVersamentoEnte()+") dell'applicazione (" + versamento.getApplicazione(this).getCodApplicazione() + ") con dominio (" + rpt.getCodDominio() + ") iuv (" + rpt.getIuv() + ") ccp (" + rpt.getCcp() + ")");
			}
			
			commit();
			closeConnection();
			
			// Spedisco le RPT al Nodo
			// Se ho una GovPayException, non ho sicuramente spedito nulla.
			// Se ho una ClientException non so come sia andata la consegna.
			
			String idTransaction = null;
			try {
				
				idTransaction = ctx.openTransaction();
				
				if(codCarrello != null) {
					ctx.setupNodoClient(stazione.getCodStazione(), null, Azione.nodoInviaCarrelloRPT);
					ctx.getContext().getRequest().addGenericProperty(new Property("codCarrello", codCarrello));
					ctx.log("rpt.invioCarrelloRpt");
				} else {
					ctx.setupNodoClient(stazione.getCodStazione(), rpts.get(0).getCodDominio(), Azione.nodoInviaRPT);
					ctx.getContext().getRequest().addGenericProperty(new Property("codDominio", rpts.get(0).getCodDominio()));
					ctx.getContext().getRequest().addGenericProperty(new Property("iuv", rpts.get(0).getIuv()));
					ctx.getContext().getRequest().addGenericProperty(new Property("ccp", rpts.get(0).getCcp()));
					ctx.log("rpt.invioRpt");
				}
				
				Risposta risposta = RptUtils.inviaRPT(intermediario, stazione, rpts, this);
			
				setupConnection();
				if(risposta.getEsito() == null || !risposta.getEsito().equals("OK")) {
					ctx.log("rpt.invioKo");
					// RPT rifiutata dal Nodo
					// Aggiorno lo stato e ritorno l'errore
					try {
						for(int i=0; i<rpts.size(); i++) {
							Rpt rpt = rpts.get(i);
							FaultBean fb = risposta.getFaultBean(i);
							String descrizione = null; 
							if(fb != null)
								descrizione = fb.getFaultCode() + ": " + fb.getFaultString();
							rptBD.updateRpt(rpt.getId(), StatoRpt.RPT_RIFIUTATA_NODO, descrizione, null, null);
						}
					} catch (Exception e) {
						// Se uno o piu' aggiornamenti vanno male, non importa. 
						// si risolvera' poi nella verifica pendenti
					} 
					log.error(risposta.getLog());
					throw new GovPayException(EsitoOperazione.NDP_001);
				} else {
					// RPT accettata dal Nodo
					// Aggiorno lo stato e ritorno
					if(risposta.getUrl() != null) {
						ctx.getContext().getResponse().addGenericProperty(new Property("redirectUrl", risposta.getUrl()));
						ctx.log("rpt.invioOk");
					} else {
						ctx.log("rpt.invioOkNoRedirect");
					}
					return updateStatoRpt(rpts, StatoRpt.RPT_ACCETTATA_NODO, risposta.getUrl(), null);
				}
			} catch (ClientException e) {
				// ClientException: tento una chiedi stato RPT per recuperare lo stato effettivo.
				//   - RPT non esistente: rendo un errore NDP per RPT non inviata
				//   - RPT esistente: faccio come OK
				//   - Errore nella richiesta: rendo un errore NDP per stato sconosciuto
				ctx.log("rpt.invioFail");
				NodoChiediStatoRPTRisposta risposta = null;
				String idTransaction2 = null;
				try {
					idTransaction2 = ctx.openTransaction();
					ctx.setupNodoClient(stazione.getCodStazione(), rpts.get(0).getCodDominio(), Azione.nodoChiediStatoRPT);
					risposta = RptUtils.chiediStatoRPT(intermediario, stazione, rpts.get(0), this);
				} catch (ClientException ee) {
					throw new GovPayException(EsitoOperazione.NDP_000, e, "Errore nella consegna della richiesta di pagamento al Nodo dei Pagamenti");
				} finally {
					ctx.closeTransaction(idTransaction2);
				}
				if(risposta.getEsito() == null) {
					// anche la chiedi stato e' fallita
					throw new GovPayException(EsitoOperazione.NDP_000, e, "Errore nella consegna della richiesta di pagamento al Nodo dei Pagamenti");
				} else {
					ctx.log("rpt.invioRecoveryStatoRPT", risposta.getEsito().getStato());
					StatoRpt statoRpt = StatoRpt.toEnum(risposta.getEsito().getStato());
					
					if(statoRpt.equals(StatoRpt.RT_ACCETTATA_PA) || statoRpt.equals(StatoRpt.RT_RIFIUTATA_PA)) {
						// Ho gia' trattato anche la RT. Non faccio nulla.
						throw new GovPayException(EsitoOperazione.NDP_000, e, "Richiesta di pagamento gia' gestita dal Nodo dei Pagamenti");
					}
					
					// Ho lo stato aggiornato. Aggiorno il db
					if(risposta.getEsito().getUrl() != null) {
						ctx.getContext().getResponse().addGenericProperty(new Property("redirectUrl", risposta.getEsito().getUrl()));
						ctx.log("rpt.invioOk");
					} else {
						ctx.log("rpt.invioOkNoRedirect");
					}
					return updateStatoRpt(rpts, statoRpt, risposta.getEsito().getUrl(), e);
				}
			} finally {
				ctx.closeTransaction(idTransaction);
			}
		} catch (ServiceException e) {
			rollback();
			throw new GovPayException(e);
		} catch (GovPayException e){
			rollback();
			throw e;
		} 
	}

	private List<Rpt> updateStatoRpt(List<Rpt> rpts, StatoRpt statoRpt, String url, Exception e) throws ServiceException, GovPayException {
		setupConnection();
		String sessionId = null;
		NotificheBD notificheBD = new NotificheBD(this);
		try {
			if(url != null)
				sessionId = UrlUtils.getCodSessione(url);
		} catch (Exception ee) {
			log.warn("Impossibile acquisire l'idSessione dalla URL di redirect al PSP: " + url, ee);
		}
		
		for(Rpt rpt : rpts) {
			Notifica notifica = new Notifica(rpt, TipoNotifica.ATTIVAZIONE, this);
			rpt.setPspRedirectURL(url);
			rpt.setStato(statoRpt);
			rpt.setCodSessione(sessionId);
			try {
				RptBD rptBD = new RptBD(this);
				rptBD.updateRpt(rpt.getId(), statoRpt, null, sessionId, url);
				notificheBD.insertNotifica(notifica);
				ThreadExecutorManager.getClientPoolExecutor().execute(new InviaNotificaThread(notifica, this));
			} catch (Exception ee) {
				// Se uno o piu' aggiornamenti vanno male, non importa. 
				// si risolvera' poi nella verifica pendenti
			} 
		}
		
		switch (statoRpt) {
		case RPT_ERRORE_INVIO_A_NODO:
		case RPT_ERRORE_INVIO_A_PSP:
		case RPT_RIFIUTATA_NODO:
		case RPT_RIFIUTATA_PSP:
			// Casi di rifiuto. Rendo l'errore
			throw new GovPayException(EsitoOperazione.NDP_000, e);
		default:
			log.info("RPT inviata correttamente al nodo");
			return rpts;
		}
	}

	public Rpt chiediTransazione(Portale portaleAutenticato, String codDominio, String iuv, String ccp) throws GovPayException, ServiceException {
		if(!portaleAutenticato.isAbilitato())
			throw new GovPayException(EsitoOperazione.PRT_001, portaleAutenticato.getCodPortale());
		
		RptBD rptBD = new RptBD(this);
		try {
			Rpt rpt = rptBD.getRpt(codDominio, iuv, ccp);
			if(!portaleAutenticato.getId().equals(rpt.getIdPortale())) {
				throw new GovPayException(EsitoOperazione.PRT_004);
			}
			return rpt;
		} catch (NotFoundException e) {
			throw new GovPayException(EsitoOperazione.PAG_008);
		}
	}

	public String verificaTransazioniPendenti() throws GovPayException {
		List<String> response = new ArrayList<String>();
		try {
			DominiBD dominiBD = new DominiBD(this);
			List<Dominio> domini = dominiBD.getDomini();

			for(Dominio dominio : domini) {
				
				Stazione stazione = dominio.getStazione(this);
				Intermediario intermediario = stazione.getIntermediario(this);
				
				closeConnection();

				log.trace("Recupero i pendenti [CodStazione: " + stazione.getCodStazione() + "][CodDominio: " + dominio.getCodDominio() + "]");
				// Costruisco una mappa di tutti i pagamenti pendenti sul nodo
				// La chiave di lettura e' iuv@ccp
				Map<String, String> statiRptPendenti = new HashMap<String, String>();
				
				NodoClient client = new NodoClient(intermediario);
				
				// Le pendenze per specifica durano 60 giorni.
				// Richiedo la lista degli ultimi 60 gg con range settimanale, poi tutto l'antecedente ai 60 gg per sicurezza.

				Calendar da = Calendar.getInstance();
				Calendar a = Calendar.getInstance();
				da.add(Calendar.DATE, -7);
				for(int i = 0; i<10; i++) {
					NodoChiediListaPendentiRPT richiesta = new NodoChiediListaPendentiRPT();
					richiesta.setIdentificativoDominio(dominio.getCodDominio());
					richiesta.setIdentificativoIntermediarioPA(intermediario.getCodIntermediario());
					richiesta.setIdentificativoStazioneIntermediarioPA(stazione.getCodStazione());
					richiesta.setPassword(stazione.getPassword());
					richiesta.setDimensioneLista(BigInteger.valueOf(500l));
					richiesta.setRangeA(a.getTime());
					if(i<9) {
						richiesta.setRangeDa(da.getTime());
						log.debug("Richiedo la lista delle RPT pendenti (da " + (i*7) + " a " + ((i+1)*7) + " giorni fa)");
					} else {
						log.debug("Richiedo la lista delle RPT pendenti (oltre " + (i*7) + " giorni fa)");
					}
					NodoChiediListaPendentiRPTRisposta risposta = null;
					try {
						risposta = client.nodoChiediListaPendentiRPT(richiesta, intermediario.getDenominazione());
					} catch (Exception e) {
						log.error("Errore durante la richiesta di lista pendenti", e);
						continue;
					} 

					if(risposta == null) {
						log.debug("Risposta vuota.");
						continue;
					}
					if(risposta.getFault() != null) {
						log.error("Ricevuto errore durante la richiesta di lista pendenti: " + risposta.getFault().getFaultCode() + ": " + risposta.getFault().getFaultString());
						continue;
					}
					
					if(risposta.getListaRPTPendenti() == null) {
						log.debug("Lista pendenti vuota.");
						continue;
					}

					for(TipoRPTPendente rptPendente : risposta.getListaRPTPendenti().getRptPendente()) {
						String rptKey = rptPendente.getIdentificativoUnivocoVersamento() + "@" + rptPendente.getCodiceContestoPagamento();
						statiRptPendenti.put(rptKey, rptPendente.getStato());
					}
					
					a.add(Calendar.DATE, -7);
					da.add(Calendar.DATE, -7);
				}

				log.info("Identificate sul NodoSPC " + statiRptPendenti.size() + " RPT pendenti");

				// Ho acquisito tutti gli stati pendenti. 
				// Tutte quelle in stato terminale, 
				setupConnection();

				RptBD rptBD = new RptBD(this);
				
				List<Rpt> rpts = rptBD.getRptPendenti(dominio.getCodDominio());

				log.info("Identificate su GovPay " + rpts.size() + " transazioni pendenti");
				
				// Scorro le transazioni. Se non risulta pendente sul nodo (quindi non e' pendente) la mando in aggiornamento.
				for(Rpt rpt : rpts) {
					String stato = statiRptPendenti.get(rpt.getIuv() + "@" + rpt.getCcp());
					if(stato != null) {
						log.debug("Rpt confermata pendente dal nodo [CodMsgRichiesta: " + rpt.getCodMsgRichiesta() + "]: stato " + stato);
						StatoRpt statoRpt = StatoRpt.toEnum(stato);
						if(!rpt.getStato().equals(statoRpt)) {
							response.add("[" + rpt.getCodDominio() + "][" + rpt.getIuv() + "][" + rpt.getCcp() + "]#Rpt confermata pendente dal nodo con stato " + stato.toString());
							rptBD.updateRpt(rpt.getId(), statoRpt, null, null, null);
						}
					} else {
						log.debug("Rpt non pendente sul nodo [CodMsgRichiesta: " + rpt.getCodMsgRichiesta() + "]");
						// Accedo alle entita che serviranno in seguito prima di chiudere la connessione;
						rpt.getStazione(this).getIntermediario(this);
						try {
							RptUtils.aggiornaRptDaNpD(client, rpt, this);
						} catch (NdpException e) {
							response.add("[" + rpt.getCodDominio() + "][" + rpt.getIuv() + "][" + rpt.getCcp() + "]#Errore durante l'aggiornamento: " + e.getFault().getFaultString() + ".");
							continue;
						} catch (Exception e) {
							response.add("[" + rpt.getCodDominio() + "][" + rpt.getIuv() + "][" + rpt.getCcp() + "]#Errore durante l'aggiornamento: " + e + ".");
							continue;
						}
						response.add("[" + rpt.getCodDominio() + "][" + rpt.getIuv() + "][" + rpt.getCcp() + "]#Rpt pendente aggiornata in stato " + rpt.getStato().toString());
					}
				}
			}
		} catch (Exception e) {
			log.error("Fallito aggiornamento pendenti", e);
			throw new GovPayException(EsitoOperazione.INTERNAL, e);
		}
		
		if(response.isEmpty()) {
			return "Acquisizione completata#Nessun pagamento pendente.";
		} else {
			return StringUtils.join(response,"|");
		}
		
	}

	public GpAvviaRichiestaStornoResponse avviaStorno(Portale portale, GpAvviaRichiestaStorno gpAvviaRichiestaStorno) throws ServiceException, GovPayException {
		GpContext ctx = GpThreadLocal.get();
		
		List<it.govpay.bd.model.Pagamento> pagamentiDaStornare = new ArrayList<it.govpay.bd.model.Pagamento>(); 
		Rpt rpt = null;
		try {
			RptBD rptBD = new RptBD(this);
			rpt = rptBD.getRpt(gpAvviaRichiestaStorno.getCodDominio(), gpAvviaRichiestaStorno.getIuv(), gpAvviaRichiestaStorno.getCcp());
			
			if(!rpt.getIdPortale().equals(portale.getId()))
				throw new GovPayException(EsitoOperazione.PRT_004, gpAvviaRichiestaStorno.getCodPortale());
			
			if(gpAvviaRichiestaStorno.getPagamento() == null || gpAvviaRichiestaStorno.getPagamento().isEmpty()) {
				for(it.govpay.bd.model.Pagamento pagamento : rpt.getPagamenti(this)) {
					if(pagamento.getImportoRevocato() != null) continue;
					pagamento.setCausaleRevoca(gpAvviaRichiestaStorno.getCausaleRevoca());
					pagamento.setDatiRevoca(gpAvviaRichiestaStorno.getDatiAggiuntivi());
					ctx.log("rr.stornoPagamentoRichiesto", pagamento.getIur(), pagamento.getImportoPagato().toString());
					pagamentiDaStornare.add(pagamento);
				}
			} else {
				for(it.govpay.servizi.gpprt.GpAvviaRichiestaStorno.Pagamento p : gpAvviaRichiestaStorno.getPagamento()) {
					it.govpay.bd.model.Pagamento pagamento = rpt.getPagamento(p.getIur(), this);
					if(pagamento.getImportoRevocato() != null) 
						throw new GovPayException(EsitoOperazione.PAG_009, p.getIur());
					pagamento.setCausaleRevoca(p.getCausaleRevoca());
					pagamento.setDatiRevoca(p.getDatiAggiuntivi());
					ctx.log("rr.stornoPagamentoTrovato", pagamento.getIur(), pagamento.getImportoPagato().toString());
					pagamentiDaStornare.add(pagamento);
				}
			}
		} catch (NotFoundException e) {
			throw new GovPayException(EsitoOperazione.PAG_008, gpAvviaRichiestaStorno.getCodPortale());
		}
		
		if(pagamentiDaStornare.isEmpty()) {
			throw new GovPayException(EsitoOperazione.PAG_011);
		}
		
		
		Rr rr = RrUtils.buildRr(rpt, pagamentiDaStornare, this);
		RrBD rrBD = new RrBD(this);
		
		Notifica notifica = new Notifica(rr, TipoNotifica.ATTIVAZIONE, this);
		NotificheBD notificheBD = new NotificheBD(this);
		PagamentiBD pagamentiBD = new PagamentiBD(this);
		
		ctx.log("rr.creazioneRr", rr.getCodDominio(), rr.getIuv(), rr.getCcp(), rr.getCodMsgRevoca());
		
		setAutoCommit(false);
		rrBD.insertRr(rr);
		notifica.setIdRr(rr.getId());
		notificheBD.insertNotifica(notifica);
		for(it.govpay.bd.model.Pagamento pagamento : pagamentiDaStornare) {
			pagamento.setIdRr(rr.getId());
			pagamentiBD.updatePagamento(pagamento);
		}
		commit();
		
		ThreadExecutorManager.getClientPoolExecutor().execute(new InviaNotificaThread(notifica, this));
		
		GpAvviaRichiestaStornoResponse response = new GpAvviaRichiestaStornoResponse();
		response.setCodRichiestaStorno(rr.getCodMsgRevoca());
		
		String idTransaction = null;
		try {
			
			idTransaction = ctx.openTransaction();
			ctx.setupNodoClient(rpt.getStazione(this).getCodStazione(), rr.getCodDominio(), Azione.nodoInviaRichiestaStorno);
			ctx.getContext().getRequest().addGenericProperty(new Property("codMessaggioRevoca", rr.getCodMsgRevoca()));
			ctx.log("rr.invioRr");

			Risposta risposta = RrUtils.inviaRr(rr, rpt, this);
		
			if(risposta.getEsito() == null || !risposta.getEsito().equals("OK")) {
				
				ctx.log("rr.invioRrKo");
				
				// RR rifiutata dal Nodo
				// Aggiorno lo stato e ritorno l'errore
				
				FaultBean fb = risposta.getFaultBean(0);
				String descrizione = null; 
				if(fb != null)
					descrizione = fb.getFaultCode() + ": " + fb.getFaultString();
				
				rrBD.updateRr(rr.getId(), StatoRr.RR_RIFIUTATA_NODO, descrizione);
				
				log.error(risposta.getLog());
				throw new GovPayException(EsitoOperazione.NDP_001);
			} else {
				ctx.log("rr.invioRrOk");
				// RPT accettata dal Nodo
				// Aggiorno lo stato e ritorno
				rrBD.updateRr(rr.getId(), StatoRr.RR_ACCETTATA_NODO, null);
				return response;
			}
		} catch (ClientException e) {
			ctx.log("rr.invioRrKo");
			rrBD.updateRr(rr.getId(), StatoRr.RR_ERRORE_INVIO_A_NODO, e.getMessage());
			throw new GovPayException(EsitoOperazione.NDP_000, e);
		} finally {
			ctx.closeTransaction(idTransaction);
		}
	}

	public Rr chiediStorno(Portale portaleAutenticato, String codRichiestaStorno) throws ServiceException, GovPayException {
		if(!portaleAutenticato.isAbilitato())
			throw new GovPayException(EsitoOperazione.PRT_001, portaleAutenticato.getCodPortale());
		
		RrBD rrBD = new RrBD(this);
		try {
			Rr rr = rrBD.getRr(codRichiestaStorno);
			if(!portaleAutenticato.getId().equals(rr.getRpt(this).getIdPortale())) {
				throw new GovPayException(EsitoOperazione.PRT_004);
			}
			return rr;
		} catch (NotFoundException e) {
			throw new GovPayException(EsitoOperazione.PAG_010);
		}
	}
}
