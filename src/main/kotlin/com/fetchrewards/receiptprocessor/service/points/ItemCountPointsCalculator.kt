package com.fetchrewards.receiptprocessor.service.points

import com.fetchrewards.receiptprocessor.model.Receipt

class ItemCountPointsCalculator : PointsCalculator {
    override fun calculatePoints(receipt: Receipt): Int {
        return (receipt.items.size / 2) * 5
    }
}
