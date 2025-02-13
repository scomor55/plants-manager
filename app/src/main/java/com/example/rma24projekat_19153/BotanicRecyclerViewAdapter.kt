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

class BotanicPlantsListAdapter(
    private var plants: List <Biljka>,
    private val context: Context
): RecyclerView.Adapter<BotanicPlantsListAdapter.BotanicPlantsViewHolder>(){
    private lateinit var itemClickListener: BotanicPlantsListAdapter.PlantItemClickListener
    private var quickSearchMode: Boolean = false
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BotanicPlantsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.botanicki_layout,parent,false)
        return BotanicPlantsViewHolder(view)
    }

    override fun getItemCount(): Int = plants.size
    override fun onBindViewHolder(holder: BotanicPlantsViewHolder, position: Int) {
        holder.bind(plants[position])
    }

    fun updatePlants(plants: List<Biljka>,quickSearchMode: Boolean = false){
        this.plants = plants
        this.quickSearchMode = quickSearchMode
        notifyDataSetChanged()
    }

    inner class BotanicPlantsViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val plantImage: ImageView = itemView.findViewById(R.id.slikaItem)
        val plantNaziv: TextView = itemView.findViewById(R.id.nazivItem)
        val plantPorodica: TextView = itemView.findViewById(R.id.porodicaItem)
        val plantKlimatskiTip: TextView = itemView.findViewById(R.id.klimatskiTipItem)
        val plantZemljisniTip: TextView = itemView.findViewById(R.id.zemljisniTipItem)
        private lateinit var biljkaDAO: BiljkaDAO

        fun bind(plant: Biljka) {
            plantNaziv.text = plant.naziv
            plantPorodica.text = plant.porodica
            plantKlimatskiTip.text = plant.klimatskiTipovi.getOrNull(0)?.opis ?: ""
            plantZemljisniTip.text = plant.zemljisniTipovi.getOrNull(0)?.naziv ?: ""

            Log.d("BotanicPlantsAdapter", "Binding plant: ${plant.naziv}")

            itemView.setOnClickListener {
                itemClickListener.onPlantItemClick(plant)
            }
            val trefleDAO = TrefleDAO(context)

            if (quickSearchMode) {
                itemView.setOnClickListener(null)
                CoroutineScope(Dispatchers.Main).launch {
                    val bitmap = trefleDAO.getImage(plant)
                    plantImage.setImageBitmap(bitmap)
                }
            }else {
            biljkaDAO = BiljkaDatabase.getDatabase(context).biljkaDao()
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    val biljkaBitmap = biljkaDAO.getBiljkaBitmap(plant.id)
                    Log.d("BotanicPlantsAdapter", "BiljkaBitmap: $biljkaBitmap")

                    val bitmap = biljkaBitmap?.bitmap ?: trefleDAO.getImage(plant).also {
                        biljkaDAO.addImage(plant.id, it)
                    }
                    Log.d("BotanicPlantsAdapter", "Bitmap retrieved for plant: ${plant.naziv}")
                    withContext(Dispatchers.Main) {
                        plantImage.setImageBitmap(bitmap)
                    }
                } catch (e: Exception) {
                    Log.e("BotanicPlantsAdapter", "Error binding plant image", e)

                }
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

