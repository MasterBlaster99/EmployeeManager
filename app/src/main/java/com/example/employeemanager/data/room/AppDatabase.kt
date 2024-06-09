package com.example.employeemanager.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.employeemanager.data.EmployeeDto

@Database(
    entities = [EmployeeDto::class],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun entityDao(): EmployeeDao
}