package ir.assignments.three;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class Writer {
	
	private static void createDirectory(String path) {
		if (!(new File(path).exists())) {
			new File(path).mkdir();
		}
	}

	public static void writeTextToFile(String text, String path) {
		StringBuilder s = new StringBuilder();
		path = Util.urlToPath(path);
		String[] list = path.split("[/]");
		for (int i = 0; i < list.length - 1; ++i) {
			s.append(list[i] + "/");
			createDirectory(s.toString());
		}

		PrintWriter out = null;
		try {
			if (new File(path).isFile()) {
				return;
			} else if (new File(path).isDirectory()) {
				path += "/index";
			}
			out = new PrintWriter(path, "UTF-8");
			for (String line: text.split("\n")) {
				line = line.trim();
				if (!line.equals("")) {
					out.println(line);
				}
			}
			out.close();
			out = null;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}
}
