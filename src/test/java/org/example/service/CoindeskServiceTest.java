package org.example.service;

import org.example.model.TransformedResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.*;

class CoindeskServiceTest {

    @Test
    void testFromatJson() throws IOException, InterruptedException, ParseException {
        // Arrange
        CoindeskService service = Mockito.spy(new CoindeskService());
        String mockJson = "{\n" +
                "  \"time\": {\"updated\": \"Jun 10, 2025 00:03:00 UTC\"},\n" +
                "  \"bpi\": {\n" +
                "    \"USD\": {\"code\": \"USD\", \"rate\": \"68,000.0000\", \"description\": \"United States Dollar\"},\n" +
                "    \"GBP\": {\"code\": \"GBP\", \"rate\": \"54,000.0000\", \"description\": \"British Pound Sterling\"}\n" +
                "  }\n" +
                "}";
        Mockito.doReturn(mockJson).when(service).getBitcoinPrice();

        // Act
        TransformedResponse result = service.fromatJson();

        // Assert
        assertEquals("2025/06/10 00:03:00", result.getUpdatedTime());
        assertEquals(2, result.getCurrencies().size());
        assertEquals("USD", result.getCurrencies().get(0).getCode());
        assertEquals("美元", result.getCurrencies().get(0).getName());
        assertEquals("68,000.0000", result.getCurrencies().get(0).getRate());
    }
}