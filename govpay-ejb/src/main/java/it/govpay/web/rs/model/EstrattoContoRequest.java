package it.govpay.web.rs.model;

import java.util.Date;

import it.govpay.bd.model.BasicModel;

public class EstrattoContoRequest extends BasicModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public EstrattoContoRequest(){super();}
	
	
	private String codiceCreditore;
	private String codiceTributo;
	private String codiceFiscaleContribuente;
	private Integer annoTributario;
	private Date dataInizio;
	private Date dataFine;
	private Integer pagina;
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
	public Integer getAnnoTributario() {
		return annoTributario;
	}
	public void setAnnoTributario(Integer annoTributario) {
		this.annoTributario = annoTributario;
	}
	public Date getDataInizio() {
		return dataInizio;
	}
	public void setDataInizio(Date dataInizio) {
		this.dataInizio = dataInizio;
	}
	public Date getDataFine() {
		return dataFine;
	}
	public void setDataFine(Date dataFine) {
		this.dataFine = dataFine;
	}
	public Integer getPagina() {
		return pagina;
	}
	public void setPagina(Integer pagina) {
		this.pagina = pagina;
	}
	
	

}
