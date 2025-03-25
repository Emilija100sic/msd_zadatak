package org.example.repository;

import org.example.model.StockPriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface StockPriceHistoryRepository extends JpaRepository<StockPriceHistory, Integer> {
    List<StockPriceHistory> findByStockIdAndDateBetween(int stockId, LocalDate startDate, LocalDate endDate);
}
