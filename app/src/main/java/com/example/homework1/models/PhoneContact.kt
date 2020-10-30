package com.example.homework1.models

data class PhoneContact(
    val name: String = "",
    val phone: String = ""
) {
    fun isEmpty() = this === EMPTY

    companion object {
        val EMPTY = PhoneContact()
    }
}
