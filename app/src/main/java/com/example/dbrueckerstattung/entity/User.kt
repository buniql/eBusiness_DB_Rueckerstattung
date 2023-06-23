package com.example.dbrueckerstattung.entity

class User(
    var email: String? = "",
    var password: String? = "",
    var surname: String? = "",
    var lastname: String? = "",
    var iban: String? = ""
) {
    constructor() : this("", "", "", "", "")
}