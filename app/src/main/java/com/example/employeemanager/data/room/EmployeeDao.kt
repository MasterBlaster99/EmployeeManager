package com.example.employeemanager.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.employeemanager.data.EmployeeDto

@Dao
interface EmployeeDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addEmployee(order: EmployeeDto): Long

    @Query("SELECT * FROM employeetable")
    suspend fun getAllEntities(): List<EmployeeDto>

    @Update
    suspend fun updateEmployee(employee: EmployeeDto)

}