package com.fetchrewards.receiptprocessor.service.points

import com.fetchrewards.receiptprocessor.model.Receipt

class TotalRoundDollarPointsCalculator : PointsCalculator {
    override fun calculatePoints(receipt: Receipt): Int {
        return if (receipt.total % 1.0 == 0.0) 50 else 0
    }
}