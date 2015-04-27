/* Jonathan Budi 53397705 */

package ir.assignments.two.helper;

import java.util.List;

import ir.assignments.two.a.Frequency;

public class FrequencyHelper {

	private FrequencyHelper() {
	}

	/**
	 * Adds token to list of frequencies.
	 * 
	 * @param frequencies List of frequencies.
	 * @param token       Token to add to list.
	 */
	public static void addTokenToList(List<Frequency> frequencies, String token) {
		Frequency freq = FrequencyHelper.wordInFrequencies(frequencies, token);
		if (freq != null) {
			freq.incrementFrequency();
		} else {
			frequencies.add(new Frequency(token, 1));
		}
	}

	/**
	 * Adds list of token (as one concatenated token separated by spaces) to list of frequencies.
	 *  
	 * @param frequencies List of frequencies.
	 * @param tokens      Tokens to add.
	 */
	public static void addTokensToList(List<Frequency> frequencies,
			List<String> tokens) {
		addTokenToList(frequencies, convertListOfTokensToString(tokens));
	}

	
	/**
	 * Concatenates all tokens separated by spaces to string.
	 *  
	 * @param tokens List of tokens.
	 */
	public static String convertListOfTokensToString(List<String> tokens) {
		if (tokens == null || tokens.size() == 0) {
			return "";
		}
		StringBuilder s = new StringBuilder();
		for (String token : tokens) {
			s.append(token + " ");
		}
		return s.toString().substring(0, s.length() - 1);
	}

	/** 
	 * Find Frequency object with token.
	 * 
	 * @param frequencies List of frequencies.
	 * @param token       Token to search for.
	 * @return Frequency object with corresponding token.
	 */
	public static Frequency wordInFrequencies(List<Frequency> frequencies,
			String token) {
		for (Frequency f : frequencies) {
			if (token.equals(f.getText())) {
				return f;
			}
		}
		return null;
	}

	/**
	 * Calculate the length of the longest word (or words) in list.
	 * 
	 * (Used only when formatting output.)
	 * 
	 * @param frequencies A list of frequencies.
	 * @return length of longest word.
	 **/
	public static int getLongestWordLength(List<Frequency> frequencies) {
		int result = -1;
		int length;
		for (Frequency f : frequencies) {
			length = f.getText().length();
			if (length > result) {
				result = length;
			}
		}
		return result;
	}

	/**
	 * Return n number of spaces
	 * 
	 * (Used only when formatting output.)
	 * 
	 * @param n Number of spaces
	 * @return n spaces
	 */
	public static String spaces(int n) {
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < n; ++i) {
			s.append(" ");
		}
		return s.toString();
	}
}
