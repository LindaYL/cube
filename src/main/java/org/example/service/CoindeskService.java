package org.example.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.CoindeskResponse;
import org.example.model.TransformedResponse;
import org.springframework.stereotype.Service; 

import java.io.IOException;
import java.text.ParseException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.Locale;

@Service
public class CoindeskService {

    private static final Map<String, String> CURRENCY_NAME_MAP = new HashMap<>();

    static {
        CURRENCY_NAME_MAP.put("USD", "美元");
        CURRENCY_NAME_MAP.put("GBP", "英鎊");
        CURRENCY_NAME_MAP.put("EUR", "歐元");
    }
    public String getBitcoinPrice() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        // Build the HTTP request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://kengp3.github.io/blog/coindesk.json"))
                .GET()
                .build();

        // Send the request and get the response
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Return the response body
        return response.body();
    }

    public TransformedResponse fromatJson() throws IOException, InterruptedException, ParseException {
        String response = this.getBitcoinPrice();
        System.out.println("Coindesk API response: " + response); // debug

        ObjectMapper objectMapper = new ObjectMapper();
        CoindeskResponse coindeskResponse = objectMapper.readValue(response, CoindeskResponse.class);

        if (coindeskResponse == null || coindeskResponse.getTime() == null || coindeskResponse.getBpi() == null) {
            throw new IllegalStateException("Coindesk API response structure error");
        }
        // Transform the data
        TransformedResponse transformedResponse = new TransformedResponse();
        // Format the updated time
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        SimpleDateFormat inputFormat = new SimpleDateFormat("MMM d, yyyy HH:mm:ss z", Locale.ENGLISH);
        Date date = inputFormat.parse(coindeskResponse.getTime().getUpdated());
        transformedResponse.setUpdatedTime(dateFormat.format(date));
        // Map currency data
        List<TransformedResponse.CurrencyInfo> currencyInfoList = new ArrayList<>();
        coindeskResponse.getBpi().forEach((key, value) -> {
            TransformedResponse.CurrencyInfo currencyInfo = new TransformedResponse.CurrencyInfo();
            currencyInfo.setCode(value.getCode());
            currencyInfo.setName(CURRENCY_NAME_MAP.getOrDefault(value.getCode(), value.getDescription())); // Map to Chinese name
            currencyInfo.setRate(value.getRate());
            currencyInfoList.add(currencyInfo);
        });
        transformedResponse.setCurrencies(currencyInfoList);
        return transformedResponse;
    }
}