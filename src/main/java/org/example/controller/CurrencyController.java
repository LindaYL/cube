package org.example.controller;

import org.example.jpa.Currency;
import org.example.jpa.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.example.model.TransformedResponse;
import org.example.service.CoindeskService;
import java.io.IOException;
import java.text.ParseException;

import java.util.List;

@RestController
@RequestMapping("/api/currencies")
public class CurrencyController {
        @Autowired
        private CurrencyRepository currencyRepository;
        
        @Autowired
        private CoindeskService coindeskService;

        // Get all currencies
        @GetMapping
        public List<Currency> getAllCurrencies() {
            return currencyRepository.findAll();
        }

        // Get a currency by code
        @GetMapping("/{code}")
        public Currency getCurrencyByCode(@PathVariable String code) {
            return currencyRepository.findById(code)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Currency not found"));
        }

        // Create a new currency
        @PostMapping
        public Currency createCurrency(@RequestBody Currency currency) {
            return currencyRepository.save(currency);
        }

        // Update an existing currency
        @PutMapping("/{code}")
        public Currency updateCurrency(@PathVariable String code, @RequestBody Currency currency) {
            if (!currencyRepository.existsById(code)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Currency not found");
            }
            currency.setCode(code);
            return currencyRepository.save(currency);
        }

        // Delete a currency
        @DeleteMapping("/{code}")
        public void deleteCurrency(@PathVariable String code) {
            if (!currencyRepository.existsById(code)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Currency not found");
            }
            currencyRepository.deleteById(code);
        }

        // Get transformed coindesk info
        @GetMapping("/coindesk-transformed")
        public TransformedResponse getCoindeskTransformed() {
            try {
                return coindeskService.fromatJson();
            } catch (IOException | InterruptedException | ParseException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch or transform data");
            }
        }
}
