package com.fetchrewards.receiptprocessor.service.points

import com.fetchrewards.receiptprocessor.model.Receipt
import java.time.LocalDate

class PurchaseDayPointsCalculator : PointsCalculator {
    override fun calculatePoints(receipt: Receipt): Int {
        val day = receipt.purchaseDate.dayOfMonth
        return if (day % 2 == 1) 6 else 0
    }
}
