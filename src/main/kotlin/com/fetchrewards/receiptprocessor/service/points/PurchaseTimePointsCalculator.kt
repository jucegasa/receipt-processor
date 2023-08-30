package com.fetchrewards.receiptprocessor.service.points

import com.fetchrewards.receiptprocessor.model.Receipt
import java.time.LocalTime

class PurchaseTimePointsCalculator : PointsCalculator {
    override fun calculatePoints(receipt: Receipt): Int {
        val purchaseTime = receipt.purchaseTime
        return if (purchaseTime.isAfter(LocalTime.of(14, 0)) && purchaseTime.isBefore(LocalTime.of(16, 0))) {
            10
        } else {
            0
        }
    }
}