package com.nh.tarotapp

import android.content.ClipData
import android.content.ClipDescription
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.card.view.*
import kotlinx.android.synthetic.main.item_view.view.*

class CardAdapter(private val cards: List<Card>, private val listener: (View,Card) -> Unit): RecyclerView.Adapter<CardAdapter.ViewHolder>(){


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(item: Card ) {
            //itemView.image.setImageDrawable(item.image)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card,parent,false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(cards[position])
        holder.itemView.setOnLongClickListener {listener(holder.itemView,cards[position]);true}

    }

    override fun getItemCount(): Int {
        return cards.size;
    }
}