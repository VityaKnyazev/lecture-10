package by.itacademy.javaenterprise.knyazev.queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Selecter implements IQuery {
	private Connection connection;
	private PreparedStatement preparedStarement;
	
	private final Logger logger;

	public Selecter(Connection connection) {
		this.connection = connection;
		logger = LoggerFactory.getLogger(getClass());
		logger.info("Selecter receive the connection.");
	}
	
	public ResultSet selectNative(String nativeSqlQuery) throws SQLException {
			
		preparedStarement = connection.prepareStatement(nativeSqlQuery);
		ResultSet resultSet = preparedStarement.executeQuery();
		return resultSet;
		
	}

	@Override
	public void closeStatement() throws SQLException {
		if (preparedStarement != null) preparedStarement.close();
		logger.info("Selecter closed preparedStatement. All selected data removed and not available from ResultSet.");
	}

}
