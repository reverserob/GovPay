package it.govpay.web.rs.model;

import java.util.List;

import it.govpay.bd.model.BasicModel;

public class EstrattoContoResponse extends BasicModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public EstrattoContoResponse(){super();}
	
	private List<Pagamento> pagamenti;

	public List<Pagamento> getPagamenti() {
		return pagamenti;
	}

	public void setPagamenti(List<Pagamento> pagamenti) {
		this.pagamenti = pagamenti;
	}
	
	
}
