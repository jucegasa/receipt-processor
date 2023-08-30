package com.fetchrewards.receiptprocessor.service.points

import com.fetchrewards.receiptprocessor.model.Receipt

class RetailerNamePointsCalculator : PointsCalculator {
    override fun calculatePoints(receipt: Receipt): Int {
        return receipt.retailer.count { it.isLetterOrDigit() }
    }
}