package de.fhdw.group3.server.bank.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Admin
 *
 */
@XmlRootElement
public class ListTransaction {
	
	List<Transaction> transactions;
	
	public List<Transaction> getTransactions() {
		return transactions;
	}
	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}
	
	/**
	 * 
	 */
	public ListTransaction() {
		this.transactions = new ArrayList<Transaction>();
	}

	
}
