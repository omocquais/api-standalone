package com.om.apistandalone.application.services;

import com.om.apistandalone.application.domain.*;
import com.om.apistandalone.application.ports.out.ApiPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    ApiPort apiPort;

    @DisplayName("it should call the api to get the products list")
    @Test
    void getProducts() {

        //Given
        Product product1 = new Product(new ProductId(1), "aSpecificLabel", true, ProductType.BOOKS, new Price(14.15));
        Product product2 = new Product(new ProductId(2), "anotherSpecificLabel", true, ProductType.FOODS, new Price(6.12));

        when(apiPort.getProducts()).thenReturn(List.of(product1, product2));

        //When
        List<Product> products = new ProductService(apiPort).getProducts();

        //Then
        assertThat(products).isNotNull();
        verify(apiPort).getProducts();
        assertThat(products).isNotNull().isNotEmpty().hasSize(2);
    }

    @Test
    void getInvoiceDetails() {

        //Given
        Product product1 = new Product(new ProductId(1), "aSpecificLabel", true, ProductType.BOOKS, new Price(14.15));
        Product product2 = new Product(new ProductId(2), "anotherSpecificLabel", true, ProductType.FOODS, new Price(6.12));

        when(apiPort.getProductById(new ProductId(1))).thenReturn(Optional.of(product1));
        when(apiPort.getProductById(new ProductId(2))).thenReturn(Optional.of(product2));

        ProductOrdered productOrdered1 = new ProductOrdered(new ProductId(1), 5);
        ProductOrdered productOrdered2 = new ProductOrdered(new ProductId(2), 10);

        List<ProductOrdered> productOrderedList = List.of(productOrdered1, productOrdered2);

        InvoiceDetails invoiceDetails = new ProductService(apiPort).getInvoiceDetails(productOrderedList);

        assertThat(invoiceDetails).isNotNull();
        assertThat(invoiceDetails.products()).isNotNull().isNotEmpty().hasSize(15).containsOnly(product1, product2);

        assertThat(invoiceDetails.basketPriceIncludingTax()).isNotNull().isEqualTo(new Price(146.45));
        assertThat(invoiceDetails.basketPriceExcludingTax()).isNotNull().isEqualTo(new Price(131.95));
    }
    @Test
    void getInvoiceDetailsInvalidProductId() {
        Product product1 = new Product(new ProductId(1), "aSpecificLabel", true, ProductType.BOOKS, new Price(14.15));
        
        when(apiPort.getProductById(new ProductId(1))).thenReturn(Optional.of(product1));
        when(apiPort.getProductById(new ProductId(2))).thenReturn(Optional.empty());

        ProductOrdered productOrdered1 = new ProductOrdered(new ProductId(1), 5);
        ProductOrdered productOrdered2 = new ProductOrdered(new ProductId(2), 10);

        assertThrows(IllegalArgumentException.class, () -> new ProductService(apiPort).getInvoiceDetails(List.of(productOrdered1, productOrdered2)));
    }
}