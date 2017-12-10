package de.fhdw.group3.server.bank.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import de.fhdw.group3.server.bank.helper.*;
import de.fhdw.group3.server.bank.model.*;
import de.fhdw.group3.server.bank.database.*;

/**
 * @author Admin
 *
 */
public class TransactionController {
	
	public static String newAccount() {
		return "200";
	}
	
	public static String updateAccount() {
		return "200";
	}
	
	/**
	 * @param number
	 * @return
	 */
	public static ReturnResponse getAccountInfo(String number) {
		//500 (Fehler auf dem Server aufgetreten (z.B. SQL Exception))
		//400 (Daten mit falschem Format etc., sonstige Client-seitige Fehler)
		//404 (Account nicht gefunden)
		Account acc = new Account();
		List<Transaction> traList = new ArrayList<Transaction>();
		
		if (number.length() != 4 || !number.matches("[0-9]+")) {
			return new ReturnResponse("400", acc);
		}
		
		DBAccessJDBCSQLite db = new DBAccessJDBCSQLite();
		db.connectTODB();
		
		acc = db.getAccount(number);
		if (db.isSQLException()) {
			return new ReturnResponse("500", acc);
		}

		if (acc.getId() == 0) {
			return new ReturnResponse("404", acc);
		}
		
		traList = db.getTransactionsFromAccount(acc.getId());
		
		if (db.isSQLException()) {
			return new ReturnResponse("500", acc);
		}
		
		db.disconnectFROMDB();
		
		acc.setTransactions(traList);
		
		return new ReturnResponse("200", acc);
	}
	
	/**
	 * @param senderNumber
	 * @param receiverNumber
	 * @param amount
	 * @param reference
	 * @return
	 */
	public static String newTransaction(String senderNumber, String receiverNumber, BigDecimal amount, String reference) {
		//500 (Fehler auf dem Server aufgetreten (z.B. SQL Exception))
		//400 (Daten mit falschem Format etc., sonstige Client-seitige Fehler)
		//404 (Account nicht gefunden)
		//412 (Nicht genug Geld für Überweisung)
		Transaction tra = new Transaction();
		Account accSender = new Account();
		Account accReceiver = new Account();
		BigDecimal minAmount = new BigDecimal(0.00);
		BigDecimal senderAmount;
		
		if (senderNumber.length() != 4 || !senderNumber.matches("[0-9]+")) {
			System.out.println("1");
			return "400";
		}
		
		if (receiverNumber.length() != 4 || !receiverNumber.matches("[0-9]+")) {
			System.out.println("2");
			return "400";
		}
		
		if (senderNumber.equals(receiverNumber)) {
			System.out.println("4");
			return "400";
		}
		
		if (!(1 == amount.compareTo(minAmount))) {
			System.out.println("3");
			return "412";
		}
		
		DBAccessJDBCSQLite db = new DBAccessJDBCSQLite();
		db.connectTODB();
		
		accSender = db.getAccount(senderNumber);
		if (db.isSQLException()) {
			System.out.println("5");
			return "500";
		}
		
		if (accSender.getId() == 0) {
			return "404";
		}
		
		accReceiver = db.getAccount(receiverNumber);
		if (db.isSQLException()) {
			System.out.println("6");
			return "500";
		}
		
		if (accReceiver.getId() == 0) {
			return "404";
		}
		
		senderAmount = db.getAccountBalance(accSender.getNumber());
		if (db.isSQLException()) {
			System.out.println("7");
			return "500";
		}
		System.out.println("a1 " + amount);
		System.out.println("a2 " + senderAmount);
		
		System.out.println("t1 " + (1 == amount.compareTo(senderAmount)));
		System.out.println("t2 " + !(0 == amount.compareTo(senderAmount)));
		System.out.println("t3 " + !accSender.getOwner().equals("BANK"));
		
		if (1 == amount.compareTo(senderAmount) && !accSender.getOwner().equals("BANK")) {
			System.out.println("8");
			return "412";
		}
		
		tra.setSender(accSender);
		tra.setReceiver(accReceiver);
		tra.setAmount(amount);
		tra.setReference(reference);

		db.newTransaction(tra);
		if (db.isSQLException()) {
			System.out.println("9");
			return "500";
		}
		
		System.out.println("S = " + db.getAccountBalance("1001"));
		System.out.println("R = " + db.getAccountBalance("1002"));
		
		db.disconnectFROMDB();
		System.out.println("10");
		
		return "200";
	}
}

/*
	synchronized (DemoClass.class)
	{
	    //other thread safe code
	}
*/