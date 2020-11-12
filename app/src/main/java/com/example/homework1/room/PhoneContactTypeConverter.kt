package com.example.homework1.room

import androidx.room.TypeConverter
import com.example.homework1.models.PhoneContact

class PhoneContactTypeConverter {
    @TypeConverter
    fun fromPhoneContact(contact: PhoneContact) = "${contact.name},${contact.phone}"

    @TypeConverter
    fun toPhoneContact(contact: String) = contact.split(",").toPhoneContact()
}

private fun <E> List<E>.toPhoneContact(): PhoneContact =
    PhoneContact(name = this[0].toString(), phone = this[1].toString())
