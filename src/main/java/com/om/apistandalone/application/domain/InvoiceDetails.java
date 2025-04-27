package com.om.apistandalone.application.domain;

import java.util.List;

public record InvoiceDetails(List<Product> products, Price basketTotalAmountTaxes, Price basketPriceIncludingTax) {
}
