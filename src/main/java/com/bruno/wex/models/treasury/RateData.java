package com.bruno.wex.models.treasury;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RateData {
    @JsonProperty("country_currency_desc")
    private String countryCurrencyDescription;

    @JsonProperty("exchange_rate")
    private String exchangeRate;

    @JsonProperty("effective_date")
    private String effectiveDate;
}
