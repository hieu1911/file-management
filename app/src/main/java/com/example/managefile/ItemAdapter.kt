package com.example.managefile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class ItemAdapter(val items: ArrayList<ItemModel>) : BaseAdapter() {
    override fun getCount(): Int = items.size

    override fun getItem(position: Int) = items[position]

    override fun getItemId(position: Int) = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val itemView: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            itemView = LayoutInflater.from(parent?.context).inflate(R.layout.layout_item, parent, false)
            viewHolder = ViewHolder(itemView)
            itemView.tag = viewHolder
        } else {
            itemView = convertView
            viewHolder = itemView.tag as ViewHolder
        }

        viewHolder.textView.text = items[position].file.name

        if (items[position].isFolder) {
            viewHolder.icon.setImageResource(R.drawable.folder)
        } else {
            viewHolder.icon.setImageResource(R.drawable.document)
        }

        return itemView
    }

    class ViewHolder(itemView: View) {
        val textView: TextView
        val icon: ImageView

        init {
            textView = itemView.findViewById(R.id.itemTextView)
            icon = itemView.findViewById(R.id.icon)
        }
    }
}