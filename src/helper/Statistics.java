package helper;

import java.util.Arrays;
import java.util.List;

/**
 * Statistics tool for data
 */
public class Statistics {

	/** Mean */
	private double mean;

	/** Median */
	private double median;

	/** Standard Deviation */
	private double standardDeviation;

	/**
	 * Initiates a newly created Statistics object and computes mean, median and
	 * standard deviation.
	 *
	 * @param data The data to be computed.
	 */
	public Statistics(double[] data) {
		int length = data.length;
		if (length != 0) {
			// compute mean
			this.mean = Arrays.stream(data).average().orElse(Double.NaN);
			// compute median
			double[] sortedArray = data.clone();
			Arrays.sort(sortedArray);
			this.median = (sortedArray[length / 2] + sortedArray[(length - 1) / 2]) / 2;
			// compute standard deviation
			double sumSquare = Arrays.stream(data).map((x) -> x * x).reduce(Double::sum).orElse(Double.NaN);
			this.standardDeviation = Math.sqrt(sumSquare / length - mean * mean);
		} else {
			this.mean = this.median = this.standardDeviation = Double.NaN;
		}
	}

	/**
	 * Initiates a newly created Statistics object and computes mean, median and
	 * standard deviation.
	 *
	 * @param data The data to be computed.
	 */
	public <T extends Number & Comparable<T>> Statistics(T[] data) {
		int length = data.length;
		if (length != 0) {
			// compute mean
			this.mean = Arrays.stream(data).mapToDouble(Number::doubleValue).average().orElse(Double.NaN);
			// compute median
			T[] sortedArray = data.clone();
			Arrays.sort(sortedArray);
			this.median = (sortedArray[length / 2].doubleValue() + sortedArray[(length - 1) / 2].doubleValue()) / 2;
			// compute standard deviation
			double sumSquare = Arrays.stream(data).mapToDouble(Number::doubleValue).map((x) -> x * x)
					.reduce(Double::sum).orElse(Double.NaN);
			this.standardDeviation = Math.sqrt(sumSquare / length - mean * mean);
		} else {
			this.mean = this.median = this.standardDeviation = Double.NaN;
		}
	}

	/**
	 * Initiates a newly created Statistics object and computes mean, median and
	 * standard deviation.
	 *
	 * @param data The data to be computed.
	 */
	public <T extends Number & Comparable<T>> Statistics(List<T> data) {
		int length = data.size();
		if (length != 0) {
			// compute mean
			this.mean = data.stream().mapToDouble(Number::doubleValue).average().orElse(Double.NaN);
			// compute median
			data.sort(T::compareTo);
			this.median = (data.get(length / 2).doubleValue() + data.get((length - 1) / 2).doubleValue()) / 2;
			// compute standard deviation
			double sumSquare = data.stream().mapToDouble(Number::doubleValue).map((x) -> x * x)
					.reduce(Double::sum).orElse(Double.NaN);
			this.standardDeviation = Math.sqrt(sumSquare / length - mean * mean);
		} else {
			this.mean = this.median = this.standardDeviation = Double.NaN;
		}
	}

	/**
	 * Returns mean of the data.
	 *
	 * @return mean of the data.
	 */
	public double getMean() {
		return mean;
	}

	/**
	 * Returns median of the data.
	 *
	 * @return median of the data.
	 */
	public double getMedian() {
		return median;
	}

	/**
	 * Returns standard deviation of the data.
	 *
	 * @return standard deviation of the data.
	 */
	public double getStandardDeviation() {
		return standardDeviation;
	}

	/**
	 * Converts this Statistics object to a String.
	 *
	 * @return a string representation of this date.
	 */
	@Override
	public String toString() {
		return String.format("Statistics : Mean = %.2f, Median = %.2f, Standard Deviation = %.2f",
				getMean(), getMedian(), getStandardDeviation());
	}

	public static String getLetterGrade(Double total) {
		String grade = "";

		if (total >= 97) {
			grade = "A+";
		} else if (total >= 93) {
			grade = "A";
		} else if (total >= 90) {
			grade = "A-";
		} else if (total >= 87) {
			grade = "B+";
		} else if (total >= 83) {
			grade = "B";
		} else if (total >= 80) {
			grade = "B-";
		} else if (total >= 77) {
			grade = "C+";
		} else if (total >= 73) {
			grade = "C";
		} else if (total >= 70) {
			grade = "C-";
		} else if (total >= 60) {
			grade = "D";
		} else {
			grade = "F";
		}

		return grade;

	}

}