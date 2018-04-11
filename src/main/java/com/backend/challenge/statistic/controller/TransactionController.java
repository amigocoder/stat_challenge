package com.backend.challenge.statistic.controller;

import com.backend.challenge.statistic.dto.Transaction;
import com.backend.challenge.statistic.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Transaction controller.
 */
@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private StatisticsService statisticsService;

    /**
     * Add transaction data.
     *
     * @param transaction transaction data
     * @return true if transaction data was added
     */
    @PostMapping
    public ResponseEntity addTransaction(@RequestBody Transaction transaction) {
        final boolean wasAdded = statisticsService.addTransaction(transaction);
        final HttpStatus status = wasAdded ? HttpStatus.CREATED : HttpStatus.NO_CONTENT;
        return new ResponseEntity(status);
    }
}
