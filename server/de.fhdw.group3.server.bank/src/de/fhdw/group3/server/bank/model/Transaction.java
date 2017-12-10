package de.fhdw.group3.server.bank.model;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * @author Admin
 *
 */
@XmlRootElement
public class Transaction {
	
	private int id;
	private Account sender;
	private Account receiver;
	private BigDecimal amount;
	private String reference;
	private String transactionDate;

	@XmlTransient
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Account getSender() {
		return sender;
	}
	public void setSender(Account sender) {
		this.sender = sender;
	}
	public Account getReceiver() {
		return receiver;
	}
	public void setReceiver(Account receiver) {
		this.receiver = receiver;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	public String getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}

	/**
	 * 
	 */
	public Transaction() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		Date date = new Date();
		
		setId(0);
		setSender(new Account());
		setReceiver(new Account());
		setAmount(new BigDecimal("0.0"));
		setReference("");
		setTransactionDate(dateFormat.format(date));
	}

	/**
	 * @param id
	 * @param sender
	 * @param receiver
	 * @param amount
	 * @param reference
	 * @param transactionDate
	 */
	public Transaction(int id, Account sender,Account receiver, BigDecimal amount, String reference, Date transactionDate) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		
		setId(id);
		setSender(sender);
		setReceiver(receiver);
		setAmount(amount);
		setReference(reference);
		setTransactionDate(dateFormat.format(transactionDate));
	}
}