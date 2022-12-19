package ie.wit.anglersguide.ui.mapAll

//import ie.wit.anglersguide.models.FishingSpotManager.findAll
import LoggedInViewModel
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import ie.wit.anglersguide.R
import ie.wit.anglersguide.databinding.FragmentMapAllFishingSpotsBinding
import ie.wit.anglersguide.models.FishingSpotModel
import timber.log.Timber

class MapAllFishingSpotsFragment : Fragment(), GoogleMap.OnMarkerClickListener{

    private var _fragBinding: FragmentMapAllFishingSpotsBinding? = null
    private val fragBinding get() = _fragBinding!!
    private val fishingSpotMapAllViewModel: FishingSpotMapAllViewModel by activityViewModels()
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()

    private lateinit var mMap : GoogleMap
    private var mapReady = false
    private lateinit var fishingspots: List<FishingSpotModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _fragBinding = FragmentMapAllFishingSpotsBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        setupMenu()

        val mapFragment = childFragmentManager.findFragmentById(R.id.mapAllMarkers) as? SupportMapFragment
        mapFragment?.getMapAsync {
                googleMap ->
            mMap = googleMap
            mapReady = true

        }
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        fishingSpotMapAllViewModel.observableMapsSpots.observe(viewLifecycleOwner, Observer {
            fishingspots -> this.fishingspots = fishingspots

            updateMap()
        })

    }


    override fun onResume() {
        super.onResume()
    //            FishingSpotManager.findAll()
        loggedInViewModel.liveFirebaseUser.observe(viewLifecycleOwner, Observer { firebaseUser ->
            if (firebaseUser != null) {
                fishingSpotMapAllViewModel.liveFirebaseUser.value = firebaseUser
                fishingSpotMapAllViewModel.load()
            }
        })
    }


    @SuppressLint("TimberArgCount", "BinaryOperationInTimber")
    private fun updateMap() {
        if (mapReady) {
            Timber.i("Got in here")
            mMap.clear()

            fishingspots.forEach { fishingspot ->
                Timber.i("Got in here%s", fishingspot)
                val loc = LatLng(fishingspot.lat, fishingspot.lng)
                val locTitle = fishingspot.title
                val locLat = fishingspot.lat
                val locLng = fishingspot.lng
                val marker = LatLng(fishingspot.lat, fishingspot.lng)
                Timber.i("1 =>%s", fishingSpotMapAllViewModel.liveFirebaseUser.value?.email.toString())
                Timber.i("2 =>"+ fishingspot.email)
                if(fishingSpotMapAllViewModel.liveFirebaseUser.value?.email.toString() == fishingspot.email)
                {
                    mMap.addMarker(MarkerOptions().position(marker).title("Name : $locTitle").snippet("Lat, Lng : $locLat, $locLng").icon(
                        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)))
                }
                else{
                mMap.addMarker(MarkerOptions().position(marker).title("Name : $locTitle").snippet("Lat, Lng : $locLat, $locLng").icon(
                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)))
                }
                mMap.uiSettings.isZoomControlsEnabled = true
                mMap.uiSettings.isMyLocationButtonEnabled = true

            }
        }
    }

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
                    Timber.i("toggleFishingSpots.isChecked!! -> ${toggleFishingSpots.isChecked!!}")
                    if (isChecked) {fishingSpotMapAllViewModel.loadAll()
                    }

                    else {fishingSpotMapAllViewModel.load()
                   }
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
