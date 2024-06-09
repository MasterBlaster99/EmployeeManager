package com.example.employeemanager.presentation.employeesList

import android.content.Intent
import android.os.Bundle
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.example.employeemanager.Constants
import com.example.employeemanager.ExtensionUtils.hide
import com.example.employeemanager.ExtensionUtils.show
import com.example.employeemanager.R
import com.example.employeemanager.data.EmployeeDto
import com.example.employeemanager.databinding.ActivityMainBinding
import com.example.employeemanager.presentation.editEmployee.EditEmployeeActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null
    private val viewModel by viewModels<EmployDetailsViewModel>()
    private var employeesList: ArrayList<EmployeeDto>? = null
    private var mAdapter: EmployeesAdapter? = null

    private var launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                viewModel.fetchEmployeeList()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        binding?.homeToolbar?.ivFilter?.show()
        getEmployeesList()
        setListeners()
        registerReceiver()
    }

    private fun getEmployeesList() {
        binding?.progressBar?.show()
        viewModel.fetchEmployeeList()
    }

    private fun registerReceiver() {
        viewModel.employeeList.observe(this) {
            binding?.progressBar?.hide()
            if (it.isSuccess) {
                setUpEmployeesList(it)
            } else {
                Toast.makeText(
                    this@MainActivity,
                    getString(R.string.failed_to_fetch_employee_details),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        viewModel.updateEmployeeResult.observe(this) {
            binding?.progressBar?.hide()
            if (it.isSuccess) {
                getEmployeesList()
            } else {
                Toast.makeText(
                    this@MainActivity,
                    getString(R.string.failed_to_change_status),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun setUpEmployeesList(data: Result<List<EmployeeDto>>?) {
        if (!data?.getOrNull().isNullOrEmpty()) {
            binding?.etSearch?.show()
            binding?.tvMessage?.hide()
            employeesList = data?.getOrNull() as ArrayList<EmployeeDto>

            // Recyclerview Adapter initialization and handle click events using lambdas
            mAdapter = EmployeesAdapter(onStatusClick = { isActive, employee ->
                activateOrDeActivateEmployee(isActive, employee)
            }, onEditClick = { employee ->
                goToEditEmployeeActivity(employee)
            })
            binding?.recyclerView?.adapter = mAdapter
            mAdapter?.submitList(data.getOrNull())
        } else {
            binding?.etSearch?.hide()
            binding?.tvMessage?.text = getString(R.string.no_entries_message)
            binding?.tvMessage?.show()
        }
    }

    private fun activateOrDeActivateEmployee(isActive: Boolean, employee: EmployeeDto?) {
        if (employee != null) {
            // Update employee status active/inactive
            employee.isActive = !isActive
            binding?.progressBar?.show()
            viewModel.updateEmployee(employee)
        }
    }

    private fun goToEditEmployeeActivity(employee: EmployeeDto?) {
        launcher.launch(Intent(this, EditEmployeeActivity::class.java).apply {
            putExtras(Bundle().apply {
                putBoolean(Constants.IS_UPDATE_USER, true)
                putParcelable(Constants.EMPLOYEE_DATA, employee)
            })
        })
    }

    private fun searchByNameOrEmail(text: CharSequence?) {
        if (text?.isNotEmpty() == true) {
            // Filter list using name and email of All items in the list
            val filteredList = employeesList?.filter { employee ->
                employee.name?.contains(text) == true || employee.email.contains(text)
            }
            if (!filteredList.isNullOrEmpty()) {
                mAdapter?.submitList(filteredList)
                binding?.recyclerView?.show()
                binding?.tvMessage?.hide()
            } else {
                binding?.recyclerView?.hide()
                binding?.tvMessage?.text = getString(R.string.no_employees_found)
                binding?.tvMessage?.show()
            }
        } else {
            binding?.recyclerView?.show()
            binding?.tvMessage?.hide()
            mAdapter?.submitList(employeesList)
        }
    }

    private fun setListeners() {
        binding?.etSearch?.doOnTextChanged { text, _, _, _ ->
            searchByNameOrEmail(text)
        }

        binding?.addProduct?.setOnClickListener {
            launcher.launch(Intent(this, EditEmployeeActivity::class.java))
        }

        binding?.homeToolbar?.ivFilter?.setOnClickListener {
            showFilterPopup()
        }
    }

    private fun showFilterPopup() {
        PopupMenu(applicationContext, binding?.homeToolbar?.ivFilter).run {
            menuInflater.inflate(R.menu.filter_menu, menu)
            show()
            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.active -> {
                        filterActiveEmployees()
                    }

                    R.id.inActive -> {
                        filterInactiveEmployees()
                    }

                    R.id.all -> {
                        removeFilter()
                    }
                }
                false
            }
        }
    }

    private fun filterActiveEmployees() {
        val filteredList = employeesList?.filter { employee ->
            employee.isActive
        }
        if (!filteredList.isNullOrEmpty()) {
            binding?.recyclerView?.show()
            binding?.tvMessage?.hide()
            mAdapter?.submitList(filteredList)
        } else {
            Toast.makeText(
                this@MainActivity,
                getString(R.string.there_are_no_active_employees),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun filterInactiveEmployees() {
        val filteredList = employeesList?.filter { employee ->
            !employee.isActive
        }
        if (!filteredList.isNullOrEmpty()) {
            binding?.recyclerView?.show()
            binding?.tvMessage?.hide()
            mAdapter?.submitList(filteredList)
        } else {
            Toast.makeText(
                this@MainActivity,
                getString(R.string.there_are_no_inactive_employees),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun removeFilter() {
        binding?.recyclerView?.show()
        binding?.tvMessage?.hide()
        mAdapter?.submitList(employeesList)
    }
}