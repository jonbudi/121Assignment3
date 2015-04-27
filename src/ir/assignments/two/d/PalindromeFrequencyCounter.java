/* Jonathan Budi 53397705 */

package ir.assignments.two.d;

import ir.assignments.two.a.Frequency;
import ir.assignments.two.a.Utilities;
import ir.assignments.two.b.WordFrequencyCounter;
import ir.assignments.two.c.NGramFrequencyCounter;
import ir.assignments.two.helper.PalindromeHelper;
import ir.assignments.two.helper.SortHelper;
import ir.assignments.two.helper.TestHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PalindromeFrequencyCounter {
	/**
	 * This class should not be instantiated.
	 */
	private PalindromeFrequencyCounter() {
	}

	/**
	 * Takes the input list of words and processes it, returning a list
	 * of {@link Frequency}s.
	 * 
	 * This method expects a list of lowercase alphanumeric strings.
	 * If the input list is null, an empty list is returned.
	 * 
	 * There is one frequency in the output list for every 
	 * unique palindrome found in the original list. The frequency of each palindrome
	 * is equal to the number of times that palindrome occurs in the original list.
	 * 
	 * Palindromes can span sequential words in the input list.
	 * 
	 * The returned list is ordered by decreasing size, with tied palindromes sorted
	 * by frequency and further tied palindromes sorted alphabetically. 
	 * 
	 * The original list is not modified.
	 * 
	 * Example:
	 * 
	 * Given the input list of strings 
	 * ["do", "geese", "see", "god", "abba", "bat", "tab"]
	 * 
	 * The output list of palindromes should be 
	 * ["do geese see god:1", "bat tab:1", "abba:1"]
	 *  
	 * @param words A list of words.
	 * @return A list of palindrome frequencies, ordered by decreasing frequency.
	 */
	private static List<Frequency> computePalindromeFrequencies(
			ArrayList<String> words) {
		List<Frequency> frequencies = new ArrayList<Frequency>();
		if (words != null) {
			long startTime, endTime;
			startTime = System.nanoTime();
			
			//concatenating the fullString
			StringBuilder fullString = new StringBuilder();
			for (int i = 0; i < words.size()-1; ++i) {
				fullString.append(words.get(i) + " ");
			}
			fullString.append(words.get(words.size()-1));
			
			endTime = System.nanoTime();
			TestHelper.debugMessage("Finished contructing full string (" + ((endTime-startTime)/1000000) + "ms)");
			
			startTime = System.nanoTime();
			
			// get the palindrome with the most words
			// this is used to determine the max n amount of "grams" to calculate within the NGramFrequency.
			// any number beyond the max will ALWAYS yield ZERO palindromes.  
			int maxWords = PalindromeHelper
					.numberOfWordsInLongestPalindrome(fullString.toString());
			
			endTime = System.nanoTime();
			TestHelper.debugMessage("Finished calculating max words = " + maxWords + " (" + ((endTime-startTime)/1000000) + "ms)");
			
			ArrayList<String> palindromes = new ArrayList<String>();
			
			for (int i = maxWords; i >= 0; --i) {
				startTime = System.nanoTime();
				for (String s: NGramFrequencyCounter.computeNGramWords(words, i)) {
					if (PalindromeHelper.isPalindrome(s.replaceAll("[ ]", ""))) {
						// best for time efficiency
						palindromes.add(s);
						// again, best for space efficiency 
						//FrequencyHelper.addTokenToList(frequencies, s);
					}
				}
				endTime = System.nanoTime();
				TestHelper.debugMessage("Finished " + i + "-gram (" + ((endTime-startTime)/1000000) + "ms)");
			}
			
			if (frequencies.isEmpty()) {
				frequencies = WordFrequencyCounter.computeWordFrequencies(palindromes);
			}
		}
		Collections.sort(frequencies, new SortHelper.FrequencyComparator2());
		return frequencies;
	}

	/**
	 * Runs the palindrome finder. The input should be the path to a text file.
	 * 
	 * @param args The first element should contain the path to a text file.
	 */
	public static void main(String[] args) {
		long startTime, endTime;
		startTime = System.nanoTime();
		
		File file = new File(args[0]);
		//File file = new File("tests/9000000.txt");
		ArrayList<String> words = Utilities.tokenizeFile(file);
		List<Frequency> frequencies = computePalindromeFrequencies(words);
		Utilities.printFrequencies(frequencies);
		endTime = System.nanoTime();

		long duration = (endTime - startTime) / 1000000;
		TestHelper.debugMessage(duration + " ms");
	}
}
