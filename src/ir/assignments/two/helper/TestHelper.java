/* Jonathan Budi 53397705 */

package ir.assignments.two.helper;

import java.io.File;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.util.ArrayList;

public class TestHelper {

	/**
	 * Set to true to see debug messages.
	 */
	private static final boolean DEBUG = false;

	/**
	 * Gets all test files in path.
	 * 
	 * @param path Path of test files.
	 * @return All test files in path.
	 */
	public static ArrayList<File> getTestFiles(String path) {
		File dir = new File(path);
		String[] fileNames = dir.list(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.matches(".*\\.txt$");
			}
		});
		ArrayList<File> files = new ArrayList<File>();
		for (String file : fileNames) {
			files.add(new File(path + "\\" + file));
		}
		return files;
	}

	/**
	 * Print message to System.out.
	 * 
	 * @param message Message to print.
	 */
	public static void debugMessage(String message) {
		if (DEBUG) {
			System.out.println("~~ " + message);
		}
	}

	/**
	 * If DEBUG, write message to output else to console
	 * 
	 * @param writer  writer object.
	 * @param message message to print.
	 */
	public static void output(PrintWriter writer, String message) {
		if (writer != null) {
			if (!DEBUG) {
				System.out.println(message);
			} else {
				writer.println(message);
			}
		}
	}
}
