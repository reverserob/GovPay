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
package it.govpay.orm.dao.jdbc.fetch;

import org.openspcoop2.generic_project.beans.IModel;
import org.openspcoop2.generic_project.dao.jdbc.utils.AbstractJDBCFetch;
import org.openspcoop2.generic_project.dao.jdbc.utils.JDBCParameterUtilities;
import org.openspcoop2.generic_project.exception.ServiceException;

import java.sql.ResultSet;
import java.util.Map;

import org.openspcoop2.utils.TipiDatabase;
import org.openspcoop2.utils.jdbc.IKeyGeneratorObject;

import it.govpay.orm.OperatorePortale;
import it.govpay.orm.OperatoreUo;
import it.govpay.orm.Operatore;
import it.govpay.orm.OperatoreApplicazione;


/**     
 * OperatoreFetch
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class OperatoreFetch extends AbstractJDBCFetch {

	@Override
	public Object fetch(TipiDatabase tipoDatabase, IModel<?> model , ResultSet rs) throws ServiceException {
		
		try{
			JDBCParameterUtilities jdbcParameterUtilities =  
					new JDBCParameterUtilities(tipoDatabase);

			if(model.equals(Operatore.model())){
				Operatore object = new Operatore();
				setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				setParameter(object, "setPrincipal", Operatore.model().PRINCIPAL.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "principal", Operatore.model().PRINCIPAL.getFieldType()));
				setParameter(object, "setNome", Operatore.model().NOME.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "nome", Operatore.model().NOME.getFieldType()));
				setParameter(object, "setProfilo", Operatore.model().PROFILO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "profilo", Operatore.model().PROFILO.getFieldType()));
				setParameter(object, "setAbilitato", Operatore.model().ABILITATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "abilitato", Operatore.model().ABILITATO.getFieldType()));
				return object;
			}
			if(model.equals(Operatore.model().OPERATORE_UO)){
				OperatoreUo object = new OperatoreUo();
				setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				return object;
			}
			if(model.equals(Operatore.model().OPERATORE_APPLICAZIONE)){
				OperatoreApplicazione object = new OperatoreApplicazione();
				setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				return object;
			}
			if(model.equals(Operatore.model().OPERATORE_PORTALE)){
				OperatorePortale object = new OperatorePortale();
				setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				return object;
			}
			
			else{
				throw new ServiceException("Model ["+model.toString()+"] not supported by fetch: "+this.getClass().getName());
			}	
					
		}catch(Exception e){
			throw new ServiceException("Model ["+model.toString()+"] occurs error in fetch: "+e.getMessage(),e);
		}
		
	}
	
	@Override
	public Object fetch(TipiDatabase tipoDatabase, IModel<?> model , Map<String,Object> map ) throws ServiceException {
		
		try{

			if(model.equals(Operatore.model())){
				Operatore object = new Operatore();
				setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"id"));
				setParameter(object, "setPrincipal", Operatore.model().PRINCIPAL.getFieldType(),
					this.getObjectFromMap(map,"principal"));
				setParameter(object, "setNome", Operatore.model().NOME.getFieldType(),
					this.getObjectFromMap(map,"nome"));
				setParameter(object, "setProfilo", Operatore.model().PROFILO.getFieldType(),
					this.getObjectFromMap(map,"profilo"));
				setParameter(object, "setAbilitato", Operatore.model().ABILITATO.getFieldType(),
					this.getObjectFromMap(map,"abilitato"));
				return object;
			}
			if(model.equals(Operatore.model().OPERATORE_UO)){
				OperatoreUo object = new OperatoreUo();
				setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"OperatoreUo.id"));
				return object;
			}
			if(model.equals(Operatore.model().OPERATORE_APPLICAZIONE)){
				OperatoreApplicazione object = new OperatoreApplicazione();
				setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"OperatoreApplicazione.id"));
				return object;
			}
			if(model.equals(Operatore.model().OPERATORE_PORTALE)){
				OperatorePortale object = new OperatorePortale();
				setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"OperatorePortale.id"));
				return object;
			}
			
			else{
				throw new ServiceException("Model ["+model.toString()+"] not supported by fetch: "+this.getClass().getName());
			}	
					
		}catch(Exception e){
			throw new ServiceException("Model ["+model.toString()+"] occurs error in fetch: "+e.getMessage(),e);
		}
		
	}
	
	
	@Override
	public IKeyGeneratorObject getKeyGeneratorObject( IModel<?> model )  throws ServiceException {
		
		try{

			if(model.equals(Operatore.model())){
				return new org.openspcoop2.utils.jdbc.CustomKeyGeneratorObject("operatori","id","seq_operatori","operatori_init_seq");
			}
			if(model.equals(Operatore.model().OPERATORE_UO)){
				return new org.openspcoop2.utils.jdbc.CustomKeyGeneratorObject("operatori_uo","id","seq_operatori_uo","operatori_uo_init_seq");
			}
			if(model.equals(Operatore.model().OPERATORE_APPLICAZIONE)){
				return new org.openspcoop2.utils.jdbc.CustomKeyGeneratorObject("operatori_applicazioni","id","seq_operatori_applicazioni","operatori_applicazioni_init_seq");
			}
			if(model.equals(Operatore.model().OPERATORE_PORTALE)){
				return new org.openspcoop2.utils.jdbc.CustomKeyGeneratorObject("operatori_portali","id","seq_operatori_portali","operatori_portali_init_seq");
			}
			
			else{
				throw new ServiceException("Model ["+model.toString()+"] not supported by getKeyGeneratorObject: "+this.getClass().getName());
			}

		}catch(Exception e){
			throw new ServiceException("Model ["+model.toString()+"] occurs error in getKeyGeneratorObject: "+e.getMessage(),e);
		}
		
	}

}
