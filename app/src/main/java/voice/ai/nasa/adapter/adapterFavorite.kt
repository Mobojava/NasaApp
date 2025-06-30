package voice.ai.nasa.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import voice.ai.nasa.databinding.ItemRecyclerviewBinding
import voice.ai.nasa.library.ClickEffect
import voice.ai.nasa.model.PlanetModel

class adapterFavorite(
    var data: List<PlanetModel>,
    private val eventclickFavorite: eventClick<PlanetModel>
) : RecyclerView.Adapter<adapterFavorite.FavoriteViewHolder>() {


    inner class FavoriteViewHolder(val binding: ItemRecyclerviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(position: Int) {

            val palent = data[position]
            Glide.with(itemView)
                .load(data[position].url)
                .into(binding.imgItemRecyclerview)

            itemView.setOnClickListener {
                ClickEffect.apply { it }
                eventclickFavorite.Click(palent,position.toString())
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding =
            ItemRecyclerviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bindData(position)
    }

    fun removePlanet(oldPlanet: PlanetModel, oldPosition: Int){


    }

    interface eventClick<T> {

        fun Click(data: T, position: String)
    }


}