package com.fetchrewards.receiptprocessor.service.points

import com.fetchrewards.receiptprocessor.model.Receipt
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalTime
import java.util.*
import kotlin.math.ceil

class ItemDescriptionPointsCalculatorTest {

    private val calculator: PointsCalculator = ItemDescriptionPointsCalculator()

    private fun createReceiptWithItem(description: String, price: Double): Receipt {
        return Receipt(
            id = UUID.randomUUID(),
            retailer = "Test Retailer",
            purchaseDate = LocalDate.now(),
            purchaseTime = LocalTime.now(),
            total = price,
            items = listOf(Receipt.Item(description, price))
        )
    }

    @Test
    fun `should give 0 points for item with non-divisible by 3 description length`() {
        val receipt = createReceiptWithItem("12", 5.0)

        val points = calculator.calculatePoints(receipt)

        assertEquals(0, points)
    }

    @Test
    fun `should give points for item with divisible by 3 description length`() {
        val receipt = createReceiptWithItem("123", 10.0)

        val points = calculator.calculatePoints(receipt)

        val expectedPoints = ceil(10.0 * 0.2).toInt()
        assertEquals(expectedPoints, points)
    }

    @Test
    fun `should consider trimmed description for calculation`() {
        val receipt = createReceiptWithItem(" 123 ", 10.0)

        val points = calculator.calculatePoints(receipt)

        val expectedPoints = ceil(10.0 * 0.2).toInt()
        assertEquals(expectedPoints, points)
    }

}