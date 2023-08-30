package com.fetchrewards.receiptprocessor.controller

import com.fetchrewards.receiptprocessor.exception.ReceiptNotFoundException
import com.fetchrewards.receiptprocessor.exception.ReceiptNotSavedException
import com.fetchrewards.receiptprocessor.model.Receipt
import com.fetchrewards.receiptprocessor.repository.ReceiptRepository
import com.fetchrewards.receiptprocessor.service.points.*
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/receipts")
class ReceiptController(private val receiptRepository: ReceiptRepository) {

    private val pointsCalculator = CombinedPointsCalculator(
        listOf(
            RetailerNamePointsCalculator(),
            TotalRoundDollarPointsCalculator(),
            TotalMultipleOfQuarterPointsCalculator(),
            ItemCountPointsCalculator(),
            ItemDescriptionPointsCalculator(),
            PurchaseDayPointsCalculator(),
            PurchaseTimePointsCalculator()
        )
    )

    @PostMapping("/process")
    @ResponseStatus(HttpStatus.CREATED)
    fun processReceipt(@RequestBody receipt: Receipt): Map<String, UUID> {
        val savedReceipt = receiptRepository.save(receipt)
        val id = savedReceipt.id ?: throw ReceiptNotSavedException()
        return mapOf("id" to id)
    }

    @GetMapping("/{id}/points")
    fun getPoints(@PathVariable id: UUID): Map<String, Int> {
        val receipt = receiptRepository.findById(id).orElseThrow { ReceiptNotFoundException(id) }

        val points = pointsCalculator.calculatePoints(receipt)

        return mapOf("points" to points)
    }
}
