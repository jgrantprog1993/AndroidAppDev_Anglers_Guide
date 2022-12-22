package ie.wit.anglersguide.adaptors

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ie.wit.anglersguide.databinding.CardFishingspotBinding
import ie.wit.anglersguide.models.FishingSpotModel
import ie.wit.anglersguide.utils.customTransformation

interface FishingSpotListener {


    fun onFishingSpotClick(fishingspot: FishingSpotModel)
    fun onQueryTextChange(query: String?): Boolean
}

class FishingSpotAdapter constructor(private var fishingspots: MutableList<FishingSpotModel>,
                                     private val listener: FishingSpotListener,
                                     private val readOnly: Boolean) :
    RecyclerView.Adapter<FishingSpotAdapter.MainHolder>() {

// https://www.youtube.com/watch?v=eEonjkmox-0
    private var removedPosition: Int = 0
    private lateinit var removedItem: FishingSpotModel
    private lateinit var fishingspot: FishingSpotModel


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardFishingspotBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainHolder(binding, readOnly)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val fishingspot = fishingspots[holder.adapterPosition]
        holder.bind(fishingspot, listener)
    }

    fun removeAt(position: Int) {
        fishingspots.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun getItemCount(): Int = fishingspots.size

//    fun removeItem(viewMainHolder: RecyclerView.ViewHolder){
//        removedPosition = viewMainHolder.adapterPosition
//        removedItem = fishingspots[viewMainHolder.adapterPosition]
//        val removedItemTitle = fishingspots[viewMainHolder.adapterPosition].title
//
//        fishingspots.removeAt(viewMainHolder.adapterPosition)
//        notifyItemRemoved(viewMainHolder.adapterPosition)
//
//        Snackbar.make(viewMainHolder.itemView, "$removedItemTitle  - DELETED FROM LIST.", Snackbar.LENGTH_LONG)
//            //.setAction("UNDO"){
//            //i("This is the removed iterm $removedItem")
//            //fishingspots.add(removedItem.copy())
//            //notifyItemInserted(removedPosition)
//            .show()
//    }

    class MainHolder(val binding: CardFishingspotBinding, private val readOnly: Boolean) :
        RecyclerView.ViewHolder(binding.root) {

        val readOnlyRow = readOnly

        fun bind(fishingspot: FishingSpotModel, listener: FishingSpotListener) {
            binding.root.tag = fishingspot

            Picasso.get().load(fishingspot.profilepic.toUri())
                .resize(200, 200)
                .transform(customTransformation())
                .centerCrop()
                .into(binding.imageIcon)
            binding.root.setOnClickListener { listener.onFishingSpotClick(fishingspot) }


            binding.fishingspotTitle.text = fishingspot.title
            binding.description.text = fishingspot.description

//            binding.executePendingBindings()


        }
    }


}
