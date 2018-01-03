package de.fhdw.group3.server.bank.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import de.fhdw.group3.server.bank.helper.*;
import de.fhdw.group3.server.bank.model.*;
import de.fhdw.group3.server.bank.database.*;

/**
 * @author Sebastian Lühnen
 * Koordiniert ablauf die erstellung einer Transaction oder die beschaffung von Information von einen Account.
 */
public class TransactionController {
	
	/**
	 * @param number
	 * @return ein Account + Fehler oder Erfolgs Code
	 * Koordiniert beschaffung von Information von einen Account.
	 */
	public static ReturnResponse getAccountInfo(String number) {
		//500 (Fehler auf dem Server aufgetreten (z.B. SQL Exception))
		//400 (Daten mit falschem Format etc., sonstige Client-seitige Fehler)
		//404 (Account nicht gefunden)
		Account acc = new Account();
		List<Transaction> traList = new ArrayList<Transaction>();
		
		//Über prüft ob die Kontonummer die richtige lange besteht und eine positive Zahl ist.
		if (number.length() != 4 || !number.matches("[0-9]+")) {
			return new ReturnResponse("400", acc);
		}
		
		synchronized (DBAccessJDBCSQLite.class)
		{
			DBAccessJDBCSQLite db = new DBAccessJDBCSQLite();
			db.connectTODB();
			
			//Generalle Fehler Überprüfung
			acc = db.getAccount(number);
			if (db.isSQLException()) {
				return new ReturnResponse("500", acc);
			}
			
			//Überprüft ob Account existirt.
			if (acc.getId() == 0) {
				return new ReturnResponse("404", acc);
			}
			
			traList = db.getTransactionsFromAccount(acc.getId());
			
			//Generalle Fehler Überprüfung
			if (db.isSQLException()) {
				return new ReturnResponse("500", acc);
			}
			
			db.disconnectFROMDB();
			
			acc.setTransactions(traList);
		}
		
		return new ReturnResponse("200", acc);
	}
	
	/**
	 * @param senderNumber
	 * @param receiverNumber
	 * @param amount
	 * @param reference
	 * @return Fehler oder Erfolgs Code
	 * Koordiniert ablauf die erstellung einer Transaction.
	 */
	public static String newTransaction(String senderNumber, String receiverNumber, BigDecimal amount, String reference) {
		//500 (Fehler auf dem Server aufgetreten (z.B. SQL Exception))
		//400 (Daten mit falschem Format etc., sonstige Client-seitige Fehler)
		//404 (Account nicht gefunden)
		//412 (Nicht genug Geld für Überweisung)
		synchronized (DBAccessJDBCSQLite.class)
		{
			Transaction tra = new Transaction();
			Account accSender = new Account();
			Account accReceiver = new Account();
			BigDecimal minAmount = new BigDecimal(0.00);
			BigDecimal senderAmount;
			
			//Über prüft ob die Kontonummer die richtige lange besteht und eine positive Zahl ist.
			if (senderNumber.length() != 4 || !senderNumber.matches("[0-9]+")) {
				return "400";
			}
			
			//Über prüft ob die Kontonummer die richtige lange besteht und eine positive Zahl ist.
			if (receiverNumber.length() != 4 || !receiverNumber.matches("[0-9]+")) {
				return "400";
			}
			
			//Überprüft ob zum selben Konto überwisen werden soll.
			if (senderNumber.equals(receiverNumber)) {
				return "400";
			}
			
			//Über pruft ob die zu Überweißende Mange der Mindes Mange etspricht.
			if (!(1 == amount.compareTo(minAmount))) {
				return "412";
			}
			
			DBAccessJDBCSQLite db = new DBAccessJDBCSQLite();
			db.connectTODB();
			
			//Generalle Fehler Überprüfung
			accSender = db.getAccount(senderNumber);
			if (db.isSQLException()) {
				return "500";
			}
			
			//Überprüft ob Account existirt.
			if (accSender.getId() == 0) {
				return "404";
			}
			
			//Generalle Fehler Überprüfung
			accReceiver = db.getAccount(receiverNumber);
			if (db.isSQLException()) {
				return "500";
			}
			
			//Überprüft ob Account existirt.
			if (accReceiver.getId() == 0) {
				return "404";
			}
			
			//Generalle Fehler Überprüfung
			senderAmount = db.getAccountBalance(accSender.getNumber());
			if (db.isSQLException()) {
				return "500";
			}
			
			//Über prüft ob der Konto Inhaber genögent Geld auf den Konto hat für die Überweisung, auser wenn es die Bank ist. 
			if (1 == amount.compareTo(senderAmount) && !accSender.getOwner().equals("BANK")) {
				return "412";
			}
			
			tra.setSender(accSender);
			tra.setReceiver(accReceiver);
			tra.setAmount(amount);
			tra.setReference(reference);
	
			db.newTransaction(tra);
			if (db.isSQLException()) {
				return "500";
			}
			
			db.disconnectFROMDB();
		}
		
		return "200";
	}
}