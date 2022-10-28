package ie.wit.anglersguide.activities

//import ie.wit.anglersguide.fragments.REQUEST_CODE

import android.R.attr.bitmap
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.Movie.decodeStream
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import ie.wit.anglersguide.R
import ie.wit.anglersguide.databinding.ActivityFishingspotBinding
import ie.wit.anglersguide.helpers.showImagePicker
import ie.wit.anglersguide.main.MainApp
import ie.wit.anglersguide.models.FishingSpotModel
import ie.wit.anglersguide.models.Location
import timber.log.Timber.i
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream


class FishingspotActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFishingspotBinding
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var cameraIntentLauncher : ActivityResultLauncher<Intent>
    var fishingSpot = FishingSpotModel()
    lateinit var app : MainApp
    val REQUEST_IMAGE_CAPTURE = 100

    var imagePicker: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFishingspotBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        app = application as MainApp

        var edit = false

        imagePicker = findViewById(R.id.fishingspotImage)
        val gallery = findViewById<Button>(R.id.chooseImage)
        val camera = findViewById<Button>(R.id.takeImage)

        if (intent.hasExtra("fishingspot_edit")) {
            edit = true
            fishingSpot = intent.extras?.getParcelable("fishingspot_edit")!!
            binding.fishingspotTitle.setText(fishingSpot.title)
            binding.description.setText(fishingSpot.description)
            binding.btnAdd.setText(R.string.update_fishingspot)
            binding.fishingspotLocation.setText(R.string.button_update_location)

            Picasso.get()
                .load(fishingSpot.image)
                .resize(200,200)
                .into(binding.fishingspotImage)
            if (fishingSpot.image != Uri.EMPTY) {
                binding.chooseImage.setText(R.string.change_fishingspot_image)
                binding.takeImage.setText(R.string.take_new_fishingspot_image)
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

        binding.takeImage.setOnClickListener{
            i ("Take Image")
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            try {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }catch(e: ActivityNotFoundException){
                Toast.makeText(this, "Error: " + e.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        }

        registerImagePickerCallback()
        registerMapCallback()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == REQUEST_IMAGE_CAPTURE &&  resultCode == Activity.RESULT_OK)
        {
            var takenImage = data?.extras?.get("data") as Bitmap
            i("Taken image - $takenImage")
            //binding.fishingspotImage.setImageBitmap(takenImage)

            val bos = ByteArrayOutputStream()
            takenImage.compress(CompressFormat.PNG, 0 /*ignored for PNG*/, bos)
            var pathImage: String
            pathImage = MediaStore.Images.Media.insertImage(getApplicationContext().contentResolver, takenImage, "New Picture", null)
            val bitmapdata = bos.toByteArray()
            val bs = ByteArrayInputStream(bitmapdata)

            i("pathImage - $pathImage")
            val x = decodeStream(bs)

            i("TdecodeStream(bs) - $x")

        } else {
        super.onActivityResult(requestCode, resultCode, data)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_fishingspot, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> {
                finish()
            }
            R.id.action_listfishingspots -> {
                startActivity(Intent(this, FishingspotListActivity::class.java))
                true
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
                            fishingSpot.lat = location.lat
                            fishingSpot.lng = location.lng
                            fishingSpot.zoom = location.zoom
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }
}