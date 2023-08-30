package com.fetchrewards.receiptprocessor.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.util.UUID

@ResponseStatus(HttpStatus.NOT_FOUND)
class ReceiptNotFoundException(id: UUID) : RuntimeException("Receipt with ID $id not found.")