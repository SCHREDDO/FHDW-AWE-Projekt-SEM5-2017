package de.fhdw.group3.server.bank.helper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import de.fhdw.group3.server.bank.model.Account;
import de.fhdw.group3.server.bank.model.Transaction;

/**
 * @author Admin
 *
 */
public class ResultToObjectData {
	
	/**
	 * @param rs
	 * @return
	 */
	public static List<Account> resultToAccount(ResultSet rs) {
		List<Account> accList = new ArrayList<Account>();
		
		try {			
			do  {			
				accList.add(new Account(rs.getInt(1), rs.getString(2), rs.getString(3), new ArrayList<Transaction>()));
			} while(rs.next());
		} catch (Exception e) {
			
			System.out.println("resultToAccount(ResultSet rs) | " + e);
		}
		
		if (accList.size() == 0) {
			accList.add(new Account());
		}
		
		return accList;
	}
	
	/**
	 * @param rs
	 * @return
	 */
	public static List<Transaction> resultToTransaction(ResultSet rs) {
		List<Transaction> traList = new ArrayList<Transaction>();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		
		try {
			do  {
				traList.add(new Transaction(rs.getInt(1), new Account(rs.getString(7), rs.getString(8)), new Account(rs.getString(9), rs.getString(10)), new BigDecimal(rs.getString(4)), rs.getString(5), dateFormat.parse(rs.getString(6))));
			} while(rs.next());
		} catch (Exception e) {
			System.out.println("resultToTransaction(ResultSet rs) | " + e);
		}
		
		return traList;
	}
}