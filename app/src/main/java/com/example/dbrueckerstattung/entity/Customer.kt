package com.example.dbrueckerstattung.entity

import android.provider.ContactsContract.CommonDataKinds.Email
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customer_table")
data class Customer(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "first_name") val firstName: String?,
    @ColumnInfo(name = "last_name") val lastName: String?,
    @ColumnInfo(name = "email") val email: Email?,
    @ColumnInfo(name = "mobile_number") val mobile_number: String?,
    @ColumnInfo(name = "iban") val iban: String?,
)
