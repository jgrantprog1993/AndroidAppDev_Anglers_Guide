package ie.wit.anglersguide.adaptors

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ie.wit.anglersguide.databinding.CardFishingspotBinding
import ie.wit.anglersguide.models.FishingSpotModel

interface FishingSpotListener {
    fun onFishingSpotClick(fishingspot: FishingSpotModel)
}

class FishingSpotAdapter constructor(private var fishingspots: MutableList<FishingSpotModel>,
                                     private val listener: FishingSpotListener) :
    RecyclerView.Adapter<FishingSpotAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardFishingspotBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val fishingspot = fishingspots[holder.adapterPosition]
        holder.bind(fishingspot, listener)
    }


    override fun getItemCount(): Int = fishingspots.size

    fun removeItem(viewMainHolder: RecyclerView.ViewHolder){
        fishingspots.removeAt(viewMainHolder.adapterPosition)
        notifyItemRemoved(viewMainHolder.adapterPosition)
    }

    class MainHolder(private val binding : CardFishingspotBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(fishingspot: FishingSpotModel, listener: FishingSpotListener) {
            binding.fishingspotTitle.text = fishingspot.title
            binding.description.text = fishingspot.description
            Picasso.get().load(fishingspot.image).resize(200,200).into(binding.imageIcon)
            binding.root.setOnClickListener { listener.onFishingSpotClick(fishingspot) }
        }
    }


}
