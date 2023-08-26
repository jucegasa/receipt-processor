package com.fetchrewards.receiptprocessor.repository

import com.fetchrewards.receiptprocessor.model.Receipt
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ReceiptRepository: JpaRepository<Receipt, UUID>