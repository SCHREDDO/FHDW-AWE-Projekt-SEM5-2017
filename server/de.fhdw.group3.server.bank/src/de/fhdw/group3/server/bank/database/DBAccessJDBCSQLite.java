package de.fhdw.group3.server.bank.database;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
	private boolean isSQLException;

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
	public boolean isSQLException() {
		return isSQLException;
	}
	public void setSQLException(boolean isSQLException) {
		this.isSQLException = isSQLException;
	}
	
	/**
	 * 
	 */
	public DBAccessJDBCSQLite() {
		setSQLException(false);
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
			setSQLException(true);
			System.out.println("connectTODB() | " + e);
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
			setSQLException(true);
			System.out.println("disconnectFROMDB() | " + e);
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
			setSQLException(true);
			System.out.println("setAutoCommit(boolean " + autoCommit + ") | " + e);
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
			setSQLException(true);
			System.out.println("commit() | " + e);
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
			setSQLException(true);
			System.out.println("getAccount(String " + number + ") | " + e);
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
			setSQLException(true);
			System.out.println("getAccounts() | " + e);
		}

		return accList;
	}

	/**
	 * @param id
	 * @return
	 */
	public Transaction getTransaction(int id) {
		Transaction tra = new Transaction();
		String sql = "Select t.id, t.account_id_sender, t.account_id_receiver, t.amount, t.reference, t.transactionDate, acs.owner, acs.number, acr.owner, acr.number FROM banktransaction t, account acs, account acr WHERE (acs.id = t.account_id_sender AND acr.id = t.account_id_receiver) AND t.id = ?";
		PreparedStatement statement;
		
		try {			
			statement = getDB().prepareStatement(sql);
			statement.setInt(1, id);
			
			tra = ResultToObjectData.resultToTransaction(statement.executeQuery()).get(0);
		} 
		catch (SQLException e) {
			setSQLException(true);
			System.out.println("getTransaction(int " + id + ") | " + e);
		}
		
		return tra;
	}

	/**
	 * @return
	 */
	public List<Transaction> getTransactions() {
		List<Transaction> traList = new ArrayList<Transaction>();
		String sql = "Select t.id, t.account_id_sender, t.account_id_receiver, t.amount, t.reference, t.transactionDate, acs.owner, acs.number, acr.owner, acr.number FROM banktransaction t, account acs, account acr WHERE (acs.id = t.account_id_sender AND acr.id = t.account_id_receiver)";
		PreparedStatement statement;
		
		try {			
			statement = getDB().prepareStatement(sql);
			ResultSet rs = statement.executeQuery();
			
			traList = ResultToObjectData.resultToTransaction(rs);
		} 
		catch (SQLException e) {
			setSQLException(true);
			System.out.println("getTransactions() | " + e);
		}

		return traList;
	}

	/**
	 * @param id
	 * @return
	 */
	public List<Transaction> getTransactionsFromAccount(int id) {
		List<Transaction> traList = new ArrayList<Transaction>();
		String sql = "Select t.id, t.account_id_sender, t.account_id_receiver, t.amount, t.reference, t.transactionDate, acs.owner, acs.number, acr.owner, acr.number FROM banktransaction t, account acs, account acr WHERE (acs.id = t.account_id_sender AND acr.id = t.account_id_receiver) AND (t.account_id_sender = ? OR t.account_id_receiver = ?) ORDER BY t.id DESC";

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
			setSQLException(true);
			System.out.println("getTransactionsFromAccount(int " + id + ") | " + e);
		}

		return traList;
	}
	
	/**
	 * @param number
	 * @return
	 */
	public BigDecimal getAccountBalance(String number) {
		BigDecimal accountBalance = new BigDecimal(0.0);;
		String sql = "Select ((Select case when SUM(t.amount) > 0 then SUM(t.amount) else 0 end FROM banktransaction t, account a WHERE t.account_id_receiver = a.id AND a.number = ?) - (Select case when SUM(t.amount) > 0 then SUM(t.amount) else 0 end FROM banktransaction t, account a WHERE t.account_id_sender = a.id AND a.number = ?)) as balance FROM account WHERE number = ?";
		PreparedStatement statement;
		
		try {			
			statement = getDB().prepareStatement(sql);
			statement.setString(1, number);
			statement.setString(2, number);
			statement.setString(3, number);
			
			ResultSet rs = statement.executeQuery();
			
			while(rs.next()) {	
				accountBalance = new BigDecimal(rs.getString(1));
			}
		} 
		catch (SQLException e) {
			setSQLException(true);
			System.out.println(e);
		}
		
		return accountBalance;
	}
	
	/**
	 * @param account
	 * @return
	 */
	public boolean newAccount(Account account) {
		String sql = "INSERT INTO account (owner, number) VALUES (?, ?)";
		PreparedStatement statement;
		
		try {			
			statement = getDB().prepareStatement(sql);
			
			statement.setInt(1, account.getId());
			statement.setString(2, account.getOwner());
			statement.setString(3, account.getNumber());
			
			statement.execute();
		} 
		catch (SQLException e) {
			setSQLException(true);
			System.out.println("newAccount(Account " + account + ") | " + e);
			return false;
		}
		
		return true;
	}
	
	/**
	 * @param transaction
	 * @return
	 */
	public boolean newTransaction(Transaction transaction) {
		String sql = "INSERT INTO banktransaction (account_id_sender, account_id_receiver, amount, reference, transactionDate) VALUES (?, ?, ?, ?, ?)";
		PreparedStatement statement;
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		
		try {
			System.out.println("|1");
			statement = getDB().prepareStatement(sql);

			statement.setInt(1, transaction.getSender().getId());
			statement.setInt(2, transaction.getReceiver().getId());
			statement.setString(3, transaction.getAmount().toString());
			statement.setString(4, transaction.getReference());
			statement.setString(5, transaction.getTransactionDate());
			
			statement.execute();
		} 
		catch (SQLException e) {
			setSQLException(true);
			System.out.println("newTransaction(Transaction " + transaction + ") | " + e);
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
			
			statement.execute();
		} 
		catch (SQLException e) {
			setSQLException(true);
			System.out.println("updateAccount(Account " + account + ") | " + e);
			return false;
		}
		
		return true;
	}

	/**
	 * @param transaction
	 * @return
	 */
	public boolean updateTransaction(Transaction transaction) {
		String sql = "UPDATE banktransaction SET account_id_sender = ?, account_id_receiver = ?, amount = ?, reference = ?, transactionDate = ? WHERE id = ?";
		PreparedStatement statement;
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		
		try {			
			statement = getDB().prepareStatement(sql);
			
			statement.setInt(1, transaction.getSender().getId());
			statement.setInt(2, transaction.getReceiver().getId());
			statement.setString(3, transaction.getAmount().toString());
			statement.setString(4, transaction.getReference());
			statement.setString(5, transaction.getTransactionDate());
			statement.setInt(6, transaction.getId());
			
			statement.execute();
		} 
		catch (SQLException e) {
			setSQLException(true);
			System.out.println("updateTransaction(Transaction " + transaction + ") | " + e);
			return false;
		}
		
		return true;
	}
}