package com.om.apistandalone.adapters.out;

import com.om.apistandalone.adapters.out.dto.ApiProductDTO;
import com.om.apistandalone.application.domain.ProductType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

import java.math.BigDecimal;
import java.nio.file.Paths;
import java.util.List;

import static java.nio.file.Files.readString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;


@RestClientTest(ApiClient.class)
class ApiClientTest {

    @Autowired
    private ApiClient apiClient;

    @Autowired
    private MockRestServiceServer server;

    @DisplayName("Given an external API that provides a list of products, it should return a list of ApiProductDTO containing all the information extracted from the API")
    @Test
    void getProductsFromAPI() throws Exception {

        //Given
        String json = readString(Paths.get("src", "test", "resources", "products.json"));
        this.server.expect(requestTo("http://localhost:9999/api/products"))
                .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

        //When
        List<ApiProductDTO> productList = apiClient.getProducts();

        //Then
        assertThat(productList)
                .isNotNull()
                .isNotEmpty()
                .hasSize(10)
                .satisfies(
                        apiProductDTOS -> {

                            assertThat(apiProductDTOS.get(0)).satisfies(productDTO -> {
                                assertThat(productDTO.id()).isEqualTo(1);
                                assertThat(productDTO.label()).isEqualTo("Product 1");
                                assertThat(productDTO.imported()).isTrue();
                                assertThat(productDTO.type()).isEqualTo(ProductType.FOODS);
                                assertThat(productDTO.priceExcludingTax()).isEqualTo(BigDecimal.valueOf(15.12));
                            });

                            assertThat(apiProductDTOS.get(1)).satisfies(productDTO -> {
                                assertThat(productDTO.id()).isEqualTo(2);
                                assertThat(productDTO.label()).isEqualTo("Product 2");
                                assertThat(productDTO.imported()).isFalse();
                                assertThat(productDTO.type()).isEqualTo(ProductType.BOOKS);
                                assertThat(productDTO.priceExcludingTax()).isEqualTo(BigDecimal.valueOf(25.45));
                            });

                            assertThat(apiProductDTOS.get(2)).satisfies(productDTO -> {
                                assertThat(productDTO.id()).isEqualTo(3);
                                assertThat(productDTO.label()).isEqualTo("Product 3");
                                assertThat(productDTO.imported()).isTrue();
                                assertThat(productDTO.type()).isEqualTo(ProductType.FOODS);
                                assertThat(productDTO.priceExcludingTax()).isEqualTo(BigDecimal.valueOf(52.12));
                            });

                            assertThat(apiProductDTOS.get(3)).satisfies(productDTO -> {
                                assertThat(productDTO.id()).isEqualTo(4);
                                assertThat(productDTO.label()).isEqualTo("Product 4");
                                assertThat(productDTO.imported()).isTrue();
                                assertThat(productDTO.type()).isEqualTo(ProductType.MEDICINE);
                                assertThat(productDTO.priceExcludingTax()).isEqualTo(BigDecimal.valueOf(32.45));
                            });

                        }
                )
        ;
    }

    @DisplayName("Given an external API that returns a server error, it should return an empty list of ApiProductDTO")
    @Test
    void emptyListOfProduct() {
        //Given
        this.server.expect(requestTo("http://localhost:9999/api/products")).andRespond(withServerError());

        //When
        List<ApiProductDTO> productList = apiClient.getProducts();

        //Then
        assertThat(productList).isNotNull().isEmpty();
    }
}