package com.example.employeemanager.presentation.editEmployee

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.employeemanager.Constants
import com.example.employeemanager.R
import com.example.employeemanager.data.EmployeeDto
import com.example.employeemanager.databinding.ActivityEditEmployeeBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class EditEmployeeActivity : AppCompatActivity() {

    private var binding: ActivityEditEmployeeBinding? = null
    private val viewModel by viewModels<EditEmployeeViewModel>()
    private var isUpdateUser: Boolean = false
    private var employeeId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditEmployeeBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        getIntentData()
        setListeners()
        registerReceiver()
    }

    private fun getIntentData() {
        if (intent.getBooleanExtra(Constants.IS_UPDATE_USER, false)) {
            isUpdateUser = true
            binding?.editEmpToolbar?.title?.text = getString(R.string.update_user)
            setEmployeeData()
        }
        else {
            binding?.editEmpToolbar?.title?.text = getString(R.string.add_employee)
            isUpdateUser = false
        }
    }

    @Suppress("DEPRECATION")
    private fun setEmployeeData() {
        val employeeData: EmployeeDto? =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra(Constants.EMPLOYEE_DATA, EmployeeDto::class.java)
            } else {
                intent.getParcelableExtra(Constants.EMPLOYEE_DATA)
            }
        binding?.apply {
            employeeId = employeeData?.id?:0
            employeeData?.apply {
                etName.setText(employeeData.name)
                etPhone.setText(employeeData.phone)
                etEmail.setText(employeeData.email)
                etDesignation.setText(employeeData.designation)
                etDepartment.setText(employeeData.department)
            }
        }
    }

    private fun setListeners() {
        binding?.btnSubmit?.setOnClickListener {
            if (validateData()) {
                binding?.progressBar?.show()
                if (!isUpdateUser)
                    addEmployeeToDb()
                else
                    updateEmployee()
            }
        }
    }

    private fun registerReceiver() {
        viewModel.addEmployeeResult.observe(this) {
            binding?.progressBar?.hide()
            if (it.isSuccess) {
                // Go back after employee added successfully
                Toast.makeText(
                    this@EditEmployeeActivity,
                    getString(R.string.user_added_successfully), Toast.LENGTH_SHORT
                ).show()
                setResult(RESULT_OK)
                finish()
            } else {
                Toast.makeText(
                    this@EditEmployeeActivity,
                    getString(R.string.failed_to_add_user), Toast.LENGTH_SHORT
                ).show()
            }
        }

        viewModel.updateEmployeeResult.observe(this) {
            binding?.progressBar?.hide()
            if (it.isSuccess) {
                // Go back after employee updated successfully
                Toast.makeText(
                    this@EditEmployeeActivity,
                    getString(R.string.user_updated_successfully), Toast.LENGTH_SHORT
                ).show()
                setResult(RESULT_OK)
                finish()
            } else {
                Toast.makeText(
                    this@EditEmployeeActivity,
                    getString(R.string.failed_to_update_user), Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun addEmployeeToDb() {
        binding?.apply {
            viewModel.addEmployee(
                EmployeeDto(
                    name = etName.text.toString(),
                    phone = etPhone.text.toString(),
                    email = etEmail.text.toString(),
                    designation = etDesignation.text.toString(),
                    department = etDepartment.text.toString(),
                    isActive = true
                )
            )
        }
    }

    private fun updateEmployee() {
        binding?.apply {
            viewModel.updateEmployee(
                EmployeeDto(
                    id = employeeId,
                    name = etName.text.toString(),
                    phone = etPhone.text.toString(),
                    email = etEmail.text.toString(),
                    designation = etDesignation.text.toString(),
                    department = etDepartment.text.toString(),
                    isActive = true
                )
            )
        }
    }


    private fun validateData(): Boolean {
        binding?.apply {
            if (etName.text.isEmpty()) {
                etName.error = getString(R.string.name_cannot_be_empty)
                return false
            }
            if (etPhone.text.isEmpty()) {
                etPhone.error = getString(R.string.please_enter_phone_number)
                return false
            }
            if (etPhone.text.length < 10) {
                etPhone.error = getString(R.string.please_enter_a_valid_phone_number)
                return false
            }
            if (etEmail.text.isEmpty()) {
                etEmail.error = getString(R.string.please_enter_employee_email)
                return false
            }
            if (!etEmail.text.contains("@") || !etEmail.text.contains(".")) {
                etEmail.error = getString(R.string.please_enter_a_valid_email)
                return false
            }
            if (etDesignation.text.isEmpty()) {
                etDesignation.error = getString(R.string.designation_cannot_be_empty)
                return false
            }
            if (etDepartment.text.isEmpty()) {
                etDepartment.error = getString(R.string.department_cannot_be_empty)
                return false
            }
        }
        return true
    }
}