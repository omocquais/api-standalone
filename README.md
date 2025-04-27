# API Standalone  - Getting Started

# Prerequisites
- Maven
- Java 21
- JQ - https://jqlang.org/download/

# Start the Mock API (provided by Wiremock)

```shell
  make start-api-mock
```

# Package the Spring Boot Application

```shell
  make build
```

# Start the application

```shell
  make start
```

# Get the products reference

```shell
  make products-list
```

## Expected Output

```json
  [
  {
    "id": 1,
    "label": "Product 1",
    "imported": true,
    "type": "nourriture",
    "priceExcludingTax": 15.12,
    "priceIncludingTax": 15.92
  },
  {
    "id": 2,
    "label": "Product 2",
    "imported": false,
    "type": "livres",
    "priceExcludingTax": 25.45,
    "priceIncludingTax": 28.00
  },
  {
    "id": 3,
    "label": "Product 3",
    "imported": true,
    "type": "nourriture",
    "priceExcludingTax": 52.12,
    "priceIncludingTax": 54.77
  },
  {
    "id": 4,
    "label": "Product 4",
    "imported": true,
    "type": "médicaments",
    "priceExcludingTax": 32.45,
    "priceIncludingTax": 34.10
  },
  {
    "id": 5,
    "label": "Product 5",
    "imported": true,
    "type": "nourriture",
    "priceExcludingTax": 15.12,
    "priceIncludingTax": 15.92
  },
  {
    "id": 6,
    "label": "Product 6",
    "imported": false,
    "type": "livres",
    "priceExcludingTax": 25.45,
    "priceIncludingTax": 28.00
  },
  {
    "id": 7,
    "label": "Product 7",
    "imported": true,
    "type": "nourriture",
    "priceExcludingTax": 41.22,
    "priceIncludingTax": 43.32
  },
  {
    "id": 8,
    "label": "Product 8",
    "imported": true,
    "type": "médicaments",
    "priceExcludingTax": 85.12,
    "priceIncludingTax": 89.42
  },
  {
    "id": 9,
    "label": "Product 9",
    "imported": true,
    "type": "nourriture",
    "priceExcludingTax": 41.22,
    "priceIncludingTax": 43.32
  },
  {
    "id": 10,
    "label": "Product 10",
    "imported": false,
    "type": "médicaments",
    "priceExcludingTax": 15.14,
    "priceIncludingTax": 15.14
  }
]
```

# Get product detail

```shell
  make product
```

## Expected Output

```json
{
  "id": 3,
  "label": "Product 3",
  "imported": true,
  "type": "nourriture",
  "priceExcludingTax": 52.12,
  "priceIncludingTax": 54.77
}
```

# Send Basket to API Standalone

```shell
  make basket
```

## Expected Output

```json
{
  "uuid": "07ee8398-8ad3-4a42-a98b-d05585d60bd8",
  "products": [
    {
      "id": 5,
      "label": "Product 5",
      "imported": true,
      "type": "nourriture",
      "priceExcludingTax": 15.12,
      "priceIncludingTax": 15.92
    },
    {
      "id": 5,
      "label": "Product 5",
      "imported": true,
      "type": "nourriture",
      "priceExcludingTax": 15.12,
      "priceIncludingTax": 15.92
    },
    {
      "id": 5,
      "label": "Product 5",
      "imported": true,
      "type": "nourriture",
      "priceExcludingTax": 15.12,
      "priceIncludingTax": 15.92
    },
    {
      "id": 5,
      "label": "Product 5",
      "imported": true,
      "type": "nourriture",
      "priceExcludingTax": 15.12,
      "priceIncludingTax": 15.92
    },
    {
      "id": 5,
      "label": "Product 5",
      "imported": true,
      "type": "nourriture",
      "priceExcludingTax": 15.12,
      "priceIncludingTax": 15.92
    },
    {
      "id": 6,
      "label": "Product 6",
      "imported": false,
      "type": "livres",
      "priceExcludingTax": 25.45,
      "priceIncludingTax": 28.00
    },
    {
      "id": 6,
      "label": "Product 6",
      "imported": false,
      "type": "livres",
      "priceExcludingTax": 25.45,
      "priceIncludingTax": 28.00
    }
  ],
  "basket": {
    "totalAmountTaxes": 9.10,
    "priceIncludingTax": 135.60
  }
}
```
