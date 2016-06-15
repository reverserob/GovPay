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
package it.govpay.core.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.SingoloVersamento.StatoSingoloVersamento;
import it.govpay.bd.model.SingoloVersamento.TipoBollo;
import it.govpay.bd.model.Tributo;
import it.govpay.bd.model.Tributo.TipoContabilta;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.model.Versamento.CausaleSemplice;
import it.govpay.bd.model.Versamento.CausaleSpezzoni;
import it.govpay.bd.model.Versamento.CausaleSpezzoniStrutturati;
import it.govpay.bd.model.Versamento.StatoVersamento;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.VersamentoAnnullatoException;
import it.govpay.core.exceptions.VersamentoDuplicatoException;
import it.govpay.core.exceptions.VersamentoScadutoException;
import it.govpay.core.exceptions.VersamentoSconosciutoException;
import it.govpay.core.utils.client.BasicClient.ClientException;
import it.govpay.core.utils.client.VerificaClient;
import it.govpay.servizi.commons.Anagrafica;
import it.govpay.servizi.commons.EsitoOperazione;
import it.govpay.servizi.commons.Versamento.SpezzoneCausaleStrutturata;

public class VersamentoUtils {

	public static void validazioneSemantica(Versamento versamento, boolean generaIuv, BasicBD bd) throws GovPayException, ServiceException {
		if(generaIuv && versamento.getSingoliVersamenti(bd).size() != 1) {
			throw new GovPayException(EsitoOperazione.VER_000, versamento.getApplicazione(bd).getCodApplicazione(), versamento.getCodVersamentoEnte());
		}
		
		BigDecimal somma = BigDecimal.ZERO;
		List<String> codSingoliVersamenti = new ArrayList<String>();
		for(SingoloVersamento sv : versamento.getSingoliVersamenti(bd)) {
			if(codSingoliVersamenti.contains(sv.getCodSingoloVersamentoEnte()))
				throw new GovPayException(EsitoOperazione.VER_001, versamento.getApplicazione(bd).getCodApplicazione(), versamento.getCodVersamentoEnte(), sv.getCodSingoloVersamentoEnte());
			
			codSingoliVersamenti.add(sv.getCodSingoloVersamentoEnte());
			somma = somma.add(sv.getImportoSingoloVersamento());
		}
		
		if(somma.compareTo(versamento.getImportoTotale()) != 0) {
			throw new GovPayException(EsitoOperazione.VER_002, versamento.getApplicazione(bd).getCodApplicazione(), versamento.getCodVersamentoEnte(), Double.toString(somma.doubleValue()), Double.toString(versamento.getImportoTotale().doubleValue()));
		}
	}

	public static Versamento toVersamentoModel(it.govpay.servizi.commons.Versamento versamento, BasicBD bd) throws ServiceException, GovPayException {
		Versamento model = new Versamento();
		model.setAggiornabile(versamento.isAggiornabile() == null ? true : versamento.isAggiornabile());
		model.setAnagraficaDebitore(toAnagraficaModel(versamento.getDebitore()));
		
		if(versamento.getCausale() != null) {
			CausaleSemplice causale = model.new CausaleSemplice();
			causale.setCausale(versamento.getCausale());
			model.setCausaleVersamento(causale);
		}
		
		if(versamento.getSpezzoneCausale() != null && versamento.getSpezzoneCausale().size() > 0) {
			CausaleSpezzoni causale = model.new CausaleSpezzoni();
			causale.setSpezzoni(versamento.getSpezzoneCausale());
			model.setCausaleVersamento(causale);
		}
		
		if(versamento.getSpezzoneCausaleStrutturata() != null && versamento.getSpezzoneCausaleStrutturata().size() > 0) {
			CausaleSpezzoniStrutturati causale = model.new CausaleSpezzoniStrutturati();
			for(SpezzoneCausaleStrutturata s : versamento.getSpezzoneCausaleStrutturata())
				causale.addSpezzoneStrutturato(s.getCausale(), s.getImporto());
			model.setCausaleVersamento(causale);
		}
		
		model.setCodVersamentoEnte(versamento.getCodVersamentoEnte());
		model.setDataCreazione(new Date());
		model.setDataScadenza(versamento.getDataScadenza());
		model.setDataUltimoAggiornamento(new Date());
		model.setDescrizioneStato(null);
		model.setId(null);
		try {
			model.setApplicazione(versamento.getCodApplicazione(), bd);
		} catch (NotFoundException e) {
			throw new GovPayException(EsitoOperazione.APP_000, versamento.getCodApplicazione());
		}
		
		Dominio dominio = null;
		try {
			dominio = AnagraficaManager.getDominio(bd, versamento.getCodDominio());
		} catch (NotFoundException e) {
			throw new GovPayException(EsitoOperazione.DOM_000, versamento.getCodDominio());
		}
		
		try {
			String codUnitaOperativa = (versamento.getCodUnitaOperativa() == null) ? Dominio.EC : versamento.getCodUnitaOperativa();
			model.setUo(dominio.getId(), codUnitaOperativa, bd);
		} catch (NotFoundException e) {
			throw new GovPayException(EsitoOperazione.UOP_000, versamento.getCodUnitaOperativa(), versamento.getCodDominio());
		}
		
		model.setImportoTotale(versamento.getImportoTotale());
		model.setStatoVersamento(StatoVersamento.NON_ESEGUITO);
		
		for(it.govpay.servizi.commons.Versamento.SingoloVersamento singoloVersamento : versamento.getSingoloVersamento()) {
			model.addSingoloVersamento(toSingoloVersamentoModel(model, singoloVersamento, bd));
		}
		
		return model;
	}
	
	public static SingoloVersamento toSingoloVersamentoModel(Versamento versamento, it.govpay.servizi.commons.Versamento.SingoloVersamento singoloVersamento, BasicBD bd) throws ServiceException, GovPayException {
		SingoloVersamento model = new SingoloVersamento();
		model.setVersamento(versamento);
		model.setCodSingoloVersamentoEnte(singoloVersamento.getCodSingoloVersamentoEnte());
		model.setId(null);
		model.setIdVersamento(0);
		model.setImportoSingoloVersamento(singoloVersamento.getImporto());
		model.setStatoSingoloVersamento(StatoSingoloVersamento.NON_ESEGUITO);
		if(singoloVersamento.getBolloTelematico() != null) {
			try {
				model.setTributo(Tributo.BOLLOT, bd);
			} catch (NotFoundException e) {
				throw new GovPayException(EsitoOperazione.TRB_000, versamento.getUo(bd).getDominio(bd).getCodDominio(), Tributo.BOLLOT);
			}
			model.setHashDocumento(singoloVersamento.getBolloTelematico().getHash());
			model.setProvinciaResidenza(singoloVersamento.getBolloTelematico().getProvincia());
			model.setTipoBollo(TipoBollo.toEnum(singoloVersamento.getBolloTelematico().getTipo()));
		} 
		
		if(singoloVersamento.getCodTributo() != null) {
			try {
				model.setTributo(singoloVersamento.getCodTributo(), bd);
			} catch (NotFoundException e) {
				throw new GovPayException(EsitoOperazione.TRB_000, versamento.getUo(bd).getDominio(bd).getCodDominio(), singoloVersamento.getCodTributo());
			}
			
			if(!versamento.getApplicazione(bd).isTrusted() && !versamento.getApplicazione(bd).getIdTributi().contains(model.getIdTributo())) {
				throw new GovPayException(EsitoOperazione.VER_022, versamento.getUo(bd).getDominio(bd).getCodDominio(), singoloVersamento.getCodTributo());
			}
		}
		
		if(singoloVersamento.getTributo() != null) {
			
			if(!versamento.getApplicazione(bd).isTrusted())
				throw new GovPayException(EsitoOperazione.VER_019);
			
			if(!versamento.getApplicazione(bd).getIdDomini().contains(versamento.getUo(bd).getDominio(bd).getId()))
				throw new GovPayException(EsitoOperazione.VER_021);
			
			try {
				model.setIbanAccredito(AnagraficaManager.getIbanAccredito(bd, versamento.getUo(bd).getDominio(bd).getId(), singoloVersamento.getTributo().getIbanAccredito()));
				model.setTipoContabilita(TipoContabilta.valueOf(singoloVersamento.getTributo().getTipoContabilita().toString()));
				model.setCodContabilita(singoloVersamento.getTributo().getCodContabilita());
			} catch (NotFoundException e) {
				throw new GovPayException(EsitoOperazione.VER_020, versamento.getUo(bd).getDominio(bd).getCodDominio(), singoloVersamento.getTributo().getIbanAccredito());
			}
		}
		
		return model;
	}

	public static void validazioneSemanticaAggiornamento(Versamento versamentoLetto, Versamento versamentoNuovo, BasicBD bd) throws GovPayException, ServiceException {
		
		if(!versamentoLetto.getStatoVersamento().equals(StatoVersamento.NON_ESEGUITO) && !versamentoLetto.getStatoVersamento().equals(StatoVersamento.ANNULLATO)) {
			throw new GovPayException(EsitoOperazione.VER_003, versamentoNuovo.getApplicazione(bd).getCodApplicazione(), versamentoLetto.getCodVersamentoEnte(), versamentoLetto.getStatoVersamento().toString());
		}
		if(versamentoLetto.getIdUo() != versamentoNuovo.getIdUo()) {
			throw new GovPayException(EsitoOperazione.VER_004, versamentoNuovo.getApplicazione(bd).getCodApplicazione(), versamentoLetto.getCodVersamentoEnte(), versamentoLetto.getUo(bd).getCodUo(), versamentoNuovo.getUo(bd).getCodUo());
		}
		versamentoNuovo.setId(versamentoLetto.getId());
		versamentoNuovo.setDataCreazione(versamentoLetto.getDataCreazione());
		
		List<SingoloVersamento> singoliversamentiLetti = versamentoLetto.getSingoliVersamenti(bd);
		List<SingoloVersamento> singoliVersamentiNuovi = versamentoNuovo.getSingoliVersamenti(bd);
		
		if(singoliversamentiLetti.size() != singoliVersamentiNuovi.size()) {
			throw new GovPayException(EsitoOperazione.VER_005, versamentoNuovo.getApplicazione(bd).getCodApplicazione(), versamentoNuovo.getCodVersamentoEnte(), Integer.toString(singoliversamentiLetti.size()), Integer.toString(singoliVersamentiNuovi.size()));
		}
		Collections.sort(singoliversamentiLetti);
		Collections.sort(singoliVersamentiNuovi);
		
		for(int i=0; i<singoliversamentiLetti.size(); i++) {
			SingoloVersamento letto = singoliversamentiLetti.get(i);
			SingoloVersamento nuovo = singoliVersamentiNuovi.get(i);
			
			if(!letto.getCodSingoloVersamentoEnte().equals(nuovo.getCodSingoloVersamentoEnte())) {
				throw new GovPayException(EsitoOperazione.VER_006, versamentoNuovo.getApplicazione(bd).getCodApplicazione(), versamentoNuovo.getCodVersamentoEnte(), letto.getCodSingoloVersamentoEnte());
			}
			
			if(letto.getIdTributo() != nuovo.getIdTributo()) {
				throw new GovPayException(EsitoOperazione.VER_007, versamentoNuovo.getApplicazione(bd).getCodApplicazione(), versamentoNuovo.getCodVersamentoEnte(), letto.getCodSingoloVersamentoEnte(), Long.toString(letto.getIdTributo()), Long.toString(nuovo.getIdTributo()));
			}
			
			if(letto.getIdIbanAccredito() != nuovo.getIdIbanAccredito()) {
				throw new GovPayException(EsitoOperazione.VER_023, versamentoNuovo.getApplicazione(bd).getCodApplicazione(), versamentoNuovo.getCodVersamentoEnte(), letto.getCodSingoloVersamentoEnte());
			}
			
			nuovo.setId(letto.getId());
			nuovo.setIdVersamento(letto.getIdVersamento());
		}
	}
	
	
	public static it.govpay.bd.model.Anagrafica toAnagraficaModel(Anagrafica anagrafica) {
		if(anagrafica == null) return null;
		it.govpay.bd.model.Anagrafica anagraficaModel = new it.govpay.bd.model.Anagrafica();
		anagraficaModel.setCap(anagrafica.getCap());
		anagraficaModel.setCellulare(anagrafica.getCellulare());
		anagraficaModel.setCivico(anagrafica.getCivico());
		anagraficaModel.setCodUnivoco(anagrafica.getCodUnivoco());
		anagraficaModel.setEmail(anagrafica.getEmail());
		anagraficaModel.setFax(anagrafica.getFax());
		anagraficaModel.setIndirizzo(anagrafica.getIndirizzo());
		anagraficaModel.setLocalita(anagrafica.getLocalita());
		anagraficaModel.setNazione(anagrafica.getNazione());
		anagraficaModel.setProvincia(anagrafica.getProvincia());
		anagraficaModel.setRagioneSociale(anagrafica.getRagioneSociale());
		anagraficaModel.setTelefono(anagrafica.getTelefono());
		return anagraficaModel;
	}
	
	
	public static Versamento aggiornaVersamento(Versamento versamento, BasicBD bd) throws VersamentoScadutoException, VersamentoAnnullatoException, VersamentoDuplicatoException, VersamentoSconosciutoException, ServiceException, ClientException, GovPayException {
		// Se il versamento non e' in attesa, non aggiorno un bel niente
		if(!versamento.getStatoVersamento().equals(StatoVersamento.NON_ESEGUITO))
			return versamento;
		
		// Controllo se la data di scadenza e' indicata ed e' decorsa
		if(versamento.getDataScadenza() != null && versamento.getDataScadenza().before(new Date())) {
			if(versamento.isAggiornabile() && versamento.getApplicazione(bd).getConnettoreVerifica() != null) {
				versamento = acquisisciVersamento(versamento.getApplicazione(bd), versamento.getCodVersamentoEnte(), null, bd);
			} else {
				throw new VersamentoScadutoException();
			}
		}
		return versamento;
	}

	public static Versamento acquisisciVersamento(Applicazione applicazione, String codVersamentoEnte, String iuv, BasicBD bd) throws VersamentoScadutoException, VersamentoAnnullatoException, VersamentoDuplicatoException, VersamentoSconosciutoException, ServiceException, ClientException, GovPayException {
		GpContext ctx = GpThreadLocal.get();
		if(applicazione.getConnettoreVerifica() == null) {
			ctx.log("versamento.verificaNonConfigurata");
			throw new VersamentoSconosciutoException();
		}
		VerificaClient verificaClient = new VerificaClient(applicazione);
		Versamento versamento = verificaClient.invoke(codVersamentoEnte, iuv, bd);
		it.govpay.core.business.Versamento versamentoBusiness = new it.govpay.core.business.Versamento(bd);
		versamentoBusiness.caricaVersamento(applicazione, versamento, false, true);
		return versamento;
	}
}