package ir.assignments.three;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Util {
	
	/** path to list of traps **/
	public static String TRAPFILEPATH = "Traps.txt";
	
	/** question mark representation **/
	private static final String QUESTIONMARK = "!!!QUESTIONMARK!!!";
	/** colon representation **/
	private static final String COLON = "!!!COLON!!!";

	/** get list of all files in path **/
	public static List<File> getFilesInPath(String path) {
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
	
	/** fix urls to allow invalid file chars **/
	public static String urlToPath(String url) {
		return url.replace("?", QUESTIONMARK).replace(":", COLON);
	}
	
	/** get url minus path **/
	public static String getUrlMinusPath(String href) {
		href = href.replaceFirst("^(http://www\\.|http://|https://www\\.|https://|www\\.)", "").toLowerCase();
		String urlMinusPath = "";
		String[] split = href.split("[/]");
		if (split.length == 1) {
			urlMinusPath = href;
		} else {
			for (int i = 0; i < split.length - 1; ++i) {
				urlMinusPath += (split[i] + "/");
			}
		}
		return urlMinusPath.toLowerCase();
	}

	/** load traps from path **/
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
