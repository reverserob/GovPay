package it.govpay.web.rs.model;

import java.sql.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

import it.govpay.bd.model.BasicModel;

public class Versamento extends BasicModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Versamento(){super();}

	private String codiceVersamento;
	private Date dataScadenza;
	
	private List<VoceVersamento> voci;
	
	private Double importo;
	private String ibanAccredito;
	private String causale;
	private String datiRiscossione;
	
	private String esito;
	private String idOperazione;
	private String iuv;
	private String identificativoGovPay;
	private String qrCode;
	private String barCode;
	
	public String getCodiceVersamento() {
		return codiceVersamento;
	}
	public void setCodiceVersamento(String codiceVersamento) {
		this.codiceVersamento = codiceVersamento;
	}
	public Double getImporto() {
		return importo;
	}
	public void setImporto(Double importo) {
		this.importo = importo;
	}
	public Date getDataScadenza() {
		return dataScadenza;
	}
	public void setDataScadenza(Date dataScadenza) {
		this.dataScadenza = dataScadenza;
	}
	public String getEsito() {
		return esito;
	}
	public void setEsito(String esito) {
		this.esito = esito;
	}
	public String getIdOperazione() {
		return idOperazione;
	}
	public void setIdOperazione(String idOperazione) {
		this.idOperazione = idOperazione;
	}
	public String getIuv() {
		return iuv;
	}
	public void setIuv(String iuv) {
		this.iuv = iuv;
	}
	public String getIdentificativoGovPay() {
		return identificativoGovPay;
	}
	public void setIdentificativoGovPay(String identificativoGovPay) {
		this.identificativoGovPay = identificativoGovPay;
	}
	public String getQrCode() {
		return qrCode;
	}
	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}
	public String getBarCode() {
		return barCode;
	}
	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}
	@JsonIgnore
	public List<VoceVersamento> getVoci() {
		return voci;
	}
	public void setVoci(List<VoceVersamento> voci) {
		this.voci = voci;
	}
	public String getIbanAccredito() {
		return ibanAccredito;
	}
	public void setIbanAccredito(String ibanAccredito) {
		this.ibanAccredito = ibanAccredito;
	}
	public String getCausale() {
		return causale;
	}
	public void setCausale(String causale) {
		this.causale = causale;
	}
	public String getDatiRiscossione() {
		return datiRiscossione;
	}
	public void setDatiRiscossione(String datiRiscossione) {
		this.datiRiscossione = datiRiscossione;
	}
	
}
