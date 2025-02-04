package com.bruno.wex.service;

import com.bruno.wex.models.treasury.RateData;
import com.bruno.wex.models.treasury.TreasuryReportingRatesResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
public class TreasuryReportingRatesService {

    public final DateTimeFormatter REPORTING_RATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public final String URL_FORMAT = """
                    https://api.fiscaldata.treasury.gov/services/api/fiscal_service/v1/accounting/od/rates_of_exchange?
                    fields=country_currency_desc,exchange_rate,effective_date&
                    filter=country_currency_desc:eq:%s,effective_date:lte:%s,effective_date:gte:%s&
                    sort=-effective_date
                    """.replaceAll("\n", "");

    public RateData findCurrencyRate(String currency, LocalDate transactionDate) {
        try {
            var url = String.format(URL_FORMAT, currency,
                    transactionDate.format(REPORTING_RATE_FORMAT), transactionDate.minusMonths(6).format(REPORTING_RATE_FORMAT));
            log.info("Querying: {}", url);
            RestTemplate restTemplate = new RestTemplate();
            var responseEntity = restTemplate.getForEntity(url, TreasuryReportingRatesResponse.class);
            if(responseEntity.getBody() != null && responseEntity.getBody().getData() != null && !responseEntity.getBody().getData().isEmpty()) {
                return responseEntity.getBody().getData().getFirst();
            }
        }
        catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
        return null;
    }

}
