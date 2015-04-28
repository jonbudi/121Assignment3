/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ir.assignments.three;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Pattern;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class BasicCrawler extends WebCrawler {

	/** all invalid file extensions will be ignored by the crawler **/
	private static final Pattern INVALIDEXTENSIONS = Pattern.compile(".*\\.(css|js|bmp|gif|jpe?g|jpg|ico"
			+ "|png|tiff?|tiff|mid|mp2|mp3|mp4|wav|avi|mov|mpeg|ram|m4v|mkv|ogg|ogv|pdf"
			+ "|ps|eps|tex|ppt|pptx|doc|docx|xls|xlsx|names|data|dat|exe|bz2|tar|msi|bin|7z"
			+ "|psd|dmg|iso|epub|dll|cnf|tgz|sha1|thmx|mso|arff|rtf|jar|csv|rm|smil|wmv|swf|wma|zip|rar|gz"
			+ "|java|py|c|h)$");

	/** path to cache **/
	private static final String CACHEPATH = "cache/";

	/** accept urls only from this domain **/
	private static final String VALIDDOMAIN = "ics.uci.edu";

	/** total number of links processed **/
	private static int LINKSPROCESSED = 0;

	/** allow max number of outgoing urls per page **/
	private static int MAXOUTGOINGURLSPERPAGE = 5000;

	/** allow max number of outgoing nodes from specified path **/
	private static int MAXURLSPERPATH = 1000;

	/** map of number of urls found in each specific path **/
	private static HashMap<String, Integer> pathLinksMap = new HashMap<String, Integer>();

	/** set of all urls that are considered traps **/
	private static HashSet<String> trapsSet = Util.loadTraps();

	/**
	 * You should implement this function to specify whether the given url
	 * should be crawled or not (based on your crawling logic).
	 */
	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
		String href = url.getURL();
		href = href.replaceFirst("^(http://www\\.|http://|https://www\\.|https://|www\\.)", "").toLowerCase();

		String domain = url.getSubDomain().replace("www.", "") + "." + url.getDomain();
		// if the domain is not valid
		if (!domain.contains(VALIDDOMAIN)) {
			return false;
		}
		
		// Ignore the url if it has an extension that matches our defined set of image extensions.
		if (INVALIDEXTENSIONS.matcher(href).matches()) {
			return false;
		}
		
		if (referringPage.getParseData() instanceof HtmlParseData) {
			HtmlParseData htmlParseData = (HtmlParseData) referringPage.getParseData();

			// if there are too many outgoing urls in a single page
			if (htmlParseData.getOutgoingUrls().size() > MAXOUTGOINGURLSPERPAGE) {
				if (trapsSet.add(href)) {
					try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(
							Util.TRAPFILEPATH, true)))) {
						out.println(href);
						out.close();
					} catch (IOException e) {
					}
				}
				return false;
			}

			String urlMinusPath = Util.getUrlMinusPath(href);

			// if url is considered a trap
			if (trapsSet.contains(urlMinusPath)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * This function is called when a page is fetched and ready to be processed
	 * by your program.
	 */
	@Override
	public void visit(Page page) {
		String url = page.getWebURL().getURL();
		System.out.println("[" + LINKSPROCESSED + "]" + url);
		System.out.println("TRAPS: " + trapsSet);
		/*
		for (String key : pathLinksMap.keySet()) {
			System.out.println(key + " " + pathLinksMap.get(key));
		}
		*/
		if (page.getParseData() instanceof HtmlParseData) {
			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();

			String html = htmlParseData.getHtml();
			String path = CACHEPATH + url + ".html";

			// write to cache if file does not exist
			if (!new File(path).isFile()) {
				Writer.writeTextToFile(html, path);
			}
			
			String href = page.getWebURL().getURL();
			String urlMinusPath = Util.getUrlMinusPath(href);
			
			// if url has too many outgoing nodes, consider it a trap
			if (pathLinksMap.containsKey(urlMinusPath)) {
				if (pathLinksMap.get(urlMinusPath) > MAXURLSPERPATH) {
					if (trapsSet.add(urlMinusPath)) {
						try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(
								Util.TRAPFILEPATH, true)))) {
							out.println(urlMinusPath);
							out.close();
						} catch (IOException e) {
						}
					}
				}
				pathLinksMap.put(urlMinusPath, pathLinksMap.get(urlMinusPath) + 1);
			} else {
				pathLinksMap.put(urlMinusPath, 1);
			}

			++LINKSPROCESSED;
		}
	}
}