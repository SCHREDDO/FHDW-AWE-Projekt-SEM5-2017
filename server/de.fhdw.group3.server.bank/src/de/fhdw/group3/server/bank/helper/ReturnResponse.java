package de.fhdw.group3.server.bank.helper;

import org.apache.log4j.Logger;

import de.fhdw.group3.server.bank.model.*;

/**
 * @author Sebastian L�hnen
 * Klasse zum �bermittel eines Account und eines Fehler Codes bzw. Erfolgs Code.
 */
public class ReturnResponse {
	
	protected String error;
	protected Account account;
	
	public String getError() {
		return error;
	}
	public Account getAccount() {
		return account;
	}

	/**
	 * @param error
	 * @param account
	 * erweiterter Standardkonstruktor
	 */
	public ReturnResponse(String error, Account account) {
		this.error = error;
		this.account = account;
	}
}