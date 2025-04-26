package com.om.apistandalone.adapters.in.dto;

import com.om.apistandalone.application.domain.ProductType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductTypeTest {

    @DisplayName("it should return the name attribute of the enum for a product type")
    @Test
    void nme() {
        assertEquals("autres", ProductType.OTHERS.getLabel());
        assertEquals("nourriture", ProductType.FOODS.getLabel());
        assertEquals("livres", ProductType.BOOKS.getLabel());
        assertEquals("autres", ProductType.OTHERS.getLabel());
    }
}