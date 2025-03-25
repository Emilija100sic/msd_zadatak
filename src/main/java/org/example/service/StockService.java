package org.example.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.opencsv.exceptions.CsvException;
import org.example.model.Stock;
import org.example.model.StockPriceHistory;
import org.example.repository.StockPriceHistoryRepository;
import org.example.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.opencsv.CSVReader;

import javax.transaction.Transactional;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class StockService {

    private StockRepository stockRepository;

    @Autowired
    private StockPriceHistoryRepository stockPriceHistoryRepository;

    @Autowired
    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public Stock createStock(Stock stock) {
        return stockRepository.save(stock);
    }

    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }

    public Optional<Stock> getStockById(int id) {
        return stockRepository.findById(id);
    }


    public Optional<Stock> getStockByStockSymbol(String stockSymbol) {
        return stockRepository.findByStockSymbol(stockSymbol);
    }

    public Stock updateStock(int id, Stock stockDetails) {
        Optional<Stock> stockOptional = stockRepository.findById(id);
        if (stockOptional.isPresent()) {
            Stock stock = stockOptional.get();
            stock.setStockSymbol(stockDetails.getStockSymbol());
            stock.setCompanyName(stockDetails.getCompanyName());
            stock.setInceptionDate(stockDetails.getInceptionDate());
            return stockRepository.save(stock);
        }
        return null;
    }

    public boolean deleteStock(int id) {
        if (stockRepository.existsById(id)) {
            stockRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<StockPriceHistory> getPriceHistoryForPeriod(int stockId, LocalDate startDate, LocalDate endDate) {
        return stockPriceHistoryRepository.findByStockIdAndDateBetween(stockId, startDate, endDate);
    }

    public ProfitCalculationResult calculateMaxProfit(int stockId, LocalDate startDate, LocalDate endDate) {
        List<StockPriceHistory> history = getPriceHistoryForPeriod(stockId, startDate, endDate);

        System.out.println(history);

        if (history.size() < 2) {
            return null;
        }

        double lowPrice = history.get(0).getLow();
        LocalDate buyDate = history.get(0).getDate();
        double lowClose = history.get(0).getClose();

        double highPrice = history.get(0).getHigh();
        LocalDate sellDate = history.get(0).getDate();
        double highClose = history.get(0).getClose();

        for(StockPriceHistory priceHistory : history) {
            if (priceHistory.getLow() < lowPrice){
                lowPrice = priceHistory.getLow();
                buyDate = priceHistory.getDate();
                lowClose = priceHistory.getClose();
            }

            if (priceHistory.getHigh() > highPrice) {
                highPrice = priceHistory.getHigh();
                sellDate = priceHistory.getDate();
                highClose = priceHistory.getClose();
            }
        }

        System.out.println("Konacno");
        System.out.println(highPrice);
        System.out.println(lowPrice);
        double maxProfit = highPrice - lowPrice;



        return new ProfitCalculationResult(buyDate, sellDate, maxProfit, highClose, lowClose);
    }

    public static class ProfitCalculationResult {
        @JsonProperty("buyDate") private LocalDate buyDate;
        @JsonProperty("sellDate")private LocalDate sellDate;
        @JsonProperty("profit") private double profit;

        @JsonProperty("highClose") private double highClose;
        @JsonProperty("lowClose") private double lowClose;

        public ProfitCalculationResult(LocalDate buyDate, LocalDate sellDate, double profit, double highClose, double lowClose) {
            this.buyDate = buyDate;
            this.sellDate = sellDate;
            this.profit = profit;
            this.highClose = highClose;
            this.lowClose = lowClose;
        }

        public LocalDate getBuyDate() {
            return buyDate;
        }

        public void setBuyDate(LocalDate buyDate) {
            this.buyDate = buyDate;
        }

        public LocalDate getSellDate() {
            return sellDate;
        }

        public void setSellDate(LocalDate sellDate) {
            this.sellDate = sellDate;
        }

        public double getProfit() {
            return profit;
        }

        public void setProfit(double profit) {
            this.profit = profit;
        }

        public double getHighClose() {
            return highClose;
        }

        public void setHighClose(double highClose) {
            this.highClose = highClose;
        }

        public double getLowClose() {
            return lowClose;
        }

        public void setLowClose(double lowClose) {
            this.lowClose = lowClose;
        }
    }

}
