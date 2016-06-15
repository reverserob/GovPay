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
package it.govpay.web.ws;

import java.util.List;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.FrApplicazione;
import it.govpay.bd.model.Pagamento;
import it.govpay.bd.model.RendicontazioneSenzaRpt;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.Versamento;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.utils.Gp21Utils;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.core.utils.IuvUtils;
import it.govpay.core.utils.VersamentoUtils;
import it.govpay.servizi.PagamentiTelematiciGPApp;
import it.govpay.servizi.commons.EsitoOperazione;
import it.govpay.servizi.commons.FlussoRendicontazione;
import it.govpay.servizi.commons.GpResponse;
import it.govpay.servizi.commons.IuvGenerato;
import it.govpay.servizi.commons.StatoVersamento;
import it.govpay.servizi.gpapp.GpAnnullaVersamento;
import it.govpay.servizi.gpapp.GpCaricaIuv;
import it.govpay.servizi.gpapp.GpCaricaIuvResponse;
import it.govpay.servizi.gpapp.GpCaricaVersamento;
import it.govpay.servizi.gpapp.GpCaricaVersamentoResponse;
import it.govpay.servizi.gpapp.GpChiediFlussoRendicontazione;
import it.govpay.servizi.gpapp.GpChiediFlussoRendicontazioneResponse;
import it.govpay.servizi.gpapp.GpChiediListaFlussiRendicontazione;
import it.govpay.servizi.gpapp.GpChiediListaFlussiRendicontazioneResponse;
import it.govpay.servizi.gpapp.GpChiediStatoVersamento;
import it.govpay.servizi.gpapp.GpChiediStatoVersamentoResponse;
import it.govpay.servizi.gpapp.GpGeneraIuv;
import it.govpay.servizi.gpapp.GpGeneraIuvResponse;
import it.govpay.servizi.gpapp.GpNotificaPagamento;

import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.logger.beans.proxy.Actor;

@WebService(serviceName = "PagamentiTelematiciGPAppService",
endpointInterface = "it.govpay.servizi.PagamentiTelematiciGPApp",
targetNamespace = "http://www.govpay.it/servizi/",
portName = "GPAppPort",
wsdlLocation="classpath:wsdl/GpApp.wsdl")

@HandlerChain(file="../../../../handler-chains/handler-chain-gpws.xml")

@org.apache.cxf.annotations.SchemaValidation
public class PagamentiTelematiciGPAppImpl implements PagamentiTelematiciGPApp {

	@Resource
	WebServiceContext wsCtxt;

	private static Logger log = LogManager.getLogger();

	@Override
	public GpGeneraIuvResponse gpGeneraIuv(GpGeneraIuv gpGeneraIuv) {
		log.info("Richiesta operazione gpGeneraIuv di " + gpGeneraIuv.getIuvRichiesto().size() + " Iuv per (" + gpGeneraIuv.getCodApplicazione() + ")");
		GpGeneraIuvResponse response = new GpGeneraIuvResponse();
		GpContext ctx = GpThreadLocal.get();
		BasicBD bd = null;
		try {
			bd = BasicBD.newInstance();
			Applicazione applicazioneAutenticata = getApplicazioneAutenticata(bd);
			ctx.log("gpapp.ricevutaRichiesta");
			it.govpay.core.business.Iuv iuvBusiness = new it.govpay.core.business.Iuv(bd);
			response = iuvBusiness.generaIUV(applicazioneAutenticata, gpGeneraIuv);
			response.setCodEsitoOperazione(EsitoOperazione.OK);
			ctx.log("gpapp.ricevutaRichiestaOk");
		} catch (GovPayException e) {
			response.setCodEsitoOperazione(e.getCodEsito());
			response.setDescrizioneEsitoOperazione(e.getMessage());
			e.log(log);
			ctx.log("gpapp.ricevutaRichiestaKo", response.getCodEsitoOperazione().toString(), response.getDescrizioneEsitoOperazione());
		} catch (Exception e) {
			response.setCodEsitoOperazione(EsitoOperazione.INTERNAL);
			response.setDescrizioneEsitoOperazione(e.getMessage());
			new GovPayException(e).log(log);
			ctx.log("gpapp.ricevutaRichiestaKo", response.getCodEsitoOperazione().toString(), response.getDescrizioneEsitoOperazione());
		} finally {
			if(ctx != null) {
				ctx.setResult(response);
				ctx.log();
			}
			if(bd != null) bd.closeConnection();
		}
		response.setCodOperazione(ThreadContext.get("op"));
		return response;
	}
	
	@Override
	public GpCaricaIuvResponse gpCaricaIuv(GpCaricaIuv bodyrichiesta) {
		log.info("Richiesta operazione gpCaricaIuv di " + bodyrichiesta.getIuvGenerato().size() + " Iuv per (" + bodyrichiesta.getCodApplicazione() + ")");
		GpCaricaIuvResponse response = new GpCaricaIuvResponse();
		GpContext ctx = GpThreadLocal.get();
		BasicBD bd = null;
		try {
			bd = BasicBD.newInstance();
			Applicazione applicazioneAutenticata = getApplicazioneAutenticata(bd);
			ctx.log("gpapp.ricevutaRichiesta");
			it.govpay.core.business.Iuv iuvBusiness = new it.govpay.core.business.Iuv(bd);
			response = iuvBusiness.caricaIUV(applicazioneAutenticata, bodyrichiesta);
			response.setCodEsitoOperazione(EsitoOperazione.OK);
			ctx.log("gpapp.ricevutaRichiestaOk");
		} catch (GovPayException e) {
			response.setCodEsitoOperazione(e.getCodEsito());
			response.setDescrizioneEsitoOperazione(e.getMessage());
			e.log(log);
			ctx.log("gpapp.ricevutaRichiestaKo", response.getCodEsitoOperazione().toString(), response.getDescrizioneEsitoOperazione());
		} catch (Exception e) {
			response.setCodEsitoOperazione(EsitoOperazione.INTERNAL);
			response.setDescrizioneEsitoOperazione(e.getMessage());
			ctx.log("gpapp.ricevutaRichiestaKo", response.getCodEsitoOperazione().toString(), response.getDescrizioneEsitoOperazione());
			new GovPayException(e).log(log);
		} finally {
			if(ctx != null) {
				ctx.setResult(response);
				ctx.log();
			}
			if(bd != null) bd.closeConnection();
		}
		response.setCodOperazione(ThreadContext.get("op"));
		return response;
	}

	@Override
	public GpCaricaVersamentoResponse gpCaricaVersamento(GpCaricaVersamento bodyrichiesta) {
		log.info("Richiesta operazione gpCaricaVersamento per il versamento (" + bodyrichiesta.getVersamento().getCodVersamentoEnte() + ") dell'applicazione (" +  bodyrichiesta.getVersamento().getCodApplicazione()+") con generazione IUV (" + bodyrichiesta.isGeneraIuv() + ")");
		GpCaricaVersamentoResponse response = new GpCaricaVersamentoResponse();
		GpContext ctx = GpThreadLocal.get();
		BasicBD bd = null;
		try {
			bd = BasicBD.newInstance();
			Applicazione applicazioneAutenticata = getApplicazioneAutenticata(bd);
			ctx.log("gpapp.ricevutaRichiesta");
			it.govpay.core.business.Versamento versamentoBusiness = new it.govpay.core.business.Versamento(bd);

			it.govpay.servizi.commons.Versamento versamento = bodyrichiesta.getVersamento();
			it.govpay.bd.model.Versamento versamentoModel = VersamentoUtils.toVersamentoModel(versamento, bd);
			boolean aggiornaSeEsiste = true;
			if(bodyrichiesta.isAggiornaSeEsiste() != null) {
				aggiornaSeEsiste = bodyrichiesta.isAggiornaSeEsiste();
			}
			it.govpay.bd.model.Iuv iuv = versamentoBusiness.caricaVersamento(applicazioneAutenticata, versamentoModel, bodyrichiesta.isGeneraIuv(), aggiornaSeEsiste);

			if(iuv != null) {
				IuvGenerato iuvGenerato = IuvUtils.toIuvGenerato(versamentoModel.getApplicazione(bd), versamentoModel.getUo(bd).getDominio(bd), iuv, versamento.getImportoTotale());
				response.setIuvGenerato(iuvGenerato);
			}

			response.setCodEsitoOperazione(EsitoOperazione.OK);
			ctx.log("gpapp.ricevutaRichiestaOk");
		} catch (GovPayException e) {
			response.setCodEsitoOperazione(e.getCodEsito());
			response.setDescrizioneEsitoOperazione(e.getMessage());
			e.log(log);
			ctx.log("gpapp.ricevutaRichiestaKo", response.getCodEsitoOperazione().toString(), response.getDescrizioneEsitoOperazione());
		} catch (Exception e) {
			response.setCodEsitoOperazione(EsitoOperazione.INTERNAL);
			response.setDescrizioneEsitoOperazione(e.getMessage());
			ctx.log("gpapp.ricevutaRichiestaKo", response.getCodEsitoOperazione().toString(), response.getDescrizioneEsitoOperazione());
			new GovPayException(e).log(log);
		} finally {
			if(ctx != null) {
				ctx.setResult(response);
				ctx.log();
			}
			if(bd != null) bd.closeConnection();
		}
		response.setCodOperazione(ThreadContext.get("op"));
		return response;
	}

	@Override
	public GpResponse gpAnnullaVersamento(GpAnnullaVersamento bodyrichiesta) {
		log.info("Richiesta operazione gpChiediAnnullaVersamento per il versamento (" + bodyrichiesta.getCodVersamentoEnte() + ") dell'applicazione (" +  bodyrichiesta.getCodApplicazione()+")");
		GpResponse response = new GpResponse();
		GpContext ctx = GpThreadLocal.get();
		BasicBD bd = null;
		try {
			bd = BasicBD.newInstance();
			Applicazione applicazioneAutenticata = getApplicazioneAutenticata(bd);
			ctx.log("gpapp.ricevutaRichiesta");
			it.govpay.core.business.Versamento versamentoBusiness = new it.govpay.core.business.Versamento(bd);
			versamentoBusiness.annullaVersamento(applicazioneAutenticata, bodyrichiesta.getCodApplicazione(), bodyrichiesta.getCodVersamentoEnte());
			response.setCodEsitoOperazione(EsitoOperazione.OK);
			ctx.log("gpapp.ricevutaRichiestaOk");
		} catch (GovPayException e) {
			response.setCodEsitoOperazione(e.getCodEsito());
			response.setDescrizioneEsitoOperazione(e.getMessage());
			e.log(log);
			ctx.log("gpapp.ricevutaRichiestaKo", response.getCodEsitoOperazione().toString(), response.getDescrizioneEsitoOperazione());
		} catch (Exception e) {
			response.setCodEsitoOperazione(EsitoOperazione.INTERNAL);
			response.setDescrizioneEsitoOperazione(e.getMessage());
			new GovPayException(e).log(log);
			ctx.log("gpapp.ricevutaRichiestaKo", response.getCodEsitoOperazione().toString(), response.getDescrizioneEsitoOperazione());
		} finally {
			if(ctx != null) {
				ctx.setResult(response);
				ctx.log();
			}
			if(bd != null) bd.closeConnection();
		}
		response.setCodOperazione(ThreadContext.get("op"));
		return response;
	}
	
	@Override
	public GpResponse gpNotificaPagamento(GpNotificaPagamento bodyrichiesta) {
		log.info("Richiesta operazione gpNotificaPagamento per il versamento (" + bodyrichiesta.getCodVersamentoEnte() + ") dell'applicazione (" +  bodyrichiesta.getCodApplicazione()+")");
		GpResponse response = new GpResponse();
		GpContext ctx = GpThreadLocal.get();
		BasicBD bd = null;
		try {
			bd = BasicBD.newInstance();
			Applicazione applicazioneAutenticata = getApplicazioneAutenticata(bd);
			ctx.log("gpapp.ricevutaRichiesta");
			it.govpay.core.business.Versamento versamentoBusiness = new it.govpay.core.business.Versamento(bd);
			versamentoBusiness.notificaPagamento(applicazioneAutenticata, bodyrichiesta.getCodApplicazione(), bodyrichiesta.getCodVersamentoEnte());
			response.setCodEsitoOperazione(EsitoOperazione.OK);
			ctx.log("gpapp.ricevutaRichiestaOk");
		} catch (GovPayException e) {
			response.setCodEsitoOperazione(e.getCodEsito());
			response.setDescrizioneEsitoOperazione(e.getMessage());
			e.log(log);
			ctx.log("gpapp.ricevutaRichiestaKo", response.getCodEsitoOperazione().toString(), response.getDescrizioneEsitoOperazione());
		} catch (Exception e) {
			response.setCodEsitoOperazione(EsitoOperazione.INTERNAL);
			response.setDescrizioneEsitoOperazione(e.getMessage());
			new GovPayException(e).log(log);
			ctx.log("gpapp.ricevutaRichiestaKo", response.getCodEsitoOperazione().toString(), response.getDescrizioneEsitoOperazione());
		} finally {
			if(ctx != null) {
				ctx.setResult(response);
				ctx.log();
			}
			if(bd != null) bd.closeConnection();
		}
		response.setCodOperazione(ThreadContext.get("op"));
		return response;
	}

	@Override
	public GpChiediStatoVersamentoResponse gpChiediStatoVersamento(GpChiediStatoVersamento bodyrichiesta) {
		log.info("Richiesta operazione gpChiediStatoVersamento per il versamento (" + bodyrichiesta.getCodVersamentoEnte() + ") dell'applicazione (" +  bodyrichiesta.getCodApplicazione()+")");
		GpChiediStatoVersamentoResponse response = new GpChiediStatoVersamentoResponse();
		GpContext ctx = GpThreadLocal.get();
		BasicBD bd = null;
		try {
			bd = BasicBD.newInstance();
			Applicazione applicazioneAutenticata = getApplicazioneAutenticata(bd);
			ctx.log("gpapp.ricevutaRichiesta");
			it.govpay.core.business.Versamento versamentoBusiness = new it.govpay.core.business.Versamento(bd);
			Versamento versamento = versamentoBusiness.chiediVersamento(applicazioneAutenticata, bodyrichiesta.getCodApplicazione(), bodyrichiesta.getCodVersamentoEnte());
			response.setCodApplicazione(versamento.getApplicazione(bd).getCodApplicazione());
			response.setCodEsitoOperazione(EsitoOperazione.OK);
			response.setCodVersamentoEnte(versamento.getCodVersamentoEnte());
			response.setStato(StatoVersamento.valueOf(versamento.getStatoVersamento().toString()));
			List<Rpt> rpts = versamento.getRpt(bd);
			for(Rpt rpt : rpts) {
				response.getTransazione().add(Gp21Utils.toTransazione(rpt, bd));
			}
			ctx.log("gpapp.ricevutaRichiestaOk");
		} catch (GovPayException e) {
			response.setCodEsitoOperazione(e.getCodEsito());
			response.setDescrizioneEsitoOperazione(e.getMessage());
			e.log(log);
			ctx.log("gpapp.ricevutaRichiestaKo", response.getCodEsitoOperazione().toString(), response.getDescrizioneEsitoOperazione());
		} catch (Exception e) {
			response.setCodEsitoOperazione(EsitoOperazione.INTERNAL);
			response.setDescrizioneEsitoOperazione(e.getMessage());
			new GovPayException(e).log(log);
			ctx.log("gpapp.ricevutaRichiestaKo", response.getCodEsitoOperazione().toString(), response.getDescrizioneEsitoOperazione());
		} finally {
			if(ctx != null) {
				ctx.setResult(response);
				ctx.log();
			}
			if(bd != null) bd.closeConnection();
		}
		response.setCodOperazione(ThreadContext.get("op"));
		return response;
	}

	@Override
	public GpChiediListaFlussiRendicontazioneResponse gpChiediListaFlussiRendicontazione(GpChiediListaFlussiRendicontazione bodyrichiesta) {
		log.info("Richiesta operazione gpChiediListaFlussiRendicontazione per l'applicazione (" +  bodyrichiesta.getCodApplicazione()+")");
		GpChiediListaFlussiRendicontazioneResponse response = new GpChiediListaFlussiRendicontazioneResponse();
		GpContext ctx = GpThreadLocal.get();
		BasicBD bd = null;
		try {
			bd = BasicBD.newInstance();
			Applicazione applicazioneAutenticata = getApplicazioneAutenticata(bd);
			ctx.log("gpapp.ricevutaRichiesta");
			it.govpay.core.business.Rendicontazioni rendicontazioneBusiness = new it.govpay.core.business.Rendicontazioni(bd);
			List<FrApplicazione> rendicontazioni = rendicontazioneBusiness.chiediListaRendicontazioni(applicazioneAutenticata, bodyrichiesta.getCodApplicazione());
			response.setCodApplicazione(applicazioneAutenticata.getCodApplicazione());
			response.setCodEsitoOperazione(EsitoOperazione.OK);
			
			for(FrApplicazione frApplicazione : rendicontazioni) {
				FlussoRendicontazione fr = new FlussoRendicontazione();
				fr.setAnnoRiferimento(frApplicazione.getFr(bd).getAnnoRiferimento());
				fr.setCodBicRiversamento(frApplicazione.getFr(bd).getCodBicRiversamento());
				fr.setCodFlusso(frApplicazione.getFr(bd).getCodFlusso());
				fr.setCodPsp(frApplicazione.getFr(bd).getPsp(bd).getCodPsp());
				fr.setDataFlusso(frApplicazione.getFr(bd).getDataFlusso());
				fr.setDataRegolamento(frApplicazione.getFr(bd).getDataRegolamento());
				fr.setImportoTotale(frApplicazione.getImportoTotalePagamenti());
				fr.setIur(frApplicazione.getFr(bd).getIur());
				fr.setNumeroPagamenti(frApplicazione.getNumeroPagamenti());
				response.getFlussoRendicontazione().add(fr);
			}
			ctx.log("gpapp.ricevutaRichiestaOk");
		} catch (GovPayException e) {
			response.setCodEsitoOperazione(e.getCodEsito());
			response.setDescrizioneEsitoOperazione(e.getMessage());
			e.log(log);
			ctx.log("gpapp.ricevutaRichiestaKo", response.getCodEsitoOperazione().toString(), response.getDescrizioneEsitoOperazione());
		} catch (Exception e) {
			response.setCodEsitoOperazione(EsitoOperazione.INTERNAL);
			response.setDescrizioneEsitoOperazione(e.getMessage());
			new GovPayException(e).log(log);
			ctx.log("gpapp.ricevutaRichiestaKo", response.getCodEsitoOperazione().toString(), response.getDescrizioneEsitoOperazione());
		} finally {
			if(ctx != null) {
				ctx.setResult(response);
				ctx.log();
			}
			if(bd != null) bd.closeConnection();
		}
		response.setCodOperazione(ThreadContext.get("op"));
		return response;
	}

	@Override
	public GpChiediFlussoRendicontazioneResponse gpChiediFlussoRendicontazione(GpChiediFlussoRendicontazione bodyrichiesta) {
		log.info("Richiesta operazione gpChiediFlussoRendicontazione per l'applicazione (" +  bodyrichiesta.getCodApplicazione()+")");
		GpChiediFlussoRendicontazioneResponse response = new GpChiediFlussoRendicontazioneResponse();
		GpContext ctx = GpThreadLocal.get();
		BasicBD bd = null;
		try {
			bd = BasicBD.newInstance();
			Applicazione applicazioneAutenticata = getApplicazioneAutenticata(bd);
			ctx.log("gpapp.ricevutaRichiesta");
			it.govpay.core.business.Rendicontazioni rendicontazioneBusiness = new it.govpay.core.business.Rendicontazioni(bd);
			FrApplicazione frApplicazione = rendicontazioneBusiness.chiediRendicontazione(applicazioneAutenticata, bodyrichiesta.getCodApplicazione(), bodyrichiesta.getAnnoRiferimento(), bodyrichiesta.getCodFlusso());
			response.setCodApplicazione(applicazioneAutenticata.getCodApplicazione());
			response.setCodEsitoOperazione(EsitoOperazione.OK);
			FlussoRendicontazione fr = new FlussoRendicontazione();
			fr.setAnnoRiferimento(frApplicazione.getFr(bd).getAnnoRiferimento());
			fr.setCodBicRiversamento(frApplicazione.getFr(bd).getCodBicRiversamento());
			fr.setCodFlusso(frApplicazione.getFr(bd).getCodFlusso());
			fr.setCodPsp(frApplicazione.getFr(bd).getPsp(bd).getCodPsp());
			fr.setDataFlusso(frApplicazione.getFr(bd).getDataFlusso());
			fr.setDataRegolamento(frApplicazione.getFr(bd).getDataRegolamento());
			fr.setImportoTotale(frApplicazione.getImportoTotalePagamenti());
			fr.setIur(frApplicazione.getFr(bd).getIur());
			fr.setNumeroPagamenti(frApplicazione.getNumeroPagamenti());
			
			if(frApplicazione.getPagamenti(bd) != null) {
				for(Pagamento pagamento : frApplicazione.getPagamenti(bd)) {
					fr.getPagamento().add(Gp21Utils.toRendicontazionePagamento(pagamento));
				}
			}
			
			if(frApplicazione.getRendicontazioniSenzaRpt(bd) != null) {
				for(RendicontazioneSenzaRpt rendicontazione : frApplicazione.getRendicontazioniSenzaRpt(bd)) {
					fr.getPagamento().add(Gp21Utils.toRendicontazionePagamento(rendicontazione, bd));
				}
			}
			
			response.setFlussoRendicontazione(fr);
			ctx.log("gpapp.ricevutaRichiestaOk");
		} catch (GovPayException e) {
			response.setCodEsitoOperazione(e.getCodEsito());
			response.setDescrizioneEsitoOperazione(e.getMessage());
			e.log(log);
			ctx.log("gpapp.ricevutaRichiestaKo", response.getCodEsitoOperazione().toString(), response.getDescrizioneEsitoOperazione());
		} catch (Exception e) {
			response.setCodEsitoOperazione(EsitoOperazione.INTERNAL);
			response.setDescrizioneEsitoOperazione(e.getMessage());
			new GovPayException(e).log(log);
			ctx.log("gpapp.ricevutaRichiestaKo", response.getCodEsitoOperazione().toString(), response.getDescrizioneEsitoOperazione());
		} finally {
			if(ctx != null) {
				ctx.setResult(response);
				ctx.log();
			}
			if(bd != null) bd.closeConnection();
		}
		response.setCodOperazione(ThreadContext.get("op"));
		return response;
	}

	private Applicazione getApplicazioneAutenticata(BasicBD bd) throws GovPayException, ServiceException {
		if(wsCtxt.getUserPrincipal() == null) {
			throw new GovPayException(EsitoOperazione.AUT_000);
		}

		Applicazione app = null;
		try {
			app = AnagraficaManager.getApplicazioneByPrincipal(bd, wsCtxt.getUserPrincipal().getName());
		} catch (NotFoundException e) {
			throw new GovPayException(EsitoOperazione.AUT_001, wsCtxt.getUserPrincipal().getName());
		}
		
		if(app != null) {
			Actor from = new Actor();
			from.setName(app.getCodApplicazione());
			from.setType(GpContext.TIPO_SOGGETTO_APP);
			GpThreadLocal.get().getTransaction().setFrom(from);
			GpThreadLocal.get().getTransaction().getClient().setName(app.getCodApplicazione());
		}
		return app;
	}

}
