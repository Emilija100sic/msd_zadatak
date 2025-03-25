package org.example.controller;

import com.sun.xml.bind.v2.TODO;
import org.example.model.Stock;
import org.example.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.example.service.StockService.ProfitCalculationResult;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/")
public class StockController {

    private final StockService stockService;

    @Autowired
    public StockController(StockService stockService) {
        this.stockService = stockService;
    }


    @GetMapping
    public List<Stock> getAllStocks() {
        return stockService.getAllStocks();
    }


    @GetMapping("/{id}")
    public ResponseEntity<Stock> getStockById(@PathVariable int id) {
        Optional<Stock> stock = stockService.getStockById(id);
        return stock.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/stockSymbol/{stockSymbol}")
    public ResponseEntity<Stock> getStockByStockSymbol(@PathVariable String stockSymbol) {
        Optional<Stock> stock = stockService.getStockByStockSymbol(stockSymbol);
        return stock.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Stock> createStock(@RequestBody Stock stock) {
        if (stock.getStockSymbol() == null || stock.getCompanyName() == null || stock.getInceptionDate() == null) {
            return ResponseEntity.badRequest().build();
        }
        Stock savedStock = stockService.createStock(stock);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedStock);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Stock> updateStock(@PathVariable int id, @RequestBody Stock stockDetails) {
        Stock updatedStock = stockService.updateStock(id, stockDetails);
        if (updatedStock != null) {
            return ResponseEntity.ok(updatedStock);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStock(@PathVariable int id) {
        if (stockService.deleteStock(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    //Kao poboljsanje za ovaj upit, treba dodati proveru da li su startDate i endDate prosledjeni
    @GetMapping("/max-profit/{stockSymbol}")
    public ResponseEntity<MaxProfitResponse> getMaxProfit(
            @PathVariable String stockSymbol,
            @RequestParam String startDate,
            @RequestParam String endDate) {

        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        var stock = stockService.getStockByStockSymbol(stockSymbol);
        if (stock.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        LocalDate startBefore = start.minusDays(end.getDayOfYear() - start.getDayOfYear());
        LocalDate endBefore = end.minusDays(end.getDayOfYear() - start.getDayOfYear());
        LocalDate startAfter = end.plusDays(1);
        LocalDate endAfter = startAfter.plusDays(end.getDayOfYear() - start.getDayOfYear());

        System.out.println(startAfter);
        System.out.println(endAfter);

        ProfitCalculationResult resultOriginal = stockService.calculateMaxProfit(stock.get().getId(), start, end);
        ProfitCalculationResult resultBefore = stockService.calculateMaxProfit(stock.get().getId(), startBefore, endBefore);
        ProfitCalculationResult resultAfter = stockService.calculateMaxProfit(stock.get().getId(), startAfter, endAfter);

        MaxProfitResponse response = new MaxProfitResponse(resultOriginal, resultBefore, resultAfter);

        return ResponseEntity.ok(response);
    }


    public static class MaxProfitResponse {
        private ProfitCalculationResult originalPeriod;
        private ProfitCalculationResult beforePeriod;
        private ProfitCalculationResult afterPeriod;

        public MaxProfitResponse(ProfitCalculationResult originalPeriod, ProfitCalculationResult beforePeriod, ProfitCalculationResult afterPeriod) {
            this.originalPeriod = originalPeriod;
            this.beforePeriod = beforePeriod;
            this.afterPeriod = afterPeriod;
        }


        public ProfitCalculationResult getOriginalPeriod() {
            return originalPeriod;
        }

        public void setOriginalPeriod(ProfitCalculationResult originalPeriod) {
            this.originalPeriod = originalPeriod;
        }

        public ProfitCalculationResult getBeforePeriod() {
            return beforePeriod;
        }

        public void setBeforePeriod(ProfitCalculationResult beforePeriod) {
            this.beforePeriod = beforePeriod;
        }

        public ProfitCalculationResult getAfterPeriod() {
            return afterPeriod;
        }

        public void setAfterPeriod(ProfitCalculationResult afterPeriod) {
            this.afterPeriod = afterPeriod;
        }
    }

}
