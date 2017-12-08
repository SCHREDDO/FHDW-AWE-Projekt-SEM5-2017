package de.fhdw.group3.server.bank.database;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import de.fhdw.group3.server.bank.helper.ResultToObjectData;
import de.fhdw.group3.server.bank.model.Account;
import de.fhdw.group3.server.bank.model.Transaction;

/**
 * @author Admin
 *
 */
public class DBAccessJDBCSQLite {

	private String url = "jdbc:sqlite:/SQLite/database";
	private Connection dB = null;

	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Connection getDB() {
		return dB;
	}
	public void setDB(Connection dB) {
		this.dB = dB;
	}

	/**
	 * 
	 */
	public DBAccessJDBCSQLite() {

	}

	/**
	 * @return
	 */
	public Boolean connectTODB() {
		try {
			Class.forName("org.sqlite.JDBC");
			setDB(DriverManager.getConnection(getUrl()));

			System.out.println("Connected to database.");
		} catch (SQLException | ClassNotFoundException e) {
			System.out.println(e);
			return false;
		}

		return true;
	}
	
	/**
	 * @return
	 */
	public boolean disconnectFROMDB() {
		try {
			getDB().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * @param autoCommit
	 * @return
	 */
	public boolean setAutoCommit(boolean autoCommit) {
		try {
			getDB().setAutoCommit(autoCommit);
		} catch (SQLException e) {
			return false;
		}

		return true;
	}

	/**
	 * @return
	 */
	public boolean commit() {
		try {
			getDB().commit();
		} catch (SQLException e) {
			return false;
		}

		return true;
	}

	/**
	 * @param number
	 * @return
	 */
	public Account getAccount(String number) {
		Account acc = new Account();
		String sql = "Select * FROM account WHERE number = ?";
		PreparedStatement statement;
		
		try {			
			statement = getDB().prepareStatement(sql);
			statement.setString(1, number);
			
			acc = ResultToObjectData.resultToAccount(statement.executeQuery()).get(0);
		} 
		catch (SQLException e) {
			System.out.println(e);
		}

		return acc;
	}

	/**
	 * @return
	 */
	public List<Account> getAccounts() {
		List<Account> accList = new ArrayList<Account>();
		String sql = "Select * FROM account";
		PreparedStatement statement;
		
		try {			
			statement = getDB().prepareStatement(sql);
			ResultSet rs = statement.executeQuery();
			
			while(rs.next()) {	
				accList = ResultToObjectData.resultToAccount(rs);
			}
		} 
		catch (SQLException e) {
			System.out.println(e);
		}

		return accList;
	}

	/**
	 * @param id
	 * @return
	 */
	public Transaction getTransaction(int id) {
		Transaction tra = new Transaction();
		String sql = "Select t.id, t.account_id_sender, t.account_id_receiver, t.amount, t.reference, t.transactionDate, as.owner, as.number, ar.owner, ar.number FROM transaction t, account as, account ar WHERE (as.id = t.account_id_sender AND ar.id = t.account_id_receiver) AND t.id = ?";
		PreparedStatement statement;
		
		try {			
			statement = getDB().prepareStatement(sql);
			statement.setInt(1, id);
			
			tra = ResultToObjectData.resultToTransaction(statement.executeQuery()).get(0);
		} 
		catch (SQLException e) {
			System.out.println(e);
		}
		
		return tra;
	}

	/**
	 * @return
	 */
	public List<Transaction> getTransactions() {
		List<Transaction> traList = new ArrayList<Transaction>();
		String sql = "Select t.id, t.account_id_sender, t.account_id_receiver, t.amount, t.reference, t.transactionDate, as.owner, as.number, ar.owner, ar.number FROM transaction t, account as, account ar WHERE (as.id = t.account_id_sender AND ar.id = t.account_id_receiver)";
		PreparedStatement statement;
		
		try {			
			statement = getDB().prepareStatement(sql);
			ResultSet rs = statement.executeQuery();
			
			while(rs.next()) {	
				traList = ResultToObjectData.resultToTransaction(rs);
			}
		} 
		catch (SQLException e) {
			System.out.println(e);
		}

		return traList;
	}

	/**
	 * @param id
	 * @return
	 */
	public List<Transaction> getTransactionsFromAccount(int id) {
		List<Transaction> traList = new ArrayList<Transaction>();
		String sql = "Select t.id, t.account_id_sender, t.account_id_receiver, t.amount, t.reference, t.transactionDate, as.owner, as.number, ar.owner, ar.number FROM transaction t, account as, account ar WHERE (as.id = t.account_id_sender AND ar.id = t.account_id_receiver) AND (t.account_id_sender = ? OR t.account_id_receiver = ?)";
		PreparedStatement statement;
		
		try {			
			statement = getDB().prepareStatement(sql);
			statement.setInt(1, id);
			statement.setInt(2, id);
			
			ResultSet rs = statement.executeQuery();
			
			while(rs.next()) {	
				traList = ResultToObjectData.resultToTransaction(rs);
			}
		} 
		catch (SQLException e) {
			System.out.println(e);
		}

		return traList;
	}
	
	/**
	 * @param number
	 * @return
	 */
	public BigDecimal getAccountBalance(String number) {
		BigDecimal accountBalance = new BigDecimal(0.0);;
		String sql = "Select as balance";
		PreparedStatement statement;
		
		try {			
			statement = getDB().prepareStatement(sql);
			
			ResultSet rs = statement.executeQuery();
			
			while(rs.next()) {	
				accountBalance = new BigDecimal(rs.getString(1));
			}
		} 
		catch (SQLException e) {
			System.out.println(e);
		}
		
		return accountBalance;
	}
	
	/**
	 * @param account
	 * @return
	 */
	public boolean newAccount(Account account) {
		String sql = "INSERT INTO account (id, owner, number) VALUES (?, ?, ?)";
		PreparedStatement statement;
		
		try {			
			statement = getDB().prepareStatement(sql);
			
			statement.setInt(1, account.getId());
			statement.setString(2, account.getOwner());
			statement.setString(3, account.getNumber());
		} 
		catch (SQLException e) {
			System.out.println(e);
			return false;
		}
		
		return true;
	}
	
	/**
	 * @param transaction
	 * @return
	 */
	public boolean newTransaction(Transaction transaction) {
		String sql = "INSERT INTO Transaction (id, account_id_sender, account_id_receiver, amount, reference, transactionDate) VALUES (?, ?, ?, ?, ?, ?)";
		PreparedStatement statement;
		
		try {			
			statement = getDB().prepareStatement(sql);
			
			statement.setInt(1, transaction.getId());
			statement.setInt(2, transaction.getSender().getId());
			statement.setInt(3, transaction.getReceiver().getId());
			statement.setBigDecimal(4, transaction.getAmount());
			statement.setString(5, transaction.getReference());
			statement.setDate(6, (java.sql.Date) transaction.getTransactionDate());
		} 
		catch (SQLException e) {
			System.out.println(e);
			return false;
		}
		
		return true;
	}

	/**
	 * @param account
	 * @return
	 */
	public boolean updateAccount(Account account) {
		String sql = "UPDATE account SET owner = ?, number = ? WHERE id = ?";
		PreparedStatement statement;
		
		try {			
			statement = getDB().prepareStatement(sql);
			
			statement.setString(1, account.getOwner());
			statement.setString(2, account.getNumber());
			statement.setInt(3, account.getId());
		} 
		catch (SQLException e) {
			System.out.println(e);
			return false;
		}
		
		return true;
	}

	/**
	 * @param transaction
	 * @return
	 */
	public boolean updateTransaction(Transaction transaction) {
		String sql = "UPDATE transaction SET account_id_sender = ?, account_id_receiver = ?, amount = ?, reference = ?, transactionDate = ? WHERE id = ?";
		PreparedStatement statement;
		
		try {			
			statement = getDB().prepareStatement(sql);
			
			statement.setInt(1, transaction.getSender().getId());
			statement.setInt(2, transaction.getReceiver().getId());
			statement.setBigDecimal(3, transaction.getAmount());
			statement.setString(4, transaction.getReference());
			statement.setDate(5, (java.sql.Date) transaction.getTransactionDate());
			statement.setInt(6, transaction.getId());
		} 
		catch (SQLException e) {
			System.out.println(e);
			return false;
		}
		
		return true;
	}
}