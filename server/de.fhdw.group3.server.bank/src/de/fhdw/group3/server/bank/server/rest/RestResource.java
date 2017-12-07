package de.fhdw.group3.server.bank.server.rest;

import java.math.BigDecimal;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.sun.jersey.spi.resource.Singleton;

import de.fhdw.group3.server.bank.controller.TransactionController;
import de.fhdw.group3.server.bank.helper.ReturnResponse;

@Path("/")
@Singleton
public class RestResource {
	private Logger logger = Logger.getLogger(getClass());

	@GET
	@Path("/account/{number}/")
	@Produces({ MediaType.APPLICATION_JSON + ";charset=utf-8"})
	public Response sendAccountData(@PathParam("number") String number) {
		ReturnResponse rr = TransactionController.getAccountInfo(number);
		
		switch (rr.getError()) {
		case "500": return Response.serverError().entity("Server side error.").build(); //500
		case "400": return Response.status(Response.Status.BAD_REQUEST).build(); //400
		case "404": return Response.status(Response.Status.NOT_FOUND).build(); //404
		default:
			break;
		}
		
		return Response.ok(rr.getAccount()).build();
	}
	
	@POST
	@Path("/transaction")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response executeTransaction(@FormParam("senderNumber") String senderNumber, @FormParam("receiverNumber") String receiverNumber, @FormParam("amount") BigDecimal amount, @FormParam("reference") String reference) {

		switch (TransactionController.newTransaction(senderNumber, receiverNumber, amount, reference)) {
		case "500": return Response.serverError().entity("Server side error.").build(); //500
		case "400": return Response.status(Response.Status.BAD_REQUEST).build(); //400
		case "404": return Response.status(Response.Status.NOT_FOUND).build(); //404
		case "412": return Response.status(Response.Status.PRECONDITION_FAILED).build(); //412
		default:
			break;
		}

		return Response.ok().build();
	}
}
