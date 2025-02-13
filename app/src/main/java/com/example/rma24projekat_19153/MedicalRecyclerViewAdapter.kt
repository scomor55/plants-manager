package com.example.rma24projekat_19153

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MedicalPlantsListAdapter(
    private var plants: List <Biljka>,
    private val context: Context
): RecyclerView.Adapter<MedicalPlantsListAdapter.MedicalPlantsViewHolder>(){
    private lateinit var itemClickListener: MedicalPlantsListAdapter.PlantItemClickListener

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MedicalPlantsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.medicinski_layout,parent,false)
        return MedicalPlantsViewHolder(view)
    }

    override fun getItemCount(): Int = plants.size
    override fun onBindViewHolder(holder: MedicalPlantsViewHolder, position: Int) {
        holder.bind(plants[position])
    }

    fun updatePlants(plants: List<Biljka>){
        this.plants = plants
        notifyDataSetChanged()
    }

    inner class MedicalPlantsViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val plantImage: ImageView = itemView.findViewById(R.id.slikaItem)
        val plantNaziv: TextView = itemView.findViewById(R.id.nazivItem)
        val plantUpozorenje: TextView = itemView.findViewById(R.id.upozorenjeItem)
        val plantKorist1: TextView = itemView.findViewById(R.id.korist1Item)
        val plantKorist2: TextView = itemView.findViewById(R.id.korist2Item)
        val plantKorist3: TextView = itemView.findViewById(R.id.korist3Item)
        private lateinit var biljkaDAO: BiljkaDAO

        fun bind(plant: Biljka){
            plantNaziv.text = plant.naziv
            plantUpozorenje.text = plant.medicinskoUpozorenje

            val koristi = plant.medicinskeKoristi.map { it.opis }
            plantKorist1.text = koristi.getOrNull(0)?.toString() ?: ""
            plantKorist2.text = koristi.getOrNull(1)?.toString() ?: ""
            plantKorist3.text = koristi.getOrNull(2)?.toString() ?: ""

            itemView.setOnClickListener {
                itemClickListener.onPlantItemClick(plant)
            }
            biljkaDAO = BiljkaDatabase.getDatabase(context).biljkaDao()
            val trefleDAO = TrefleDAO(context)
            CoroutineScope(Dispatchers.Main).launch {
                val biljkaBitmap = biljkaDAO.getBiljkaBitmap(plant.id)
                val bitmap = biljkaBitmap?.bitmap ?: trefleDAO.getImage(plant).also {
                    biljkaDAO.addImage(plant.id, it)
                }
                withContext(Dispatchers.Main) {
                    plantImage.setImageBitmap(bitmap)
                }
            }
        }
    }

    interface PlantItemClickListener {
        fun onPlantItemClick(plant: Biljka)
    }

    fun setOnPlantItemClickListener(listener: PlantItemClickListener) {
        itemClickListener = listener
    }
}
