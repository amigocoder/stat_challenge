package com.backend.challenge.statistic.controller;

import com.backend.challenge.statistic.dto.Statistics;
import com.backend.challenge.statistic.service.StatisticsService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * Statistics controller test.
 */
public class StatisticsControllerTest {
    @Mock
    private StatisticsService statisticsService;

    @InjectMocks
    @Resource
    private StatisticsController statisticsController;

    /**
     * Initialisation.
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(statisticsService.calculateStatistics())
                .thenReturn(new Statistics(1, 1, 1, 1,1));
    }

    /**
     * Test statistics request.
     */
    @Test
    public void getStatistics() throws Exception {
        Assert.assertEquals(new Statistics(1, 1, 1, 1,1),
                statisticsController.getStatistics());
    }

}