package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
	private static DBConnection instance;
	private Connection connection = null;
	private static final String DBNAME = "DMA-CSD-V252_10667959";
	private static final String SERVERNAME = "hildur.ucn.dk";
	private static final String PORTNUMBER = "1433";
	private static final String USERNAME = "DMA-CSD-V252_10667959";
	private static final String PASSWORD = "Password1!";

	private DBConnection() {
		String urlString = String.format("jdbc:sqlserver://%s:%s;databaseName=%s;encrypt=false", SERVERNAME, PORTNUMBER,
				DBNAME);
		try {
			connection = DriverManager.getConnection(urlString, USERNAME, PASSWORD);
		} catch (SQLException e) {
			System.out.printf("Error: Cannot connect to %s database!\n", DBNAME);
			System.out.println(e);
			System.exit(1);
		}
	}

	public static DBConnection getInstance() {
	    if (instance == null) {
	        instance = new DBConnection();
	    }
	    return instance;
	}

	public Connection getConnection() {
		return connection;
	}

	public void startTransaction() throws SQLException {
		if (connection == null || connection.isClosed()) {
			throw new SQLException("Cannot start transaction: connection is null or closed.");
		}
		connection.setAutoCommit(false);
		System.out.println("startTransaction Connection hash: " + System.identityHashCode(connection));

		System.out.println("[DBConnection] autoCommit set to FALSE");
	}

	public void commitTransaction() throws SQLException {
		if (connection == null || connection.isClosed()) {
			throw new SQLException("Cannot commit: connection is null or closed.");
		}
		connection.commit();
		connection.setAutoCommit(true);
		System.out.println("commitTransaction Connection hash: " + System.identityHashCode(connection));

		System.out.println("[DBConnection] Transaction COMMITTED (autoCommit TRUE)");
	}

	public void rollbackTransaction() throws SQLException {
		if (connection == null || connection.isClosed()) {
			System.err.println("Cannot rollback: connection is null or already closed.");
			return;
		}
		System.out.println("rollbackTransaction Connection hash: " + System.identityHashCode(connection));

		System.out.println("[DBConnection] Rollback requested, autoCommit=" + connection.getAutoCommit());
		if (!connection.getAutoCommit()) {
			connection.rollback();
			System.out.println("Rollback successful.");
		} else {
			System.out.println("Skipped rollback — autoCommit=true (no transaction in progress).");
		}
		connection.setAutoCommit(true);
	}

	public void close() {
		try {
			DBConnection.getInstance().getConnection().close();
		} catch (SQLException e) {
			System.out.printf("Error: Cannot close connection to %s database!\n", DBNAME);
		}
	}
}
