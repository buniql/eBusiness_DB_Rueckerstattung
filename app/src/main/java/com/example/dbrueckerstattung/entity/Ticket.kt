package com.example.dbrueckerstattung.entity

import android.provider.ContactsContract
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.util.Date

@Entity(tableName = "ticket_table")
data class Ticket(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "start") val start: Int?,
    @ColumnInfo(name = "destination") val destination: String?,
    @ColumnInfo(name = "date") val date: Date?,
    @ColumnInfo(name = "price") val price: BigDecimal?,
    //Amount of delay in minutes
    @ColumnInfo(name = "delay") val delay: Int?,
)
