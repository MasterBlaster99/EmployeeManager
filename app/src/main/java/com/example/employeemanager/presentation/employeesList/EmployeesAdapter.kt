package com.example.employeemanager.presentation.employeesList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.employeemanager.Constants
import com.example.employeemanager.R
import com.example.employeemanager.data.EmployeeDto
import com.example.employeemanager.databinding.RowEmployeeBinding

class EmployeesAdapter(val onStatusClick: (isActive: Boolean,model: EmployeeDto?) -> Unit,val onEditClick: (model: EmployeeDto?) -> Unit): ListAdapter<EmployeeDto, EmployeesAdapter.EmployeeDtoViewHolder>(DiffCallback()) {

    inner class EmployeeDtoViewHolder(private val binding: RowEmployeeBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: EmployeeDto?) {
            val context = binding.root.context
            binding.tvName.text = item?.name ?:""
            binding.tvPhone.text = item?.phone ?:""
            binding.tvEmail.text = item?.email ?:""
            binding.tvDesignationDepartment.text = "${item?.designation}, (${item?.department})"
            if(item?.isActive==true){
                binding.tvActive.setBackgroundResource(R.drawable.bg_status_active)
                binding.tvActive.text = Constants.STATUS_ACTIVE
                binding.tvActive.setTextColor(context.getColor(R.color.black))
            }
            else {
                binding.tvActive.setBackgroundResource(R.drawable.bg_status_in_active)
                binding.tvActive.text = Constants.STATUS_INACTIVE
                binding.tvActive.setTextColor(context.getColor(R.color.black))
            }

            binding.tvActive.setOnClickListener {
                if(binding.tvActive.text.equals(Constants.STATUS_ACTIVE)) {
                    onStatusClick(true,item)
                } else {
                    onStatusClick(false,item)
                }
            }

            binding.ivEdit.setOnClickListener {
                onEditClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeDtoViewHolder {
        return EmployeeDtoViewHolder(
            RowEmployeeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: EmployeeDtoViewHolder, position: Int) {
        holder.bind(getItem(holder.adapterPosition))
    }

    class DiffCallback: DiffUtil.ItemCallback<EmployeeDto>() {
        override fun areItemsTheSame(oldItem: EmployeeDto, newItem: EmployeeDto): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: EmployeeDto, newItem: EmployeeDto): Boolean {
            return oldItem.equals(newItem)
        }

    }

}