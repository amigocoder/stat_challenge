package com.backend.challenge.statistic.service;

import com.backend.challenge.statistic.dto.Statistics;
import com.backend.challenge.statistic.dto.Transaction;

/**
 * Service to handle transaction statistics for last minute.
 */
public interface StatisticsService {

    /**
     * Add a transaction to the statistics if the transaction timestamp is not too old.
     *
     * @param transaction transaction data
     * @return true if transaction was added
     */
    boolean addTransaction(Transaction transaction);

    /**
     * Calculate statistics for the last 60 seconds.
     *
     * @return statistics for the last 60 seconds
     */
    Statistics calculateStatistics();
}
