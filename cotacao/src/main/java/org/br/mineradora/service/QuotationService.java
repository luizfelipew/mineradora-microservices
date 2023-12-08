package org.br.mineradora.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.br.mineradora.client.CurrencyPriceClient;
import org.br.mineradora.dto.CurrencyPriceDTO;
import org.br.mineradora.dto.QuotationDTO;
import org.br.mineradora.entity.QuotationEntity;
import org.br.mineradora.message.KafkaEvents;
import org.br.mineradora.repository.QuotationRepository;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.math.BigDecimal;
import java.util.Date;

@ApplicationScoped
public class QuotationService {

    @Inject
    @RestClient
    CurrencyPriceClient currencyPriceClient;

    @Inject
    QuotationRepository quotationRepository;

    @Inject
    KafkaEvents kafkaEvents;

    public void getCurrencyPrice() {

        var currencyPriceInfo = currencyPriceClient.getPriceByPair("USD-BRL");

        if (updateCurrentInfoPrice(currencyPriceInfo)) {
            kafkaEvents.sendNewKafkaEvent(QuotationDTO
                    .builder()
                            .currencyPrice(new BigDecimal(currencyPriceInfo.getUSDBRL().getBid()))
                            .date(new Date())
                    .build());
        }
    }

    private boolean updateCurrentInfoPrice(final CurrencyPriceDTO currencyPriceInfo) {
        var currentPrice = new BigDecimal(currencyPriceInfo.getUSDBRL().getBid());
        var updatePrice = false;

        var quotationlist = quotationRepository.findAll().list();

        if (quotationlist.isEmpty()) {
            saveQuotation(currencyPriceInfo);
            updatePrice = true;
        } else {
            var lastDolarPrice = quotationlist.get(quotationlist.size() - 1);

            if (currentPrice.floatValue() > lastDolarPrice.getCurrencyPrice().floatValue()) {
                updatePrice = true;
                saveQuotation(currencyPriceInfo);
            }
        }

        return updatePrice;
    }

    private void saveQuotation(CurrencyPriceDTO currencyinfo) {
        var quotation = new QuotationEntity();

        quotation.setDate(new Date());
        quotation.setCurrencyPrice(new BigDecimal(currencyinfo.getUSDBRL().getBid()));
        quotation.setPctChange(currencyinfo.getUSDBRL().getPctChange());
        quotation.setPair("USD-BRL");

        quotationRepository.persist(quotation);

    }
}
