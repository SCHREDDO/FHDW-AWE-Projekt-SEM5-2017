package de.fhdw.group3.server.bank.helper;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import de.fhdw.group3.server.bank.model.Account;
import de.fhdw.group3.server.bank.model.Transaction;

public class ResultToObjectData {
	public static List<Account> resultToAccount(ResultSet rs) {
		List<Account> accList = new ArrayList<Account>();
		
		try {			
			while(rs.next()) {				
				accList.add(new Account(rs.getInt(1), rs.getString(2), rs.getString(3), new ArrayList<Transaction>()));
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		
		return accList;
	}
	
	public static List<Transaction> resultToTransaction(ResultSet rs) {
		List<Transaction> traList = new ArrayList<Transaction>();
		
		try {			
			while(rs.next()) {				
				traList.add(new Transaction(rs.getInt(1), new Account(rs.getString(7), rs.getString(8)), new Account(rs.getString(9), rs.getString(10)), rs.getBigDecimal(4), rs.getString(5), rs.getDate(6)));
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		
		return traList;
	}
}