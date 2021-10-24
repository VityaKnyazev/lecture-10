package by.itacademy.javaenterprise.knyazev.queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Saver implements IQuery{
	private Connection connection;
	private PreparedStatement preparedStarement;
	private final Logger logger;
	
	public final static int SAVING_ID = 0;
	
	public Saver(Connection connection) {
		this.connection = connection;
		logger = LoggerFactory.getLogger(getClass());
		logger.info("Saver receive the connection.");
	}
	 
	public int save(String tableName, String[] columnNames, String[] columnValues, int id) throws SQLException {
		int result = 0;
		String query = "";
		
		if (tableName == null) return result;
		if (columnNames == null || columnNames.length == 0) return result;
		if (columnValues == null || columnValues.length != columnValues.length) return result;
		
		
		if (id > 0) {
			String parameters = convertUpdateStr(columnNames);
			query = "UPDATE " + tableName + " SET " + parameters + " WHERE id = ?";
			
			logger.info("Preparing update query: " + query);
			
			preparedStarement = connection.prepareStatement(query);
			
			for (int i = 0; i < columnValues.length; i++) {
				preparedStarement.setString(i + 1, columnValues[i]);
			}
			
			preparedStarement.setInt(columnNames.length + 1, id);			
		} else {
			String columns = convertInsertStr(columnNames, false);			
			String values = convertInsertStr(columnValues, true);	
						
			query = "INSERT INTO " + tableName + " (" + columns + ")" + " values(" + values +  ")";
			
			logger.info("Preparing insert query: " + query);
			
			preparedStarement = connection.prepareStatement(query);
			
			for (int j = 0; j < columnValues.length; j++) {
				preparedStarement.setString(j + 1, columnValues[j]);
			}
		}
				
		return preparedStarement.executeUpdate();
	}
	
	public int saveNative(String nativeSqlQuery) throws SQLException {
		int result = 0;
		
		preparedStarement = connection.prepareStatement(nativeSqlQuery);
		result = preparedStarement.executeUpdate();
		return result;
	}
	
	private String convertInsertStr(String[] str, boolean questions) {
		String result = "";
		
		if (questions) {
			result = Arrays.asList(str).stream().map(s -> "?, ").collect(Collectors.joining());
		} else {
			result = Arrays.asList(str).stream().map(s -> s + ", ").collect(Collectors.joining());
		}
		result = result.substring(0, result.length() - 2);
		
		return result;
	}
	
	private String convertUpdateStr(String[] str) {
		String result = "";
		
		for (int i = 0; i < str.length; i++) {
			result += str[i] + " = " + "?, ";
		}
		
		result = result.substring(0, result.length() - 2);
		
		return result;
	}
	
	@Override
	public void closeStatement() throws SQLException {
		if (preparedStarement != null) preparedStarement.close();
		logger.info("Saver closed preparedStatement.");
	}
}
