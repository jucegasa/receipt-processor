package com.fetchrewards.receiptprocessor.service.points

import com.fetchrewards.receiptprocessor.model.Receipt
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

class RetailerNamePointsCalculatorTest {

    private val calculator: PointsCalculator = RetailerNamePointsCalculator()

    private fun createReceiptWithRetailerName(retailerName: String): Receipt {
        return Receipt(
            id = UUID.randomUUID(),
            retailer = retailerName,
            purchaseDate = LocalDate.now(),
            purchaseTime = LocalTime.now(),
            total = 10.0,
            items = emptyList()
        )
    }

    @Test
    fun `should count letters and digits in retailer name`() {
        val retailerName = "Test123"
        val receipt = createReceiptWithRetailerName(retailerName)

        val points = calculator.calculatePoints(receipt)

        assertEquals(retailerName.count { it.isLetterOrDigit() }, points)
    }

    @Test
    fun `should not count spaces in retailer name`() {
        val retailerName = "Test Shop 123"
        val receipt = createReceiptWithRetailerName(retailerName)

        val points = calculator.calculatePoints(receipt)

        assertEquals(retailerName.count { it.isLetterOrDigit() }, points)
    }

    @Test
    fun `should not count special characters in retailer name`() {
        val retailerName = "Test-Shop_123!"
        val receipt = createReceiptWithRetailerName(retailerName)

        val points = calculator.calculatePoints(receipt)

        assertEquals(retailerName.count { it.isLetterOrDigit() }, points)
    }

}
