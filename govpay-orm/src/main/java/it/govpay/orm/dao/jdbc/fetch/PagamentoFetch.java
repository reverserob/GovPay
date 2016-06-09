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

import it.govpay.orm.Pagamento;


/**     
 * PagamentoFetch
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class PagamentoFetch extends AbstractJDBCFetch {

	@Override
	public Object fetch(TipiDatabase tipoDatabase, IModel<?> model , ResultSet rs) throws ServiceException {
		
		try{
			JDBCParameterUtilities jdbcParameterUtilities =  
					new JDBCParameterUtilities(tipoDatabase);

			if(model.equals(Pagamento.model())){
				Pagamento object = new Pagamento();
				setParameter(object, "setId", Long.class,
					jdbcParameterUtilities.readParameter(rs, "id", Long.class));
				setParameter(object, "setCodSingoloVersamentoEnte", Pagamento.model().COD_SINGOLO_VERSAMENTO_ENTE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_singolo_versamento_ente", Pagamento.model().COD_SINGOLO_VERSAMENTO_ENTE.getFieldType()));
				setParameter(object, "setImportoPagato", Pagamento.model().IMPORTO_PAGATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "importo_pagato", Pagamento.model().IMPORTO_PAGATO.getFieldType()));
				setParameter(object, "setDataAcquisizione", Pagamento.model().DATA_ACQUISIZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_acquisizione", Pagamento.model().DATA_ACQUISIZIONE.getFieldType()));
				setParameter(object, "setIur", Pagamento.model().IUR.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "iur", Pagamento.model().IUR.getFieldType()));
				setParameter(object, "setDataPagamento", Pagamento.model().DATA_PAGAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_pagamento", Pagamento.model().DATA_PAGAMENTO.getFieldType()));
				setParameter(object, "setCommissioniPsp", Pagamento.model().COMMISSIONI_PSP.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "commissioni_psp", Pagamento.model().COMMISSIONI_PSP.getFieldType()));
				setParameter(object, "setTipoAllegato", Pagamento.model().TIPO_ALLEGATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "tipo_allegato", Pagamento.model().TIPO_ALLEGATO.getFieldType()));
				setParameter(object, "setAllegato", Pagamento.model().ALLEGATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "allegato", Pagamento.model().ALLEGATO.getFieldType()));
				setParameter(object, "setRendicontazioneEsito", Pagamento.model().RENDICONTAZIONE_ESITO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "rendicontazione_esito", Pagamento.model().RENDICONTAZIONE_ESITO.getFieldType()));
				setParameter(object, "setRendicontazioneData", Pagamento.model().RENDICONTAZIONE_DATA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "rendicontazione_data", Pagamento.model().RENDICONTAZIONE_DATA.getFieldType()));
				setParameter(object, "setCodflussoRendicontazione", Pagamento.model().CODFLUSSO_RENDICONTAZIONE.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "codflusso_rendicontazione", Pagamento.model().CODFLUSSO_RENDICONTAZIONE.getFieldType()));
				setParameter(object, "setAnnoRiferimento", Pagamento.model().ANNO_RIFERIMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "anno_riferimento", Pagamento.model().ANNO_RIFERIMENTO.getFieldType()));
				setParameter(object, "setIndiceSingoloPagamento", Pagamento.model().INDICE_SINGOLO_PAGAMENTO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "indice_singolo_pagamento", Pagamento.model().INDICE_SINGOLO_PAGAMENTO.getFieldType()));
				setParameter(object, "setDataAcquisizioneRevoca", Pagamento.model().DATA_ACQUISIZIONE_REVOCA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "data_acquisizione_revoca", Pagamento.model().DATA_ACQUISIZIONE_REVOCA.getFieldType()));
				setParameter(object, "setCausaleRevoca", Pagamento.model().CAUSALE_REVOCA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "causale_revoca", Pagamento.model().CAUSALE_REVOCA.getFieldType()));
				setParameter(object, "setDatiRevoca", Pagamento.model().DATI_REVOCA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "dati_revoca", Pagamento.model().DATI_REVOCA.getFieldType()));
				setParameter(object, "setImportoRevocato", Pagamento.model().IMPORTO_REVOCATO.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "importo_revocato", Pagamento.model().IMPORTO_REVOCATO.getFieldType()));
				setParameter(object, "setEsitoRevoca", Pagamento.model().ESITO_REVOCA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "esito_revoca", Pagamento.model().ESITO_REVOCA.getFieldType()));
				setParameter(object, "setDatiEsitoRevoca", Pagamento.model().DATI_ESITO_REVOCA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "dati_esito_revoca", Pagamento.model().DATI_ESITO_REVOCA.getFieldType()));
				setParameter(object, "setRendicontazioneEsitoRevoca", Pagamento.model().RENDICONTAZIONE_ESITO_REVOCA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "rendicontazione_esito_revoca", Pagamento.model().RENDICONTAZIONE_ESITO_REVOCA.getFieldType()));
				setParameter(object, "setRendicontazioneDataRevoca", Pagamento.model().RENDICONTAZIONE_DATA_REVOCA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "rendicontazione_data_revoca", Pagamento.model().RENDICONTAZIONE_DATA_REVOCA.getFieldType()));
				setParameter(object, "setCodFlussoRendicontazioneRevoca", Pagamento.model().COD_FLUSSO_RENDICONTAZIONE_REVOCA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "cod_flusso_rendicontaz_revoca", Pagamento.model().COD_FLUSSO_RENDICONTAZIONE_REVOCA.getFieldType()));
				setParameter(object, "setAnnoRiferimentoRevoca", Pagamento.model().ANNO_RIFERIMENTO_REVOCA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "anno_riferimento_revoca", Pagamento.model().ANNO_RIFERIMENTO_REVOCA.getFieldType()));
				setParameter(object, "setIndiceSingoloPagamentoRevoca", Pagamento.model().INDICE_SINGOLO_PAGAMENTO_REVOCA.getFieldType(),
					jdbcParameterUtilities.readParameter(rs, "ind_singolo_pagamento_revoca", Pagamento.model().INDICE_SINGOLO_PAGAMENTO_REVOCA.getFieldType()));
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

			if(model.equals(Pagamento.model())){
				Pagamento object = new Pagamento();
				setParameter(object, "setId", Long.class,
					this.getObjectFromMap(map,"id"));
				setParameter(object, "setCodSingoloVersamentoEnte", Pagamento.model().COD_SINGOLO_VERSAMENTO_ENTE.getFieldType(),
					this.getObjectFromMap(map,"codSingoloVersamentoEnte"));
				setParameter(object, "setImportoPagato", Pagamento.model().IMPORTO_PAGATO.getFieldType(),
					this.getObjectFromMap(map,"importoPagato"));
				setParameter(object, "setDataAcquisizione", Pagamento.model().DATA_ACQUISIZIONE.getFieldType(),
					this.getObjectFromMap(map,"dataAcquisizione"));
				setParameter(object, "setIur", Pagamento.model().IUR.getFieldType(),
					this.getObjectFromMap(map,"iur"));
				setParameter(object, "setDataPagamento", Pagamento.model().DATA_PAGAMENTO.getFieldType(),
					this.getObjectFromMap(map,"dataPagamento"));
				setParameter(object, "setCommissioniPsp", Pagamento.model().COMMISSIONI_PSP.getFieldType(),
					this.getObjectFromMap(map,"commissioniPsp"));
				setParameter(object, "setTipoAllegato", Pagamento.model().TIPO_ALLEGATO.getFieldType(),
					this.getObjectFromMap(map,"tipoAllegato"));
				setParameter(object, "setAllegato", Pagamento.model().ALLEGATO.getFieldType(),
					this.getObjectFromMap(map,"allegato"));
				setParameter(object, "setRendicontazioneEsito", Pagamento.model().RENDICONTAZIONE_ESITO.getFieldType(),
					this.getObjectFromMap(map,"rendicontazioneEsito"));
				setParameter(object, "setRendicontazioneData", Pagamento.model().RENDICONTAZIONE_DATA.getFieldType(),
					this.getObjectFromMap(map,"rendicontazioneData"));
				setParameter(object, "setCodflussoRendicontazione", Pagamento.model().CODFLUSSO_RENDICONTAZIONE.getFieldType(),
					this.getObjectFromMap(map,"codflussoRendicontazione"));
				setParameter(object, "setAnnoRiferimento", Pagamento.model().ANNO_RIFERIMENTO.getFieldType(),
					this.getObjectFromMap(map,"annoRiferimento"));
				setParameter(object, "setIndiceSingoloPagamento", Pagamento.model().INDICE_SINGOLO_PAGAMENTO.getFieldType(),
					this.getObjectFromMap(map,"indiceSingoloPagamento"));
				setParameter(object, "setDataAcquisizioneRevoca", Pagamento.model().DATA_ACQUISIZIONE_REVOCA.getFieldType(),
					this.getObjectFromMap(map,"dataAcquisizioneRevoca"));
				setParameter(object, "setCausaleRevoca", Pagamento.model().CAUSALE_REVOCA.getFieldType(),
					this.getObjectFromMap(map,"causaleRevoca"));
				setParameter(object, "setDatiRevoca", Pagamento.model().DATI_REVOCA.getFieldType(),
					this.getObjectFromMap(map,"datiRevoca"));
				setParameter(object, "setImportoRevocato", Pagamento.model().IMPORTO_REVOCATO.getFieldType(),
					this.getObjectFromMap(map,"importoRevocato"));
				setParameter(object, "setEsitoRevoca", Pagamento.model().ESITO_REVOCA.getFieldType(),
					this.getObjectFromMap(map,"esitoRevoca"));
				setParameter(object, "setDatiEsitoRevoca", Pagamento.model().DATI_ESITO_REVOCA.getFieldType(),
					this.getObjectFromMap(map,"datiEsitoRevoca"));
				setParameter(object, "setRendicontazioneEsitoRevoca", Pagamento.model().RENDICONTAZIONE_ESITO_REVOCA.getFieldType(),
					this.getObjectFromMap(map,"rendicontazioneEsitoRevoca"));
				setParameter(object, "setRendicontazioneDataRevoca", Pagamento.model().RENDICONTAZIONE_DATA_REVOCA.getFieldType(),
					this.getObjectFromMap(map,"rendicontazioneDataRevoca"));
				setParameter(object, "setCodFlussoRendicontazioneRevoca", Pagamento.model().COD_FLUSSO_RENDICONTAZIONE_REVOCA.getFieldType(),
					this.getObjectFromMap(map,"codFlussoRendicontazioneRevoca"));
				setParameter(object, "setAnnoRiferimentoRevoca", Pagamento.model().ANNO_RIFERIMENTO_REVOCA.getFieldType(),
					this.getObjectFromMap(map,"annoRiferimentoRevoca"));
				setParameter(object, "setIndiceSingoloPagamentoRevoca", Pagamento.model().INDICE_SINGOLO_PAGAMENTO_REVOCA.getFieldType(),
					this.getObjectFromMap(map,"indiceSingoloPagamentoRevoca"));
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

			if(model.equals(Pagamento.model())){
				return new org.openspcoop2.utils.jdbc.CustomKeyGeneratorObject("pagamenti","id","seq_pagamenti","pagamenti_init_seq");
			}
			
			else{
				throw new ServiceException("Model ["+model.toString()+"] not supported by getKeyGeneratorObject: "+this.getClass().getName());
			}

		}catch(Exception e){
			throw new ServiceException("Model ["+model.toString()+"] occurs error in getKeyGeneratorObject: "+e.getMessage(),e);
		}
		
	}

}
