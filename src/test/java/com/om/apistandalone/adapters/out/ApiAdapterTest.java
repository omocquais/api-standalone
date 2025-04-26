package com.om.apistandalone.adapters.out;

import com.om.apistandalone.adapters.out.dto.ApiProductDTO;
import com.om.apistandalone.application.domain.Product;
import com.om.apistandalone.application.domain.ProductId;
import com.om.apistandalone.application.domain.ProductType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApiAdapterTest {

    @Mock
    ApiClient apiClient;

    @Test
    void getProductById() {

        //Given
        ApiProductDTO apiProductDTO1 = new ApiProductDTO(1, "a Book Label", true, ProductType.BOOKS,
                BigDecimal.valueOf(10.12));
        ApiProductDTO apiProductDTO2 = new ApiProductDTO(2, "a Food Label", true, ProductType.FOODS,
                BigDecimal.valueOf(20.12));
        ApiProductDTO apiProductDTO3 = new ApiProductDTO(3, "an Other Label", true, ProductType.OTHERS,
                BigDecimal.valueOf(30.12));
        List<ApiProductDTO> productList = List.of(apiProductDTO1, apiProductDTO2, apiProductDTO3);

        when(apiClient.getProducts()).thenReturn(productList);

        //When
        Optional<Product> optProduct = new ApiAdapter(apiClient).getProductById(new ProductId(2));

        assertThat(optProduct).isPresent();

        Product product = optProduct.get();
        assertThat(product).isEqualTo(productList.get(1).toDomain());
    }

    @Test
    void getProducts() {
        //Given
        List<ApiProductDTO> productList = List.of(new ApiProductDTO(1, "aLabel", true, ProductType.BOOKS,
                BigDecimal.valueOf(12.12)));

        when(apiClient.getProducts()).thenReturn(productList);

        //When
        List<Product> products = new ApiAdapter(apiClient).getProducts();

        //Then
        verify(apiClient).getProducts();
        assertThat(products).isNotNull().isNotEmpty().hasSize(productList.size());
    }
}