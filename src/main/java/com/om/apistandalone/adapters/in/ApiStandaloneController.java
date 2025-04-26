package com.om.apistandalone.adapters.in;

import com.om.apistandalone.adapters.in.dto.BasketDTO;
import com.om.apistandalone.adapters.in.dto.BasketRequestDTO;
import com.om.apistandalone.adapters.in.dto.InvoiceDTO;
import com.om.apistandalone.adapters.in.dto.ProductDTO;
import com.om.apistandalone.application.domain.InvoiceDetails;
import com.om.apistandalone.application.domain.Product;
import com.om.apistandalone.application.domain.ProductId;
import com.om.apistandalone.application.domain.ProductOrdered;
import com.om.apistandalone.application.ports.in.ProductInPort;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
public class ApiStandaloneController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiStandaloneController.class);

    private final ProductInPort productInPort;

    public ApiStandaloneController(ProductInPort productInPort) {
        this.productInPort = productInPort;
    }

    @GetMapping("/products")
    public List<ProductDTO> products() {

        LOGGER.info("Retrieving all products - start");

        List<ProductDTO> productDTOList = productInPort.getProducts().stream().map(product -> new ProductDTO(
                product.id().id(),
                product.label(),
                product.imported(),
                product.type(),
                product.priceExcludingTax().amount(),
                product.computeTax().amount()
        )).toList();

        LOGGER.info("Retrieving all products - end: {}", productDTOList);

        return productDTOList;
    }


    @GetMapping("/products/{productId}")
    public ResponseEntity<ProductDTO> product(@PathVariable Integer productId) {

        LOGGER.info("Retrieving a product by id - start: {}", productId);

        Optional<Product> optProduct = productInPort.getProducts().stream()
                .filter(product -> product.id().id().equals(productId))
                .findFirst();
        if (optProduct.isPresent()) {
            Product product = optProduct.get();

            LOGGER.info("Retrieving products: {}", product);

            ProductDTO productDTO = new ProductDTO(product.id().id(),
                    product.label(),
                    product.imported(),
                    product.type(),
                    product.priceExcludingTax().amount(),
                    product.computeTax().amount());

            LOGGER.info("Retrieving a product by id - end: {}", productDTO);

            return ResponseEntity.ok().body(productDTO);

        } else {
            LOGGER.info("No product found with id: {}", productId);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/basket")
    public ResponseEntity<InvoiceDTO> basket(@RequestBody @Valid BasketRequestDTO basketRequestDTO) {

        LOGGER.info("Processing a basket request - start: {}", basketRequestDTO);

        List<ProductOrdered> productOrderedList =
                basketRequestDTO.productOrderedDTOS().stream().map(productOrderedDTO -> new ProductOrdered(
                        new ProductId(productOrderedDTO.productDTO().id()), productOrderedDTO.quantity())
                ).toList();

        InvoiceDetails invoiceDetails = productInPort.getInvoiceDetails(productOrderedList);

        BasketDTO basketDTO = new BasketDTO(invoiceDetails.basketPriceExcludingTax().amount(),
                invoiceDetails.basketPriceIncludingTax().amount());

        List<ProductDTO> products =
                invoiceDetails.products().stream().map(product -> new ProductDTO(product.id().id(), product.label(),
                        product.imported(), product.type(), product.priceExcludingTax().amount(),
                        product.computeTax().amount())).toList();

        InvoiceDTO invoiceDTO = new InvoiceDTO(UUID.randomUUID(), products, basketDTO);

        LOGGER.info("Processing a basket request - end: {}", invoiceDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(invoiceDTO);
    }


}