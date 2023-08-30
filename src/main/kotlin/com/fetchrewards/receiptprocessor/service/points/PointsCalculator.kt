package com.fetchrewards.receiptprocessor.service.points

import com.fetchrewards.receiptprocessor.model.Receipt

interface PointsCalculator {
    fun calculatePoints(receipt: Receipt): Int
}