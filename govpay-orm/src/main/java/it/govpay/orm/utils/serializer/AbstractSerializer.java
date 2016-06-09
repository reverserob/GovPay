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

import org.openspcoop2.generic_project.exception.SerializerException;
import org.openspcoop2.utils.beans.WriteToSerializerType;
import org.openspcoop2.utils.xml.JaxbUtils;

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

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.File;
import java.lang.reflect.Method;

import javax.xml.bind.JAXBElement;

/**     
 * XML Serializer of beans
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public abstract class AbstractSerializer {


	protected abstract WriteToSerializerType getType();
	
	protected void _objToXml(OutputStream out, Class<?> c, Object object,
			boolean prettyPrint) throws Exception {
		if(object instanceof JAXBElement){
			// solo per il tipo WriteToSerializerType.JAXB
			JaxbUtils.objToXml(out, c, object, prettyPrint);
		}else{
			Method m = c.getMethod("writeTo", OutputStream.class, WriteToSerializerType.class);
			m.invoke(object, out, this.getType());
		}
	}
	
	protected void objToXml(OutputStream out,Class<?> c,Object object,boolean prettyPrint) throws SerializerException{
		try{
			this._objToXml(out, c, object, prettyPrint);
		}catch(Exception e){
			throw new SerializerException(e.getMessage(), e);
		}
		finally{
			try{
				out.flush();
			}catch(Exception e){}
		}
	}
	protected void objToXml(String fileName,Class<?> c,Object object,boolean prettyPrint) throws SerializerException{
		try{
			this.objToXml(new File(fileName), c, object, prettyPrint);
		}catch(Exception e){
			throw new SerializerException(e.getMessage(), e);
		}
	}
	protected void objToXml(File file,Class<?> c,Object object,boolean prettyPrint) throws SerializerException{
		FileOutputStream fout = null;
		try{
			fout = new FileOutputStream(file);
			this._objToXml(fout, c, object, prettyPrint);
		}catch(Exception e){
			throw new SerializerException(e.getMessage(), e);
		}
		finally{
			try{
				fout.flush();
			}catch(Exception e){}
			try{
				fout.close();
			}catch(Exception e){}
		}
	}
	protected ByteArrayOutputStream objToXml(Class<?> c,Object object,boolean prettyPrint) throws SerializerException{
		ByteArrayOutputStream bout = null;
		try{
			bout = new ByteArrayOutputStream();
			this._objToXml(bout, c, object, prettyPrint);
		}catch(Exception e){
			throw new SerializerException(e.getMessage(), e);
		}
		finally{
			try{
				bout.flush();
			}catch(Exception e){}
			try{
				bout.close();
			}catch(Exception e){}
		}
		return bout;
	}




	/*
	 =================================================================================
	 Object: id-dominio
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idDominio</var> of type {@link it.govpay.orm.IdDominio}
	 * 
	 * @param fileName Xml file to serialize the object <var>idDominio</var>
	 * @param idDominio Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdDominio idDominio) throws SerializerException {
		this.objToXml(fileName, IdDominio.class, idDominio, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idDominio</var> of type {@link it.govpay.orm.IdDominio}
	 * 
	 * @param fileName Xml file to serialize the object <var>idDominio</var>
	 * @param idDominio Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdDominio idDominio,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdDominio.class, idDominio, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idDominio</var> of type {@link it.govpay.orm.IdDominio}
	 * 
	 * @param file Xml file to serialize the object <var>idDominio</var>
	 * @param idDominio Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdDominio idDominio) throws SerializerException {
		this.objToXml(file, IdDominio.class, idDominio, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idDominio</var> of type {@link it.govpay.orm.IdDominio}
	 * 
	 * @param file Xml file to serialize the object <var>idDominio</var>
	 * @param idDominio Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdDominio idDominio,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdDominio.class, idDominio, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idDominio</var> of type {@link it.govpay.orm.IdDominio}
	 * 
	 * @param out OutputStream to serialize the object <var>idDominio</var>
	 * @param idDominio Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdDominio idDominio) throws SerializerException {
		this.objToXml(out, IdDominio.class, idDominio, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idDominio</var> of type {@link it.govpay.orm.IdDominio}
	 * 
	 * @param out OutputStream to serialize the object <var>idDominio</var>
	 * @param idDominio Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdDominio idDominio,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdDominio.class, idDominio, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idDominio</var> of type {@link it.govpay.orm.IdDominio}
	 * 
	 * @param idDominio Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdDominio idDominio) throws SerializerException {
		return this.objToXml(IdDominio.class, idDominio, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idDominio</var> of type {@link it.govpay.orm.IdDominio}
	 * 
	 * @param idDominio Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdDominio idDominio,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdDominio.class, idDominio, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idDominio</var> of type {@link it.govpay.orm.IdDominio}
	 * 
	 * @param idDominio Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdDominio idDominio) throws SerializerException {
		return this.objToXml(IdDominio.class, idDominio, false).toString();
	}
	/**
	 * Serialize to String the object <var>idDominio</var> of type {@link it.govpay.orm.IdDominio}
	 * 
	 * @param idDominio Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdDominio idDominio,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdDominio.class, idDominio, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: ApplicazioneDominio
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>applicazioneDominio</var> of type {@link it.govpay.orm.ApplicazioneDominio}
	 * 
	 * @param fileName Xml file to serialize the object <var>applicazioneDominio</var>
	 * @param applicazioneDominio Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,ApplicazioneDominio applicazioneDominio) throws SerializerException {
		this.objToXml(fileName, ApplicazioneDominio.class, applicazioneDominio, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>applicazioneDominio</var> of type {@link it.govpay.orm.ApplicazioneDominio}
	 * 
	 * @param fileName Xml file to serialize the object <var>applicazioneDominio</var>
	 * @param applicazioneDominio Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,ApplicazioneDominio applicazioneDominio,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, ApplicazioneDominio.class, applicazioneDominio, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>applicazioneDominio</var> of type {@link it.govpay.orm.ApplicazioneDominio}
	 * 
	 * @param file Xml file to serialize the object <var>applicazioneDominio</var>
	 * @param applicazioneDominio Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,ApplicazioneDominio applicazioneDominio) throws SerializerException {
		this.objToXml(file, ApplicazioneDominio.class, applicazioneDominio, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>applicazioneDominio</var> of type {@link it.govpay.orm.ApplicazioneDominio}
	 * 
	 * @param file Xml file to serialize the object <var>applicazioneDominio</var>
	 * @param applicazioneDominio Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,ApplicazioneDominio applicazioneDominio,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, ApplicazioneDominio.class, applicazioneDominio, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>applicazioneDominio</var> of type {@link it.govpay.orm.ApplicazioneDominio}
	 * 
	 * @param out OutputStream to serialize the object <var>applicazioneDominio</var>
	 * @param applicazioneDominio Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,ApplicazioneDominio applicazioneDominio) throws SerializerException {
		this.objToXml(out, ApplicazioneDominio.class, applicazioneDominio, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>applicazioneDominio</var> of type {@link it.govpay.orm.ApplicazioneDominio}
	 * 
	 * @param out OutputStream to serialize the object <var>applicazioneDominio</var>
	 * @param applicazioneDominio Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,ApplicazioneDominio applicazioneDominio,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, ApplicazioneDominio.class, applicazioneDominio, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>applicazioneDominio</var> of type {@link it.govpay.orm.ApplicazioneDominio}
	 * 
	 * @param applicazioneDominio Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(ApplicazioneDominio applicazioneDominio) throws SerializerException {
		return this.objToXml(ApplicazioneDominio.class, applicazioneDominio, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>applicazioneDominio</var> of type {@link it.govpay.orm.ApplicazioneDominio}
	 * 
	 * @param applicazioneDominio Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(ApplicazioneDominio applicazioneDominio,boolean prettyPrint) throws SerializerException {
		return this.objToXml(ApplicazioneDominio.class, applicazioneDominio, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>applicazioneDominio</var> of type {@link it.govpay.orm.ApplicazioneDominio}
	 * 
	 * @param applicazioneDominio Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(ApplicazioneDominio applicazioneDominio) throws SerializerException {
		return this.objToXml(ApplicazioneDominio.class, applicazioneDominio, false).toString();
	}
	/**
	 * Serialize to String the object <var>applicazioneDominio</var> of type {@link it.govpay.orm.ApplicazioneDominio}
	 * 
	 * @param applicazioneDominio Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(ApplicazioneDominio applicazioneDominio,boolean prettyPrint) throws SerializerException {
		return this.objToXml(ApplicazioneDominio.class, applicazioneDominio, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: Versamento
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>versamento</var> of type {@link it.govpay.orm.Versamento}
	 * 
	 * @param fileName Xml file to serialize the object <var>versamento</var>
	 * @param versamento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Versamento versamento) throws SerializerException {
		this.objToXml(fileName, Versamento.class, versamento, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>versamento</var> of type {@link it.govpay.orm.Versamento}
	 * 
	 * @param fileName Xml file to serialize the object <var>versamento</var>
	 * @param versamento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Versamento versamento,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, Versamento.class, versamento, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>versamento</var> of type {@link it.govpay.orm.Versamento}
	 * 
	 * @param file Xml file to serialize the object <var>versamento</var>
	 * @param versamento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Versamento versamento) throws SerializerException {
		this.objToXml(file, Versamento.class, versamento, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>versamento</var> of type {@link it.govpay.orm.Versamento}
	 * 
	 * @param file Xml file to serialize the object <var>versamento</var>
	 * @param versamento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Versamento versamento,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, Versamento.class, versamento, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>versamento</var> of type {@link it.govpay.orm.Versamento}
	 * 
	 * @param out OutputStream to serialize the object <var>versamento</var>
	 * @param versamento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Versamento versamento) throws SerializerException {
		this.objToXml(out, Versamento.class, versamento, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>versamento</var> of type {@link it.govpay.orm.Versamento}
	 * 
	 * @param out OutputStream to serialize the object <var>versamento</var>
	 * @param versamento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Versamento versamento,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, Versamento.class, versamento, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>versamento</var> of type {@link it.govpay.orm.Versamento}
	 * 
	 * @param versamento Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Versamento versamento) throws SerializerException {
		return this.objToXml(Versamento.class, versamento, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>versamento</var> of type {@link it.govpay.orm.Versamento}
	 * 
	 * @param versamento Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Versamento versamento,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Versamento.class, versamento, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>versamento</var> of type {@link it.govpay.orm.Versamento}
	 * 
	 * @param versamento Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Versamento versamento) throws SerializerException {
		return this.objToXml(Versamento.class, versamento, false).toString();
	}
	/**
	 * Serialize to String the object <var>versamento</var> of type {@link it.govpay.orm.Versamento}
	 * 
	 * @param versamento Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Versamento versamento,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Versamento.class, versamento, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: id-uo
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idUo</var> of type {@link it.govpay.orm.IdUo}
	 * 
	 * @param fileName Xml file to serialize the object <var>idUo</var>
	 * @param idUo Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdUo idUo) throws SerializerException {
		this.objToXml(fileName, IdUo.class, idUo, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idUo</var> of type {@link it.govpay.orm.IdUo}
	 * 
	 * @param fileName Xml file to serialize the object <var>idUo</var>
	 * @param idUo Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdUo idUo,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdUo.class, idUo, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idUo</var> of type {@link it.govpay.orm.IdUo}
	 * 
	 * @param file Xml file to serialize the object <var>idUo</var>
	 * @param idUo Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdUo idUo) throws SerializerException {
		this.objToXml(file, IdUo.class, idUo, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idUo</var> of type {@link it.govpay.orm.IdUo}
	 * 
	 * @param file Xml file to serialize the object <var>idUo</var>
	 * @param idUo Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdUo idUo,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdUo.class, idUo, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idUo</var> of type {@link it.govpay.orm.IdUo}
	 * 
	 * @param out OutputStream to serialize the object <var>idUo</var>
	 * @param idUo Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdUo idUo) throws SerializerException {
		this.objToXml(out, IdUo.class, idUo, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idUo</var> of type {@link it.govpay.orm.IdUo}
	 * 
	 * @param out OutputStream to serialize the object <var>idUo</var>
	 * @param idUo Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdUo idUo,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdUo.class, idUo, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idUo</var> of type {@link it.govpay.orm.IdUo}
	 * 
	 * @param idUo Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdUo idUo) throws SerializerException {
		return this.objToXml(IdUo.class, idUo, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idUo</var> of type {@link it.govpay.orm.IdUo}
	 * 
	 * @param idUo Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdUo idUo,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdUo.class, idUo, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idUo</var> of type {@link it.govpay.orm.IdUo}
	 * 
	 * @param idUo Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdUo idUo) throws SerializerException {
		return this.objToXml(IdUo.class, idUo, false).toString();
	}
	/**
	 * Serialize to String the object <var>idUo</var> of type {@link it.govpay.orm.IdUo}
	 * 
	 * @param idUo Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdUo idUo,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdUo.class, idUo, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: id-applicazione
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idApplicazione</var> of type {@link it.govpay.orm.IdApplicazione}
	 * 
	 * @param fileName Xml file to serialize the object <var>idApplicazione</var>
	 * @param idApplicazione Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdApplicazione idApplicazione) throws SerializerException {
		this.objToXml(fileName, IdApplicazione.class, idApplicazione, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idApplicazione</var> of type {@link it.govpay.orm.IdApplicazione}
	 * 
	 * @param fileName Xml file to serialize the object <var>idApplicazione</var>
	 * @param idApplicazione Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdApplicazione idApplicazione,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdApplicazione.class, idApplicazione, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idApplicazione</var> of type {@link it.govpay.orm.IdApplicazione}
	 * 
	 * @param file Xml file to serialize the object <var>idApplicazione</var>
	 * @param idApplicazione Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdApplicazione idApplicazione) throws SerializerException {
		this.objToXml(file, IdApplicazione.class, idApplicazione, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idApplicazione</var> of type {@link it.govpay.orm.IdApplicazione}
	 * 
	 * @param file Xml file to serialize the object <var>idApplicazione</var>
	 * @param idApplicazione Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdApplicazione idApplicazione,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdApplicazione.class, idApplicazione, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idApplicazione</var> of type {@link it.govpay.orm.IdApplicazione}
	 * 
	 * @param out OutputStream to serialize the object <var>idApplicazione</var>
	 * @param idApplicazione Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdApplicazione idApplicazione) throws SerializerException {
		this.objToXml(out, IdApplicazione.class, idApplicazione, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idApplicazione</var> of type {@link it.govpay.orm.IdApplicazione}
	 * 
	 * @param out OutputStream to serialize the object <var>idApplicazione</var>
	 * @param idApplicazione Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdApplicazione idApplicazione,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdApplicazione.class, idApplicazione, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idApplicazione</var> of type {@link it.govpay.orm.IdApplicazione}
	 * 
	 * @param idApplicazione Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdApplicazione idApplicazione) throws SerializerException {
		return this.objToXml(IdApplicazione.class, idApplicazione, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idApplicazione</var> of type {@link it.govpay.orm.IdApplicazione}
	 * 
	 * @param idApplicazione Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdApplicazione idApplicazione,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdApplicazione.class, idApplicazione, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idApplicazione</var> of type {@link it.govpay.orm.IdApplicazione}
	 * 
	 * @param idApplicazione Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdApplicazione idApplicazione) throws SerializerException {
		return this.objToXml(IdApplicazione.class, idApplicazione, false).toString();
	}
	/**
	 * Serialize to String the object <var>idApplicazione</var> of type {@link it.govpay.orm.IdApplicazione}
	 * 
	 * @param idApplicazione Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdApplicazione idApplicazione,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdApplicazione.class, idApplicazione, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: Psp
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>psp</var> of type {@link it.govpay.orm.Psp}
	 * 
	 * @param fileName Xml file to serialize the object <var>psp</var>
	 * @param psp Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Psp psp) throws SerializerException {
		this.objToXml(fileName, Psp.class, psp, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>psp</var> of type {@link it.govpay.orm.Psp}
	 * 
	 * @param fileName Xml file to serialize the object <var>psp</var>
	 * @param psp Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Psp psp,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, Psp.class, psp, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>psp</var> of type {@link it.govpay.orm.Psp}
	 * 
	 * @param file Xml file to serialize the object <var>psp</var>
	 * @param psp Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Psp psp) throws SerializerException {
		this.objToXml(file, Psp.class, psp, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>psp</var> of type {@link it.govpay.orm.Psp}
	 * 
	 * @param file Xml file to serialize the object <var>psp</var>
	 * @param psp Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Psp psp,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, Psp.class, psp, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>psp</var> of type {@link it.govpay.orm.Psp}
	 * 
	 * @param out OutputStream to serialize the object <var>psp</var>
	 * @param psp Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Psp psp) throws SerializerException {
		this.objToXml(out, Psp.class, psp, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>psp</var> of type {@link it.govpay.orm.Psp}
	 * 
	 * @param out OutputStream to serialize the object <var>psp</var>
	 * @param psp Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Psp psp,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, Psp.class, psp, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>psp</var> of type {@link it.govpay.orm.Psp}
	 * 
	 * @param psp Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Psp psp) throws SerializerException {
		return this.objToXml(Psp.class, psp, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>psp</var> of type {@link it.govpay.orm.Psp}
	 * 
	 * @param psp Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Psp psp,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Psp.class, psp, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>psp</var> of type {@link it.govpay.orm.Psp}
	 * 
	 * @param psp Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Psp psp) throws SerializerException {
		return this.objToXml(Psp.class, psp, false).toString();
	}
	/**
	 * Serialize to String the object <var>psp</var> of type {@link it.govpay.orm.Psp}
	 * 
	 * @param psp Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Psp psp,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Psp.class, psp, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: id-versamento
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idVersamento</var> of type {@link it.govpay.orm.IdVersamento}
	 * 
	 * @param fileName Xml file to serialize the object <var>idVersamento</var>
	 * @param idVersamento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdVersamento idVersamento) throws SerializerException {
		this.objToXml(fileName, IdVersamento.class, idVersamento, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idVersamento</var> of type {@link it.govpay.orm.IdVersamento}
	 * 
	 * @param fileName Xml file to serialize the object <var>idVersamento</var>
	 * @param idVersamento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdVersamento idVersamento,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdVersamento.class, idVersamento, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idVersamento</var> of type {@link it.govpay.orm.IdVersamento}
	 * 
	 * @param file Xml file to serialize the object <var>idVersamento</var>
	 * @param idVersamento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdVersamento idVersamento) throws SerializerException {
		this.objToXml(file, IdVersamento.class, idVersamento, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idVersamento</var> of type {@link it.govpay.orm.IdVersamento}
	 * 
	 * @param file Xml file to serialize the object <var>idVersamento</var>
	 * @param idVersamento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdVersamento idVersamento,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdVersamento.class, idVersamento, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idVersamento</var> of type {@link it.govpay.orm.IdVersamento}
	 * 
	 * @param out OutputStream to serialize the object <var>idVersamento</var>
	 * @param idVersamento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdVersamento idVersamento) throws SerializerException {
		this.objToXml(out, IdVersamento.class, idVersamento, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idVersamento</var> of type {@link it.govpay.orm.IdVersamento}
	 * 
	 * @param out OutputStream to serialize the object <var>idVersamento</var>
	 * @param idVersamento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdVersamento idVersamento,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdVersamento.class, idVersamento, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idVersamento</var> of type {@link it.govpay.orm.IdVersamento}
	 * 
	 * @param idVersamento Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdVersamento idVersamento) throws SerializerException {
		return this.objToXml(IdVersamento.class, idVersamento, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idVersamento</var> of type {@link it.govpay.orm.IdVersamento}
	 * 
	 * @param idVersamento Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdVersamento idVersamento,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdVersamento.class, idVersamento, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idVersamento</var> of type {@link it.govpay.orm.IdVersamento}
	 * 
	 * @param idVersamento Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdVersamento idVersamento) throws SerializerException {
		return this.objToXml(IdVersamento.class, idVersamento, false).toString();
	}
	/**
	 * Serialize to String the object <var>idVersamento</var> of type {@link it.govpay.orm.IdVersamento}
	 * 
	 * @param idVersamento Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdVersamento idVersamento,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdVersamento.class, idVersamento, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: id-intermediario
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idIntermediario</var> of type {@link it.govpay.orm.IdIntermediario}
	 * 
	 * @param fileName Xml file to serialize the object <var>idIntermediario</var>
	 * @param idIntermediario Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdIntermediario idIntermediario) throws SerializerException {
		this.objToXml(fileName, IdIntermediario.class, idIntermediario, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idIntermediario</var> of type {@link it.govpay.orm.IdIntermediario}
	 * 
	 * @param fileName Xml file to serialize the object <var>idIntermediario</var>
	 * @param idIntermediario Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdIntermediario idIntermediario,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdIntermediario.class, idIntermediario, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idIntermediario</var> of type {@link it.govpay.orm.IdIntermediario}
	 * 
	 * @param file Xml file to serialize the object <var>idIntermediario</var>
	 * @param idIntermediario Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdIntermediario idIntermediario) throws SerializerException {
		this.objToXml(file, IdIntermediario.class, idIntermediario, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idIntermediario</var> of type {@link it.govpay.orm.IdIntermediario}
	 * 
	 * @param file Xml file to serialize the object <var>idIntermediario</var>
	 * @param idIntermediario Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdIntermediario idIntermediario,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdIntermediario.class, idIntermediario, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idIntermediario</var> of type {@link it.govpay.orm.IdIntermediario}
	 * 
	 * @param out OutputStream to serialize the object <var>idIntermediario</var>
	 * @param idIntermediario Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdIntermediario idIntermediario) throws SerializerException {
		this.objToXml(out, IdIntermediario.class, idIntermediario, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idIntermediario</var> of type {@link it.govpay.orm.IdIntermediario}
	 * 
	 * @param out OutputStream to serialize the object <var>idIntermediario</var>
	 * @param idIntermediario Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdIntermediario idIntermediario,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdIntermediario.class, idIntermediario, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idIntermediario</var> of type {@link it.govpay.orm.IdIntermediario}
	 * 
	 * @param idIntermediario Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdIntermediario idIntermediario) throws SerializerException {
		return this.objToXml(IdIntermediario.class, idIntermediario, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idIntermediario</var> of type {@link it.govpay.orm.IdIntermediario}
	 * 
	 * @param idIntermediario Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdIntermediario idIntermediario,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdIntermediario.class, idIntermediario, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idIntermediario</var> of type {@link it.govpay.orm.IdIntermediario}
	 * 
	 * @param idIntermediario Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdIntermediario idIntermediario) throws SerializerException {
		return this.objToXml(IdIntermediario.class, idIntermediario, false).toString();
	}
	/**
	 * Serialize to String the object <var>idIntermediario</var> of type {@link it.govpay.orm.IdIntermediario}
	 * 
	 * @param idIntermediario Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdIntermediario idIntermediario,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdIntermediario.class, idIntermediario, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: Uo
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>uo</var> of type {@link it.govpay.orm.Uo}
	 * 
	 * @param fileName Xml file to serialize the object <var>uo</var>
	 * @param uo Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Uo uo) throws SerializerException {
		this.objToXml(fileName, Uo.class, uo, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>uo</var> of type {@link it.govpay.orm.Uo}
	 * 
	 * @param fileName Xml file to serialize the object <var>uo</var>
	 * @param uo Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Uo uo,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, Uo.class, uo, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>uo</var> of type {@link it.govpay.orm.Uo}
	 * 
	 * @param file Xml file to serialize the object <var>uo</var>
	 * @param uo Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Uo uo) throws SerializerException {
		this.objToXml(file, Uo.class, uo, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>uo</var> of type {@link it.govpay.orm.Uo}
	 * 
	 * @param file Xml file to serialize the object <var>uo</var>
	 * @param uo Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Uo uo,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, Uo.class, uo, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>uo</var> of type {@link it.govpay.orm.Uo}
	 * 
	 * @param out OutputStream to serialize the object <var>uo</var>
	 * @param uo Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Uo uo) throws SerializerException {
		this.objToXml(out, Uo.class, uo, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>uo</var> of type {@link it.govpay.orm.Uo}
	 * 
	 * @param out OutputStream to serialize the object <var>uo</var>
	 * @param uo Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Uo uo,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, Uo.class, uo, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>uo</var> of type {@link it.govpay.orm.Uo}
	 * 
	 * @param uo Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Uo uo) throws SerializerException {
		return this.objToXml(Uo.class, uo, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>uo</var> of type {@link it.govpay.orm.Uo}
	 * 
	 * @param uo Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Uo uo,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Uo.class, uo, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>uo</var> of type {@link it.govpay.orm.Uo}
	 * 
	 * @param uo Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Uo uo) throws SerializerException {
		return this.objToXml(Uo.class, uo, false).toString();
	}
	/**
	 * Serialize to String the object <var>uo</var> of type {@link it.govpay.orm.Uo}
	 * 
	 * @param uo Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Uo uo,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Uo.class, uo, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: id-singola-rendicontazione
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idSingolaRendicontazione</var> of type {@link it.govpay.orm.IdSingolaRendicontazione}
	 * 
	 * @param fileName Xml file to serialize the object <var>idSingolaRendicontazione</var>
	 * @param idSingolaRendicontazione Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdSingolaRendicontazione idSingolaRendicontazione) throws SerializerException {
		this.objToXml(fileName, IdSingolaRendicontazione.class, idSingolaRendicontazione, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idSingolaRendicontazione</var> of type {@link it.govpay.orm.IdSingolaRendicontazione}
	 * 
	 * @param fileName Xml file to serialize the object <var>idSingolaRendicontazione</var>
	 * @param idSingolaRendicontazione Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdSingolaRendicontazione idSingolaRendicontazione,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdSingolaRendicontazione.class, idSingolaRendicontazione, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idSingolaRendicontazione</var> of type {@link it.govpay.orm.IdSingolaRendicontazione}
	 * 
	 * @param file Xml file to serialize the object <var>idSingolaRendicontazione</var>
	 * @param idSingolaRendicontazione Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdSingolaRendicontazione idSingolaRendicontazione) throws SerializerException {
		this.objToXml(file, IdSingolaRendicontazione.class, idSingolaRendicontazione, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idSingolaRendicontazione</var> of type {@link it.govpay.orm.IdSingolaRendicontazione}
	 * 
	 * @param file Xml file to serialize the object <var>idSingolaRendicontazione</var>
	 * @param idSingolaRendicontazione Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdSingolaRendicontazione idSingolaRendicontazione,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdSingolaRendicontazione.class, idSingolaRendicontazione, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idSingolaRendicontazione</var> of type {@link it.govpay.orm.IdSingolaRendicontazione}
	 * 
	 * @param out OutputStream to serialize the object <var>idSingolaRendicontazione</var>
	 * @param idSingolaRendicontazione Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdSingolaRendicontazione idSingolaRendicontazione) throws SerializerException {
		this.objToXml(out, IdSingolaRendicontazione.class, idSingolaRendicontazione, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idSingolaRendicontazione</var> of type {@link it.govpay.orm.IdSingolaRendicontazione}
	 * 
	 * @param out OutputStream to serialize the object <var>idSingolaRendicontazione</var>
	 * @param idSingolaRendicontazione Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdSingolaRendicontazione idSingolaRendicontazione,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdSingolaRendicontazione.class, idSingolaRendicontazione, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idSingolaRendicontazione</var> of type {@link it.govpay.orm.IdSingolaRendicontazione}
	 * 
	 * @param idSingolaRendicontazione Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdSingolaRendicontazione idSingolaRendicontazione) throws SerializerException {
		return this.objToXml(IdSingolaRendicontazione.class, idSingolaRendicontazione, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idSingolaRendicontazione</var> of type {@link it.govpay.orm.IdSingolaRendicontazione}
	 * 
	 * @param idSingolaRendicontazione Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdSingolaRendicontazione idSingolaRendicontazione,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdSingolaRendicontazione.class, idSingolaRendicontazione, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idSingolaRendicontazione</var> of type {@link it.govpay.orm.IdSingolaRendicontazione}
	 * 
	 * @param idSingolaRendicontazione Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdSingolaRendicontazione idSingolaRendicontazione) throws SerializerException {
		return this.objToXml(IdSingolaRendicontazione.class, idSingolaRendicontazione, false).toString();
	}
	/**
	 * Serialize to String the object <var>idSingolaRendicontazione</var> of type {@link it.govpay.orm.IdSingolaRendicontazione}
	 * 
	 * @param idSingolaRendicontazione Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdSingolaRendicontazione idSingolaRendicontazione,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdSingolaRendicontazione.class, idSingolaRendicontazione, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: id-psp
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idPsp</var> of type {@link it.govpay.orm.IdPsp}
	 * 
	 * @param fileName Xml file to serialize the object <var>idPsp</var>
	 * @param idPsp Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdPsp idPsp) throws SerializerException {
		this.objToXml(fileName, IdPsp.class, idPsp, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idPsp</var> of type {@link it.govpay.orm.IdPsp}
	 * 
	 * @param fileName Xml file to serialize the object <var>idPsp</var>
	 * @param idPsp Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdPsp idPsp,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdPsp.class, idPsp, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idPsp</var> of type {@link it.govpay.orm.IdPsp}
	 * 
	 * @param file Xml file to serialize the object <var>idPsp</var>
	 * @param idPsp Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdPsp idPsp) throws SerializerException {
		this.objToXml(file, IdPsp.class, idPsp, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idPsp</var> of type {@link it.govpay.orm.IdPsp}
	 * 
	 * @param file Xml file to serialize the object <var>idPsp</var>
	 * @param idPsp Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdPsp idPsp,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdPsp.class, idPsp, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idPsp</var> of type {@link it.govpay.orm.IdPsp}
	 * 
	 * @param out OutputStream to serialize the object <var>idPsp</var>
	 * @param idPsp Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdPsp idPsp) throws SerializerException {
		this.objToXml(out, IdPsp.class, idPsp, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idPsp</var> of type {@link it.govpay.orm.IdPsp}
	 * 
	 * @param out OutputStream to serialize the object <var>idPsp</var>
	 * @param idPsp Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdPsp idPsp,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdPsp.class, idPsp, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idPsp</var> of type {@link it.govpay.orm.IdPsp}
	 * 
	 * @param idPsp Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdPsp idPsp) throws SerializerException {
		return this.objToXml(IdPsp.class, idPsp, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idPsp</var> of type {@link it.govpay.orm.IdPsp}
	 * 
	 * @param idPsp Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdPsp idPsp,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdPsp.class, idPsp, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idPsp</var> of type {@link it.govpay.orm.IdPsp}
	 * 
	 * @param idPsp Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdPsp idPsp) throws SerializerException {
		return this.objToXml(IdPsp.class, idPsp, false).toString();
	}
	/**
	 * Serialize to String the object <var>idPsp</var> of type {@link it.govpay.orm.IdPsp}
	 * 
	 * @param idPsp Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdPsp idPsp,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdPsp.class, idPsp, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: id-operatore
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idOperatore</var> of type {@link it.govpay.orm.IdOperatore}
	 * 
	 * @param fileName Xml file to serialize the object <var>idOperatore</var>
	 * @param idOperatore Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdOperatore idOperatore) throws SerializerException {
		this.objToXml(fileName, IdOperatore.class, idOperatore, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idOperatore</var> of type {@link it.govpay.orm.IdOperatore}
	 * 
	 * @param fileName Xml file to serialize the object <var>idOperatore</var>
	 * @param idOperatore Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdOperatore idOperatore,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdOperatore.class, idOperatore, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idOperatore</var> of type {@link it.govpay.orm.IdOperatore}
	 * 
	 * @param file Xml file to serialize the object <var>idOperatore</var>
	 * @param idOperatore Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdOperatore idOperatore) throws SerializerException {
		this.objToXml(file, IdOperatore.class, idOperatore, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idOperatore</var> of type {@link it.govpay.orm.IdOperatore}
	 * 
	 * @param file Xml file to serialize the object <var>idOperatore</var>
	 * @param idOperatore Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdOperatore idOperatore,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdOperatore.class, idOperatore, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idOperatore</var> of type {@link it.govpay.orm.IdOperatore}
	 * 
	 * @param out OutputStream to serialize the object <var>idOperatore</var>
	 * @param idOperatore Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdOperatore idOperatore) throws SerializerException {
		this.objToXml(out, IdOperatore.class, idOperatore, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idOperatore</var> of type {@link it.govpay.orm.IdOperatore}
	 * 
	 * @param out OutputStream to serialize the object <var>idOperatore</var>
	 * @param idOperatore Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdOperatore idOperatore,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdOperatore.class, idOperatore, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idOperatore</var> of type {@link it.govpay.orm.IdOperatore}
	 * 
	 * @param idOperatore Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdOperatore idOperatore) throws SerializerException {
		return this.objToXml(IdOperatore.class, idOperatore, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idOperatore</var> of type {@link it.govpay.orm.IdOperatore}
	 * 
	 * @param idOperatore Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdOperatore idOperatore,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdOperatore.class, idOperatore, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idOperatore</var> of type {@link it.govpay.orm.IdOperatore}
	 * 
	 * @param idOperatore Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdOperatore idOperatore) throws SerializerException {
		return this.objToXml(IdOperatore.class, idOperatore, false).toString();
	}
	/**
	 * Serialize to String the object <var>idOperatore</var> of type {@link it.govpay.orm.IdOperatore}
	 * 
	 * @param idOperatore Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdOperatore idOperatore,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdOperatore.class, idOperatore, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: id-sla
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idSla</var> of type {@link it.govpay.orm.IdSla}
	 * 
	 * @param fileName Xml file to serialize the object <var>idSla</var>
	 * @param idSla Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdSla idSla) throws SerializerException {
		this.objToXml(fileName, IdSla.class, idSla, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idSla</var> of type {@link it.govpay.orm.IdSla}
	 * 
	 * @param fileName Xml file to serialize the object <var>idSla</var>
	 * @param idSla Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdSla idSla,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdSla.class, idSla, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idSla</var> of type {@link it.govpay.orm.IdSla}
	 * 
	 * @param file Xml file to serialize the object <var>idSla</var>
	 * @param idSla Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdSla idSla) throws SerializerException {
		this.objToXml(file, IdSla.class, idSla, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idSla</var> of type {@link it.govpay.orm.IdSla}
	 * 
	 * @param file Xml file to serialize the object <var>idSla</var>
	 * @param idSla Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdSla idSla,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdSla.class, idSla, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idSla</var> of type {@link it.govpay.orm.IdSla}
	 * 
	 * @param out OutputStream to serialize the object <var>idSla</var>
	 * @param idSla Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdSla idSla) throws SerializerException {
		this.objToXml(out, IdSla.class, idSla, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idSla</var> of type {@link it.govpay.orm.IdSla}
	 * 
	 * @param out OutputStream to serialize the object <var>idSla</var>
	 * @param idSla Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdSla idSla,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdSla.class, idSla, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idSla</var> of type {@link it.govpay.orm.IdSla}
	 * 
	 * @param idSla Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdSla idSla) throws SerializerException {
		return this.objToXml(IdSla.class, idSla, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idSla</var> of type {@link it.govpay.orm.IdSla}
	 * 
	 * @param idSla Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdSla idSla,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdSla.class, idSla, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idSla</var> of type {@link it.govpay.orm.IdSla}
	 * 
	 * @param idSla Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdSla idSla) throws SerializerException {
		return this.objToXml(IdSla.class, idSla, false).toString();
	}
	/**
	 * Serialize to String the object <var>idSla</var> of type {@link it.govpay.orm.IdSla}
	 * 
	 * @param idSla Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdSla idSla,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdSla.class, idSla, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: id-media-rilevamento
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idMediaRilevamento</var> of type {@link it.govpay.orm.IdMediaRilevamento}
	 * 
	 * @param fileName Xml file to serialize the object <var>idMediaRilevamento</var>
	 * @param idMediaRilevamento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdMediaRilevamento idMediaRilevamento) throws SerializerException {
		this.objToXml(fileName, IdMediaRilevamento.class, idMediaRilevamento, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idMediaRilevamento</var> of type {@link it.govpay.orm.IdMediaRilevamento}
	 * 
	 * @param fileName Xml file to serialize the object <var>idMediaRilevamento</var>
	 * @param idMediaRilevamento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdMediaRilevamento idMediaRilevamento,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdMediaRilevamento.class, idMediaRilevamento, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idMediaRilevamento</var> of type {@link it.govpay.orm.IdMediaRilevamento}
	 * 
	 * @param file Xml file to serialize the object <var>idMediaRilevamento</var>
	 * @param idMediaRilevamento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdMediaRilevamento idMediaRilevamento) throws SerializerException {
		this.objToXml(file, IdMediaRilevamento.class, idMediaRilevamento, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idMediaRilevamento</var> of type {@link it.govpay.orm.IdMediaRilevamento}
	 * 
	 * @param file Xml file to serialize the object <var>idMediaRilevamento</var>
	 * @param idMediaRilevamento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdMediaRilevamento idMediaRilevamento,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdMediaRilevamento.class, idMediaRilevamento, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idMediaRilevamento</var> of type {@link it.govpay.orm.IdMediaRilevamento}
	 * 
	 * @param out OutputStream to serialize the object <var>idMediaRilevamento</var>
	 * @param idMediaRilevamento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdMediaRilevamento idMediaRilevamento) throws SerializerException {
		this.objToXml(out, IdMediaRilevamento.class, idMediaRilevamento, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idMediaRilevamento</var> of type {@link it.govpay.orm.IdMediaRilevamento}
	 * 
	 * @param out OutputStream to serialize the object <var>idMediaRilevamento</var>
	 * @param idMediaRilevamento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdMediaRilevamento idMediaRilevamento,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdMediaRilevamento.class, idMediaRilevamento, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idMediaRilevamento</var> of type {@link it.govpay.orm.IdMediaRilevamento}
	 * 
	 * @param idMediaRilevamento Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdMediaRilevamento idMediaRilevamento) throws SerializerException {
		return this.objToXml(IdMediaRilevamento.class, idMediaRilevamento, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idMediaRilevamento</var> of type {@link it.govpay.orm.IdMediaRilevamento}
	 * 
	 * @param idMediaRilevamento Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdMediaRilevamento idMediaRilevamento,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdMediaRilevamento.class, idMediaRilevamento, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idMediaRilevamento</var> of type {@link it.govpay.orm.IdMediaRilevamento}
	 * 
	 * @param idMediaRilevamento Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdMediaRilevamento idMediaRilevamento) throws SerializerException {
		return this.objToXml(IdMediaRilevamento.class, idMediaRilevamento, false).toString();
	}
	/**
	 * Serialize to String the object <var>idMediaRilevamento</var> of type {@link it.govpay.orm.IdMediaRilevamento}
	 * 
	 * @param idMediaRilevamento Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdMediaRilevamento idMediaRilevamento,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdMediaRilevamento.class, idMediaRilevamento, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: id-singolo-versamento
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idSingoloVersamento</var> of type {@link it.govpay.orm.IdSingoloVersamento}
	 * 
	 * @param fileName Xml file to serialize the object <var>idSingoloVersamento</var>
	 * @param idSingoloVersamento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdSingoloVersamento idSingoloVersamento) throws SerializerException {
		this.objToXml(fileName, IdSingoloVersamento.class, idSingoloVersamento, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idSingoloVersamento</var> of type {@link it.govpay.orm.IdSingoloVersamento}
	 * 
	 * @param fileName Xml file to serialize the object <var>idSingoloVersamento</var>
	 * @param idSingoloVersamento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdSingoloVersamento idSingoloVersamento,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdSingoloVersamento.class, idSingoloVersamento, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idSingoloVersamento</var> of type {@link it.govpay.orm.IdSingoloVersamento}
	 * 
	 * @param file Xml file to serialize the object <var>idSingoloVersamento</var>
	 * @param idSingoloVersamento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdSingoloVersamento idSingoloVersamento) throws SerializerException {
		this.objToXml(file, IdSingoloVersamento.class, idSingoloVersamento, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idSingoloVersamento</var> of type {@link it.govpay.orm.IdSingoloVersamento}
	 * 
	 * @param file Xml file to serialize the object <var>idSingoloVersamento</var>
	 * @param idSingoloVersamento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdSingoloVersamento idSingoloVersamento,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdSingoloVersamento.class, idSingoloVersamento, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idSingoloVersamento</var> of type {@link it.govpay.orm.IdSingoloVersamento}
	 * 
	 * @param out OutputStream to serialize the object <var>idSingoloVersamento</var>
	 * @param idSingoloVersamento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdSingoloVersamento idSingoloVersamento) throws SerializerException {
		this.objToXml(out, IdSingoloVersamento.class, idSingoloVersamento, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idSingoloVersamento</var> of type {@link it.govpay.orm.IdSingoloVersamento}
	 * 
	 * @param out OutputStream to serialize the object <var>idSingoloVersamento</var>
	 * @param idSingoloVersamento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdSingoloVersamento idSingoloVersamento,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdSingoloVersamento.class, idSingoloVersamento, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idSingoloVersamento</var> of type {@link it.govpay.orm.IdSingoloVersamento}
	 * 
	 * @param idSingoloVersamento Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdSingoloVersamento idSingoloVersamento) throws SerializerException {
		return this.objToXml(IdSingoloVersamento.class, idSingoloVersamento, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idSingoloVersamento</var> of type {@link it.govpay.orm.IdSingoloVersamento}
	 * 
	 * @param idSingoloVersamento Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdSingoloVersamento idSingoloVersamento,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdSingoloVersamento.class, idSingoloVersamento, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idSingoloVersamento</var> of type {@link it.govpay.orm.IdSingoloVersamento}
	 * 
	 * @param idSingoloVersamento Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdSingoloVersamento idSingoloVersamento) throws SerializerException {
		return this.objToXml(IdSingoloVersamento.class, idSingoloVersamento, false).toString();
	}
	/**
	 * Serialize to String the object <var>idSingoloVersamento</var> of type {@link it.govpay.orm.IdSingoloVersamento}
	 * 
	 * @param idSingoloVersamento Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdSingoloVersamento idSingoloVersamento,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdSingoloVersamento.class, idSingoloVersamento, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: id-tributo
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idTributo</var> of type {@link it.govpay.orm.IdTributo}
	 * 
	 * @param fileName Xml file to serialize the object <var>idTributo</var>
	 * @param idTributo Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdTributo idTributo) throws SerializerException {
		this.objToXml(fileName, IdTributo.class, idTributo, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idTributo</var> of type {@link it.govpay.orm.IdTributo}
	 * 
	 * @param fileName Xml file to serialize the object <var>idTributo</var>
	 * @param idTributo Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdTributo idTributo,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdTributo.class, idTributo, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idTributo</var> of type {@link it.govpay.orm.IdTributo}
	 * 
	 * @param file Xml file to serialize the object <var>idTributo</var>
	 * @param idTributo Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdTributo idTributo) throws SerializerException {
		this.objToXml(file, IdTributo.class, idTributo, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idTributo</var> of type {@link it.govpay.orm.IdTributo}
	 * 
	 * @param file Xml file to serialize the object <var>idTributo</var>
	 * @param idTributo Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdTributo idTributo,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdTributo.class, idTributo, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idTributo</var> of type {@link it.govpay.orm.IdTributo}
	 * 
	 * @param out OutputStream to serialize the object <var>idTributo</var>
	 * @param idTributo Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdTributo idTributo) throws SerializerException {
		this.objToXml(out, IdTributo.class, idTributo, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idTributo</var> of type {@link it.govpay.orm.IdTributo}
	 * 
	 * @param out OutputStream to serialize the object <var>idTributo</var>
	 * @param idTributo Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdTributo idTributo,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdTributo.class, idTributo, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idTributo</var> of type {@link it.govpay.orm.IdTributo}
	 * 
	 * @param idTributo Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdTributo idTributo) throws SerializerException {
		return this.objToXml(IdTributo.class, idTributo, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idTributo</var> of type {@link it.govpay.orm.IdTributo}
	 * 
	 * @param idTributo Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdTributo idTributo,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdTributo.class, idTributo, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idTributo</var> of type {@link it.govpay.orm.IdTributo}
	 * 
	 * @param idTributo Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdTributo idTributo) throws SerializerException {
		return this.objToXml(IdTributo.class, idTributo, false).toString();
	}
	/**
	 * Serialize to String the object <var>idTributo</var> of type {@link it.govpay.orm.IdTributo}
	 * 
	 * @param idTributo Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdTributo idTributo,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdTributo.class, idTributo, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: id-iuv
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idIuv</var> of type {@link it.govpay.orm.IdIuv}
	 * 
	 * @param fileName Xml file to serialize the object <var>idIuv</var>
	 * @param idIuv Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdIuv idIuv) throws SerializerException {
		this.objToXml(fileName, IdIuv.class, idIuv, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idIuv</var> of type {@link it.govpay.orm.IdIuv}
	 * 
	 * @param fileName Xml file to serialize the object <var>idIuv</var>
	 * @param idIuv Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdIuv idIuv,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdIuv.class, idIuv, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idIuv</var> of type {@link it.govpay.orm.IdIuv}
	 * 
	 * @param file Xml file to serialize the object <var>idIuv</var>
	 * @param idIuv Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdIuv idIuv) throws SerializerException {
		this.objToXml(file, IdIuv.class, idIuv, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idIuv</var> of type {@link it.govpay.orm.IdIuv}
	 * 
	 * @param file Xml file to serialize the object <var>idIuv</var>
	 * @param idIuv Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdIuv idIuv,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdIuv.class, idIuv, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idIuv</var> of type {@link it.govpay.orm.IdIuv}
	 * 
	 * @param out OutputStream to serialize the object <var>idIuv</var>
	 * @param idIuv Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdIuv idIuv) throws SerializerException {
		this.objToXml(out, IdIuv.class, idIuv, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idIuv</var> of type {@link it.govpay.orm.IdIuv}
	 * 
	 * @param out OutputStream to serialize the object <var>idIuv</var>
	 * @param idIuv Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdIuv idIuv,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdIuv.class, idIuv, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idIuv</var> of type {@link it.govpay.orm.IdIuv}
	 * 
	 * @param idIuv Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdIuv idIuv) throws SerializerException {
		return this.objToXml(IdIuv.class, idIuv, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idIuv</var> of type {@link it.govpay.orm.IdIuv}
	 * 
	 * @param idIuv Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdIuv idIuv,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdIuv.class, idIuv, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idIuv</var> of type {@link it.govpay.orm.IdIuv}
	 * 
	 * @param idIuv Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdIuv idIuv) throws SerializerException {
		return this.objToXml(IdIuv.class, idIuv, false).toString();
	}
	/**
	 * Serialize to String the object <var>idIuv</var> of type {@link it.govpay.orm.IdIuv}
	 * 
	 * @param idIuv Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdIuv idIuv,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdIuv.class, idIuv, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: SingoloVersamento
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>singoloVersamento</var> of type {@link it.govpay.orm.SingoloVersamento}
	 * 
	 * @param fileName Xml file to serialize the object <var>singoloVersamento</var>
	 * @param singoloVersamento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,SingoloVersamento singoloVersamento) throws SerializerException {
		this.objToXml(fileName, SingoloVersamento.class, singoloVersamento, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>singoloVersamento</var> of type {@link it.govpay.orm.SingoloVersamento}
	 * 
	 * @param fileName Xml file to serialize the object <var>singoloVersamento</var>
	 * @param singoloVersamento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,SingoloVersamento singoloVersamento,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, SingoloVersamento.class, singoloVersamento, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>singoloVersamento</var> of type {@link it.govpay.orm.SingoloVersamento}
	 * 
	 * @param file Xml file to serialize the object <var>singoloVersamento</var>
	 * @param singoloVersamento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,SingoloVersamento singoloVersamento) throws SerializerException {
		this.objToXml(file, SingoloVersamento.class, singoloVersamento, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>singoloVersamento</var> of type {@link it.govpay.orm.SingoloVersamento}
	 * 
	 * @param file Xml file to serialize the object <var>singoloVersamento</var>
	 * @param singoloVersamento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,SingoloVersamento singoloVersamento,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, SingoloVersamento.class, singoloVersamento, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>singoloVersamento</var> of type {@link it.govpay.orm.SingoloVersamento}
	 * 
	 * @param out OutputStream to serialize the object <var>singoloVersamento</var>
	 * @param singoloVersamento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,SingoloVersamento singoloVersamento) throws SerializerException {
		this.objToXml(out, SingoloVersamento.class, singoloVersamento, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>singoloVersamento</var> of type {@link it.govpay.orm.SingoloVersamento}
	 * 
	 * @param out OutputStream to serialize the object <var>singoloVersamento</var>
	 * @param singoloVersamento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,SingoloVersamento singoloVersamento,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, SingoloVersamento.class, singoloVersamento, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>singoloVersamento</var> of type {@link it.govpay.orm.SingoloVersamento}
	 * 
	 * @param singoloVersamento Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(SingoloVersamento singoloVersamento) throws SerializerException {
		return this.objToXml(SingoloVersamento.class, singoloVersamento, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>singoloVersamento</var> of type {@link it.govpay.orm.SingoloVersamento}
	 * 
	 * @param singoloVersamento Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(SingoloVersamento singoloVersamento,boolean prettyPrint) throws SerializerException {
		return this.objToXml(SingoloVersamento.class, singoloVersamento, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>singoloVersamento</var> of type {@link it.govpay.orm.SingoloVersamento}
	 * 
	 * @param singoloVersamento Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(SingoloVersamento singoloVersamento) throws SerializerException {
		return this.objToXml(SingoloVersamento.class, singoloVersamento, false).toString();
	}
	/**
	 * Serialize to String the object <var>singoloVersamento</var> of type {@link it.govpay.orm.SingoloVersamento}
	 * 
	 * @param singoloVersamento Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(SingoloVersamento singoloVersamento,boolean prettyPrint) throws SerializerException {
		return this.objToXml(SingoloVersamento.class, singoloVersamento, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: id-iban-accredito
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idIbanAccredito</var> of type {@link it.govpay.orm.IdIbanAccredito}
	 * 
	 * @param fileName Xml file to serialize the object <var>idIbanAccredito</var>
	 * @param idIbanAccredito Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdIbanAccredito idIbanAccredito) throws SerializerException {
		this.objToXml(fileName, IdIbanAccredito.class, idIbanAccredito, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idIbanAccredito</var> of type {@link it.govpay.orm.IdIbanAccredito}
	 * 
	 * @param fileName Xml file to serialize the object <var>idIbanAccredito</var>
	 * @param idIbanAccredito Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdIbanAccredito idIbanAccredito,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdIbanAccredito.class, idIbanAccredito, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idIbanAccredito</var> of type {@link it.govpay.orm.IdIbanAccredito}
	 * 
	 * @param file Xml file to serialize the object <var>idIbanAccredito</var>
	 * @param idIbanAccredito Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdIbanAccredito idIbanAccredito) throws SerializerException {
		this.objToXml(file, IdIbanAccredito.class, idIbanAccredito, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idIbanAccredito</var> of type {@link it.govpay.orm.IdIbanAccredito}
	 * 
	 * @param file Xml file to serialize the object <var>idIbanAccredito</var>
	 * @param idIbanAccredito Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdIbanAccredito idIbanAccredito,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdIbanAccredito.class, idIbanAccredito, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idIbanAccredito</var> of type {@link it.govpay.orm.IdIbanAccredito}
	 * 
	 * @param out OutputStream to serialize the object <var>idIbanAccredito</var>
	 * @param idIbanAccredito Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdIbanAccredito idIbanAccredito) throws SerializerException {
		this.objToXml(out, IdIbanAccredito.class, idIbanAccredito, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idIbanAccredito</var> of type {@link it.govpay.orm.IdIbanAccredito}
	 * 
	 * @param out OutputStream to serialize the object <var>idIbanAccredito</var>
	 * @param idIbanAccredito Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdIbanAccredito idIbanAccredito,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdIbanAccredito.class, idIbanAccredito, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idIbanAccredito</var> of type {@link it.govpay.orm.IdIbanAccredito}
	 * 
	 * @param idIbanAccredito Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdIbanAccredito idIbanAccredito) throws SerializerException {
		return this.objToXml(IdIbanAccredito.class, idIbanAccredito, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idIbanAccredito</var> of type {@link it.govpay.orm.IdIbanAccredito}
	 * 
	 * @param idIbanAccredito Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdIbanAccredito idIbanAccredito,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdIbanAccredito.class, idIbanAccredito, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idIbanAccredito</var> of type {@link it.govpay.orm.IdIbanAccredito}
	 * 
	 * @param idIbanAccredito Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdIbanAccredito idIbanAccredito) throws SerializerException {
		return this.objToXml(IdIbanAccredito.class, idIbanAccredito, false).toString();
	}
	/**
	 * Serialize to String the object <var>idIbanAccredito</var> of type {@link it.govpay.orm.IdIbanAccredito}
	 * 
	 * @param idIbanAccredito Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdIbanAccredito idIbanAccredito,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdIbanAccredito.class, idIbanAccredito, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: FR
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>fr</var> of type {@link it.govpay.orm.FR}
	 * 
	 * @param fileName Xml file to serialize the object <var>fr</var>
	 * @param fr Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,FR fr) throws SerializerException {
		this.objToXml(fileName, FR.class, fr, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>fr</var> of type {@link it.govpay.orm.FR}
	 * 
	 * @param fileName Xml file to serialize the object <var>fr</var>
	 * @param fr Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,FR fr,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, FR.class, fr, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>fr</var> of type {@link it.govpay.orm.FR}
	 * 
	 * @param file Xml file to serialize the object <var>fr</var>
	 * @param fr Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,FR fr) throws SerializerException {
		this.objToXml(file, FR.class, fr, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>fr</var> of type {@link it.govpay.orm.FR}
	 * 
	 * @param file Xml file to serialize the object <var>fr</var>
	 * @param fr Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,FR fr,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, FR.class, fr, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>fr</var> of type {@link it.govpay.orm.FR}
	 * 
	 * @param out OutputStream to serialize the object <var>fr</var>
	 * @param fr Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,FR fr) throws SerializerException {
		this.objToXml(out, FR.class, fr, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>fr</var> of type {@link it.govpay.orm.FR}
	 * 
	 * @param out OutputStream to serialize the object <var>fr</var>
	 * @param fr Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,FR fr,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, FR.class, fr, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>fr</var> of type {@link it.govpay.orm.FR}
	 * 
	 * @param fr Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(FR fr) throws SerializerException {
		return this.objToXml(FR.class, fr, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>fr</var> of type {@link it.govpay.orm.FR}
	 * 
	 * @param fr Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(FR fr,boolean prettyPrint) throws SerializerException {
		return this.objToXml(FR.class, fr, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>fr</var> of type {@link it.govpay.orm.FR}
	 * 
	 * @param fr Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(FR fr) throws SerializerException {
		return this.objToXml(FR.class, fr, false).toString();
	}
	/**
	 * Serialize to String the object <var>fr</var> of type {@link it.govpay.orm.FR}
	 * 
	 * @param fr Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(FR fr,boolean prettyPrint) throws SerializerException {
		return this.objToXml(FR.class, fr, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: id-mail
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idMail</var> of type {@link it.govpay.orm.IdMail}
	 * 
	 * @param fileName Xml file to serialize the object <var>idMail</var>
	 * @param idMail Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdMail idMail) throws SerializerException {
		this.objToXml(fileName, IdMail.class, idMail, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idMail</var> of type {@link it.govpay.orm.IdMail}
	 * 
	 * @param fileName Xml file to serialize the object <var>idMail</var>
	 * @param idMail Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdMail idMail,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdMail.class, idMail, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idMail</var> of type {@link it.govpay.orm.IdMail}
	 * 
	 * @param file Xml file to serialize the object <var>idMail</var>
	 * @param idMail Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdMail idMail) throws SerializerException {
		this.objToXml(file, IdMail.class, idMail, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idMail</var> of type {@link it.govpay.orm.IdMail}
	 * 
	 * @param file Xml file to serialize the object <var>idMail</var>
	 * @param idMail Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdMail idMail,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdMail.class, idMail, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idMail</var> of type {@link it.govpay.orm.IdMail}
	 * 
	 * @param out OutputStream to serialize the object <var>idMail</var>
	 * @param idMail Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdMail idMail) throws SerializerException {
		this.objToXml(out, IdMail.class, idMail, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idMail</var> of type {@link it.govpay.orm.IdMail}
	 * 
	 * @param out OutputStream to serialize the object <var>idMail</var>
	 * @param idMail Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdMail idMail,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdMail.class, idMail, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idMail</var> of type {@link it.govpay.orm.IdMail}
	 * 
	 * @param idMail Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdMail idMail) throws SerializerException {
		return this.objToXml(IdMail.class, idMail, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idMail</var> of type {@link it.govpay.orm.IdMail}
	 * 
	 * @param idMail Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdMail idMail,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdMail.class, idMail, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idMail</var> of type {@link it.govpay.orm.IdMail}
	 * 
	 * @param idMail Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdMail idMail) throws SerializerException {
		return this.objToXml(IdMail.class, idMail, false).toString();
	}
	/**
	 * Serialize to String the object <var>idMail</var> of type {@link it.govpay.orm.IdMail}
	 * 
	 * @param idMail Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdMail idMail,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdMail.class, idMail, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: id-portale
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idPortale</var> of type {@link it.govpay.orm.IdPortale}
	 * 
	 * @param fileName Xml file to serialize the object <var>idPortale</var>
	 * @param idPortale Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdPortale idPortale) throws SerializerException {
		this.objToXml(fileName, IdPortale.class, idPortale, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idPortale</var> of type {@link it.govpay.orm.IdPortale}
	 * 
	 * @param fileName Xml file to serialize the object <var>idPortale</var>
	 * @param idPortale Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdPortale idPortale,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdPortale.class, idPortale, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idPortale</var> of type {@link it.govpay.orm.IdPortale}
	 * 
	 * @param file Xml file to serialize the object <var>idPortale</var>
	 * @param idPortale Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdPortale idPortale) throws SerializerException {
		this.objToXml(file, IdPortale.class, idPortale, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idPortale</var> of type {@link it.govpay.orm.IdPortale}
	 * 
	 * @param file Xml file to serialize the object <var>idPortale</var>
	 * @param idPortale Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdPortale idPortale,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdPortale.class, idPortale, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idPortale</var> of type {@link it.govpay.orm.IdPortale}
	 * 
	 * @param out OutputStream to serialize the object <var>idPortale</var>
	 * @param idPortale Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdPortale idPortale) throws SerializerException {
		this.objToXml(out, IdPortale.class, idPortale, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idPortale</var> of type {@link it.govpay.orm.IdPortale}
	 * 
	 * @param out OutputStream to serialize the object <var>idPortale</var>
	 * @param idPortale Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdPortale idPortale,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdPortale.class, idPortale, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idPortale</var> of type {@link it.govpay.orm.IdPortale}
	 * 
	 * @param idPortale Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdPortale idPortale) throws SerializerException {
		return this.objToXml(IdPortale.class, idPortale, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idPortale</var> of type {@link it.govpay.orm.IdPortale}
	 * 
	 * @param idPortale Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdPortale idPortale,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdPortale.class, idPortale, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idPortale</var> of type {@link it.govpay.orm.IdPortale}
	 * 
	 * @param idPortale Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdPortale idPortale) throws SerializerException {
		return this.objToXml(IdPortale.class, idPortale, false).toString();
	}
	/**
	 * Serialize to String the object <var>idPortale</var> of type {@link it.govpay.orm.IdPortale}
	 * 
	 * @param idPortale Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdPortale idPortale,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdPortale.class, idPortale, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: id-fr-applicazione
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idFrApplicazione</var> of type {@link it.govpay.orm.IdFrApplicazione}
	 * 
	 * @param fileName Xml file to serialize the object <var>idFrApplicazione</var>
	 * @param idFrApplicazione Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdFrApplicazione idFrApplicazione) throws SerializerException {
		this.objToXml(fileName, IdFrApplicazione.class, idFrApplicazione, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idFrApplicazione</var> of type {@link it.govpay.orm.IdFrApplicazione}
	 * 
	 * @param fileName Xml file to serialize the object <var>idFrApplicazione</var>
	 * @param idFrApplicazione Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdFrApplicazione idFrApplicazione,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdFrApplicazione.class, idFrApplicazione, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idFrApplicazione</var> of type {@link it.govpay.orm.IdFrApplicazione}
	 * 
	 * @param file Xml file to serialize the object <var>idFrApplicazione</var>
	 * @param idFrApplicazione Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdFrApplicazione idFrApplicazione) throws SerializerException {
		this.objToXml(file, IdFrApplicazione.class, idFrApplicazione, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idFrApplicazione</var> of type {@link it.govpay.orm.IdFrApplicazione}
	 * 
	 * @param file Xml file to serialize the object <var>idFrApplicazione</var>
	 * @param idFrApplicazione Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdFrApplicazione idFrApplicazione,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdFrApplicazione.class, idFrApplicazione, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idFrApplicazione</var> of type {@link it.govpay.orm.IdFrApplicazione}
	 * 
	 * @param out OutputStream to serialize the object <var>idFrApplicazione</var>
	 * @param idFrApplicazione Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdFrApplicazione idFrApplicazione) throws SerializerException {
		this.objToXml(out, IdFrApplicazione.class, idFrApplicazione, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idFrApplicazione</var> of type {@link it.govpay.orm.IdFrApplicazione}
	 * 
	 * @param out OutputStream to serialize the object <var>idFrApplicazione</var>
	 * @param idFrApplicazione Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdFrApplicazione idFrApplicazione,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdFrApplicazione.class, idFrApplicazione, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idFrApplicazione</var> of type {@link it.govpay.orm.IdFrApplicazione}
	 * 
	 * @param idFrApplicazione Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdFrApplicazione idFrApplicazione) throws SerializerException {
		return this.objToXml(IdFrApplicazione.class, idFrApplicazione, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idFrApplicazione</var> of type {@link it.govpay.orm.IdFrApplicazione}
	 * 
	 * @param idFrApplicazione Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdFrApplicazione idFrApplicazione,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdFrApplicazione.class, idFrApplicazione, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idFrApplicazione</var> of type {@link it.govpay.orm.IdFrApplicazione}
	 * 
	 * @param idFrApplicazione Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdFrApplicazione idFrApplicazione) throws SerializerException {
		return this.objToXml(IdFrApplicazione.class, idFrApplicazione, false).toString();
	}
	/**
	 * Serialize to String the object <var>idFrApplicazione</var> of type {@link it.govpay.orm.IdFrApplicazione}
	 * 
	 * @param idFrApplicazione Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdFrApplicazione idFrApplicazione,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdFrApplicazione.class, idFrApplicazione, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: RendicontazioneSenzaRPT
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>rendicontazioneSenzaRPT</var> of type {@link it.govpay.orm.RendicontazioneSenzaRPT}
	 * 
	 * @param fileName Xml file to serialize the object <var>rendicontazioneSenzaRPT</var>
	 * @param rendicontazioneSenzaRPT Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,RendicontazioneSenzaRPT rendicontazioneSenzaRPT) throws SerializerException {
		this.objToXml(fileName, RendicontazioneSenzaRPT.class, rendicontazioneSenzaRPT, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>rendicontazioneSenzaRPT</var> of type {@link it.govpay.orm.RendicontazioneSenzaRPT}
	 * 
	 * @param fileName Xml file to serialize the object <var>rendicontazioneSenzaRPT</var>
	 * @param rendicontazioneSenzaRPT Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,RendicontazioneSenzaRPT rendicontazioneSenzaRPT,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, RendicontazioneSenzaRPT.class, rendicontazioneSenzaRPT, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>rendicontazioneSenzaRPT</var> of type {@link it.govpay.orm.RendicontazioneSenzaRPT}
	 * 
	 * @param file Xml file to serialize the object <var>rendicontazioneSenzaRPT</var>
	 * @param rendicontazioneSenzaRPT Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,RendicontazioneSenzaRPT rendicontazioneSenzaRPT) throws SerializerException {
		this.objToXml(file, RendicontazioneSenzaRPT.class, rendicontazioneSenzaRPT, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>rendicontazioneSenzaRPT</var> of type {@link it.govpay.orm.RendicontazioneSenzaRPT}
	 * 
	 * @param file Xml file to serialize the object <var>rendicontazioneSenzaRPT</var>
	 * @param rendicontazioneSenzaRPT Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,RendicontazioneSenzaRPT rendicontazioneSenzaRPT,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, RendicontazioneSenzaRPT.class, rendicontazioneSenzaRPT, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>rendicontazioneSenzaRPT</var> of type {@link it.govpay.orm.RendicontazioneSenzaRPT}
	 * 
	 * @param out OutputStream to serialize the object <var>rendicontazioneSenzaRPT</var>
	 * @param rendicontazioneSenzaRPT Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,RendicontazioneSenzaRPT rendicontazioneSenzaRPT) throws SerializerException {
		this.objToXml(out, RendicontazioneSenzaRPT.class, rendicontazioneSenzaRPT, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>rendicontazioneSenzaRPT</var> of type {@link it.govpay.orm.RendicontazioneSenzaRPT}
	 * 
	 * @param out OutputStream to serialize the object <var>rendicontazioneSenzaRPT</var>
	 * @param rendicontazioneSenzaRPT Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,RendicontazioneSenzaRPT rendicontazioneSenzaRPT,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, RendicontazioneSenzaRPT.class, rendicontazioneSenzaRPT, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>rendicontazioneSenzaRPT</var> of type {@link it.govpay.orm.RendicontazioneSenzaRPT}
	 * 
	 * @param rendicontazioneSenzaRPT Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(RendicontazioneSenzaRPT rendicontazioneSenzaRPT) throws SerializerException {
		return this.objToXml(RendicontazioneSenzaRPT.class, rendicontazioneSenzaRPT, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>rendicontazioneSenzaRPT</var> of type {@link it.govpay.orm.RendicontazioneSenzaRPT}
	 * 
	 * @param rendicontazioneSenzaRPT Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(RendicontazioneSenzaRPT rendicontazioneSenzaRPT,boolean prettyPrint) throws SerializerException {
		return this.objToXml(RendicontazioneSenzaRPT.class, rendicontazioneSenzaRPT, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>rendicontazioneSenzaRPT</var> of type {@link it.govpay.orm.RendicontazioneSenzaRPT}
	 * 
	 * @param rendicontazioneSenzaRPT Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(RendicontazioneSenzaRPT rendicontazioneSenzaRPT) throws SerializerException {
		return this.objToXml(RendicontazioneSenzaRPT.class, rendicontazioneSenzaRPT, false).toString();
	}
	/**
	 * Serialize to String the object <var>rendicontazioneSenzaRPT</var> of type {@link it.govpay.orm.RendicontazioneSenzaRPT}
	 * 
	 * @param rendicontazioneSenzaRPT Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(RendicontazioneSenzaRPT rendicontazioneSenzaRPT,boolean prettyPrint) throws SerializerException {
		return this.objToXml(RendicontazioneSenzaRPT.class, rendicontazioneSenzaRPT, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: id-fr
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idFr</var> of type {@link it.govpay.orm.IdFr}
	 * 
	 * @param fileName Xml file to serialize the object <var>idFr</var>
	 * @param idFr Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdFr idFr) throws SerializerException {
		this.objToXml(fileName, IdFr.class, idFr, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idFr</var> of type {@link it.govpay.orm.IdFr}
	 * 
	 * @param fileName Xml file to serialize the object <var>idFr</var>
	 * @param idFr Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdFr idFr,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdFr.class, idFr, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idFr</var> of type {@link it.govpay.orm.IdFr}
	 * 
	 * @param file Xml file to serialize the object <var>idFr</var>
	 * @param idFr Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdFr idFr) throws SerializerException {
		this.objToXml(file, IdFr.class, idFr, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idFr</var> of type {@link it.govpay.orm.IdFr}
	 * 
	 * @param file Xml file to serialize the object <var>idFr</var>
	 * @param idFr Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdFr idFr,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdFr.class, idFr, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idFr</var> of type {@link it.govpay.orm.IdFr}
	 * 
	 * @param out OutputStream to serialize the object <var>idFr</var>
	 * @param idFr Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdFr idFr) throws SerializerException {
		this.objToXml(out, IdFr.class, idFr, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idFr</var> of type {@link it.govpay.orm.IdFr}
	 * 
	 * @param out OutputStream to serialize the object <var>idFr</var>
	 * @param idFr Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdFr idFr,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdFr.class, idFr, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idFr</var> of type {@link it.govpay.orm.IdFr}
	 * 
	 * @param idFr Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdFr idFr) throws SerializerException {
		return this.objToXml(IdFr.class, idFr, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idFr</var> of type {@link it.govpay.orm.IdFr}
	 * 
	 * @param idFr Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdFr idFr,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdFr.class, idFr, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idFr</var> of type {@link it.govpay.orm.IdFr}
	 * 
	 * @param idFr Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdFr idFr) throws SerializerException {
		return this.objToXml(IdFr.class, idFr, false).toString();
	}
	/**
	 * Serialize to String the object <var>idFr</var> of type {@link it.govpay.orm.IdFr}
	 * 
	 * @param idFr Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdFr idFr,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdFr.class, idFr, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: Portale
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>portale</var> of type {@link it.govpay.orm.Portale}
	 * 
	 * @param fileName Xml file to serialize the object <var>portale</var>
	 * @param portale Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Portale portale) throws SerializerException {
		this.objToXml(fileName, Portale.class, portale, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>portale</var> of type {@link it.govpay.orm.Portale}
	 * 
	 * @param fileName Xml file to serialize the object <var>portale</var>
	 * @param portale Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Portale portale,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, Portale.class, portale, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>portale</var> of type {@link it.govpay.orm.Portale}
	 * 
	 * @param file Xml file to serialize the object <var>portale</var>
	 * @param portale Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Portale portale) throws SerializerException {
		this.objToXml(file, Portale.class, portale, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>portale</var> of type {@link it.govpay.orm.Portale}
	 * 
	 * @param file Xml file to serialize the object <var>portale</var>
	 * @param portale Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Portale portale,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, Portale.class, portale, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>portale</var> of type {@link it.govpay.orm.Portale}
	 * 
	 * @param out OutputStream to serialize the object <var>portale</var>
	 * @param portale Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Portale portale) throws SerializerException {
		this.objToXml(out, Portale.class, portale, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>portale</var> of type {@link it.govpay.orm.Portale}
	 * 
	 * @param out OutputStream to serialize the object <var>portale</var>
	 * @param portale Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Portale portale,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, Portale.class, portale, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>portale</var> of type {@link it.govpay.orm.Portale}
	 * 
	 * @param portale Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Portale portale) throws SerializerException {
		return this.objToXml(Portale.class, portale, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>portale</var> of type {@link it.govpay.orm.Portale}
	 * 
	 * @param portale Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Portale portale,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Portale.class, portale, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>portale</var> of type {@link it.govpay.orm.Portale}
	 * 
	 * @param portale Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Portale portale) throws SerializerException {
		return this.objToXml(Portale.class, portale, false).toString();
	}
	/**
	 * Serialize to String the object <var>portale</var> of type {@link it.govpay.orm.Portale}
	 * 
	 * @param portale Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Portale portale,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Portale.class, portale, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: PortaleApplicazione
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>portaleApplicazione</var> of type {@link it.govpay.orm.PortaleApplicazione}
	 * 
	 * @param fileName Xml file to serialize the object <var>portaleApplicazione</var>
	 * @param portaleApplicazione Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,PortaleApplicazione portaleApplicazione) throws SerializerException {
		this.objToXml(fileName, PortaleApplicazione.class, portaleApplicazione, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>portaleApplicazione</var> of type {@link it.govpay.orm.PortaleApplicazione}
	 * 
	 * @param fileName Xml file to serialize the object <var>portaleApplicazione</var>
	 * @param portaleApplicazione Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,PortaleApplicazione portaleApplicazione,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, PortaleApplicazione.class, portaleApplicazione, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>portaleApplicazione</var> of type {@link it.govpay.orm.PortaleApplicazione}
	 * 
	 * @param file Xml file to serialize the object <var>portaleApplicazione</var>
	 * @param portaleApplicazione Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,PortaleApplicazione portaleApplicazione) throws SerializerException {
		this.objToXml(file, PortaleApplicazione.class, portaleApplicazione, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>portaleApplicazione</var> of type {@link it.govpay.orm.PortaleApplicazione}
	 * 
	 * @param file Xml file to serialize the object <var>portaleApplicazione</var>
	 * @param portaleApplicazione Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,PortaleApplicazione portaleApplicazione,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, PortaleApplicazione.class, portaleApplicazione, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>portaleApplicazione</var> of type {@link it.govpay.orm.PortaleApplicazione}
	 * 
	 * @param out OutputStream to serialize the object <var>portaleApplicazione</var>
	 * @param portaleApplicazione Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,PortaleApplicazione portaleApplicazione) throws SerializerException {
		this.objToXml(out, PortaleApplicazione.class, portaleApplicazione, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>portaleApplicazione</var> of type {@link it.govpay.orm.PortaleApplicazione}
	 * 
	 * @param out OutputStream to serialize the object <var>portaleApplicazione</var>
	 * @param portaleApplicazione Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,PortaleApplicazione portaleApplicazione,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, PortaleApplicazione.class, portaleApplicazione, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>portaleApplicazione</var> of type {@link it.govpay.orm.PortaleApplicazione}
	 * 
	 * @param portaleApplicazione Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(PortaleApplicazione portaleApplicazione) throws SerializerException {
		return this.objToXml(PortaleApplicazione.class, portaleApplicazione, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>portaleApplicazione</var> of type {@link it.govpay.orm.PortaleApplicazione}
	 * 
	 * @param portaleApplicazione Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(PortaleApplicazione portaleApplicazione,boolean prettyPrint) throws SerializerException {
		return this.objToXml(PortaleApplicazione.class, portaleApplicazione, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>portaleApplicazione</var> of type {@link it.govpay.orm.PortaleApplicazione}
	 * 
	 * @param portaleApplicazione Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(PortaleApplicazione portaleApplicazione) throws SerializerException {
		return this.objToXml(PortaleApplicazione.class, portaleApplicazione, false).toString();
	}
	/**
	 * Serialize to String the object <var>portaleApplicazione</var> of type {@link it.govpay.orm.PortaleApplicazione}
	 * 
	 * @param portaleApplicazione Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(PortaleApplicazione portaleApplicazione,boolean prettyPrint) throws SerializerException {
		return this.objToXml(PortaleApplicazione.class, portaleApplicazione, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: id-tabella-controparti
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idTabellaControparti</var> of type {@link it.govpay.orm.IdTabellaControparti}
	 * 
	 * @param fileName Xml file to serialize the object <var>idTabellaControparti</var>
	 * @param idTabellaControparti Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdTabellaControparti idTabellaControparti) throws SerializerException {
		this.objToXml(fileName, IdTabellaControparti.class, idTabellaControparti, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idTabellaControparti</var> of type {@link it.govpay.orm.IdTabellaControparti}
	 * 
	 * @param fileName Xml file to serialize the object <var>idTabellaControparti</var>
	 * @param idTabellaControparti Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdTabellaControparti idTabellaControparti,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdTabellaControparti.class, idTabellaControparti, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idTabellaControparti</var> of type {@link it.govpay.orm.IdTabellaControparti}
	 * 
	 * @param file Xml file to serialize the object <var>idTabellaControparti</var>
	 * @param idTabellaControparti Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdTabellaControparti idTabellaControparti) throws SerializerException {
		this.objToXml(file, IdTabellaControparti.class, idTabellaControparti, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idTabellaControparti</var> of type {@link it.govpay.orm.IdTabellaControparti}
	 * 
	 * @param file Xml file to serialize the object <var>idTabellaControparti</var>
	 * @param idTabellaControparti Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdTabellaControparti idTabellaControparti,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdTabellaControparti.class, idTabellaControparti, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idTabellaControparti</var> of type {@link it.govpay.orm.IdTabellaControparti}
	 * 
	 * @param out OutputStream to serialize the object <var>idTabellaControparti</var>
	 * @param idTabellaControparti Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdTabellaControparti idTabellaControparti) throws SerializerException {
		this.objToXml(out, IdTabellaControparti.class, idTabellaControparti, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idTabellaControparti</var> of type {@link it.govpay.orm.IdTabellaControparti}
	 * 
	 * @param out OutputStream to serialize the object <var>idTabellaControparti</var>
	 * @param idTabellaControparti Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdTabellaControparti idTabellaControparti,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdTabellaControparti.class, idTabellaControparti, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idTabellaControparti</var> of type {@link it.govpay.orm.IdTabellaControparti}
	 * 
	 * @param idTabellaControparti Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdTabellaControparti idTabellaControparti) throws SerializerException {
		return this.objToXml(IdTabellaControparti.class, idTabellaControparti, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idTabellaControparti</var> of type {@link it.govpay.orm.IdTabellaControparti}
	 * 
	 * @param idTabellaControparti Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdTabellaControparti idTabellaControparti,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdTabellaControparti.class, idTabellaControparti, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idTabellaControparti</var> of type {@link it.govpay.orm.IdTabellaControparti}
	 * 
	 * @param idTabellaControparti Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdTabellaControparti idTabellaControparti) throws SerializerException {
		return this.objToXml(IdTabellaControparti.class, idTabellaControparti, false).toString();
	}
	/**
	 * Serialize to String the object <var>idTabellaControparti</var> of type {@link it.govpay.orm.IdTabellaControparti}
	 * 
	 * @param idTabellaControparti Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdTabellaControparti idTabellaControparti,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdTabellaControparti.class, idTabellaControparti, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: id-rpt
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idRpt</var> of type {@link it.govpay.orm.IdRpt}
	 * 
	 * @param fileName Xml file to serialize the object <var>idRpt</var>
	 * @param idRpt Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdRpt idRpt) throws SerializerException {
		this.objToXml(fileName, IdRpt.class, idRpt, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idRpt</var> of type {@link it.govpay.orm.IdRpt}
	 * 
	 * @param fileName Xml file to serialize the object <var>idRpt</var>
	 * @param idRpt Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdRpt idRpt,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdRpt.class, idRpt, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idRpt</var> of type {@link it.govpay.orm.IdRpt}
	 * 
	 * @param file Xml file to serialize the object <var>idRpt</var>
	 * @param idRpt Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdRpt idRpt) throws SerializerException {
		this.objToXml(file, IdRpt.class, idRpt, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idRpt</var> of type {@link it.govpay.orm.IdRpt}
	 * 
	 * @param file Xml file to serialize the object <var>idRpt</var>
	 * @param idRpt Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdRpt idRpt,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdRpt.class, idRpt, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idRpt</var> of type {@link it.govpay.orm.IdRpt}
	 * 
	 * @param out OutputStream to serialize the object <var>idRpt</var>
	 * @param idRpt Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdRpt idRpt) throws SerializerException {
		this.objToXml(out, IdRpt.class, idRpt, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idRpt</var> of type {@link it.govpay.orm.IdRpt}
	 * 
	 * @param out OutputStream to serialize the object <var>idRpt</var>
	 * @param idRpt Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdRpt idRpt,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdRpt.class, idRpt, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idRpt</var> of type {@link it.govpay.orm.IdRpt}
	 * 
	 * @param idRpt Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdRpt idRpt) throws SerializerException {
		return this.objToXml(IdRpt.class, idRpt, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idRpt</var> of type {@link it.govpay.orm.IdRpt}
	 * 
	 * @param idRpt Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdRpt idRpt,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdRpt.class, idRpt, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idRpt</var> of type {@link it.govpay.orm.IdRpt}
	 * 
	 * @param idRpt Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdRpt idRpt) throws SerializerException {
		return this.objToXml(IdRpt.class, idRpt, false).toString();
	}
	/**
	 * Serialize to String the object <var>idRpt</var> of type {@link it.govpay.orm.IdRpt}
	 * 
	 * @param idRpt Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdRpt idRpt,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdRpt.class, idRpt, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: RR
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>rr</var> of type {@link it.govpay.orm.RR}
	 * 
	 * @param fileName Xml file to serialize the object <var>rr</var>
	 * @param rr Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,RR rr) throws SerializerException {
		this.objToXml(fileName, RR.class, rr, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>rr</var> of type {@link it.govpay.orm.RR}
	 * 
	 * @param fileName Xml file to serialize the object <var>rr</var>
	 * @param rr Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,RR rr,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, RR.class, rr, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>rr</var> of type {@link it.govpay.orm.RR}
	 * 
	 * @param file Xml file to serialize the object <var>rr</var>
	 * @param rr Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,RR rr) throws SerializerException {
		this.objToXml(file, RR.class, rr, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>rr</var> of type {@link it.govpay.orm.RR}
	 * 
	 * @param file Xml file to serialize the object <var>rr</var>
	 * @param rr Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,RR rr,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, RR.class, rr, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>rr</var> of type {@link it.govpay.orm.RR}
	 * 
	 * @param out OutputStream to serialize the object <var>rr</var>
	 * @param rr Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,RR rr) throws SerializerException {
		this.objToXml(out, RR.class, rr, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>rr</var> of type {@link it.govpay.orm.RR}
	 * 
	 * @param out OutputStream to serialize the object <var>rr</var>
	 * @param rr Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,RR rr,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, RR.class, rr, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>rr</var> of type {@link it.govpay.orm.RR}
	 * 
	 * @param rr Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(RR rr) throws SerializerException {
		return this.objToXml(RR.class, rr, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>rr</var> of type {@link it.govpay.orm.RR}
	 * 
	 * @param rr Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(RR rr,boolean prettyPrint) throws SerializerException {
		return this.objToXml(RR.class, rr, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>rr</var> of type {@link it.govpay.orm.RR}
	 * 
	 * @param rr Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(RR rr) throws SerializerException {
		return this.objToXml(RR.class, rr, false).toString();
	}
	/**
	 * Serialize to String the object <var>rr</var> of type {@link it.govpay.orm.RR}
	 * 
	 * @param rr Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(RR rr,boolean prettyPrint) throws SerializerException {
		return this.objToXml(RR.class, rr, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: id-messaggio
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idMessaggio</var> of type {@link it.govpay.orm.IdMessaggio}
	 * 
	 * @param fileName Xml file to serialize the object <var>idMessaggio</var>
	 * @param idMessaggio Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdMessaggio idMessaggio) throws SerializerException {
		this.objToXml(fileName, IdMessaggio.class, idMessaggio, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idMessaggio</var> of type {@link it.govpay.orm.IdMessaggio}
	 * 
	 * @param fileName Xml file to serialize the object <var>idMessaggio</var>
	 * @param idMessaggio Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdMessaggio idMessaggio,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdMessaggio.class, idMessaggio, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idMessaggio</var> of type {@link it.govpay.orm.IdMessaggio}
	 * 
	 * @param file Xml file to serialize the object <var>idMessaggio</var>
	 * @param idMessaggio Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdMessaggio idMessaggio) throws SerializerException {
		this.objToXml(file, IdMessaggio.class, idMessaggio, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idMessaggio</var> of type {@link it.govpay.orm.IdMessaggio}
	 * 
	 * @param file Xml file to serialize the object <var>idMessaggio</var>
	 * @param idMessaggio Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdMessaggio idMessaggio,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdMessaggio.class, idMessaggio, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idMessaggio</var> of type {@link it.govpay.orm.IdMessaggio}
	 * 
	 * @param out OutputStream to serialize the object <var>idMessaggio</var>
	 * @param idMessaggio Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdMessaggio idMessaggio) throws SerializerException {
		this.objToXml(out, IdMessaggio.class, idMessaggio, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idMessaggio</var> of type {@link it.govpay.orm.IdMessaggio}
	 * 
	 * @param out OutputStream to serialize the object <var>idMessaggio</var>
	 * @param idMessaggio Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdMessaggio idMessaggio,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdMessaggio.class, idMessaggio, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idMessaggio</var> of type {@link it.govpay.orm.IdMessaggio}
	 * 
	 * @param idMessaggio Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdMessaggio idMessaggio) throws SerializerException {
		return this.objToXml(IdMessaggio.class, idMessaggio, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idMessaggio</var> of type {@link it.govpay.orm.IdMessaggio}
	 * 
	 * @param idMessaggio Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdMessaggio idMessaggio,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdMessaggio.class, idMessaggio, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idMessaggio</var> of type {@link it.govpay.orm.IdMessaggio}
	 * 
	 * @param idMessaggio Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdMessaggio idMessaggio) throws SerializerException {
		return this.objToXml(IdMessaggio.class, idMessaggio, false).toString();
	}
	/**
	 * Serialize to String the object <var>idMessaggio</var> of type {@link it.govpay.orm.IdMessaggio}
	 * 
	 * @param idMessaggio Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdMessaggio idMessaggio,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdMessaggio.class, idMessaggio, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: ApplicazioneTributo
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>applicazioneTributo</var> of type {@link it.govpay.orm.ApplicazioneTributo}
	 * 
	 * @param fileName Xml file to serialize the object <var>applicazioneTributo</var>
	 * @param applicazioneTributo Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,ApplicazioneTributo applicazioneTributo) throws SerializerException {
		this.objToXml(fileName, ApplicazioneTributo.class, applicazioneTributo, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>applicazioneTributo</var> of type {@link it.govpay.orm.ApplicazioneTributo}
	 * 
	 * @param fileName Xml file to serialize the object <var>applicazioneTributo</var>
	 * @param applicazioneTributo Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,ApplicazioneTributo applicazioneTributo,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, ApplicazioneTributo.class, applicazioneTributo, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>applicazioneTributo</var> of type {@link it.govpay.orm.ApplicazioneTributo}
	 * 
	 * @param file Xml file to serialize the object <var>applicazioneTributo</var>
	 * @param applicazioneTributo Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,ApplicazioneTributo applicazioneTributo) throws SerializerException {
		this.objToXml(file, ApplicazioneTributo.class, applicazioneTributo, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>applicazioneTributo</var> of type {@link it.govpay.orm.ApplicazioneTributo}
	 * 
	 * @param file Xml file to serialize the object <var>applicazioneTributo</var>
	 * @param applicazioneTributo Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,ApplicazioneTributo applicazioneTributo,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, ApplicazioneTributo.class, applicazioneTributo, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>applicazioneTributo</var> of type {@link it.govpay.orm.ApplicazioneTributo}
	 * 
	 * @param out OutputStream to serialize the object <var>applicazioneTributo</var>
	 * @param applicazioneTributo Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,ApplicazioneTributo applicazioneTributo) throws SerializerException {
		this.objToXml(out, ApplicazioneTributo.class, applicazioneTributo, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>applicazioneTributo</var> of type {@link it.govpay.orm.ApplicazioneTributo}
	 * 
	 * @param out OutputStream to serialize the object <var>applicazioneTributo</var>
	 * @param applicazioneTributo Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,ApplicazioneTributo applicazioneTributo,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, ApplicazioneTributo.class, applicazioneTributo, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>applicazioneTributo</var> of type {@link it.govpay.orm.ApplicazioneTributo}
	 * 
	 * @param applicazioneTributo Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(ApplicazioneTributo applicazioneTributo) throws SerializerException {
		return this.objToXml(ApplicazioneTributo.class, applicazioneTributo, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>applicazioneTributo</var> of type {@link it.govpay.orm.ApplicazioneTributo}
	 * 
	 * @param applicazioneTributo Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(ApplicazioneTributo applicazioneTributo,boolean prettyPrint) throws SerializerException {
		return this.objToXml(ApplicazioneTributo.class, applicazioneTributo, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>applicazioneTributo</var> of type {@link it.govpay.orm.ApplicazioneTributo}
	 * 
	 * @param applicazioneTributo Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(ApplicazioneTributo applicazioneTributo) throws SerializerException {
		return this.objToXml(ApplicazioneTributo.class, applicazioneTributo, false).toString();
	}
	/**
	 * Serialize to String the object <var>applicazioneTributo</var> of type {@link it.govpay.orm.ApplicazioneTributo}
	 * 
	 * @param applicazioneTributo Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(ApplicazioneTributo applicazioneTributo,boolean prettyPrint) throws SerializerException {
		return this.objToXml(ApplicazioneTributo.class, applicazioneTributo, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: id-rr
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idRr</var> of type {@link it.govpay.orm.IdRr}
	 * 
	 * @param fileName Xml file to serialize the object <var>idRr</var>
	 * @param idRr Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdRr idRr) throws SerializerException {
		this.objToXml(fileName, IdRr.class, idRr, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idRr</var> of type {@link it.govpay.orm.IdRr}
	 * 
	 * @param fileName Xml file to serialize the object <var>idRr</var>
	 * @param idRr Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdRr idRr,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdRr.class, idRr, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idRr</var> of type {@link it.govpay.orm.IdRr}
	 * 
	 * @param file Xml file to serialize the object <var>idRr</var>
	 * @param idRr Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdRr idRr) throws SerializerException {
		this.objToXml(file, IdRr.class, idRr, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idRr</var> of type {@link it.govpay.orm.IdRr}
	 * 
	 * @param file Xml file to serialize the object <var>idRr</var>
	 * @param idRr Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdRr idRr,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdRr.class, idRr, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idRr</var> of type {@link it.govpay.orm.IdRr}
	 * 
	 * @param out OutputStream to serialize the object <var>idRr</var>
	 * @param idRr Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdRr idRr) throws SerializerException {
		this.objToXml(out, IdRr.class, idRr, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idRr</var> of type {@link it.govpay.orm.IdRr}
	 * 
	 * @param out OutputStream to serialize the object <var>idRr</var>
	 * @param idRr Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdRr idRr,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdRr.class, idRr, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idRr</var> of type {@link it.govpay.orm.IdRr}
	 * 
	 * @param idRr Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdRr idRr) throws SerializerException {
		return this.objToXml(IdRr.class, idRr, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idRr</var> of type {@link it.govpay.orm.IdRr}
	 * 
	 * @param idRr Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdRr idRr,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdRr.class, idRr, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idRr</var> of type {@link it.govpay.orm.IdRr}
	 * 
	 * @param idRr Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdRr idRr) throws SerializerException {
		return this.objToXml(IdRr.class, idRr, false).toString();
	}
	/**
	 * Serialize to String the object <var>idRr</var> of type {@link it.govpay.orm.IdRr}
	 * 
	 * @param idRr Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdRr idRr,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdRr.class, idRr, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: id-carrello
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idCarrello</var> of type {@link it.govpay.orm.IdCarrello}
	 * 
	 * @param fileName Xml file to serialize the object <var>idCarrello</var>
	 * @param idCarrello Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdCarrello idCarrello) throws SerializerException {
		this.objToXml(fileName, IdCarrello.class, idCarrello, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idCarrello</var> of type {@link it.govpay.orm.IdCarrello}
	 * 
	 * @param fileName Xml file to serialize the object <var>idCarrello</var>
	 * @param idCarrello Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdCarrello idCarrello,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdCarrello.class, idCarrello, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idCarrello</var> of type {@link it.govpay.orm.IdCarrello}
	 * 
	 * @param file Xml file to serialize the object <var>idCarrello</var>
	 * @param idCarrello Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdCarrello idCarrello) throws SerializerException {
		this.objToXml(file, IdCarrello.class, idCarrello, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idCarrello</var> of type {@link it.govpay.orm.IdCarrello}
	 * 
	 * @param file Xml file to serialize the object <var>idCarrello</var>
	 * @param idCarrello Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdCarrello idCarrello,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdCarrello.class, idCarrello, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idCarrello</var> of type {@link it.govpay.orm.IdCarrello}
	 * 
	 * @param out OutputStream to serialize the object <var>idCarrello</var>
	 * @param idCarrello Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdCarrello idCarrello) throws SerializerException {
		this.objToXml(out, IdCarrello.class, idCarrello, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idCarrello</var> of type {@link it.govpay.orm.IdCarrello}
	 * 
	 * @param out OutputStream to serialize the object <var>idCarrello</var>
	 * @param idCarrello Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdCarrello idCarrello,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdCarrello.class, idCarrello, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idCarrello</var> of type {@link it.govpay.orm.IdCarrello}
	 * 
	 * @param idCarrello Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdCarrello idCarrello) throws SerializerException {
		return this.objToXml(IdCarrello.class, idCarrello, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idCarrello</var> of type {@link it.govpay.orm.IdCarrello}
	 * 
	 * @param idCarrello Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdCarrello idCarrello,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdCarrello.class, idCarrello, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idCarrello</var> of type {@link it.govpay.orm.IdCarrello}
	 * 
	 * @param idCarrello Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdCarrello idCarrello) throws SerializerException {
		return this.objToXml(IdCarrello.class, idCarrello, false).toString();
	}
	/**
	 * Serialize to String the object <var>idCarrello</var> of type {@link it.govpay.orm.IdCarrello}
	 * 
	 * @param idCarrello Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdCarrello idCarrello,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdCarrello.class, idCarrello, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: Operatore
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>operatore</var> of type {@link it.govpay.orm.Operatore}
	 * 
	 * @param fileName Xml file to serialize the object <var>operatore</var>
	 * @param operatore Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Operatore operatore) throws SerializerException {
		this.objToXml(fileName, Operatore.class, operatore, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>operatore</var> of type {@link it.govpay.orm.Operatore}
	 * 
	 * @param fileName Xml file to serialize the object <var>operatore</var>
	 * @param operatore Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Operatore operatore,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, Operatore.class, operatore, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>operatore</var> of type {@link it.govpay.orm.Operatore}
	 * 
	 * @param file Xml file to serialize the object <var>operatore</var>
	 * @param operatore Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Operatore operatore) throws SerializerException {
		this.objToXml(file, Operatore.class, operatore, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>operatore</var> of type {@link it.govpay.orm.Operatore}
	 * 
	 * @param file Xml file to serialize the object <var>operatore</var>
	 * @param operatore Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Operatore operatore,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, Operatore.class, operatore, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>operatore</var> of type {@link it.govpay.orm.Operatore}
	 * 
	 * @param out OutputStream to serialize the object <var>operatore</var>
	 * @param operatore Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Operatore operatore) throws SerializerException {
		this.objToXml(out, Operatore.class, operatore, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>operatore</var> of type {@link it.govpay.orm.Operatore}
	 * 
	 * @param out OutputStream to serialize the object <var>operatore</var>
	 * @param operatore Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Operatore operatore,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, Operatore.class, operatore, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>operatore</var> of type {@link it.govpay.orm.Operatore}
	 * 
	 * @param operatore Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Operatore operatore) throws SerializerException {
		return this.objToXml(Operatore.class, operatore, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>operatore</var> of type {@link it.govpay.orm.Operatore}
	 * 
	 * @param operatore Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Operatore operatore,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Operatore.class, operatore, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>operatore</var> of type {@link it.govpay.orm.Operatore}
	 * 
	 * @param operatore Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Operatore operatore) throws SerializerException {
		return this.objToXml(Operatore.class, operatore, false).toString();
	}
	/**
	 * Serialize to String the object <var>operatore</var> of type {@link it.govpay.orm.Operatore}
	 * 
	 * @param operatore Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Operatore operatore,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Operatore.class, operatore, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: OperatoreUo
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>operatoreUo</var> of type {@link it.govpay.orm.OperatoreUo}
	 * 
	 * @param fileName Xml file to serialize the object <var>operatoreUo</var>
	 * @param operatoreUo Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,OperatoreUo operatoreUo) throws SerializerException {
		this.objToXml(fileName, OperatoreUo.class, operatoreUo, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>operatoreUo</var> of type {@link it.govpay.orm.OperatoreUo}
	 * 
	 * @param fileName Xml file to serialize the object <var>operatoreUo</var>
	 * @param operatoreUo Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,OperatoreUo operatoreUo,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, OperatoreUo.class, operatoreUo, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>operatoreUo</var> of type {@link it.govpay.orm.OperatoreUo}
	 * 
	 * @param file Xml file to serialize the object <var>operatoreUo</var>
	 * @param operatoreUo Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,OperatoreUo operatoreUo) throws SerializerException {
		this.objToXml(file, OperatoreUo.class, operatoreUo, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>operatoreUo</var> of type {@link it.govpay.orm.OperatoreUo}
	 * 
	 * @param file Xml file to serialize the object <var>operatoreUo</var>
	 * @param operatoreUo Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,OperatoreUo operatoreUo,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, OperatoreUo.class, operatoreUo, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>operatoreUo</var> of type {@link it.govpay.orm.OperatoreUo}
	 * 
	 * @param out OutputStream to serialize the object <var>operatoreUo</var>
	 * @param operatoreUo Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,OperatoreUo operatoreUo) throws SerializerException {
		this.objToXml(out, OperatoreUo.class, operatoreUo, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>operatoreUo</var> of type {@link it.govpay.orm.OperatoreUo}
	 * 
	 * @param out OutputStream to serialize the object <var>operatoreUo</var>
	 * @param operatoreUo Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,OperatoreUo operatoreUo,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, OperatoreUo.class, operatoreUo, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>operatoreUo</var> of type {@link it.govpay.orm.OperatoreUo}
	 * 
	 * @param operatoreUo Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(OperatoreUo operatoreUo) throws SerializerException {
		return this.objToXml(OperatoreUo.class, operatoreUo, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>operatoreUo</var> of type {@link it.govpay.orm.OperatoreUo}
	 * 
	 * @param operatoreUo Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(OperatoreUo operatoreUo,boolean prettyPrint) throws SerializerException {
		return this.objToXml(OperatoreUo.class, operatoreUo, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>operatoreUo</var> of type {@link it.govpay.orm.OperatoreUo}
	 * 
	 * @param operatoreUo Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(OperatoreUo operatoreUo) throws SerializerException {
		return this.objToXml(OperatoreUo.class, operatoreUo, false).toString();
	}
	/**
	 * Serialize to String the object <var>operatoreUo</var> of type {@link it.govpay.orm.OperatoreUo}
	 * 
	 * @param operatoreUo Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(OperatoreUo operatoreUo,boolean prettyPrint) throws SerializerException {
		return this.objToXml(OperatoreUo.class, operatoreUo, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: OperatoreApplicazione
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>operatoreApplicazione</var> of type {@link it.govpay.orm.OperatoreApplicazione}
	 * 
	 * @param fileName Xml file to serialize the object <var>operatoreApplicazione</var>
	 * @param operatoreApplicazione Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,OperatoreApplicazione operatoreApplicazione) throws SerializerException {
		this.objToXml(fileName, OperatoreApplicazione.class, operatoreApplicazione, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>operatoreApplicazione</var> of type {@link it.govpay.orm.OperatoreApplicazione}
	 * 
	 * @param fileName Xml file to serialize the object <var>operatoreApplicazione</var>
	 * @param operatoreApplicazione Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,OperatoreApplicazione operatoreApplicazione,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, OperatoreApplicazione.class, operatoreApplicazione, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>operatoreApplicazione</var> of type {@link it.govpay.orm.OperatoreApplicazione}
	 * 
	 * @param file Xml file to serialize the object <var>operatoreApplicazione</var>
	 * @param operatoreApplicazione Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,OperatoreApplicazione operatoreApplicazione) throws SerializerException {
		this.objToXml(file, OperatoreApplicazione.class, operatoreApplicazione, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>operatoreApplicazione</var> of type {@link it.govpay.orm.OperatoreApplicazione}
	 * 
	 * @param file Xml file to serialize the object <var>operatoreApplicazione</var>
	 * @param operatoreApplicazione Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,OperatoreApplicazione operatoreApplicazione,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, OperatoreApplicazione.class, operatoreApplicazione, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>operatoreApplicazione</var> of type {@link it.govpay.orm.OperatoreApplicazione}
	 * 
	 * @param out OutputStream to serialize the object <var>operatoreApplicazione</var>
	 * @param operatoreApplicazione Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,OperatoreApplicazione operatoreApplicazione) throws SerializerException {
		this.objToXml(out, OperatoreApplicazione.class, operatoreApplicazione, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>operatoreApplicazione</var> of type {@link it.govpay.orm.OperatoreApplicazione}
	 * 
	 * @param out OutputStream to serialize the object <var>operatoreApplicazione</var>
	 * @param operatoreApplicazione Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,OperatoreApplicazione operatoreApplicazione,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, OperatoreApplicazione.class, operatoreApplicazione, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>operatoreApplicazione</var> of type {@link it.govpay.orm.OperatoreApplicazione}
	 * 
	 * @param operatoreApplicazione Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(OperatoreApplicazione operatoreApplicazione) throws SerializerException {
		return this.objToXml(OperatoreApplicazione.class, operatoreApplicazione, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>operatoreApplicazione</var> of type {@link it.govpay.orm.OperatoreApplicazione}
	 * 
	 * @param operatoreApplicazione Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(OperatoreApplicazione operatoreApplicazione,boolean prettyPrint) throws SerializerException {
		return this.objToXml(OperatoreApplicazione.class, operatoreApplicazione, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>operatoreApplicazione</var> of type {@link it.govpay.orm.OperatoreApplicazione}
	 * 
	 * @param operatoreApplicazione Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(OperatoreApplicazione operatoreApplicazione) throws SerializerException {
		return this.objToXml(OperatoreApplicazione.class, operatoreApplicazione, false).toString();
	}
	/**
	 * Serialize to String the object <var>operatoreApplicazione</var> of type {@link it.govpay.orm.OperatoreApplicazione}
	 * 
	 * @param operatoreApplicazione Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(OperatoreApplicazione operatoreApplicazione,boolean prettyPrint) throws SerializerException {
		return this.objToXml(OperatoreApplicazione.class, operatoreApplicazione, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: OperatorePortale
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>operatorePortale</var> of type {@link it.govpay.orm.OperatorePortale}
	 * 
	 * @param fileName Xml file to serialize the object <var>operatorePortale</var>
	 * @param operatorePortale Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,OperatorePortale operatorePortale) throws SerializerException {
		this.objToXml(fileName, OperatorePortale.class, operatorePortale, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>operatorePortale</var> of type {@link it.govpay.orm.OperatorePortale}
	 * 
	 * @param fileName Xml file to serialize the object <var>operatorePortale</var>
	 * @param operatorePortale Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,OperatorePortale operatorePortale,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, OperatorePortale.class, operatorePortale, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>operatorePortale</var> of type {@link it.govpay.orm.OperatorePortale}
	 * 
	 * @param file Xml file to serialize the object <var>operatorePortale</var>
	 * @param operatorePortale Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,OperatorePortale operatorePortale) throws SerializerException {
		this.objToXml(file, OperatorePortale.class, operatorePortale, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>operatorePortale</var> of type {@link it.govpay.orm.OperatorePortale}
	 * 
	 * @param file Xml file to serialize the object <var>operatorePortale</var>
	 * @param operatorePortale Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,OperatorePortale operatorePortale,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, OperatorePortale.class, operatorePortale, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>operatorePortale</var> of type {@link it.govpay.orm.OperatorePortale}
	 * 
	 * @param out OutputStream to serialize the object <var>operatorePortale</var>
	 * @param operatorePortale Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,OperatorePortale operatorePortale) throws SerializerException {
		this.objToXml(out, OperatorePortale.class, operatorePortale, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>operatorePortale</var> of type {@link it.govpay.orm.OperatorePortale}
	 * 
	 * @param out OutputStream to serialize the object <var>operatorePortale</var>
	 * @param operatorePortale Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,OperatorePortale operatorePortale,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, OperatorePortale.class, operatorePortale, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>operatorePortale</var> of type {@link it.govpay.orm.OperatorePortale}
	 * 
	 * @param operatorePortale Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(OperatorePortale operatorePortale) throws SerializerException {
		return this.objToXml(OperatorePortale.class, operatorePortale, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>operatorePortale</var> of type {@link it.govpay.orm.OperatorePortale}
	 * 
	 * @param operatorePortale Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(OperatorePortale operatorePortale,boolean prettyPrint) throws SerializerException {
		return this.objToXml(OperatorePortale.class, operatorePortale, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>operatorePortale</var> of type {@link it.govpay.orm.OperatorePortale}
	 * 
	 * @param operatorePortale Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(OperatorePortale operatorePortale) throws SerializerException {
		return this.objToXml(OperatorePortale.class, operatorePortale, false).toString();
	}
	/**
	 * Serialize to String the object <var>operatorePortale</var> of type {@link it.govpay.orm.OperatorePortale}
	 * 
	 * @param operatorePortale Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(OperatorePortale operatorePortale,boolean prettyPrint) throws SerializerException {
		return this.objToXml(OperatorePortale.class, operatorePortale, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: Intermediario
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>intermediario</var> of type {@link it.govpay.orm.Intermediario}
	 * 
	 * @param fileName Xml file to serialize the object <var>intermediario</var>
	 * @param intermediario Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Intermediario intermediario) throws SerializerException {
		this.objToXml(fileName, Intermediario.class, intermediario, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>intermediario</var> of type {@link it.govpay.orm.Intermediario}
	 * 
	 * @param fileName Xml file to serialize the object <var>intermediario</var>
	 * @param intermediario Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Intermediario intermediario,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, Intermediario.class, intermediario, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>intermediario</var> of type {@link it.govpay.orm.Intermediario}
	 * 
	 * @param file Xml file to serialize the object <var>intermediario</var>
	 * @param intermediario Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Intermediario intermediario) throws SerializerException {
		this.objToXml(file, Intermediario.class, intermediario, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>intermediario</var> of type {@link it.govpay.orm.Intermediario}
	 * 
	 * @param file Xml file to serialize the object <var>intermediario</var>
	 * @param intermediario Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Intermediario intermediario,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, Intermediario.class, intermediario, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>intermediario</var> of type {@link it.govpay.orm.Intermediario}
	 * 
	 * @param out OutputStream to serialize the object <var>intermediario</var>
	 * @param intermediario Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Intermediario intermediario) throws SerializerException {
		this.objToXml(out, Intermediario.class, intermediario, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>intermediario</var> of type {@link it.govpay.orm.Intermediario}
	 * 
	 * @param out OutputStream to serialize the object <var>intermediario</var>
	 * @param intermediario Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Intermediario intermediario,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, Intermediario.class, intermediario, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>intermediario</var> of type {@link it.govpay.orm.Intermediario}
	 * 
	 * @param intermediario Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Intermediario intermediario) throws SerializerException {
		return this.objToXml(Intermediario.class, intermediario, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>intermediario</var> of type {@link it.govpay.orm.Intermediario}
	 * 
	 * @param intermediario Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Intermediario intermediario,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Intermediario.class, intermediario, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>intermediario</var> of type {@link it.govpay.orm.Intermediario}
	 * 
	 * @param intermediario Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Intermediario intermediario) throws SerializerException {
		return this.objToXml(Intermediario.class, intermediario, false).toString();
	}
	/**
	 * Serialize to String the object <var>intermediario</var> of type {@link it.govpay.orm.Intermediario}
	 * 
	 * @param intermediario Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Intermediario intermediario,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Intermediario.class, intermediario, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: id-tracciato
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idTracciato</var> of type {@link it.govpay.orm.IdTracciato}
	 * 
	 * @param fileName Xml file to serialize the object <var>idTracciato</var>
	 * @param idTracciato Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdTracciato idTracciato) throws SerializerException {
		this.objToXml(fileName, IdTracciato.class, idTracciato, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idTracciato</var> of type {@link it.govpay.orm.IdTracciato}
	 * 
	 * @param fileName Xml file to serialize the object <var>idTracciato</var>
	 * @param idTracciato Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdTracciato idTracciato,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdTracciato.class, idTracciato, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idTracciato</var> of type {@link it.govpay.orm.IdTracciato}
	 * 
	 * @param file Xml file to serialize the object <var>idTracciato</var>
	 * @param idTracciato Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdTracciato idTracciato) throws SerializerException {
		this.objToXml(file, IdTracciato.class, idTracciato, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idTracciato</var> of type {@link it.govpay.orm.IdTracciato}
	 * 
	 * @param file Xml file to serialize the object <var>idTracciato</var>
	 * @param idTracciato Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdTracciato idTracciato,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdTracciato.class, idTracciato, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idTracciato</var> of type {@link it.govpay.orm.IdTracciato}
	 * 
	 * @param out OutputStream to serialize the object <var>idTracciato</var>
	 * @param idTracciato Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdTracciato idTracciato) throws SerializerException {
		this.objToXml(out, IdTracciato.class, idTracciato, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idTracciato</var> of type {@link it.govpay.orm.IdTracciato}
	 * 
	 * @param out OutputStream to serialize the object <var>idTracciato</var>
	 * @param idTracciato Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdTracciato idTracciato,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdTracciato.class, idTracciato, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idTracciato</var> of type {@link it.govpay.orm.IdTracciato}
	 * 
	 * @param idTracciato Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdTracciato idTracciato) throws SerializerException {
		return this.objToXml(IdTracciato.class, idTracciato, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idTracciato</var> of type {@link it.govpay.orm.IdTracciato}
	 * 
	 * @param idTracciato Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdTracciato idTracciato,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdTracciato.class, idTracciato, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idTracciato</var> of type {@link it.govpay.orm.IdTracciato}
	 * 
	 * @param idTracciato Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdTracciato idTracciato) throws SerializerException {
		return this.objToXml(IdTracciato.class, idTracciato, false).toString();
	}
	/**
	 * Serialize to String the object <var>idTracciato</var> of type {@link it.govpay.orm.IdTracciato}
	 * 
	 * @param idTracciato Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdTracciato idTracciato,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdTracciato.class, idTracciato, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: id-stazione
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idStazione</var> of type {@link it.govpay.orm.IdStazione}
	 * 
	 * @param fileName Xml file to serialize the object <var>idStazione</var>
	 * @param idStazione Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdStazione idStazione) throws SerializerException {
		this.objToXml(fileName, IdStazione.class, idStazione, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idStazione</var> of type {@link it.govpay.orm.IdStazione}
	 * 
	 * @param fileName Xml file to serialize the object <var>idStazione</var>
	 * @param idStazione Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdStazione idStazione,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdStazione.class, idStazione, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idStazione</var> of type {@link it.govpay.orm.IdStazione}
	 * 
	 * @param file Xml file to serialize the object <var>idStazione</var>
	 * @param idStazione Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdStazione idStazione) throws SerializerException {
		this.objToXml(file, IdStazione.class, idStazione, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idStazione</var> of type {@link it.govpay.orm.IdStazione}
	 * 
	 * @param file Xml file to serialize the object <var>idStazione</var>
	 * @param idStazione Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdStazione idStazione,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdStazione.class, idStazione, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idStazione</var> of type {@link it.govpay.orm.IdStazione}
	 * 
	 * @param out OutputStream to serialize the object <var>idStazione</var>
	 * @param idStazione Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdStazione idStazione) throws SerializerException {
		this.objToXml(out, IdStazione.class, idStazione, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idStazione</var> of type {@link it.govpay.orm.IdStazione}
	 * 
	 * @param out OutputStream to serialize the object <var>idStazione</var>
	 * @param idStazione Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdStazione idStazione,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdStazione.class, idStazione, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idStazione</var> of type {@link it.govpay.orm.IdStazione}
	 * 
	 * @param idStazione Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdStazione idStazione) throws SerializerException {
		return this.objToXml(IdStazione.class, idStazione, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idStazione</var> of type {@link it.govpay.orm.IdStazione}
	 * 
	 * @param idStazione Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdStazione idStazione,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdStazione.class, idStazione, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idStazione</var> of type {@link it.govpay.orm.IdStazione}
	 * 
	 * @param idStazione Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdStazione idStazione) throws SerializerException {
		return this.objToXml(IdStazione.class, idStazione, false).toString();
	}
	/**
	 * Serialize to String the object <var>idStazione</var> of type {@link it.govpay.orm.IdStazione}
	 * 
	 * @param idStazione Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdStazione idStazione,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdStazione.class, idStazione, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: RPT
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>rpt</var> of type {@link it.govpay.orm.RPT}
	 * 
	 * @param fileName Xml file to serialize the object <var>rpt</var>
	 * @param rpt Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,RPT rpt) throws SerializerException {
		this.objToXml(fileName, RPT.class, rpt, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>rpt</var> of type {@link it.govpay.orm.RPT}
	 * 
	 * @param fileName Xml file to serialize the object <var>rpt</var>
	 * @param rpt Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,RPT rpt,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, RPT.class, rpt, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>rpt</var> of type {@link it.govpay.orm.RPT}
	 * 
	 * @param file Xml file to serialize the object <var>rpt</var>
	 * @param rpt Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,RPT rpt) throws SerializerException {
		this.objToXml(file, RPT.class, rpt, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>rpt</var> of type {@link it.govpay.orm.RPT}
	 * 
	 * @param file Xml file to serialize the object <var>rpt</var>
	 * @param rpt Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,RPT rpt,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, RPT.class, rpt, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>rpt</var> of type {@link it.govpay.orm.RPT}
	 * 
	 * @param out OutputStream to serialize the object <var>rpt</var>
	 * @param rpt Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,RPT rpt) throws SerializerException {
		this.objToXml(out, RPT.class, rpt, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>rpt</var> of type {@link it.govpay.orm.RPT}
	 * 
	 * @param out OutputStream to serialize the object <var>rpt</var>
	 * @param rpt Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,RPT rpt,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, RPT.class, rpt, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>rpt</var> of type {@link it.govpay.orm.RPT}
	 * 
	 * @param rpt Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(RPT rpt) throws SerializerException {
		return this.objToXml(RPT.class, rpt, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>rpt</var> of type {@link it.govpay.orm.RPT}
	 * 
	 * @param rpt Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(RPT rpt,boolean prettyPrint) throws SerializerException {
		return this.objToXml(RPT.class, rpt, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>rpt</var> of type {@link it.govpay.orm.RPT}
	 * 
	 * @param rpt Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(RPT rpt) throws SerializerException {
		return this.objToXml(RPT.class, rpt, false).toString();
	}
	/**
	 * Serialize to String the object <var>rpt</var> of type {@link it.govpay.orm.RPT}
	 * 
	 * @param rpt Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(RPT rpt,boolean prettyPrint) throws SerializerException {
		return this.objToXml(RPT.class, rpt, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: id-canale
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idCanale</var> of type {@link it.govpay.orm.IdCanale}
	 * 
	 * @param fileName Xml file to serialize the object <var>idCanale</var>
	 * @param idCanale Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdCanale idCanale) throws SerializerException {
		this.objToXml(fileName, IdCanale.class, idCanale, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idCanale</var> of type {@link it.govpay.orm.IdCanale}
	 * 
	 * @param fileName Xml file to serialize the object <var>idCanale</var>
	 * @param idCanale Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdCanale idCanale,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdCanale.class, idCanale, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idCanale</var> of type {@link it.govpay.orm.IdCanale}
	 * 
	 * @param file Xml file to serialize the object <var>idCanale</var>
	 * @param idCanale Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdCanale idCanale) throws SerializerException {
		this.objToXml(file, IdCanale.class, idCanale, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idCanale</var> of type {@link it.govpay.orm.IdCanale}
	 * 
	 * @param file Xml file to serialize the object <var>idCanale</var>
	 * @param idCanale Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdCanale idCanale,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdCanale.class, idCanale, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idCanale</var> of type {@link it.govpay.orm.IdCanale}
	 * 
	 * @param out OutputStream to serialize the object <var>idCanale</var>
	 * @param idCanale Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdCanale idCanale) throws SerializerException {
		this.objToXml(out, IdCanale.class, idCanale, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idCanale</var> of type {@link it.govpay.orm.IdCanale}
	 * 
	 * @param out OutputStream to serialize the object <var>idCanale</var>
	 * @param idCanale Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdCanale idCanale,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdCanale.class, idCanale, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idCanale</var> of type {@link it.govpay.orm.IdCanale}
	 * 
	 * @param idCanale Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdCanale idCanale) throws SerializerException {
		return this.objToXml(IdCanale.class, idCanale, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idCanale</var> of type {@link it.govpay.orm.IdCanale}
	 * 
	 * @param idCanale Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdCanale idCanale,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdCanale.class, idCanale, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idCanale</var> of type {@link it.govpay.orm.IdCanale}
	 * 
	 * @param idCanale Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdCanale idCanale) throws SerializerException {
		return this.objToXml(IdCanale.class, idCanale, false).toString();
	}
	/**
	 * Serialize to String the object <var>idCanale</var> of type {@link it.govpay.orm.IdCanale}
	 * 
	 * @param idCanale Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdCanale idCanale,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdCanale.class, idCanale, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: id-pagamento
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idPagamento</var> of type {@link it.govpay.orm.IdPagamento}
	 * 
	 * @param fileName Xml file to serialize the object <var>idPagamento</var>
	 * @param idPagamento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdPagamento idPagamento) throws SerializerException {
		this.objToXml(fileName, IdPagamento.class, idPagamento, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idPagamento</var> of type {@link it.govpay.orm.IdPagamento}
	 * 
	 * @param fileName Xml file to serialize the object <var>idPagamento</var>
	 * @param idPagamento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdPagamento idPagamento,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdPagamento.class, idPagamento, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idPagamento</var> of type {@link it.govpay.orm.IdPagamento}
	 * 
	 * @param file Xml file to serialize the object <var>idPagamento</var>
	 * @param idPagamento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdPagamento idPagamento) throws SerializerException {
		this.objToXml(file, IdPagamento.class, idPagamento, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idPagamento</var> of type {@link it.govpay.orm.IdPagamento}
	 * 
	 * @param file Xml file to serialize the object <var>idPagamento</var>
	 * @param idPagamento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdPagamento idPagamento,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdPagamento.class, idPagamento, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idPagamento</var> of type {@link it.govpay.orm.IdPagamento}
	 * 
	 * @param out OutputStream to serialize the object <var>idPagamento</var>
	 * @param idPagamento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdPagamento idPagamento) throws SerializerException {
		this.objToXml(out, IdPagamento.class, idPagamento, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idPagamento</var> of type {@link it.govpay.orm.IdPagamento}
	 * 
	 * @param out OutputStream to serialize the object <var>idPagamento</var>
	 * @param idPagamento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdPagamento idPagamento,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdPagamento.class, idPagamento, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idPagamento</var> of type {@link it.govpay.orm.IdPagamento}
	 * 
	 * @param idPagamento Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdPagamento idPagamento) throws SerializerException {
		return this.objToXml(IdPagamento.class, idPagamento, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idPagamento</var> of type {@link it.govpay.orm.IdPagamento}
	 * 
	 * @param idPagamento Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdPagamento idPagamento,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdPagamento.class, idPagamento, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idPagamento</var> of type {@link it.govpay.orm.IdPagamento}
	 * 
	 * @param idPagamento Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdPagamento idPagamento) throws SerializerException {
		return this.objToXml(IdPagamento.class, idPagamento, false).toString();
	}
	/**
	 * Serialize to String the object <var>idPagamento</var> of type {@link it.govpay.orm.IdPagamento}
	 * 
	 * @param idPagamento Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdPagamento idPagamento,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdPagamento.class, idPagamento, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: Dominio
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>dominio</var> of type {@link it.govpay.orm.Dominio}
	 * 
	 * @param fileName Xml file to serialize the object <var>dominio</var>
	 * @param dominio Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Dominio dominio) throws SerializerException {
		this.objToXml(fileName, Dominio.class, dominio, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>dominio</var> of type {@link it.govpay.orm.Dominio}
	 * 
	 * @param fileName Xml file to serialize the object <var>dominio</var>
	 * @param dominio Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Dominio dominio,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, Dominio.class, dominio, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>dominio</var> of type {@link it.govpay.orm.Dominio}
	 * 
	 * @param file Xml file to serialize the object <var>dominio</var>
	 * @param dominio Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Dominio dominio) throws SerializerException {
		this.objToXml(file, Dominio.class, dominio, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>dominio</var> of type {@link it.govpay.orm.Dominio}
	 * 
	 * @param file Xml file to serialize the object <var>dominio</var>
	 * @param dominio Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Dominio dominio,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, Dominio.class, dominio, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>dominio</var> of type {@link it.govpay.orm.Dominio}
	 * 
	 * @param out OutputStream to serialize the object <var>dominio</var>
	 * @param dominio Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Dominio dominio) throws SerializerException {
		this.objToXml(out, Dominio.class, dominio, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>dominio</var> of type {@link it.govpay.orm.Dominio}
	 * 
	 * @param out OutputStream to serialize the object <var>dominio</var>
	 * @param dominio Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Dominio dominio,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, Dominio.class, dominio, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>dominio</var> of type {@link it.govpay.orm.Dominio}
	 * 
	 * @param dominio Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Dominio dominio) throws SerializerException {
		return this.objToXml(Dominio.class, dominio, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>dominio</var> of type {@link it.govpay.orm.Dominio}
	 * 
	 * @param dominio Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Dominio dominio,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Dominio.class, dominio, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>dominio</var> of type {@link it.govpay.orm.Dominio}
	 * 
	 * @param dominio Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Dominio dominio) throws SerializerException {
		return this.objToXml(Dominio.class, dominio, false).toString();
	}
	/**
	 * Serialize to String the object <var>dominio</var> of type {@link it.govpay.orm.Dominio}
	 * 
	 * @param dominio Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Dominio dominio,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Dominio.class, dominio, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: FrApplicazione
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>frApplicazione</var> of type {@link it.govpay.orm.FrApplicazione}
	 * 
	 * @param fileName Xml file to serialize the object <var>frApplicazione</var>
	 * @param frApplicazione Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,FrApplicazione frApplicazione) throws SerializerException {
		this.objToXml(fileName, FrApplicazione.class, frApplicazione, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>frApplicazione</var> of type {@link it.govpay.orm.FrApplicazione}
	 * 
	 * @param fileName Xml file to serialize the object <var>frApplicazione</var>
	 * @param frApplicazione Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,FrApplicazione frApplicazione,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, FrApplicazione.class, frApplicazione, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>frApplicazione</var> of type {@link it.govpay.orm.FrApplicazione}
	 * 
	 * @param file Xml file to serialize the object <var>frApplicazione</var>
	 * @param frApplicazione Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,FrApplicazione frApplicazione) throws SerializerException {
		this.objToXml(file, FrApplicazione.class, frApplicazione, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>frApplicazione</var> of type {@link it.govpay.orm.FrApplicazione}
	 * 
	 * @param file Xml file to serialize the object <var>frApplicazione</var>
	 * @param frApplicazione Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,FrApplicazione frApplicazione,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, FrApplicazione.class, frApplicazione, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>frApplicazione</var> of type {@link it.govpay.orm.FrApplicazione}
	 * 
	 * @param out OutputStream to serialize the object <var>frApplicazione</var>
	 * @param frApplicazione Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,FrApplicazione frApplicazione) throws SerializerException {
		this.objToXml(out, FrApplicazione.class, frApplicazione, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>frApplicazione</var> of type {@link it.govpay.orm.FrApplicazione}
	 * 
	 * @param out OutputStream to serialize the object <var>frApplicazione</var>
	 * @param frApplicazione Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,FrApplicazione frApplicazione,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, FrApplicazione.class, frApplicazione, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>frApplicazione</var> of type {@link it.govpay.orm.FrApplicazione}
	 * 
	 * @param frApplicazione Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(FrApplicazione frApplicazione) throws SerializerException {
		return this.objToXml(FrApplicazione.class, frApplicazione, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>frApplicazione</var> of type {@link it.govpay.orm.FrApplicazione}
	 * 
	 * @param frApplicazione Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(FrApplicazione frApplicazione,boolean prettyPrint) throws SerializerException {
		return this.objToXml(FrApplicazione.class, frApplicazione, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>frApplicazione</var> of type {@link it.govpay.orm.FrApplicazione}
	 * 
	 * @param frApplicazione Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(FrApplicazione frApplicazione) throws SerializerException {
		return this.objToXml(FrApplicazione.class, frApplicazione, false).toString();
	}
	/**
	 * Serialize to String the object <var>frApplicazione</var> of type {@link it.govpay.orm.FrApplicazione}
	 * 
	 * @param frApplicazione Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(FrApplicazione frApplicazione,boolean prettyPrint) throws SerializerException {
		return this.objToXml(FrApplicazione.class, frApplicazione, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: id-mail-template
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idMailTemplate</var> of type {@link it.govpay.orm.IdMailTemplate}
	 * 
	 * @param fileName Xml file to serialize the object <var>idMailTemplate</var>
	 * @param idMailTemplate Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdMailTemplate idMailTemplate) throws SerializerException {
		this.objToXml(fileName, IdMailTemplate.class, idMailTemplate, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idMailTemplate</var> of type {@link it.govpay.orm.IdMailTemplate}
	 * 
	 * @param fileName Xml file to serialize the object <var>idMailTemplate</var>
	 * @param idMailTemplate Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdMailTemplate idMailTemplate,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdMailTemplate.class, idMailTemplate, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idMailTemplate</var> of type {@link it.govpay.orm.IdMailTemplate}
	 * 
	 * @param file Xml file to serialize the object <var>idMailTemplate</var>
	 * @param idMailTemplate Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdMailTemplate idMailTemplate) throws SerializerException {
		this.objToXml(file, IdMailTemplate.class, idMailTemplate, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idMailTemplate</var> of type {@link it.govpay.orm.IdMailTemplate}
	 * 
	 * @param file Xml file to serialize the object <var>idMailTemplate</var>
	 * @param idMailTemplate Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdMailTemplate idMailTemplate,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdMailTemplate.class, idMailTemplate, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idMailTemplate</var> of type {@link it.govpay.orm.IdMailTemplate}
	 * 
	 * @param out OutputStream to serialize the object <var>idMailTemplate</var>
	 * @param idMailTemplate Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdMailTemplate idMailTemplate) throws SerializerException {
		this.objToXml(out, IdMailTemplate.class, idMailTemplate, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idMailTemplate</var> of type {@link it.govpay.orm.IdMailTemplate}
	 * 
	 * @param out OutputStream to serialize the object <var>idMailTemplate</var>
	 * @param idMailTemplate Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdMailTemplate idMailTemplate,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdMailTemplate.class, idMailTemplate, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idMailTemplate</var> of type {@link it.govpay.orm.IdMailTemplate}
	 * 
	 * @param idMailTemplate Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdMailTemplate idMailTemplate) throws SerializerException {
		return this.objToXml(IdMailTemplate.class, idMailTemplate, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idMailTemplate</var> of type {@link it.govpay.orm.IdMailTemplate}
	 * 
	 * @param idMailTemplate Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdMailTemplate idMailTemplate,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdMailTemplate.class, idMailTemplate, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idMailTemplate</var> of type {@link it.govpay.orm.IdMailTemplate}
	 * 
	 * @param idMailTemplate Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdMailTemplate idMailTemplate) throws SerializerException {
		return this.objToXml(IdMailTemplate.class, idMailTemplate, false).toString();
	}
	/**
	 * Serialize to String the object <var>idMailTemplate</var> of type {@link it.govpay.orm.IdMailTemplate}
	 * 
	 * @param idMailTemplate Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdMailTemplate idMailTemplate,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdMailTemplate.class, idMailTemplate, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: Applicazione
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>applicazione</var> of type {@link it.govpay.orm.Applicazione}
	 * 
	 * @param fileName Xml file to serialize the object <var>applicazione</var>
	 * @param applicazione Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Applicazione applicazione) throws SerializerException {
		this.objToXml(fileName, Applicazione.class, applicazione, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>applicazione</var> of type {@link it.govpay.orm.Applicazione}
	 * 
	 * @param fileName Xml file to serialize the object <var>applicazione</var>
	 * @param applicazione Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Applicazione applicazione,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, Applicazione.class, applicazione, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>applicazione</var> of type {@link it.govpay.orm.Applicazione}
	 * 
	 * @param file Xml file to serialize the object <var>applicazione</var>
	 * @param applicazione Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Applicazione applicazione) throws SerializerException {
		this.objToXml(file, Applicazione.class, applicazione, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>applicazione</var> of type {@link it.govpay.orm.Applicazione}
	 * 
	 * @param file Xml file to serialize the object <var>applicazione</var>
	 * @param applicazione Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Applicazione applicazione,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, Applicazione.class, applicazione, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>applicazione</var> of type {@link it.govpay.orm.Applicazione}
	 * 
	 * @param out OutputStream to serialize the object <var>applicazione</var>
	 * @param applicazione Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Applicazione applicazione) throws SerializerException {
		this.objToXml(out, Applicazione.class, applicazione, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>applicazione</var> of type {@link it.govpay.orm.Applicazione}
	 * 
	 * @param out OutputStream to serialize the object <var>applicazione</var>
	 * @param applicazione Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Applicazione applicazione,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, Applicazione.class, applicazione, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>applicazione</var> of type {@link it.govpay.orm.Applicazione}
	 * 
	 * @param applicazione Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Applicazione applicazione) throws SerializerException {
		return this.objToXml(Applicazione.class, applicazione, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>applicazione</var> of type {@link it.govpay.orm.Applicazione}
	 * 
	 * @param applicazione Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Applicazione applicazione,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Applicazione.class, applicazione, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>applicazione</var> of type {@link it.govpay.orm.Applicazione}
	 * 
	 * @param applicazione Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Applicazione applicazione) throws SerializerException {
		return this.objToXml(Applicazione.class, applicazione, false).toString();
	}
	/**
	 * Serialize to String the object <var>applicazione</var> of type {@link it.govpay.orm.Applicazione}
	 * 
	 * @param applicazione Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Applicazione applicazione,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Applicazione.class, applicazione, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: Canale
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>canale</var> of type {@link it.govpay.orm.Canale}
	 * 
	 * @param fileName Xml file to serialize the object <var>canale</var>
	 * @param canale Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Canale canale) throws SerializerException {
		this.objToXml(fileName, Canale.class, canale, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>canale</var> of type {@link it.govpay.orm.Canale}
	 * 
	 * @param fileName Xml file to serialize the object <var>canale</var>
	 * @param canale Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Canale canale,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, Canale.class, canale, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>canale</var> of type {@link it.govpay.orm.Canale}
	 * 
	 * @param file Xml file to serialize the object <var>canale</var>
	 * @param canale Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Canale canale) throws SerializerException {
		this.objToXml(file, Canale.class, canale, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>canale</var> of type {@link it.govpay.orm.Canale}
	 * 
	 * @param file Xml file to serialize the object <var>canale</var>
	 * @param canale Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Canale canale,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, Canale.class, canale, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>canale</var> of type {@link it.govpay.orm.Canale}
	 * 
	 * @param out OutputStream to serialize the object <var>canale</var>
	 * @param canale Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Canale canale) throws SerializerException {
		this.objToXml(out, Canale.class, canale, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>canale</var> of type {@link it.govpay.orm.Canale}
	 * 
	 * @param out OutputStream to serialize the object <var>canale</var>
	 * @param canale Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Canale canale,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, Canale.class, canale, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>canale</var> of type {@link it.govpay.orm.Canale}
	 * 
	 * @param canale Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Canale canale) throws SerializerException {
		return this.objToXml(Canale.class, canale, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>canale</var> of type {@link it.govpay.orm.Canale}
	 * 
	 * @param canale Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Canale canale,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Canale.class, canale, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>canale</var> of type {@link it.govpay.orm.Canale}
	 * 
	 * @param canale Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Canale canale) throws SerializerException {
		return this.objToXml(Canale.class, canale, false).toString();
	}
	/**
	 * Serialize to String the object <var>canale</var> of type {@link it.govpay.orm.Canale}
	 * 
	 * @param canale Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Canale canale,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Canale.class, canale, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: id-notifica
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idNotifica</var> of type {@link it.govpay.orm.IdNotifica}
	 * 
	 * @param fileName Xml file to serialize the object <var>idNotifica</var>
	 * @param idNotifica Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdNotifica idNotifica) throws SerializerException {
		this.objToXml(fileName, IdNotifica.class, idNotifica, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idNotifica</var> of type {@link it.govpay.orm.IdNotifica}
	 * 
	 * @param fileName Xml file to serialize the object <var>idNotifica</var>
	 * @param idNotifica Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdNotifica idNotifica,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdNotifica.class, idNotifica, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idNotifica</var> of type {@link it.govpay.orm.IdNotifica}
	 * 
	 * @param file Xml file to serialize the object <var>idNotifica</var>
	 * @param idNotifica Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdNotifica idNotifica) throws SerializerException {
		this.objToXml(file, IdNotifica.class, idNotifica, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idNotifica</var> of type {@link it.govpay.orm.IdNotifica}
	 * 
	 * @param file Xml file to serialize the object <var>idNotifica</var>
	 * @param idNotifica Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdNotifica idNotifica,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdNotifica.class, idNotifica, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idNotifica</var> of type {@link it.govpay.orm.IdNotifica}
	 * 
	 * @param out OutputStream to serialize the object <var>idNotifica</var>
	 * @param idNotifica Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdNotifica idNotifica) throws SerializerException {
		this.objToXml(out, IdNotifica.class, idNotifica, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idNotifica</var> of type {@link it.govpay.orm.IdNotifica}
	 * 
	 * @param out OutputStream to serialize the object <var>idNotifica</var>
	 * @param idNotifica Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdNotifica idNotifica,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdNotifica.class, idNotifica, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idNotifica</var> of type {@link it.govpay.orm.IdNotifica}
	 * 
	 * @param idNotifica Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdNotifica idNotifica) throws SerializerException {
		return this.objToXml(IdNotifica.class, idNotifica, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idNotifica</var> of type {@link it.govpay.orm.IdNotifica}
	 * 
	 * @param idNotifica Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdNotifica idNotifica,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdNotifica.class, idNotifica, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idNotifica</var> of type {@link it.govpay.orm.IdNotifica}
	 * 
	 * @param idNotifica Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdNotifica idNotifica) throws SerializerException {
		return this.objToXml(IdNotifica.class, idNotifica, false).toString();
	}
	/**
	 * Serialize to String the object <var>idNotifica</var> of type {@link it.govpay.orm.IdNotifica}
	 * 
	 * @param idNotifica Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdNotifica idNotifica,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdNotifica.class, idNotifica, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: id-er
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idEr</var> of type {@link it.govpay.orm.IdEr}
	 * 
	 * @param fileName Xml file to serialize the object <var>idEr</var>
	 * @param idEr Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdEr idEr) throws SerializerException {
		this.objToXml(fileName, IdEr.class, idEr, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idEr</var> of type {@link it.govpay.orm.IdEr}
	 * 
	 * @param fileName Xml file to serialize the object <var>idEr</var>
	 * @param idEr Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdEr idEr,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdEr.class, idEr, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idEr</var> of type {@link it.govpay.orm.IdEr}
	 * 
	 * @param file Xml file to serialize the object <var>idEr</var>
	 * @param idEr Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdEr idEr) throws SerializerException {
		this.objToXml(file, IdEr.class, idEr, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idEr</var> of type {@link it.govpay.orm.IdEr}
	 * 
	 * @param file Xml file to serialize the object <var>idEr</var>
	 * @param idEr Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdEr idEr,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdEr.class, idEr, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idEr</var> of type {@link it.govpay.orm.IdEr}
	 * 
	 * @param out OutputStream to serialize the object <var>idEr</var>
	 * @param idEr Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdEr idEr) throws SerializerException {
		this.objToXml(out, IdEr.class, idEr, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idEr</var> of type {@link it.govpay.orm.IdEr}
	 * 
	 * @param out OutputStream to serialize the object <var>idEr</var>
	 * @param idEr Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdEr idEr,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdEr.class, idEr, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idEr</var> of type {@link it.govpay.orm.IdEr}
	 * 
	 * @param idEr Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdEr idEr) throws SerializerException {
		return this.objToXml(IdEr.class, idEr, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idEr</var> of type {@link it.govpay.orm.IdEr}
	 * 
	 * @param idEr Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdEr idEr,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdEr.class, idEr, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idEr</var> of type {@link it.govpay.orm.IdEr}
	 * 
	 * @param idEr Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdEr idEr) throws SerializerException {
		return this.objToXml(IdEr.class, idEr, false).toString();
	}
	/**
	 * Serialize to String the object <var>idEr</var> of type {@link it.govpay.orm.IdEr}
	 * 
	 * @param idEr Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdEr idEr,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdEr.class, idEr, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: id-singola-revoca
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idSingolaRevoca</var> of type {@link it.govpay.orm.IdSingolaRevoca}
	 * 
	 * @param fileName Xml file to serialize the object <var>idSingolaRevoca</var>
	 * @param idSingolaRevoca Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdSingolaRevoca idSingolaRevoca) throws SerializerException {
		this.objToXml(fileName, IdSingolaRevoca.class, idSingolaRevoca, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idSingolaRevoca</var> of type {@link it.govpay.orm.IdSingolaRevoca}
	 * 
	 * @param fileName Xml file to serialize the object <var>idSingolaRevoca</var>
	 * @param idSingolaRevoca Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdSingolaRevoca idSingolaRevoca,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdSingolaRevoca.class, idSingolaRevoca, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idSingolaRevoca</var> of type {@link it.govpay.orm.IdSingolaRevoca}
	 * 
	 * @param file Xml file to serialize the object <var>idSingolaRevoca</var>
	 * @param idSingolaRevoca Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdSingolaRevoca idSingolaRevoca) throws SerializerException {
		this.objToXml(file, IdSingolaRevoca.class, idSingolaRevoca, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idSingolaRevoca</var> of type {@link it.govpay.orm.IdSingolaRevoca}
	 * 
	 * @param file Xml file to serialize the object <var>idSingolaRevoca</var>
	 * @param idSingolaRevoca Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdSingolaRevoca idSingolaRevoca,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdSingolaRevoca.class, idSingolaRevoca, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idSingolaRevoca</var> of type {@link it.govpay.orm.IdSingolaRevoca}
	 * 
	 * @param out OutputStream to serialize the object <var>idSingolaRevoca</var>
	 * @param idSingolaRevoca Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdSingolaRevoca idSingolaRevoca) throws SerializerException {
		this.objToXml(out, IdSingolaRevoca.class, idSingolaRevoca, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idSingolaRevoca</var> of type {@link it.govpay.orm.IdSingolaRevoca}
	 * 
	 * @param out OutputStream to serialize the object <var>idSingolaRevoca</var>
	 * @param idSingolaRevoca Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdSingolaRevoca idSingolaRevoca,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdSingolaRevoca.class, idSingolaRevoca, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idSingolaRevoca</var> of type {@link it.govpay.orm.IdSingolaRevoca}
	 * 
	 * @param idSingolaRevoca Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdSingolaRevoca idSingolaRevoca) throws SerializerException {
		return this.objToXml(IdSingolaRevoca.class, idSingolaRevoca, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idSingolaRevoca</var> of type {@link it.govpay.orm.IdSingolaRevoca}
	 * 
	 * @param idSingolaRevoca Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdSingolaRevoca idSingolaRevoca,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdSingolaRevoca.class, idSingolaRevoca, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idSingolaRevoca</var> of type {@link it.govpay.orm.IdSingolaRevoca}
	 * 
	 * @param idSingolaRevoca Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdSingolaRevoca idSingolaRevoca) throws SerializerException {
		return this.objToXml(IdSingolaRevoca.class, idSingolaRevoca, false).toString();
	}
	/**
	 * Serialize to String the object <var>idSingolaRevoca</var> of type {@link it.govpay.orm.IdSingolaRevoca}
	 * 
	 * @param idSingolaRevoca Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdSingolaRevoca idSingolaRevoca,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdSingolaRevoca.class, idSingolaRevoca, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: id-conto-accredito
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idContoAccredito</var> of type {@link it.govpay.orm.IdContoAccredito}
	 * 
	 * @param fileName Xml file to serialize the object <var>idContoAccredito</var>
	 * @param idContoAccredito Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdContoAccredito idContoAccredito) throws SerializerException {
		this.objToXml(fileName, IdContoAccredito.class, idContoAccredito, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idContoAccredito</var> of type {@link it.govpay.orm.IdContoAccredito}
	 * 
	 * @param fileName Xml file to serialize the object <var>idContoAccredito</var>
	 * @param idContoAccredito Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdContoAccredito idContoAccredito,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdContoAccredito.class, idContoAccredito, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idContoAccredito</var> of type {@link it.govpay.orm.IdContoAccredito}
	 * 
	 * @param file Xml file to serialize the object <var>idContoAccredito</var>
	 * @param idContoAccredito Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdContoAccredito idContoAccredito) throws SerializerException {
		this.objToXml(file, IdContoAccredito.class, idContoAccredito, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idContoAccredito</var> of type {@link it.govpay.orm.IdContoAccredito}
	 * 
	 * @param file Xml file to serialize the object <var>idContoAccredito</var>
	 * @param idContoAccredito Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdContoAccredito idContoAccredito,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdContoAccredito.class, idContoAccredito, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idContoAccredito</var> of type {@link it.govpay.orm.IdContoAccredito}
	 * 
	 * @param out OutputStream to serialize the object <var>idContoAccredito</var>
	 * @param idContoAccredito Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdContoAccredito idContoAccredito) throws SerializerException {
		this.objToXml(out, IdContoAccredito.class, idContoAccredito, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idContoAccredito</var> of type {@link it.govpay.orm.IdContoAccredito}
	 * 
	 * @param out OutputStream to serialize the object <var>idContoAccredito</var>
	 * @param idContoAccredito Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdContoAccredito idContoAccredito,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdContoAccredito.class, idContoAccredito, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idContoAccredito</var> of type {@link it.govpay.orm.IdContoAccredito}
	 * 
	 * @param idContoAccredito Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdContoAccredito idContoAccredito) throws SerializerException {
		return this.objToXml(IdContoAccredito.class, idContoAccredito, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idContoAccredito</var> of type {@link it.govpay.orm.IdContoAccredito}
	 * 
	 * @param idContoAccredito Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdContoAccredito idContoAccredito,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdContoAccredito.class, idContoAccredito, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idContoAccredito</var> of type {@link it.govpay.orm.IdContoAccredito}
	 * 
	 * @param idContoAccredito Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdContoAccredito idContoAccredito) throws SerializerException {
		return this.objToXml(IdContoAccredito.class, idContoAccredito, false).toString();
	}
	/**
	 * Serialize to String the object <var>idContoAccredito</var> of type {@link it.govpay.orm.IdContoAccredito}
	 * 
	 * @param idContoAccredito Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdContoAccredito idContoAccredito,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdContoAccredito.class, idContoAccredito, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: Stazione
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>stazione</var> of type {@link it.govpay.orm.Stazione}
	 * 
	 * @param fileName Xml file to serialize the object <var>stazione</var>
	 * @param stazione Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Stazione stazione) throws SerializerException {
		this.objToXml(fileName, Stazione.class, stazione, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>stazione</var> of type {@link it.govpay.orm.Stazione}
	 * 
	 * @param fileName Xml file to serialize the object <var>stazione</var>
	 * @param stazione Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Stazione stazione,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, Stazione.class, stazione, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>stazione</var> of type {@link it.govpay.orm.Stazione}
	 * 
	 * @param file Xml file to serialize the object <var>stazione</var>
	 * @param stazione Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Stazione stazione) throws SerializerException {
		this.objToXml(file, Stazione.class, stazione, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>stazione</var> of type {@link it.govpay.orm.Stazione}
	 * 
	 * @param file Xml file to serialize the object <var>stazione</var>
	 * @param stazione Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Stazione stazione,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, Stazione.class, stazione, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>stazione</var> of type {@link it.govpay.orm.Stazione}
	 * 
	 * @param out OutputStream to serialize the object <var>stazione</var>
	 * @param stazione Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Stazione stazione) throws SerializerException {
		this.objToXml(out, Stazione.class, stazione, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>stazione</var> of type {@link it.govpay.orm.Stazione}
	 * 
	 * @param out OutputStream to serialize the object <var>stazione</var>
	 * @param stazione Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Stazione stazione,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, Stazione.class, stazione, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>stazione</var> of type {@link it.govpay.orm.Stazione}
	 * 
	 * @param stazione Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Stazione stazione) throws SerializerException {
		return this.objToXml(Stazione.class, stazione, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>stazione</var> of type {@link it.govpay.orm.Stazione}
	 * 
	 * @param stazione Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Stazione stazione,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Stazione.class, stazione, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>stazione</var> of type {@link it.govpay.orm.Stazione}
	 * 
	 * @param stazione Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Stazione stazione) throws SerializerException {
		return this.objToXml(Stazione.class, stazione, false).toString();
	}
	/**
	 * Serialize to String the object <var>stazione</var> of type {@link it.govpay.orm.Stazione}
	 * 
	 * @param stazione Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Stazione stazione,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Stazione.class, stazione, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: id-rilevamento
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idRilevamento</var> of type {@link it.govpay.orm.IdRilevamento}
	 * 
	 * @param fileName Xml file to serialize the object <var>idRilevamento</var>
	 * @param idRilevamento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdRilevamento idRilevamento) throws SerializerException {
		this.objToXml(fileName, IdRilevamento.class, idRilevamento, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idRilevamento</var> of type {@link it.govpay.orm.IdRilevamento}
	 * 
	 * @param fileName Xml file to serialize the object <var>idRilevamento</var>
	 * @param idRilevamento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdRilevamento idRilevamento,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdRilevamento.class, idRilevamento, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idRilevamento</var> of type {@link it.govpay.orm.IdRilevamento}
	 * 
	 * @param file Xml file to serialize the object <var>idRilevamento</var>
	 * @param idRilevamento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdRilevamento idRilevamento) throws SerializerException {
		this.objToXml(file, IdRilevamento.class, idRilevamento, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idRilevamento</var> of type {@link it.govpay.orm.IdRilevamento}
	 * 
	 * @param file Xml file to serialize the object <var>idRilevamento</var>
	 * @param idRilevamento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdRilevamento idRilevamento,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdRilevamento.class, idRilevamento, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idRilevamento</var> of type {@link it.govpay.orm.IdRilevamento}
	 * 
	 * @param out OutputStream to serialize the object <var>idRilevamento</var>
	 * @param idRilevamento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdRilevamento idRilevamento) throws SerializerException {
		this.objToXml(out, IdRilevamento.class, idRilevamento, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idRilevamento</var> of type {@link it.govpay.orm.IdRilevamento}
	 * 
	 * @param out OutputStream to serialize the object <var>idRilevamento</var>
	 * @param idRilevamento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdRilevamento idRilevamento,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdRilevamento.class, idRilevamento, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idRilevamento</var> of type {@link it.govpay.orm.IdRilevamento}
	 * 
	 * @param idRilevamento Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdRilevamento idRilevamento) throws SerializerException {
		return this.objToXml(IdRilevamento.class, idRilevamento, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idRilevamento</var> of type {@link it.govpay.orm.IdRilevamento}
	 * 
	 * @param idRilevamento Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdRilevamento idRilevamento,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdRilevamento.class, idRilevamento, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idRilevamento</var> of type {@link it.govpay.orm.IdRilevamento}
	 * 
	 * @param idRilevamento Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdRilevamento idRilevamento) throws SerializerException {
		return this.objToXml(IdRilevamento.class, idRilevamento, false).toString();
	}
	/**
	 * Serialize to String the object <var>idRilevamento</var> of type {@link it.govpay.orm.IdRilevamento}
	 * 
	 * @param idRilevamento Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdRilevamento idRilevamento,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdRilevamento.class, idRilevamento, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: Pagamento
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>pagamento</var> of type {@link it.govpay.orm.Pagamento}
	 * 
	 * @param fileName Xml file to serialize the object <var>pagamento</var>
	 * @param pagamento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Pagamento pagamento) throws SerializerException {
		this.objToXml(fileName, Pagamento.class, pagamento, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>pagamento</var> of type {@link it.govpay.orm.Pagamento}
	 * 
	 * @param fileName Xml file to serialize the object <var>pagamento</var>
	 * @param pagamento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Pagamento pagamento,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, Pagamento.class, pagamento, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>pagamento</var> of type {@link it.govpay.orm.Pagamento}
	 * 
	 * @param file Xml file to serialize the object <var>pagamento</var>
	 * @param pagamento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Pagamento pagamento) throws SerializerException {
		this.objToXml(file, Pagamento.class, pagamento, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>pagamento</var> of type {@link it.govpay.orm.Pagamento}
	 * 
	 * @param file Xml file to serialize the object <var>pagamento</var>
	 * @param pagamento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Pagamento pagamento,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, Pagamento.class, pagamento, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>pagamento</var> of type {@link it.govpay.orm.Pagamento}
	 * 
	 * @param out OutputStream to serialize the object <var>pagamento</var>
	 * @param pagamento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Pagamento pagamento) throws SerializerException {
		this.objToXml(out, Pagamento.class, pagamento, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>pagamento</var> of type {@link it.govpay.orm.Pagamento}
	 * 
	 * @param out OutputStream to serialize the object <var>pagamento</var>
	 * @param pagamento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Pagamento pagamento,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, Pagamento.class, pagamento, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>pagamento</var> of type {@link it.govpay.orm.Pagamento}
	 * 
	 * @param pagamento Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Pagamento pagamento) throws SerializerException {
		return this.objToXml(Pagamento.class, pagamento, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>pagamento</var> of type {@link it.govpay.orm.Pagamento}
	 * 
	 * @param pagamento Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Pagamento pagamento,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Pagamento.class, pagamento, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>pagamento</var> of type {@link it.govpay.orm.Pagamento}
	 * 
	 * @param pagamento Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Pagamento pagamento) throws SerializerException {
		return this.objToXml(Pagamento.class, pagamento, false).toString();
	}
	/**
	 * Serialize to String the object <var>pagamento</var> of type {@link it.govpay.orm.Pagamento}
	 * 
	 * @param pagamento Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Pagamento pagamento,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Pagamento.class, pagamento, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: Notifica
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>notifica</var> of type {@link it.govpay.orm.Notifica}
	 * 
	 * @param fileName Xml file to serialize the object <var>notifica</var>
	 * @param notifica Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Notifica notifica) throws SerializerException {
		this.objToXml(fileName, Notifica.class, notifica, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>notifica</var> of type {@link it.govpay.orm.Notifica}
	 * 
	 * @param fileName Xml file to serialize the object <var>notifica</var>
	 * @param notifica Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Notifica notifica,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, Notifica.class, notifica, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>notifica</var> of type {@link it.govpay.orm.Notifica}
	 * 
	 * @param file Xml file to serialize the object <var>notifica</var>
	 * @param notifica Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Notifica notifica) throws SerializerException {
		this.objToXml(file, Notifica.class, notifica, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>notifica</var> of type {@link it.govpay.orm.Notifica}
	 * 
	 * @param file Xml file to serialize the object <var>notifica</var>
	 * @param notifica Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Notifica notifica,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, Notifica.class, notifica, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>notifica</var> of type {@link it.govpay.orm.Notifica}
	 * 
	 * @param out OutputStream to serialize the object <var>notifica</var>
	 * @param notifica Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Notifica notifica) throws SerializerException {
		this.objToXml(out, Notifica.class, notifica, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>notifica</var> of type {@link it.govpay.orm.Notifica}
	 * 
	 * @param out OutputStream to serialize the object <var>notifica</var>
	 * @param notifica Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Notifica notifica,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, Notifica.class, notifica, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>notifica</var> of type {@link it.govpay.orm.Notifica}
	 * 
	 * @param notifica Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Notifica notifica) throws SerializerException {
		return this.objToXml(Notifica.class, notifica, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>notifica</var> of type {@link it.govpay.orm.Notifica}
	 * 
	 * @param notifica Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Notifica notifica,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Notifica.class, notifica, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>notifica</var> of type {@link it.govpay.orm.Notifica}
	 * 
	 * @param notifica Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Notifica notifica) throws SerializerException {
		return this.objToXml(Notifica.class, notifica, false).toString();
	}
	/**
	 * Serialize to String the object <var>notifica</var> of type {@link it.govpay.orm.Notifica}
	 * 
	 * @param notifica Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Notifica notifica,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Notifica.class, notifica, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: IUV
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>iuv</var> of type {@link it.govpay.orm.IUV}
	 * 
	 * @param fileName Xml file to serialize the object <var>iuv</var>
	 * @param iuv Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IUV iuv) throws SerializerException {
		this.objToXml(fileName, IUV.class, iuv, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>iuv</var> of type {@link it.govpay.orm.IUV}
	 * 
	 * @param fileName Xml file to serialize the object <var>iuv</var>
	 * @param iuv Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IUV iuv,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IUV.class, iuv, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>iuv</var> of type {@link it.govpay.orm.IUV}
	 * 
	 * @param file Xml file to serialize the object <var>iuv</var>
	 * @param iuv Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IUV iuv) throws SerializerException {
		this.objToXml(file, IUV.class, iuv, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>iuv</var> of type {@link it.govpay.orm.IUV}
	 * 
	 * @param file Xml file to serialize the object <var>iuv</var>
	 * @param iuv Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IUV iuv,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IUV.class, iuv, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>iuv</var> of type {@link it.govpay.orm.IUV}
	 * 
	 * @param out OutputStream to serialize the object <var>iuv</var>
	 * @param iuv Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IUV iuv) throws SerializerException {
		this.objToXml(out, IUV.class, iuv, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>iuv</var> of type {@link it.govpay.orm.IUV}
	 * 
	 * @param out OutputStream to serialize the object <var>iuv</var>
	 * @param iuv Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IUV iuv,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IUV.class, iuv, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>iuv</var> of type {@link it.govpay.orm.IUV}
	 * 
	 * @param iuv Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IUV iuv) throws SerializerException {
		return this.objToXml(IUV.class, iuv, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>iuv</var> of type {@link it.govpay.orm.IUV}
	 * 
	 * @param iuv Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IUV iuv,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IUV.class, iuv, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>iuv</var> of type {@link it.govpay.orm.IUV}
	 * 
	 * @param iuv Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IUV iuv) throws SerializerException {
		return this.objToXml(IUV.class, iuv, false).toString();
	}
	/**
	 * Serialize to String the object <var>iuv</var> of type {@link it.govpay.orm.IUV}
	 * 
	 * @param iuv Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IUV iuv,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IUV.class, iuv, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: id-anagrafica
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idAnagrafica</var> of type {@link it.govpay.orm.IdAnagrafica}
	 * 
	 * @param fileName Xml file to serialize the object <var>idAnagrafica</var>
	 * @param idAnagrafica Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdAnagrafica idAnagrafica) throws SerializerException {
		this.objToXml(fileName, IdAnagrafica.class, idAnagrafica, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idAnagrafica</var> of type {@link it.govpay.orm.IdAnagrafica}
	 * 
	 * @param fileName Xml file to serialize the object <var>idAnagrafica</var>
	 * @param idAnagrafica Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdAnagrafica idAnagrafica,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdAnagrafica.class, idAnagrafica, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idAnagrafica</var> of type {@link it.govpay.orm.IdAnagrafica}
	 * 
	 * @param file Xml file to serialize the object <var>idAnagrafica</var>
	 * @param idAnagrafica Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdAnagrafica idAnagrafica) throws SerializerException {
		this.objToXml(file, IdAnagrafica.class, idAnagrafica, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idAnagrafica</var> of type {@link it.govpay.orm.IdAnagrafica}
	 * 
	 * @param file Xml file to serialize the object <var>idAnagrafica</var>
	 * @param idAnagrafica Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdAnagrafica idAnagrafica,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdAnagrafica.class, idAnagrafica, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idAnagrafica</var> of type {@link it.govpay.orm.IdAnagrafica}
	 * 
	 * @param out OutputStream to serialize the object <var>idAnagrafica</var>
	 * @param idAnagrafica Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdAnagrafica idAnagrafica) throws SerializerException {
		this.objToXml(out, IdAnagrafica.class, idAnagrafica, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idAnagrafica</var> of type {@link it.govpay.orm.IdAnagrafica}
	 * 
	 * @param out OutputStream to serialize the object <var>idAnagrafica</var>
	 * @param idAnagrafica Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdAnagrafica idAnagrafica,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdAnagrafica.class, idAnagrafica, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idAnagrafica</var> of type {@link it.govpay.orm.IdAnagrafica}
	 * 
	 * @param idAnagrafica Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdAnagrafica idAnagrafica) throws SerializerException {
		return this.objToXml(IdAnagrafica.class, idAnagrafica, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idAnagrafica</var> of type {@link it.govpay.orm.IdAnagrafica}
	 * 
	 * @param idAnagrafica Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdAnagrafica idAnagrafica,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdAnagrafica.class, idAnagrafica, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idAnagrafica</var> of type {@link it.govpay.orm.IdAnagrafica}
	 * 
	 * @param idAnagrafica Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdAnagrafica idAnagrafica) throws SerializerException {
		return this.objToXml(IdAnagrafica.class, idAnagrafica, false).toString();
	}
	/**
	 * Serialize to String the object <var>idAnagrafica</var> of type {@link it.govpay.orm.IdAnagrafica}
	 * 
	 * @param idAnagrafica Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdAnagrafica idAnagrafica,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdAnagrafica.class, idAnagrafica, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: IbanAccredito
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>ibanAccredito</var> of type {@link it.govpay.orm.IbanAccredito}
	 * 
	 * @param fileName Xml file to serialize the object <var>ibanAccredito</var>
	 * @param ibanAccredito Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IbanAccredito ibanAccredito) throws SerializerException {
		this.objToXml(fileName, IbanAccredito.class, ibanAccredito, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>ibanAccredito</var> of type {@link it.govpay.orm.IbanAccredito}
	 * 
	 * @param fileName Xml file to serialize the object <var>ibanAccredito</var>
	 * @param ibanAccredito Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IbanAccredito ibanAccredito,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IbanAccredito.class, ibanAccredito, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>ibanAccredito</var> of type {@link it.govpay.orm.IbanAccredito}
	 * 
	 * @param file Xml file to serialize the object <var>ibanAccredito</var>
	 * @param ibanAccredito Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IbanAccredito ibanAccredito) throws SerializerException {
		this.objToXml(file, IbanAccredito.class, ibanAccredito, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>ibanAccredito</var> of type {@link it.govpay.orm.IbanAccredito}
	 * 
	 * @param file Xml file to serialize the object <var>ibanAccredito</var>
	 * @param ibanAccredito Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IbanAccredito ibanAccredito,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IbanAccredito.class, ibanAccredito, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>ibanAccredito</var> of type {@link it.govpay.orm.IbanAccredito}
	 * 
	 * @param out OutputStream to serialize the object <var>ibanAccredito</var>
	 * @param ibanAccredito Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IbanAccredito ibanAccredito) throws SerializerException {
		this.objToXml(out, IbanAccredito.class, ibanAccredito, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>ibanAccredito</var> of type {@link it.govpay.orm.IbanAccredito}
	 * 
	 * @param out OutputStream to serialize the object <var>ibanAccredito</var>
	 * @param ibanAccredito Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IbanAccredito ibanAccredito,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IbanAccredito.class, ibanAccredito, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>ibanAccredito</var> of type {@link it.govpay.orm.IbanAccredito}
	 * 
	 * @param ibanAccredito Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IbanAccredito ibanAccredito) throws SerializerException {
		return this.objToXml(IbanAccredito.class, ibanAccredito, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>ibanAccredito</var> of type {@link it.govpay.orm.IbanAccredito}
	 * 
	 * @param ibanAccredito Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IbanAccredito ibanAccredito,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IbanAccredito.class, ibanAccredito, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>ibanAccredito</var> of type {@link it.govpay.orm.IbanAccredito}
	 * 
	 * @param ibanAccredito Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IbanAccredito ibanAccredito) throws SerializerException {
		return this.objToXml(IbanAccredito.class, ibanAccredito, false).toString();
	}
	/**
	 * Serialize to String the object <var>ibanAccredito</var> of type {@link it.govpay.orm.IbanAccredito}
	 * 
	 * @param ibanAccredito Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IbanAccredito ibanAccredito,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IbanAccredito.class, ibanAccredito, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: Connettore
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>connettore</var> of type {@link it.govpay.orm.Connettore}
	 * 
	 * @param fileName Xml file to serialize the object <var>connettore</var>
	 * @param connettore Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Connettore connettore) throws SerializerException {
		this.objToXml(fileName, Connettore.class, connettore, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>connettore</var> of type {@link it.govpay.orm.Connettore}
	 * 
	 * @param fileName Xml file to serialize the object <var>connettore</var>
	 * @param connettore Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Connettore connettore,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, Connettore.class, connettore, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>connettore</var> of type {@link it.govpay.orm.Connettore}
	 * 
	 * @param file Xml file to serialize the object <var>connettore</var>
	 * @param connettore Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Connettore connettore) throws SerializerException {
		this.objToXml(file, Connettore.class, connettore, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>connettore</var> of type {@link it.govpay.orm.Connettore}
	 * 
	 * @param file Xml file to serialize the object <var>connettore</var>
	 * @param connettore Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Connettore connettore,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, Connettore.class, connettore, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>connettore</var> of type {@link it.govpay.orm.Connettore}
	 * 
	 * @param out OutputStream to serialize the object <var>connettore</var>
	 * @param connettore Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Connettore connettore) throws SerializerException {
		this.objToXml(out, Connettore.class, connettore, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>connettore</var> of type {@link it.govpay.orm.Connettore}
	 * 
	 * @param out OutputStream to serialize the object <var>connettore</var>
	 * @param connettore Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Connettore connettore,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, Connettore.class, connettore, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>connettore</var> of type {@link it.govpay.orm.Connettore}
	 * 
	 * @param connettore Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Connettore connettore) throws SerializerException {
		return this.objToXml(Connettore.class, connettore, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>connettore</var> of type {@link it.govpay.orm.Connettore}
	 * 
	 * @param connettore Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Connettore connettore,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Connettore.class, connettore, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>connettore</var> of type {@link it.govpay.orm.Connettore}
	 * 
	 * @param connettore Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Connettore connettore) throws SerializerException {
		return this.objToXml(Connettore.class, connettore, false).toString();
	}
	/**
	 * Serialize to String the object <var>connettore</var> of type {@link it.govpay.orm.Connettore}
	 * 
	 * @param connettore Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Connettore connettore,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Connettore.class, connettore, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: Tributo
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>tributo</var> of type {@link it.govpay.orm.Tributo}
	 * 
	 * @param fileName Xml file to serialize the object <var>tributo</var>
	 * @param tributo Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Tributo tributo) throws SerializerException {
		this.objToXml(fileName, Tributo.class, tributo, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>tributo</var> of type {@link it.govpay.orm.Tributo}
	 * 
	 * @param fileName Xml file to serialize the object <var>tributo</var>
	 * @param tributo Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Tributo tributo,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, Tributo.class, tributo, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>tributo</var> of type {@link it.govpay.orm.Tributo}
	 * 
	 * @param file Xml file to serialize the object <var>tributo</var>
	 * @param tributo Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Tributo tributo) throws SerializerException {
		this.objToXml(file, Tributo.class, tributo, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>tributo</var> of type {@link it.govpay.orm.Tributo}
	 * 
	 * @param file Xml file to serialize the object <var>tributo</var>
	 * @param tributo Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Tributo tributo,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, Tributo.class, tributo, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>tributo</var> of type {@link it.govpay.orm.Tributo}
	 * 
	 * @param out OutputStream to serialize the object <var>tributo</var>
	 * @param tributo Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Tributo tributo) throws SerializerException {
		this.objToXml(out, Tributo.class, tributo, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>tributo</var> of type {@link it.govpay.orm.Tributo}
	 * 
	 * @param out OutputStream to serialize the object <var>tributo</var>
	 * @param tributo Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Tributo tributo,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, Tributo.class, tributo, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>tributo</var> of type {@link it.govpay.orm.Tributo}
	 * 
	 * @param tributo Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Tributo tributo) throws SerializerException {
		return this.objToXml(Tributo.class, tributo, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>tributo</var> of type {@link it.govpay.orm.Tributo}
	 * 
	 * @param tributo Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Tributo tributo,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Tributo.class, tributo, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>tributo</var> of type {@link it.govpay.orm.Tributo}
	 * 
	 * @param tributo Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Tributo tributo) throws SerializerException {
		return this.objToXml(Tributo.class, tributo, false).toString();
	}
	/**
	 * Serialize to String the object <var>tributo</var> of type {@link it.govpay.orm.Tributo}
	 * 
	 * @param tributo Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Tributo tributo,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Tributo.class, tributo, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: id-connettore
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idConnettore</var> of type {@link it.govpay.orm.IdConnettore}
	 * 
	 * @param fileName Xml file to serialize the object <var>idConnettore</var>
	 * @param idConnettore Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdConnettore idConnettore) throws SerializerException {
		this.objToXml(fileName, IdConnettore.class, idConnettore, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idConnettore</var> of type {@link it.govpay.orm.IdConnettore}
	 * 
	 * @param fileName Xml file to serialize the object <var>idConnettore</var>
	 * @param idConnettore Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdConnettore idConnettore,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdConnettore.class, idConnettore, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idConnettore</var> of type {@link it.govpay.orm.IdConnettore}
	 * 
	 * @param file Xml file to serialize the object <var>idConnettore</var>
	 * @param idConnettore Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdConnettore idConnettore) throws SerializerException {
		this.objToXml(file, IdConnettore.class, idConnettore, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idConnettore</var> of type {@link it.govpay.orm.IdConnettore}
	 * 
	 * @param file Xml file to serialize the object <var>idConnettore</var>
	 * @param idConnettore Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdConnettore idConnettore,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdConnettore.class, idConnettore, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idConnettore</var> of type {@link it.govpay.orm.IdConnettore}
	 * 
	 * @param out OutputStream to serialize the object <var>idConnettore</var>
	 * @param idConnettore Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdConnettore idConnettore) throws SerializerException {
		this.objToXml(out, IdConnettore.class, idConnettore, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idConnettore</var> of type {@link it.govpay.orm.IdConnettore}
	 * 
	 * @param out OutputStream to serialize the object <var>idConnettore</var>
	 * @param idConnettore Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdConnettore idConnettore,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdConnettore.class, idConnettore, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idConnettore</var> of type {@link it.govpay.orm.IdConnettore}
	 * 
	 * @param idConnettore Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdConnettore idConnettore) throws SerializerException {
		return this.objToXml(IdConnettore.class, idConnettore, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idConnettore</var> of type {@link it.govpay.orm.IdConnettore}
	 * 
	 * @param idConnettore Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdConnettore idConnettore,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdConnettore.class, idConnettore, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idConnettore</var> of type {@link it.govpay.orm.IdConnettore}
	 * 
	 * @param idConnettore Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdConnettore idConnettore) throws SerializerException {
		return this.objToXml(IdConnettore.class, idConnettore, false).toString();
	}
	/**
	 * Serialize to String the object <var>idConnettore</var> of type {@link it.govpay.orm.IdConnettore}
	 * 
	 * @param idConnettore Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdConnettore idConnettore,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdConnettore.class, idConnettore, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: id-evento
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idEvento</var> of type {@link it.govpay.orm.IdEvento}
	 * 
	 * @param fileName Xml file to serialize the object <var>idEvento</var>
	 * @param idEvento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdEvento idEvento) throws SerializerException {
		this.objToXml(fileName, IdEvento.class, idEvento, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idEvento</var> of type {@link it.govpay.orm.IdEvento}
	 * 
	 * @param fileName Xml file to serialize the object <var>idEvento</var>
	 * @param idEvento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdEvento idEvento,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdEvento.class, idEvento, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idEvento</var> of type {@link it.govpay.orm.IdEvento}
	 * 
	 * @param file Xml file to serialize the object <var>idEvento</var>
	 * @param idEvento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdEvento idEvento) throws SerializerException {
		this.objToXml(file, IdEvento.class, idEvento, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idEvento</var> of type {@link it.govpay.orm.IdEvento}
	 * 
	 * @param file Xml file to serialize the object <var>idEvento</var>
	 * @param idEvento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdEvento idEvento,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdEvento.class, idEvento, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idEvento</var> of type {@link it.govpay.orm.IdEvento}
	 * 
	 * @param out OutputStream to serialize the object <var>idEvento</var>
	 * @param idEvento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdEvento idEvento) throws SerializerException {
		this.objToXml(out, IdEvento.class, idEvento, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idEvento</var> of type {@link it.govpay.orm.IdEvento}
	 * 
	 * @param out OutputStream to serialize the object <var>idEvento</var>
	 * @param idEvento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdEvento idEvento,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdEvento.class, idEvento, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idEvento</var> of type {@link it.govpay.orm.IdEvento}
	 * 
	 * @param idEvento Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdEvento idEvento) throws SerializerException {
		return this.objToXml(IdEvento.class, idEvento, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idEvento</var> of type {@link it.govpay.orm.IdEvento}
	 * 
	 * @param idEvento Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdEvento idEvento,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdEvento.class, idEvento, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idEvento</var> of type {@link it.govpay.orm.IdEvento}
	 * 
	 * @param idEvento Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdEvento idEvento) throws SerializerException {
		return this.objToXml(IdEvento.class, idEvento, false).toString();
	}
	/**
	 * Serialize to String the object <var>idEvento</var> of type {@link it.govpay.orm.IdEvento}
	 * 
	 * @param idEvento Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdEvento idEvento,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdEvento.class, idEvento, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: Evento
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>evento</var> of type {@link it.govpay.orm.Evento}
	 * 
	 * @param fileName Xml file to serialize the object <var>evento</var>
	 * @param evento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Evento evento) throws SerializerException {
		this.objToXml(fileName, Evento.class, evento, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>evento</var> of type {@link it.govpay.orm.Evento}
	 * 
	 * @param fileName Xml file to serialize the object <var>evento</var>
	 * @param evento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Evento evento,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, Evento.class, evento, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>evento</var> of type {@link it.govpay.orm.Evento}
	 * 
	 * @param file Xml file to serialize the object <var>evento</var>
	 * @param evento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Evento evento) throws SerializerException {
		this.objToXml(file, Evento.class, evento, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>evento</var> of type {@link it.govpay.orm.Evento}
	 * 
	 * @param file Xml file to serialize the object <var>evento</var>
	 * @param evento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Evento evento,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, Evento.class, evento, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>evento</var> of type {@link it.govpay.orm.Evento}
	 * 
	 * @param out OutputStream to serialize the object <var>evento</var>
	 * @param evento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Evento evento) throws SerializerException {
		this.objToXml(out, Evento.class, evento, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>evento</var> of type {@link it.govpay.orm.Evento}
	 * 
	 * @param out OutputStream to serialize the object <var>evento</var>
	 * @param evento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Evento evento,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, Evento.class, evento, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>evento</var> of type {@link it.govpay.orm.Evento}
	 * 
	 * @param evento Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Evento evento) throws SerializerException {
		return this.objToXml(Evento.class, evento, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>evento</var> of type {@link it.govpay.orm.Evento}
	 * 
	 * @param evento Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Evento evento,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Evento.class, evento, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>evento</var> of type {@link it.govpay.orm.Evento}
	 * 
	 * @param evento Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Evento evento) throws SerializerException {
		return this.objToXml(Evento.class, evento, false).toString();
	}
	/**
	 * Serialize to String the object <var>evento</var> of type {@link it.govpay.orm.Evento}
	 * 
	 * @param evento Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Evento evento,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Evento.class, evento, prettyPrint).toString();
	}
	
	
	

}
