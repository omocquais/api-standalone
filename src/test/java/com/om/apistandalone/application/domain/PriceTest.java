package com.om.apistandalone.application.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PriceTest {

    @Test
    void add() {
        Price result = new Price(10.15).add(new Price(10.15));
        Price expected = new Price(20.30);
        assertEquals(expected, result);
    }

    @Test
    void multiply() {
        Price result = new Price(10.15).multiply(new Price(3.0));
        Price expected = new Price(30.45);
        assertEquals(expected, result);
    }

    @Test
    void multiplyBtAmountInDouble() {
        Price result = new Price(10.15).multiply(3.0);
        Price expected = new Price(30.45);
        assertEquals(expected, result);
    }
}