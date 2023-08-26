package com.fetchrewards.receiptprocessor.controller

import com.fetchrewards.receiptprocessor.exception.ReceiptNotSavedException
import com.fetchrewards.receiptprocessor.model.Receipt
import com.fetchrewards.receiptprocessor.repository.ReceiptRepository
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/receipts")
class ReceiptController(private val receiptRepository: ReceiptRepository) {

    @PostMapping("/process")
    @ResponseStatus(HttpStatus.CREATED)
    fun processReceipt(@RequestBody receipt: Receipt): Map<String, UUID> {
        val savedReceipt = receiptRepository.save(receipt)
        val id = savedReceipt.id ?: throw ReceiptNotSavedException()
        return mapOf("id" to id)
    }
}