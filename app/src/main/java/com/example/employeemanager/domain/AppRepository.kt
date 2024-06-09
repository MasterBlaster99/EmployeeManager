package com.example.employeemanager.domain

import com.example.employeemanager.data.EmployeeDto
import com.example.employeemanager.data.room.EmployeeDao
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AppRepository @Inject constructor(private val employeeDao: EmployeeDao) {

    suspend fun getEmployeesList() = flow {
        try {
            emit(Result.success(employeeDao.getAllEntities()))
        } catch (e: Exception) {
             emit(Result.failure(e))
        }
    }

    suspend fun addEmployee(employee: EmployeeDto): Result<Boolean> {
        try {
            employeeDao.addEmployee(employee)
            return Result.success(true)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    suspend fun updateEmployee(employee: EmployeeDto): Result<Boolean> {
        try {
            employeeDao.updateEmployee(employee)
            return Result.success(true)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

}