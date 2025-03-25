package org.example.controller;

import org.example.service.StockPriceHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
@RequestMapping("/api/stock-price-history")
public class StockPriceHistoryController {

    private final StockPriceHistoryService priceHistoryService;

    @Autowired
    public StockPriceHistoryController(StockPriceHistoryService priceHistoryService) {
        this.priceHistoryService = priceHistoryService;
    }

    // GET /api/stock-price-history/{stockId}?startDate={startDate}&endDate={endDate}
    @GetMapping("/{stockId}")
    public ResponseEntity<?> getStockPriceHistory(
            @PathVariable int stockId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {


        return ResponseEntity.ok(priceHistoryService.getStockPriceHistory(stockId, startDate, endDate));
    }

    // GET /api/stock-price-history/{stockId}/profit?startDate={startDate}&endDate={endDate}
    @GetMapping("/{stockId}/profit")
    public ResponseEntity<String> calculateBestTradeProfit(
            @PathVariable int stockId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {

        String bestTrade = priceHistoryService.calculateBestTradeProfit(stockId, startDate, endDate);
        return ResponseEntity.ok(bestTrade);
    }
}
