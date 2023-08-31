package com.fetchrewards.receiptprocessor.service.points

import com.fetchrewards.receiptprocessor.model.Receipt
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

class PurchaseDayPointsCalculatorTest {

    private val calculator: PointsCalculator = PurchaseDayPointsCalculator()

    private fun createReceiptWithPurchaseDate(date: LocalDate): Receipt {
        return Receipt(
            id = UUID.randomUUID(),
            retailer = "Test Retailer",
            purchaseDate = date,
            purchaseTime = LocalTime.now(),
            total = 10.0,
            items = emptyList()
        )
    }

    @Test
    fun `should give 0 points for even purchase day`() {
        val receipt = createReceiptWithPurchaseDate(LocalDate.of(2023, 8, 30))

        val points = calculator.calculatePoints(receipt)

        assertEquals(0, points)
    }

    @Test
    fun `should give 6 points for odd purchase day`() {
        val receipt = createReceiptWithPurchaseDate(LocalDate.of(2023, 8, 31))

        val points = calculator.calculatePoints(receipt)

        assertEquals(6, points)
    }
}
