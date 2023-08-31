package com.fetchrewards.receiptprocessor.service.points

import com.fetchrewards.receiptprocessor.model.Receipt
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

class CombinedPointsCalculatorTest {

    @Test
    fun `should combine the results of all calculators`() {
        val mockCalculator1 = mockk<PointsCalculator>()
        val mockCalculator2 = mockk<PointsCalculator>()

        every { mockCalculator1.calculatePoints(any()) } returns 10
        every { mockCalculator2.calculatePoints(any()) } returns 20

        val combinedCalculator = CombinedPointsCalculator(listOf(mockCalculator1, mockCalculator2))

        val testReceipt = Receipt(
            id = UUID.randomUUID(),
            retailer = "Test Retailer",
            purchaseDate = LocalDate.now(),
            purchaseTime = LocalTime.now(),
            total = 10.0,
            items = emptyList()
        )

        assertEquals(30, combinedCalculator.calculatePoints(testReceipt))
    }

    @Test
    fun `should return 0 if no calculators provided`() {
        val combinedCalculator = CombinedPointsCalculator(emptyList())

        val testReceipt = Receipt(
            id = UUID.randomUUID(),
            retailer = "Test Retailer",
            purchaseDate = LocalDate.now(),
            purchaseTime = LocalTime.now(),
            total = 10.0,
            items = emptyList()
        )

        assertEquals(0, combinedCalculator.calculatePoints(testReceipt))
    }
}
