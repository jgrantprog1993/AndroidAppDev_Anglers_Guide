package ie.wit.anglersguide.ui.mapAll

//import ie.wit.anglersguide.models.FishingSpotManager.findAll
import LoggedInViewModel
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import ie.wit.anglersguide.R
import ie.wit.anglersguide.adaptors.FishingSpotAdapter
import ie.wit.anglersguide.databinding.FragmentFishingSpotListBinding
import ie.wit.anglersguide.databinding.FragmentMapAllFishingSpotsBinding
import ie.wit.anglersguide.firebase.FirebaseDBManager
import ie.wit.anglersguide.models.FishingSpotModel
import ie.wit.anglersguide.ui.activity.FishingSpotListViewModel
import timber.log.Timber.i
import timber.log.Timber

class MapAllFishingSpotsFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener{
    private lateinit var mMap : GoogleMap
    private var mapReady = false
    private lateinit var fishingspots: List<FishingSpotModel>

    private var _fragBinding: FragmentMapAllFishingSpotsBinding? = null
    private val fragBinding get() = _fragBinding!!

    private lateinit var fishingSpotMapAllViewModel: FishingSpotMapAllViewModel
//    private val callback = OnMapReadyCallback { googleMap ->
//        fishingSpotMapAllViewModel.map = googleMap
//        val loc = LatLng(52.245696, -7.1234)
//
//        fishingSpotMapAllViewModel.map.uiSettings.isZoomControlsEnabled = true
//        fishingSpotMapAllViewModel.map.uiSettings.isMyLocationButtonEnabled = true
//        fishingSpotMapAllViewModel.map.addMarker(MarkerOptions().position(loc).title("you are here !"))
//        fishingSpotMapAllViewModel.map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc,14f))
//
//    }
//    private val loggedInViewModel : LoggedInViewModel by activityViewModels()
//
//    private lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //var root = inflater.inflate(R.layout.fragment_map_all_fishing_spots, container, false)
//        _fragBinding = FragmentMapAllFishingSpotsBinding.inflate(inflater, container, false)
//        val root = fragBinding.root
//        setupMenu()
//
        fishingSpotMapAllViewModel = ViewModelProvider(this)[FishingSpotMapAllViewModel::class.java]
        fishingSpotMapAllViewModel.observableMapsSpots.observe(
            viewLifecycleOwner,
            Observer { fishingspots ->
                fishingspots?.let {
                    render(fishingspots as ArrayList<FishingSpotModel>)
                }
            })
//        return root mapAllMarkers
        var rootView =  inflater.inflate(R.layout.fragment_map_all_fishing_spots, container, false)
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapAllMarkers) as? SupportMapFragment
        mapFragment?.getMapAsync {
                googleMap ->
            mMap = googleMap
            mapReady = true
            onMapReady(mMap)
            updateMap()
        }
        return rootView

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        fishingSpotMapAllViewModel.fishingSpotList.observe(viewLifecycleOwner, Observer {
            fishingspots -> this.fishingspots = fishingspots
            updateMap()
        })
    }
    private fun render(fishingSpots: List<FishingSpotModel>) {
//            _fragBinding.mapFragment.adapter =
//            FishingSpotAdapter(fishingSpots as MutableList<FishingSpotModel>, this)
//        if (fishingSpots.isEmpty()) {
//            mapFragment.visibility = View.GONE
//            fragBinding.FishingSpotsNotFound.visibility = View.VISIBLE
//        } else {
//            fragBinding.recyclerView.visibility = View.VISIBLE
//            fragBinding.FishingSpotsNotFound.visibility = View.GONE
//        }
    }
       override fun onMapReady(googleMap: GoogleMap) {
//            mMap = googleMap!!
//            for (location in fishingspots) {
//                val loc = LatLng(location.lat, location.lng)
//                val locTitle = location.title
//                val locLat = location.lat
//                val locLng = location.lng
//                val options = MarkerOptions()
//                    .title("Name : $locTitle")
//                    .snippet("Lat, Lng : $locLat, $locLng")
//                    .draggable(true)
//                    .position(loc)
//                mMap.addMarker(options)
//                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, location.zoom))
//
//            }
      }

//        override fun onResume() {
//            super.onResume()
////            FishingSpotManager.findAll()
//            loggedInViewModel.liveFirebaseUser.observe(viewLifecycleOwner, Observer { firebaseUser ->
//                if (firebaseUser != null) {
//                    fishingSpotMapAllViewModel.liveFirebaseUser.value = firebaseUser
//                    fishingSpotMapAllViewModel.load()
//                }
//            })
//        }


    private fun updateMap() {
        if (mapReady && fishingspots != null) {
            fishingspots.forEach { fishingspot ->
                val loc = LatLng(fishingspot.lat, fishingspot.lng)
                val locTitle = fishingspot.title
                val locLat = fishingspot.lat
                val locLng = fishingspot.lng
                val marker = LatLng(fishingspot.lat, fishingspot.lng)
                mMap.addMarker(MarkerOptions().position(marker).title("Name : $locTitle").snippet("Lat, Lng : $locLat, $locLng"))
                mMap.uiSettings.isZoomControlsEnabled = true
                mMap.uiSettings.isMyLocationButtonEnabled = true

            }
        }
    }

//        override fun onDestroyView() {
//            super.onDestroyView()
//            _fragBinding = null
//        }

//       object {
//            var mapFragment : SupportMapFragment?=null
//
//            @JvmStatic
//            fun newInstance() =
//                MapAllFishingSpotsFragment().apply {
//                    arguments = Bundle().apply {
//
//                    }
//                }
//        }

    fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
                // Handle for example visibility of menu items
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_main, menu)

                val item = menu.findItem(R.id.toggleFishingSpots) as MenuItem
                item.setActionView(R.layout.togglebutton_layout)
                val toggleFishingSpots: SwitchCompat = item.actionView!!.findViewById(R.id.toggleButton)
                toggleFishingSpots.isChecked = false

                toggleFishingSpots.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) fishingSpotMapAllViewModel.loadAll()
                    else fishingSpotMapAllViewModel.load()
                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Validate and handle the selected menu item
                return NavigationUI.onNavDestinationSelected(
                    menuItem,
                    requireView().findNavController()
                )
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onMarkerClick(p0: Marker): Boolean {
        TODO("Not yet implemented")
    }
}
