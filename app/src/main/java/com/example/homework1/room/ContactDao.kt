package com.example.homework1.room

import androidx.room.*

@Dao
@TypeConverters(PhoneContactTypeConverter::class)
interface ContactDao {
    @Query("SELECT * FROM contacts")
    suspend fun getAllContacts(): List<PhoneContactEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllContacts(items: List<PhoneContactEntity>)

    @Query("DELETE FROM contacts")
    suspend fun clearTable()
}
