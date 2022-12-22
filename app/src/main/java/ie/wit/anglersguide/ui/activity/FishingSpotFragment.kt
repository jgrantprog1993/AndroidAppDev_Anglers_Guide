package ie.wit.anglersguide.ui.activity

//import ie.wit.anglersguide.ARG_PARAM1
//import ie.wit.anglersguide.ARG_PARAM2
import LoggedInViewModel
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import ie.wit.anglersguide.R
import ie.wit.anglersguide.databinding.FragmentAddFishingSpotBinding
import ie.wit.anglersguide.helpers.showImagePicker
import ie.wit.anglersguide.models.FishingSpotModel
import ie.wit.anglersguide.models.Location
import timber.log.Timber.i


class FishingSpotFragment : Fragment() {

    private var _fragBinding: FragmentAddFishingSpotBinding? = null
    private val fragBinding get() = _fragBinding!!
    var fishingSpot = FishingSpotModel()
    private lateinit var imageIntentLauncher: ActivityResultLauncher<Intent>
    private lateinit var cameraIntentLauncher: ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher: ActivityResultLauncher<Intent>
    private val loggedInViewModel: LoggedInViewModel by activityViewModels()
    private val fishingSpotListViewModel: FishingSpotListViewModel by activityViewModels()

    //private val args by navArgs<FishingSpotFragmentArgs>()
    private lateinit var fishingSpotViewModel: FishingSpotViewModel
    var edit = false
    val REQUEST_IMAGE_CAPTURE = 100

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    val PERMISSION_ID = 1010

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @SuppressLint("MissingPermission")
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

        fishingSpotViewModel.observableFishingSpot.observe(
            viewLifecycleOwner,
            Observer { fishingspot ->
                fishingspot?.let { render(fishingspot) }
            })
        //Toast.makeText(context,"FishingSpot ID Selected : ${args.fishingspotId}",Toast.LENGTH_LONG).show()
        setButtonListener(fragBinding)

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())

        fragBinding.fishingspotTitle.text.clear();
        fragBinding.description.text.clear();

        Log.d("Debug:", CheckPermission().toString())
        Log.d("Debug:", isLocationEnabled().toString())
        RequestPermission()
        getLastLocation()

//        fusedLocationClient.lastLocation
//            .addOnSuccessListener { location : android.location.Location? ->
//                if (location != null) {
//                    fragBinding.lat.text = location.latitude.toString()
//                }
//
//                if (location != null) {
//                    fragBinding.lng.text = location.longitude.toString()
//                }
//            }
        i("LOCATION LAT----> " + fragBinding.lat.text)
        i("LOCATION LNG----> " + fragBinding.lat.text)
        return root
    }


    @SuppressLint("MissingPermission")
    fun getLastLocation() {
        if (CheckPermission()) {
            if (isLocationEnabled()) {
                fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
                    var location: android.location.Location? = task.result

                        NewLocationData()

                        Log.d("Debug:", "Your Location:" + location?.latitude + "" + location?.longitude)
                        fragBinding.lat.text = location?.latitude.toString()
                        fragBinding.lng.text = location?.longitude.toString()

                }

        } else {
            Toast.makeText(
                this@FishingSpotFragment?.context,
                "Please Turn on Your device Location", Toast.LENGTH_SHORT).show()
        }
    }else {
        RequestPermission()
    }
}

    @SuppressLint("MissingPermission")
    fun NewLocationData(){
        var locationRequest =  LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 1
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        fusedLocationProviderClient!!.requestLocationUpdates(
            locationRequest,locationCallback, Looper.myLooper()
        )
    }

    private val locationCallback = object : LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult) {
            var lastLocation: android.location.Location? = locationResult.lastLocation
            Log.d("Debug:","your last last location: "+ lastLocation?.latitude.toString() + ""+ lastLocation?.longitude.toString())

        }
    }

    private fun CheckPermission():Boolean{
        //this function will return a boolean
        //true: if we have permission
        //false if not
        if(
            ActivityCompat.checkSelfPermission(requireContext(),android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(requireContext(),android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ){
            return true
        }

        return false

    }
    fun RequestPermission(){
        //this function will allows us to tell the user to requesut the necessary permsiion if they are not garented
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION,android.Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_ID
        )
    }

    fun isLocationEnabled():Boolean{
        //this function will return to us the state of the location service
        //if the gps or the network provider is enabled then it will return true otherwise it will return false
        var locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == PERMISSION_ID){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Log.d("Debug:","You have the Permission")
            }
        }
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
       // RequestPermission()
        getLastLocation()
        fragBinding.fishingspotTitle.text.clear();
        fragBinding.description.text.clear();
    }

    private fun render(fishingspot: FishingSpotModel) {
        fragBinding.fishingspotTitle.setText(fishingspot.title)
        fragBinding.description.setText(fishingspot.description)
        if (fishingspot.image != "") {
            Picasso.get()
                .load(fishingspot.image)
                .into(_fragBinding?.fishingspotImage)
        }

    }

    @SuppressLint("MissingPermission")
    fun setButtonListener(layout: FragmentAddFishingSpotBinding) {
        layout.btnAdd.setOnClickListener {
            i("btnAdd pressed")
            fishingSpot.title = _fragBinding?.fishingspotTitle?.text.toString()
            fishingSpot.description = _fragBinding?.description?.text.toString()
            //fishingSpot.image = _fragBinding?.fishingspotImage.setImageURI(fishingSpot.image)

            fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
                var location: android.location.Location? = task.result
                if (!edit) {
                    if (fishingSpot.title.isNotEmpty() && fishingSpot.description.isNotEmpty()) {
                        //fishingspots.create(fishingSpot.copy())
                        if (location != null) {
                            fishingSpotViewModel.addFishingSpot(
                                loggedInViewModel.liveFirebaseUser,
                                FishingSpotModel(
                                    title = fishingSpot.title,
                                    description = fishingSpot.description,
                                    lat = location.latitude, //fishingSpot.lat,
                                    lng = location.longitude, //fishingSpot.lng,
                                    zoom = 14f, //fishingSpot.zoom,
                                    email = loggedInViewModel.liveFirebaseUser.value?.email!!
                                )
                            )
                        }

                        Snackbar
                            .make(
                                it,
                                "Fishing Spot ${fishingSpot.title} Added",
                                Snackbar.LENGTH_LONG
                            )
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

                    layout.btnAdd.setOnClickListener {
                        fishingSpot.title = _fragBinding?.fishingspotTitle?.text.toString()
                        fishingSpot.description = _fragBinding?.description?.text.toString()
                        fishingSpotViewModel.addFishingSpot(
                            loggedInViewModel.liveFirebaseUser,
                            FishingSpotModel(
                                title = fishingSpot.title,
                                description = fishingSpot.description,
                                lat = location!!.latitude,
                                lng = location!!.longitude,
                                zoom = fishingSpot.zoom,
                                email = loggedInViewModel.liveFirebaseUser.value?.email!!
                            )
                        )
                        // val launcherIntent =
                        //   Intent(this@FishingSpotFragment.context, FishingspotActivity::class.java)
                        // startActivityForResult(launcherIntent, 0)
                        activity?.setResult(AppCompatActivity.RESULT_OK)
                        activity?.finish()
                    }
                }
            }
        }
        fragBinding?.chooseImage?.setOnClickListener {
            i("Select image")
            showImagePicker(imageIntentLauncher)
        }

//        _fragBinding?.takeImage?.setOnClickListener{
//            i("Take picture frag")
//
//            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//            try {
//
//                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
//            }catch(e: ActivityNotFoundException){
//                Toast.makeText(this@FishingSpotFragment?.context, "Error: " + e.localizedMessage, Toast.LENGTH_SHORT).show()
//            }
//        }

//        _fragBinding?.fishingspotLocation?.setOnClickListener {
//            val location = Location(52.149, -6.9896, )
//            i ("Set Location Pressed")
//
//            if (fishingSpot.zoom != 0f) {
//                location.lat =  fishingSpot.lat
//                location.lng = fishingSpot.lng
//
//            }
//            val launcherIntent = Intent(this@FishingSpotFragment.context, MapAllFishingSpotsFragment::class.java)
//                .putExtra("location", location)
//            mapIntentLauncher.launch(launcherIntent)
//        }
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

                        } // end of if
                    }
                    AppCompatActivity.RESULT_CANCELED -> { } else -> { }
                }
            }
    }
}



