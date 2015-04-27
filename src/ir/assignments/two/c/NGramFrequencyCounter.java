/* Jonathan Budi 53397705 */

package ir.assignments.two.c;

import ir.assignments.two.a.Frequency;
import ir.assignments.two.b.WordFrequencyCounter;
import ir.assignments.two.helper.FrequencyHelper;
import ir.assignments.two.helper.SortHelper;
import ir.assignments.two.helper.TestHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Count the total number of n-grams and their frequencies in a text file.
 */
public class NGramFrequencyCounter {
	/**
	 * This class should not be instantiated.
	 */
	private NGramFrequencyCounter() {
	}

	/**
	 * Takes the input list of words and processes it, returning a list
	 * of {@link Frequency}s.
	 * 
	 * @param words A list of words.
	 * @param gram  N gram.
	 * @return A list of two gram frequencies, ordered by decreasing frequency.
	 */
	public static List<Frequency> computeNGramFrequencies(
			ArrayList<String> words, int gram) {
		List<Frequency> frequencies = new ArrayList<Frequency>();
		ArrayList<String> concatWords = new ArrayList<String>();
		if (words != null || gram <= 0) {
			int size = 0;
			LinkedList<String> tokens = new LinkedList<String>();

			// add tokens to queue, if queue.size() == gram,
			// then concat tokens into one string, add string to frequencies list,
			// then pop (first) token off tokens queue. 
			for (String word: words) {
				tokens.add(word);
				if (tokens.size() == gram) {
					// in terms of time, this is the most efficient algorithm
					concatWords.add(FrequencyHelper.convertListOfTokensToString(tokens));
					// in terms of space, this is the most efficient algorithm
					//FrequencyHelper.addTokensToList(frequencies, tokens);
					tokens.pop();
				}
				++size;
				if (size % 100000 == 0) TestHelper.debugMessage("NGram processed: " + size);
			}
			
			if (frequencies.isEmpty()) {
				frequencies = WordFrequencyCounter.computeWordFrequencies(concatWords);
			}
		}
		Collections.sort(frequencies, new SortHelper.FrequencyComparator());
		return frequencies;
	}
	
	public static List<String> computeNGramWords(ArrayList<String> words, int gram) {
		ArrayList<String> result = new ArrayList<String>();
		if (words != null || gram <= 0) {
			LinkedList<String> tokens = new LinkedList<String>();
			for (String word: words) {
				tokens.add(word);
				if (tokens.size() == gram) {
					result.add(FrequencyHelper.convertListOfTokensToString(tokens));
					tokens.pop();
				}
			}
		}
		return result;
	}
}
