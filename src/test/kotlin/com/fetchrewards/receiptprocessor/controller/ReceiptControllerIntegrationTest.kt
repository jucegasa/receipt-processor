package com.fetchrewards.receiptprocessor.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fetchrewards.receiptprocessor.model.Receipt
import com.fetchrewards.receiptprocessor.repository.ReceiptRepository
import jakarta.validation.Validator
import jakarta.validation.constraints.PastOrPresent
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ReceiptControllerIntegrationTest(@Autowired val restTemplate: TestRestTemplate) {

    @Autowired
    private lateinit var receiptRepository: ReceiptRepository

    @Autowired
    private lateinit var validator: Validator

    @Test
    fun `should process receipt and return id`() {
        val request = Receipt(
            retailer = "Target",
            purchaseDate = LocalDate.parse("2022-01-01"),
            purchaseTime = LocalTime.parse("13:01"),
            total = 35.35,
            items = listOf(
                Receipt.Item("Mountain Dew 12PK", 6.49),
                Receipt.Item("Emils Cheese Pizza", 12.25)
            )
        )

        val responseEntity = restTemplate.postForEntity<Map<String, String>>("/receipts/process", request, Map::class.java)
        assertEquals(HttpStatus.CREATED, responseEntity.statusCode)
        assertTrue(responseEntity.body?.containsKey("id") == true)
    }

    @Test
    fun `should fetch points for a given receipt id`() {
        val receipt = Receipt(
            retailer = "M&M Corner Market",
            purchaseDate = LocalDate.parse("2022-03-20"),
            purchaseTime = LocalTime.parse("14:33"),
            total = 9.00,
            items = listOf(
                Receipt.Item(shortDescription = "Gatorade", price = 2.25),
                Receipt.Item(shortDescription = "Gatorade", price = 2.25),
                Receipt.Item(shortDescription = "Gatorade", price = 2.25),
                Receipt.Item(shortDescription = "Gatorade", price = 2.25)
            )
        )

        val savedReceipt = receiptRepository.save(receipt)

        val responseEntity = restTemplate.getForEntity<String>("/receipts/${savedReceipt.id}/points")

        assertThat(responseEntity.statusCode).isEqualTo(HttpStatus.OK)
        val responseBody = jacksonObjectMapper().readTree(responseEntity.body)
        assertThat(responseBody["points"].intValue()).isEqualTo(109)
    }

    @Test
    fun `should return 404 error for a given random id`() {
        val id = UUID.randomUUID()
        val responseEntity = restTemplate.getForEntity<Map<String, Any>>("/receipts/$id/points")

        assertThat(responseEntity.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
        val actualMessage = responseEntity.body?.get("message")?.toString().orEmpty()
        val expectedMessage = "Receipt with ID $id not found."
        assertEquals(expectedMessage, actualMessage)
    }

    @Test
    fun `should return error for invalid date`() {
        val request = mapOf(
            "retailer" to "Target",
            "purchaseDate" to "2022-01-35",
            "purchaseTime" to "13:01",
            "total" to 35.35,
            "items" to listOf(
                mapOf("shortDescription" to "Mountain Dew 12PK", "price" to 6.49),
                mapOf("shortDescription" to "Emils Cheese Pizza", "price" to 12.25)
            )
        )

        val responseEntity = restTemplate.postForEntity<Map<String, Any>>("/receipts/process", request, Map::class.java)
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.statusCode)
    }

    @Test
    fun `should return error for future date`() {
        val future = LocalDate.now().plusDays(1)
        val request = mapOf(
            "retailer" to "Target",
            "purchaseDate" to future.toString(),
            "purchaseTime" to "13:01",
            "total" to 35.35,
            "items" to listOf(
                mapOf("shortDescription" to "Mountain Dew 12PK", "price" to 6.49),
                mapOf("shortDescription" to "Emils Cheese Pizza", "price" to 12.25)
            )
        )

        val responseEntity = restTemplate.postForEntity<Map<String, Any>>("/receipts/process", request, Map::class.java)
        val receipt = Receipt(purchaseDate = future, retailer = "Target", purchaseTime = LocalTime.parse("13:01"), total = 35.35, items = listOf())
        val errorMessage = responseEntity.body?.get("purchaseDate")?.toString().orEmpty()
        val violations = validator.validate(receipt)
        val expectedMessage = violations.first { it.constraintDescriptor.annotation is PastOrPresent }.message

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.statusCode)
        assertEquals(errorMessage, expectedMessage)
    }

}
