package com.backend.challenge.statistic.aggregation;

/**
 * Interface to provide statistics data.
 */
public interface StatisticsProvider {
    /**
     * Get total sum of transaction value for the time period.
     *
     * @return total sum of transaction value for the time period
     */
    double getSum();

    /**
     * Get the highest transaction value in the time period.
     *
     * @return the highest transaction value in the time period
     */
    double getMax();

    /**
     * Get the lowest transaction value in the time period.
     *
     * @return the lowest transaction value in the time period
     */
    double getMin();

    /**
     * Get total number of transactions happened in the time period.
     *
     * @return total number of transactions happened in the time period
     */
    long getCount();
}
