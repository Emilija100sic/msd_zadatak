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
import java.time.format.DateTimeFormatter;
import java.util.List;


@Service
public class CsvLoaderService {

    private final StockPriceHistoryRepository priceHistoryRepository;
    private final StockRepository stockRepository;

    @Autowired
    public CsvLoaderService(StockPriceHistoryRepository priceHistoryRepository, StockRepository stockRepository) {
        this.priceHistoryRepository = priceHistoryRepository;
        this.stockRepository = stockRepository;
    }

    @Transactional
    public void loadCsvToDatabase(String csvFilePath) throws IOException, CsvException {
        String fullCsvFilePath = csvFilePath + "Stocks.csv";
        CSVReader csvReader = new CSVReader(new FileReader(fullCsvFilePath));
        List<String[]> records = csvReader.readAll();


        for (int i=2;i<3;i++) {
            String[] record = records.get(i);
            Stock stock = new Stock();
            stock.setCompanyName(record[0]);
            stock.setStockSymbol(record[1]);
            System.out.println(record[0]);
            stock.setInceptionDate(LocalDate.parse(record[2], DateTimeFormatter.ofPattern("yyyy-MM-dd")));

            stockRepository.save(stock);


            //filePath='/home/emilija/IdeaProjects/zadatak1/untitled/src/main/resources/data/' + stock.ge
            fullCsvFilePath = csvFilePath + stock.getCompanyName() + ".csv";
            csvReader = new CSVReader(new FileReader(fullCsvFilePath));
            List<String[]> histories = csvReader.readAll();

            for (int j=1;j<histories.size();j++) {
                String[] history = histories.get(j);
                System.out.println(j);
                StockPriceHistory stockPriceHistory = new StockPriceHistory();

                stockPriceHistory.setDate((history[0].equals("null")) ? null : LocalDate.parse(history[0], DateTimeFormatter.ofPattern("yyyy-MM-dd")));

                if (history[1] == null || history[1].trim().isEmpty() || history[1].equals("null")) {
                    stockPriceHistory.setOpen(null);
                } else {
                    stockPriceHistory.setOpen(Double.parseDouble(history[1]));
                }

                if (history[2] == null || history[2].trim().isEmpty() || history[2].equals("null")) {
                    stockPriceHistory.setHigh(null);
                } else {
                    stockPriceHistory.setHigh(Double.parseDouble(history[2]));
                }

                if (history[3] == null || history[3].trim().isEmpty() || history[3].equals("null")) {
                    stockPriceHistory.setLow(null);
                } else {
                    stockPriceHistory.setLow(Double.parseDouble(history[3]));
                }

                if (history[4] == null || history[4].trim().isEmpty() || history[4].equals("null")) {
                    stockPriceHistory.setClose(null);
                } else {
                    stockPriceHistory.setClose(Double.parseDouble(history[4]));
                }

                if (history[5] == null || history[5].trim().isEmpty() || history[5].equals("null")) {
                    stockPriceHistory.setAdjClose(null);
                } else {
                    stockPriceHistory.setAdjClose(Double.parseDouble(history[5]));
                }

                if (history[6] == null || history[6].trim().isEmpty() || history[6].equals("null")) {
                    stockPriceHistory.setVolume(null);
                } else {
                    stockPriceHistory.setVolume(Long.parseLong(history[6]));
                }

                stockPriceHistory.setStock(stock);

                priceHistoryRepository.save(stockPriceHistory);
            }
        }
        System.out.println("Gotovo");
        csvReader.close();
    }
}
