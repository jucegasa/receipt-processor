package com.fetchrewards.receiptprocessor.model

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

@Entity
@Table(name = "receipts")
data class Receipt(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: UUID? = null,

    val retailer: String,
    val purchaseDate: LocalDate,
    val purchaseTime: LocalTime,
    val total: BigDecimal,

    @ElementCollection
    @CollectionTable(name = "receipt_items", joinColumns = [JoinColumn(name = "receipt_id")])
    val items: List<Item>
) {
    @Embeddable
    data class Item(
        val shortDescription: String,
        val price: BigDecimal
    )
}
