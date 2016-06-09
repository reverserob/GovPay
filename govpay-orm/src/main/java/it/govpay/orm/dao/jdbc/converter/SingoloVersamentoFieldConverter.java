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
package it.govpay.orm.dao.jdbc.converter;

import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.IModel;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.expression.impl.sql.AbstractSQLFieldConverter;
import org.openspcoop2.utils.TipiDatabase;

import it.govpay.orm.SingoloVersamento;


/**     
 * SingoloVersamentoFieldConverter
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class SingoloVersamentoFieldConverter extends AbstractSQLFieldConverter {

	private TipiDatabase databaseType;
	
	public SingoloVersamentoFieldConverter(String databaseType){
		this.databaseType = TipiDatabase.toEnumConstant(databaseType);
	}
	public SingoloVersamentoFieldConverter(TipiDatabase databaseType){
		this.databaseType = databaseType;
	}


	@Override
	public IModel<?> getRootModel() throws ExpressionException {
		return SingoloVersamento.model();
	}
	
	@Override
	public TipiDatabase getDatabaseType() throws ExpressionException {
		return this.databaseType;
	}
	


	@Override
	public String toColumn(IField field,boolean returnAlias,boolean appendTablePrefix) throws ExpressionException {
		
		// In the case of columns with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the column containing the alias
		
		if(field.equals(SingoloVersamento.model().ID_VERSAMENTO.COD_VERSAMENTO_ENTE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_versamento_ente";
			}else{
				return "cod_versamento_ente";
			}
		}
		if(field.equals(SingoloVersamento.model().ID_VERSAMENTO.ID_APPLICAZIONE.COD_APPLICAZIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_applicazione";
			}else{
				return "cod_applicazione";
			}
		}
		if(field.equals(SingoloVersamento.model().ID_VERSAMENTO.DEBITORE_IDENTIFICATIVO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_identificativo";
			}else{
				return "debitore_identificativo";
			}
		}
		if(field.equals(SingoloVersamento.model().ID_VERSAMENTO.DEBITORE_ANAGRAFICA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".debitore_anagrafica";
			}else{
				return "debitore_anagrafica";
			}
		}
		if(field.equals(SingoloVersamento.model().ID_VERSAMENTO.COD_VERSAMENTO_LOTTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_versamento_lotto";
			}else{
				return "cod_versamento_lotto";
			}
		}
		if(field.equals(SingoloVersamento.model().ID_VERSAMENTO.COD_ANNO_TRIBUTARIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_anno_tributario";
			}else{
				return "cod_anno_tributario";
			}
		}
		if(field.equals(SingoloVersamento.model().ID_TRIBUTO.ID_DOMINIO.COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(SingoloVersamento.model().ID_TRIBUTO.COD_TRIBUTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_tributo";
			}else{
				return "cod_tributo";
			}
		}
		if(field.equals(SingoloVersamento.model().COD_SINGOLO_VERSAMENTO_ENTE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_singolo_versamento_ente";
			}else{
				return "cod_singolo_versamento_ente";
			}
		}
		if(field.equals(SingoloVersamento.model().STATO_SINGOLO_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".stato_singolo_versamento";
			}else{
				return "stato_singolo_versamento";
			}
		}
		if(field.equals(SingoloVersamento.model().IMPORTO_SINGOLO_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".importo_singolo_versamento";
			}else{
				return "importo_singolo_versamento";
			}
		}
		if(field.equals(SingoloVersamento.model().ANNO_RIFERIMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".anno_riferimento";
			}else{
				return "anno_riferimento";
			}
		}
		if(field.equals(SingoloVersamento.model().TIPO_BOLLO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".tipo_bollo";
			}else{
				return "tipo_bollo";
			}
		}
		if(field.equals(SingoloVersamento.model().HASH_DOCUMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".hash_documento";
			}else{
				return "hash_documento";
			}
		}
		if(field.equals(SingoloVersamento.model().PROVINCIA_RESIDENZA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".provincia_residenza";
			}else{
				return "provincia_residenza";
			}
		}
		if(field.equals(SingoloVersamento.model().ID_IBAN_ACCREDITO.COD_IBAN)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_iban";
			}else{
				return "cod_iban";
			}
		}
		if(field.equals(SingoloVersamento.model().ID_IBAN_ACCREDITO.ID_DOMINIO.COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(SingoloVersamento.model().TIPO_CONTABILITA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".tipo_contabilita";
			}else{
				return "tipo_contabilita";
			}
		}
		if(field.equals(SingoloVersamento.model().CODICE_CONTABILITA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".codice_contabilita";
			}else{
				return "codice_contabilita";
			}
		}


		return super.toColumn(field,returnAlias,appendTablePrefix);
		
	}
	
	@Override
	public String toTable(IField field,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(field.equals(SingoloVersamento.model().ID_VERSAMENTO.COD_VERSAMENTO_ENTE)){
			return this.toTable(SingoloVersamento.model().ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(SingoloVersamento.model().ID_VERSAMENTO.ID_APPLICAZIONE.COD_APPLICAZIONE)){
			return this.toTable(SingoloVersamento.model().ID_VERSAMENTO.ID_APPLICAZIONE, returnAlias);
		}
		if(field.equals(SingoloVersamento.model().ID_VERSAMENTO.DEBITORE_IDENTIFICATIVO)){
			return this.toTable(SingoloVersamento.model().ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(SingoloVersamento.model().ID_VERSAMENTO.DEBITORE_ANAGRAFICA)){
			return this.toTable(SingoloVersamento.model().ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(SingoloVersamento.model().ID_VERSAMENTO.COD_VERSAMENTO_LOTTO)){
			return this.toTable(SingoloVersamento.model().ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(SingoloVersamento.model().ID_VERSAMENTO.COD_ANNO_TRIBUTARIO)){
			return this.toTable(SingoloVersamento.model().ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(SingoloVersamento.model().ID_TRIBUTO.ID_DOMINIO.COD_DOMINIO)){
			return this.toTable(SingoloVersamento.model().ID_TRIBUTO.ID_DOMINIO, returnAlias);
		}
		if(field.equals(SingoloVersamento.model().ID_TRIBUTO.COD_TRIBUTO)){
			return this.toTable(SingoloVersamento.model().ID_TRIBUTO, returnAlias);
		}
		if(field.equals(SingoloVersamento.model().COD_SINGOLO_VERSAMENTO_ENTE)){
			return this.toTable(SingoloVersamento.model(), returnAlias);
		}
		if(field.equals(SingoloVersamento.model().STATO_SINGOLO_VERSAMENTO)){
			return this.toTable(SingoloVersamento.model(), returnAlias);
		}
		if(field.equals(SingoloVersamento.model().IMPORTO_SINGOLO_VERSAMENTO)){
			return this.toTable(SingoloVersamento.model(), returnAlias);
		}
		if(field.equals(SingoloVersamento.model().ANNO_RIFERIMENTO)){
			return this.toTable(SingoloVersamento.model(), returnAlias);
		}
		if(field.equals(SingoloVersamento.model().TIPO_BOLLO)){
			return this.toTable(SingoloVersamento.model(), returnAlias);
		}
		if(field.equals(SingoloVersamento.model().HASH_DOCUMENTO)){
			return this.toTable(SingoloVersamento.model(), returnAlias);
		}
		if(field.equals(SingoloVersamento.model().PROVINCIA_RESIDENZA)){
			return this.toTable(SingoloVersamento.model(), returnAlias);
		}
		if(field.equals(SingoloVersamento.model().ID_IBAN_ACCREDITO.COD_IBAN)){
			return this.toTable(SingoloVersamento.model().ID_IBAN_ACCREDITO, returnAlias);
		}
		if(field.equals(SingoloVersamento.model().ID_IBAN_ACCREDITO.ID_DOMINIO.COD_DOMINIO)){
			return this.toTable(SingoloVersamento.model().ID_IBAN_ACCREDITO.ID_DOMINIO, returnAlias);
		}
		if(field.equals(SingoloVersamento.model().TIPO_CONTABILITA)){
			return this.toTable(SingoloVersamento.model(), returnAlias);
		}
		if(field.equals(SingoloVersamento.model().CODICE_CONTABILITA)){
			return this.toTable(SingoloVersamento.model(), returnAlias);
		}


		return super.toTable(field,returnAlias);
		
	}

	@Override
	public String toTable(IModel<?> model,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(model.equals(SingoloVersamento.model())){
			return "singoli_versamenti";
		}
		if(model.equals(SingoloVersamento.model().ID_VERSAMENTO)){
			return "versamenti";
		}
		if(model.equals(SingoloVersamento.model().ID_VERSAMENTO.ID_APPLICAZIONE)){
			return "applicazioni";
		}
		if(model.equals(SingoloVersamento.model().ID_TRIBUTO)){
			return "tributi";
		}
		if(model.equals(SingoloVersamento.model().ID_TRIBUTO.ID_DOMINIO)){
			return "domini";
		}
		if(model.equals(SingoloVersamento.model().ID_IBAN_ACCREDITO)){
			return "iban_accredito";
		}
		if(model.equals(SingoloVersamento.model().ID_IBAN_ACCREDITO.ID_DOMINIO)){
			return "domini";
		}


		return super.toTable(model,returnAlias);
		
	}

}
