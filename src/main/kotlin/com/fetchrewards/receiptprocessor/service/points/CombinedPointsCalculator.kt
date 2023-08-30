package com.fetchrewards.receiptprocessor.service.points

import com.fetchrewards.receiptprocessor.model.Receipt

class CombinedPointsCalculator(private val calculators: List<PointsCalculator>) : PointsCalculator {
    override fun calculatePoints(receipt: Receipt): Int {
        return calculators.sumOf { it.calculatePoints(receipt) }
    }
}
