package com.om.apistandalone.application.services;

import com.om.apistandalone.application.domain.*;
import com.om.apistandalone.application.domain.Currency;
import com.om.apistandalone.application.ports.out.ApiPort;
import com.om.apistandalone.application.ports.in.ProductInPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductService implements ProductInPort {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductService.class);
    public static final String MSG_EXCEPTION_PRODUCT_ID_NOT_FOUND = "Product Id not found: ";

    private final ApiPort apiPort;

    public ProductService(ApiPort apiPort) {
        this.apiPort = apiPort;
    }

    public List<Product> getProducts() {
        return apiPort.getProducts();
    }

    public InvoiceDetails getInvoiceDetails(List<ProductOrdered> productOrderedList) {

        LOGGER.info("Get invoice details - start: {}", productOrderedList);

        List<BigDecimal> numbersIncludingTax =
                productOrderedList.stream().map(productOrdered -> apiPort.getProductById(productOrdered.product()).map(product -> product.computeTax().amount().multiply(BigDecimal.valueOf(productOrdered.quantity()))).orElseThrow(() -> new IllegalArgumentException(MSG_EXCEPTION_PRODUCT_ID_NOT_FOUND + productOrdered.product().id()))).toList();
        List<BigDecimal> numbersExcludingTax =
                productOrderedList.stream().map(productOrdered -> apiPort.getProductById(productOrdered.product()).map(product -> product.priceExcludingTax().amount().multiply(BigDecimal.valueOf(productOrdered.quantity()))).orElseThrow(() -> new IllegalArgumentException(MSG_EXCEPTION_PRODUCT_ID_NOT_FOUND + productOrdered.product().id()))).toList();

        Price basketPriceIncludingTax = new Price(numbersIncludingTax.stream().reduce(BigDecimal.ZERO, BigDecimal::add), Currency.EURO);
        Price basketPriceExcludingTax = new Price(numbersExcludingTax.stream().reduce(BigDecimal.ZERO, BigDecimal::add), Currency.EURO);

        Map<Integer, Product> mapProductIdToProduct = productOrderedList.stream().map(productOrdered -> apiPort.getProductById(productOrdered.product()).orElseThrow(() -> new IllegalArgumentException(MSG_EXCEPTION_PRODUCT_ID_NOT_FOUND + productOrdered.product().id())))
                .collect(Collectors.toMap(product -> product.id().id(), product -> product));

        List<Product> productsList = new ArrayList<>();

        for (ProductOrdered productOrdered : productOrderedList) {
            Product product = mapProductIdToProduct.get(productOrdered.product().id());
            if (product != null) {
                productsList.addAll(Collections.nCopies(productOrdered.quantity(), product));
            }
        }

        InvoiceDetails invoiceDetails = new InvoiceDetails(productsList, basketPriceExcludingTax, basketPriceIncludingTax);

        LOGGER.info("Get invoice details - end: {}", invoiceDetails);

        return invoiceDetails;

    }
}
