package ie.wit.anglersguide.ui.activity

import LoggedInViewModel
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Movie
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
//import ie.wit.anglersguide.ARG_PARAM1
//import ie.wit.anglersguide.ARG_PARAM2
import ie.wit.anglersguide.R
import ie.wit.anglersguide.databinding.FragmentAddFishingSpotBinding
import ie.wit.anglersguide.helpers.showImagePicker
import ie.wit.anglersguide.models.FishingSpotModel
import ie.wit.anglersguide.models.Location
import ie.wit.anglersguide.ui.mapAll.MapAllFishingSpotsFragment
import timber.log.Timber.i
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream



class FishingSpotFragment : Fragment() {

    private var _fragBinding: FragmentAddFishingSpotBinding? = null
    private val fragBinding get() = _fragBinding!!
    var fishingSpot = FishingSpotModel()
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var cameraIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()
    private val fishingSpotListViewModel: FishingSpotListViewModel by activityViewModels()
    //private val args by navArgs<FishingSpotFragmentArgs>()
    private lateinit var fishingSpotViewModel: FishingSpotViewModel
    var edit = false
    val REQUEST_IMAGE_CAPTURE = 100


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_fishing_spot, container, false)
        // Inflate the layout for this fragment
        _fragBinding = FragmentAddFishingSpotBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        activity?.title = getString(R.string.action_addFishingSpot)
        setupMenu()
        fishingSpotViewModel = ViewModelProvider(this).get(FishingSpotViewModel::class.java)

        fishingSpotViewModel.observableFishingSpot.observe(viewLifecycleOwner, Observer {
                fishingspot -> fishingspot?.let { render(fishingspot) }
        })
        //Toast.makeText(context,"FishingSpot ID Selected : ${args.fishingspotId}",Toast.LENGTH_LONG).show()

        setButtonListener(fragBinding)
        return root
    }

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
                // Handle for example visibility of menu items
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_fishingspot, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Validate and handle the selected menu item
                return NavigationUI.onNavDestinationSelected(menuItem,
                    requireView().findNavController())
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            var takenImage = data?.data!!.toString()
            i("Taken image - $takenImage")


           // val bos = ByteArrayOutputStream()
            //takenImage.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos)
            //var pathImage: String
            //pathImage = MediaStore.Images.Media.insertImage(
            ///    getActivity()?.getApplicationContext()?.contentResolver,
            //    takenImage,
            //    "New Picture",
             //   null
           // )
//            val bitmapdata = bos.toByteArray()
//            val bs = ByteArrayInputStream(bitmapdata)
//
//            i("pathImage - $pathImage")
//            val x = Movie.decodeStream(bs)
//
//            i("TdecodeStream(bs) - $x")
//
//        } else {
//            super.onActivityResult(requestCode, resultCode, data)
       }
    }

    override fun onResume() {
        super.onResume()
        fishingSpotViewModel.load()
    }

    private fun render(fishingspot: FishingSpotModel) {
        fragBinding.fishingspotTitle.setText(fishingspot.title)
        fragBinding.description.setText(fishingspot.description)
        if (fishingspot.image != "") {
            Picasso.get()
                .load(fishingspot.image)
                .into(_fragBinding?.fishingspotImage)
        }
        fragBinding.fishingspotvm = fishingSpotViewModel
    }

    fun setButtonListener(layout: FragmentAddFishingSpotBinding) {
        layout.btnAdd.setOnClickListener {
            i("btnAdd pressed")
            fishingSpot.title = _fragBinding?.fishingspotTitle?.text.toString()
            fishingSpot.description = _fragBinding?.description?.text.toString()
            //fishingSpot.image = _fragBinding?.fishingspotImage.setImageURI(fishingSpot.image)
            if (!edit) {
                if (fishingSpot.title.isNotEmpty() && fishingSpot.description.isNotEmpty()) {
                    //fishingspots.create(fishingSpot.copy())
                    fishingSpotViewModel.addFishingSpot(loggedInViewModel.liveFirebaseUser,
                        FishingSpotModel(title = fishingSpot.title,
                            description = fishingSpot.description,
                            lat = 0.0, //fishingSpot.lat,
                            lng = 0.0, //fishingSpot.lng,
                            zoom = 0f, //fishingSpot.zoom,
                            email = loggedInViewModel.liveFirebaseUser.value?.email!! )
                    )
                    //activity?.setResult(AppCompatActivity.RESULT_OK)
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
                    fishingSpotViewModel.addFishingSpot(loggedInViewModel.liveFirebaseUser,
                        FishingSpotModel(title = fishingSpot.title,
                            description = fishingSpot.description,
                            lat = fishingSpot.lat,
                            lng = fishingSpot.lng,
                            zoom = fishingSpot.zoom,
                            email = loggedInViewModel.liveFirebaseUser.value?.email!! )
                    )
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
            val launcherIntent = Intent(this@FishingSpotFragment.context, MapAllFishingSpotsFragment::class.java)
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
                            fishingSpot.image = result.data!!.data!!.toString()
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
                            fishingSpot.image = result.data!!.data!!.toString()
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


