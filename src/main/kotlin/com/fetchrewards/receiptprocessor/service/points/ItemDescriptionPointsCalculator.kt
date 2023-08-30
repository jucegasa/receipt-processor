package com.fetchrewards.receiptprocessor.service.points

import com.fetchrewards.receiptprocessor.model.Receipt
import kotlin.math.ceil

class ItemDescriptionPointsCalculator : PointsCalculator {
    override fun calculatePoints(receipt: Receipt): Int {
        return receipt.items.sumOf {
            if (it.shortDescription.trim().length % 3 == 0) {
                ceil(it.price * 0.2).toInt()
            } else {
                0
            }
        }
    }
}
