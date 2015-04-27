/* Jonathan Budi 53397705 */

package ir.assignments.two.helper;

import ir.assignments.two.a.Frequency;

public class PalindromeHelper {

	/**
	 * Get the longest palindrome in terms of most words.
	 * 
	 * This is used to determine the max n amount of "grams" to calculate within the NGramFrequency.
	 * Any number beyond the max will ALWAYS yield ZERO palindromes.  
	 * 
	 * @param s String to examine.
	 * @return Number of words in longest palindrome.
	 */
	public static int numberOfWordsInLongestPalindrome(String s) {
		if (s.length() < 1) {
			return s.length();
		}

		int numberOfWords = 0;

		int temp;
		for (int i = 0; i < s.length(); ++i) {
			if (s.charAt(i) != ' ') {
				temp = StringHelper.countSpaces(longestPalindrome(s, i)) + 1;
				if (temp > numberOfWords) {
					numberOfWords = temp;
				}
			}
		}

		return numberOfWords;
	}

	/** 
	 * Find longest palindrome within a given string beginning at the given index.
	 * 
	 * @param s     String to examine.
	 * @param begin Index of start of potential palindrome.
	 * @param end   Index of end of potential palindrome.
	 * @return Longest possible palindrome at index.
	 **/
	private static String longestPalindrome(String s, int index) {
		int begin = index, end = index;
		boolean beginSpace = false, endSpace = false;
		while (begin >= 0 && end <= s.length() - 1
				&& s.charAt(begin) == s.charAt(end)) {
			--begin;
			++end;
			beginSpace = false;
			endSpace = false;

			// ignore spaces, should only ignore one char at most assuming properly formatted string
			// used while loop for clarity
			while (begin >= 0 && s.charAt(begin) == ' ') {
				--begin;
				beginSpace = true;
			}
			while (end <= s.length() - 1 && s.charAt(end) == ' ') {
				++end;
				endSpace = true;
			}
		}

		String result;

		// long and complicated!

		// when the result contains a space in the beginning and the end
		if (beginSpace && endSpace) {
			result = s.substring(begin + 2, end - 1);
		}
		// when the result contains only a space in the beginning
		else if (beginSpace) {
			result = s.substring(begin + 2, end);
		}
		// when the result contains only a space in the end
		else if (endSpace) {
			result = s.substring(begin + 1, end - 1);
		}
		// when the result doesn't begin or end in a space
		else {
			result = s.substring(begin + 1, end);
		}

		// if beginning of s is an imcomplete word
		if (s.charAt(begin + 1) != ' ') {
			if (result.contains(" ")) {
				result = result.substring(result.indexOf(' '),
						result.length());
			} else {
				result = "";
			}
		}
		
		// if end of s is an imcomplete word
		if (s.charAt(end - 1) != ' ') {
			if (result.contains(" ")) {
				result = result.substring(result.indexOf(' '));
			} else {
				result = "";
			}
		}
		
		return result;
	}

	/**
	 * Determine if Frequency f is a palindrome.
	 * 
	 * @param f Frequency to examine.
	 * @return f is a palindrome.
	 */
	public static boolean isPalindrome(Frequency f) {
		return isPalindrome(f.getText().replaceAll("[ ]", ""));
	}

	/**
	 * Determine if String s is a palindrome.
	 * 
	 * @param s String to examine.
	 * @return s is a palindrome.
	 */
	public static boolean isPalindrome(String s) {
		return isPalindrome(s.toCharArray());
	}

	/**
	 * Determine if char[] c is a palindrome.
	 * Helper function, used to optimize isPalindrome.
	 * 
	 * @param c char[] to examine.
	 * @return c is a palindrome.
	 */
	private static boolean isPalindrome(char[] c) {
		// assume that a palindrome must be > 2 char
		if (c == null || c.length <= 2) {
			return false;
		}
		for (int i = 0; i < c.length / 2; ++i) {
			if (c[i] != c[c.length - i - 1]) {
				return false;
			}
		}
		return true;
	}
}
