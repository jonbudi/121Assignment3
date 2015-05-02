package ir.assignments.three;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Crawler {
	
	/** path to cache **/
	private static final String CACHEPATH = "cache/";
	
	/** question mark representation **/
	private static final String QUESTIONMARK = "!!!QUESTIONMARK!!!";
	/** colon representation **/
	private static final String COLON = "!!!COLON!!!";

	
	/**
	 * This method is for testing purposes only. It does not need to be used
	 * to answer any of the questions in the assignment. However, it must
	 * function as specified so that your crawler can be verified programatically.
	 * 
	 * This methods performs a crawl starting at the specified seed URL. Returns a
	 * collection containing all URLs visited during the crawl.
	 */
	public static Collection<String> crawl(String seedURL) {
		List<File> files = Util.getFilesInPath(CACHEPATH);
		List<String> urls = new ArrayList<String>();
		String url;
		for (File f : files) {
			url = f.getAbsolutePath().replace("\\", "/");
			url = url.substring(url.indexOf(CACHEPATH) + 6); // "cache/".length == 6
			url = url.replace(QUESTIONMARK, "?").replace(COLON, ":");
			urls.add(url);
		}
		return urls;
	}
	
	public static void main(String[] args) {
		Collection<String> urls = crawl("ics.uci.edu");
		/*
		for (String url : urls) {
			System.out.println(url);
		}*/
		System.out.println(urls.size());
	}
}
