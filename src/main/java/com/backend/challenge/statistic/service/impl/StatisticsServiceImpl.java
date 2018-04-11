package com.backend.challenge.statistic.service.impl;

import com.backend.challenge.statistic.aggregation.StatisticsAggregator;
import com.backend.challenge.statistic.dto.Statistics;
import com.backend.challenge.statistic.dto.Transaction;
import com.backend.challenge.statistic.service.StatisticsService;
import org.springframework.stereotype.Service;

import java.time.Clock;

/**
 * Statistics service implementation.
 */
@Service
public class StatisticsServiceImpl implements StatisticsService {
    private StatisticsAggregator statisticsAggregator;

    public StatisticsServiceImpl(StatisticsAggregator statisticsAggregator) {
        this.statisticsAggregator = statisticsAggregator;
    }

    public StatisticsServiceImpl() {
        this.statisticsAggregator = new StatisticsAggregator(Clock.systemUTC());
    }

    @Override
    public boolean addTransaction(Transaction transaction) {
        return statisticsAggregator.addTransaction(transaction);
    }

    @Override
    public Statistics calculateStatistics() {
        return statisticsAggregator.getStatistics();
    }
}
