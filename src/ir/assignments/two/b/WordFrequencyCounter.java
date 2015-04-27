/* Jonathan Budi 53397705 */

package ir.assignments.two.b;

import ir.assignments.two.a.Frequency;
import ir.assignments.two.a.Utilities;
import ir.assignments.two.helper.SortHelper;
import ir.assignments.two.helper.TestHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Counts the total number of words and their frequencies in a text file.
 */
public final class WordFrequencyCounter {
	/**
	 * This class should not be instantiated.
	 */
	private WordFrequencyCounter() {
	}

	/**
	 * Takes the input list of words and processes it, returning a list
	 * of {@link Frequency}s.
	 * 
	 * This method expects a list of lowercase alphanumeric strings.
	 * If the input list is null, an empty list is returned.
	 * 
	 * There is one frequency in the output list for every 
	 * unique word in the original list. The frequency of each word
	 * is equal to the number of times that word occurs in the original list. 
	 * 
	 * The returned list is ordered by decreasing frequency, with tied words sorted
	 * alphabetically.
	 * 
	 * The original list is not modified.
	 * 
	 * Example:
	 * 
	 * Given the input list of strings 
	 * ["this", "sentence", "repeats", "the", "word", "sentence"]
	 * 
	 * The output list of frequencies should be 
	 * ["sentence:2", "the:1", "this:1", "repeats:1",  "word:1"]
	 *  
	 * @param words A list of words.
	 * @return A list of word frequencies, ordered by decreasing frequency.
	 */
	public static List<Frequency> computeWordFrequencies(List<String> words) {
		ArrayList<Frequency> frequencies = new ArrayList<Frequency>();
		int size = 0;
		if (words != null && !words.isEmpty()) {
			// sort words in alphabetical order
			// this ensures that when iterating and we see a different token,
			// the token will never appear in the list again
			Collections.sort(words);
			
			// create frequency object and increment while token is the same
			// else add token to list, create new frequency object
			Frequency f = new Frequency(words.get(0), 0);
			for (String token : words) {
				if (f.getText().equals(token)) {
					f.incrementFrequency();
				} else {
					frequencies.add(f);
					f = new Frequency(token, 1);
				}
				++size;
				if (size % 100000 == 0)
					TestHelper.debugMessage("Tokens processed: " + size);
			}
			frequencies.add(f);
		}
		Collections.sort(frequencies, new SortHelper.FrequencyComparator());
		return frequencies;
	}

	/**
	 * Runs the word frequency counter. The input should be the path to a text file.
	 * 
	 * @param args The first element should contain the path to a text file.
	 */
	public static void main(String[] args) {
		long startTime, endTime;
		startTime = System.nanoTime();

		File file = new File(args[0]);
		List<String> words = Utilities.tokenizeFile(file);
		List<Frequency> frequencies = computeWordFrequencies(words);
		Utilities.printFrequencies(frequencies);
		endTime = System.nanoTime();

		long duration = (endTime - startTime) / 1000000;
		TestHelper.debugMessage(duration + " ms");
	}
}
