package it.govpay.web.rs.model;

import it.govpay.bd.model.BasicModel;

public class VoceVersamento extends BasicModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public VoceVersamento(){super();}
	
	private Double importo;
	private String ibanAccredito;
	private String causale;
	private String datiRiscossione;

	public Double getImporto() {
		return importo;
	}
	public void setImporto(Double importo) {
		this.importo = importo;
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
