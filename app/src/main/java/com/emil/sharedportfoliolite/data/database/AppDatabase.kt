package com.emil.sharedportfoliolite.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.emil.sharedportfoliolite.data.dao.AssetDao
import com.emil.sharedportfoliolite.data.dao.UserDao
import com.emil.sharedportfoliolite.data.entities.Asset
import com.emil.sharedportfoliolite.data.entities.User

@Database(entities = [User::class, Asset::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun assetDao(): AssetDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Add purchasePrice column, copying existing pricePerShareUSD as the purchase price
                database.execSQL("ALTER TABLE assets ADD COLUMN purchasePrice REAL NOT NULL DEFAULT 0.0")
                database.execSQL("UPDATE assets SET purchasePrice = pricePerShareUSD")
            }
        }

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Already has purchasePrice, no changes needed
            }
        }

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "portfolio_database"
                )
                .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}