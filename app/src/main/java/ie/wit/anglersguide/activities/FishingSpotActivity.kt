package ie.wit.anglersguide.activities

import android.content.Intent

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import ie.wit.anglersguide.R
import ie.wit.anglersguide.databinding.ActivityFishingspotBinding
import ie.wit.anglersguide.helpers.showImagePicker
import ie.wit.anglersguide.main.MainApp
import ie.wit.anglersguide.models.FishingSpotModel
import ie.wit.anglersguide.models.Location
import timber.log.Timber
import timber.log.Timber.i

class FishingspotActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFishingspotBinding
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    var fishingSpot = FishingSpotModel()
    lateinit var app : MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFishingspotBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)
        app = application as MainApp
        var edit = false
        registerImagePickerCallback()
        registerMapCallback()

        if (intent.hasExtra("fishingspot_edit")) {
            edit = true
            fishingSpot = intent.extras?.getParcelable("fishingspot_edit")!!
            binding.fishingspotTitle.setText(fishingSpot.title)
            binding.description.setText(fishingSpot.description)
            binding.btnAdd.setText(R.string.save_fishingspot)
            Picasso.get()
                .load(fishingSpot.image)
                .into(binding.fishingspotImage)
            if (fishingSpot.image != Uri.EMPTY) {
                binding.chooseImage.setText(R.string.change_fishingspot_image)
            }
        }
        binding.btnAdd.setOnClickListener() {
            fishingSpot.title = binding.fishingspotTitle.text.toString()
            fishingSpot.description = binding.description.text.toString()
            if(!edit)
            {
                if (fishingSpot.title.isNotEmpty() && fishingSpot.description.isNotEmpty()) {
                    app.fishingspots.create(fishingSpot.copy())
                    setResult(RESULT_OK)
                    finish()
                } else if (fishingSpot.title.isNotEmpty() && fishingSpot.description.isEmpty()) {
                    Snackbar
                        .make(it, "Please Enter a Description", Snackbar.LENGTH_LONG)
                        .show()
                } else if (fishingSpot.title.isEmpty() && fishingSpot.description.isNotEmpty()) {
                    Snackbar
                        .make(it, "Please Enter a title", Snackbar.LENGTH_LONG)
                        .show()
                } else if (fishingSpot.title.isEmpty() && fishingSpot.description.isEmpty()) {
                    Snackbar
                        .make(it, "Please Enter a Fishing Spot details", Snackbar.LENGTH_LONG)
                        .show()
                }
            }
            else {
                //Todo: FishingSpotActivity->OnCreate() Add if conditions to saving a spot i.e. deleting desc entirely
                app.fishingspots.update(fishingSpot.copy())
                setResult(RESULT_OK)
                finish()
            }
        }

        binding.chooseImage.setOnClickListener {
            i("Select image")
            showImagePicker(imageIntentLauncher)
        }

        binding.fishingspotLocation.setOnClickListener {
            val location = Location(52.149, -6.9896, 15f)
            i ("Set Location Pressed")

            if (fishingSpot.zoom != 0f) {
                location.lat =  fishingSpot.lat
                location.lng = fishingSpot.lng
                location.zoom = fishingSpot.zoom
            }
            val launcherIntent = Intent(this, MapActivity::class.java)
                .putExtra("location", location)
            mapIntentLauncher.launch(launcherIntent)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_fishingspot, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Result ${result.data!!.data}")
                            fishingSpot.image = result.data!!.data!!
                            Picasso.get()
                                .load(fishingSpot.image)
                                .into(binding.fishingspotImage)
                            binding.chooseImage.setText(R.string.change_fishingspot_image)
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Location ${result.data.toString()}")
                            val location = result.data!!.extras?.getParcelable<Location>("location")!!
                            i("Location == $location")
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }
}