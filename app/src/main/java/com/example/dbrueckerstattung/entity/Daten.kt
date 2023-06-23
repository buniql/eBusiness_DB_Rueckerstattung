package com.example.dbrueckerstattung.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

//Datenklasse für die Rückerstattungen
@Entity(tableName = "daten")
data class Daten(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val von: String,
    val nach: String,
    val datum: String,
    val status: String,
    val verspeatung:String,
    val betrag: Double
)
