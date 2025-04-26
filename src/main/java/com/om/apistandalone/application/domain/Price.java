package com.om.apistandalone.application.domain;

import java.math.BigDecimal;
import java.util.Objects;

public record Price(BigDecimal amount, Currency currency) {

    public Price(double amount) {
        this(BigDecimal.valueOf(amount), Currency.EURO);
    }

    public Price add(Price price) {
        return new Price(price.amount().add(this.amount), currency);
    }

    public Price multiply(Price price) {
        return new Price(price.amount().multiply(this.amount), currency);
    }

    public Price multiply(double amount) {
        return new Price(this.amount.multiply(BigDecimal.valueOf(amount)), currency);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Price(BigDecimal amount1, Currency currency1))) return false;

        return this.amount.compareTo(amount1) == 0 && this.currency.equals(currency1);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount.stripTrailingZeros(), currency);
    }


}
