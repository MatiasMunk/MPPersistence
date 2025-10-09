/*
 * Author: SKO
 * Date: 2025-09-28
 * Description: Hardcoded database msg lookup with hex keys. Used by DataAccessException.
 */

package db;

import java.util.HashMap;
import java.util.Map;

public class DBMessages {
	private static final Map<Integer, String> lookup = new HashMap<>();

	static {
		lookup.put(0x1001, "Database error: Cannot start transaction");
		lookup.put(0x1002, "Database error: Cannot end transaction");
		lookup.put(0x1003, "Database error: Cannot rollback transaction");
		lookup.put(0x1004, "Database error: Cannot create order");
		lookup.put(0x1005, "Database error: Cannot find product");
		lookup.put(0x1006, "Database error: Cannot reserve product");
		lookup.put(0x1007, "Database error: Cannot update product");
		lookup.put(0x1008, "Database error: Cannot add product");
		lookup.put(0x1009, "Database error: Cannot build product");
		lookup.put(0x1010, "Database error: Cannot add customer (phoneNo) to saleOrder");
	}

	public static String msg(int errorCode) {
		return lookup.get(errorCode);
	}
}
