package ir.assignments.three;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

public class Util {
	private static final String QUESTIONMARK = "!!!QUESTIONMARK!!!";
	private static final String COLON = "!!!COLON!!!";

	public static String TRAPFILEPATH = "Traps.txt";

	public static String urlToPath(String url) {
		return url.replace("?", QUESTIONMARK).replace(":", COLON);
	}

	public static HashSet<String> loadTraps() {
		HashSet<String> set = new HashSet<String>();
		BufferedReader br = null;

		try {
			String line;
			br = new BufferedReader(new FileReader(TRAPFILEPATH));
			while ((line = br.readLine()) != null) {
				set.add(line.trim());
			}
		} catch (IOException e) {
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (IOException e) {
			}
		}

		return set;
	}
}
