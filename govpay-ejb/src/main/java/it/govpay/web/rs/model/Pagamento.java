package it.govpay.web.rs.model;

import java.sql.Date;

import it.govpay.bd.model.BasicModel;

public class Pagamento extends BasicModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Pagamento(){super();}
	
	private String codiceCreditore;
	private String codiceTributo;
	private String codiceFiscaleContribuente;
	private String anagraficaContribuente;
	private Integer annoTributario;
	private String codiceVersamento;
	private Date dataPagamento;
	private Double importo;
	private String codiceRiversamento;
	private String codiceRendicontazione;
	private String iuv;
	private String identificativoGovPay;
	
	
	
	public String getCodiceCreditore() {
		return codiceCreditore;
	}
	public void setCodiceCreditore(String codiceCreditore) {
		this.codiceCreditore = codiceCreditore;
	}
	public String getCodiceTributo() {
		return codiceTributo;
	}
	public void setCodiceTributo(String codiceTributo) {
		this.codiceTributo = codiceTributo;
	}
	public String getCodiceFiscaleContribuente() {
		return codiceFiscaleContribuente;
	}
	public void setCodiceFiscaleContribuente(String codiceFiscaleContribuente) {
		this.codiceFiscaleContribuente = codiceFiscaleContribuente;
	}
	public String getAnagraficaContribuente() {
		return anagraficaContribuente;
	}
	public void setAnagraficaContribuente(String anagraficaContribuente) {
		this.anagraficaContribuente = anagraficaContribuente;
	}
	public Integer getAnnoTributario() {
		return annoTributario;
	}
	public void setAnnoTributario(Integer annoTributario) {
		this.annoTributario = annoTributario;
	}
	public String getCodiceVersamento() {
		return codiceVersamento;
	}
	public void setCodiceVersamento(String codiceVersamento) {
		this.codiceVersamento = codiceVersamento;
	}
	public Date getDataPagamento() {
		return dataPagamento;
	}
	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}
	public Double getImporto() {
		return importo;
	}
	public void setImporto(Double importo) {
		this.importo = importo;
	}
	public String getCodiceRiversamento() {
		return codiceRiversamento;
	}
	public void setCodiceRiversamento(String codiceRiversamento) {
		this.codiceRiversamento = codiceRiversamento;
	}
	public String getCodiceRendicontazione() {
		return codiceRendicontazione;
	}
	public void setCodiceRendicontazione(String codiceRendicontazione) {
		this.codiceRendicontazione = codiceRendicontazione;
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

}
