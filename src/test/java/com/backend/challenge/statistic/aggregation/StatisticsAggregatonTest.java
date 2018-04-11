package com.backend.challenge.statistic.aggregation;

import com.backend.challenge.statistic.dto.Statistics;
import com.backend.challenge.statistic.dto.Transaction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.Clock;
import java.time.Instant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Tests for statistics aggregation.
 */
public class StatisticsAggregatonTest {

    private StatisticsAggregator statAggr;

    /**
     * Initialisation.
     */
    @Before
    public void init() {
        statAggr = new StatisticsAggregator(Clock.systemUTC());
    }

    /**
     * Test statistics calculation without any transactions.
     */
    @Test
    public void testEmptyStat() {
        Assert.assertEquals(new Statistics(0, 0, 0, 0, 0), statAggr.getStatistics());
    }

    /**
     * Test transaction addition with timestamp less than 60 seconds ago.
     */
    @Test
    public void testExpiredTransactionAddition() {
        Assert.assertFalse(statAggr.addTransaction(new Transaction(10, 100)));
    }

    /**
     * Test that transaction with timestamp in the last 60 seconds was added.
     */
    @Test
    public void testCorrectTransactionAddition() {
        Assert.assertTrue(statAggr.addTransaction(new Transaction(10, System.currentTimeMillis())));
    }

    /**
     * Test statistics calculation for one transaction.
     */
    @Test
    public void testOneStat() {
        statAggr.addTransaction(new Transaction(1, System.currentTimeMillis()));
        Assert.assertEquals(new Statistics(1, 1, 1, 1, 1), statAggr.getStatistics());
    }

    /**
     * Test statistics calculation for several transactions with the same timestamp.
     */
    @Test
    public void testSeveralTransactionsInTheSameTime() {
        final long curTime = System.currentTimeMillis();
        statAggr.addTransaction(new Transaction(50, curTime));
        statAggr.addTransaction(new Transaction(30, curTime));
        statAggr.addTransaction(new Transaction(10, curTime));
        statAggr.addTransaction(new Transaction(9, curTime));
        statAggr.addTransaction(new Transaction(1, curTime));
        Assert.assertEquals(new Statistics(100, 20, 50, 1, 5), statAggr.getStatistics());
    }

    /**
     * Test statistics calculation for several transactions with different timestamps.
     */
    @Test
    public void testSeveralTransactionsInDifferentTimes() {
        final long curTime = System.currentTimeMillis();
        statAggr.addTransaction(new Transaction(50, curTime - 1));
        statAggr.addTransaction(new Transaction(30, curTime - 2));
        statAggr.addTransaction(new Transaction(10, curTime - 3));
        statAggr.addTransaction(new Transaction(9, curTime - 4));
        statAggr.addTransaction(new Transaction(1, curTime - 5));
        Assert.assertEquals(new Statistics(100, 20, 50, 1, 5), statAggr.getStatistics());
    }

    /**
     * Tests that statistics is empty if all added transactions were
     * expired (their timestamps < currentTime - 60 seconds).
     */
    @Test
    public void testExpiredTransactions() {
        final Clock clockMock = Mockito.mock(Clock.class);
        final Instant now = Instant.now();
        Mockito.when(clockMock.millis()).thenReturn(now.toEpochMilli(), now.plusSeconds(1).toEpochMilli(),
                now.plusSeconds(2).toEpochMilli(), now.plusSeconds(70).toEpochMilli());
        statAggr = new StatisticsAggregator(clockMock);
        statAggr.addTransaction(new Transaction(5, now.toEpochMilli()));
        statAggr.addTransaction(new Transaction(3, now.plusMillis(1).toEpochMilli()));
        statAggr.addTransaction(new Transaction(2, now.plusMillis(2).toEpochMilli()));
        Assert.assertEquals(new Statistics(0, 0, 0, 0, 0), statAggr.getStatistics());
    }

    /**
     * Test statistics calculation if a transaciton was added with
     * timestamp = (existing transaciton timestamp + 60 seconds).
     * Previous transaction must not be count in this case.
     */
    @Test
    public void testOverwriteStatisticsAfterCycle() {
        final Clock clockMock = Mockito.mock(Clock.class);
        final Instant now = Instant.now();
        Mockito.when(clockMock.millis()).thenReturn(now.toEpochMilli(), now.plusSeconds(60).toEpochMilli(),
                now.plusSeconds(60).toEpochMilli());
        statAggr = new StatisticsAggregator(clockMock);
        statAggr.addTransaction(new Transaction(10, now.toEpochMilli()));
        statAggr.addTransaction(new Transaction(20, now.plusSeconds(60).toEpochMilli()));
        Assert.assertEquals(new Statistics(20, 20, 20, 20, 1), statAggr.getStatistics());
    }

    /**
     * Test concurrent writes with the same timestamps.
     *
     * @throws InterruptedException if interrupted while waiting
     */
    @Test
    public void testConcurrentWrites() throws InterruptedException {
        final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        final Instant now = Instant.now();
        for (int i = 1; i <= 10; i++) {
            // Write to 2 different timestamps
            final long transactionTime = i % 2 == 0 ? now.toEpochMilli() : now.minusMillis(5).toEpochMilli();
            //final long transactionTime = now.toEpochMilli();
            final Transaction trans = new Transaction(i, transactionTime);
            executor.execute(() ->
                    statAggr.addTransaction(trans));
        }
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);
        Assert.assertEquals(new Statistics(55, 5.5, 10, 1, 10), statAggr.getStatistics());
    }

}