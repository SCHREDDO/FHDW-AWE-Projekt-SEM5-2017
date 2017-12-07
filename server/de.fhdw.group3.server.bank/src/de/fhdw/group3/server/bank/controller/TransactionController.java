package de.fhdw.group3.server.bank.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import de.fhdw.group3.server.bank.helper.*;
import de.fhdw.group3.server.bank.model.*;
import de.fhdw.group3.server.bank.database.*;

public class TransactionController {
	public static String newAccount() {
		return "200";
	}
	
	public static String updateAccount() {
		return "200";
	}
	
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
		db.setAutoCommit(false);
		
		acc = db.getAccount(number);
		if (acc.getId() == 0) {
			return new ReturnResponse("404", acc);
		}
		
		traList = db.getTransactionsFromAccount(acc.getId());
		
		acc.setTransactions(traList);
		
		return new ReturnResponse("200", acc);
	}
	
	public static String newTransaction(String senderNumber, String receiverNumber, BigDecimal amount, String reference) {
		//500 (Fehler auf dem Server aufgetreten (z.B. SQL Exception))
		//400 (Daten mit falschem Format etc., sonstige Client-seitige Fehler)
		//404 (Account nicht gefunden)
		//412 (Nicht genug Geld für Überweisung)
		Transaction tra = new Transaction();
		Account accSender = new Account();
		Account accReceiver = new Account();
		
		DBAccessJDBCSQLite db = new DBAccessJDBCSQLite();
		db.connectTODB();
		db.setAutoCommit(false);
		
		accSender = db.getAccount(senderNumber);
		accReceiver = db.getAccount(receiverNumber);
		
		tra.setSender(accSender);
		tra.setReceiver(accReceiver);
		tra.setAmount(amount);
		tra.setReference(reference);
		
		db.newTransaction(tra);
		
		return "200";
	}
}