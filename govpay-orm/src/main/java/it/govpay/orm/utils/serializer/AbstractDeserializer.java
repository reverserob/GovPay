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
package it.govpay.orm.utils.serializer;

import org.openspcoop2.generic_project.exception.DeserializerException;

import it.govpay.orm.IdDominio;
import it.govpay.orm.ApplicazioneDominio;
import it.govpay.orm.Versamento;
import it.govpay.orm.IdUo;
import it.govpay.orm.IdApplicazione;
import it.govpay.orm.Psp;
import it.govpay.orm.IdVersamento;
import it.govpay.orm.IdIntermediario;
import it.govpay.orm.Uo;
import it.govpay.orm.IdSingolaRendicontazione;
import it.govpay.orm.IdPsp;
import it.govpay.orm.IdOperatore;
import it.govpay.orm.IdSla;
import it.govpay.orm.IdMediaRilevamento;
import it.govpay.orm.IdSingoloVersamento;
import it.govpay.orm.IdTributo;
import it.govpay.orm.IdIuv;
import it.govpay.orm.SingoloVersamento;
import it.govpay.orm.IdIbanAccredito;
import it.govpay.orm.FR;
import it.govpay.orm.IdMail;
import it.govpay.orm.IdPortale;
import it.govpay.orm.IdFrApplicazione;
import it.govpay.orm.RendicontazioneSenzaRPT;
import it.govpay.orm.IdFr;
import it.govpay.orm.Portale;
import it.govpay.orm.PortaleApplicazione;
import it.govpay.orm.IdTabellaControparti;
import it.govpay.orm.IdRpt;
import it.govpay.orm.RR;
import it.govpay.orm.IdMessaggio;
import it.govpay.orm.ApplicazioneTributo;
import it.govpay.orm.IdRr;
import it.govpay.orm.IdCarrello;
import it.govpay.orm.Operatore;
import it.govpay.orm.OperatoreUo;
import it.govpay.orm.OperatoreApplicazione;
import it.govpay.orm.OperatorePortale;
import it.govpay.orm.Intermediario;
import it.govpay.orm.IdTracciato;
import it.govpay.orm.IdStazione;
import it.govpay.orm.RPT;
import it.govpay.orm.IdCanale;
import it.govpay.orm.IdPagamento;
import it.govpay.orm.Dominio;
import it.govpay.orm.FrApplicazione;
import it.govpay.orm.IdMailTemplate;
import it.govpay.orm.Applicazione;
import it.govpay.orm.Canale;
import it.govpay.orm.IdNotifica;
import it.govpay.orm.IdEr;
import it.govpay.orm.IdSingolaRevoca;
import it.govpay.orm.IdContoAccredito;
import it.govpay.orm.Stazione;
import it.govpay.orm.IdRilevamento;
import it.govpay.orm.Pagamento;
import it.govpay.orm.Notifica;
import it.govpay.orm.IUV;
import it.govpay.orm.IdAnagrafica;
import it.govpay.orm.IbanAccredito;
import it.govpay.orm.Connettore;
import it.govpay.orm.Tributo;
import it.govpay.orm.IdConnettore;
import it.govpay.orm.IdEvento;
import it.govpay.orm.Evento;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.File;

/**     
 * XML Deserializer of beans
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */

public abstract class AbstractDeserializer {



	protected abstract Object _xmlToObj(InputStream is, Class<?> c) throws Exception;
	
	private Object xmlToObj(InputStream is,Class<?> c) throws DeserializerException{
		try{
			return this._xmlToObj(is, c);
		}catch(Exception e){
			throw new DeserializerException(e.getMessage(), e);
		}
	}
	private Object xmlToObj(String fileName,Class<?> c) throws DeserializerException{
		try{
			return this.xmlToObj(new File(fileName), c);
		}catch(Exception e){
			throw new DeserializerException(e.getMessage(), e);
		}
	}
	private Object xmlToObj(File file,Class<?> c) throws DeserializerException{
		FileInputStream fin = null;
		try{
			fin = new FileInputStream(file);
			return this._xmlToObj(fin, c);
		}catch(Exception e){
			throw new DeserializerException(e.getMessage(),e);
		}finally{
			try{
				fin.close();
			}catch(Exception e){}
		}
	}
	private Object xmlToObj(byte[] file,Class<?> c) throws DeserializerException{
		ByteArrayInputStream fin = null;
		try{
			fin = new ByteArrayInputStream(file);
			return this._xmlToObj(fin, c);
		}catch(Exception e){
			throw new DeserializerException(e.getMessage(),e);
		}finally{
			try{
				fin.close();
			}catch(Exception e){}
		}
	}






	/*
	 =================================================================================
	 Object: id-dominio
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdDominio}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdDominio}
	 * @return Object type {@link it.govpay.orm.IdDominio}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdDominio readIdDominio(String fileName) throws DeserializerException {
		return (IdDominio) this.xmlToObj(fileName, IdDominio.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdDominio}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdDominio}
	 * @return Object type {@link it.govpay.orm.IdDominio}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdDominio readIdDominio(File file) throws DeserializerException {
		return (IdDominio) this.xmlToObj(file, IdDominio.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdDominio}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdDominio}
	 * @return Object type {@link it.govpay.orm.IdDominio}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdDominio readIdDominio(InputStream in) throws DeserializerException {
		return (IdDominio) this.xmlToObj(in, IdDominio.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdDominio}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdDominio}
	 * @return Object type {@link it.govpay.orm.IdDominio}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdDominio readIdDominio(byte[] in) throws DeserializerException {
		return (IdDominio) this.xmlToObj(in, IdDominio.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdDominio}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdDominio}
	 * @return Object type {@link it.govpay.orm.IdDominio}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdDominio readIdDominioFromString(String in) throws DeserializerException {
		return (IdDominio) this.xmlToObj(in.getBytes(), IdDominio.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: ApplicazioneDominio
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.ApplicazioneDominio}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.ApplicazioneDominio}
	 * @return Object type {@link it.govpay.orm.ApplicazioneDominio}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public ApplicazioneDominio readApplicazioneDominio(String fileName) throws DeserializerException {
		return (ApplicazioneDominio) this.xmlToObj(fileName, ApplicazioneDominio.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.ApplicazioneDominio}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.ApplicazioneDominio}
	 * @return Object type {@link it.govpay.orm.ApplicazioneDominio}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public ApplicazioneDominio readApplicazioneDominio(File file) throws DeserializerException {
		return (ApplicazioneDominio) this.xmlToObj(file, ApplicazioneDominio.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.ApplicazioneDominio}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.ApplicazioneDominio}
	 * @return Object type {@link it.govpay.orm.ApplicazioneDominio}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public ApplicazioneDominio readApplicazioneDominio(InputStream in) throws DeserializerException {
		return (ApplicazioneDominio) this.xmlToObj(in, ApplicazioneDominio.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.ApplicazioneDominio}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.ApplicazioneDominio}
	 * @return Object type {@link it.govpay.orm.ApplicazioneDominio}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public ApplicazioneDominio readApplicazioneDominio(byte[] in) throws DeserializerException {
		return (ApplicazioneDominio) this.xmlToObj(in, ApplicazioneDominio.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.ApplicazioneDominio}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.ApplicazioneDominio}
	 * @return Object type {@link it.govpay.orm.ApplicazioneDominio}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public ApplicazioneDominio readApplicazioneDominioFromString(String in) throws DeserializerException {
		return (ApplicazioneDominio) this.xmlToObj(in.getBytes(), ApplicazioneDominio.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: Versamento
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.Versamento}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Versamento}
	 * @return Object type {@link it.govpay.orm.Versamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Versamento readVersamento(String fileName) throws DeserializerException {
		return (Versamento) this.xmlToObj(fileName, Versamento.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.Versamento}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Versamento}
	 * @return Object type {@link it.govpay.orm.Versamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Versamento readVersamento(File file) throws DeserializerException {
		return (Versamento) this.xmlToObj(file, Versamento.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.Versamento}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.Versamento}
	 * @return Object type {@link it.govpay.orm.Versamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Versamento readVersamento(InputStream in) throws DeserializerException {
		return (Versamento) this.xmlToObj(in, Versamento.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.Versamento}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.Versamento}
	 * @return Object type {@link it.govpay.orm.Versamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Versamento readVersamento(byte[] in) throws DeserializerException {
		return (Versamento) this.xmlToObj(in, Versamento.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.Versamento}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.Versamento}
	 * @return Object type {@link it.govpay.orm.Versamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Versamento readVersamentoFromString(String in) throws DeserializerException {
		return (Versamento) this.xmlToObj(in.getBytes(), Versamento.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: id-uo
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdUo}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdUo}
	 * @return Object type {@link it.govpay.orm.IdUo}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdUo readIdUo(String fileName) throws DeserializerException {
		return (IdUo) this.xmlToObj(fileName, IdUo.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdUo}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdUo}
	 * @return Object type {@link it.govpay.orm.IdUo}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdUo readIdUo(File file) throws DeserializerException {
		return (IdUo) this.xmlToObj(file, IdUo.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdUo}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdUo}
	 * @return Object type {@link it.govpay.orm.IdUo}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdUo readIdUo(InputStream in) throws DeserializerException {
		return (IdUo) this.xmlToObj(in, IdUo.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdUo}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdUo}
	 * @return Object type {@link it.govpay.orm.IdUo}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdUo readIdUo(byte[] in) throws DeserializerException {
		return (IdUo) this.xmlToObj(in, IdUo.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdUo}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdUo}
	 * @return Object type {@link it.govpay.orm.IdUo}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdUo readIdUoFromString(String in) throws DeserializerException {
		return (IdUo) this.xmlToObj(in.getBytes(), IdUo.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: id-applicazione
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdApplicazione}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdApplicazione}
	 * @return Object type {@link it.govpay.orm.IdApplicazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdApplicazione readIdApplicazione(String fileName) throws DeserializerException {
		return (IdApplicazione) this.xmlToObj(fileName, IdApplicazione.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdApplicazione}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdApplicazione}
	 * @return Object type {@link it.govpay.orm.IdApplicazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdApplicazione readIdApplicazione(File file) throws DeserializerException {
		return (IdApplicazione) this.xmlToObj(file, IdApplicazione.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdApplicazione}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdApplicazione}
	 * @return Object type {@link it.govpay.orm.IdApplicazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdApplicazione readIdApplicazione(InputStream in) throws DeserializerException {
		return (IdApplicazione) this.xmlToObj(in, IdApplicazione.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdApplicazione}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdApplicazione}
	 * @return Object type {@link it.govpay.orm.IdApplicazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdApplicazione readIdApplicazione(byte[] in) throws DeserializerException {
		return (IdApplicazione) this.xmlToObj(in, IdApplicazione.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdApplicazione}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdApplicazione}
	 * @return Object type {@link it.govpay.orm.IdApplicazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdApplicazione readIdApplicazioneFromString(String in) throws DeserializerException {
		return (IdApplicazione) this.xmlToObj(in.getBytes(), IdApplicazione.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: Psp
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.Psp}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Psp}
	 * @return Object type {@link it.govpay.orm.Psp}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Psp readPsp(String fileName) throws DeserializerException {
		return (Psp) this.xmlToObj(fileName, Psp.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.Psp}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Psp}
	 * @return Object type {@link it.govpay.orm.Psp}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Psp readPsp(File file) throws DeserializerException {
		return (Psp) this.xmlToObj(file, Psp.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.Psp}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.Psp}
	 * @return Object type {@link it.govpay.orm.Psp}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Psp readPsp(InputStream in) throws DeserializerException {
		return (Psp) this.xmlToObj(in, Psp.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.Psp}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.Psp}
	 * @return Object type {@link it.govpay.orm.Psp}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Psp readPsp(byte[] in) throws DeserializerException {
		return (Psp) this.xmlToObj(in, Psp.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.Psp}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.Psp}
	 * @return Object type {@link it.govpay.orm.Psp}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Psp readPspFromString(String in) throws DeserializerException {
		return (Psp) this.xmlToObj(in.getBytes(), Psp.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: id-versamento
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdVersamento}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdVersamento}
	 * @return Object type {@link it.govpay.orm.IdVersamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdVersamento readIdVersamento(String fileName) throws DeserializerException {
		return (IdVersamento) this.xmlToObj(fileName, IdVersamento.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdVersamento}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdVersamento}
	 * @return Object type {@link it.govpay.orm.IdVersamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdVersamento readIdVersamento(File file) throws DeserializerException {
		return (IdVersamento) this.xmlToObj(file, IdVersamento.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdVersamento}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdVersamento}
	 * @return Object type {@link it.govpay.orm.IdVersamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdVersamento readIdVersamento(InputStream in) throws DeserializerException {
		return (IdVersamento) this.xmlToObj(in, IdVersamento.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdVersamento}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdVersamento}
	 * @return Object type {@link it.govpay.orm.IdVersamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdVersamento readIdVersamento(byte[] in) throws DeserializerException {
		return (IdVersamento) this.xmlToObj(in, IdVersamento.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdVersamento}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdVersamento}
	 * @return Object type {@link it.govpay.orm.IdVersamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdVersamento readIdVersamentoFromString(String in) throws DeserializerException {
		return (IdVersamento) this.xmlToObj(in.getBytes(), IdVersamento.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: id-intermediario
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdIntermediario}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdIntermediario}
	 * @return Object type {@link it.govpay.orm.IdIntermediario}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdIntermediario readIdIntermediario(String fileName) throws DeserializerException {
		return (IdIntermediario) this.xmlToObj(fileName, IdIntermediario.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdIntermediario}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdIntermediario}
	 * @return Object type {@link it.govpay.orm.IdIntermediario}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdIntermediario readIdIntermediario(File file) throws DeserializerException {
		return (IdIntermediario) this.xmlToObj(file, IdIntermediario.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdIntermediario}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdIntermediario}
	 * @return Object type {@link it.govpay.orm.IdIntermediario}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdIntermediario readIdIntermediario(InputStream in) throws DeserializerException {
		return (IdIntermediario) this.xmlToObj(in, IdIntermediario.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdIntermediario}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdIntermediario}
	 * @return Object type {@link it.govpay.orm.IdIntermediario}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdIntermediario readIdIntermediario(byte[] in) throws DeserializerException {
		return (IdIntermediario) this.xmlToObj(in, IdIntermediario.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdIntermediario}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdIntermediario}
	 * @return Object type {@link it.govpay.orm.IdIntermediario}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdIntermediario readIdIntermediarioFromString(String in) throws DeserializerException {
		return (IdIntermediario) this.xmlToObj(in.getBytes(), IdIntermediario.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: Uo
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.Uo}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Uo}
	 * @return Object type {@link it.govpay.orm.Uo}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Uo readUo(String fileName) throws DeserializerException {
		return (Uo) this.xmlToObj(fileName, Uo.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.Uo}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Uo}
	 * @return Object type {@link it.govpay.orm.Uo}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Uo readUo(File file) throws DeserializerException {
		return (Uo) this.xmlToObj(file, Uo.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.Uo}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.Uo}
	 * @return Object type {@link it.govpay.orm.Uo}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Uo readUo(InputStream in) throws DeserializerException {
		return (Uo) this.xmlToObj(in, Uo.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.Uo}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.Uo}
	 * @return Object type {@link it.govpay.orm.Uo}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Uo readUo(byte[] in) throws DeserializerException {
		return (Uo) this.xmlToObj(in, Uo.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.Uo}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.Uo}
	 * @return Object type {@link it.govpay.orm.Uo}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Uo readUoFromString(String in) throws DeserializerException {
		return (Uo) this.xmlToObj(in.getBytes(), Uo.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: id-singola-rendicontazione
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdSingolaRendicontazione}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdSingolaRendicontazione}
	 * @return Object type {@link it.govpay.orm.IdSingolaRendicontazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdSingolaRendicontazione readIdSingolaRendicontazione(String fileName) throws DeserializerException {
		return (IdSingolaRendicontazione) this.xmlToObj(fileName, IdSingolaRendicontazione.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdSingolaRendicontazione}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdSingolaRendicontazione}
	 * @return Object type {@link it.govpay.orm.IdSingolaRendicontazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdSingolaRendicontazione readIdSingolaRendicontazione(File file) throws DeserializerException {
		return (IdSingolaRendicontazione) this.xmlToObj(file, IdSingolaRendicontazione.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdSingolaRendicontazione}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdSingolaRendicontazione}
	 * @return Object type {@link it.govpay.orm.IdSingolaRendicontazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdSingolaRendicontazione readIdSingolaRendicontazione(InputStream in) throws DeserializerException {
		return (IdSingolaRendicontazione) this.xmlToObj(in, IdSingolaRendicontazione.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdSingolaRendicontazione}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdSingolaRendicontazione}
	 * @return Object type {@link it.govpay.orm.IdSingolaRendicontazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdSingolaRendicontazione readIdSingolaRendicontazione(byte[] in) throws DeserializerException {
		return (IdSingolaRendicontazione) this.xmlToObj(in, IdSingolaRendicontazione.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdSingolaRendicontazione}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdSingolaRendicontazione}
	 * @return Object type {@link it.govpay.orm.IdSingolaRendicontazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdSingolaRendicontazione readIdSingolaRendicontazioneFromString(String in) throws DeserializerException {
		return (IdSingolaRendicontazione) this.xmlToObj(in.getBytes(), IdSingolaRendicontazione.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: id-psp
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdPsp}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdPsp}
	 * @return Object type {@link it.govpay.orm.IdPsp}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdPsp readIdPsp(String fileName) throws DeserializerException {
		return (IdPsp) this.xmlToObj(fileName, IdPsp.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdPsp}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdPsp}
	 * @return Object type {@link it.govpay.orm.IdPsp}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdPsp readIdPsp(File file) throws DeserializerException {
		return (IdPsp) this.xmlToObj(file, IdPsp.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdPsp}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdPsp}
	 * @return Object type {@link it.govpay.orm.IdPsp}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdPsp readIdPsp(InputStream in) throws DeserializerException {
		return (IdPsp) this.xmlToObj(in, IdPsp.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdPsp}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdPsp}
	 * @return Object type {@link it.govpay.orm.IdPsp}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdPsp readIdPsp(byte[] in) throws DeserializerException {
		return (IdPsp) this.xmlToObj(in, IdPsp.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdPsp}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdPsp}
	 * @return Object type {@link it.govpay.orm.IdPsp}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdPsp readIdPspFromString(String in) throws DeserializerException {
		return (IdPsp) this.xmlToObj(in.getBytes(), IdPsp.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: id-operatore
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdOperatore}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdOperatore}
	 * @return Object type {@link it.govpay.orm.IdOperatore}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdOperatore readIdOperatore(String fileName) throws DeserializerException {
		return (IdOperatore) this.xmlToObj(fileName, IdOperatore.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdOperatore}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdOperatore}
	 * @return Object type {@link it.govpay.orm.IdOperatore}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdOperatore readIdOperatore(File file) throws DeserializerException {
		return (IdOperatore) this.xmlToObj(file, IdOperatore.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdOperatore}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdOperatore}
	 * @return Object type {@link it.govpay.orm.IdOperatore}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdOperatore readIdOperatore(InputStream in) throws DeserializerException {
		return (IdOperatore) this.xmlToObj(in, IdOperatore.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdOperatore}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdOperatore}
	 * @return Object type {@link it.govpay.orm.IdOperatore}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdOperatore readIdOperatore(byte[] in) throws DeserializerException {
		return (IdOperatore) this.xmlToObj(in, IdOperatore.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdOperatore}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdOperatore}
	 * @return Object type {@link it.govpay.orm.IdOperatore}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdOperatore readIdOperatoreFromString(String in) throws DeserializerException {
		return (IdOperatore) this.xmlToObj(in.getBytes(), IdOperatore.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: id-sla
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdSla}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdSla}
	 * @return Object type {@link it.govpay.orm.IdSla}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdSla readIdSla(String fileName) throws DeserializerException {
		return (IdSla) this.xmlToObj(fileName, IdSla.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdSla}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdSla}
	 * @return Object type {@link it.govpay.orm.IdSla}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdSla readIdSla(File file) throws DeserializerException {
		return (IdSla) this.xmlToObj(file, IdSla.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdSla}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdSla}
	 * @return Object type {@link it.govpay.orm.IdSla}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdSla readIdSla(InputStream in) throws DeserializerException {
		return (IdSla) this.xmlToObj(in, IdSla.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdSla}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdSla}
	 * @return Object type {@link it.govpay.orm.IdSla}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdSla readIdSla(byte[] in) throws DeserializerException {
		return (IdSla) this.xmlToObj(in, IdSla.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdSla}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdSla}
	 * @return Object type {@link it.govpay.orm.IdSla}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdSla readIdSlaFromString(String in) throws DeserializerException {
		return (IdSla) this.xmlToObj(in.getBytes(), IdSla.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: id-media-rilevamento
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdMediaRilevamento}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdMediaRilevamento}
	 * @return Object type {@link it.govpay.orm.IdMediaRilevamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdMediaRilevamento readIdMediaRilevamento(String fileName) throws DeserializerException {
		return (IdMediaRilevamento) this.xmlToObj(fileName, IdMediaRilevamento.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdMediaRilevamento}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdMediaRilevamento}
	 * @return Object type {@link it.govpay.orm.IdMediaRilevamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdMediaRilevamento readIdMediaRilevamento(File file) throws DeserializerException {
		return (IdMediaRilevamento) this.xmlToObj(file, IdMediaRilevamento.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdMediaRilevamento}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdMediaRilevamento}
	 * @return Object type {@link it.govpay.orm.IdMediaRilevamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdMediaRilevamento readIdMediaRilevamento(InputStream in) throws DeserializerException {
		return (IdMediaRilevamento) this.xmlToObj(in, IdMediaRilevamento.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdMediaRilevamento}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdMediaRilevamento}
	 * @return Object type {@link it.govpay.orm.IdMediaRilevamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdMediaRilevamento readIdMediaRilevamento(byte[] in) throws DeserializerException {
		return (IdMediaRilevamento) this.xmlToObj(in, IdMediaRilevamento.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdMediaRilevamento}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdMediaRilevamento}
	 * @return Object type {@link it.govpay.orm.IdMediaRilevamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdMediaRilevamento readIdMediaRilevamentoFromString(String in) throws DeserializerException {
		return (IdMediaRilevamento) this.xmlToObj(in.getBytes(), IdMediaRilevamento.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: id-singolo-versamento
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdSingoloVersamento}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdSingoloVersamento}
	 * @return Object type {@link it.govpay.orm.IdSingoloVersamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdSingoloVersamento readIdSingoloVersamento(String fileName) throws DeserializerException {
		return (IdSingoloVersamento) this.xmlToObj(fileName, IdSingoloVersamento.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdSingoloVersamento}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdSingoloVersamento}
	 * @return Object type {@link it.govpay.orm.IdSingoloVersamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdSingoloVersamento readIdSingoloVersamento(File file) throws DeserializerException {
		return (IdSingoloVersamento) this.xmlToObj(file, IdSingoloVersamento.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdSingoloVersamento}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdSingoloVersamento}
	 * @return Object type {@link it.govpay.orm.IdSingoloVersamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdSingoloVersamento readIdSingoloVersamento(InputStream in) throws DeserializerException {
		return (IdSingoloVersamento) this.xmlToObj(in, IdSingoloVersamento.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdSingoloVersamento}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdSingoloVersamento}
	 * @return Object type {@link it.govpay.orm.IdSingoloVersamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdSingoloVersamento readIdSingoloVersamento(byte[] in) throws DeserializerException {
		return (IdSingoloVersamento) this.xmlToObj(in, IdSingoloVersamento.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdSingoloVersamento}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdSingoloVersamento}
	 * @return Object type {@link it.govpay.orm.IdSingoloVersamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdSingoloVersamento readIdSingoloVersamentoFromString(String in) throws DeserializerException {
		return (IdSingoloVersamento) this.xmlToObj(in.getBytes(), IdSingoloVersamento.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: id-tributo
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdTributo}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdTributo}
	 * @return Object type {@link it.govpay.orm.IdTributo}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdTributo readIdTributo(String fileName) throws DeserializerException {
		return (IdTributo) this.xmlToObj(fileName, IdTributo.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdTributo}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdTributo}
	 * @return Object type {@link it.govpay.orm.IdTributo}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdTributo readIdTributo(File file) throws DeserializerException {
		return (IdTributo) this.xmlToObj(file, IdTributo.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdTributo}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdTributo}
	 * @return Object type {@link it.govpay.orm.IdTributo}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdTributo readIdTributo(InputStream in) throws DeserializerException {
		return (IdTributo) this.xmlToObj(in, IdTributo.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdTributo}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdTributo}
	 * @return Object type {@link it.govpay.orm.IdTributo}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdTributo readIdTributo(byte[] in) throws DeserializerException {
		return (IdTributo) this.xmlToObj(in, IdTributo.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdTributo}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdTributo}
	 * @return Object type {@link it.govpay.orm.IdTributo}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdTributo readIdTributoFromString(String in) throws DeserializerException {
		return (IdTributo) this.xmlToObj(in.getBytes(), IdTributo.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: id-iuv
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdIuv}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdIuv}
	 * @return Object type {@link it.govpay.orm.IdIuv}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdIuv readIdIuv(String fileName) throws DeserializerException {
		return (IdIuv) this.xmlToObj(fileName, IdIuv.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdIuv}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdIuv}
	 * @return Object type {@link it.govpay.orm.IdIuv}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdIuv readIdIuv(File file) throws DeserializerException {
		return (IdIuv) this.xmlToObj(file, IdIuv.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdIuv}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdIuv}
	 * @return Object type {@link it.govpay.orm.IdIuv}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdIuv readIdIuv(InputStream in) throws DeserializerException {
		return (IdIuv) this.xmlToObj(in, IdIuv.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdIuv}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdIuv}
	 * @return Object type {@link it.govpay.orm.IdIuv}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdIuv readIdIuv(byte[] in) throws DeserializerException {
		return (IdIuv) this.xmlToObj(in, IdIuv.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdIuv}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdIuv}
	 * @return Object type {@link it.govpay.orm.IdIuv}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdIuv readIdIuvFromString(String in) throws DeserializerException {
		return (IdIuv) this.xmlToObj(in.getBytes(), IdIuv.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: SingoloVersamento
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.SingoloVersamento}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.SingoloVersamento}
	 * @return Object type {@link it.govpay.orm.SingoloVersamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public SingoloVersamento readSingoloVersamento(String fileName) throws DeserializerException {
		return (SingoloVersamento) this.xmlToObj(fileName, SingoloVersamento.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.SingoloVersamento}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.SingoloVersamento}
	 * @return Object type {@link it.govpay.orm.SingoloVersamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public SingoloVersamento readSingoloVersamento(File file) throws DeserializerException {
		return (SingoloVersamento) this.xmlToObj(file, SingoloVersamento.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.SingoloVersamento}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.SingoloVersamento}
	 * @return Object type {@link it.govpay.orm.SingoloVersamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public SingoloVersamento readSingoloVersamento(InputStream in) throws DeserializerException {
		return (SingoloVersamento) this.xmlToObj(in, SingoloVersamento.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.SingoloVersamento}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.SingoloVersamento}
	 * @return Object type {@link it.govpay.orm.SingoloVersamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public SingoloVersamento readSingoloVersamento(byte[] in) throws DeserializerException {
		return (SingoloVersamento) this.xmlToObj(in, SingoloVersamento.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.SingoloVersamento}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.SingoloVersamento}
	 * @return Object type {@link it.govpay.orm.SingoloVersamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public SingoloVersamento readSingoloVersamentoFromString(String in) throws DeserializerException {
		return (SingoloVersamento) this.xmlToObj(in.getBytes(), SingoloVersamento.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: id-iban-accredito
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdIbanAccredito}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdIbanAccredito}
	 * @return Object type {@link it.govpay.orm.IdIbanAccredito}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdIbanAccredito readIdIbanAccredito(String fileName) throws DeserializerException {
		return (IdIbanAccredito) this.xmlToObj(fileName, IdIbanAccredito.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdIbanAccredito}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdIbanAccredito}
	 * @return Object type {@link it.govpay.orm.IdIbanAccredito}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdIbanAccredito readIdIbanAccredito(File file) throws DeserializerException {
		return (IdIbanAccredito) this.xmlToObj(file, IdIbanAccredito.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdIbanAccredito}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdIbanAccredito}
	 * @return Object type {@link it.govpay.orm.IdIbanAccredito}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdIbanAccredito readIdIbanAccredito(InputStream in) throws DeserializerException {
		return (IdIbanAccredito) this.xmlToObj(in, IdIbanAccredito.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdIbanAccredito}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdIbanAccredito}
	 * @return Object type {@link it.govpay.orm.IdIbanAccredito}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdIbanAccredito readIdIbanAccredito(byte[] in) throws DeserializerException {
		return (IdIbanAccredito) this.xmlToObj(in, IdIbanAccredito.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdIbanAccredito}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdIbanAccredito}
	 * @return Object type {@link it.govpay.orm.IdIbanAccredito}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdIbanAccredito readIdIbanAccreditoFromString(String in) throws DeserializerException {
		return (IdIbanAccredito) this.xmlToObj(in.getBytes(), IdIbanAccredito.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: FR
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.FR}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.FR}
	 * @return Object type {@link it.govpay.orm.FR}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public FR readFR(String fileName) throws DeserializerException {
		return (FR) this.xmlToObj(fileName, FR.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.FR}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.FR}
	 * @return Object type {@link it.govpay.orm.FR}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public FR readFR(File file) throws DeserializerException {
		return (FR) this.xmlToObj(file, FR.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.FR}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.FR}
	 * @return Object type {@link it.govpay.orm.FR}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public FR readFR(InputStream in) throws DeserializerException {
		return (FR) this.xmlToObj(in, FR.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.FR}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.FR}
	 * @return Object type {@link it.govpay.orm.FR}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public FR readFR(byte[] in) throws DeserializerException {
		return (FR) this.xmlToObj(in, FR.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.FR}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.FR}
	 * @return Object type {@link it.govpay.orm.FR}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public FR readFRFromString(String in) throws DeserializerException {
		return (FR) this.xmlToObj(in.getBytes(), FR.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: id-mail
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdMail}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdMail}
	 * @return Object type {@link it.govpay.orm.IdMail}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdMail readIdMail(String fileName) throws DeserializerException {
		return (IdMail) this.xmlToObj(fileName, IdMail.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdMail}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdMail}
	 * @return Object type {@link it.govpay.orm.IdMail}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdMail readIdMail(File file) throws DeserializerException {
		return (IdMail) this.xmlToObj(file, IdMail.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdMail}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdMail}
	 * @return Object type {@link it.govpay.orm.IdMail}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdMail readIdMail(InputStream in) throws DeserializerException {
		return (IdMail) this.xmlToObj(in, IdMail.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdMail}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdMail}
	 * @return Object type {@link it.govpay.orm.IdMail}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdMail readIdMail(byte[] in) throws DeserializerException {
		return (IdMail) this.xmlToObj(in, IdMail.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdMail}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdMail}
	 * @return Object type {@link it.govpay.orm.IdMail}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdMail readIdMailFromString(String in) throws DeserializerException {
		return (IdMail) this.xmlToObj(in.getBytes(), IdMail.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: id-portale
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdPortale}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdPortale}
	 * @return Object type {@link it.govpay.orm.IdPortale}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdPortale readIdPortale(String fileName) throws DeserializerException {
		return (IdPortale) this.xmlToObj(fileName, IdPortale.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdPortale}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdPortale}
	 * @return Object type {@link it.govpay.orm.IdPortale}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdPortale readIdPortale(File file) throws DeserializerException {
		return (IdPortale) this.xmlToObj(file, IdPortale.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdPortale}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdPortale}
	 * @return Object type {@link it.govpay.orm.IdPortale}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdPortale readIdPortale(InputStream in) throws DeserializerException {
		return (IdPortale) this.xmlToObj(in, IdPortale.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdPortale}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdPortale}
	 * @return Object type {@link it.govpay.orm.IdPortale}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdPortale readIdPortale(byte[] in) throws DeserializerException {
		return (IdPortale) this.xmlToObj(in, IdPortale.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdPortale}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdPortale}
	 * @return Object type {@link it.govpay.orm.IdPortale}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdPortale readIdPortaleFromString(String in) throws DeserializerException {
		return (IdPortale) this.xmlToObj(in.getBytes(), IdPortale.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: id-fr-applicazione
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdFrApplicazione}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdFrApplicazione}
	 * @return Object type {@link it.govpay.orm.IdFrApplicazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdFrApplicazione readIdFrApplicazione(String fileName) throws DeserializerException {
		return (IdFrApplicazione) this.xmlToObj(fileName, IdFrApplicazione.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdFrApplicazione}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdFrApplicazione}
	 * @return Object type {@link it.govpay.orm.IdFrApplicazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdFrApplicazione readIdFrApplicazione(File file) throws DeserializerException {
		return (IdFrApplicazione) this.xmlToObj(file, IdFrApplicazione.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdFrApplicazione}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdFrApplicazione}
	 * @return Object type {@link it.govpay.orm.IdFrApplicazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdFrApplicazione readIdFrApplicazione(InputStream in) throws DeserializerException {
		return (IdFrApplicazione) this.xmlToObj(in, IdFrApplicazione.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdFrApplicazione}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdFrApplicazione}
	 * @return Object type {@link it.govpay.orm.IdFrApplicazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdFrApplicazione readIdFrApplicazione(byte[] in) throws DeserializerException {
		return (IdFrApplicazione) this.xmlToObj(in, IdFrApplicazione.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdFrApplicazione}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdFrApplicazione}
	 * @return Object type {@link it.govpay.orm.IdFrApplicazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdFrApplicazione readIdFrApplicazioneFromString(String in) throws DeserializerException {
		return (IdFrApplicazione) this.xmlToObj(in.getBytes(), IdFrApplicazione.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: RendicontazioneSenzaRPT
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.RendicontazioneSenzaRPT}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.RendicontazioneSenzaRPT}
	 * @return Object type {@link it.govpay.orm.RendicontazioneSenzaRPT}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public RendicontazioneSenzaRPT readRendicontazioneSenzaRPT(String fileName) throws DeserializerException {
		return (RendicontazioneSenzaRPT) this.xmlToObj(fileName, RendicontazioneSenzaRPT.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.RendicontazioneSenzaRPT}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.RendicontazioneSenzaRPT}
	 * @return Object type {@link it.govpay.orm.RendicontazioneSenzaRPT}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public RendicontazioneSenzaRPT readRendicontazioneSenzaRPT(File file) throws DeserializerException {
		return (RendicontazioneSenzaRPT) this.xmlToObj(file, RendicontazioneSenzaRPT.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.RendicontazioneSenzaRPT}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.RendicontazioneSenzaRPT}
	 * @return Object type {@link it.govpay.orm.RendicontazioneSenzaRPT}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public RendicontazioneSenzaRPT readRendicontazioneSenzaRPT(InputStream in) throws DeserializerException {
		return (RendicontazioneSenzaRPT) this.xmlToObj(in, RendicontazioneSenzaRPT.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.RendicontazioneSenzaRPT}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.RendicontazioneSenzaRPT}
	 * @return Object type {@link it.govpay.orm.RendicontazioneSenzaRPT}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public RendicontazioneSenzaRPT readRendicontazioneSenzaRPT(byte[] in) throws DeserializerException {
		return (RendicontazioneSenzaRPT) this.xmlToObj(in, RendicontazioneSenzaRPT.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.RendicontazioneSenzaRPT}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.RendicontazioneSenzaRPT}
	 * @return Object type {@link it.govpay.orm.RendicontazioneSenzaRPT}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public RendicontazioneSenzaRPT readRendicontazioneSenzaRPTFromString(String in) throws DeserializerException {
		return (RendicontazioneSenzaRPT) this.xmlToObj(in.getBytes(), RendicontazioneSenzaRPT.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: id-fr
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdFr}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdFr}
	 * @return Object type {@link it.govpay.orm.IdFr}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdFr readIdFr(String fileName) throws DeserializerException {
		return (IdFr) this.xmlToObj(fileName, IdFr.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdFr}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdFr}
	 * @return Object type {@link it.govpay.orm.IdFr}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdFr readIdFr(File file) throws DeserializerException {
		return (IdFr) this.xmlToObj(file, IdFr.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdFr}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdFr}
	 * @return Object type {@link it.govpay.orm.IdFr}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdFr readIdFr(InputStream in) throws DeserializerException {
		return (IdFr) this.xmlToObj(in, IdFr.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdFr}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdFr}
	 * @return Object type {@link it.govpay.orm.IdFr}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdFr readIdFr(byte[] in) throws DeserializerException {
		return (IdFr) this.xmlToObj(in, IdFr.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdFr}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdFr}
	 * @return Object type {@link it.govpay.orm.IdFr}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdFr readIdFrFromString(String in) throws DeserializerException {
		return (IdFr) this.xmlToObj(in.getBytes(), IdFr.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: Portale
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.Portale}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Portale}
	 * @return Object type {@link it.govpay.orm.Portale}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Portale readPortale(String fileName) throws DeserializerException {
		return (Portale) this.xmlToObj(fileName, Portale.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.Portale}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Portale}
	 * @return Object type {@link it.govpay.orm.Portale}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Portale readPortale(File file) throws DeserializerException {
		return (Portale) this.xmlToObj(file, Portale.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.Portale}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.Portale}
	 * @return Object type {@link it.govpay.orm.Portale}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Portale readPortale(InputStream in) throws DeserializerException {
		return (Portale) this.xmlToObj(in, Portale.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.Portale}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.Portale}
	 * @return Object type {@link it.govpay.orm.Portale}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Portale readPortale(byte[] in) throws DeserializerException {
		return (Portale) this.xmlToObj(in, Portale.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.Portale}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.Portale}
	 * @return Object type {@link it.govpay.orm.Portale}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Portale readPortaleFromString(String in) throws DeserializerException {
		return (Portale) this.xmlToObj(in.getBytes(), Portale.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: PortaleApplicazione
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.PortaleApplicazione}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.PortaleApplicazione}
	 * @return Object type {@link it.govpay.orm.PortaleApplicazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public PortaleApplicazione readPortaleApplicazione(String fileName) throws DeserializerException {
		return (PortaleApplicazione) this.xmlToObj(fileName, PortaleApplicazione.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.PortaleApplicazione}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.PortaleApplicazione}
	 * @return Object type {@link it.govpay.orm.PortaleApplicazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public PortaleApplicazione readPortaleApplicazione(File file) throws DeserializerException {
		return (PortaleApplicazione) this.xmlToObj(file, PortaleApplicazione.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.PortaleApplicazione}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.PortaleApplicazione}
	 * @return Object type {@link it.govpay.orm.PortaleApplicazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public PortaleApplicazione readPortaleApplicazione(InputStream in) throws DeserializerException {
		return (PortaleApplicazione) this.xmlToObj(in, PortaleApplicazione.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.PortaleApplicazione}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.PortaleApplicazione}
	 * @return Object type {@link it.govpay.orm.PortaleApplicazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public PortaleApplicazione readPortaleApplicazione(byte[] in) throws DeserializerException {
		return (PortaleApplicazione) this.xmlToObj(in, PortaleApplicazione.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.PortaleApplicazione}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.PortaleApplicazione}
	 * @return Object type {@link it.govpay.orm.PortaleApplicazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public PortaleApplicazione readPortaleApplicazioneFromString(String in) throws DeserializerException {
		return (PortaleApplicazione) this.xmlToObj(in.getBytes(), PortaleApplicazione.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: id-tabella-controparti
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdTabellaControparti}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdTabellaControparti}
	 * @return Object type {@link it.govpay.orm.IdTabellaControparti}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdTabellaControparti readIdTabellaControparti(String fileName) throws DeserializerException {
		return (IdTabellaControparti) this.xmlToObj(fileName, IdTabellaControparti.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdTabellaControparti}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdTabellaControparti}
	 * @return Object type {@link it.govpay.orm.IdTabellaControparti}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdTabellaControparti readIdTabellaControparti(File file) throws DeserializerException {
		return (IdTabellaControparti) this.xmlToObj(file, IdTabellaControparti.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdTabellaControparti}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdTabellaControparti}
	 * @return Object type {@link it.govpay.orm.IdTabellaControparti}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdTabellaControparti readIdTabellaControparti(InputStream in) throws DeserializerException {
		return (IdTabellaControparti) this.xmlToObj(in, IdTabellaControparti.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdTabellaControparti}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdTabellaControparti}
	 * @return Object type {@link it.govpay.orm.IdTabellaControparti}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdTabellaControparti readIdTabellaControparti(byte[] in) throws DeserializerException {
		return (IdTabellaControparti) this.xmlToObj(in, IdTabellaControparti.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdTabellaControparti}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdTabellaControparti}
	 * @return Object type {@link it.govpay.orm.IdTabellaControparti}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdTabellaControparti readIdTabellaContropartiFromString(String in) throws DeserializerException {
		return (IdTabellaControparti) this.xmlToObj(in.getBytes(), IdTabellaControparti.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: id-rpt
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdRpt}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdRpt}
	 * @return Object type {@link it.govpay.orm.IdRpt}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdRpt readIdRpt(String fileName) throws DeserializerException {
		return (IdRpt) this.xmlToObj(fileName, IdRpt.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdRpt}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdRpt}
	 * @return Object type {@link it.govpay.orm.IdRpt}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdRpt readIdRpt(File file) throws DeserializerException {
		return (IdRpt) this.xmlToObj(file, IdRpt.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdRpt}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdRpt}
	 * @return Object type {@link it.govpay.orm.IdRpt}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdRpt readIdRpt(InputStream in) throws DeserializerException {
		return (IdRpt) this.xmlToObj(in, IdRpt.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdRpt}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdRpt}
	 * @return Object type {@link it.govpay.orm.IdRpt}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdRpt readIdRpt(byte[] in) throws DeserializerException {
		return (IdRpt) this.xmlToObj(in, IdRpt.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdRpt}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdRpt}
	 * @return Object type {@link it.govpay.orm.IdRpt}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdRpt readIdRptFromString(String in) throws DeserializerException {
		return (IdRpt) this.xmlToObj(in.getBytes(), IdRpt.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: RR
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.RR}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.RR}
	 * @return Object type {@link it.govpay.orm.RR}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public RR readRR(String fileName) throws DeserializerException {
		return (RR) this.xmlToObj(fileName, RR.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.RR}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.RR}
	 * @return Object type {@link it.govpay.orm.RR}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public RR readRR(File file) throws DeserializerException {
		return (RR) this.xmlToObj(file, RR.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.RR}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.RR}
	 * @return Object type {@link it.govpay.orm.RR}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public RR readRR(InputStream in) throws DeserializerException {
		return (RR) this.xmlToObj(in, RR.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.RR}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.RR}
	 * @return Object type {@link it.govpay.orm.RR}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public RR readRR(byte[] in) throws DeserializerException {
		return (RR) this.xmlToObj(in, RR.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.RR}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.RR}
	 * @return Object type {@link it.govpay.orm.RR}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public RR readRRFromString(String in) throws DeserializerException {
		return (RR) this.xmlToObj(in.getBytes(), RR.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: id-messaggio
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdMessaggio}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdMessaggio}
	 * @return Object type {@link it.govpay.orm.IdMessaggio}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdMessaggio readIdMessaggio(String fileName) throws DeserializerException {
		return (IdMessaggio) this.xmlToObj(fileName, IdMessaggio.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdMessaggio}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdMessaggio}
	 * @return Object type {@link it.govpay.orm.IdMessaggio}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdMessaggio readIdMessaggio(File file) throws DeserializerException {
		return (IdMessaggio) this.xmlToObj(file, IdMessaggio.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdMessaggio}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdMessaggio}
	 * @return Object type {@link it.govpay.orm.IdMessaggio}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdMessaggio readIdMessaggio(InputStream in) throws DeserializerException {
		return (IdMessaggio) this.xmlToObj(in, IdMessaggio.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdMessaggio}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdMessaggio}
	 * @return Object type {@link it.govpay.orm.IdMessaggio}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdMessaggio readIdMessaggio(byte[] in) throws DeserializerException {
		return (IdMessaggio) this.xmlToObj(in, IdMessaggio.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdMessaggio}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdMessaggio}
	 * @return Object type {@link it.govpay.orm.IdMessaggio}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdMessaggio readIdMessaggioFromString(String in) throws DeserializerException {
		return (IdMessaggio) this.xmlToObj(in.getBytes(), IdMessaggio.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: ApplicazioneTributo
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.ApplicazioneTributo}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.ApplicazioneTributo}
	 * @return Object type {@link it.govpay.orm.ApplicazioneTributo}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public ApplicazioneTributo readApplicazioneTributo(String fileName) throws DeserializerException {
		return (ApplicazioneTributo) this.xmlToObj(fileName, ApplicazioneTributo.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.ApplicazioneTributo}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.ApplicazioneTributo}
	 * @return Object type {@link it.govpay.orm.ApplicazioneTributo}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public ApplicazioneTributo readApplicazioneTributo(File file) throws DeserializerException {
		return (ApplicazioneTributo) this.xmlToObj(file, ApplicazioneTributo.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.ApplicazioneTributo}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.ApplicazioneTributo}
	 * @return Object type {@link it.govpay.orm.ApplicazioneTributo}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public ApplicazioneTributo readApplicazioneTributo(InputStream in) throws DeserializerException {
		return (ApplicazioneTributo) this.xmlToObj(in, ApplicazioneTributo.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.ApplicazioneTributo}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.ApplicazioneTributo}
	 * @return Object type {@link it.govpay.orm.ApplicazioneTributo}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public ApplicazioneTributo readApplicazioneTributo(byte[] in) throws DeserializerException {
		return (ApplicazioneTributo) this.xmlToObj(in, ApplicazioneTributo.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.ApplicazioneTributo}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.ApplicazioneTributo}
	 * @return Object type {@link it.govpay.orm.ApplicazioneTributo}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public ApplicazioneTributo readApplicazioneTributoFromString(String in) throws DeserializerException {
		return (ApplicazioneTributo) this.xmlToObj(in.getBytes(), ApplicazioneTributo.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: id-rr
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdRr}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdRr}
	 * @return Object type {@link it.govpay.orm.IdRr}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdRr readIdRr(String fileName) throws DeserializerException {
		return (IdRr) this.xmlToObj(fileName, IdRr.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdRr}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdRr}
	 * @return Object type {@link it.govpay.orm.IdRr}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdRr readIdRr(File file) throws DeserializerException {
		return (IdRr) this.xmlToObj(file, IdRr.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdRr}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdRr}
	 * @return Object type {@link it.govpay.orm.IdRr}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdRr readIdRr(InputStream in) throws DeserializerException {
		return (IdRr) this.xmlToObj(in, IdRr.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdRr}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdRr}
	 * @return Object type {@link it.govpay.orm.IdRr}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdRr readIdRr(byte[] in) throws DeserializerException {
		return (IdRr) this.xmlToObj(in, IdRr.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdRr}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdRr}
	 * @return Object type {@link it.govpay.orm.IdRr}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdRr readIdRrFromString(String in) throws DeserializerException {
		return (IdRr) this.xmlToObj(in.getBytes(), IdRr.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: id-carrello
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdCarrello}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdCarrello}
	 * @return Object type {@link it.govpay.orm.IdCarrello}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdCarrello readIdCarrello(String fileName) throws DeserializerException {
		return (IdCarrello) this.xmlToObj(fileName, IdCarrello.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdCarrello}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdCarrello}
	 * @return Object type {@link it.govpay.orm.IdCarrello}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdCarrello readIdCarrello(File file) throws DeserializerException {
		return (IdCarrello) this.xmlToObj(file, IdCarrello.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdCarrello}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdCarrello}
	 * @return Object type {@link it.govpay.orm.IdCarrello}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdCarrello readIdCarrello(InputStream in) throws DeserializerException {
		return (IdCarrello) this.xmlToObj(in, IdCarrello.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdCarrello}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdCarrello}
	 * @return Object type {@link it.govpay.orm.IdCarrello}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdCarrello readIdCarrello(byte[] in) throws DeserializerException {
		return (IdCarrello) this.xmlToObj(in, IdCarrello.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdCarrello}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdCarrello}
	 * @return Object type {@link it.govpay.orm.IdCarrello}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdCarrello readIdCarrelloFromString(String in) throws DeserializerException {
		return (IdCarrello) this.xmlToObj(in.getBytes(), IdCarrello.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: Operatore
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.Operatore}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Operatore}
	 * @return Object type {@link it.govpay.orm.Operatore}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Operatore readOperatore(String fileName) throws DeserializerException {
		return (Operatore) this.xmlToObj(fileName, Operatore.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.Operatore}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Operatore}
	 * @return Object type {@link it.govpay.orm.Operatore}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Operatore readOperatore(File file) throws DeserializerException {
		return (Operatore) this.xmlToObj(file, Operatore.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.Operatore}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.Operatore}
	 * @return Object type {@link it.govpay.orm.Operatore}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Operatore readOperatore(InputStream in) throws DeserializerException {
		return (Operatore) this.xmlToObj(in, Operatore.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.Operatore}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.Operatore}
	 * @return Object type {@link it.govpay.orm.Operatore}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Operatore readOperatore(byte[] in) throws DeserializerException {
		return (Operatore) this.xmlToObj(in, Operatore.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.Operatore}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.Operatore}
	 * @return Object type {@link it.govpay.orm.Operatore}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Operatore readOperatoreFromString(String in) throws DeserializerException {
		return (Operatore) this.xmlToObj(in.getBytes(), Operatore.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: OperatoreUo
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.OperatoreUo}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.OperatoreUo}
	 * @return Object type {@link it.govpay.orm.OperatoreUo}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public OperatoreUo readOperatoreUo(String fileName) throws DeserializerException {
		return (OperatoreUo) this.xmlToObj(fileName, OperatoreUo.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.OperatoreUo}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.OperatoreUo}
	 * @return Object type {@link it.govpay.orm.OperatoreUo}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public OperatoreUo readOperatoreUo(File file) throws DeserializerException {
		return (OperatoreUo) this.xmlToObj(file, OperatoreUo.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.OperatoreUo}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.OperatoreUo}
	 * @return Object type {@link it.govpay.orm.OperatoreUo}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public OperatoreUo readOperatoreUo(InputStream in) throws DeserializerException {
		return (OperatoreUo) this.xmlToObj(in, OperatoreUo.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.OperatoreUo}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.OperatoreUo}
	 * @return Object type {@link it.govpay.orm.OperatoreUo}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public OperatoreUo readOperatoreUo(byte[] in) throws DeserializerException {
		return (OperatoreUo) this.xmlToObj(in, OperatoreUo.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.OperatoreUo}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.OperatoreUo}
	 * @return Object type {@link it.govpay.orm.OperatoreUo}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public OperatoreUo readOperatoreUoFromString(String in) throws DeserializerException {
		return (OperatoreUo) this.xmlToObj(in.getBytes(), OperatoreUo.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: OperatoreApplicazione
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.OperatoreApplicazione}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.OperatoreApplicazione}
	 * @return Object type {@link it.govpay.orm.OperatoreApplicazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public OperatoreApplicazione readOperatoreApplicazione(String fileName) throws DeserializerException {
		return (OperatoreApplicazione) this.xmlToObj(fileName, OperatoreApplicazione.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.OperatoreApplicazione}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.OperatoreApplicazione}
	 * @return Object type {@link it.govpay.orm.OperatoreApplicazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public OperatoreApplicazione readOperatoreApplicazione(File file) throws DeserializerException {
		return (OperatoreApplicazione) this.xmlToObj(file, OperatoreApplicazione.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.OperatoreApplicazione}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.OperatoreApplicazione}
	 * @return Object type {@link it.govpay.orm.OperatoreApplicazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public OperatoreApplicazione readOperatoreApplicazione(InputStream in) throws DeserializerException {
		return (OperatoreApplicazione) this.xmlToObj(in, OperatoreApplicazione.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.OperatoreApplicazione}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.OperatoreApplicazione}
	 * @return Object type {@link it.govpay.orm.OperatoreApplicazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public OperatoreApplicazione readOperatoreApplicazione(byte[] in) throws DeserializerException {
		return (OperatoreApplicazione) this.xmlToObj(in, OperatoreApplicazione.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.OperatoreApplicazione}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.OperatoreApplicazione}
	 * @return Object type {@link it.govpay.orm.OperatoreApplicazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public OperatoreApplicazione readOperatoreApplicazioneFromString(String in) throws DeserializerException {
		return (OperatoreApplicazione) this.xmlToObj(in.getBytes(), OperatoreApplicazione.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: OperatorePortale
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.OperatorePortale}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.OperatorePortale}
	 * @return Object type {@link it.govpay.orm.OperatorePortale}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public OperatorePortale readOperatorePortale(String fileName) throws DeserializerException {
		return (OperatorePortale) this.xmlToObj(fileName, OperatorePortale.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.OperatorePortale}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.OperatorePortale}
	 * @return Object type {@link it.govpay.orm.OperatorePortale}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public OperatorePortale readOperatorePortale(File file) throws DeserializerException {
		return (OperatorePortale) this.xmlToObj(file, OperatorePortale.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.OperatorePortale}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.OperatorePortale}
	 * @return Object type {@link it.govpay.orm.OperatorePortale}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public OperatorePortale readOperatorePortale(InputStream in) throws DeserializerException {
		return (OperatorePortale) this.xmlToObj(in, OperatorePortale.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.OperatorePortale}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.OperatorePortale}
	 * @return Object type {@link it.govpay.orm.OperatorePortale}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public OperatorePortale readOperatorePortale(byte[] in) throws DeserializerException {
		return (OperatorePortale) this.xmlToObj(in, OperatorePortale.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.OperatorePortale}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.OperatorePortale}
	 * @return Object type {@link it.govpay.orm.OperatorePortale}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public OperatorePortale readOperatorePortaleFromString(String in) throws DeserializerException {
		return (OperatorePortale) this.xmlToObj(in.getBytes(), OperatorePortale.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: Intermediario
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.Intermediario}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Intermediario}
	 * @return Object type {@link it.govpay.orm.Intermediario}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Intermediario readIntermediario(String fileName) throws DeserializerException {
		return (Intermediario) this.xmlToObj(fileName, Intermediario.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.Intermediario}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Intermediario}
	 * @return Object type {@link it.govpay.orm.Intermediario}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Intermediario readIntermediario(File file) throws DeserializerException {
		return (Intermediario) this.xmlToObj(file, Intermediario.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.Intermediario}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.Intermediario}
	 * @return Object type {@link it.govpay.orm.Intermediario}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Intermediario readIntermediario(InputStream in) throws DeserializerException {
		return (Intermediario) this.xmlToObj(in, Intermediario.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.Intermediario}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.Intermediario}
	 * @return Object type {@link it.govpay.orm.Intermediario}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Intermediario readIntermediario(byte[] in) throws DeserializerException {
		return (Intermediario) this.xmlToObj(in, Intermediario.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.Intermediario}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.Intermediario}
	 * @return Object type {@link it.govpay.orm.Intermediario}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Intermediario readIntermediarioFromString(String in) throws DeserializerException {
		return (Intermediario) this.xmlToObj(in.getBytes(), Intermediario.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: id-tracciato
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdTracciato}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdTracciato}
	 * @return Object type {@link it.govpay.orm.IdTracciato}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdTracciato readIdTracciato(String fileName) throws DeserializerException {
		return (IdTracciato) this.xmlToObj(fileName, IdTracciato.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdTracciato}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdTracciato}
	 * @return Object type {@link it.govpay.orm.IdTracciato}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdTracciato readIdTracciato(File file) throws DeserializerException {
		return (IdTracciato) this.xmlToObj(file, IdTracciato.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdTracciato}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdTracciato}
	 * @return Object type {@link it.govpay.orm.IdTracciato}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdTracciato readIdTracciato(InputStream in) throws DeserializerException {
		return (IdTracciato) this.xmlToObj(in, IdTracciato.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdTracciato}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdTracciato}
	 * @return Object type {@link it.govpay.orm.IdTracciato}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdTracciato readIdTracciato(byte[] in) throws DeserializerException {
		return (IdTracciato) this.xmlToObj(in, IdTracciato.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdTracciato}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdTracciato}
	 * @return Object type {@link it.govpay.orm.IdTracciato}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdTracciato readIdTracciatoFromString(String in) throws DeserializerException {
		return (IdTracciato) this.xmlToObj(in.getBytes(), IdTracciato.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: id-stazione
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdStazione}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdStazione}
	 * @return Object type {@link it.govpay.orm.IdStazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdStazione readIdStazione(String fileName) throws DeserializerException {
		return (IdStazione) this.xmlToObj(fileName, IdStazione.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdStazione}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdStazione}
	 * @return Object type {@link it.govpay.orm.IdStazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdStazione readIdStazione(File file) throws DeserializerException {
		return (IdStazione) this.xmlToObj(file, IdStazione.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdStazione}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdStazione}
	 * @return Object type {@link it.govpay.orm.IdStazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdStazione readIdStazione(InputStream in) throws DeserializerException {
		return (IdStazione) this.xmlToObj(in, IdStazione.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdStazione}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdStazione}
	 * @return Object type {@link it.govpay.orm.IdStazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdStazione readIdStazione(byte[] in) throws DeserializerException {
		return (IdStazione) this.xmlToObj(in, IdStazione.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdStazione}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdStazione}
	 * @return Object type {@link it.govpay.orm.IdStazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdStazione readIdStazioneFromString(String in) throws DeserializerException {
		return (IdStazione) this.xmlToObj(in.getBytes(), IdStazione.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: RPT
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.RPT}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.RPT}
	 * @return Object type {@link it.govpay.orm.RPT}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public RPT readRPT(String fileName) throws DeserializerException {
		return (RPT) this.xmlToObj(fileName, RPT.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.RPT}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.RPT}
	 * @return Object type {@link it.govpay.orm.RPT}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public RPT readRPT(File file) throws DeserializerException {
		return (RPT) this.xmlToObj(file, RPT.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.RPT}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.RPT}
	 * @return Object type {@link it.govpay.orm.RPT}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public RPT readRPT(InputStream in) throws DeserializerException {
		return (RPT) this.xmlToObj(in, RPT.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.RPT}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.RPT}
	 * @return Object type {@link it.govpay.orm.RPT}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public RPT readRPT(byte[] in) throws DeserializerException {
		return (RPT) this.xmlToObj(in, RPT.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.RPT}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.RPT}
	 * @return Object type {@link it.govpay.orm.RPT}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public RPT readRPTFromString(String in) throws DeserializerException {
		return (RPT) this.xmlToObj(in.getBytes(), RPT.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: id-canale
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdCanale}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdCanale}
	 * @return Object type {@link it.govpay.orm.IdCanale}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdCanale readIdCanale(String fileName) throws DeserializerException {
		return (IdCanale) this.xmlToObj(fileName, IdCanale.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdCanale}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdCanale}
	 * @return Object type {@link it.govpay.orm.IdCanale}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdCanale readIdCanale(File file) throws DeserializerException {
		return (IdCanale) this.xmlToObj(file, IdCanale.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdCanale}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdCanale}
	 * @return Object type {@link it.govpay.orm.IdCanale}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdCanale readIdCanale(InputStream in) throws DeserializerException {
		return (IdCanale) this.xmlToObj(in, IdCanale.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdCanale}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdCanale}
	 * @return Object type {@link it.govpay.orm.IdCanale}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdCanale readIdCanale(byte[] in) throws DeserializerException {
		return (IdCanale) this.xmlToObj(in, IdCanale.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdCanale}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdCanale}
	 * @return Object type {@link it.govpay.orm.IdCanale}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdCanale readIdCanaleFromString(String in) throws DeserializerException {
		return (IdCanale) this.xmlToObj(in.getBytes(), IdCanale.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: id-pagamento
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdPagamento}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdPagamento}
	 * @return Object type {@link it.govpay.orm.IdPagamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdPagamento readIdPagamento(String fileName) throws DeserializerException {
		return (IdPagamento) this.xmlToObj(fileName, IdPagamento.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdPagamento}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdPagamento}
	 * @return Object type {@link it.govpay.orm.IdPagamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdPagamento readIdPagamento(File file) throws DeserializerException {
		return (IdPagamento) this.xmlToObj(file, IdPagamento.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdPagamento}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdPagamento}
	 * @return Object type {@link it.govpay.orm.IdPagamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdPagamento readIdPagamento(InputStream in) throws DeserializerException {
		return (IdPagamento) this.xmlToObj(in, IdPagamento.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdPagamento}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdPagamento}
	 * @return Object type {@link it.govpay.orm.IdPagamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdPagamento readIdPagamento(byte[] in) throws DeserializerException {
		return (IdPagamento) this.xmlToObj(in, IdPagamento.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdPagamento}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdPagamento}
	 * @return Object type {@link it.govpay.orm.IdPagamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdPagamento readIdPagamentoFromString(String in) throws DeserializerException {
		return (IdPagamento) this.xmlToObj(in.getBytes(), IdPagamento.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: Dominio
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.Dominio}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Dominio}
	 * @return Object type {@link it.govpay.orm.Dominio}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Dominio readDominio(String fileName) throws DeserializerException {
		return (Dominio) this.xmlToObj(fileName, Dominio.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.Dominio}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Dominio}
	 * @return Object type {@link it.govpay.orm.Dominio}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Dominio readDominio(File file) throws DeserializerException {
		return (Dominio) this.xmlToObj(file, Dominio.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.Dominio}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.Dominio}
	 * @return Object type {@link it.govpay.orm.Dominio}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Dominio readDominio(InputStream in) throws DeserializerException {
		return (Dominio) this.xmlToObj(in, Dominio.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.Dominio}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.Dominio}
	 * @return Object type {@link it.govpay.orm.Dominio}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Dominio readDominio(byte[] in) throws DeserializerException {
		return (Dominio) this.xmlToObj(in, Dominio.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.Dominio}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.Dominio}
	 * @return Object type {@link it.govpay.orm.Dominio}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Dominio readDominioFromString(String in) throws DeserializerException {
		return (Dominio) this.xmlToObj(in.getBytes(), Dominio.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: FrApplicazione
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.FrApplicazione}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.FrApplicazione}
	 * @return Object type {@link it.govpay.orm.FrApplicazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public FrApplicazione readFrApplicazione(String fileName) throws DeserializerException {
		return (FrApplicazione) this.xmlToObj(fileName, FrApplicazione.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.FrApplicazione}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.FrApplicazione}
	 * @return Object type {@link it.govpay.orm.FrApplicazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public FrApplicazione readFrApplicazione(File file) throws DeserializerException {
		return (FrApplicazione) this.xmlToObj(file, FrApplicazione.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.FrApplicazione}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.FrApplicazione}
	 * @return Object type {@link it.govpay.orm.FrApplicazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public FrApplicazione readFrApplicazione(InputStream in) throws DeserializerException {
		return (FrApplicazione) this.xmlToObj(in, FrApplicazione.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.FrApplicazione}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.FrApplicazione}
	 * @return Object type {@link it.govpay.orm.FrApplicazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public FrApplicazione readFrApplicazione(byte[] in) throws DeserializerException {
		return (FrApplicazione) this.xmlToObj(in, FrApplicazione.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.FrApplicazione}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.FrApplicazione}
	 * @return Object type {@link it.govpay.orm.FrApplicazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public FrApplicazione readFrApplicazioneFromString(String in) throws DeserializerException {
		return (FrApplicazione) this.xmlToObj(in.getBytes(), FrApplicazione.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: id-mail-template
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdMailTemplate}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdMailTemplate}
	 * @return Object type {@link it.govpay.orm.IdMailTemplate}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdMailTemplate readIdMailTemplate(String fileName) throws DeserializerException {
		return (IdMailTemplate) this.xmlToObj(fileName, IdMailTemplate.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdMailTemplate}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdMailTemplate}
	 * @return Object type {@link it.govpay.orm.IdMailTemplate}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdMailTemplate readIdMailTemplate(File file) throws DeserializerException {
		return (IdMailTemplate) this.xmlToObj(file, IdMailTemplate.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdMailTemplate}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdMailTemplate}
	 * @return Object type {@link it.govpay.orm.IdMailTemplate}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdMailTemplate readIdMailTemplate(InputStream in) throws DeserializerException {
		return (IdMailTemplate) this.xmlToObj(in, IdMailTemplate.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdMailTemplate}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdMailTemplate}
	 * @return Object type {@link it.govpay.orm.IdMailTemplate}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdMailTemplate readIdMailTemplate(byte[] in) throws DeserializerException {
		return (IdMailTemplate) this.xmlToObj(in, IdMailTemplate.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdMailTemplate}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdMailTemplate}
	 * @return Object type {@link it.govpay.orm.IdMailTemplate}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdMailTemplate readIdMailTemplateFromString(String in) throws DeserializerException {
		return (IdMailTemplate) this.xmlToObj(in.getBytes(), IdMailTemplate.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: Applicazione
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.Applicazione}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Applicazione}
	 * @return Object type {@link it.govpay.orm.Applicazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Applicazione readApplicazione(String fileName) throws DeserializerException {
		return (Applicazione) this.xmlToObj(fileName, Applicazione.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.Applicazione}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Applicazione}
	 * @return Object type {@link it.govpay.orm.Applicazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Applicazione readApplicazione(File file) throws DeserializerException {
		return (Applicazione) this.xmlToObj(file, Applicazione.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.Applicazione}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.Applicazione}
	 * @return Object type {@link it.govpay.orm.Applicazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Applicazione readApplicazione(InputStream in) throws DeserializerException {
		return (Applicazione) this.xmlToObj(in, Applicazione.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.Applicazione}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.Applicazione}
	 * @return Object type {@link it.govpay.orm.Applicazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Applicazione readApplicazione(byte[] in) throws DeserializerException {
		return (Applicazione) this.xmlToObj(in, Applicazione.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.Applicazione}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.Applicazione}
	 * @return Object type {@link it.govpay.orm.Applicazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Applicazione readApplicazioneFromString(String in) throws DeserializerException {
		return (Applicazione) this.xmlToObj(in.getBytes(), Applicazione.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: Canale
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.Canale}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Canale}
	 * @return Object type {@link it.govpay.orm.Canale}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Canale readCanale(String fileName) throws DeserializerException {
		return (Canale) this.xmlToObj(fileName, Canale.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.Canale}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Canale}
	 * @return Object type {@link it.govpay.orm.Canale}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Canale readCanale(File file) throws DeserializerException {
		return (Canale) this.xmlToObj(file, Canale.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.Canale}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.Canale}
	 * @return Object type {@link it.govpay.orm.Canale}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Canale readCanale(InputStream in) throws DeserializerException {
		return (Canale) this.xmlToObj(in, Canale.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.Canale}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.Canale}
	 * @return Object type {@link it.govpay.orm.Canale}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Canale readCanale(byte[] in) throws DeserializerException {
		return (Canale) this.xmlToObj(in, Canale.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.Canale}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.Canale}
	 * @return Object type {@link it.govpay.orm.Canale}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Canale readCanaleFromString(String in) throws DeserializerException {
		return (Canale) this.xmlToObj(in.getBytes(), Canale.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: id-notifica
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdNotifica}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdNotifica}
	 * @return Object type {@link it.govpay.orm.IdNotifica}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdNotifica readIdNotifica(String fileName) throws DeserializerException {
		return (IdNotifica) this.xmlToObj(fileName, IdNotifica.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdNotifica}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdNotifica}
	 * @return Object type {@link it.govpay.orm.IdNotifica}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdNotifica readIdNotifica(File file) throws DeserializerException {
		return (IdNotifica) this.xmlToObj(file, IdNotifica.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdNotifica}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdNotifica}
	 * @return Object type {@link it.govpay.orm.IdNotifica}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdNotifica readIdNotifica(InputStream in) throws DeserializerException {
		return (IdNotifica) this.xmlToObj(in, IdNotifica.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdNotifica}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdNotifica}
	 * @return Object type {@link it.govpay.orm.IdNotifica}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdNotifica readIdNotifica(byte[] in) throws DeserializerException {
		return (IdNotifica) this.xmlToObj(in, IdNotifica.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdNotifica}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdNotifica}
	 * @return Object type {@link it.govpay.orm.IdNotifica}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdNotifica readIdNotificaFromString(String in) throws DeserializerException {
		return (IdNotifica) this.xmlToObj(in.getBytes(), IdNotifica.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: id-er
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdEr}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdEr}
	 * @return Object type {@link it.govpay.orm.IdEr}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdEr readIdEr(String fileName) throws DeserializerException {
		return (IdEr) this.xmlToObj(fileName, IdEr.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdEr}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdEr}
	 * @return Object type {@link it.govpay.orm.IdEr}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdEr readIdEr(File file) throws DeserializerException {
		return (IdEr) this.xmlToObj(file, IdEr.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdEr}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdEr}
	 * @return Object type {@link it.govpay.orm.IdEr}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdEr readIdEr(InputStream in) throws DeserializerException {
		return (IdEr) this.xmlToObj(in, IdEr.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdEr}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdEr}
	 * @return Object type {@link it.govpay.orm.IdEr}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdEr readIdEr(byte[] in) throws DeserializerException {
		return (IdEr) this.xmlToObj(in, IdEr.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdEr}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdEr}
	 * @return Object type {@link it.govpay.orm.IdEr}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdEr readIdErFromString(String in) throws DeserializerException {
		return (IdEr) this.xmlToObj(in.getBytes(), IdEr.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: id-singola-revoca
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdSingolaRevoca}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdSingolaRevoca}
	 * @return Object type {@link it.govpay.orm.IdSingolaRevoca}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdSingolaRevoca readIdSingolaRevoca(String fileName) throws DeserializerException {
		return (IdSingolaRevoca) this.xmlToObj(fileName, IdSingolaRevoca.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdSingolaRevoca}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdSingolaRevoca}
	 * @return Object type {@link it.govpay.orm.IdSingolaRevoca}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdSingolaRevoca readIdSingolaRevoca(File file) throws DeserializerException {
		return (IdSingolaRevoca) this.xmlToObj(file, IdSingolaRevoca.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdSingolaRevoca}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdSingolaRevoca}
	 * @return Object type {@link it.govpay.orm.IdSingolaRevoca}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdSingolaRevoca readIdSingolaRevoca(InputStream in) throws DeserializerException {
		return (IdSingolaRevoca) this.xmlToObj(in, IdSingolaRevoca.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdSingolaRevoca}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdSingolaRevoca}
	 * @return Object type {@link it.govpay.orm.IdSingolaRevoca}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdSingolaRevoca readIdSingolaRevoca(byte[] in) throws DeserializerException {
		return (IdSingolaRevoca) this.xmlToObj(in, IdSingolaRevoca.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdSingolaRevoca}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdSingolaRevoca}
	 * @return Object type {@link it.govpay.orm.IdSingolaRevoca}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdSingolaRevoca readIdSingolaRevocaFromString(String in) throws DeserializerException {
		return (IdSingolaRevoca) this.xmlToObj(in.getBytes(), IdSingolaRevoca.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: id-conto-accredito
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdContoAccredito}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdContoAccredito}
	 * @return Object type {@link it.govpay.orm.IdContoAccredito}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdContoAccredito readIdContoAccredito(String fileName) throws DeserializerException {
		return (IdContoAccredito) this.xmlToObj(fileName, IdContoAccredito.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdContoAccredito}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdContoAccredito}
	 * @return Object type {@link it.govpay.orm.IdContoAccredito}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdContoAccredito readIdContoAccredito(File file) throws DeserializerException {
		return (IdContoAccredito) this.xmlToObj(file, IdContoAccredito.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdContoAccredito}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdContoAccredito}
	 * @return Object type {@link it.govpay.orm.IdContoAccredito}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdContoAccredito readIdContoAccredito(InputStream in) throws DeserializerException {
		return (IdContoAccredito) this.xmlToObj(in, IdContoAccredito.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdContoAccredito}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdContoAccredito}
	 * @return Object type {@link it.govpay.orm.IdContoAccredito}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdContoAccredito readIdContoAccredito(byte[] in) throws DeserializerException {
		return (IdContoAccredito) this.xmlToObj(in, IdContoAccredito.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdContoAccredito}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdContoAccredito}
	 * @return Object type {@link it.govpay.orm.IdContoAccredito}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdContoAccredito readIdContoAccreditoFromString(String in) throws DeserializerException {
		return (IdContoAccredito) this.xmlToObj(in.getBytes(), IdContoAccredito.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: Stazione
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.Stazione}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Stazione}
	 * @return Object type {@link it.govpay.orm.Stazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Stazione readStazione(String fileName) throws DeserializerException {
		return (Stazione) this.xmlToObj(fileName, Stazione.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.Stazione}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Stazione}
	 * @return Object type {@link it.govpay.orm.Stazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Stazione readStazione(File file) throws DeserializerException {
		return (Stazione) this.xmlToObj(file, Stazione.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.Stazione}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.Stazione}
	 * @return Object type {@link it.govpay.orm.Stazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Stazione readStazione(InputStream in) throws DeserializerException {
		return (Stazione) this.xmlToObj(in, Stazione.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.Stazione}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.Stazione}
	 * @return Object type {@link it.govpay.orm.Stazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Stazione readStazione(byte[] in) throws DeserializerException {
		return (Stazione) this.xmlToObj(in, Stazione.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.Stazione}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.Stazione}
	 * @return Object type {@link it.govpay.orm.Stazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Stazione readStazioneFromString(String in) throws DeserializerException {
		return (Stazione) this.xmlToObj(in.getBytes(), Stazione.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: id-rilevamento
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdRilevamento}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdRilevamento}
	 * @return Object type {@link it.govpay.orm.IdRilevamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdRilevamento readIdRilevamento(String fileName) throws DeserializerException {
		return (IdRilevamento) this.xmlToObj(fileName, IdRilevamento.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdRilevamento}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdRilevamento}
	 * @return Object type {@link it.govpay.orm.IdRilevamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdRilevamento readIdRilevamento(File file) throws DeserializerException {
		return (IdRilevamento) this.xmlToObj(file, IdRilevamento.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdRilevamento}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdRilevamento}
	 * @return Object type {@link it.govpay.orm.IdRilevamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdRilevamento readIdRilevamento(InputStream in) throws DeserializerException {
		return (IdRilevamento) this.xmlToObj(in, IdRilevamento.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdRilevamento}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdRilevamento}
	 * @return Object type {@link it.govpay.orm.IdRilevamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdRilevamento readIdRilevamento(byte[] in) throws DeserializerException {
		return (IdRilevamento) this.xmlToObj(in, IdRilevamento.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdRilevamento}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdRilevamento}
	 * @return Object type {@link it.govpay.orm.IdRilevamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdRilevamento readIdRilevamentoFromString(String in) throws DeserializerException {
		return (IdRilevamento) this.xmlToObj(in.getBytes(), IdRilevamento.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: Pagamento
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.Pagamento}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Pagamento}
	 * @return Object type {@link it.govpay.orm.Pagamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Pagamento readPagamento(String fileName) throws DeserializerException {
		return (Pagamento) this.xmlToObj(fileName, Pagamento.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.Pagamento}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Pagamento}
	 * @return Object type {@link it.govpay.orm.Pagamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Pagamento readPagamento(File file) throws DeserializerException {
		return (Pagamento) this.xmlToObj(file, Pagamento.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.Pagamento}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.Pagamento}
	 * @return Object type {@link it.govpay.orm.Pagamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Pagamento readPagamento(InputStream in) throws DeserializerException {
		return (Pagamento) this.xmlToObj(in, Pagamento.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.Pagamento}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.Pagamento}
	 * @return Object type {@link it.govpay.orm.Pagamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Pagamento readPagamento(byte[] in) throws DeserializerException {
		return (Pagamento) this.xmlToObj(in, Pagamento.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.Pagamento}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.Pagamento}
	 * @return Object type {@link it.govpay.orm.Pagamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Pagamento readPagamentoFromString(String in) throws DeserializerException {
		return (Pagamento) this.xmlToObj(in.getBytes(), Pagamento.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: Notifica
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.Notifica}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Notifica}
	 * @return Object type {@link it.govpay.orm.Notifica}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Notifica readNotifica(String fileName) throws DeserializerException {
		return (Notifica) this.xmlToObj(fileName, Notifica.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.Notifica}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Notifica}
	 * @return Object type {@link it.govpay.orm.Notifica}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Notifica readNotifica(File file) throws DeserializerException {
		return (Notifica) this.xmlToObj(file, Notifica.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.Notifica}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.Notifica}
	 * @return Object type {@link it.govpay.orm.Notifica}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Notifica readNotifica(InputStream in) throws DeserializerException {
		return (Notifica) this.xmlToObj(in, Notifica.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.Notifica}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.Notifica}
	 * @return Object type {@link it.govpay.orm.Notifica}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Notifica readNotifica(byte[] in) throws DeserializerException {
		return (Notifica) this.xmlToObj(in, Notifica.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.Notifica}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.Notifica}
	 * @return Object type {@link it.govpay.orm.Notifica}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Notifica readNotificaFromString(String in) throws DeserializerException {
		return (Notifica) this.xmlToObj(in.getBytes(), Notifica.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: IUV
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IUV}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IUV}
	 * @return Object type {@link it.govpay.orm.IUV}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IUV readIUV(String fileName) throws DeserializerException {
		return (IUV) this.xmlToObj(fileName, IUV.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IUV}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IUV}
	 * @return Object type {@link it.govpay.orm.IUV}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IUV readIUV(File file) throws DeserializerException {
		return (IUV) this.xmlToObj(file, IUV.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IUV}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IUV}
	 * @return Object type {@link it.govpay.orm.IUV}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IUV readIUV(InputStream in) throws DeserializerException {
		return (IUV) this.xmlToObj(in, IUV.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IUV}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IUV}
	 * @return Object type {@link it.govpay.orm.IUV}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IUV readIUV(byte[] in) throws DeserializerException {
		return (IUV) this.xmlToObj(in, IUV.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IUV}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IUV}
	 * @return Object type {@link it.govpay.orm.IUV}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IUV readIUVFromString(String in) throws DeserializerException {
		return (IUV) this.xmlToObj(in.getBytes(), IUV.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: id-anagrafica
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdAnagrafica}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdAnagrafica}
	 * @return Object type {@link it.govpay.orm.IdAnagrafica}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdAnagrafica readIdAnagrafica(String fileName) throws DeserializerException {
		return (IdAnagrafica) this.xmlToObj(fileName, IdAnagrafica.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdAnagrafica}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdAnagrafica}
	 * @return Object type {@link it.govpay.orm.IdAnagrafica}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdAnagrafica readIdAnagrafica(File file) throws DeserializerException {
		return (IdAnagrafica) this.xmlToObj(file, IdAnagrafica.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdAnagrafica}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdAnagrafica}
	 * @return Object type {@link it.govpay.orm.IdAnagrafica}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdAnagrafica readIdAnagrafica(InputStream in) throws DeserializerException {
		return (IdAnagrafica) this.xmlToObj(in, IdAnagrafica.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdAnagrafica}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdAnagrafica}
	 * @return Object type {@link it.govpay.orm.IdAnagrafica}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdAnagrafica readIdAnagrafica(byte[] in) throws DeserializerException {
		return (IdAnagrafica) this.xmlToObj(in, IdAnagrafica.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdAnagrafica}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdAnagrafica}
	 * @return Object type {@link it.govpay.orm.IdAnagrafica}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdAnagrafica readIdAnagraficaFromString(String in) throws DeserializerException {
		return (IdAnagrafica) this.xmlToObj(in.getBytes(), IdAnagrafica.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: IbanAccredito
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IbanAccredito}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IbanAccredito}
	 * @return Object type {@link it.govpay.orm.IbanAccredito}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IbanAccredito readIbanAccredito(String fileName) throws DeserializerException {
		return (IbanAccredito) this.xmlToObj(fileName, IbanAccredito.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IbanAccredito}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IbanAccredito}
	 * @return Object type {@link it.govpay.orm.IbanAccredito}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IbanAccredito readIbanAccredito(File file) throws DeserializerException {
		return (IbanAccredito) this.xmlToObj(file, IbanAccredito.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IbanAccredito}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IbanAccredito}
	 * @return Object type {@link it.govpay.orm.IbanAccredito}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IbanAccredito readIbanAccredito(InputStream in) throws DeserializerException {
		return (IbanAccredito) this.xmlToObj(in, IbanAccredito.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IbanAccredito}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IbanAccredito}
	 * @return Object type {@link it.govpay.orm.IbanAccredito}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IbanAccredito readIbanAccredito(byte[] in) throws DeserializerException {
		return (IbanAccredito) this.xmlToObj(in, IbanAccredito.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IbanAccredito}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IbanAccredito}
	 * @return Object type {@link it.govpay.orm.IbanAccredito}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IbanAccredito readIbanAccreditoFromString(String in) throws DeserializerException {
		return (IbanAccredito) this.xmlToObj(in.getBytes(), IbanAccredito.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: Connettore
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.Connettore}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Connettore}
	 * @return Object type {@link it.govpay.orm.Connettore}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Connettore readConnettore(String fileName) throws DeserializerException {
		return (Connettore) this.xmlToObj(fileName, Connettore.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.Connettore}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Connettore}
	 * @return Object type {@link it.govpay.orm.Connettore}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Connettore readConnettore(File file) throws DeserializerException {
		return (Connettore) this.xmlToObj(file, Connettore.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.Connettore}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.Connettore}
	 * @return Object type {@link it.govpay.orm.Connettore}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Connettore readConnettore(InputStream in) throws DeserializerException {
		return (Connettore) this.xmlToObj(in, Connettore.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.Connettore}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.Connettore}
	 * @return Object type {@link it.govpay.orm.Connettore}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Connettore readConnettore(byte[] in) throws DeserializerException {
		return (Connettore) this.xmlToObj(in, Connettore.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.Connettore}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.Connettore}
	 * @return Object type {@link it.govpay.orm.Connettore}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Connettore readConnettoreFromString(String in) throws DeserializerException {
		return (Connettore) this.xmlToObj(in.getBytes(), Connettore.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: Tributo
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.Tributo}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Tributo}
	 * @return Object type {@link it.govpay.orm.Tributo}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Tributo readTributo(String fileName) throws DeserializerException {
		return (Tributo) this.xmlToObj(fileName, Tributo.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.Tributo}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Tributo}
	 * @return Object type {@link it.govpay.orm.Tributo}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Tributo readTributo(File file) throws DeserializerException {
		return (Tributo) this.xmlToObj(file, Tributo.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.Tributo}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.Tributo}
	 * @return Object type {@link it.govpay.orm.Tributo}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Tributo readTributo(InputStream in) throws DeserializerException {
		return (Tributo) this.xmlToObj(in, Tributo.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.Tributo}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.Tributo}
	 * @return Object type {@link it.govpay.orm.Tributo}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Tributo readTributo(byte[] in) throws DeserializerException {
		return (Tributo) this.xmlToObj(in, Tributo.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.Tributo}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.Tributo}
	 * @return Object type {@link it.govpay.orm.Tributo}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Tributo readTributoFromString(String in) throws DeserializerException {
		return (Tributo) this.xmlToObj(in.getBytes(), Tributo.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: id-connettore
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdConnettore}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdConnettore}
	 * @return Object type {@link it.govpay.orm.IdConnettore}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdConnettore readIdConnettore(String fileName) throws DeserializerException {
		return (IdConnettore) this.xmlToObj(fileName, IdConnettore.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdConnettore}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdConnettore}
	 * @return Object type {@link it.govpay.orm.IdConnettore}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdConnettore readIdConnettore(File file) throws DeserializerException {
		return (IdConnettore) this.xmlToObj(file, IdConnettore.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdConnettore}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdConnettore}
	 * @return Object type {@link it.govpay.orm.IdConnettore}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdConnettore readIdConnettore(InputStream in) throws DeserializerException {
		return (IdConnettore) this.xmlToObj(in, IdConnettore.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdConnettore}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdConnettore}
	 * @return Object type {@link it.govpay.orm.IdConnettore}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdConnettore readIdConnettore(byte[] in) throws DeserializerException {
		return (IdConnettore) this.xmlToObj(in, IdConnettore.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdConnettore}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdConnettore}
	 * @return Object type {@link it.govpay.orm.IdConnettore}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdConnettore readIdConnettoreFromString(String in) throws DeserializerException {
		return (IdConnettore) this.xmlToObj(in.getBytes(), IdConnettore.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: id-evento
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdEvento}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdEvento}
	 * @return Object type {@link it.govpay.orm.IdEvento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdEvento readIdEvento(String fileName) throws DeserializerException {
		return (IdEvento) this.xmlToObj(fileName, IdEvento.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdEvento}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdEvento}
	 * @return Object type {@link it.govpay.orm.IdEvento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdEvento readIdEvento(File file) throws DeserializerException {
		return (IdEvento) this.xmlToObj(file, IdEvento.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdEvento}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdEvento}
	 * @return Object type {@link it.govpay.orm.IdEvento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdEvento readIdEvento(InputStream in) throws DeserializerException {
		return (IdEvento) this.xmlToObj(in, IdEvento.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdEvento}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdEvento}
	 * @return Object type {@link it.govpay.orm.IdEvento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdEvento readIdEvento(byte[] in) throws DeserializerException {
		return (IdEvento) this.xmlToObj(in, IdEvento.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdEvento}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdEvento}
	 * @return Object type {@link it.govpay.orm.IdEvento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdEvento readIdEventoFromString(String in) throws DeserializerException {
		return (IdEvento) this.xmlToObj(in.getBytes(), IdEvento.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: Evento
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.Evento}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Evento}
	 * @return Object type {@link it.govpay.orm.Evento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Evento readEvento(String fileName) throws DeserializerException {
		return (Evento) this.xmlToObj(fileName, Evento.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.Evento}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Evento}
	 * @return Object type {@link it.govpay.orm.Evento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Evento readEvento(File file) throws DeserializerException {
		return (Evento) this.xmlToObj(file, Evento.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.Evento}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.Evento}
	 * @return Object type {@link it.govpay.orm.Evento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Evento readEvento(InputStream in) throws DeserializerException {
		return (Evento) this.xmlToObj(in, Evento.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.Evento}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.Evento}
	 * @return Object type {@link it.govpay.orm.Evento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Evento readEvento(byte[] in) throws DeserializerException {
		return (Evento) this.xmlToObj(in, Evento.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.Evento}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.Evento}
	 * @return Object type {@link it.govpay.orm.Evento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Evento readEventoFromString(String in) throws DeserializerException {
		return (Evento) this.xmlToObj(in.getBytes(), Evento.class);
	}	
	
	
	

}
