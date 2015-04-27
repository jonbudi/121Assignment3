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

public class CalculateFrequency {
	private static final String CACHEPATH = "cache/";
	private static final String STOPWORDSPATH = "Stopwords.txt";
	
	private static List<File> getFilesInPath(String path) {
		List<File> files = new ArrayList<File>();
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		
		for (int i = 0; i < listOfFiles.length; ++i) {
			if (listOfFiles[i].isFile()) {
				files.add(listOfFiles[i]);
			} else if (listOfFiles[i].isDirectory()) {
				files.addAll(getFilesInPath(listOfFiles[i].getAbsolutePath()));
			}
		}
		
		return files;
	}
	
	private static List<Frequency> calculateFrequencies(List<File> files) {
		List<String> words;
		Map<String, Integer> map = new HashMap<String, Integer>();
		String currentWord;
		int count;
		
		Utilities.initStopWords(STOPWORDSPATH);
		
		for (File file : files) {
			words = Utilities.tokenizeHTMLFile(file);
			if (words.size() > 0) {
				Collections.sort(words);
				currentWord = words.get(0);
				count = 1;
				for (int index = 1; index < words.size(); ++index) {
					if (currentWord.equals(words.get(index))) {
						++count;
					} else {
						map.put(currentWord, count);
						currentWord = words.get(index);
						count = 1;
					}
				}
				map.put(currentWord, count);
			}
		}
		
		List<Frequency> frequencies = new ArrayList<Frequency>();
		for (String key: map.keySet()) {
			frequencies.add(new Frequency(key, map.get(key)));
		}
		Collections.sort(frequencies, new SortHelper.FrequencyComparator());
		return frequencies;
	}

	public static void main(String[] args) {
		List<Frequency> frequencies = calculateFrequencies(getFilesInPath(CACHEPATH));
		Utilities.printFrequencies(frequencies, 10);
	}
}
