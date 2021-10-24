package by.itacademy.javaenterprise.knyazev.db;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DbConnection {
	private final static String RESOURCE = "database";
	
	private String url;
	private String driver;
	private String user;
	private String password;
	
	private final Logger logger;
	
	private static DbConnection dbConnection;
	
	
	private DbConnection() {
		logger = LoggerFactory.getLogger(DbConnection.class);
		
		
		try {
			setResources();
			Class.forName(driver);
			logger.info("Dtiver has been set successfully.");
		} catch (ReflectiveOperationException e) {
			logger.error(e.getMessage());
		}
	}
	
	public static DbConnection getDBO() throws ReflectiveOperationException {
		if (dbConnection == null) {
			synchronized (DbConnection.class) {
				if (dbConnection == null) {
					dbConnection = new DbConnection();
				}
			}			
		}
		
		return dbConnection;
	}
	
	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection(url, user, password);	 	
	}
	
	private void setResources() throws ReflectiveOperationException {
		ResourceBundle resource = ResourceBundle.getBundle(RESOURCE);		
		Enumeration<String> keys = resource.getKeys();
		
		keys.asIterator().forEachRemaining(k -> {					
			try {
				Field field = this.getClass().getDeclaredField(k);
				field.setAccessible(true);
				try {
					field.set(this, resource.getString(k));
					logger.info("String field with name " + k + " has been extracting from property file successfully.");
				} catch (IllegalArgumentException | IllegalAccessException e) {
					logger.error(e.getMessage());
				}			
			} catch (NoSuchFieldException | SecurityException e) {
				logger.error(e.getMessage());
			}
		});
		
		
		if ((url == null || url.isEmpty()) || (driver == null || driver.isEmpty()) 
				|| (user == null || user.isEmpty()) || (password == null || password.isEmpty()))	{
			throw new ReflectiveOperationException("Not all fields has been successfully initialized from property file");
		} else {		
			logger.info("Database connection properties has been set successfully!");	
		}
	}
	

}
