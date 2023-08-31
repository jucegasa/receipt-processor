package com.fetchrewards.receiptprocessor.model

import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.persistence.*
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.PastOrPresent
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

@Entity
@Table(name = "receipts")
data class Receipt(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: UUID? = null,

    @field:NotBlank(message = "Retailer cannot be blank.")
    val retailer: String,

    @field:JsonFormat(pattern = "yyyy-MM-dd")
    @field:DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @field:NotNull(message = "Purchase date cannot be null.")
    @field:PastOrPresent
    val purchaseDate: LocalDate,

    @field:JsonFormat(pattern = "HH:mm")
    @field:DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    @field:NotNull(message = "Purchase time cannot be null.")
    val purchaseTime: LocalTime,
    val total: Double,

    @field:Valid
    @field:NotEmpty(message = "Items list cannot be empty.")
    @ElementCollection
    @CollectionTable(name = "receipt_items", joinColumns = [JoinColumn(name = "receipt_id")])
    val items: List<Item>
) {
    @Embeddable
    data class Item(
        val shortDescription: String,
        val price: Double
    )
}
