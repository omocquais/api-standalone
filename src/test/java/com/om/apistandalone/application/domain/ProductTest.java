package com.om.apistandalone.application.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProductTest {

    @Nested
    class AroundTax {
        @Test
        void aroundTax095() {
            assertThat(Product.aroundTax(new Price(0.95))).isEqualTo(new Price(0.95));
        }
        @Test
        void aroundTax099() {
            assertThat(Product.aroundTax(new Price(0.99))).isEqualTo(new Price(1));
        }
        @Test
        void aroundTax1() {
            assertThat(Product.aroundTax(new Price(1))).isEqualTo(new Price(1));
        }

        @Test
        void aroundTax101() {
            assertThat(Product.aroundTax(new Price(1.01))).isEqualTo(new Price(1.05));
        }
        @Test
        void aroundTax102() {
            assertThat(Product.aroundTax( new Price(1.02))).isEqualTo(new Price(1.05));
        }
    }

    @Nested
    class ImportTax {

        @DisplayName("It should compute the imported tax price for a product that has been imported")
        @Test
        void computeImportedTaxImportedProduct() {
            Product product = new Product(new ProductId(1), "Product 1", true, ProductType.BOOKS, new Price(10.0));
            assertThat(product.computeImportTax()).isEqualTo(new Price(0.50));
        }

        @DisplayName("It should compute the imported tax price for a product that has NOT been imported")
        @Test
        void computeImportedTaxNoImportedProduct() {
            Product product = new Product(new ProductId(1), "Product 1", false, ProductType.BOOKS, new Price(10.0));
            assertThat(product.computeImportTax()).isEqualTo(new Price(0));
        }

    }

    @Nested
    class ValueAddedTax {

        @DisplayName("It should compute the value added tax (TVA) for a BOOK product")
        @Test
        void computeValueAddedTaxBookProduct() {
            Product product = new Product(new ProductId(1), "Product 1", true, ProductType.BOOKS, new Price(10.0));
            assertThat(product.computeValueAddedTax()).isEqualTo(new Price(1));
        }

        @DisplayName("It should compute the value added tax (TVA) for an OTHER product")
        @Test
        void computeValueAddedTaxOthersProduct() {
            Product product = new Product(new ProductId(1), "Product 1", true, ProductType.OTHERS, new Price(10.0));
            assertThat(product.computeValueAddedTax()).isEqualTo(new Price(2));
        }

        @DisplayName("It should compute the value added tax (TVA) for a FOOD product")
        @Test
        void computeValueAddedTaxFoodsProduct() {
            Product product = new Product(new ProductId(1), "Product 1", true, ProductType.FOODS, new Price(10.0));
            assertThat(product.computeValueAddedTax()).isEqualTo(new Price(0));
        }

        @DisplayName("It should compute the value added tax (TVA) for a MEDICINE product")
        @Test
        void computeValueAddedTaxMedecineProduct() {
            Product product = new Product(new ProductId(1), "Product 1", true, ProductType.MEDICINE, new Price(10.0));
            assertThat(product.computeValueAddedTax()).isEqualTo(new Price(0));
        }

    }

    @Nested
    class TotalAmountTaxOnImportProducts {
        @Test
        void computeTaxMedicineImported() {
            Product product = new Product(new ProductId(1), "product 1", true, ProductType.MEDICINE, new Price(10.0));
            assertThat(product.computeTax()).isNotNull().isEqualTo(new Price(10.5));
        }

        @Test
        void computeTaxMedicineImportedWithAroundTax() {
            Product product = new Product(new ProductId(1), "product 1", true, ProductType.MEDICINE, new Price(99.99));
            assertThat(product.computeTax()).isNotNull().isEqualTo(new Price(104.99));
        }

        @Test
        void computeTaxFoodsImported() {
            Product product = new Product(new ProductId(1), "product 1", true, ProductType.FOODS, new Price(10.0));
            assertThat(product.computeTax()).isNotNull().isEqualTo(new Price(10.5));
        }

        @Test
        void computeTaxImportedBooks() {
            Product product = new Product(new ProductId(1), "product 1", true, ProductType.BOOKS, new Price(10.0));
            assertThat(product.computeTax()).isNotNull().isEqualTo(new Price(11.5));
        }

        @Test
        void computeTaxOtherImported() {
            Product product = new Product(new ProductId(1), "product 1", true, ProductType.OTHERS, new Price(10.0));
            assertThat(product.computeTax()).isNotNull().isEqualTo(new Price(12.5));
        }
    }

    @Nested
    class TotalAmountTaxOnNoImportProducts {
        @Test
        void computeTaxMedicineImported() {
            Product product = new Product(new ProductId(1), "product 1", false, ProductType.MEDICINE, new Price(10.0));
            assertThat(product.computeTax()).isNotNull().isEqualTo(new Price(10));
        }
        @Test
        void computeTaxFoodsImported() {
            Product product = new Product(new ProductId(1), "product 1", false, ProductType.FOODS, new Price(10.0));
            assertThat(product.computeTax()).isNotNull().isEqualTo(new Price(10));
        }

        @Test
        void computeTaxImportedBooks() {
            Product product = new Product(new ProductId(1), "product 1", false, ProductType.BOOKS, new Price(10.0));
            assertThat(product.computeTax()).isNotNull().isEqualTo(new Price(11));
        }

        @Test
        void computeTaxOtherImported() {
            Product product = new Product(new ProductId(1), "product 1", false, ProductType.OTHERS, new Price(10.0));
            assertThat(product.computeTax()).isNotNull().isEqualTo(new Price(12));
        }
    }
    
}