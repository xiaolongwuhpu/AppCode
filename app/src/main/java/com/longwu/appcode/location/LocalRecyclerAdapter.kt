package com.longwu.appcode.location

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.longwu.appcode.R

class LocalRecyclerAdapter (private var dataList: MutableList<String>) : RecyclerView.Adapter<LocalRecyclerAdapter.ViewHolder>() {


    fun setDataList(dataList: MutableList<String>) {
        this.dataList = dataList
    }

    fun addItem(str: String) {
        dataList.add(str)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewItem: TextView = itemView.findViewById(R.id.textViewItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.location_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemText = dataList[position]
        holder.textViewItem.text = itemText
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}