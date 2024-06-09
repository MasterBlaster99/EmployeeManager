package com.example.employeemanager.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity(tableName = "EmployeeTable")
@Parcelize
data class EmployeeDto(
    @SerializedName("name")
    val name: String? = "",
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("email")
    val email: String = "",
    @SerializedName("phone")
    val phone: String = "",
    @SerializedName("designation")
    val designation: String = "",
    @SerializedName("department")
    val department: String = "",
    @SerializedName("is_active")
    var isActive: Boolean = false
): Parcelable
