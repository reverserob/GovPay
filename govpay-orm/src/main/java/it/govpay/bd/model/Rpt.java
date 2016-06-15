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
package it.govpay.bd.model;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.Canale.ModelloPagamento;
import it.govpay.bd.pagamento.PagamentiBD;
import it.govpay.bd.pagamento.VersamentiBD;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;

public class Rpt extends BasicModel{
	
	private static final long serialVersionUID = 1L;
	public static final String VERSIONE = "6.2";
	public static final int VERSIONE_ENCODED = 060200;
	
	public static final String CCP_NA = "n/a";
	
	public enum FirmaRichiesta {
	    CA_DES("1"),
	    XA_DES("3"),
	    AVANZATA("4"),
	    NESSUNA("0");
	    
		private String codifica;

		FirmaRichiesta(String codifica) {
			this.codifica = codifica;
		}
		public String getCodifica() {
			return codifica;
		}
		
		public static FirmaRichiesta toEnum(String codifica) throws ServiceException {
			// FIX Bug Nodo che imposta firma vuota in caso di NESSUNA
			if(codifica.isEmpty())
				return NESSUNA;
			
			for(FirmaRichiesta p : FirmaRichiesta.values()){
				if(p.getCodifica().equals(codifica))
					return p;
			}
			
			throw new ServiceException("Codifica inesistente per FirmaRichiesta. Valore fornito [" + codifica + "] valori possibili " + ArrayUtils.toString(FirmaRichiesta.values()));
		}
	}
	
	public enum EsitoPagamento {
		PAGAMENTO_ESEGUITO(0), 
		PAGAMENTO_NON_ESEGUITO(1),
		PAGAMENTO_PARZIALMENTE_ESEGUITO(2),
		DECORRENZA_TERMINI(3), 
		DECORRENZA_TERMINI_PARZIALE(4);

		private int codifica;

		EsitoPagamento(int codifica) {
			this.codifica = codifica;
		}

		public int getCodifica() {
			return codifica;
		}

		public static EsitoPagamento toEnum(String codifica) throws ServiceException {
			try {
				int codifica2 = Integer.parseInt(codifica);
				return toEnum(codifica2);
			} catch (NumberFormatException e) {
				throw new ServiceException("Codifica inesistente per EsitoPagamento. Valore fornito [" + codifica + "] valori possibili " + ArrayUtils.toString(EsitoPagamento.values()));
			}
		}

		public static EsitoPagamento toEnum(int codifica) throws ServiceException {
			for(EsitoPagamento p : EsitoPagamento.values()){
				if(p.getCodifica() == codifica)
					return p;
			}
			throw new ServiceException("Codifica inesistente per EsitoPagamento. Valore fornito [" + codifica + "] valori possibili " + ArrayUtils.toString(EsitoPagamento.values()));
		}
	}
	
	public enum StatoRpt {
		RPT_ATTIVATA,
		RPT_ERRORE_INVIO_A_NODO, 
		RPT_RICEVUTA_NODO, 
		RPT_RIFIUTATA_NODO, 
		RPT_ACCETTATA_NODO, 
		RPT_RIFIUTATA_PSP, 
		RPT_ACCETTATA_PSP, 
		RPT_ERRORE_INVIO_A_PSP, 
		RPT_INVIATA_A_PSP, 
		RPT_DECORSI_TERMINI,
		RT_RICEVUTA_NODO,
		RT_RIFIUTATA_NODO,
		RT_ACCETTATA_NODO,
		RT_ACCETTATA_PA,
		RT_RIFIUTATA_PA,
		RT_ESITO_SCONOSCIUTO_PA,
		INTERNO_NODO;
		
		public static StatoRpt toEnum(String s) {
			try {
				return StatoRpt.valueOf(s);
			} catch (IllegalArgumentException e) {
				return INTERNO_NODO;
			}
		}
	}
	
	private Long id;
	private long idVersamento;
	private long idCanale;
	private Long idPortale;
	private String ccp;
	private String codCarrello;
	private String codStazione;
	private String codDominio;
	private String iuv;
	private String codMsgRichiesta;
	private Date dataMsgRichiesta;
	private StatoRpt stato;
	private String descrizioneStato;
	private String codSessione;
	private String pspRedirectURL;
	private byte[] xmlRpt;
	private Date dataAggiornamento;
	private String callbackURL;
	private ModelloPagamento modelloPagamento;
	private FirmaRichiesta firmaRichiesta;
	
	private String codMsgRicevuta;
	private Date dataMsgRicevuta;
	private EsitoPagamento esitoPagamento;
	private BigDecimal importoTotalePagato;
	private byte[] xmlRt;
	
	private String idTransazioneRpt;
	private String idTransazioneRt;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public long getIdVersamento() {
		return idVersamento;
	}
	public void setIdVersamento(long idVersamento) {
		this.idVersamento = idVersamento;
	}
	public long getIdCanale() {
		return idCanale;
	}
	public void setIdCanale(long idCanale) {
		this.idCanale = idCanale;
	}
	public Long getIdPortale() {
		return idPortale;
	}
	public void setIdPortale(Long idPortale) {
		this.idPortale = idPortale;
	}
	public String getCcp() {
		return ccp;
	}
	public void setCcp(String ccp) {
		this.ccp = ccp;
	}
	public String getCodCarrello() {
		return codCarrello;
	}
	public void setCodCarrello(String codCarrello) {
		this.codCarrello = codCarrello;
	}
	public String getCodStazione() {
		return codStazione;
	}
	public void setCodStazione(String codStazione) {
		this.codStazione = codStazione;
	}
	public String getCodDominio() {
		return codDominio;
	}
	public void setCodDominio(String codDominio) {
		this.codDominio = codDominio;
	}
	public String getIuv() {
		return iuv;
	}
	public void setIuv(String iuv) {
		this.iuv = iuv;
	}
	public String getCodMsgRichiesta() {
		return codMsgRichiesta;
	}
	public void setCodMsgRichiesta(String codMsgRichiesta) {
		this.codMsgRichiesta = codMsgRichiesta;
	}
	public Date getDataMsgRichiesta() {
		return dataMsgRichiesta;
	}
	public void setDataMsgRichiesta(Date dataMsgRichiesta) {
		this.dataMsgRichiesta = dataMsgRichiesta;
	}
	public StatoRpt getStato() {
		return stato;
	}
	public void setStato(StatoRpt stato) {
		this.stato = stato;
	}
	public String getDescrizioneStato() {
		return descrizioneStato;
	}
	public void setDescrizioneStato(String descrizioneStato) {
		this.descrizioneStato = descrizioneStato;
	}
	public String getCodSessione() {
		return codSessione;
	}
	public void setCodSessione(String codSessione) {
		this.codSessione = codSessione;
	}
	public String getPspRedirectURL() {
		return pspRedirectURL;
	}
	public void setPspRedirectURL(String pspRedirectURL) {
		this.pspRedirectURL = pspRedirectURL;
	}
	public byte[] getXmlRpt() {
		return xmlRpt;
	}
	public void setXmlRpt(byte[] xml) {
		this.xmlRpt = xml;
	}
	public Date getDataAggiornamento() {
		return dataAggiornamento;
	}
	public void setDataAggiornamento(Date dataAggiornamento) {
		this.dataAggiornamento = dataAggiornamento;
	}
	public String getCallbackURL() {
		return callbackURL;
	}
	public void setCallbackURL(String callbackURL) {
		this.callbackURL = callbackURL;
	}
	public ModelloPagamento getModelloPagamento() {
		return modelloPagamento;
	}
	public void setModelloPagamento(ModelloPagamento modelloPagamento) {
		this.modelloPagamento = modelloPagamento;
	}
	public FirmaRichiesta getFirmaRichiesta() {
		return firmaRichiesta;
	}
	public void setFirmaRichiesta(FirmaRichiesta firmaRichiesta) {
		this.firmaRichiesta = firmaRichiesta;
	}
	public Date getDataMsgRicevuta() {
		return dataMsgRicevuta;
	}
	public void setDataMsgRicevuta(Date dataMsgRicevuta) {
		this.dataMsgRicevuta = dataMsgRicevuta;
	}
	public String getCodMsgRicevuta() {
		return codMsgRicevuta;
	}
	public void setCodMsgRicevuta(String codMsgRicevuta) {
		this.codMsgRicevuta = codMsgRicevuta;
	}
	public EsitoPagamento getEsitoPagamento() {
		return esitoPagamento;
	}
	public void setEsitoPagamento(EsitoPagamento esitoPagamento) {
		this.esitoPagamento = esitoPagamento;
	}
	public BigDecimal getImportoTotalePagato() {
		return importoTotalePagato;
	}
	public void setImportoTotalePagato(BigDecimal importoTotalePagato) {
		this.importoTotalePagato = importoTotalePagato;
	}
	public byte[] getXmlRt() {
		return xmlRt;
	}
	public void setXmlRt(byte[] xml) {
		this.xmlRt = xml;
	}
	public String getIdTransazioneRt() {
		return idTransazioneRt;
	}
	public void setIdTransazioneRt(String idTransazioneRt) {
		this.idTransazioneRt = idTransazioneRt;
	}
	public String getIdTransazioneRpt() {
		return idTransazioneRpt;
	}
	public void setIdTransazioneRpt(String idTransazioneRpt) {
		this.idTransazioneRpt = idTransazioneRpt;
	}
	// Business
	
	private Versamento versamento;
	private Stazione stazione;
	private Intermediario intermediario;
	private Canale canale;
	private Psp psp;
	private List<Pagamento> pagamenti;
	
	
	public Versamento getVersamento(BasicBD bd) throws ServiceException {
		if(this.versamento == null) {
			VersamentiBD versamentiBD = new VersamentiBD(bd);
			this.versamento = versamentiBD.getVersamento(getIdVersamento());
		}
		return this.versamento;
	}
	
	public void setVersamento(Versamento versamento) {
		this.versamento = versamento;
	}
	
	public Stazione getStazione(BasicBD bd) throws ServiceException {
		if(this.stazione == null) {
			try {
				this.stazione = AnagraficaManager.getStazione(bd, getCodStazione());
			} catch (NotFoundException e) {
				throw new ServiceException(e);
			}
		}
		return this.stazione;
	}
	
	public void setStazione(Stazione stazione) {
		this.stazione = stazione;
	}
	
	public Intermediario getIntermediario(BasicBD bd) throws ServiceException {
		if(this.intermediario == null) {
			this.intermediario = AnagraficaManager.getIntermediario(bd, getStazione(bd).getIdIntermediario());
		}
		return this.intermediario;
	}
	public void setIntermediario(Intermediario intermediario) {
		this.intermediario = intermediario;
	}
	
	public Canale getCanale(BasicBD bd) throws ServiceException {
		if(canale == null)
			canale = AnagraficaManager.getCanale(bd, getIdCanale());
		return canale;
	}
	public void setCanale(Canale canale) {
		this.canale = canale;
	}
	
	public Psp getPsp(BasicBD bd) throws ServiceException {
		if(psp == null) 
			psp = AnagraficaManager.getPsp(bd, getCanale(bd).getIdPsp());
		return psp;
	}
	public void setPsp(Psp psp) {
		this.psp = psp;
	}
	
	public List<Pagamento> getPagamenti(BasicBD bd) throws ServiceException {
		if(pagamenti == null) {
			PagamentiBD pagamentiBD = new PagamentiBD(bd);
			pagamenti = pagamentiBD.getPagamenti(getId());
		}
		return pagamenti;
	}
	
	public Pagamento getPagamento(String iur, BasicBD bd) throws ServiceException, NotFoundException {
		List<Pagamento> pagamenti = getPagamenti(bd);
		for(Pagamento pagamento : pagamenti) {
			if(pagamento.getIur().equals(iur))
				return pagamento;
		}
		throw new NotFoundException();
	}
	
	public void setPagamenti(List<Pagamento> pagamenti) {
		this.pagamenti = pagamenti;
	}

}
