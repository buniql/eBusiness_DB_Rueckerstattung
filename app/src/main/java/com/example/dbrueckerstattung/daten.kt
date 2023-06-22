package com.example.dbrueckerstattung

//data class für csv datei
data class daten(
    val id: String,
    val von: String,
    val nach: String,
    val datum: String,
    val status: String,
    val verspeatung:String,
    val betrag: Double
)
