package com.example.dbrueckerstattung.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

// Entity wird über DAO auf DB gemappt
// Kunden sind endgeil, die bringen uns Cashmoney
@Entity(tableName = "customer_table",
    foreignKeys = [ForeignKey(
    entity = Address::class,
    parentColumns = arrayOf("address_fk"),
    childColumns = arrayOf("id")
    ),
    ForeignKey(
        entity = Refund::class,
        parentColumns = arrayOf("refund_fk"),
        childColumns = arrayOf("id")
    )])
data class Customer(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "address_fk") val address_fk: Int?,
    @ColumnInfo(name = "refund_fk") val refund_fk: Int?,
    @ColumnInfo(name = "first_name") val first_name: String?,
    @ColumnInfo(name = "last_name") val last_name: String?,
    @ColumnInfo(name = "email") val email: String?,
    @ColumnInfo(name = "password") val password: String?,
    @ColumnInfo(name = "mobile_number") val mobile_number: String?,
    @ColumnInfo(name = "iban") val iban: String?
    )
