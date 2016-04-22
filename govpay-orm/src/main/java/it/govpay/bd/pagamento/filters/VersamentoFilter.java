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
package it.govpay.bd.pagamento.filters;

import it.govpay.bd.AbstractFilter;
import it.govpay.bd.ConnectionManager;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.orm.Versamento;
import it.govpay.orm.dao.jdbc.converter.VersamentoFieldConverter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.beans.CustomField;
import org.openspcoop2.generic_project.dao.IExpressionConstructor;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.generic_project.expression.SortOrder;

public class VersamentoFilter extends AbstractFilter {
	
	private String statoPagamento =  null;
	private String codUnivocoDebitore;
	private List<Long> idUo;
	private List<Long> idApplicazioni;
	private Date datainizio;
	private Date dataFine;
	
	public enum SortFields {
		STATO
	}
	
	public VersamentoFilter(IExpressionConstructor expressionConstructor) {
		super(expressionConstructor);
	}

	@Override
	public IExpression toExpression() throws ServiceException {
		try {
			IExpression newExpression = this.newExpression();
			boolean addAnd = false;
			// Filtro sullo stato pagamenti
			if(this.statoPagamento != null && StringUtils.isNotEmpty(this.statoPagamento)){
				newExpression.equals(Versamento.model().STATO_VERSAMENTO, this.statoPagamento);
				addAnd = true;
			}
			
			if(this.datainizio != null && this.dataFine != null) {
				if(addAnd)
					newExpression.and();
				
				newExpression.between(Versamento.model().DATA_ORA_ULTIMO_AGGIORNAMENTO, this.datainizio,this.dataFine);
				addAnd = true;
			}
			
			if((this.idUo != null && !this.idUo.isEmpty()) || (this.idApplicazioni != null && !this.idApplicazioni.isEmpty())) {
				IExpression newExpressionEntiApplicazioni = this.newExpression();
				VersamentoFieldConverter versamentoFieldConverter = new VersamentoFieldConverter(ConnectionManager.getJDBCServiceManagerProperties().getDatabase()); 
				
				CustomField idEnteCustomField = new CustomField("id_ente",  Long.class, "id_ente",  versamentoFieldConverter.toTable(Versamento.model()));
				CustomField idApplicazioneCustomField = new CustomField("id_applicazione",  Long.class, "id_applicazione",  versamentoFieldConverter.toTable(Versamento.model()));
				
				if(this.idUo != null && !this.idUo.isEmpty()) {
					for(Long id: this.idUo) {
						newExpressionEntiApplicazioni.equals(idEnteCustomField, id).or();
					}
				}
				
				if(this.idApplicazioni != null && !this.idApplicazioni.isEmpty()) {
					for(Long id: this.idApplicazioni) {
						newExpressionEntiApplicazioni.equals(idApplicazioneCustomField, id).or();
					}
				}
				newExpression.and(newExpressionEntiApplicazioni);
				
			}
			
			if(this.codUnivocoDebitore != null) {
				newExpression.equals(Versamento.model().DEBITORE_IDENTIFICATIVO, this.codUnivocoDebitore);
			}
			
			return newExpression;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionNotImplementedException e) {
			throw new ServiceException(e);
		} catch (ExpressionException e) {
			throw new ServiceException(e);
		}
	}

	public void addSortField(SortFields field, boolean asc) {
		FilterSortWrapper filterSortWrapper = new FilterSortWrapper();
		if(field.equals(SortFields.STATO)) 
			filterSortWrapper.setField(Versamento.model().STATO_VERSAMENTO); 
		filterSortWrapper.setSortOrder((asc ? SortOrder.ASC : SortOrder.DESC));
		this.filterSortList.add(filterSortWrapper);
	}

	public String getStatoPagamento() {
		return statoPagamento;
	}

	public void setStatoPagamento(String statoPagamento) {
		this.statoPagamento = statoPagamento;
	}

	public List<Long> getIdUo() {
		if(idUo == null) idUo = new ArrayList<Long>();
		return idUo;
	}

	public void setIdUo(List<Long> idUo) {
		this.idUo = idUo;
	}

	public List<Long> getIdApplicazioni() {
		if(idApplicazioni == null) idApplicazioni = new ArrayList<Long>();
		return idApplicazioni;
	}

	public void setIdApplicazioni(List<Long> idApplicazioni) {
		this.idApplicazioni = idApplicazioni;
	}

	public String getCodUnivocoDebitore() {
		return codUnivocoDebitore;
	}

	public void setCodUnivocoDebitore(String codUnivocoDebitore) {
		this.codUnivocoDebitore = codUnivocoDebitore;
	}

}