package de.fhdw.group3.server.bank.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Admin
 *
 */
@XmlRootElement
public class ListAccount {
	private List<Account> accounts;
	
	public List<Account> getAccounts() {
		return accounts;
	}
	public void setAccounts(List<Account> accounts) {
		this.accounts = accounts;
	}

	/**
	 * 
	 */
	public ListAccount() {
		this.accounts = new ArrayList<>();
	}
}
