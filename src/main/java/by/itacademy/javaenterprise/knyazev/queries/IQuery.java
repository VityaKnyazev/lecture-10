package by.itacademy.javaenterprise.knyazev.queries;

import java.sql.SQLException;

public interface IQuery {
	
	void closeStatement() throws SQLException;
	
}
