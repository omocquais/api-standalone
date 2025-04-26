package com.om.apistandalone.application.ports.in;

import com.om.apistandalone.application.domain.InvoiceDetails;
import com.om.apistandalone.application.domain.Product;
import com.om.apistandalone.application.domain.ProductOrdered;

import java.util.List;

public interface ProductInPort {
    List<Product> getProducts();

    InvoiceDetails getInvoiceDetails(List<ProductOrdered> productOrdereds);

}
