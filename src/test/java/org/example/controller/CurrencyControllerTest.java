package org.example.controller;

import org.example.jpa.Currency;
import org.example.jpa.CurrencyRepository;
import org.example.model.TransformedResponse;
import org.example.service.CoindeskService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CurrencyController.class)
class CurrencyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CurrencyRepository currencyRepository;

    @MockBean
    private CoindeskService coindeskService;

    @Test
    void testGetAllCurrencies() throws Exception {
        Currency usd = new Currency();
        usd.setCode("USD");
        usd.setName("美元");
        Mockito.when(currencyRepository.findAll()).thenReturn(Arrays.asList(usd));
        mockMvc.perform(get("/api/currencies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].code").value("USD"));
    }

    @Test
    void testGetCurrencyByCode() throws Exception {
        Currency usd = new Currency();
        usd.setCode("USD");
        usd.setName("美元");
        Mockito.when(currencyRepository.findById("USD")).thenReturn(Optional.of(usd));
        mockMvc.perform(get("/api/currencies/USD"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("美元"));
    }

    @Test
    void testGetCurrencyByCode_NotFound() throws Exception {
        Mockito.when(currencyRepository.findById("TWD")).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/currencies/TWD"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateCurrency() throws Exception {
        Currency usd = new Currency();
        usd.setCode("USD");
        usd.setName("美元");
        Mockito.when(currencyRepository.save(any(Currency.class))).thenReturn(usd);
        mockMvc.perform(post("/api/currencies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"code\":\"USD\",\"name\":\"美元\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("USD"));
    }

    @Test
    void testUpdateCurrency() throws Exception {
        Currency usd = new Currency();
        usd.setCode("USD");
        usd.setName("美元");
        Mockito.when(currencyRepository.existsById("USD")).thenReturn(true);
        Mockito.when(currencyRepository.save(any(Currency.class))).thenReturn(usd);
        mockMvc.perform(put("/api/currencies/USD")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"code\":\"USD\",\"name\":\"美元\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("美元"));
    }

    @Test
    void testUpdateCurrency_NotFound() throws Exception {
        Mockito.when(currencyRepository.existsById("TWD")).thenReturn(false);
        mockMvc.perform(put("/api/currencies/TWD")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"code\":\"TWD\",\"name\":\"台幣\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteCurrency() throws Exception {
        Mockito.when(currencyRepository.existsById("USD")).thenReturn(true);
        mockMvc.perform(delete("/api/currencies/USD"))
                .andExpect(status().isOk());
        Mockito.verify(currencyRepository).deleteById(eq("USD"));
    }

    @Test
    void testDeleteCurrency_NotFound() throws Exception {
        Mockito.when(currencyRepository.existsById("TWD")).thenReturn(false);
        mockMvc.perform(delete("/api/currencies/TWD"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetCoindeskTransformed() throws Exception {
        TransformedResponse mockResponse = new TransformedResponse();
        Mockito.when(coindeskService.fromatJson()).thenReturn(mockResponse);
        mockMvc.perform(get("/api/currencies/coindesk-transformed"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetCoindeskTransformed_Exception() throws Exception {
        Mockito.when(coindeskService.fromatJson()).thenThrow(new RuntimeException("fail"));
        mockMvc.perform(get("/api/currencies/coindesk-transformed"))
                .andExpect(status().isInternalServerError());
    }
}