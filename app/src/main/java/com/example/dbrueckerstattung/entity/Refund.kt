package com.example.dbrueckerstattung.entity

import android.provider.ContactsContract
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "refund_table",
    foreignKeys = [ForeignKey(
        entity = Customer::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("customer_fk")
    ),
    ForeignKey(
        entity = Ticket::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("ticket_fk")
    )
    ])
data class Refund (
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "customer_fk") val customer_fk: Int?,
    @ColumnInfo(name = "ticket_fk") val ticket_fk: Int?,
    @ColumnInfo(name = "date") val date: Date?,
    @ColumnInfo(name = "status") val status: String?,

)