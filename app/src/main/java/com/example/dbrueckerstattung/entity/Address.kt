package com.example.dbrueckerstattung.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "address_table")
data class Address(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "street") val street: String?,
    @ColumnInfo(name = "plz") val plz: String?,
    @ColumnInfo(name = "city") val city: String?,
    @ColumnInfo(name = "country") val country: String?
)