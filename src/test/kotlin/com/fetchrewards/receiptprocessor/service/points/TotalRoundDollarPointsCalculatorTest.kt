package com.fetchrewards.receiptprocessor.service.points

import com.fetchrewards.receiptprocessor.model.Receipt
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

class TotalRoundDollarPointsCalculatorTest {

    private val calculator: PointsCalculator = TotalRoundDollarPointsCalculator()

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
    fun `should give 50 points if total is a round dollar value`() {
        val total = 10.0
        val receipt = createReceiptWithTotal(total)

        val points = calculator.calculatePoints(receipt)

        assertEquals(50, points)
    }

    @Test
    fun `should give 0 points if total is not a round dollar value`() {
        val total = 10.01
        val receipt = createReceiptWithTotal(total)

        val points = calculator.calculatePoints(receipt)

        assertEquals(0, points)
    }

    @Test
    fun `should give 50 points for exact 1 dollar`() {
        val total = 1.0
        val receipt = createReceiptWithTotal(total)

        val points = calculator.calculatePoints(receipt)

        assertEquals(50, points)
    }
}
