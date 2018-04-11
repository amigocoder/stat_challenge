package com.backend.challenge.statistic.controller;

import com.backend.challenge.statistic.dto.Statistics;
import com.backend.challenge.statistic.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Statistic controller.
 */
@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    /**
     * Get statistics for last 60 seconds.
     * @return statistics for last 60 seconds
     */
    @GetMapping
    public Statistics getStatistics() {
        return statisticsService.calculateStatistics();
    }
}
