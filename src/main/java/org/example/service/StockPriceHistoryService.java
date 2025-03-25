package org.example.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.example.model.Stock;
import org.example.model.StockPriceHistory;
import org.example.repository.StockPriceHistoryRepository;
import org.example.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import com.opencsv.CSVReader;

@Service
public class StockPriceHistoryService {

    private final StockPriceHistoryRepository priceHistoryRepository;
    private final StockRepository stockRepository;

    @Autowired
    public StockPriceHistoryService(StockPriceHistoryRepository priceHistoryRepository, StockRepository stockRepository) {
        this.priceHistoryRepository = priceHistoryRepository;
        this.stockRepository = stockRepository;
    }


    public List<StockPriceHistory> getStockPriceHistory(int stockId, LocalDate startDate, LocalDate endDate) {
        return priceHistoryRepository.findByStockIdAndDateBetween(stockId, startDate, endDate);
    }


    public String calculateBestTradeProfit(int stockId, LocalDate startDate, LocalDate endDate) {
        List<StockPriceHistory> priceHistory = priceHistoryRepository.findByStockIdAndDateBetween(stockId, startDate, endDate);

        if (priceHistory.isEmpty()) {
            return "No data available for the provided date range.";
        }

        Double maxProfit = 0.0;
        LocalDate bestBuyDate = null;
        LocalDate bestSellDate = null;

        for (int i = 0; i < priceHistory.size(); i++) {
            StockPriceHistory buy = priceHistory.get(i);
            for (int j = i + 1; j < priceHistory.size(); j++) {
                StockPriceHistory sell = priceHistory.get(j);
                double profit = sell.getClose() - buy.getClose();

                if (profit > maxProfit) {
                    maxProfit = profit;
                    bestBuyDate = buy.getDate();
                    bestSellDate = sell.getDate();
                }
            }
        }

        return String.format("Best trade: Buy on %s and sell on %s for a profit of %.2f", bestBuyDate, bestSellDate, maxProfit);
    }
}
