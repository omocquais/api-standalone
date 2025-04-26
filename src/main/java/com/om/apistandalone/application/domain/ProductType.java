package com.om.apistandalone.application.domain;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ProductType {

    FOODS("nourriture"),
    MEDICINE("médicaments"),
    BOOKS("livres"),
    OTHERS("autres"),
    ;

    ProductType(String label) {
        this.label = label;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }

    private final String label;
}
