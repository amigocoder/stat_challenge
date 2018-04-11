package com.backend.challenge.statistic.aggregation;

import com.backend.challenge.statistic.dto.Transaction;

/**
 * Statistics aggregation for millisecond.
 */
public class StatisticAggregation implements StatisticsProvider {
    private final long periodNumber;
    private final double sum;
    private final double max;
    private final double min;
    private final long count;

    /**
     * Create {@link StatisticAggregation} instance.
     *
     * @param periodNumber period number
     * @param sum total sum of transaction value for the time period
     * @param max highest transaction value in the time period
     * @param min lowest transaction value in the time period
     * @param count total number of transactions happened in the time period
     * @param count total number of transactions happened in the time period
     */
    public StatisticAggregation(long periodNumber, double sum, double max,
                                double min, long count) {
        this.periodNumber = periodNumber;
        this.sum = sum;
        this.max = max;
        this.min = min;
        this.count = count;
    }

    public long getPeriodNumber() {
        return periodNumber;
    }

    /**
     * Add transaction data to current aggregation.
     *
     * @param periodNumber transaction period number
     * @param transaction transaciton data
     * @return new statistics aggregation that included the transaction data
     */
    public StatisticAggregation addTransaction(long periodNumber, Transaction transaction) {
        if (periodNumber > this.periodNumber) {
            return createFromTransaction(periodNumber, transaction);
        } else {
            final double newSum = sum + transaction.getAmount();
            final double newMin = Math.min(min, transaction.getAmount());
            final double newMax = Math.max(max, transaction.getAmount());
            final long newCount = count + 1;
            return new StatisticAggregation(periodNumber, newSum, newMax, newMin, newCount);
        }
    }

    @Override
    public double getSum() {
        return sum;
    }

    @Override
    public double getMax() {
        return max;
    }

    @Override
    public double getMin() {
        return min;
    }

    @Override
    public long getCount() {
        return count;
    }

    /**
     * Create statistics aggregation from single transaction data.
     *
     * @param periodNumber transaction period number
     * @param transaction transaction data
     * @return statistics aggregation from single transaction data
     */
    public static StatisticAggregation createFromTransaction(long periodNumber, Transaction transaction) {
        double amount = transaction.getAmount();
        return new StatisticAggregation(periodNumber, amount, amount, amount, 1);
    }

    @Override
    public String toString() {
        return "StatisticAggregation{" +
                "periodNumber=" + periodNumber +
                ", sum=" + sum +
                ", max=" + max +
                ", min=" + min +
                ", count=" + count +
                '}';
    }
}
