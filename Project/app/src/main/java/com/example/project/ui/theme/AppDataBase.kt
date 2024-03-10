package com.example.project.ui.theme

import androidx.room.Database
import androidx.room.RoomDatabase



@Database(entities = [User::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDaoInterface(): UserDaoInterface
}
