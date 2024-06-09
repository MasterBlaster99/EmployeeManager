package com.example.employeemanager.di

import android.content.Context
import androidx.room.Room
import com.example.employeemanager.data.room.AppDatabase
import com.example.employeemanager.data.room.EmployeeDao
import com.example.employeemanager.domain.AppRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMoviesRepo(employeeDao: EmployeeDao): AppRepository = AppRepository(employeeDao)

    @Provides
    fun provideOrderDao(appDatabase: AppDatabase): EmployeeDao = appDatabase.entityDao()

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase =
        Room.databaseBuilder(appContext, AppDatabase::class.java, "Employees.db").build()

}