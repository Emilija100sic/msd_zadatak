package org.example;

import org.example.controller.StockController;
import org.example.model.Stock;
import org.example.service.StockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@WebMvcTest(StockController.class)
//@SpringBootTest
@SpringBootTest  // Pokreće celu Spring aplikaciju u testnom okruženju
@AutoConfigureMockMvc
public class StockControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StockService stockService;


    private Stock stock;

    @BeforeEach
    public void setUp() {
        stock = new Stock("Amazon", "AM", LocalDate.parse("1994-07-05", DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }

    // Testiranje GET metoda: /stockSymbol/{stockSymbol}
    @Test
    public void testGetStockByStockSymbol_Found() throws Exception {

        when(stockService.getStockByStockSymbol("AM")).thenReturn(Optional.of(stock));

        mockMvc.perform(get("/stockSymbol/AM"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stockSymbol").value("AM"))
                .andExpect(jsonPath("$.companyName").value("Amazon"))
                .andExpect(jsonPath("$.inceptionDate").value(LocalDate.parse("1994-07-05", DateTimeFormatter.ofPattern("yyyy-MM-dd"))));

        verify(stockService, times(1)).getStockByStockSymbol("AM");
    }

    /*@Test
    public void testGetStockByTicker_NotFound() throws Exception {
        when(stockService.getStockByStockSymbol("AAPL")).thenReturn(null);

        mockMvc.perform(get("/stocks/AAPL"))
                .andExpect(status().isNotFound());

        verify(stockService, times(1)).getStockByStockSymbol("AAPL");
    }

    // Testiranje POST metoda: /stocks
    @Test
    public void testCreateStock() throws Exception {
        Stock newStock = new Stock("Apple", "AAPL", LocalDate.parse("2020-01-01", DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        when(stockService.createStock(any(Stock.class))).thenReturn(newStock);

        mockMvc.perform(post("/stocks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"companyName\": \"Apple\", \"ticker\": \"AAPL\", \"date\": \"2020-01-01\", \"price\": 150.0}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.ticker").value("AAPL"))
                .andExpect(jsonPath("$.companyName").value("Apple"))
                .andExpect(jsonPath("$.price").value(150.0));

        verify(stockService, times(1)).createStock(any(Stock.class));
    }

    // Testiranje PUT metoda: /stocks/{ticker}
    @Test
    public void testUpdateStock_Found() throws Exception {
        Stock updatedStock = new Stock("Apple", "AAPL", LocalDate.parse("2020-01-01", DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        when(stockService.updateStock(eq(1), any(Stock.class))).thenReturn(updatedStock);

        mockMvc.perform(put("/stocks/AAPL")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"companyName\": \"Apple\", \"ticker\": \"AAPL\", \"date\": \"2020-01-01\", \"price\": 200.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ticker").value("AAPL"))
                .andExpect(jsonPath("$.companyName").value("Apple"))
                .andExpect(jsonPath("$.price").value(200.0));

        verify(stockService, times(1)).updateStock(eq(1), any(Stock.class));
    }

    @Test
    public void testUpdateStock_NotFound() throws Exception {
        when(stockService.updateStock(eq(1), any(Stock.class))).thenReturn(null);

        mockMvc.perform(put("/stocks/AAPL")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"companyName\": \"Apple\", \"ticker\": \"AAPL\", \"date\": \"2020-01-01\", \"price\": 200.0}"))
                .andExpect(status().isNotFound());

        verify(stockService, times(1)).updateStock(eq(1), any(Stock.class));
    }

    // Testiranje DELETE metoda: /stocks/{ticker}
    @Test
    public void testDeleteStock_Found() throws Exception {
        when(stockService.deleteStock(1)).thenReturn(true);

        mockMvc.perform(delete("/stocks/AAPL"))
                .andExpect(status().isNoContent());

        verify(stockService, times(1)).deleteStock(1);
    }

    @Test
    public void testDeleteStock_NotFound() throws Exception {
        when(stockService.deleteStock(1)).thenReturn(false);

        mockMvc.perform(delete("/stocks/AAPL"))
                .andExpect(status().isNotFound());

        verify(stockService, times(1)).deleteStock(1);
    }*/
}
