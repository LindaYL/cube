package org.example.model;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

public class CoindeskResponse {
    private Time time;
    private String disclaimer;
    private String chartName;
    private Map<String, CurrencyRate> bpi;
    
    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public String getDisclaimer() {
        return disclaimer;
    }

    public void setDisclaimer(String disclaimer) {
        this.disclaimer = disclaimer;
    }

    public String getChartName() {
        return chartName;
    }

    public void setChartName(String chartName) {
        this.chartName = chartName;
    }

    public Map<String, CurrencyRate> getBpi() {
        return bpi;
    }

    public void setBpi(Map<String, CurrencyRate> bpi) {
        this.bpi = bpi;
    }

    public static class Time {
        private String updated;
        private String updatedISO;
        private String updateduk;

        // Getters and Setters
        public String getUpdated() {
            return updated;
        }

        public void setUpdated(String updated) {
            this.updated = updated;
        }

        public String getUpdatedISO() {
            return updatedISO;
        }

        public void setUpdatedISO(String updatedISO) {
            this.updatedISO = updatedISO;
        }

        public String getUpdateduk() {
            return updateduk;
        }

        public void setUpdateduk(String updateduk) {
            this.updateduk = updateduk;
        }
    }

    public static class CurrencyRate {
        private String code;
        private String symbol;
        private String rate;
        private String description;
        @JsonProperty("rate_float")
        private float rateFloat;

        // Getters and Setters
        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public String getRate() {
            return rate;
        }

        public void setRate(String rate) {
            this.rate = rate;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public float getRateFloat() {
            return rateFloat;
        }

        public void setRateFloat(float rateFloat) {
            this.rateFloat = rateFloat;
        }
    }
}