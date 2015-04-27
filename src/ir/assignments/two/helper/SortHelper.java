/* Jonathan Budi 53397705 */

package ir.assignments.two.helper;

import ir.assignments.two.a.Frequency;

import java.util.Comparator;

public class SortHelper {

	/**
	 * Sort Frequency by frequency, alphabetical
	 **/
	public static class FrequencyComparator implements Comparator<Frequency> {
		public int compare(Frequency f1, Frequency f2) {
			int i = f1.getFrequency() - f2.getFrequency();
			if (i > 0) {
				return -1;
			} else if (i == 0) {
				return f1.getText().compareTo(f2.getText());
			} else {
				return 1;
			}
		}
	}

	/**
	 * Sort Frequency by decreasing size, frequency, alphabetical
	 **/
	public static class FrequencyComparator2 implements Comparator<Frequency> {
		public int compare(Frequency f1, Frequency f2) {
			int i = f1.getText().length() - f2.getText().length();
			if (i > 0) {
				return -1;
			} else if (i == 0) {
				return new FrequencyComparator().compare(f1, f2);
			} else {
				return 1;
			}
		}
	}
}
