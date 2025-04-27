package com.om.apistandalone.adapters.in.dto;

import java.math.BigDecimal;

public record BasketDTO(BigDecimal totalAmountTaxes, BigDecimal priceIncludingTax) {
}