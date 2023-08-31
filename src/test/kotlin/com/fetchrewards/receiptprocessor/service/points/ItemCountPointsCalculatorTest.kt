package com.fetchrewards.receiptprocessor.service.points

import com.fetchrewards.receiptprocessor.model.Receipt
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

class ItemCountPointsCalculatorTest {

    private val calculator: PointsCalculator = ItemCountPointsCalculator()

    private fun createReceiptWithItems(itemsCount: Int): Receipt {
        val items = List(itemsCount) {
            Receipt.Item("Item ${it + 1}", (it + 1).toDouble())
        }
        return Receipt(
            id = UUID.randomUUID(),
            retailer = "Test Retailer",
            purchaseDate = LocalDate.now(),
            purchaseTime = LocalTime.now(),
            total = items.sumOf { it.price },
            items = items
        )
    }

    @Test
    fun `should return 0 points when receipt has no items`() {
        val receipt = createReceiptWithItems(0)

        val points = calculator.calculatePoints(receipt)

        assertEquals(0, points)
    }

    @Test
    fun `should return 5 points when receipt has 2 items`() {
        val receipt = createReceiptWithItems(2)

        val points = calculator.calculatePoints(receipt)

        assertEquals(5, points)
    }

    @Test
    fun `should return 5 points when receipt has 3 items`() {
        val receipt = createReceiptWithItems(3)

        val points = calculator.calculatePoints(receipt)

        assertEquals(5, points)
    }

    @Test
    fun `should return 25 points when receipt has 10 items`() {
        val receipt = createReceiptWithItems(10)

        val points = calculator.calculatePoints(receipt)

        assertEquals(25, points)
    }
}