package com.nh.tarotapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_view.view.*

class ItemListAdapter(private val items: List<ItemList>, private val listener: (ItemList) -> Unit): RecyclerView.Adapter<ItemListAdapter.ViewHolder>(){


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(item: ItemList ) {
            itemView.title.text = item.title
            itemView.description.text = item.description

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_view,parent,false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
        holder.itemView.setOnClickListener{listener(items[position])}

    }

    override fun getItemCount(): Int {
        return items.size;
    }
}