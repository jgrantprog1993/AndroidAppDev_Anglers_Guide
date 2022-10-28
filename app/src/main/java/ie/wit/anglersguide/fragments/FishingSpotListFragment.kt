package ie.wit.anglersguide.fragments


import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.icu.text.DateFormat
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ie.wit.anglersguide.R
import ie.wit.anglersguide.activities.FishingspotActivity
import ie.wit.anglersguide.adaptors.FishingSpotAdapter
import ie.wit.anglersguide.databinding.FragmentFishingSpotListBinding
import ie.wit.anglersguide.databinding.FragmentSummariseBinding
import ie.wit.anglersguide.main.MainApp
import ie.wit.anglersguide.models.SummaryModel
import ie.wit.anglersguide.models.generateRandomId
import ie.wit.anglersguide.adaptors.FishingSpotListener
import ie.wit.anglersguide.models.FishingSpotModel

class FishingSpotListFragment: Fragment(), FishingSpotListener{

    private var _fragBinding: FragmentFishingSpotListBinding? = null
    private val fragBinding get() = _fragBinding!!
    lateinit var  app: MainApp
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var deleteIcon: Drawable
    var fishingSpot = FishingSpotModel()
    private var swipeBackground: ColorDrawable = ColorDrawable(Color.parseColor("#FF0000"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as MainApp
        setHasOptionsMenu(true)
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentFishingSpotListBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        activity?.title = getString(R.string.action_fishingSpot)
        fragBinding.recyclerView.setLayoutManager(LinearLayoutManager(activity))
        fragBinding.recyclerView.adapter = FishingSpotAdapter(app.fishingspots.findAll(), this)
        deleteIcon = this.context?.let { ContextCompat.getDrawable(it, R.drawable.ic_baseline_delete_sweep_24) }!!

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT){
            override fun onMove(p0: RecyclerView, p1: RecyclerView.ViewHolder, p2: RecyclerView.ViewHolder): Boolean{
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, position: Int) {
                //(viewAdaptor as FishingSpotAdapter).removeItem(viewHolder)
                (_fragBinding?.recyclerView?.adapter as FishingSpotAdapter).removeItem(viewHolder)
                app.fishingspots.delete(fishingSpot.copy())
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val itemView = viewHolder.itemView
                val iconMargin =  (itemView.height - deleteIcon.intrinsicHeight) / 2
                if (dX < 0)  {
                    swipeBackground.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
                    deleteIcon.setBounds(itemView.right - iconMargin - deleteIcon.intrinsicWidth, itemView.top + iconMargin, itemView.right + iconMargin,
                        itemView.bottom - iconMargin)
                }
                swipeBackground.draw(c)
                c.save()

                if(dX<0) {
                    c.clipRect(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
                }
                deleteIcon.draw(c)

                c.restore()

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }

        }
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(_fragBinding!!.recyclerView)
        return root
    }



    companion object {
        @JvmStatic
        fun newInstance() =
            FishingSpotListFragment().apply {
                arguments = Bundle().apply {  }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

    override fun onFishingSpotClick(fishingspot: FishingSpotModel) {
        val launcherIntent = Intent(this@FishingSpotListFragment.context, FishingspotActivity::class.java)
        launcherIntent.putExtra("fishingspot_edit", fishingspot)
        startActivityForResult(launcherIntent,0)
    }

    override fun onResume() {
        fragBinding.recyclerView.adapter = FishingSpotAdapter(app.fishingspots.findAll(), this)
        super.onResume()
    }

    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { loadFishingSpots() }
    }

    private fun loadFishingSpots() {
        showFishingSpots(app.fishingspots.findAll())
    }

    fun showFishingSpots (fishingspots: MutableList<FishingSpotModel>) {
        _fragBinding?.recyclerView?.adapter = FishingSpotAdapter(fishingspots, this)
        _fragBinding?.recyclerView?.adapter?.notifyDataSetChanged()
    }

   /// override fun onCreateOptionsMenu(menu: Menu): Boolean {
       // menuInflater.inflate(R.menu.menu_main, menu)
       // return super.onCreateOptionsMenu(menu)
   // }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                val launcherIntent = Intent(this@FishingSpotListFragment.context, FishingspotActivity::class.java)
                startActivityForResult(launcherIntent,0)
            }
        }
        return super.onOptionsItemSelected(item)
    }

}