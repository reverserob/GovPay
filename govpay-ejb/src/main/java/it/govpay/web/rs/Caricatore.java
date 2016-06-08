package it.govpay.web.rs;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import it.govpay.web.rs.model.CaricamentoRequest;
import it.govpay.web.rs.model.CaricamentoResponse;
import it.govpay.web.rs.model.EstrattoContoRequest;
import it.govpay.web.rs.model.Pagamento;
import it.govpay.web.rs.model.Versamento;
import it.govpay.bd.BasicBD;
import it.govpay.web.rs.BaseRsService;

@Path("/caricatore")
public class Caricatore extends BaseRsService{

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); //"yyyy-MM-dd"

	private Logger log = LogManager.getLogger();

	@POST
	@Path("/caricaVersamenti")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public Response caricaVersamenti(CaricamentoRequest request, @Context UriInfo uriInfo){
		String methodName = "Carica Versamenti"; 
		this.initLogger(methodName);
		BasicBD bd = null;
		try{
			bd = BasicBD.newInstance();

			CaricamentoResponse response = new  CaricamentoResponse();
			response.setAnagraficaContribuente(request.getAnagraficaContribuente());
			response.setCodiceCreditore(request.getCodiceCreditore());
			response.setCodiceFiscaleContribuente(request.getCodiceFiscaleContribuente());
			response.setCodiceTributo(request.getCodiceTributo());
			response.setAnnoTributario(request.getAnnoTributario());

			if(!isEmpty(request.getVersamenti())){
				Random r = new Random();
				List<Versamento> versamenti = new ArrayList<Versamento>();
				for (Versamento versamentoRequest : request.getVersamenti()) {
					Versamento versamentoResponse = new Versamento();

					versamentoResponse.setCodiceVersamento(versamentoRequest.getCodiceVersamento());
					versamentoResponse.setImporto(versamentoRequest.getImporto());
					versamentoResponse.setDataScadenza(versamentoRequest.getDataScadenza());
					versamentoResponse.setEsito("OK");

					String iuv = "" + ( System.currentTimeMillis() + r.nextInt(100));
					versamentoResponse.setIuv(iuv);
					String idOperazione = UUID.randomUUID().toString().replace("-", "");
					versamentoResponse.setIdOperazione(idOperazione );


					String identificativoGovPay = request.getCodiceTributo() + 
							request.getAnnoTributario() + request.getCodiceFiscaleContribuente() + versamentoRequest.getCodiceVersamento() ;
					versamentoResponse.setIdentificativoGovPay(identificativoGovPay );

					String qrCode = "PAGOPA|01|0|" + iuv + "|...";
					versamentoResponse.setQrCode(qrCode );

					String barCode = "01" + iuv + "...";
					versamentoResponse.setBarCode(barCode );

					versamenti.add(versamentoResponse);
				}

				response.setVersamenti(versamenti); 
			}

			return Response.ok(response).build();
		}catch(Exception e){
			log.error("Errore durante l'esecuzione del metodo "+methodName+":" + e.getMessage(),e);
			return Response.serverError().build();
		}
	}

	@POST
	@Path("/estrattoConto")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public Response estrattoConto(EstrattoContoRequest request, @Context UriInfo uriInfo){
		String methodName = "Estratto Conto"; 
		this.initLogger(methodName);
		BasicBD bd = null;
		
		try{
			bd = BasicBD.newInstance();
			
			Random r = new Random();
			List<Pagamento> lst = new ArrayList<Pagamento>();

			Pagamento p1 = new Pagamento();
			p1.setCodiceCreditore("01234567890");
			p1.setCodiceTributo("TARI");
			p1.setCodiceFiscaleContribuente("RSSMRA75L01H501A");
			p1.setAnagraficaContribuente("Rossi Mario");
			p1.setAnnoTributario(2017);
			p1.setCodiceVersamento("O");
			Date dataPag1 = new Date(this.sdf.parse("2017-03-29").getTime());
			p1.setDataPagamento(dataPag1);
			p1.setImporto(136.50);
			p1.setCodiceRendicontazione("UCCBI" + p1.getAnnoTributario() + "121212");
			p1.setCodiceRiversamento("abcd1234567890");
			String iuv1 = "" + ( System.currentTimeMillis() + r.nextInt(100));
			p1.setIuv(iuv1);
			String identificativoGovPay1 = p1.getCodiceTributo() + 
					p1.getAnnoTributario() + p1.getCodiceFiscaleContribuente() + p1.getCodiceVersamento() ;

			p1.setIdentificativoGovPay(identificativoGovPay1);

			lst.add(p1);

			Pagamento p2 = new Pagamento();
			p2.setCodiceCreditore("01234567890");
			p2.setCodiceTributo("IRPEF");
			p2.setCodiceFiscaleContribuente("VRDLGU75L01H501A");
			p2.setAnagraficaContribuente("Verdi Luigi");
			p2.setAnnoTributario(2017);
			p2.setCodiceVersamento("O");
			Date dataPag2 = new Date(this.sdf.parse("2017-03-26").getTime());
			p2.setDataPagamento(dataPag2);
			p2.setImporto(213.56);
			p2.setCodiceRiversamento("efgh0987654321");
			String iuv2 = "" + ( System.currentTimeMillis() + r.nextInt(100));
			p2.setIuv(iuv2);
			String identificativoGovPay2 = p2.getCodiceTributo() + 
					p2.getAnnoTributario() + p2.getCodiceFiscaleContribuente() + p2.getCodiceVersamento() ;

			p2.setIdentificativoGovPay(identificativoGovPay2);

			lst.add(p2);


			return Response.ok(lst).build();
		}catch(Exception e){
			log.error("Errore durante l'esecuzione del metodo "+methodName+":" + e.getMessage(),e);
			return Response.serverError().build();
		}
	}

	@POST
	@Path("/notificaPagamento")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public Response notificapagamento(Pagamento pagamento, @Context UriInfo uriInfo){
		String methodName = "Notifica Pagamento"; 
		this.initLogger(methodName);
		BasicBD bd = null;
		
		try{
			bd = BasicBD.newInstance();
			return Response.ok().build();

		}catch(Exception e){
			log.error("Errore durante l'esecuzione del metodo "+methodName+":" + e.getMessage(),e);
			return Response.serverError().build();
		}
	}
}
