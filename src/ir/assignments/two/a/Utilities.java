/* Jonathan Budi 53397705 */

package ir.assignments.two.a;

import ir.assignments.two.helper.FrequencyHelper;
import ir.assignments.two.helper.StringHelper;
import ir.assignments.two.helper.TestHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * A collection of utility methods for text processing.
 */
public class Utilities {

	/**
	 * Maximum amount of tokens to be processed
	 */
	private static final int MAXTOKENS = 5000000;

	private static HashSet<String> stopwords = new HashSet<String>();

	/**
	 * Reads the input text file and splits it into alphanumeric tokens.
	 * Returns an ArrayList of these tokens, ordered according to their
	 * occurrence in the original text file.
	 * 
	 * Non-alphanumeric characters delineate tokens, and are discarded.
	 *
	 * Words are also normalized to lower case. 
	 * 
	 * Example:
	 * 
	 * Given this input string
	 * "An input string, this is! (or is it?)"
	 * 
	 * The output list of strings should be
	 * ["an", "input", "string", "this", "is", "or", "is", "it"]
	 * 
	 * @param input The file to read in and tokenize.
	 * @return The list of tokens (words) from the input file, ordered by occurrence.
	 */
	public static ArrayList<String> tokenizeFile(File input) {
		ArrayList<String> result = new ArrayList<String>(MAXTOKENS);
		FileReader reader = null;
		int c;
		StringBuilder token = new StringBuilder();
		String tokenString;
		int size = 0;
		try {
			reader = new FileReader(input);
			while (true) {
				c = reader.read();
				if (c == -1 || size >= MAXTOKENS) {
					if (token.length() != 0) {
						tokenString = token.toString().toLowerCase();
						if (!stopwords.contains(tokenString)) {
							result.add(tokenString);
						}
					}
					break; // EOF
				}
				if (isValidChar(c)) {
					// append c to end of accumulated token
					token.append((char) c);
				} else {
					if (token.length() != 0) {
						// add accumulated token to list
						tokenString = token.toString().toLowerCase();
						if (!stopwords.contains(tokenString)) {
							result.add(tokenString);
						}
						// clear token object
						token.delete(0, token.length());
						++size;
						if (size % 100000 == 0) {
							TestHelper.debugMessage("Size of Tokens List: " + size);
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	public static ArrayList<String> tokenizeHTMLFile(File input) {
		ArrayList<String> result = new ArrayList<String>(MAXTOKENS);
		StringBuilder token = new StringBuilder();
		String tokenString;
		int size = 0;
		Document doc = null;

		try {
			doc = Jsoup.parse(input, "UTF-8", "http://example.com/");
			for (char c : doc.body().text().toCharArray()) {
				if (c == -1 || size >= MAXTOKENS) {
					if (token.length() != 0) {
						tokenString = token.toString().toLowerCase();
						if (!stopwords.contains(tokenString)) {
							result.add(tokenString);
						}
					}
					break; // EOF
				}
				if (isValidChar(c)) {
					// append c to end of accumulated token
					token.append(c);
				} else {
					if (token.length() != 0) {
						// add accumulated token to list
						tokenString = token.toString().toLowerCase();
						if (!stopwords.contains(tokenString)) {
							result.add(tokenString);
						}
						// clear token object
						token.delete(0, token.length());
						++size;
						if (size % 100000 == 0) {
							TestHelper.debugMessage("Size of Tokens List: " + size);
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static void initStopWords(String path) {
		if (!stopwords.isEmpty()) {
			return;
		}
		BufferedReader br = null;

		try {
			String currentLine;
			br = new BufferedReader(new FileReader(path));
			while ((currentLine = br.readLine()) != null) {
				stopwords.add(currentLine.trim());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Determines if char c is valid char in token.
	 * 
	 * @param c Char to examine.
	 * @return Validity of c.
	 */
	private static boolean isValidChar(int c) {
		return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9');
	}

	/**
	 * Takes a list of {@link Frequency}s and prints it to standard out. It also
	 * prints out the total number of items, and the total number of unique items.
	 * 
	 * Example one:
	 * 
	 * Given the input list of word frequencies
	 * ["sentence:2", "the:1", "this:1", "repeats:1",  "word:1"]
	 * 
	 * The following should be printed to standard out
	 * 
	 * Total item count: 6
	 * Unique item count: 5
	 * 
	 * sentence	2
	 * the		1
	 * this		1
	 * repeats	1
	 * word		1
	 * 
	 * 
	 * Example two:
	 * 
	 * Given the input list of 2-gram frequencies
	 * ["you think:2", "how you:1", "know how:1", "think you:1", "you know:1"]
	 * 
	 * The following should be printed to standard out
	 * 
	 * Total 2-gram count: 6
	 * Unique 2-gram count: 5
	 * 
	 * you think	2
	 * how you		1
	 * know how		1
	 * think you	1
	 * you know		1
	 * 
	 * @param frequencies A list of frequencies.
	 */
	public static void printFrequencies(List<Frequency> frequencies) {
		printFrequencies(frequencies, frequencies.size());
	}

	public static void printFrequencies(List<Frequency> frequencies, int lines) {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter("output.txt", "UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		if (frequencies == null || frequencies.isEmpty()) {
			TestHelper.output(writer, "Total item count: 0");
			TestHelper.output(writer, "Unique item count: 0");
			if (writer != null) {
				writer.close();
			}
			return;
		}

		int total = 0;
		int unique = frequencies.size();
		int gramCount = StringHelper.countSpaces(frequencies.get(0).getText()) + 1;

		int longestWordLength = FrequencyHelper.getLongestWordLength(frequencies);
		for (Frequency f : frequencies) {
			total += f.getFrequency();
			// if gramCount is not equal throughout all f, set gramCount to 1
			if (gramCount != StringHelper.countSpaces(f.getText()) + 1) {
				gramCount = 1;
			}
		}

		TestHelper.output(writer, "Total " + ((gramCount == 1) ? "item" : gramCount + "-gram")
				+ " count: " + total);

		TestHelper.output(writer, "Unique " + ((gramCount == 1) ? "item" : gramCount + "-gram")
				+ " count: " + unique);

		Frequency f;
		for (int i = 0; i < lines; ++i) {
			f = frequencies.get(i);
			TestHelper.output(writer, f.getText() + "\t" + f.getFrequency());
		}

		if (writer != null) {
			writer.close();
		}
	}
}
