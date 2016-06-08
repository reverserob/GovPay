package it.govpay.web.rs.model;

import java.util.List;

import it.govpay.bd.model.BasicModel;

public class CaricamentoRequest  extends BasicModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public CaricamentoRequest(){super();}
	
	private String codiceCreditore;
	private String codiceTributo;
	private String codiceFiscaleContribuente;
	private String anagraficaContribuente;
	private Integer annoTributario;
	private List<Versamento> versamenti;

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
	public List<Versamento> getVersamenti() {
		return versamenti;
	}
	public void setVersamenti(List<Versamento> versamenti) {
		this.versamenti = versamenti;
	}

	
}
