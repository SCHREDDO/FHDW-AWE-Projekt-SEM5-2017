package de.fhdw.group3.server.bank.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

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
	private Date transactionDate;

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
	public Date getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	/**
	 * 
	 */
	public Transaction() {
		setId(0);
		setSender(new Account());
		setReceiver(new Account());
		setAmount(new BigDecimal("0.0"));
		setReference("");
		setTransactionDate(new Date());
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
		setId(id);
		setSender(sender);
		setReceiver(receiver);
		setAmount(amount);
		setReference(reference);
		setTransactionDate(transactionDate);
	}
}