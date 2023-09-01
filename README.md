# Receipt Processor
Technical Exercise for Fetch

## Description

This service process the receipts

### Features

- Create receipts 
- Process the points of a receipt by id

### Built with

- [Kotlin](https://kotlinlang.org/)
- [Gradle](https://gradle.org/)
- [Spring Boot](https://spring.io/projects/spring-boot)
- [H2](https://www.h2database.com/html/main.html)

## Getting started

### Prerequisites

- Docker
- Make (Optional)

### Install

To install this project in the project's root you can use:
```shell
make build
```
Or the other hand you can run:
```shell
docker build -t receipt-processor
```

### Run

To run you choose between option 1 or option 2:
1. Run Makefile:
```shell
make run
```
2. Run docker image:
```shell
docker run --rm -p 8080:8080 --name fetch-container receipt-processor
```
### Recommendation
If you want to skip build and run, you can use only one command:
```shell
make all
```
This command builds and runs the application in a single step.

### Run Tests

To run test:
```shell
make test
```

Or

```shell
docker build -t receipt-processor-test -f Dockerfile-test .
docker run --rm --name fetch-test-container receipt-processor-test
```

### Usage

#### Test health:
```shell
curl --location --request GET 'http://localhost:8080/actuator/health'
```

#### Try the endpoints:
You can use the [swagger-ui](http://localhost:8080/swagger-ui.html) or do it manually:

- Endpoint to create a new receipt process:
```shell
curl --location 'localhost:8080/receipts/process' \
--header 'Content-Type: application/json' \
--data '{
    "retailer": "Target",
    "purchaseDate": "2023-08-31",
    "purchaseTime": "23:53",
    "total": 1.25,
    "items": [
        {"shortDescription": "Pepsi - 12-oz", "price": "1.25"}
    ]
}'
```
Example success response:
```json
{
    "id": "c1a59354-ce40-473e-bbc5-0c0679984f53"
}
```

- Endpoint to get the points of a receipt by id:
```shell
curl --location 'localhost:8080/receipts/c1a59354-ce40-473e-bbc5-0c0679984f53/points'
```
Example success response:
```json
{
    "points": 37
}
```

## Improvements

- [ ] Span Id for logs
- [ ] Add specific logs for tracking and monitoring
- [ ] Caching by id because the receipt not change
- [ ] Authentication and Authorization