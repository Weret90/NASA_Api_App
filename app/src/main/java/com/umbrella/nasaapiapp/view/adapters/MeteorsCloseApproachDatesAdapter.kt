package com.umbrella.nasaapiapp.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.umbrella.nasaapiapp.R
import com.umbrella.nasaapiapp.model.CloseApproachData

class MeteorsCloseApproachDatesAdapter :
    RecyclerView.Adapter<MeteorsCloseApproachDatesAdapter.MeteorsDatesViewHolder>() {

    private var meteorsDates: List<CloseApproachData> = ArrayList()

    inner class MeteorsDatesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(closeApproachData: CloseApproachData) {
            itemView.findViewById<TextView>(R.id.date).text = closeApproachData.closeApproachDate
            itemView.findViewById<TextView>(R.id.distance).text =
                closeApproachData.missDistance.kilometers
        }
    }

    fun setData(meteorsDates: List<CloseApproachData>) {
        this.meteorsDates = meteorsDates
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeteorsDatesViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_meteor_close_approach_date, parent, false)
        return MeteorsDatesViewHolder(view)
    }

    override fun onBindViewHolder(holder: MeteorsDatesViewHolder, position: Int) {
        holder.bind(meteorsDates[position])
    }

    override fun getItemCount(): Int {
        return meteorsDates.size
    }
}