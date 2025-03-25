package org.example.controller;


import org.example.service.CsvLoaderService;
import org.example.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api/csv-loader")
public class CsvLoaderController {

    private final CsvLoaderService csvLoaderService;

    @Autowired
    public CsvLoaderController(CsvLoaderService csvLoaderService) {
        this.csvLoaderService = csvLoaderService;
    }

    // POST /api/csv-loader/upload/?filePath={filePath} filePath - Putanja do Stock.csv fajla	
    @PostMapping("/upload")
    public ResponseEntity<String> uploadCsv(@RequestParam("filePath") String filePath) {
        try {
            csvLoaderService.loadCsvToDatabase(filePath);

            return ResponseEntity.ok("CSV file successfully uploaded and data saved to the database.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error occurred: " + e.getMessage());
        }
    }
}
