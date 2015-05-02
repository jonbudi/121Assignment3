package ir.assignments.three;

import ir.assignments.two.a.Frequency;
import ir.assignments.two.a.Utilities;
import ir.assignments.two.helper.SortHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Calculator {

	/** path to cache of all crawled html pages **/
	private static final String CACHEPATH = "cache/";

	/** path to list of stopwords **/
	private static final String STOPWORDSPATH = "Stopwords.txt";

	/** question mark representation **/
	private static final String QUESTIONMARK = "!!!QUESTIONMARK!!!";
	/** colon representation **/
	private static final String COLON = "!!!COLON!!!";

	/** html file with most words **/
	@SuppressWarnings("unused")
	private static String mostWordsUrl;
	/** number of words in most words html file **/
	private static int mostWords = 0;

	/** calculate frequencies given list of files **/
	@SuppressWarnings("unused")
	private static List<Frequency> calculateFrequencies(List<File> files) {
		List<String> words;
		Map<String, Integer> map = new HashMap<String, Integer>();
		String currentWord;
		int count;

		// init stop words
		Utilities.initStopWords(STOPWORDSPATH);

		int wordCount = 0;

		for (File file : files) {
			wordCount = 0;
			words = Utilities.tokenizeHTMLFile(file);
			if (words.size() > 0) {
				Collections.sort(words);
				currentWord = words.get(0);
				count = 1;
				for (int index = 1; index < words.size(); ++index) {
					if (currentWord.equals(words.get(index))) {
						++wordCount;
						++count;
					} else {
						if (map.containsKey(currentWord)) {
							map.put(currentWord, map.get(currentWord) + count);
						} else {
							map.put(currentWord, count);
						}
						currentWord = words.get(index);
						count = 1;
					}
				}
			}

			// do this here because for efficiency... yeah it's messy but oh well
			if (wordCount > mostWords && file.getAbsolutePath().contains(".html")) {
				mostWords = wordCount;
				mostWordsUrl = file.getAbsolutePath();
			}
		}

		List<Frequency> frequencies = new ArrayList<Frequency>();
		for (String key : map.keySet()) {
			frequencies.add(new Frequency(key, map.get(key)));
		}
		Collections.sort(frequencies, new SortHelper.FrequencyComparator());
		return frequencies;
	}

	@SuppressWarnings("unused")
	private static void printSubdomainFrequency(List<File> files) {
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		String subdomain;
		for (File f : files) {
			subdomain = f.getAbsolutePath().replace("\\", "/");
			subdomain = subdomain.substring(subdomain.indexOf(CACHEPATH)).split("/")[2];
			subdomain = subdomain.replace(QUESTIONMARK, "?").replace(COLON, ":").toLowerCase();
			if (map.containsKey(subdomain)) {
				map.put(subdomain, map.get(subdomain) + 1);
			} else {
				map.put(subdomain, 1);
			}
		}
		ArrayList<String> list = new ArrayList<String>(map.keySet());
		Collections.sort(list);
		System.out.println(list.size() + " subdomains found!");
		for (String key : list) {
			System.out.println(key + ", " + map.get(key));
		}
	}

	public static void main(String[] args) {

		System.err.println("See main function :)");
		
		//List<File> files = Util.getFilesInPath(CACHEPATH);
		//List<Frequency> frequencies = calculateFrequencies(files);
		//Utilities.printFrequencies(frequencies, 500);

		//printSubdomainFrequency(files);

		//System.out.println(mostWords);
		//System.out.println(mostWordsUrl);
	}
}
