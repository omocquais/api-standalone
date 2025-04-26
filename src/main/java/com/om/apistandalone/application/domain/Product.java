package com.om.apistandalone.application.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record Product(ProductId id, String label, boolean imported, ProductType type, Price priceExcludingTax) {

    /**
     * Compute Tax Price (TTC)
     *
     * @return Price
     */
    public Price computeTax() {
        return priceExcludingTax.add(Product.aroundTax(computeValueAddedTax())).add(Product.aroundTax(computeImportTax()));
    }

    /**
     * Compute Import Tax Price.
     *
     * @return Price
     */
    public Price computeImportTax() {
        if (imported) {
            return new Price(priceExcludingTax.amount().multiply(BigDecimal.valueOf(0.05)), Currency.EURO);
        } else {
            return new Price(0);
        }
    }

    /**
     * Compute Value Added Tax (TVA)
     *
     * @return Price
     */
    public Price computeValueAddedTax() {
        switch (type) {
            case BOOKS -> {
                return new Price(priceExcludingTax.amount().multiply(BigDecimal.valueOf(0.1)), Currency.EURO);
            }
            case OTHERS -> {
                return new Price(priceExcludingTax.amount().multiply(BigDecimal.valueOf(0.2)), Currency.EURO);
            }
            case FOODS, MEDICINE -> {
                return new Price(0);
            }
            default -> throw new IllegalArgumentException((type + " is not a valid product type"));
        }
    }

    public static Price aroundTax(Price price) {
        BigDecimal increment = new BigDecimal("0.05");
        BigDecimal divided = price.amount().divide(increment, 0, RoundingMode.UP);
        return new Price(divided.multiply(increment), Currency.EURO);
    }

}
