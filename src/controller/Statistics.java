package controller;

import java.util.Arrays;

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
	 * Initiates a newly created Statistics object and computes mean, median and standard deviation.
	 *
	 * @param data The data to be computed.
	 */
	public Statistics(double[] data) {
		if (data.length != 0) {
			// compute mean
			this.mean = Arrays.stream(data).parallel().average().orElse(Double.NaN);
			// compute median
			double[] sortedArray = data.clone();
			Arrays.parallelSort(sortedArray);
			this.median = (sortedArray[sortedArray.length / 2] + sortedArray[(sortedArray.length - 1) / 2]) / 2;
			// compute standard deviation
			double sumSquare = Arrays.stream(data).parallel().map((x) -> x * x).reduce(Double::sum).orElse(Double.NaN);
			this.standardDeviation = Math.sqrt(sumSquare / data.length - mean * mean);
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
		return String.format("Statistics [ Mean = %f, Median = %f, Standard Deviation = %f ]", getMean(), getMedian(), getStandardDeviation());
	}

}
