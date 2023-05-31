package com.example.dbrueckerstattung.entity

import android.provider.ContactsContract.CommonDataKinds.Email
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "customer_table",
    foreignKeys = [ForeignKey(
    entity = Address::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("address_fk")
)])
data class Customer(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "address_fk") val address_fk: Int?,
    @ColumnInfo(name = "first_name") val first_name: String?,
    @ColumnInfo(name = "last_name") val last_name: String?,
    @ColumnInfo(name = "email") val email: Email?,
    @ColumnInfo(name = "mobile_number") val mobile_number: String?,
    @ColumnInfo(name = "iban") val iban: String?
)

