package com.example.homework1.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(entities = [PhoneContactEntity::class], version = 1)
@TypeConverters(PhoneContactTypeConverter::class)
abstract class PhoneContactsDatabase : RoomDatabase() {

    abstract fun contactDao(): ContactDao

    companion object {
        private var INSTANCE: PhoneContactsDatabase? = null

        fun getInstance(context: Context): PhoneContactsDatabase? {
            if (INSTANCE == null) {
                synchronized(PhoneContactsDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        PhoneContactsDatabase::class.java,
                        "contactsDB.db"
                    ).build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}
