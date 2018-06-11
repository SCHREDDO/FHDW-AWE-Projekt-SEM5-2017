package de.fhdw.group3.server.bank.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * @author Sebastian Lühnen
 * Klasse Account beinhaltet ID, Besitzer, Kontonummer und Liste von allen Transaction.
 */
@XmlRootElement
public class Account {
	
	private int id;
	private String owner;
	private String number;
	private List<Transaction> transactions;

	@XmlTransient
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public List<Transaction> getTransactions() {
		return transactions;
	}
	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}

	/**
	 * Standardkonstruktor
	 */
	public Account() {
		setId(0);
		setOwner("");
		setNumber("");
		setTransactions(new ArrayList<Transaction>());
	}
	
	/**
	 * @param owner
	 * @param number
	 * erweiterter Standardkonstruktor
	 */
	public Account(String owner, String number) {
		setOwner(owner);
		setNumber(number);
	}

	/**
	 * @param id
	 * @param owner
	 * @param number
	 * @param transactions
	 * erweiterter Standardkonstruktor
	 */
	public Account(int id, String owner, String number, List<Transaction> transactions) {
		setId(id);
		setOwner(owner);
		setNumber(number);
		setTransactions(transactions);
	}
}