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
package it.govpay.web.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

import it.govpay.web.rs.dars.model.RawParamValue;

/***
 * 
 * Utils Fornisce una serie di utilities.
 * 
 * @author Pintori Giuliano (pintori@link.it)
 */
public class Utils {

	public static final String MISSING_RESOURCE_END_PLACEHOLDER = " not found ??";
	public static final String MISSING_RESOURCE_START_PLACEHOLDER = "?? key ";

	private static Utils instance = null;

	public static Utils getInstance(){

		if(Utils.instance == null)
			init();


		return Utils.instance;
	}

	private synchronized static void init(){
		if(Utils.instance == null)
			Utils.instance = new Utils();
	}

	public   String getMessageFromResourceBundle(String key) {
		Locale locale = this.getLocale();
		return this.getMessageFromResourceBundle("messages", key, null, locale);
	}

	public   String getMessageWithParamsFromResourceBundle(String key, Object ... params) {
		Locale locale = this.getLocale();
		return this.getMessageFromResourceBundle("messages", key, params, locale);
	}


	public   String getMessageFromResourceBundle(String key,Locale locale){
		return this.getMessageFromResourceBundle("messages", key, null, locale);
	}

	public   String getMessageFromResourceBundle(String key, String bundleName){
		Locale locale = this.getLocale();

		if(bundleName == null)
			bundleName = "messages";

		return this.getMessageFromResourceBundle(bundleName, key, null, locale);
	}

	public   String getMessageFromResourceBundle(String key, String bundleName, Locale locale){
		if(locale == null){
			locale = this.getLocale();
		}

		if(bundleName == null)
			bundleName = "messages";

		return this.getMessageFromResourceBundle(bundleName, key, null, locale);
	}

	public String getMessageFromResourceBundle(
			String bundleName, 
			String key, 
			Object params[], 
			Locale locale){

		String text = null;
		try{
			ResourceBundle bundle = 
					ResourceBundle.getBundle(bundleName, locale, 
							this.getCurrentClassLoader(params));


			text = bundle.getString(key);
		} catch(MissingResourceException e){
			text = MISSING_RESOURCE_START_PLACEHOLDER + key + MISSING_RESOURCE_END_PLACEHOLDER;
		}
		if(params != null){
			MessageFormat mf = new MessageFormat(text, locale);
			text = mf.format(params, new StringBuffer(), null).toString();
		}
		return text;
	}

	// copy method from From E.R. Harold's book "Java I/O"
	public static void copy(InputStream in, OutputStream out) 
			throws IOException {

		// do not allow other threads to read from the
		// input or write to the output while copying is
		// taking place

		synchronized (in) {
			synchronized (out) {

				byte[] buffer = new byte[256];
				while (true) {
					int bytesRead = in.read(buffer);
					if (bytesRead == -1) break;
					out.write(buffer, 0, bytesRead);
				}
			}
		}
	}

	public static String getProperty(Properties reader, String name,boolean required) throws Exception{
		String tmp = null;

		tmp = reader.getProperty(name);

		if(tmp==null){
			if(required){
				throw new Exception("Property ["+name+"] not found");
			}
		}
		if(tmp!=null){
			return tmp.trim();
		}else{
			return null;
		}
	}

	public static Boolean getBooleanProperty(Properties reader,String name,boolean required) throws Exception{
		String propAsString = getProperty(reader,name, required);

		if(propAsString != null){
			Boolean b = new Boolean(propAsString.equalsIgnoreCase("true"));
			return b;
		}
		return null;
	}

	public ClassLoader getCurrentClassLoader(Object defaultObject){

		ClassLoader loader = Thread.currentThread().getContextClassLoader();

		if(loader == null){
			loader = defaultObject.getClass().getClassLoader();
		}

		return loader;
	}

	public Locale getLocale() {
		Locale locale = Locale.ITALY;

		return locale;
	}

	public static String getAbilitatoAsLabel(boolean abilitato){
		return getBooleanAsLabel(abilitato, "commons.label.abilitato","commons.label.nonAbilitato");
	}
	
	public static String getSiNoAsLabel(boolean abilitato){
		return getBooleanAsLabel(abilitato, "commons.label.si","commons.label.no");
	}

	public static String getBooleanAsLabel(boolean flag, String yesPropertyName, String noPropertyName ){
		if(flag)
			return getInstance().getMessageFromResourceBundle(yesPropertyName);
		else 
			return getInstance().getMessageFromResourceBundle(noPropertyName);
	}


	public static String getValue(List<RawParamValue> values, String parameterName){
		String toReturn = null;
		for(RawParamValue paramValue : values) {
			if(paramValue.getId().equals(parameterName))
				return paramValue.getValue();
		}

		return toReturn;
	}
	
	public static boolean isEmpty(List<?> lista){
		if(lista == null)
			return true;
		
		return lista.isEmpty();
	}
}
