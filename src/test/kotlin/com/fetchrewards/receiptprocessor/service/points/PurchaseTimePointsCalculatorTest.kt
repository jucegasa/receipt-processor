package com.fetchrewards.receiptprocessor.service.points

import com.fetchrewards.receiptprocessor.model.Receipt
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

class PurchaseTimePointsCalculatorTest {

    private val calculator: PointsCalculator = PurchaseTimePointsCalculator()

    private fun createReceiptWithPurchaseTime(time: LocalTime): Receipt {
        return Receipt(
            id = UUID.randomUUID(),
            retailer = "Test Retailer",
            purchaseDate = LocalDate.now(),
            purchaseTime = time,
            total = 10.0,
            items = emptyList()
        )
    }

    @Test
    fun `should give 0 points for purchase time before 14 00`() {
        val receipt = createReceiptWithPurchaseTime(LocalTime.of(13, 59))

        val points = calculator.calculatePoints(receipt)

        assertEquals(0, points)
    }

    @Test
    fun `should give 10 points for purchase time just after 14 00`() {
        val receipt = createReceiptWithPurchaseTime(LocalTime.of(14, 1))

        val points = calculator.calculatePoints(receipt)

        assertEquals(10, points)
    }

    @Test
    fun `should give 0 points for purchase time at 16 00`() {
        val receipt = createReceiptWithPurchaseTime(LocalTime.of(16, 0))

        val points = calculator.calculatePoints(receipt)

        assertEquals(0, points)
    }

    @Test
    fun `should give 10 points for purchase time just before 16 00`() {
        val receipt = createReceiptWithPurchaseTime(LocalTime.of(15, 59))

        val points = calculator.calculatePoints(receipt)

        assertEquals(10, points)
    }
}
