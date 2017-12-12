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
		ReturnResponse rr = TransactionController.getAccountInfo(number); // #
		
		//List<Transaction> test = new ArrayList<Transaction>(); // @
		//test.add(new Transaction(1, new Account("Test O1", "8888"), new Account("Test O2", "7777"), new BigDecimal(300.00), "Test T", new Date())); // @
		//test.add(new Transaction(2, new Account("Test O3", "5555"), new Account("Test O4", "6666"), new BigDecimal(500.00), "Test T", new Date())); // @
		//ReturnResponse rr = new ReturnResponse(number, new Account(1, "Test Owner", "1111", test)); // @
		
		switch (rr.getError()) {
		case "500": return Response.serverError().entity("Server side error.").build(); //500
		case "400": return Response.status(Response.Status.BAD_REQUEST).build(); //400
		case "404": return Response.status(Response.Status.NOT_FOUND).build(); //404
		default:
			break;
		}
		
		
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
		System.out.println("================="); // @
		System.out.println("================="); // @
		System.out.println("================="); // @
		System.out.println("================="); // @
		
		System.out.println("= " + senderNumber); // @
		System.out.println("= " + receiverNumber); // @
		System.out.println("= " + amount); // @
		System.out.println("= " + reference); // @
		String testString = amount;
		BigDecimal bigDecimal;
		
		System.out.println("T= " + amount.length());
		System.out.println("T= " + (((amount.indexOf('.') == (amount.length() - 3) && amount.length() >= 4)) || amount.indexOf('.') == -1 ));
		System.out.println("T= " + (testString.matches("[0-9]+")));
		
		if (senderNumber.equals("") || receiverNumber.equals("") || amount.equals("") || reference.equals("")) {
			System.out.println("#1"); // @
			return Response.status(Response.Status.BAD_REQUEST).build(); //400
		}
		
		if ((((amount.indexOf('.') == (amount.length() - 3) && amount.length() >= 4)) || amount.indexOf('.') == -1 ) && (testString.matches("[.0-9]+"))) {
			bigDecimal = new BigDecimal(amount);
		} else {
			System.out.println("#2"); // @
			return Response.status(Response.Status.BAD_REQUEST).build(); //400
		}
		
		System.out.println("abc " + reference.matches("[ a-zA-Z0-9]*")); // @
		if (!reference.matches("[ a-zA-Z0-9]*")) {
			System.out.println("#3"); // @
			return Response.status(Response.Status.BAD_REQUEST).build(); //400
		}
		
		
		
		String rr = TransactionController.newTransaction(senderNumber, receiverNumber, bigDecimal, reference); //#
		
		switch (rr) {
		case "500": return Response.serverError().entity("Server side error.").build(); //500
		case "400": return Response.status(Response.Status.BAD_REQUEST).build(); //400
		case "404": return Response.status(Response.Status.NOT_FOUND).build(); //404
		case "412": return Response.status(Response.Status.PRECONDITION_FAILED).build(); //412
		default:
			break;
		}

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
		System.out.println(ownerStartAmount);
		
		String[] parts = ownerStartAmount.split("-");		
		String owner = parts[0];
		BigDecimal startAmount = new BigDecimal(parts[1]);
		
		Account account = new Account();
		String number = "";
		
		account.setNumber(number);
		account.setOwner(owner);
		
		synchronized (DBAccessJDBCSQLite.class)
		{
			DBAccessJDBCSQLite db = new DBAccessJDBCSQLite();
			db.connectTODB();
			
			Account bankAccount = db.getAccount("1000");
			
			db.newAccount(account);
			account = db.getAccount(account.getNumber());
			
			Transaction transaction = new Transaction(0, bankAccount, account, startAmount, "START", new Date());
			
			db.newTransaction(transaction);
			
			db.disconnectFROMDB();
		}
		
		return Response.ok().build();
	}
	
	@POST
	@Path("/account/update")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response updateAccount(@FormParam("newOwnerNumber") String newOwnerNumber) {
		System.out.println(newOwnerNumber);
		
		String[] parts = newOwnerNumber.split("-");		
		String newOwner = parts[0];
		String number = parts[1];
		
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
		
		return Response.ok().build();
	}
	
	/**
	 * @return
	 */
	@GET
	@Path("/account/all")
	@Produces({ MediaType.APPLICATION_JSON + ";charset=utf-8"})
	public Response getAllAccounts() {		
		ListAccount listAccount = new ListAccount();
		/*listAccount.getAccounts().add(new Account("Test O1", "5555"));
		listAccount.getAccounts().add(new Account("Test O2", "7777"));
		listAccount.getAccounts().add(new Account("Test O3", "8888"));*/
		DBAccessJDBCSQLite db = new DBAccessJDBCSQLite();
		db.connectTODB();
		
		listAccount.setAccounts(db.getAccounts());
		
		db.disconnectFROMDB();
		
		
		return Response.ok(listAccount).build();
	}
	
	/**
	 * @return
	 */
	@GET
	@Path("/transaction/all/{number}")
	@Produces({ MediaType.APPLICATION_JSON + ";charset=utf-8"})
	public Response getAllTransactions(@PathParam("number") String number) {
		System.out.println(number);
		
		ListTransaction listTransaction = new ListTransaction();
		/*listTransaction.getTransactions().add(new Transaction(1, new Account("Test O1", "8888"), new Account("Test O2", "7777"), new BigDecimal(300.00), "Test T", new Date()));
		listTransaction.getTransactions().add(new Transaction(2, new Account("Test O3", "5555"), new Account("Test O4", "6666"), new BigDecimal(500.00), "Test T", new Date()));
		listTransaction.getTransactions().add(new Transaction(2, new Account("Test O3", "5555"), new Account("Test O4", "6666"), new BigDecimal(500.00), "Test T", new Date()));
		listTransaction.getTransactions().add(new Transaction(2, new Account("Test O3", "5555"), new Account("Test O4", "6666"), new BigDecimal(500.00), "Test T", new Date()));*/
		
		DBAccessJDBCSQLite db = new DBAccessJDBCSQLite();
		db.connectTODB();
		
		if (number.equals("0")) {
			listTransaction.setTransactions(db.getTransactions());
		} else {
			Account account = db.getAccount(number);
			listTransaction.setTransactions(db.getTransactionsFromAccount(account.getId()));
		}
		
		db.disconnectFROMDB();
		
		
		return Response.ok(listTransaction).build();
	}
}
