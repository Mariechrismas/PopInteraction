package com.example.popinteraction.parameter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView
import com.example.popinteraction.R

class ToggleButtonAdapter(val categoryList: List<CategoryObject>) :
    RecyclerView.Adapter<ToggleButtonAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val toggleButton: ToggleButton = itemView.findViewById(R.id.toggleButton)
        val categoryNameTextView: TextView = itemView.findViewById(R.id.categoryNameTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_toggle, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categoryList[position]
        holder.categoryNameTextView.text = category.name
        holder.toggleButton.isChecked = category.isChecked

        holder.toggleButton.setOnCheckedChangeListener { _, isChecked ->
            category.isChecked = isChecked
        }
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }
}
