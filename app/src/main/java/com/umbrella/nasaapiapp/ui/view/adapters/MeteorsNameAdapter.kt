package com.umbrella.nasaapiapp.ui.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.umbrella.nasaapiapp.R
import com.umbrella.nasaapiapp.model.Meteor

class MeteorsNameAdapter : RecyclerView.Adapter<MeteorsNameAdapter.MeteorsNameViewHolder>() {

    private var meteors: List<Meteor> = ArrayList()
    private var onItemClick: (Meteor) -> Unit = {}

    inner class MeteorsNameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(meteor: Meteor) {
            itemView.findViewById<TextView>(R.id.asteroidName).text = meteor.name
            itemView.findViewById<TextView>(R.id.diameter).text =
                meteor.estimatedDiameter.kilometers.estimatedDiameterMax.toString()
            itemView.setOnClickListener {
                onItemClick(meteor)
            }
        }
    }

    fun setOnItemClickListener(onItemClick: (Meteor) -> Unit) {
        this.onItemClick = onItemClick
    }

    fun setData(meteors: List<Meteor>) {
        this.meteors = meteors
        notifyDataSetChanged()
    }

    fun getData() = meteors

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeteorsNameViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_meteor_name, parent, false)
        return MeteorsNameViewHolder(view)
    }

    override fun onBindViewHolder(holder: MeteorsNameViewHolder, position: Int) {
        holder.bind(meteors[position])
    }

    override fun getItemCount(): Int {
        return meteors.size
    }
}