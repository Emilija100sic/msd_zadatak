package org.example.model;



import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "stock")
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String companyName;
    private String stockSymbol;
    private LocalDate inceptionDate;

    public Stock() {}

    public Stock(String companyName, String stockSymbol, LocalDate inceptionDate) {
        this.companyName = companyName;
        this.stockSymbol = stockSymbol;
        this.inceptionDate = inceptionDate;
    }

    @OneToMany(mappedBy = "stock", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<StockPriceHistory> priceHistory;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public LocalDate getInceptionDate() {
        return inceptionDate;
    }

    public void setInceptionDate(LocalDate inceptionDate) {
        this.inceptionDate = inceptionDate;
    }

    public List<StockPriceHistory> getPriceHistory() {
        return priceHistory;
    }

    public void setPriceHistory(List<StockPriceHistory> priceHistory) {
        this.priceHistory = priceHistory;
    }
}
