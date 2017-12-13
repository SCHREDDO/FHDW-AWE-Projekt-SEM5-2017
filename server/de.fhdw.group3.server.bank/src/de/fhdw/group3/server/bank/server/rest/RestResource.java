package de.fhdw.group3.server.bank.server.rest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import de.fhdw.group3.server.bank.database.DBAccessJDBCSQLite;
import de.fhdw.group3.server.bank.helper.ReturnResponse;
import de.fhdw.group3.server.bank.model.Account;
import de.fhdw.group3.server.bank.model.ListAccount;
import de.fhdw.group3.server.bank.model.ListTransaction;
import de.fhdw.group3.server.bank.model.Transaction;

/**
 * @author Admin
 *
 */
@Path("/")
@Singleton
public class RestResource {
	private Logger logger = Logger.getLogger(getClass());
	
	//public interfaces
	
	// # = auskomentiren @ = einkoentiren | zum testen
	
	/**
	 * @param number
	 * @return
	 */
	@GET
	@Path("/account/{number}/")
	@Produces({ MediaType.APPLICATION_JSON + ";charset=utf-8"})
	public Response sendAccountData(@PathParam("number") String number) {
		logger.info("" + (new Date()) + ": " + "sendAccountData(number: " + number + "): Start");
		ReturnResponse rr = TransactionController.getAccountInfo(number); // #
				
		switch (rr.getError()) {
		case "500": logger.info("" + (new Date()) + ": " + "sendAccountData(number: " + number + "): Error(500)"); return Response.serverError().entity("Server side error.").build(); //500
		case "400": logger.info("" + (new Date()) + ": " + "sendAccountData(number: " + number + "): Error(400)"); return Response.status(Response.Status.BAD_REQUEST).build(); //400
		case "404": logger.info("" + (new Date()) + ": " + "sendAccountData(number: " + number + "): Error(404)"); return Response.status(Response.Status.NOT_FOUND).build(); //404
		default:
			break;
		}
		
		logger.info("" + (new Date()) + ": " + "sendAccountData(number: " + number + "): OK");
		return Response.ok(rr.getAccount()).build();
	}
	
	/**
	 * @param senderNumber
	 * @param receiverNumber
	 * @param amount
	 * @param reference
	 * @return
	 */
	@POST
	@Path("/transaction")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response executeTransaction(@FormParam("senderNumber") String senderNumber, @FormParam("receiverNumber") String receiverNumber, @FormParam("amount") String amount, @FormParam("reference") String reference) {
		logger.info("" + (new Date()) + ": " + "executeTransaction(senderNumber: " + senderNumber + ", receiverNumber: " + receiverNumber + ", amount: " + amount + ", reference: " + reference + "): Strat");
		String testString = amount;
		BigDecimal bigDecimal;
		
		if (senderNumber.equals("") || receiverNumber.equals("") || amount.equals("") || reference.equals("")) {
			logger.info("" + (new Date()) + ": " + "executeTransaction(senderNumber: " + senderNumber + ", receiverNumber: " + receiverNumber + ", amount: " + amount + ", reference: " + reference + "): Strat");
			return Response.status(Response.Status.BAD_REQUEST).build(); //400
		}
		
		if ((((amount.indexOf('.') == (amount.length() - 3) && amount.length() >= 4)) || amount.indexOf('.') == -1 ) && (testString.matches("[.0-9]+"))) {
			bigDecimal = new BigDecimal(amount);
		} else {
			logger.info("" + (new Date()) + ": " + "executeTransaction(senderNumber: " + senderNumber + ", receiverNumber: " + receiverNumber + ", amount: " + amount + ", reference: " + reference + "): Strat");
			return Response.status(Response.Status.BAD_REQUEST).build(); //400
		}
		
		if (!reference.matches("[ a-zA-Z0-9]*")) {
			logger.info("" + (new Date()) + ": " + "executeTransaction(senderNumber: " + senderNumber + ", receiverNumber: " + receiverNumber + ", amount: " + amount + ", reference: " + reference + "): Strat");
			return Response.status(Response.Status.BAD_REQUEST).build(); //400
		}
		
		String rr = TransactionController.newTransaction(senderNumber, receiverNumber, bigDecimal, reference); //#
		
		switch (rr) {
		case "500": logger.info("" + (new Date()) + ": " + "executeTransaction(senderNumber: " + senderNumber + ", receiverNumber: " + receiverNumber + ", amount: " + amount + ", reference: " + reference + "): Error(500)");
					return Response.serverError().entity("Server side error.").build(); //500
		case "400": logger.info("" + (new Date()) + ": " + "executeTransaction(senderNumber: " + senderNumber + ", receiverNumber: " + receiverNumber + ", amount: " + amount + ", reference: " + reference + "): Error(400)"); 
					return Response.status(Response.Status.BAD_REQUEST).build(); //400
		case "404": logger.info("" + (new Date()) + ": " + "executeTransaction(senderNumber: " + senderNumber + ", receiverNumber: " + receiverNumber + ", amount: " + amount + ", reference: " + reference + "): Error(404)"); 
					return Response.status(Response.Status.NOT_FOUND).build(); //404
		case "412": logger.info("" + (new Date()) + ": " + "executeTransaction(senderNumber: " + senderNumber + ", receiverNumber: " + receiverNumber + ", amount: " + amount + ", reference: " + reference + "): Error(412)"); 
					return Response.status(Response.Status.PRECONDITION_FAILED).build(); //412
		default:
			break;
		}

		logger.info("" + (new Date()) + ": " + "executeTransaction(senderNumber: " + senderNumber + ", receiverNumber: " + receiverNumber + ", amount: " + amount + ", reference: " + reference + "): OK");
		return Response.ok().build();
	}
	
	//Angular interfaces
	
	/**
	 * @param owner
	 * @param startAmount
	 * @return
	 */
	@POST
	@Path("/account/new")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response newAccount(@FormParam("ownerStartAmount") String ownerStartAmount) {
		logger.info("" + (new Date()) + ": " + "newAccount(ownerStartAmount: " + ownerStartAmount + "): Start");
		String[] parts = ownerStartAmount.split("-");		
		String owner = parts[0];
		BigDecimal startAmount = new BigDecimal(parts[1]);
		
		if (!owner.matches("[ a-zA-Z]*")) {
			logger.info("" + (new Date()) + ": " + "newAccount(ownerStartAmount: " + ownerStartAmount + "): Error(400)");
			return Response.ok().build();
		}
		
		Account account = new Account();
		String number = "";
		
		synchronized (DBAccessJDBCSQLite.class)
		{
			DBAccessJDBCSQLite db = new DBAccessJDBCSQLite();
			db.connectTODB();
			
			List<Account> accounts = db.getAccounts();
			
			int num = Integer.parseInt(accounts.get((accounts.size() - 1)).getNumber());
			num++;
			number = Integer.toString(num);
			
			account.setNumber(number);
			account.setOwner(owner);
			
			Account bankAccount = db.getAccount("1000");
			
			db.newAccount(account);
			account = db.getAccount(account.getNumber());
			
			Transaction transaction = new Transaction(0, bankAccount, account, startAmount, "START", new Date());
			
			db.newTransaction(transaction);
			
			db.disconnectFROMDB();
		}
		
		logger.info("" + (new Date()) + ": " + "newAccount(ownerStartAmount: " + ownerStartAmount + "): OK");
		
		return Response.ok().build();
	}
	
	@POST
	@Path("/account/update")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response updateAccount(@FormParam("newOwnerNumber") String newOwnerNumber) {
		logger.info("" + (new Date()) + ": " + "");
		String[] parts = newOwnerNumber.split("-");		
		String newOwner = parts[0];
		String number = parts[1];
		
		if (!newOwner.matches("[ a-zA-Z]*")) {
			logger.info("" + (new Date()) + ": " + "");
			return Response.ok().build();
		}
		
		if (!number.matches("[.0-9]*")) {
			logger.info("" + (new Date()) + ": " + "");
			return Response.ok().build();
		}
		
		Account account = new Account();
		account.setOwner(newOwner);
		account.setNumber(number);
		
		
		synchronized (DBAccessJDBCSQLite.class)
		{
			DBAccessJDBCSQLite db = new DBAccessJDBCSQLite();
			db.connectTODB();
			
			db.updateAccount(account);
			
			db.disconnectFROMDB();
		}
		
		logger.info("" + (new Date()) + ": " + "");
		
		return Response.ok().build();
	}
	
	/**
	 * @return
	 */
	@GET
	@Path("/account/all")
	@Produces({ MediaType.APPLICATION_JSON + ";charset=utf-8"})
	public Response getAllAccounts() {
		logger.info("" + (new Date()) + ": " + "getAllAccounts(): Start");
		ListAccount listAccount = new ListAccount();
		DBAccessJDBCSQLite db = new DBAccessJDBCSQLite();
		db.connectTODB();
		
		listAccount.setAccounts(db.getAccounts());
		
		db.disconnectFROMDB();
		
		logger.info("" + (new Date()) + ": " + "getAllAccounts(): OK");
		return Response.ok(listAccount).build();
	}
	
	/**
	 * @return
	 */
	@GET
	@Path("/transaction/all/{number}")
	@Produces({ MediaType.APPLICATION_JSON + ";charset=utf-8"})
	public Response getAllTransactions(@PathParam("number") String number) {
		logger.info("" + (new Date()) + ": " + "getAllTransactions(number: " + number + "): Start");
		ListTransaction listTransaction = new ListTransaction();
		
		DBAccessJDBCSQLite db = new DBAccessJDBCSQLite();
		db.connectTODB();
		
		if (number.equals("0")) {
			listTransaction.setTransactions(db.getTransactions());
		} else {
			Account account = db.getAccount(number);
			listTransaction.setTransactions(db.getTransactionsFromAccount(account.getId()));
		}
		
		db.disconnectFROMDB();
		
		logger.info("" + (new Date()) + ": " + "getAllTransactions(number: " + number + "): OK");
		
		return Response.ok(listTransaction).build();
	}
}
