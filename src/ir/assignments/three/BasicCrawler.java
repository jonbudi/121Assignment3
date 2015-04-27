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

/**
 * @author Yasser Ganjisaffar
 */
public class BasicCrawler extends WebCrawler {

	private static final Pattern INVALIDEXTENSIONS = Pattern.compile(".*\\.(css|js|bmp|gif|jpe?g|jpg|ico"
			+ "|png|tiff?|tiff|mid|mp2|mp3|mp4|wav|avi|mov|mpeg|ram|m4v|mkv|ogg|ogv|pdf"
			+ "|ps|eps|tex|ppt|pptx|doc|docx|xls|xlsx|names|data|dat|exe|bz2|tar|msi|bin|7z"
			+ "|psd|dmg|iso|epub|dll|cnf|tgz|sha1|thmx|mso|arff|rtf|jar|csv|rm|smil|wmv|swf|wma|zip|rar|gz"
			+ "|java|py|c|h)$");

	private static final String CACHEPATH = "cache/";
	private static final String VALIDDOMAIN = "ics.uci.edu";
	private static int LINKSPROCESSED = 0;
	private static int temp = 0;
	
	private static int MAXOUTGOINGURLSPERPAGE = 1000;
	private static HashSet<String> tooManyUrlsSet = Util.loadTooManyUrls();

	private static int MAXURLSPERPATH = 10000;
	private static HashMap<String, Integer> pathLinksMap = new HashMap<String, Integer>();
	private static HashSet<String> trapsSet = Util.loadTraps();

	/**
	 * You should implement this function to specify whether the given url
	 * should be crawled or not (based on your crawling logic).
	 */
	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
		String href = url.getURL().toLowerCase();
		// Ignore the url if it has an extension that matches our defined set of image extensions.
		if (INVALIDEXTENSIONS.matcher(href).matches()) {
			return false;
		}
		if (referringPage.getParseData() instanceof HtmlParseData) {
			HtmlParseData htmlParseData = (HtmlParseData) referringPage.getParseData();
			if (htmlParseData.getOutgoingUrls().size() > MAXOUTGOINGURLSPERPAGE) {
				tooManyUrlsSet.add(href);
				try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(
						Util.TOOMANYURLSFILEPATH, true)))) {
					out.println(href);
					out.close();
				} catch (IOException e) {
				}
				return false;
			}

			String urlMinusPath = href.substring(0, href.length() - url.getPath().length());
			if (trapsSet.contains(urlMinusPath)) {
				return false;
			}

			if (pathLinksMap.containsKey(urlMinusPath)) {
				if (pathLinksMap.get(urlMinusPath) > MAXURLSPERPATH) {
					trapsSet.add(urlMinusPath);
					try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(
							Util.TRAPFILEPATH, true)))) {
						out.println(urlMinusPath);
						out.close();
					} catch (IOException e) {
					}
				}
				pathLinksMap.put(urlMinusPath, pathLinksMap.get(urlMinusPath) + 1);
			} else {
				pathLinksMap.put(urlMinusPath, 1);
			}
		}

		String domain = url.getSubDomain().replace("www.", "") + "." + url.getDomain();
		return domain.contains(VALIDDOMAIN);
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
		System.out.println("TOO MANY URLS: " + tooManyUrlsSet);

		if (page.getParseData() instanceof HtmlParseData) {
			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();

			String html = htmlParseData.getHtml();
			//String text = htmlParseData.getText();
			String path = CACHEPATH + url + ".html";
			if (!new File(path).isFile()) {
				Writer.writeTextToFile(html, path);
			}
			//Writer.writeTextToFile(text, CACHEPATH + url + ".txt");
			//Writer.writeTextToFile(outgoing.toString().replace("[", "").replace("]", ""), CACHEPATH + url + "(links).csv");

			++LINKSPROCESSED;
		}
	}
}