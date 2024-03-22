package com.example.rma24projekat_19153

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PlantsListAdapter(
    private var plants: List <Biljka>
): RecyclerView.Adapter<PlantsListAdapter.PlantsViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlantsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.medicinski_layout,parent,false)
        return PlantsViewHolder(view)
    }

    override fun getItemCount(): Int = plants.size
    override fun onBindViewHolder(holder: PlantsViewHolder, position: Int) {
        holder.plantNaziv.text = plants[position].naziv
        holder.plantImage.setImageResource(0)
        holder.plantUpozorenje.text = plants[position].medicinskoUpozorenje
      // Ovdje mozda prepravka
        holder.plantKorist1.text = plants[position].medicinskeKoristi[0].toString()
       /* holder.plantKorist2.text = plants[position].medicinskeKoristi[1].toString()
        holder.plantKorist3.text = plants[position].medicinskeKoristi[2].toString()*/
    }

    fun updatePlants(plants: List<Biljka>){
        this.plants = plants
        notifyDataSetChanged()
    }

    inner class PlantsViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val plantImage: ImageView = itemView.findViewById(R.id.slikaItem)
        val plantNaziv: TextView = itemView.findViewById(R.id.nazivItem)
        val plantUpozorenje: TextView = itemView.findViewById(R.id.upozorenjeItem)
        val plantKorist1: TextView = itemView.findViewById(R.id.korist1Item)
        val plantKorist2: TextView = itemView.findViewById(R.id.korist2Item)
        val plantKorist3: TextView = itemView.findViewById(R.id.korist3Item)
    }
}
