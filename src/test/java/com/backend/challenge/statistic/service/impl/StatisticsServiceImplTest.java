package com.backend.challenge.statistic.service.impl;

import com.backend.challenge.statistic.aggregation.StatisticsAggregator;
import com.backend.challenge.statistic.dto.Statistics;
import com.backend.challenge.statistic.dto.Transaction;
import com.backend.challenge.statistic.service.StatisticsService;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

/**
 * Tests for {@link StatisticsServiceImpl}.
 */
public class StatisticsServiceImplTest {

    /**
     * Test transaction addition.
     */
    @Test
    public void addTransaction() {
        final StatisticsAggregator statAggr = Mockito.mock(StatisticsAggregator.class);
        Mockito.when(statAggr.addTransaction(Mockito.any())).thenReturn(true);
        StatisticsService statService = new StatisticsServiceImpl(statAggr);
        Assert.assertTrue(statService.addTransaction(new Transaction(1, 1)));
    }

    /**
     * Test statistics calculation.
     */
    @Test
    public void calculateStatistics() throws Exception {
        final StatisticsAggregator statAggr = Mockito.mock(StatisticsAggregator.class);
        Mockito.when(statAggr.getStatistics()).thenReturn(new Statistics(1, 1, 1,1 ,1));
        StatisticsService statService = new StatisticsServiceImpl(statAggr);
        Assert.assertEquals(new Statistics(1, 1, 1, 1, 1), statService.calculateStatistics());
    }

}