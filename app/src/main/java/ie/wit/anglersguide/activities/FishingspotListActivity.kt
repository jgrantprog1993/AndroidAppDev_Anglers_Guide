package ie.wit.anglersguide.activities

import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ie.wit.anglersguide.R
import ie.wit.anglersguide.adaptors.FishingSpotAdapter
import ie.wit.anglersguide.adaptors.FishingSpotListener
import ie.wit.anglersguide.databinding.ActivityFishingspotListBinding
import ie.wit.anglersguide.main.MainApp
import ie.wit.anglersguide.models.FishingSpotModel

class FishingspotListActivity : AppCompatActivity() , FishingSpotListener{

    lateinit var app: MainApp
    lateinit var viewAdaptor: RecyclerView.Adapter<*>
    private lateinit var binding: ActivityFishingspotListBinding
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>
    private var dataset = mutableListOf<FishingSpotModel>()
    var fishingSpot = FishingSpotModel()
    private var swipeBackground: ColorDrawable = ColorDrawable(Color.parseColor("#FF0000"))
    private lateinit var deleteIcon: Drawable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFishingspotListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)
        //i("This is a list to see if i can access $list")
        app = application as MainApp
        deleteIcon = ContextCompat.getDrawable(this, R.drawable.ic_baseline_delete_sweep_24)!!
        viewAdaptor = FishingSpotAdapter(dataset, this)
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = FishingSpotAdapter(app.fishingspots.findAll(), this)
        loadFishingSpots()
        registerRefreshCallback()
        //listOf(binding.recyclerView.adapter).elementAt(1)

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT){
            override fun onMove(p0: RecyclerView, p1: RecyclerView.ViewHolder, p2: RecyclerView.ViewHolder): Boolean{
               return false
            }
            //https://www.youtube.com/watch?v=eEonjkmox-0
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, position: Int) {
                //(viewAdaptor as FishingSpotAdapter).removeItem(viewHolder)
                (binding.recyclerView.adapter as FishingSpotAdapter).removeItem(viewHolder)
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
                if (dX > 0) {
                    swipeBackground.setBounds(itemView.left, itemView.top, dX.toInt(), itemView.bottom)
                    deleteIcon.setBounds(itemView.left + iconMargin, itemView.top + iconMargin, itemView.left + iconMargin + deleteIcon.intrinsicWidth,
                    itemView.bottom - iconMargin)
                } else {
                    swipeBackground.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
                    deleteIcon.setBounds(itemView.right - iconMargin - deleteIcon.intrinsicWidth, itemView.top + iconMargin, itemView.right + iconMargin,
                        itemView.bottom - iconMargin)
                }
                swipeBackground.draw(c)
                c.save()

                if(dX>0) {
                    c.clipRect(itemView.left, itemView.top, dX.toInt(), itemView.bottom)
                } else {
                  c.clipRect(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
                }

                deleteIcon.draw(c)

                c.restore()

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }

        }
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                val launcherIntent = Intent(this, FishingspotActivity::class.java)
                refreshIntentLauncher.launch(launcherIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onFishingSpotClick(fishingspot: FishingSpotModel) {
        val launcherIntent = Intent(this, FishingspotActivity::class.java)
        launcherIntent.putExtra("fishingspot_edit", fishingspot)
        refreshIntentLauncher.launch(launcherIntent)
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
        binding.recyclerView.adapter = FishingSpotAdapter(fishingspots, this)
        binding.recyclerView.adapter?.notifyDataSetChanged()
    }

}