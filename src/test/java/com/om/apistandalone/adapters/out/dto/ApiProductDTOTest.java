package com.om.apistandalone.adapters.out.dto;

import com.om.apistandalone.application.domain.Product;
import com.om.apistandalone.application.domain.ProductId;
import com.om.apistandalone.application.domain.ProductType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ApiProductDTOTest {

    @Test
    void toDomain() {
        ApiProductDTO apiProductDTO = new ApiProductDTO(1, "Product 1", true, ProductType.BOOKS,
                BigDecimal.valueOf(15.15));
        Product product = apiProductDTO.toDomain();
        assertNotNull(product);
        assertThat(product.id()).isNotNull().isEqualTo(new ProductId(1));
        assertThat(product.label()).isNotNull().isEqualTo(apiProductDTO.label());
        assertThat(product.imported()).isEqualTo(apiProductDTO.imported());
        assertThat(product.type()).isEqualTo(apiProductDTO.type());
        assertThat(product.priceExcludingTax().amount()).isEqualTo(apiProductDTO.priceExcludingTax());
    }
}