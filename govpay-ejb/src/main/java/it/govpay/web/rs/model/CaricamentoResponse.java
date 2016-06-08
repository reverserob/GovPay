package it.govpay.web.rs.model;

public class CaricamentoResponse extends CaricamentoRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public CaricamentoResponse(){super();}
	
	private String codiceApplicazione;

	public String getCodiceApplicazione() {
		return codiceApplicazione;
	}
	public void setCodiceApplicazione(String codApplicazione) {
		this.codiceApplicazione = codApplicazione;
	}

}
