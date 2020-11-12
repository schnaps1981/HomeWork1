package com.example.homework1.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.homework1.models.PhoneContact

interface PhoneContactEntityData {
    val phoneContact: PhoneContact
}

@Entity(tableName = "contacts")
@TypeConverters(PhoneContactTypeConverter::class)
data class PhoneContactEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(defaultValue = "")
    override val phoneContact: PhoneContact = PhoneContact.EMPTY
) : PhoneContactEntityData
