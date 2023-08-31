package com.fetchrewards.receiptprocessor.service.points

import com.fetchrewards.receiptprocessor.model.Receipt
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

class TotalMultipleOfQuarterPointsCalculatorTest {

    private val calculator: PointsCalculator = TotalMultipleOfQuarterPointsCalculator()

    private fun createReceiptWithTotal(total: Double): Receipt {
        return Receipt(
            id = UUID.randomUUID(),
            retailer = "Test Retailer",
            purchaseDate = LocalDate.now(),
            purchaseTime = LocalTime.now(),
            total = total,
            items = emptyList()
        )
    }

    @Test
    fun `should give 25 points if total is multiple of 0,25`() {
        val total = 5.0
        val receipt = createReceiptWithTotal(total)

        val points = calculator.calculatePoints(receipt)

        assertEquals(25, points)
    }

    @Test
    fun `should give 0 points if total is not multiple of 0,25`() {
        val total = 5.03
        val receipt = createReceiptWithTotal(total)

        val points = calculator.calculatePoints(receipt)

        assertEquals(0, points)
    }

    @Test
    fun `should give 25 points for exact multiple of 0,25`() {
        val total = 0.25
        val receipt = createReceiptWithTotal(total)

        val points = calculator.calculatePoints(receipt)

        assertEquals(25, points)
    }
}
