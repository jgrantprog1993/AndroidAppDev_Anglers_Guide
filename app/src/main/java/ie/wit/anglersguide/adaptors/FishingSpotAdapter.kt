package ie.wit.anglersguide.adaptors

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import ie.wit.anglersguide.databinding.CardFishingspotBinding
import ie.wit.anglersguide.models.FishingSpotModel
import timber.log.Timber.i

interface FishingSpotListener {
    fun onFishingSpotClick(fishingspot: FishingSpotModel)
}

class FishingSpotAdapter constructor(private var fishingspots: MutableList<FishingSpotModel>,
                                     private val listener: FishingSpotListener) :
    RecyclerView.Adapter<FishingSpotAdapter.MainHolder>() {

// https://www.youtube.com/watch?v=eEonjkmox-0
    private var removedPosition: Int = 0
    private lateinit var removedItem: FishingSpotModel
    private lateinit var fishingspot: FishingSpotModel

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
        removedPosition = viewMainHolder.adapterPosition
        removedItem = fishingspots[viewMainHolder.adapterPosition]
        val removedItemTitle = fishingspots[viewMainHolder.adapterPosition].title

        fishingspots.removeAt(viewMainHolder.adapterPosition)
        notifyItemRemoved(viewMainHolder.adapterPosition)

        Snackbar.make(viewMainHolder.itemView, "$removedItemTitle  - DELETED FROM LIST.", Snackbar.LENGTH_LONG)
            //.setAction("UNDO"){
            //i("This is the removed iterm $removedItem")
            //fishingspots.add(removedItem.copy())
            //notifyItemInserted(removedPosition)
            .show()
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
