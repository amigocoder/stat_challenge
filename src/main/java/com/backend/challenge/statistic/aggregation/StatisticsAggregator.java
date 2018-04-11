package com.backend.challenge.statistic.aggregation;

import com.backend.challenge.statistic.dto.Statistics;
import com.backend.challenge.statistic.dto.Transaction;

import java.time.Clock;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class for saving and returning statistics for last time period.
 * The statistics is save in map from millisecond to statistic aggregation.
 * Every millisecond key in the map related to millisecond in the minute period.
 * An entry value contains statistics aggregation for this millisecond transactions.
 * Statistics for transactions that happened early than 60 seconds ago will be overwritten
 * by the latest statistics.
 * To track statistics version "periodNumber" field is used.
 * Max size of the map is 60000 entries. So it fit O(1) restriction.
 * Add transaction operation performance is O(1).
 * Calculate statistics operation requires iteration through all map, but it contains 60000 entries
 * in the worst case. So the operation fit O(1) restriction too.
 */
public class StatisticsAggregator {
    private static final int TIME_PERIOD_MS = 60 * 1000;

    private final Map<Long, StatisticAggregation> statMap;
    private final Clock clock;

    /**
     * Create {@link StatisticsAggregator} instance.
     *
     * @param clock clock to get current time
     */
    public StatisticsAggregator(Clock clock) {
        statMap = new ConcurrentHashMap<>();
        this.clock = clock;
    }

    /**
     * Add transaction to statistics.
     *
     * @param transaction transaction data
     * @return is transaction data was added
     */
    public boolean addTransaction(Transaction transaction) {
        final long currentTime = clock.millis();
        final long timeSixtySecondsAgo = currentTime - TIME_PERIOD_MS;

        // Don't add transaction with future time or older than 60 seconds
        if ((transaction.getTimestamp() > currentTime)
                || (transaction.getTimestamp() <= timeSixtySecondsAgo)) {
            return false;
        }
        final PeriodAndMillisecond periodAndMillisecond = new PeriodAndMillisecond(transaction.getTimestamp());
        statMap.compute(periodAndMillisecond.milliSecond, (k, v) ->
                (v == null)
                        ? StatisticAggregation.createFromTransaction(periodAndMillisecond.periodNumber, transaction)
                        : v.addTransaction(periodAndMillisecond.periodNumber, transaction)
        );
        return true;
    }

    /**
     * Calculate statistics for the last 60 seconds.
     *
     * @return statistics for the last 60 seconds
     */
    public Statistics getStatistics() {
        final PeriodAndMillisecond periodAndMillisecond = new PeriodAndMillisecond(
                clock.millis());
        return statMap.entrySet().stream()
                .filter(e -> filterActualAggregations(e, periodAndMillisecond))
                .map(Map.Entry::getValue)
                .collect(new StatisticsCollector());
    }

    private static boolean filterActualAggregations(Map.Entry<Long, StatisticAggregation> statEntry,
                                                          PeriodAndMillisecond currentPeriodAndMs) {
        final long entryMs = statEntry.getKey();
        final long entryPeriodNumber = statEntry.getValue().getPeriodNumber();
        return (entryMs <= currentPeriodAndMs.milliSecond && entryPeriodNumber == currentPeriodAndMs.periodNumber)
                || (entryMs > currentPeriodAndMs.milliSecond && entryPeriodNumber == currentPeriodAndMs.periodNumber - 1);
    }

    private static final class PeriodAndMillisecond {
        private final long periodNumber;
        private final long milliSecond;

        private PeriodAndMillisecond(long transactionTime) {
            this.periodNumber = transactionTime / TIME_PERIOD_MS;
            this.milliSecond = transactionTime % TIME_PERIOD_MS;
        }
    }
}
