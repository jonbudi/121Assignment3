/* Jonathan Budi 53397705 */

package ir.assignments.two.helper;

public class StringHelper {
	
	/**
	 * Count the number of spaces in a string.
	 * 
	 * @param s String to analyze.
	 * @return number of spaces in s.
	 */
	public static int countSpaces(String s) {
		int count = 0;
		for (char c : s.toCharArray()) {
			if (c == ' ') {
				++count;
			}
		}
		return count;
	}
}
