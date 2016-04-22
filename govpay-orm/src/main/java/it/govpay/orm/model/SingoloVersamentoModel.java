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
package it.govpay.orm.model;

import it.govpay.orm.SingoloVersamento;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model SingoloVersamento 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class SingoloVersamentoModel extends AbstractModel<SingoloVersamento> {

	public SingoloVersamentoModel(){
	
		super();
	
		this.ID_VERSAMENTO = new it.govpay.orm.model.IdVersamentoModel(new Field("idVersamento",it.govpay.orm.IdVersamento.class,"SingoloVersamento",SingoloVersamento.class));
		this.ID_TRIBUTO = new it.govpay.orm.model.IdTributoModel(new Field("idTributo",it.govpay.orm.IdTributo.class,"SingoloVersamento",SingoloVersamento.class));
		this.COD_SINGOLO_VERSAMENTO_ENTE = new Field("codSingoloVersamentoEnte",java.lang.String.class,"SingoloVersamento",SingoloVersamento.class);
		this.STATO_SINGOLO_VERSAMENTO = new Field("statoSingoloVersamento",java.lang.String.class,"SingoloVersamento",SingoloVersamento.class);
		this.IMPORTO_SINGOLO_VERSAMENTO = new Field("importoSingoloVersamento",double.class,"SingoloVersamento",SingoloVersamento.class);
		this.ANNO_RIFERIMENTO = new Field("annoRiferimento",int.class,"SingoloVersamento",SingoloVersamento.class);
		this.TIPO_BOLLO = new Field("tipoBollo",java.lang.String.class,"SingoloVersamento",SingoloVersamento.class);
		this.HASH_DOCUMENTO = new Field("hashDocumento",java.lang.String.class,"SingoloVersamento",SingoloVersamento.class);
		this.PROVINCIA_RESIDENZA = new Field("provinciaResidenza",java.lang.String.class,"SingoloVersamento",SingoloVersamento.class);
	
	}
	
	public SingoloVersamentoModel(IField father){
	
		super(father);
	
		this.ID_VERSAMENTO = new it.govpay.orm.model.IdVersamentoModel(new ComplexField(father,"idVersamento",it.govpay.orm.IdVersamento.class,"SingoloVersamento",SingoloVersamento.class));
		this.ID_TRIBUTO = new it.govpay.orm.model.IdTributoModel(new ComplexField(father,"idTributo",it.govpay.orm.IdTributo.class,"SingoloVersamento",SingoloVersamento.class));
		this.COD_SINGOLO_VERSAMENTO_ENTE = new ComplexField(father,"codSingoloVersamentoEnte",java.lang.String.class,"SingoloVersamento",SingoloVersamento.class);
		this.STATO_SINGOLO_VERSAMENTO = new ComplexField(father,"statoSingoloVersamento",java.lang.String.class,"SingoloVersamento",SingoloVersamento.class);
		this.IMPORTO_SINGOLO_VERSAMENTO = new ComplexField(father,"importoSingoloVersamento",double.class,"SingoloVersamento",SingoloVersamento.class);
		this.ANNO_RIFERIMENTO = new ComplexField(father,"annoRiferimento",int.class,"SingoloVersamento",SingoloVersamento.class);
		this.TIPO_BOLLO = new ComplexField(father,"tipoBollo",java.lang.String.class,"SingoloVersamento",SingoloVersamento.class);
		this.HASH_DOCUMENTO = new ComplexField(father,"hashDocumento",java.lang.String.class,"SingoloVersamento",SingoloVersamento.class);
		this.PROVINCIA_RESIDENZA = new ComplexField(father,"provinciaResidenza",java.lang.String.class,"SingoloVersamento",SingoloVersamento.class);
	
	}
	
	

	public it.govpay.orm.model.IdVersamentoModel ID_VERSAMENTO = null;
	 
	public it.govpay.orm.model.IdTributoModel ID_TRIBUTO = null;
	 
	public IField COD_SINGOLO_VERSAMENTO_ENTE = null;
	 
	public IField STATO_SINGOLO_VERSAMENTO = null;
	 
	public IField IMPORTO_SINGOLO_VERSAMENTO = null;
	 
	public IField ANNO_RIFERIMENTO = null;
	 
	public IField TIPO_BOLLO = null;
	 
	public IField HASH_DOCUMENTO = null;
	 
	public IField PROVINCIA_RESIDENZA = null;
	 

	@Override
	public Class<SingoloVersamento> getModeledClass(){
		return SingoloVersamento.class;
	}
	
	@Override
	public String toString(){
		if(this.getModeledClass()!=null){
			return this.getModeledClass().getName();
		}else{
			return "N.D.";
		}
	}

}