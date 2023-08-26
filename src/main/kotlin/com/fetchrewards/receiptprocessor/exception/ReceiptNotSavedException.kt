package com.fetchrewards.receiptprocessor.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
class ReceiptNotSavedException : RuntimeException("Receipt was not saved correctly. ID is missing.")