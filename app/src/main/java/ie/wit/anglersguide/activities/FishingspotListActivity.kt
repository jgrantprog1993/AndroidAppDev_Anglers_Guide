package ie.wit.anglersguide.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ie.wit.anglersguide.R
import ie.wit.anglersguide.adaptors.FishingSpotAdapter
import ie.wit.anglersguide.adaptors.FishingSpotListener
import ie.wit.anglersguide.databinding.ActivityFishingspotListBinding
import ie.wit.anglersguide.databinding.CardFishingspotBinding
import ie.wit.anglersguide.main.MainApp
import ie.wit.anglersguide.models.FishingSpotModel

class FishingspotListActivity : AppCompatActivity() , FishingSpotListener{

    lateinit var app: MainApp
    private lateinit var binding: ActivityFishingspotListBinding
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFishingspotListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        // binding.recyclerView.adapter = FishingSpotAdapter(app.fishingspots.findAll(), this)
        loadFishingSpots()
        registerRefreshCallback()
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

    fun showFishingSpots (fishingspots: List<FishingSpotModel>) {
        binding.recyclerView.adapter = FishingSpotAdapter(fishingspots, this)
        binding.recyclerView.adapter?.notifyDataSetChanged()
    }

}