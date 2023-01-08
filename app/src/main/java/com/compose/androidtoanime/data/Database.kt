package com.compose.androidtoanime.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.compose.androidtoanime.Utils.AppUtils.Companion.DATABASE_NAME


@Database(
    entities = [
        ResponsePhoto::class,
    ],
    version = 1,
    exportSchema = false
)

@TypeConverters(imageTypeConverter::class)
abstract class roomDatabase : RoomDatabase() {
    abstract fun dao(): IDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: roomDatabase? = null

        fun getDatabase(context: Context): roomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    roomDatabase::class.java,
                    DATABASE_NAME
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

}

