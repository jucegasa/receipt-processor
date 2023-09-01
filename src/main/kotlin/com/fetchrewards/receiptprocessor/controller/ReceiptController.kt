package com.fetchrewards.receiptprocessor.controller

import com.fetchrewards.receiptprocessor.dto.PointsResponse
import com.fetchrewards.receiptprocessor.dto.ReceiptResponse
import com.fetchrewards.receiptprocessor.exception.ReceiptNotFoundException
import com.fetchrewards.receiptprocessor.exception.ReceiptNotSavedException
import com.fetchrewards.receiptprocessor.model.Receipt
import com.fetchrewards.receiptprocessor.repository.ReceiptRepository
import com.fetchrewards.receiptprocessor.service.points.*
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/receipts")
@Tag(name = "Receipt Processor", description = "Operations for processing receipts and fetching points")
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

    @Operation(
        summary = "Process a receipt",
        description = "Create a receipt process",
        tags = ["fetch", "post"]
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "201",
            content = arrayOf(Content(schema = Schema(implementation = ReceiptResponse::class), mediaType = "application/json" ))
        ),
        ApiResponse(responseCode = "500", content = arrayOf(Content(schema = Schema()))),
        ApiResponse(responseCode = "400", content = arrayOf(Content(schema = Schema())))
    )
    @PostMapping("/process")
    @ResponseStatus(HttpStatus.CREATED)
    fun processReceipt(@Valid @RequestBody receipt: Receipt): ReceiptResponse {
        val savedReceipt = receiptRepository.save(receipt)
        val id = savedReceipt.id ?: throw ReceiptNotSavedException()
        return ReceiptResponse(id)
    }

    @Operation(
        summary = "Get points of a receipt",
        description = "get points of a receipt",
        tags = ["fetch", "get"]
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            content = arrayOf(Content(schema = Schema(implementation = PointsResponse::class), mediaType = "application/json" ))
        ),
        ApiResponse(responseCode = "500", content = arrayOf(Content(schema = Schema()))),
        ApiResponse(responseCode = "404", content = arrayOf(Content(schema = Schema())))
    )
    @GetMapping("/{id}/points")
    fun getPoints(@PathVariable id: UUID): PointsResponse {
        val receipt = receiptRepository.findById(id).orElseThrow { ReceiptNotFoundException(id) }

        val points = pointsCalculator.calculatePoints(receipt)

        return PointsResponse(points)
    }
}
