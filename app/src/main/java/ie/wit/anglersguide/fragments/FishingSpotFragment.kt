package ie.wit.anglersguide.fragments

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Movie
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
//import ie.wit.anglersguide.ARG_PARAM1
//import ie.wit.anglersguide.ARG_PARAM2
import ie.wit.anglersguide.FishingSpotListFragment
import ie.wit.anglersguide.R
import ie.wit.anglersguide.activities.FishingspotActivity
import ie.wit.anglersguide.activities.FishingspotListActivity
import ie.wit.anglersguide.activities.MapActivity
import ie.wit.anglersguide.databinding.FragmentAddFishingSpotBinding
import ie.wit.anglersguide.helpers.showImagePicker
import ie.wit.anglersguide.main.MainApp
import ie.wit.anglersguide.models.FishingSpotModel
import ie.wit.anglersguide.models.Location
import timber.log.Timber.i
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream


val Fragment.packageManager get() = activity?.packageManager
class FishingSpotFragment : Fragment() {

    lateinit var app: MainApp
    private var _fragBinding: FragmentAddFishingSpotBinding? = null
    private val fragBinding get() = _fragBinding!!
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var cameraIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    var fishingSpot = FishingSpotModel()
    var edit = false
    val REQUEST_IMAGE_CAPTURE = 100


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as MainApp

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _fragBinding = FragmentAddFishingSpotBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        activity?.title = getString(R.string.action_addFishingSpot)

        if (getActivity()?.getIntent()?.getExtras()?.getString("fishingspot_edit") == "fishingspot_edit" ){
            edit = true
            fishingSpot = requireActivity().getIntent()?.getExtras()?.getParcelable("fishingspot_edit")!!
            _fragBinding?.fishingspotTitle?.setText(fishingSpot.title)
            _fragBinding?.description?.setText(fishingSpot.description)
            _fragBinding?.btnAdd?.setText(R.string.save_fishingspot)
            _fragBinding?.takeImage?.setText(R.string.take_new_fishingspot_image)
            Picasso.get()
                .load(fishingSpot.image)
                .into( _fragBinding?.fishingspotImage)
            if (fishingSpot.image != Uri.EMPTY) {
                _fragBinding?.chooseImage?.setText(R.string.change_fishingspot_image)
            }
        }
        setButtonListener(fragBinding)

        return root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            var takenImage = data?.extras?.get("data") as Bitmap
            i("Taken image - $takenImage")
            //binding.fishingspotImage.setImageBitmap(takenImage)

            val bos = ByteArrayOutputStream()
            takenImage.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos)
            var pathImage: String
            pathImage = MediaStore.Images.Media.insertImage(
                getActivity()?.getApplicationContext()?.contentResolver,
                takenImage,
                "New Picture",
                null
            )
            val bitmapdata = bos.toByteArray()
            val bs = ByteArrayInputStream(bitmapdata)

            i("pathImage - $pathImage")
            val x = Movie.decodeStream(bs)

            i("TdecodeStream(bs) - $x")

        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onResume() {
        super.onResume()
    }

    fun setButtonListener(layout: FragmentAddFishingSpotBinding) {
        layout.btnAdd.setOnClickListener {
            fishingSpot.title = _fragBinding?.fishingspotTitle?.text.toString()
            fishingSpot.description = _fragBinding?.description?.text.toString()
            if (!edit) {
                if (fishingSpot.title.isNotEmpty() && fishingSpot.description.isNotEmpty()) {
                    app.fishingspots.create(fishingSpot.copy())
                    activity?.setResult(AppCompatActivity.RESULT_OK)
                    //activity?.finish()
                    Snackbar
                        .make(it, "Fishing Spot ${fishingSpot.title} Added", Snackbar.LENGTH_LONG)
                        .show()

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
            } else {
                //Todo: FishingSpotActivity->OnCreate() Add if conditions to saving a spot i.e. deleting desc entirely
                layout.btnAdd.setOnClickListener {
                    fishingSpot.title = _fragBinding?.fishingspotTitle?.text.toString()
                    fishingSpot.description = _fragBinding?.description?.text.toString()
                    app.fishingspots.update(fishingSpot.copy())
                   // val launcherIntent =
                     //   Intent(this@FishingSpotFragment.context, FishingspotActivity::class.java)
                   // startActivityForResult(launcherIntent, 0)
                    activity?.setResult(AppCompatActivity.RESULT_OK)
                    activity?.finish()
                }
            }

        }
        fragBinding?.chooseImage?.setOnClickListener {
            i("Select image")
            showImagePicker(imageIntentLauncher)
        }

        _fragBinding?.takeImage?.setOnClickListener{
            i("Take picture frag")

            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            try {

                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }catch(e: ActivityNotFoundException){
                Toast.makeText(this@FishingSpotFragment?.context, "Error: " + e.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        }

        _fragBinding?.fishingspotLocation?.setOnClickListener {
            val location = Location(52.149, -6.9896, 15f)
            i ("Set Location Pressed")

            if (fishingSpot.zoom != 0f) {
                location.lat =  fishingSpot.lat
                location.lng = fishingSpot.lng
                location.zoom = fishingSpot.zoom
            }
            val launcherIntent = Intent(this@FishingSpotFragment.context, MapActivity::class.java)
                .putExtra("location", location)
            mapIntentLauncher.launch(launcherIntent)
        }
        registerImagePickerCallback()
        registerMapCallback()
    }


    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FishingSpotFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    AppCompatActivity.RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Result ${result.data!!.data}")
                            fishingSpot.image = result.data!!.data!!
                            Picasso.get()
                                .load(fishingSpot.image)
                                .into(_fragBinding?.fishingspotImage)
                            _fragBinding?.chooseImage?.setText(R.string.change_fishingspot_image)
                        } // end of if
                    }
                    AppCompatActivity.RESULT_CANCELED -> { } else -> { }
                }
            }
    }
    private fun registerCameraPickerCallback() {
        cameraIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    AppCompatActivity.RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Result ${result.data!!.data}")
                            fishingSpot.image = result.data!!.data!!
                            Picasso.get()
                                .load(fishingSpot.image)
                                .into(_fragBinding?.fishingspotImage)
                            _fragBinding?.chooseImage?.setText(R.string.take_new_fishingspot_image)
                        } // end of if
                    }
                    AppCompatActivity.RESULT_CANCELED -> { } else -> { }
                }
            }
    }


    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    AppCompatActivity.RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Location ${result.data.toString()}")
                            val location = result.data!!.extras?.getParcelable<Location>("location")!!
                            i("Location == $location")
                            fishingSpot.lat = location.lat
                            fishingSpot.lng = location.lng
                            fishingSpot.zoom = location.zoom
                        } // end of if
                    }
                    AppCompatActivity.RESULT_CANCELED -> { } else -> { }
                }
            }
    }
}


