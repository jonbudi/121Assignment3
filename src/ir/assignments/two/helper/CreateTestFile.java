/* Jonathan Budi 53397705 */

package ir.assignments.two.helper;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Random;

public class CreateTestFile {

	/**
	 * Holds Random object.
	 */
	private static final Random random = new Random();

	/**
	 * Create a file with random lines.
	 * 
	 * @param lines Number of lines to generate.
	 */
	public static void createTestFile(int lines) {
		PrintWriter writer;
		try {
			writer = new PrintWriter("tests/" + lines + ".txt", "UTF-8");
			int div = lines / 10;
			for (int i = 0; i < lines; ++i) {
				writer.println(generateRandomLine());
				if (i % div == 0 && i != 0) {
					System.out.println(i + " lines completed! ("
							+ ((i * 100) / lines) + "%)");
				}
			}
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Generate a random string.
	 * 
	 * @return Random String.
	 */
	private static String generateRandomLine() {
		StringBuilder s = new StringBuilder();
		/*
		for (int i = 0; i < randomInt(50, 100); ++i) {
			if (Math.random() > 0.1) {
				s.append((char) randomInt(97, 122));
			} else {
				s.append(' ');
			}
		}
		*/
		for (int x = 0; x < 5; ++x) {
			for (int i = 0; i < 10; ++i) {
				s.append((char) randomInt(97, 122));
			}
			s.append(' ');
		}
		return s.toString();
	}

	/**
	 * Get a random int from range min to max.
	 * 
	 * @param min Min number.
	 * @param max Max number.
	 * @return int in range min to max.
	 */
	private static int randomInt(int min, int max) {
		return random.nextInt((max - min) + 1) + min;
	}

	public static void main(String[] args) {
		int lines = 9400000;
		createTestFile(lines);
		System.out.println("Done!");
	}
}
