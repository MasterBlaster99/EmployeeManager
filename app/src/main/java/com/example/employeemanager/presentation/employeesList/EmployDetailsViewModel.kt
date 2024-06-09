package com.example.employeemanager.presentation.employeesList

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
class EmployDetailsViewModel @Inject constructor(private val repository: AppRepository): ViewModel() {

    private val _employeeList: MutableLiveData<Result<List<EmployeeDto>>> = MutableLiveData()
    val employeeList: LiveData<Result<List<EmployeeDto>>> get() = _employeeList

    private val _updateEmployeeResult: MutableLiveData<Result<Boolean>> = MutableLiveData()
    val updateEmployeeResult: LiveData<Result<Boolean>> get() = _updateEmployeeResult

    fun updateEmployee(employeeDto: EmployeeDto) = viewModelScope.launch(Dispatchers.IO) {
        _updateEmployeeResult.postValue(repository.updateEmployee(employeeDto))
    }

    fun fetchEmployeeList() = viewModelScope.launch(Dispatchers.IO) {
        repository.getEmployeesList().collect {
            _employeeList.postValue(it)
        }
    }
}