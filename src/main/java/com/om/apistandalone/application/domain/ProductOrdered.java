package com.om.apistandalone.application.domain;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ProductOrdered(@NotNull ProductId product, @Min(value = 1) Integer quantity) {
}
