package com.om.apistandalone.adapters.in;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.om.apistandalone.adapters.in.dto.InvoiceDTO;
import com.om.apistandalone.adapters.in.dto.ProductDTO;
import com.om.apistandalone.adapters.in.dto.BasketRequestDTO;
import com.om.apistandalone.adapters.in.dto.ProductOrderedDTO;
import com.om.apistandalone.application.domain.*;
import com.om.apistandalone.application.ports.out.ApiPort;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class ApiStandaloneControllerTest {

    @Autowired
    MockMvcTester mockMvcTester;

    @MockitoBean
    ApiPort apiPort;

    @Nested
    class ProductReferenceList {
        @DisplayName("Given a product reference list, it should return a list of products")
        @Test
        void listProducts() {

            //Given
            Product product1 = new Product(new ProductId(1), "aLabel", true, ProductType.OTHERS, new Price(12.12));
            Product product2 = new Product(new ProductId(2), "a Book", true, ProductType.BOOKS, new Price(15.12));
            when(apiPort.getProducts()).thenReturn(List.of(product1, product2));

            //When
            assertThat(mockMvcTester.get()
                    .uri("/products")
            ).hasStatusOk()
                    .bodyJson()
                    .extractingPath("$")
                    .convertTo(InstanceOfAssertFactories.list(ProductDTO.class))
                    .isNotEmpty().hasSize(2)
                    .satisfies(productDTOS -> {

                        assertThat(productDTOS.get(0)).satisfies(productDTO -> {
                            assertThat(productDTO.id()).isEqualTo(1);
                            assertThat(productDTO.label()).isEqualTo("aLabel");
                            assertThat(productDTO.imported()).isTrue();
                            assertThat(productDTO.type()).isEqualTo(ProductType.OTHERS);
                            assertThat(productDTO.priceExcludingTax()).isEqualTo(BigDecimal.valueOf(12.12));
                            assertThat(productDTO.priceIncludingTax()).isEqualTo(BigDecimal.valueOf(15.22));
                        });

                        assertThat(productDTOS.get(1)).satisfies(productDTO -> {
                            assertThat(productDTO.id()).isEqualTo(2);
                            assertThat(productDTO.label()).isEqualTo("a Book");
                            assertThat(productDTO.imported()).isTrue();
                            assertThat(productDTO.type()).isEqualTo(ProductType.BOOKS);
                            assertThat(productDTO.priceExcludingTax()).isEqualTo(BigDecimal.valueOf(15.12));
                            assertThat(productDTO.priceIncludingTax()).isEqualTo(BigDecimal.valueOf(17.47));
                        });

                    })
            ;
            verify(apiPort).getProducts();
        }
    }

    @Nested
    class ProductById {
        @DisplayName("given an id of a product, it should return the product")
        @Test
        void getProductById() {

            List<Product> productlist = new ArrayList<>();

            //Given
            for (int i = 0; i < 10; i++) {
                productlist.add(new Product(new ProductId(i), "aLabel" + i, true, ProductType.OTHERS, new Price(12.12)));
            }

            when(apiPort.getProducts()).thenReturn(productlist);

            //When
            assertThat(mockMvcTester.get()
                    .uri("/products/1")
            ).hasStatusOk()
                    .bodyJson()
                    .extractingPath("$")
                    .convertTo(ProductDTO.class)
                    .satisfies(productDTO -> {
                        assertThat(productDTO.id()).isEqualTo(1);
                        assertThat(productDTO.label()).isEqualTo("aLabel1");
                        assertThat(productDTO.imported()).isTrue();
                        assertThat(productDTO.type()).isEqualTo(ProductType.OTHERS);
                        assertThat(productDTO.priceExcludingTax()).isEqualTo(BigDecimal.valueOf(12.12));
                        assertThat(productDTO.priceIncludingTax()).isEqualTo(BigDecimal.valueOf(15.22));
                    })
            ;
            verify(apiPort).getProducts();
        }

        @DisplayName("Given a product ID that doesn’t exist in the product reference, it should return a 404 HTTP error")
        @Test
        void getProductByBadId() {

            List<Product> productlist = new ArrayList<>();

            //Given
            for (int i = 0; i < 10; i++) {
                productlist.add(new Product(new ProductId(i), "aLabel" + i, true, ProductType.OTHERS, new Price(12.12)));
            }

            when(apiPort.getProducts()).thenReturn(productlist);

            //When
            assertThat(mockMvcTester.get().uri("/products/100")).hasStatus(HttpStatus.NOT_FOUND);

            //Then
            verify(apiPort).getProducts();
        }
    }

    @Nested
    class Basket {

        @DisplayName("When a negative quantity is provided as an input parameter, it should return a 400 HTTP error")
        @Test
        void invalidBasket() throws JsonProcessingException {
            ProductDTO productDTO = new ProductDTO(1, "a label", true, ProductType.BOOKS, BigDecimal.valueOf(12.12), BigDecimal.valueOf(124.124));
            List<ProductOrderedDTO> productOrderedDTOS = List.of(new ProductOrderedDTO(productDTO, -1));

            assertThat(mockMvcTester.post()
                    .content(new ObjectMapper().writeValueAsString(new BasketRequestDTO(productOrderedDTOS)))
                    .contentType(MediaType.APPLICATION_JSON)
                    .uri("/basket")
            ).hasStatus(HttpStatus.BAD_REQUEST);
        }

        @DisplayName("Given an empty list of products as the input parameter, it should return a 400 HTTP error")
        @Test
        void invalidBasketWithEmptyList() throws JsonProcessingException {
            assertThat(mockMvcTester.post()
                    .content(new ObjectMapper().writeValueAsString(new BasketRequestDTO(List.of())))
                    .contentType(MediaType.APPLICATION_JSON)
                    .uri("/basket")
            ).hasStatus(HttpStatus.BAD_REQUEST);
        }

        @DisplayName("Given a basket containing a list of products along with their respective quantity numbers, it " +
                "should generate an invoice that includes all the tax amounts")
        @Test
        void computeBasketAndGetInvoice() throws JsonProcessingException {

            //Given
            List<Product> productlist = buildNewProductsList();
            when(apiPort.getProducts()).thenReturn(productlist);

            Product firstProduct = productlist.getFirst();
            when(apiPort.getProductById(firstProduct.id())).thenReturn(Optional.of(firstProduct));

            Product lastProduct = productlist.getLast();
            when(apiPort.getProductById(lastProduct.id())).thenReturn(Optional.of(lastProduct));
            
            List<ProductOrderedDTO> productOrderedDTOS = List.of(buildProductOrderedDTO(firstProduct, 5), buildProductOrderedDTO(lastProduct, 2));

            //When
            String content = new ObjectMapper().writeValueAsString(new BasketRequestDTO(productOrderedDTOS));
            assertThat(mockMvcTester.post()
                    .content(content)
                    .contentType(MediaType.APPLICATION_JSON)
                    .uri("/basket")
            ).hasStatus(HttpStatus.CREATED)
                    .bodyJson()
                    .extractingPath("$")
                    .convertTo(InvoiceDTO.class)
                    .satisfies(invoiceDTO -> {
                        assertThat(invoiceDTO.uuid()).isNotNull();
                        assertThat(invoiceDTO.products()).isNotEmpty().hasSize(7);
                        assertThat(invoiceDTO.basket().totalAmountTaxes()).isEqualTo(BigDecimal.valueOf(21.70));
                        assertThat(invoiceDTO.basket().priceIncludingTax()).isEqualTo(BigDecimal.valueOf(106.54));
                    });
        }

        private static List<Product> buildNewProductsList() {
            List<Product> productlist = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                productlist.add(new Product(new ProductId(i), "aLabel" + i, true, ProductType.OTHERS, new Price(12.12)));
            }
            return productlist;
        }

        private static ProductOrderedDTO buildProductOrderedDTO(Product product, int quantity) {
            return new ProductOrderedDTO(new ProductDTO(product.id().id(), product.label(), product.imported(), product.type(),
                    product.priceExcludingTax().amount(), product.computeTax().amount()), quantity);
        }
    }


}