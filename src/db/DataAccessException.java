/*
 * Author: SKO
 * Date: 2025-09-28
 * Description: Thrown when SQLException is caught. Depends on DBMessages.java
 */

package db;

public class DataAccessException extends Exception {
	private static final long serialVersionUID = 1L;

	private final int errorCode;

	public DataAccessException(int errorCode, Throwable e) {
		super(String.format("%s (%04X)", DBMessages.msg(errorCode), errorCode), e);
	    this.errorCode = errorCode;
	}

	public int getErrorCode() {
		return errorCode;
	}

	@Override
	public String toString() {
		return DBMessages.msg(errorCode);
	}
}
