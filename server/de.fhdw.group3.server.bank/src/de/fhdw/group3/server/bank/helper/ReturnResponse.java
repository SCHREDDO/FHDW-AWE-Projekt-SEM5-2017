package de.fhdw.group3.server.bank.helper;

import de.fhdw.group3.server.bank.model.*;

public class ReturnResponse {
	
	protected String error;
	protected Account account;
	
	public String getError() {
		return error;
	}
	public Account getAccount() {
		return account;
	}

	public ReturnResponse(String error, Account account) {
		this.error = error;
		this.account = account;
	}
}