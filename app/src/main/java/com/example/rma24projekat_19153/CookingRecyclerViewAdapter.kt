package com.example.rma24projekat_19153

import android.content.Context
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

class CookingPlantsListAdapter(
    private var plants: List <Biljka>,
    private val context: Context
): RecyclerView.Adapter<CookingPlantsListAdapter.CookingPlantsViewHolder>(){
    private lateinit var itemClickListener: CookingPlantsListAdapter.PlantItemClickListener

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CookingPlantsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.kuharski_layout,parent,false)
        return CookingPlantsViewHolder(view)
    }

    override fun getItemCount(): Int = plants.size
    override fun onBindViewHolder(holder: CookingPlantsViewHolder, position: Int) {
        holder.bind(plants[position])
    }

    fun updatePlants(plants: List<Biljka>){
        this.plants = plants
        notifyDataSetChanged()
    }

    inner class CookingPlantsViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val plantImage: ImageView = itemView.findViewById(R.id.slikaItem)
        val plantNaziv: TextView = itemView.findViewById(R.id.nazivItem)
        val plantOkus: TextView = itemView.findViewById(R.id.profilOkusaItem)
        val plantJelo1: TextView = itemView.findViewById(R.id.jelo1Item)
        val plantJelo2: TextView = itemView.findViewById(R.id.jelo2Item)
        val plantJelo3: TextView = itemView.findViewById(R.id.jelo3Item)
        private lateinit var biljkaDAO: BiljkaDAO

        fun bind(plant: Biljka){
            plantNaziv.text = plant.naziv
            plantOkus.text = plant.profilOkusa.opis

            val jela = plant.jela
            plantJelo1.text = jela.getOrNull(0)?.toString() ?: ""
            plantJelo2.text = jela.getOrNull(1)?.toString() ?: ""
            plantJelo3.text = jela.getOrNull(2)?.toString() ?: ""

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

