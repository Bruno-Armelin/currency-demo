package com.bruno.wex.models.treasury;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TreasuryReportingRatesResponse {
    @JsonProperty("data")
    private ArrayList<RateData> data;
}
