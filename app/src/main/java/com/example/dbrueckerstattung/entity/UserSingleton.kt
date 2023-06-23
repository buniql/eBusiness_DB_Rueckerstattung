package com.example.dbrueckerstattung.entity

//Singeltonobjekt für Userdaten
object UserSingleton {
    var user: User = User()
        private set

    fun setUserEmail(email: String?) {
        user?.email = email
    }

    fun setUserPassword(password: String?) {
        user?.password = password
    }

    fun setUserSurname(surname: String?) {
        user?.surname = surname
    }

    fun setUserLastname(lastname: String?) {
        user?.lastname = lastname
    }

    fun setUserIban(iban: String?) {
        user?.iban = iban
    }
}