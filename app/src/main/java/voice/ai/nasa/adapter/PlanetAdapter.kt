package voice.ai.nasa.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import voice.ai.nasa.databinding.ItemRecyclerviewBinding
import voice.ai.nasa.library.ClickEffect
import voice.ai.nasa.model.PlanetModel

class PlanetAdapter(
    var planetList: List<PlanetModel>,
    private val eventClick: EventClick<PlanetModel>
) : RecyclerView.Adapter<PlanetAdapter.PlanetViewHolder>() {

    inner class PlanetViewHolder(val binding: ItemRecyclerviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindData(position: Int) {
            val planet = planetList[position]

            Glide.with(itemView)
                .load(planet.url)
                .into(binding.imgItemRecyclerview)

            binding.imgItemRecyclerview.setOnClickListener {
                ClickEffect.apply(it)
                eventClick.Click(planet)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanetViewHolder {
        val binding =
            ItemRecyclerviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlanetViewHolder(binding)
    }

    override fun getItemCount() = planetList.size

    override fun onBindViewHolder(holder: PlanetViewHolder, position: Int) {
        holder.bindData(position)
    }

    fun addData(newItems: List<PlanetModel>) {
        val start = planetList.size
        planetList = planetList + newItems
        notifyItemRangeInserted(start, newItems.size)
    }


    interface EventClick<T> {
        fun Click(data: T)
    }
}
