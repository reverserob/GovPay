package it.govpay.core.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;

import org.apache.logging.log4j.LogManager;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.logger.ILogger;
import org.openspcoop2.utils.logger.LoggerFactory;
import org.openspcoop2.utils.logger.beans.Message;
import org.openspcoop2.utils.logger.beans.proxy.Actor;
import org.openspcoop2.utils.logger.beans.proxy.Client;
import org.openspcoop2.utils.logger.beans.proxy.Operation;
import org.openspcoop2.utils.logger.beans.proxy.ProxyContext;
import org.openspcoop2.utils.logger.beans.proxy.Request;
import org.openspcoop2.utils.logger.beans.proxy.Role;
import org.openspcoop2.utils.logger.beans.proxy.Server;
import org.openspcoop2.utils.logger.beans.proxy.Service;
import org.openspcoop2.utils.logger.beans.proxy.Transaction;
import org.openspcoop2.utils.logger.constants.proxy.FlowMode;
import org.openspcoop2.utils.logger.constants.proxy.Result;

import it.gov.spcoop.nodopagamentispc.servizi.pagamentitelematicirpt.PagamentiTelematiciRPTservice;
import it.govpay.bd.model.Connettore.Versione;
import it.govpay.bd.model.Rpt;
import it.govpay.core.exceptions.NdpException.FaultPa;
import it.govpay.core.utils.client.NodoClient.Azione;
import it.govpay.servizi.PagamentiTelematiciPAService;
import it.govpay.servizi.commons.GpResponse;

public class GpContext {

	private List<ILogger> loggers;
	private List<Context> contexts;
	
	public static String NodoDeiPagamentiSPC = "NodoDeiPagamentiSPC";
	public static String GovPay = "GovPay";
	public static String TIPO_SOGGETTO_NDP = "NDP";
	public static String TIPO_SERVIZIO_NDP = "NDP";
	
	public static String TIPO_SOGGETTO_APP = "APP";
	public static String TIPO_SOGGETTO_PRT = "PRT";
	public static String TIPO_SOGGETTO_STAZIONE = "STZ";
	public static String TIPO_SOGGETTO_GOVPAY = "GP";
	public static String TIPO_SERVIZIO_GOVPAY = "GP";
	public static String TIPO_SERVIZIO_GOVPAY_WS = "GPWS";
	public static String TIPO_SERVIZIO_GOVPAY_BATCH = "GPB";
	
	
	public GpContext(MessageContext msgCtx, String tipoServizio, int versioneServizio) throws ServiceException {
		try {
			loggers = new ArrayList<ILogger>();
			ILogger logger = LoggerFactory.newLogger(new Context());	
			loggers.add(logger);
			
			contexts = new ArrayList<Context>();
			Context context = (Context) logger.getContext();
			context.getTransaction().setProtocol("govpay");
			contexts.add(context);
			
			Transaction transaction = context.getTransaction();
			transaction.setRole(Role.SERVER);
			
			Service service = new Service();
			service.setName(((QName) msgCtx.get(MessageContext.WSDL_SERVICE)).getLocalPart());
			service.setVersion(versioneServizio);
			service.setType(tipoServizio);
			
			transaction.setService(service);
			
			Operation operation = new Operation();
			operation.setMode(FlowMode.INPUT_OUTPUT);
			operation.setName(((QName) msgCtx.get(MessageContext.WSDL_OPERATION)).getLocalPart());
			transaction.setOperation(operation);
			
			HttpServletRequest servletRequest = (HttpServletRequest) msgCtx.get(MessageContext.SERVLET_REQUEST);
			Client client = new Client();
			client.setInvocationEndpoint(servletRequest.getRequestURI());
			client.setInterfaceName(((QName) msgCtx.get(MessageContext.WSDL_INTERFACE)).getLocalPart());
			if(((HttpServletRequest) msgCtx.get(MessageContext.SERVLET_REQUEST)).getUserPrincipal() != null)
				client.setPrincipal(((HttpServletRequest) msgCtx.get(MessageContext.SERVLET_REQUEST)).getUserPrincipal().getName());
			
			transaction.setClient(client);
			
			Server server = new Server();
			server.setName(GovPay);
			
			Actor to = new Actor();
			to.setName(GovPay);
			to.setType(TIPO_SOGGETTO_GOVPAY);
			transaction.setTo(to);
			
			transaction.setServer(server);
		} catch (UtilsException e) {
			throw new ServiceException(e);
		}
	}
	
	public GpContext() throws ServiceException {
		loggers = new ArrayList<ILogger>();
		contexts = new ArrayList<Context>();
		openTransaction();
	}
	
	public GpContext(String correlationId) throws ServiceException {
		this();
		setCorrelationId(correlationId);
	}
	
	public String openTransaction() throws ServiceException {
		try {
			ILogger logger = LoggerFactory.newLogger(new Context());	
			loggers.add(logger);
			
			Context context = (Context) logger.getContext();
			context.getTransaction().setProtocol("govpay");
			if(!contexts.isEmpty())
				context.getRequest().setCorrelationIdentifier(contexts.get(0).getIdTransaction());
			contexts.add(context);
			
			Request request = context.getRequest();
			request.setInDate(new Date());
			
			Transaction transaction = context.getTransaction();
			transaction.setRole(Role.CLIENT);
			
			return context.getIdTransaction();
		} catch (UtilsException e) {
			throw new ServiceException(e);
		}
	}
	
	public void closeTransaction(String idTransaction) {
		if(idTransaction == null) return;
		
		Context c = getContext(idTransaction);
		if(c != null) c.setActive(false);
	}
	
	public Context getContext(){
		for(int i=contexts.size() -1; i>=0; i--) {
			if(contexts.get(i).isActive) return contexts.get(i);
		}
		return null;
	}
	
	private Context getContext(String idTransaction){
		for(Context c : contexts) {
			if(c.getIdTransaction().equals(idTransaction))
					return c;
		}
		return null;
	}
	
	public void setupNodoClient(String codStazione, String codDominio, Azione azione) {
		Actor to = new Actor();
		to.setName(NodoDeiPagamentiSPC);
		to.setType(TIPO_SOGGETTO_NDP);
		GpThreadLocal.get().getTransaction().setTo(to);
		
		Actor from = new Actor();
		from.setName(codStazione);
		from.setType(TIPO_SOGGETTO_STAZIONE);
		GpThreadLocal.get().getTransaction().setFrom(from);
		
		GpThreadLocal.get().setInfoFruizione(TIPO_SERVIZIO_NDP, PagamentiTelematiciRPTservice.SERVICE.getLocalPart(), azione.toString(), Rpt.VERSIONE_ENCODED);
		
		Server server = new Server();
		server.setName(NodoDeiPagamentiSPC);
		GpThreadLocal.get().getTransaction().setServer(server);
		
		if(codDominio != null) {
			Client client = new Client();
			client.setName(codDominio);
			GpThreadLocal.get().getTransaction().setClient(client);
		}
	}
	
	public void setupPaClient(String codApplicazione, String azione, Versione versione) {
		Actor to = new Actor();
		to.setName(codApplicazione);
		to.setType(TIPO_SOGGETTO_APP);
		GpThreadLocal.get().getTransaction().setTo(to);
		
		Actor from = new Actor();
		from.setName(GovPay);
		from.setType(TIPO_SERVIZIO_GOVPAY);
		GpThreadLocal.get().getTransaction().setFrom(from);
		
		GpThreadLocal.get().setInfoFruizione(TIPO_SERVIZIO_GOVPAY_WS, PagamentiTelematiciPAService.SERVICE.getLocalPart(), azione, versione.getVersione());
		
		Server server = new Server();
		server.setName(codApplicazione);
		GpThreadLocal.get().getTransaction().setServer(server);
		
		Client client = new Client();
		client.setName(GovPay);
		GpThreadLocal.get().getTransaction().setClient(client);
	}
	
	private ILogger getActiveLogger(){
		for(int i=contexts.size()-1; i>=0; i--) {
			if(contexts.get(i).isActive) return loggers.get(i);
		}
		return null;
	}
	
	public void setInfoFruizione(String tipoServizio, String servizio, String operazione, int version) {
		Service service = new Service();
		service.setName(servizio);
		service.setVersion(version);
		service.setType(tipoServizio);
		getContext().getTransaction().setService(service);
		
		Operation operation = new Operation();
		operation.setMode(FlowMode.INPUT_OUTPUT);
		operation.setName(operazione);
		getContext().getTransaction().setOperation(operation);
	}
	
	public Transaction getTransaction() {
		return getContext().getTransaction();
	}
	
	public String getTransactionId() {
		return getContext().getIdTransaction();
	}
	
	public void setCorrelationId(String id) {
		contexts.get(0).getRequest().setCorrelationIdentifier(id);
	}

	public void setResult(GpResponse response) {
		if(response == null || response.getCodEsitoOperazione() == null) {
			getContext().getTransaction().setResult(Result.INTERNAL_ERROR);
			return;
		}
		switch (response.getCodEsitoOperazione()) {
		case OK:
			getContext().getTransaction().setResult(Result.SUCCESS);
			break;
		case INTERNAL:
			getContext().getTransaction().setResult(Result.INTERNAL_ERROR);
			break;
		default:
			getContext().getTransaction().setResult(Result.PROCESSING_ERROR);
			break;
		}
	}
	
	public void setResult(String faultCode) {
		if(faultCode == null) {
			getContext().getTransaction().setResult(Result.SUCCESS);
			return;
		}
			
		if(faultCode.equals(FaultPa.PAA_SYSTEM_ERROR.name())) {
			getContext().getTransaction().setResult(Result.INTERNAL_ERROR);
			return; 
		}
		
		getContext().getTransaction().setResult(Result.PROCESSING_ERROR);
	}
	
	public void log(String string, String...params) {
		try {
			getActiveLogger().log(string, params);
		} catch (Exception e) {
			LogManager.getLogger().error("Errore nell'emissione del diagnostico", e);
		}
	}
	
	public void log() {
		for(ILogger l : loggers) {
			try {
				l.log();
			} catch (UtilsException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void log(Message m) {
		try {
			getActiveLogger().log(m);
		} catch (Exception e) {
			LogManager.getLogger().error("Errore nell'emissione della transazione", e);
		}
	}
	
	public class Context extends ProxyContext {
		private static final long serialVersionUID = 1L;
		
		private boolean isActive = true;
		
		public boolean isActive() {
			return isActive;
		}

		public void setActive(boolean isActive) {
			this.isActive = isActive;
		}
	}
}