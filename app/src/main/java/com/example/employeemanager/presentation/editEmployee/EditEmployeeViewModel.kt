package com.example.employeemanager.presentation.editEmployee

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.employeemanager.data.EmployeeDto
import com.example.employeemanager.domain.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditEmployeeViewModel @Inject constructor(private val repository: AppRepository): ViewModel() {

    private val _addEmployeeResult: MutableLiveData<Result<Boolean>> = MutableLiveData()
    val addEmployeeResult: LiveData<Result<Boolean>> get() = _addEmployeeResult

    private val _updateEmployeeResult: MutableLiveData<Result<Boolean>> = MutableLiveData()
    val updateEmployeeResult: LiveData<Result<Boolean>> get() = _updateEmployeeResult

    fun updateEmployee(employeeDto: EmployeeDto) = viewModelScope.launch(Dispatchers.IO) {
        _updateEmployeeResult.postValue(repository.updateEmployee(employeeDto))
    }

    fun addEmployee(employeeDto: EmployeeDto) = viewModelScope.launch(Dispatchers.IO) {
        _addEmployeeResult.postValue(repository.addEmployee(employeeDto))
    }
}