package com.fetchrewards.receiptprocessor.service.points

import com.fetchrewards.receiptprocessor.model.Receipt

class TotalMultipleOfQuarterPointsCalculator : PointsCalculator {
    override fun calculatePoints(receipt: Receipt): Int {
        return if (receipt.total % 0.25 == 0.0) 25 else 0
    }
}