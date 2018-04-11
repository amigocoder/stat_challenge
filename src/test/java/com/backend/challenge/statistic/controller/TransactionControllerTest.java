package com.backend.challenge.statistic.controller;

import com.backend.challenge.statistic.dto.Transaction;
import com.backend.challenge.statistic.service.StatisticsService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import javax.annotation.Resource;

/**
 * Transaction controller tests.
 */
public class TransactionControllerTest {

    @Mock
    private StatisticsService statisticsService;

    private Transaction validTransaction;
    private Transaction expiredTransaction;

    @InjectMocks
    @Resource
    private TransactionController transactionController;

    /**
     * Initialisation.
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        validTransaction = new Transaction(10, 10);
        Mockito.when(statisticsService.addTransaction(validTransaction))
                .thenReturn(true);
        expiredTransaction = new Transaction(5, 5);
        Mockito.when(statisticsService.addTransaction(expiredTransaction))
                .thenReturn(false);
    }

    /**
     * Test valid transaction addition.
     */
    @Test
    public void addTransaction() {
        Assert.assertEquals(HttpStatus.CREATED,
                transactionController.addTransaction(validTransaction).getStatusCode());
    }

    /**
     * Test expired transaction addition.
     */
    @Test
    public void addExpiredTransaction() {
        Assert.assertEquals(HttpStatus.NO_CONTENT,
                transactionController.addTransaction(expiredTransaction).getStatusCode());
    }

}