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
		lookup.put(0x1011, "Database error: Cannot create group");
		lookup.put(0x1021, "Database error: Cannot find specific group");
		lookup.put(0x1022, "Database error: Cannot find all groups");
		lookup.put(0x1031, "Database error: Cannot update group");
		lookup.put(0x1041, "Database error: Cannot delete group");
		lookup.put(0x10F1, "Database error: Cannot build group");
		lookup.put(0x10F2, "Database error: Cannot build groups");

		lookup.put(0x2011, "Database error: Cannot create student");
		lookup.put(0x2021, "Database error: Cannot find specific student");
		lookup.put(0x2022, "Database error: Cannot find all students");
		lookup.put(0x2031, "Database error: Cannot update student");
		lookup.put(0x2032, "Database error: Cannot update student name");
		lookup.put(0x2041, "Database error: Cannot delete student");
		lookup.put(0x2051, "Database error: Cannot remove student from group");
		lookup.put(0x20F1, "Database error: Cannot build student");
		lookup.put(0x20F2, "Database error: Cannot build students");

		lookup.put(0x3001, "Cannot find group in attempt to assign student to group");
		lookup.put(0x3002, "Cannot find student in attempt to assign student to group");
		lookup.put(0x3003, "Database error: Cannot update student's group");
		lookup.put(0x3101, "Database error: Error: Group does not exist");
		lookup.put(0x3102, "Database error: Student does not exist");

		lookup.put(0xF001, "Serious database error: Transaction not completed");
		lookup.put(0xF002, "Serious database error: Transaction cannot be rolled back");
	}

	public static String msg(int errorCode) {
		return lookup.get(errorCode);
	}
}
